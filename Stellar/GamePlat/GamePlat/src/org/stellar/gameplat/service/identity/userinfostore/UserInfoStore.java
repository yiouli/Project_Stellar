package org.stellar.gameplat.service.identity.userinfostore;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;

import org.stellar.gameplat.service.ServiceSetting;
import org.stellar.gameplat.service.contract.IUserInfoService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;
import org.stellar.gameplat.service.contract.data.UserInfo;
import org.stellar.gameplat.service.webexchange.RequestInterpreter;
import org.stellar.gameplat.service.webexchange.RequestMethod;
import org.stellar.gameplat.service.webexchange.ResponseGenerator;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

public class UserInfoStore implements IUserInfoService {

	private ServiceSetting setting;
	private final FileRepository repo;
	private Hashtable<String, UserInfo> userInfoTable;
	private Gson gson;
	private boolean initialized;
	
	public UserInfoStore() throws IOException {
		setting = ServiceSetting.instance();
		repo = new FileRepository(setting.getRepoDir());
		userInfoTable = new Hashtable<String, UserInfo>();
		gson = new Gson();
		init();
	}
	
	private void init() throws IOException {
		if(initialized)
			return;
		assert setting != null && userInfoTable != null && gson != null && repo != null;
		HashSet<String> usernames = repo.getKeys();
		for(String username : usernames) {
			String json = repo.get(username);
			userInfoTable.put(username, gson.fromJson(json, UserInfo.class));
		}
		initialized = true;
	}
	
	@Override
	public ServiceResponse addUserInfo(UserInfo info) {
		assert gson != null;
		try {
			if(userInfoTable.containsKey(info.username))
				return ResponseGenerator.serviceResponse(409, "User already exists");
			repo.put(info.username, gson.toJson(info));
			userInfoTable.put(info.username, info);
			return ResponseGenerator.serviceResponse(200, null, setting.getLobbyViewUrl());
		} catch (Exception ex) {
			return ResponseGenerator.serviceResponse(500, ex.getMessage());
		}
	}

	@Override
	public ServiceResponse getUserInfo(String username, String password) {
		assert gson != null;
		if(!userInfoTable.containsKey(username))
			return ResponseGenerator.serviceResponse(409, 
						"User doesn't exist", setting.getUserInfoViewUrl());
		UserInfo info = userInfoTable.get(username);
		if(info.password != null && !info.password.equals(password))
			return ResponseGenerator.serviceResponse(401, 
					"Wrong password", setting.getUserInfoViewUrl());
		return new ServiceResponse(200,
					ResponseGenerator.jsonResponse(true,
						new String[]{"userInfo"}, 
						new UserInfo[]{info}, 
						ServiceSetting.instance().getLobbyViewUrl()));
	}

	@Override
	public ServiceResponse setUserInfo(String password, UserInfo info) {
		assert gson != null;
		try {
			if(!userInfoTable.containsKey(info.username))
				return ResponseGenerator.serviceResponse(409, 
							"User doesn't exist", setting.getUserInfoViewUrl());
			UserInfo storedInfo = userInfoTable.get(info.username);
			if(storedInfo.password != null && !storedInfo.password.equals(password))
				return ResponseGenerator.serviceResponse(401, 
						"Wrong password", setting.getUserInfoViewUrl());
			repo.put(info.username, gson.toJson(info));
			userInfoTable.put(info.username, info);
			return ResponseGenerator.serviceResponse(200, null, setting.getLobbyViewUrl());
		} catch (Exception ex) {
			return ResponseGenerator.serviceResponse(500, ex.getMessage());
		}
	}

	@Override
	public ServiceResponse handleRequest(HttpExchange httpExchange, String url, 
			String method, String reqBody, Hashtable<String, String> params) {
		try {
			UserInfo info = gson.fromJson(reqBody, UserInfo.class);
			if(info == null)
				info = new UserInfo();
			Hashtable<String, String> qv = RequestInterpreter.getQueryValues(url);
			RequestMethod reqMethod = RequestInterpreter.getRequestMethod(method);
			info.username = params.get("username");
			switch(reqMethod) {
				case Get: return getUserInfo(info.username, qv.get("password"));
				case Set: return setUserInfo(qv.get("password"), info);
				case Post: return addUserInfo(info);
				default: return ResponseGenerator.serviceResponse(404, "Method not supported");
			}
		} catch(Exception ex) {
			return ResponseGenerator.serviceResponse(500, ex.getMessage());
		}
	}
}
