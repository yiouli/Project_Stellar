package org.stellar.gameplat.service.hub;

import java.util.Hashtable;

import org.stellar.gameplat.service.contract.IReceptionService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;

import com.sun.net.httpserver.HttpExchange;

public class ReceptionService implements IReceptionService {

	@Override
	public ServiceResponse handleRequest(HttpExchange httpExchange, String url,
			 String method, String reqBody, Hashtable<String, String> params) {
		String ticket = params.get("ticket");
		return null;
	}

	@Override
	public boolean accept(HttpExchange httpExchange, String gameId, String gameParams) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accept(HttpExchange httpExchange, String ticket) {
		// TODO Auto-generated method stub
		return false;
	}
}
