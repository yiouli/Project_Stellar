package org.stellar.gameplat.service.webexchange;

import java.util.Date;
import java.util.Hashtable;

import org.stellar.gameplat.service.contract.data.ServiceResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ResponseGenerator {

	public static ServiceResponse serviceResponse(int status) {
		return serviceResponse(status, null);
	}
	
	public static ServiceResponse serviceResponse(int status, String message) {
		return serviceResponse(status, message, null);
	}
	
	public static ServiceResponse serviceResponse(int status, String message, String directTo) {
		if(message == null)
			return new ServiceResponse(status, jsonResponse(status < 300, directTo));
		return new ServiceResponse(status,
					jsonResponse(status < 300,
						new String[]{"message"}, new String[]{message}, directTo));
	}
	
	public static String jsonResponse(boolean success, String directTo) {
		JsonObject resJson = new JsonObject();
		resJson.addProperty("success", success);
		if(directTo != null)
			resJson.addProperty("directTo", directTo);
		return resJson.toString();
	}
	
	public static String jsonResponse(boolean success, String[] dataNames, Object[] data, String directTo) {
		if(dataNames == null || data == null || dataNames.length != data.length)
			throw new IllegalArgumentException("Data names or data is null or mismatching");
		JsonObject resJson = new JsonObject();
		resJson.addProperty("success", success);
		Gson gson = new Gson();
		for(int i = 0; i < data.length; i++)
			resJson.add(dataNames[i], gson.toJsonTree(data[i]));
		if(directTo != null)
			resJson.addProperty("directTo", directTo);
		return resJson.toString();
	}
	
	//----test stub----------------------------------------------
	
	public static void main(String[] args) {
		System.out.println(jsonResponse(false, "error page url"));
		System.out.println(jsonResponse(true, 
				new String[]{"username"}, 
				new String[]{"user1"}, null));
		Hashtable<String, Date> table = new Hashtable<String, Date>();
		table.put("date1", new Date());
		table.put("date2", new Date(1));
		System.out.println(jsonResponse(false,
				new String[]{"message", "data", "table"},
				new Object[]{"message from service", new int[4], table},
				"direct url"));
	}
}
