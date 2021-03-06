package org.stellar.gameplat.service.contract.data;

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
	
	public boolean success() {
		return status >= 200 && status <= 300;
	}
}
