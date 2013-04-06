package org.stellar.gameplat.service.hub;

import java.io.IOException;
import java.util.Hashtable;

import org.stellar.gameplat.service.ServiceSetting;
import org.stellar.gameplat.service.contract.IGameService;
import org.stellar.gameplat.service.contract.ILobbyService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;
import org.stellar.gameplat.service.userinfostore.UserInfoClient;
import org.stellar.gameplat.service.webexchange.ResponseGenerator;

/**
 * LobbyService should be created by Arranger or other programmatic clients,
 * instead of from ServiceLoader, unless the requirement needs singleton LobbyService.
 * Mapping should be added to UrlMapper by client after service is created.
 * @author leoSU
 *
 */
public class LobbyService implements ILobbyService {

	private ServiceSetting setting = ServiceSetting.instance();
	private UserInfoClient userInfoClient;
	private Lobby<String> lobby;
	
	public LobbyService() {
		userInfoClient = new UserInfoClient();
	}

	public void init(IGameService game, String[] player) {
		if(isInitialized())
			throw new IllegalStateException();
		lobby = new Lobby<String>(game, player);
	}
	
	public void init(IGameService game, String[] player, int seatCount) {
		if(isInitialized())
			throw new IllegalStateException();
		lobby = new Lobby<String>(game, player, seatCount);
	}

	public void init(IGameService game, String[] player, int seatCount, int capacity) {
		if(isInitialized())
			throw new IllegalStateException();
		lobby = new Lobby<String>(game, player, seatCount, capacity);
	}
	
	public boolean isInitialized() {
		return lobby != null;
	}
	
	private ServiceResponse authenticationFail() {
		return ResponseGenerator.serviceResponse(401, 
				userInfoClient.getLastMessage(), setting.getUserInfoViewUrl());
	}
	
	@Override
	public ServiceResponse handleRequest(String url, String method,
			String reqBody, Hashtable<String, String> params) {
		assert isInitialized();
		return null;
	}

	@Override
	public String host() {
		assert isInitialized();
		return lobby.getPlayer(lobby.getHost());
	}

	@Override
	public String[] participants() {
		assert isInitialized();
		return lobby.getPlayers();
	}

	@Override
	public ServiceResponse changeHost(String username, String password, int newHost) {
		assert isInitialized();
		try {
			if(!userInfoClient.authenticate(username, password))
				return authenticationFail();
			if(username != host())
				return ResponseGenerator.serviceResponse(401, "not host");
			lobby.changeHost(newHost);
			return ResponseGenerator.serviceResponse(200, host());
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseGenerator.serviceResponse(500, e.getMessage());
		}
	}
	
	@Override
	public boolean isReady() {
		assert isInitialized();
		return lobby.game.isReady();
	}

	@Override
	public ServiceResponse startGame(String username, String password) {
		try {
			if(!userInfoClient.authenticate(username, password))
				return authenticationFail();
			if(!isReady())
				return ResponseGenerator.serviceResponse(409, "Game state not ready to start");
			return null;
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseGenerator.serviceResponse(500, e.getMessage());
		}
	}

	@Override
	public ServiceResponse changeSetting(String username, String password,
			String gameParams) {
		try {
			if(!userInfoClient.authenticate(username, password))
				return authenticationFail();
			return null;
		}
		catch(Exception ex) {
			return ResponseGenerator.serviceResponse(500, ex.getMessage());
		}
	}


}
