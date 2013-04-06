package org.stellar.gameplat.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class ServiceSetting {

	//for Gson de-serialization
	private static class Setting {
		String repoDir;
		String userInfoViewUrl;
		String lobbyViewUrl;
		String gameViewUrl;
		String userInfoServiceUrl;
	}
	
	private Setting setting;
	private static ServiceSetting instance = null;
	
	private ServiceSetting(String configPath) throws IOException {
		File f = new File(configPath);
		if(!f.exists() || !f.isFile());
		BufferedReader reader = new BufferedReader(new FileReader(f));
		setting = new Gson().fromJson(reader, Setting.class);
		reader.close();
	}
	
	public static void init(String configPath) throws IOException {
		if (instance != null)
			throw new IllegalStateException("already initalized");
		instance = new ServiceSetting(configPath);
	}
	
	public static ServiceSetting instance() {
		if(instance == null)
			throw new IllegalStateException("Not initialized, call init(configPath) first");
		return instance;
	}
	
	public String getRepoDir() {
		return setting.repoDir;
	}
	
	public String getUserInfoViewUrl() {
		return setting.userInfoViewUrl;
	}
	
	public String getLobbyViewUrl() {
		return setting.lobbyViewUrl;
	}
	
	public String getGameViewUrl() {
		return setting.gameViewUrl;
	}

	public String getUserInfoServiceUrl() {
		return setting.userInfoServiceUrl;
	}
	
	//----test stub--------------------------------------
	
	public static void main(String[] args) throws IOException {
		String configPath = "testInput/config.json";
		ServiceSetting.init(configPath);
		System.out.println("repoDir : " + ServiceSetting.instance().getRepoDir());
		System.out.println("userInfoViewUrl : " + ServiceSetting.instance().getUserInfoViewUrl());
		System.out.println("lobbyViewUrl : " + ServiceSetting.instance().getLobbyViewUrl());
		System.out.println("gameViewUrl : " + ServiceSetting.instance().getGameViewUrl());
		System.out.println("userInfoServiceUrl : " + ServiceSetting.instance().getUserInfoServiceUrl());
	}
}
