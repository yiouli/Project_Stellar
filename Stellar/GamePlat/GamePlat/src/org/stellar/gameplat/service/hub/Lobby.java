package org.stellar.gameplat.service.hub;

import java.util.Hashtable;

import org.stellar.gameplat.service.contract.ILobbyService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;

public class Lobby implements ILobbyService {

	@Override
	public ServiceResponse handleRequest(String url, String method,
			String reqBody, Hashtable<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String host() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] participants() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String startGame(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changeSetting(String username, String password,
			String gameParams) {
		// TODO Auto-generated method stub
		return false;
	}

}
