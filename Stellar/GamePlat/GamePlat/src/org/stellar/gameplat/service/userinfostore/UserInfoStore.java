package org.stellar.gameplat.service.userinfostore;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;

import org.stellar.gameplat.service.ServiceSetting;
import org.stellar.gameplat.service.contract.IUserInfoService;
import org.stellar.gameplat.service.web.ResponseGenerator;

import com.google.gson.Gson;

public class UserInfoStore implements IUserInfoService {

	private final FileRepository repo;
	private Hashtable<String, UserInfo> userInfoTable;
	private Gson gson;
	private boolean initialized;
	
	public UserInfoStore() throws IOException {
		repo = new FileRepository(ServiceSetting.instance().getRepoDir());
		userInfoTable = new Hashtable<String, UserInfo>();
		gson = new Gson();
		init();
	}
	
	private void init() throws IOException {
		if(initialized)
			return;
		assert userInfoTable != null && gson != null && repo != null;
		HashSet<String> usernames = repo.getKeys();
		for(String username : usernames) {
			String json = repo.get(username);
			userInfoTable.put(username, gson.fromJson(json, UserInfo.class));
		}
		initialized = true;
	}
	
	@Override
	public String addUserInfo(String userInfoJson) {
		assert gson != null;
		try {
			UserInfo info = gson.fromJson(userInfoJson, UserInfo.class);
			if(userInfoTable.containsKey(info.username))
				return ResponseGenerator.createJsonResponse(false,
						new String[]{"Message"}, 
						new String[]{"User already exists"}, null);
			repo.put(info.username, userInfoJson);
			userInfoTable.put(info.username, info);
			return ResponseGenerator.createJsonResponse(true, 
					ServiceSetting.instance().getLobbyViewUrl());
		} catch (Exception ex) {
			return ResponseGenerator.createJsonResponse(false,
					new String[]{"Message"}, 
					new String[]{ex.getMessage()}, null);
		}
	}

	@Override
	public String getUserInfo(String username, String password) {
		assert gson != null;
		if(!userInfoTable.containsKey(username))
			return ResponseGenerator.createJsonResponse(false,
					new String[]{"Message"}, 
					new String[]{"User doesn't exist"},
					ServiceSetting.instance().getUserInfoViewUrl());
		UserInfo info = userInfoTable.get(username);
		if(!password.equals(info.password))
			return ResponseGenerator.createJsonResponse(false,
					new String[]{"Message"}, 
					new String[]{"Wrong password"},
					ServiceSetting.instance().getUserInfoViewUrl());
		return ResponseGenerator.createJsonResponse(true,
				new String[]{"userInfo"}, 
				new UserInfo[]{info}, 
				ServiceSetting.instance().getLobbyViewUrl());
	}

	@Override
	public String setUserInfo(String userInfoJson) {
		assert gson != null;
		try {
			UserInfo info = gson.fromJson(userInfoJson, UserInfo.class);
			if(!userInfoTable.containsKey(info.username))
				return ResponseGenerator.createJsonResponse(false,
						new String[]{"Message"}, 
						new String[]{"User doesn't exist"},
						ServiceSetting.instance().getUserInfoViewUrl());
			if(!info.password.equals(userInfoTable.get(info.username)))
				return ResponseGenerator.createJsonResponse(false,
						new String[]{"Message"}, 
						new String[]{"Wrong password"},
						ServiceSetting.instance().getUserInfoViewUrl());
			repo.put(info.username, userInfoJson);
			userInfoTable.put(info.username, info);
			return ResponseGenerator.createJsonResponse(true, 
					ServiceSetting.instance().getLobbyViewUrl());
		} catch (Exception ex) {
			return ResponseGenerator.createJsonResponse(false,
					new String[]{"Message"}, 
					new String[]{ex.getMessage()}, null);
		}
	}

	@Override
	public String handleRequest(String url, String method, String reqBody) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
