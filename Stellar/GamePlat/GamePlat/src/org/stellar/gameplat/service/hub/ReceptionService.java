package org.stellar.gameplat.service.hub;

import java.util.Hashtable;

import org.stellar.gameplat.service.contract.IReceptionService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;

public class ReceptionService implements IReceptionService {
	
	private boolean checkCredential(String username, String password) {
		return false;
	}
	
	@Override
	public ServiceResponse handleRequest(String url, String method,
			String reqBody, Hashtable<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean accept(String username, String password, String gameId, String gameParams) {
		if (!checkCredential(username, password))
			return false;
		// TODO Auto-generated method stub
		return false;
	}
}