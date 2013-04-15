package org.stellar.gameplat.service.interactive;

import java.util.Hashtable;

import org.stellar.gameplat.security.Encoding;

public class WebSocketExchange {
	
	private static class Request {
		
		Hashtable<String, String> headers;
		
		Request(String request) {
			headers = new Hashtable<String, String>();
			String[] lines = request.split("\n");
			for(String line : lines) {
				String[] tok = line.split(":");
				if(tok.length != 2)
					continue;
				headers.put(tok[0].trim().toLowerCase(), tok[1].trim());
			}
		}
	}
	
	private Request req = null;
	
	private boolean isUpgradeRequest(Request req) {
		return "websocket".equalsIgnoreCase(req.headers.get("upgrade"))
				&& "upgrade".equalsIgnoreCase(req.headers.get("connection"))
				&& req.headers.containsKey("sec-websocket-key");
	}
	
	public boolean isUpgradeRequest(String request) {
		req = new Request(request);
		return isUpgradeRequest(req);
	}
	
	private String hash(String key) {
		String salt = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		key = key.concat(salt);
		return Encoding.toBase64(Encoding.toSHA1(key));
	}
	
	protected String successResponse() {
		StringBuffer sb = new StringBuffer();
		String lb = System.getProperty("line.separator");
		sb.append("HTTP/1.1 101 Switching Protocols");
		sb.append(lb);
		sb.append("Upgrade: websocket");
		sb.append(lb);
		sb.append("Connection: Upgrade");
		sb.append(lb);
		sb.append("Sec-WebSocket-Accept: ");
		sb.append(hash(req.headers.get("sec-websocket-key")));
		sb.append(lb);
		if(req.headers.containsKey("sec-websocket-protocol")) {
			sb.append("Sec-WebSocket-Protocol: ");
			sb.append(req.headers.get("sec-websocket-protocol"));
			sb.append(lb);
		}
		sb.append(lb);
		return sb.toString();
	}
	
	protected String invalidOperationResponse() {
		return "HTTP/1.1 400 Bad Request";
	}
	
	public String handleRequest(String request) {
		if(isUpgradeRequest(request))
			return successResponse();
		return invalidOperationResponse();
	}
}
