package org.stellar.gameplat.service.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.stellar.gameplat.service.webexchange.RequestInterpreter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UrlMapper {

	private Hashtable<String, String> services;
	private Hashtable<String, ArrayList<UrlMapping>> serviceMappings;
	private LinkedHashMap<String, UrlMapping> mappings;
	
	private static UrlMapper instance = null;
	
	private void getMappings(JsonElement config) {
		Gson gson = new Gson();
		JsonArray mapping = config.getAsJsonObject().getAsJsonArray("mapping");
		mappings = new LinkedHashMap<String, UrlMapping>(mapping.size());
		serviceMappings = new Hashtable<String, ArrayList<UrlMapping>>();
		for(JsonElement elem : mapping) {
			UrlMapping m = gson.fromJson(elem, UrlMapping.class);
			if(m.name == null) {
				System.err.println("Invalid Url Mapping config");
				System.err.println("Mapping must have name property");
				continue;
			}
			if(m.pattern != null)
				m.urlPattern = new UrlPattern(m.pattern);
			mappings.put(m.name, m);
			if(!serviceMappings.containsKey(m.service))
				serviceMappings.put(m.service, new ArrayList<UrlMapping>());
			serviceMappings.get(m.service).add(m);
		}
	}
	
	private void getServices(JsonElement config) {
		JsonArray service = config.getAsJsonObject().getAsJsonArray("service");
		services = new Hashtable<String, String>();
		for(JsonElement elem : service) {
			JsonObject serv = elem.getAsJsonObject();
			String name = serv.get("name").getAsString();
			String className = serv.get("className").getAsString();
			services.put(name, className);
		}
	}
	
	private UrlMapper(String configPath) throws IOException {
		File f = new File(configPath);
		if(!f.exists() || !f.isFile());
		BufferedReader reader = new BufferedReader(new FileReader(f));
		JsonElement config = new JsonParser().parse(reader);
		reader.close();
		getServices(config);
		getMappings(config);
	}
	
	public static void init(String configPath) throws IOException {
		if (instance != null)
			throw new IllegalStateException("already initalized");
		instance = new UrlMapper(configPath);
	}
	
	public static UrlMapper instance() {
		if(instance == null)
			throw new IllegalStateException("Not initialized, call init(configPath) first");
		return instance;
	}
	
	public String getServiceClassName(String serviceName) {
		return services.get(serviceName);
	}
	
	private boolean matches(UrlMapping m, String method, String url) {
		url = RequestInterpreter.removeQueryString(url);
		return (m.method == null || m.method.equalsIgnoreCase(method)) 
				&& (m.urlPattern == null || m.urlPattern.matches(url))
				&& (m.exclude == null || !url.matches(m.exclude));
	}
	
	//return first match, according to the order in config file
	private UrlMapping getMapping(String method, String url) {
		for(String key : mappings.keySet()) {
			UrlMapping m = mappings.get(key);
			if(matches(m, method, url))
				return m;
		}
		return null;
	}
	
	public String getServiceClassName(String method, String url) {
		UrlMapping mapping = getMapping(method, url);
		if(mapping != null)
			return getServiceClassName(mapping.service);
		System.err.println("Service not found : ");
		System.err.println("\tmethod = " + method);
		System.err.println("\turl = " + url);
		return null;
	}

	public String[] getMappingNames(String serviceName) {
		ArrayList<UrlMapping> ms = serviceMappings.get(serviceName);
		if(ms == null)
			return null;
		String[] ret = new String[ms.size()];
		int i = 0;
		for(UrlMapping m : ms)
			ret[i++] = m.name;
		return ret;
	}

	public String getUrl(String mappingName, String[] params) {
		UrlMapping m = mappings.get(mappingName);
		if(m == null)
			return null;
		return m.urlPattern.getUrl(params);
	}

	//get variables for first matching mapping
	public Hashtable<String, String> getParameters(String url, String method) {
		UrlMapping m = getMapping(method, url);
		if(m == null)
			return new Hashtable<String, String>();
		return getParameters(m.name, url, method);
	}
	
	public Hashtable<String, String> getParameters(String mappingName, String url, String method) {
		UrlMapping m = mappings.get(mappingName);
		if(m == null)
			return new Hashtable<String, String>();
		if(!matches(m, method, url))
			throw new IllegalArgumentException("Method or url not matching with mappingName");
		return m.urlPattern.getParameters(url);
	}
	
	//----sub classes-----------------------------------------------------------------
	
	public static class UrlMapping {
		
		public String name;
		public String method;
		public String pattern;
		public String exclude;
		public String service;
		public UrlPattern urlPattern; 
	}
	
	static class UrlPattern {
		
		private static final String VAR_REGEX = "#\\{(.+)\\}";
		private static final int VAR_GROUP = 1;
		
		final String pattern;
		private String urlRegex;
		String[] text;
		String[] parameter;
		Hashtable<String, Integer> paramIndex;
		
		UrlPattern(String pattern) {
			this.pattern = pattern;
			paramIndex = new Hashtable<String, Integer>();
			parse();
		}
		
		private void parse() {
			assert pattern != null;
			assert paramIndex != null;
			text = pattern.split(VAR_REGEX);
			Pattern p = Pattern.compile(VAR_REGEX);
			Matcher matcher = p.matcher(pattern);
			int idx = 0;
			while(matcher.find())
				paramIndex.put(matcher.group(VAR_GROUP), idx++);
			parameter = new String[idx];
			for(String var : paramIndex.keySet())
				parameter[paramIndex.get(var)] = var;
			String[] placeholder = new String[parameter.length];
			for(int i = 0; i < parameter.length; i++)
				placeholder[i] = "(.+)";
			urlRegex = getUrl(placeholder);
		}
		
		String getUrl(String[] params) {
			if(params == null)
				throw new NullPointerException();
			if(params.length != parameter.length)
				throw new IllegalArgumentException("number of variable expected "
						+parameter.length+" but "+params.length);
			StringBuffer sb = new StringBuffer();
			int ti = 0, pi = 0;
			while(ti < text.length || pi < params.length) {
				if(ti < text.length)
					sb.append(text[ti++]);
				if(pi < params.length)
					sb.append(params[pi++]);
			}
			return sb.toString();
		}
		
		boolean matches(String url) {
			url = RequestInterpreter.removeQueryString(url);
			return url.matches(urlRegex);
		}
		
		Hashtable<String, String> getParameters(String url) {
			url = RequestInterpreter.removeQueryString(url);
			assert matches(url);
			Hashtable<String, String> ret = new Hashtable<String, String>();
			Pattern p = Pattern.compile(urlRegex);
			Matcher matcher = p.matcher(url);
			matcher.find();
			assert matcher.groupCount() == parameter.length;
			for(int i = 0; i < parameter.length; i++)
				ret.put(parameter[i], matcher.group(i+1));
			return ret;
		}
	}
	
	//----test stub---------------------------------------------------------------------
	
	private static void findService(UrlMapper mapper, String method, String url) {
		System.out.println("Find service class : ");
		System.out.println("\tmethod = " + method);
		System.out.println("\turl = " + url);
		String className = mapper.getServiceClassName(method, url);
		System.out.println("\tclass name = " + className);
	}
	
	public static void main(String[] args) throws IOException {
		UrlMapper.init("testInput/config.web.json");
		UrlMapper mapper = UrlMapper.instance();
		findService(mapper, "Get", "/gameplatform/user/help");
		findService(mapper, "Post", "/gameplatform/user/help");
		findService(mapper, "Get", "/gameplatform/user/user1");
		findService(mapper, "Get", "otherservice/user1");
	}
}
