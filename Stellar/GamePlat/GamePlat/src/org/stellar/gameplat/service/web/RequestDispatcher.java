package org.stellar.gameplat.service.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Hashtable;

import org.stellar.gameplat.service.contract.IServiceContract;

import com.sun.net.httpserver.*;

public class RequestDispatcher implements HttpHandler {
	
	private Hashtable<String, IServiceContract> services = new Hashtable<String, IServiceContract>();;
	
	
	private String read(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null)
			sb.append(line);
		return sb.toString();
	}

	//dispatch to service via contract call and get response string
	private String dispatch(String url, String method, String reqBody) {
		if(!services.containsKey(url))
			return "{\"success\":false,\"message\":\"service not found\"}";
		return services.get(url).handleRequest(url, method, reqBody);
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
   		String reqBody = read(t.getRequestBody());
   		String url = t.getRequestURI().toString().toLowerCase();
   		String resBody = dispatch(url, t.getRequestMethod(), reqBody);
   		t.getResponseHeaders().set("Content-Type", "text/json");
   		//need get response code from service??
   		t.sendResponseHeaders(200, resBody.length());
   		OutputStream os = t.getResponseBody();
   		os.write(resBody.getBytes());
   		os.close();
	}

	/**
	 * test stub
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 10);
		server.createContext("/applications/myapp", new RequestDispatcher());
		server.setExecutor(null); // creates a default executor
		server.start();
	}
}
