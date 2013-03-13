package org.stellar.gameplat.service.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class UrlMapper {
	
	public static class UrlMapping {
		
		public String method;
		public String url;
		public String exclude;
		public String className;
	}

	private ArrayList<UrlMapping> mappings;
	
	private static UrlMapper instance = null;
	
	private UrlMapper(String configPath) throws IOException {
		File f = new File(configPath);
		if(!f.exists() || !f.isFile());
		BufferedReader reader = new BufferedReader(new FileReader(f));
		JsonElement config = new JsonParser().parse(reader);
		reader.close();
		Gson gson = new Gson();
		JsonArray mapping = config.getAsJsonObject().getAsJsonArray("mapping");
		mappings = new ArrayList<UrlMapping>(mapping.size());
		for(JsonElement elem : mapping)
			mappings.add(gson.fromJson(elem, UrlMapping.class));
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
	
	/**
	 * return first match
	 * @param method
	 * @param url
	 * @return
	 */
	public String getServiceClassName(String method, String url) {
		for(UrlMapping m : mappings) {
			if((m.method == null || m.method.equalsIgnoreCase(method)) 
				&& (m.url == null || url.matches(m.url))
				&& (m.exclude == null || !url.matches(m.exclude)))
				return m.className;
		}
		System.err.println("Service not found : ");
		System.err.println("\tmethod = " + method);
		System.err.println("\turl = " + url);
		return null;
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
		findService(mapper, "Get", "user/help");
		findService(mapper, "Post", "user/help");
		findService(mapper, "Get", "user/user1");
		findService(mapper, "Get", "otherservice/user1");
	}
}
