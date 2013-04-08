package org.stellar.gameplat.service.web;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.stellar.gameplat.service.ServiceLoader;
import org.stellar.gameplat.service.ServiceSetting;

import com.sun.net.httpserver.HttpServer;

public class ServerBuilder {

	public String serviceConfigPath;
	public String webConfigPath; 
	
	public ServerBuilder(String serviceConfigPath, String webConfigPath) {
		if(serviceConfigPath == null || webConfigPath == null)
			throw new NullPointerException();
		this.serviceConfigPath = serviceConfigPath;
		this.webConfigPath = webConfigPath;
	}
	
	public HttpServer createServer(int port, String context) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 10);
		ServiceSetting.init(serviceConfigPath);
		UrlMapper.init(webConfigPath);
		ServiceLoader.init();
		server.createContext(context, 
				new RequestDispatcher(UrlMapper.instance(), ServiceLoader.instance()));
		server.setExecutor(null); // creates a default executor
		return server;
	}
	
	//----test stub-------------------------------------------------------------------
	
	public static void main(String[] args) throws IOException {
		ServerBuilder builder = new ServerBuilder("testInput/config.json", "testInput/config.web.json");
		builder.createServer(8000, "/gameplatform").start();
		System.out.println("Server started...");
	}
}
