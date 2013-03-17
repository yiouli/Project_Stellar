package org.stellar.gameplat.service.contract;

import java.util.Hashtable;

public class ServiceResponse {

	public int status;
	public String body;
	public Hashtable<String, String> headers;
	
	public ServiceResponse(int status, String body) {
		this.status = status;
		this.body = body;
		headers = new Hashtable<String, String>();
	}
}
