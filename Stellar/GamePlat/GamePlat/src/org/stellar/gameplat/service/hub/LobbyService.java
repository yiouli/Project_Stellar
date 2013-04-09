package org.stellar.gameplat.service.hub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.stellar.gameplat.service.ServiceLoader;
import org.stellar.gameplat.service.ServiceSetting;
import org.stellar.gameplat.service.contract.IGameService;
import org.stellar.gameplat.service.contract.ILobbyService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;
import org.stellar.gameplat.service.userinfostore.UserInfoClient;
import org.stellar.gameplat.service.web.ServerBuilder;
import org.stellar.gameplat.service.webexchange.RequestInterpreter;
import org.stellar.gameplat.service.webexchange.RequestMethod;
import org.stellar.gameplat.service.webexchange.ResponseGenerator;
import org.yiouli.datastructure.array.SmartArray;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

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
	
	private ServiceResponse createPropertyResponse(String name, Object value) {
		ServiceResponse res = ResponseGenerator.serviceResponse(200, null);
		res.body = ResponseGenerator.jsonResponse(true, new String[]{name}, 
						new Object[]{value}, null);
		return res;
	}
	
	private ServiceResponse handlePropertyRequest(String lobbyId, 
				RequestMethod method, String property, String reqBody) {
		int lId = Integer.parseInt(lobbyId);
		if(method == RequestMethod.Post 
				&& property.equalsIgnoreCase("game")) {
			Gson gson = new Gson();
			Setting setting = gson.fromJson(reqBody, Setting.class);
			return startGame(lId, setting.username, setting.password);
		}
		else if(method != RequestMethod.Get)
			return ResponseGenerator.serviceResponse(404, "Method not supported, try GET");
		else if(property.equalsIgnoreCase("host"))
			return createPropertyResponse("host", host(lId));
		else if(property.equalsIgnoreCase("participant")
				|| property.equalsIgnoreCase("participants")
				|| property.equalsIgnoreCase("people"))
			return createPropertyResponse("participant", participants(lId));
		else if(property.equalsIgnoreCase("status")
				|| property.equalsIgnoreCase("ready"))
			return createPropertyResponse("ready", isReady(lId));
		else
			return ResponseGenerator.serviceResponse(404, "Unkown property of lobby");
	}
	
	private ServiceResponse handleOperationRequest(String lobbyId,
				RequestMethod method, String reqBody) {
		int lId = Integer.parseInt(lobbyId);
		//getLobbyById
		if(method == RequestMethod.Get)
			return lobby(lId);
		Gson gson = new Gson();
		Setting setting = gson.fromJson(reqBody, Setting.class);
		if(setting.host != -1)
			return changeHost(lId, setting.username, setting.password, setting.host);
		else if(setting.gameParams != null)
			return changeGameParams(lId, setting.username, setting.password, setting.gameParams);
		return ResponseGenerator.serviceResponse(404, "No method can handle the request");
	}
	
	@Override
	public ServiceResponse handleRequest(String url, String method,
			String reqBody, Hashtable<String, String> params) {
		assert isInitialized();
		assert url != null && method != null && params != null;
		try {
			RequestMethod reqMethod = RequestInterpreter.getRequestMethod(method);
			String lobbyId = params.get("lobbyId");
			String property = params.get("property");
			//get property, start game

			if(lobbyId != null && property != null)
				return handlePropertyRequest(lobbyId, reqMethod, property, reqBody);
			//getLobbyById, changeHost, changeGameParams
			else if(lobbyId != null)
				return handleOperationRequest(lobbyId, reqMethod, reqBody);
			//getLobbyIds
			else {
				ServiceResponse res = ResponseGenerator.serviceResponse(200, null);
				int[] lobbyIds = getLobbyIds();
				Integer[] idObjs = new Integer[lobbyIds.length];
				for(int i = 0; i < lobbyIds.length; i++)
					idObjs[i] = lobbyIds[i];
				res.body = ResponseGenerator.jsonResponse(true, new String[]{"lobby"}, idObjs, null);
				return res;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return ResponseGenerator.serviceResponse(500, ex.getMessage());
		}
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
		ArrayList<String> players = lobby.getPlayers();
		return players.toArray(new String[players.size()]);
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
		if(!lobbies.has(lobbyId))
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
	public ServiceResponse changeGameParams(int lobbyId, String username, String password,
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
		if(!lobbies.has(lobbyId))
			return ResponseGenerator.serviceResponse(404, "lobby not found");
		return ResponseGenerator.serviceResponse(404, "Not implemented");
	}

	private static class Setting {
		String username;
		String password;
		int host = -1;
		String gameParams;
	}
	
	//----test stub---------------------------------------------------------------------
	
	private static class MockGame implements IGameService {

		@Override
		public ServiceResponse handleRequest(String url, String method,
				String reqBody, Hashtable<String, String> params) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isReady() {
			System.out.println("Game is ready!");
			return true;
		}

		@Override
		public void start(String gameParams) {
			System.out.println("Game started!");
		}

		@Override
		public String getGameView() {
			return "test game view";
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		ServerBuilder builder = new ServerBuilder("testInput/config.json", "testInput/config.web.json");
		HttpServer server = builder.createServer(8000, "/gameplatform");
		LobbyService service = new LobbyService();
		service.createLobby(new MockGame(), new String[]{"user1", "user2"}, 4, 8);
		ServiceLoader.instance().addServiceInstance(service);
		server.start();
		System.out.println("Server started...");
	}
}
