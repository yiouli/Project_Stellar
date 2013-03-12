package org.stellar.gameplat.service.userinfostore;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.stellar.gameplat.service.httpserver.ServiceSetting;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Function tests for UserInfoStore. UserInfoStore depends on httpserver.ServiceSetting and
 * httpserver.ResponseGenerator, make sure dependencies function correctly in order to get
 * proper test results.
 * 
 * @author leoSU
 *
 */
public class UserInfoStoreTest {

	private Hashtable<String, UserInfo> infos = new Hashtable<String, UserInfo>();
	private final String pw = "abcd1234";
	
	private UserInfo addUserInfo(String name, String pw, String ui) throws IOException {
		UserInfo info = new UserInfo();
		info.username = name;
		info.password = pw;
		info.userinfo = ui;
		File f = new File(ServiceSetting.Instance().getRepoDir()
					+ File.separator + info.username + ".json");
		f.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		writer.write(new Gson().toJson(info));
		infos.put(info.username, info);
		writer.close();
		return info;
	}
	
	private void addUserInfos() throws IOException {
		addUserInfo("user1", pw, "user1's info");
		addUserInfo("user2", pw, "user2's info");
		addUserInfo("user3", pw, "user3's info");
	}
	
	@Before
	public void init() throws IOException {
		ServiceSetting.init("testInput/config.json");
		//setup repository, clean up first
		File repo = new File(ServiceSetting.Instance().getRepoDir());
		if(!repo.exists())
			repo.mkdirs();
		File[] files = repo.listFiles();
		for(File f : files)
			f.delete();
		addUserInfos();
	}
	
	@After
	public void cleanup() {
		File repo = new File(ServiceSetting.Instance().getRepoDir());
		File[] files = repo.listFiles();
		for(File f : files)
			f.delete();
		repo.delete();
	}
	
	private String getJsonString(String username, String password, String userinfo) {
		JsonObject json = new JsonObject();
		json.addProperty("username", username);
		json.addProperty("password", password);
		json.addProperty("userInfo", userinfo);
		return json.toString();
	}
	
	@Test
	public void testAddUserInfo() throws IOException {
		UserInfoStore store = new UserInfoStore();
		JsonParser parser = new JsonParser();
		String jsonStr = getJsonString("user4", pw, "user4's info");
		String result = store.addUserInfo(jsonStr);
		System.out.println(result);
		JsonObject resJson = parser.parse(result).getAsJsonObject();
		assertTrue("add new user success", resJson.get("success").getAsBoolean());
		assertEquals("add new user directTo", 
				resJson.get("directTo").getAsString(),
				ServiceSetting.Instance().getLobbyViewUrl());
	}

	@Test
	public void testGetUserInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUserInfo() {
		fail("Not yet implemented");
	}

}
