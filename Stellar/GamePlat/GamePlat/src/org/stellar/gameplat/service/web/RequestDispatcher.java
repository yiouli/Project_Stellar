package org.stellar.gameplat.service.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Enumeration;

import org.stellar.gameplat.service.contract.IServiceContract;
import org.stellar.gameplat.service.contract.ServiceResponse;
import org.stellar.gameplat.service.ServiceLoader;
import org.stellar.gameplat.service.ServiceSetting;

import com.sun.net.httpserver.*;

class RequestDispatcher implements HttpHandler {

	private UrlMapper mapper;
	private ServiceLoader loader;
	
	RequestDispatcher(UrlMapper mapper, ServiceLoader loader) {
		if(mapper == null || loader == null)
			throw new NullPointerException();
		this.mapper = mapper;
		this.loader = loader;
	}
	
	private String read(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null)
			sb.append(line);
		return sb.toString();
	}

	private void sendHttpResponse(HttpExchange t, ServiceResponse res) throws IOException {
   		Headers headers = t.getResponseHeaders();
   		headers.set("Content-Type", "text/json");
   		Enumeration<String> enumKey = res.headers.keys();
   		while(enumKey.hasMoreElements()) {
   			String key = enumKey.nextElement();
   			headers.set(key, res.headers.get(key));
   		}
   		t.sendResponseHeaders(res.status, res.body.length());
   		OutputStream os = t.getResponseBody();
   		os.write(res.body.getBytes());
   		os.close();
		
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		String method = t.getRequestMethod();
   		String url = t.getRequestURI().toString().toLowerCase();
   		String serviceClassName = mapper.getServiceClassName(method, url);
   		if(serviceClassName == null) {
   			t.sendResponseHeaders(404, 0);
   			return;
   		}
   		IServiceContract service = loader.getServiceInstance(serviceClassName);
   		if(service == null)
   			throw new IllegalArgumentException("Service class not found: "+serviceClassName);
   		String reqBody = read(t.getRequestBody());
   		sendHttpResponse(t, service.handleRequest(url, method, reqBody));
	}

	/**
	 * test stub
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 10);
		ServiceSetting.init("testInput/config.json");
		UrlMapper.init("testInput/config.web.json");
		ServiceLoader.init();
		server.createContext("/gameplatform", 
				new RequestDispatcher(UrlMapper.instance(), ServiceLoader.instance()));
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Server started...");
	}
}
