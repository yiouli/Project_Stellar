package org.stellar.gameplat.service.userinfostore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.stellar.gameplat.service.ServiceSetting;
import org.stellar.gameplat.service.contract.data.UserInfo;
import org.stellar.gameplat.service.web.UrlMapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserInfoClient {

	private String hostUrl;
	private String lastMessage;
	
	public String getLastMessage() {
		return lastMessage;
	}
	
	public UserInfoClient() {
		this.hostUrl = ServiceSetting.instance().getUserInfoServiceUrl();
		assert this.hostUrl != null;
	}
	
	public UserInfoClient(String hostUrl) {
		if(hostUrl == null)
			throw new NullPointerException();
		this.hostUrl = hostUrl;
	}
	
	private String read(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null)
			sb.append(line);
		return sb.toString();
	}

	public UserInfo getUserInfo(String username, String password) throws IOException {
		if(username == null)
			throw new NullPointerException();
		assert hostUrl != null;
		String url = UrlMapper.instance().getUrl("userInfo", new String[]{username});
		if(password != null)
			url += "?password="+password;
		URL host = new URL(hostUrl + url);
		HttpURLConnection connection = (HttpURLConnection)host.openConnection();
	    connection.setDoInput(true);
		connection.setRequestMethod("GET");
		InputStream is = connection.getResponseCode()!=200 ?
				connection.getErrorStream() : connection.getInputStream();
		String json = read(is);
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(json).getAsJsonObject();
		if(!o.get("success").getAsBoolean()) {
			lastMessage = o.get("message").getAsString();
			return null;
		}
		Gson gson = new Gson();
		return gson.fromJson(o.get("userInfo"), UserInfo.class);
	}
	
	public boolean authenticate(String username, String password) throws IOException {
		return getUserInfo(username, password) != null;
	}
	
	//----test stub--------------------------------------------------------------------
	
	private static void print(UserInfoClient client, String username, String password) throws IOException {
		UserInfo info = client.getUserInfo(username, password);
		System.out.println("Get userInfo for "+username);
		if(info == null)
			System.out.println("error : "+client.getLastMessage());
		else {
			System.out.println("username : "+info.username);
			System.out.println("password : "+info.password);
			System.out.println("userinfo : "+info.userinfo);
		}
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		UrlMapper.init("testInput/config.web.json");
		UserInfoClient client = new UserInfoClient("http://localhost:8000");
		print(client, "user1", "abcd1234");
		print(client, "user2", "aaaa");
		print(client, "user1", "aaaa");
		print(client, "user3", "aaaa");
	}
}
