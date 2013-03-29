package org.stellar.gameplat.service.webexchange;

import java.util.Hashtable;

public class RequestInterpreter {

	public static final String DELIMITER = "/";
	
	public static RequestMethod getRequestMethod(String method) {
		if(method.equalsIgnoreCase("Get"))
			return RequestMethod.Get;
		if(method.equalsIgnoreCase("Set"))
			return RequestMethod.Set;
		if(method.equalsIgnoreCase("Post"))
			return RequestMethod.Post;
		if(method.equalsIgnoreCase("Delete"))
			return RequestMethod.Delete;
		throw new IllegalArgumentException("Invalid request method");
	}
	
	public static Hashtable<String, String> getQueryValues(String url) {
		int qIdx = url.indexOf('?');
		Hashtable<String, String> ret = new Hashtable<String, String>();
		if(qIdx == -1)
			return ret;
		String queryStr = url.substring(qIdx+1);
		String[] tokens = queryStr.split("&");
		for(String token : tokens) {
			String[] kv = token.split("=");
			if(kv.length != 2)
				continue;
			ret.put(kv[0], kv[1]);
		}
		return ret;
	}
	
	public static String[] tokenize(String url) {
		int qIdx = url.indexOf('?');
		if(qIdx != -1)
			url = url.substring(0, qIdx);
		if(url.startsWith(DELIMITER))
			url = url.substring(1);
		if(url.endsWith(DELIMITER))
			url = url.substring(0, url.length()-1);
		if(url.equals(""))
			return new String[0];
		return url.split(DELIMITER);
	}
	
	//----test stub-----------------------------------------------
	
	public static void main(String[] args) {
		String url = "/gameplat/user/user1?password=pw&userinfo=hello";
		String[] tokens = RequestInterpreter.tokenize(url);
		for(String token : tokens)
			System.out.print(token + " ");
		System.out.println();
		Hashtable<String, String> qs = RequestInterpreter.getQueryValues(url);
		for(String key : qs.keySet())
			System.out.println(key + " : " + qs.get(key));
	}
}
