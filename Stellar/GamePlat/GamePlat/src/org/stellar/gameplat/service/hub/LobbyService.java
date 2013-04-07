package org.stellar.gameplat.service.hub;

import java.io.IOException;
import java.util.Hashtable;

import org.stellar.gameplat.service.ServiceSetting;
import org.stellar.gameplat.service.contract.IGameService;
import org.stellar.gameplat.service.contract.ILobbyService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;
import org.stellar.gameplat.service.userinfostore.UserInfoClient;
import org.stellar.gameplat.service.webexchange.ResponseGenerator;
import org.yiouli.datastructure.array.SmartArray;

/**
 * LobbyService should be created by Arranger or other programmatic clients,
 * instead of from ServiceLoader, unless the requirement needs singleton LobbyService.
 * Mapping should be added to UrlMapper by client after service is created.
 * @author leoSU
 *
 */
public class LobbyService implements ILobbyService {

	public static final int LOBBY_CAPACITY = 200;
	
	private ServiceSetting setting = ServiceSetting.instance();
	private UserInfoClient userInfoClient;
	private SmartArray<Lobby<String>> lobbies;
	
	public LobbyService() {
		userInfoClient = new UserInfoClient();
		lobbies = new SmartArray<Lobby<String>>(LOBBY_CAPACITY);
	}

	int createLobby(IGameService game, String[] player) {
		return lobbies.add(new Lobby<String>(game, player));
	}
	
	int createLobby(IGameService game, String[] player, int seatCount) {
		return lobbies.add(new Lobby<String>(game, player, seatCount));
	}

	int createLobby(IGameService game, String[] player, int seatCount, int capacity) {
		return lobbies.add(new Lobby<String>(game, player, seatCount, capacity));
	}
	
	private ServiceResponse authenticationFail() {
		return ResponseGenerator.serviceResponse(401, 
				userInfoClient.getLastMessage(), setting.getUserInfoViewUrl());
	}
	
	private boolean isInitialized() {
		return userInfoClient != null && lobbies != null;
	}
	
	@Override
	public ServiceResponse handleRequest(String url, String method,
			String reqBody, Hashtable<String, String> params) {
		assert isInitialized();
		return null;
	}

	@Override
	public String host(int lobbyId) {
		assert isInitialized();
		if(!lobbies.has(lobbyId))
			return null;
		Lobby<String> lobby = lobbies.get(lobbyId);
		return lobby.getPlayer(lobby.getHost());
	}

	@Override
	public String[] participants(int lobbyId) {
		assert isInitialized();
		if(!lobbies.has(lobbyId))
			return null;
		Lobby<String> lobby = lobbies.get(lobbyId);
		return lobby.getPlayers();
	}

	@Override
	public ServiceResponse changeHost(int lobbyId, String username, String password, int newHost) {
		assert isInitialized();
		try {
			if(!userInfoClient.authenticate(username, password))
				return authenticationFail();
			if(!lobbies.has(lobbyId))
				return ResponseGenerator.serviceResponse(404, "lobby not found");
			if(username != host(lobbyId))
				return ResponseGenerator.serviceResponse(401, "not host");
			Lobby<String> lobby = lobbies.get(lobbyId);
			lobby.changeHost(newHost);
			ServiceResponse res = ResponseGenerator.serviceResponse(200, null);
			res.body = ResponseGenerator.jsonResponse(true, 
						new String[]{"host"}, new Object[]{lobby.getHost()}, null);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseGenerator.serviceResponse(500, e.getMessage());
		}
	}
	
	@Override
	public boolean isReady(int lobbyId) {
		assert isInitialized();
		if(lobbies.has(lobbyId))
			return false;
		return lobbies.get(lobbyId).game.isReady();
	}

	@Override
	public ServiceResponse startGame(int lobbyId, String username, String password) {
		try {
			if(!userInfoClient.authenticate(username, password))
				return authenticationFail();
			if(lobbies.has(lobbyId))
				return ResponseGenerator.serviceResponse(404, "lobby not found");;
			if(!isReady(lobbyId))
				return ResponseGenerator.serviceResponse(409, "Game not ready to start");
			if(username != host(lobbyId))
				return ResponseGenerator.serviceResponse(401, "not host");
			Lobby<String> lobby = lobbies.get(lobbyId);
			lobby.game.start(lobby.gameParams);
			lobbies.delete(lobbyId);
			ServiceResponse res = ResponseGenerator.serviceResponse(200, null);
			res.body = ResponseGenerator.jsonResponse(true, lobby.game.getGameView());
			return res;
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseGenerator.serviceResponse(500, e.getMessage());
		}
	}

	@Override
	public ServiceResponse changeSetting(int lobbyId, String username, String password,
			String gameParams) {
		try {
			if(!userInfoClient.authenticate(username, password))
				return authenticationFail();
			if(lobbies.has(lobbyId))
				return ResponseGenerator.serviceResponse(404, "lobby not found");
			if(username != host(lobbyId))
				return ResponseGenerator.serviceResponse(401, "not host");
			Lobby<String> lobby = lobbies.get(lobbyId);
			lobby.gameParams = gameParams;
			ServiceResponse res = ResponseGenerator.serviceResponse(200, null);
			res.body = ResponseGenerator.jsonResponse(true, 
						new String[]{"setting"}, new Object[]{lobby.gameParams}, null);
			return res;
		}
		catch(Exception ex) {
			return ResponseGenerator.serviceResponse(500, ex.getMessage());
		}
	}

	@Override
	public int[] getLobbyIds() {
		return lobbies.indices();
	}

	@Override
	public ServiceResponse lobby(int lobbyId) {
		if(lobbies.has(lobbyId))
			return ResponseGenerator.serviceResponse(404, "lobby not found");
		return ResponseGenerator.serviceResponse(404, "Not implemented");
	}


}
