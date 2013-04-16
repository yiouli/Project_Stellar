package org.yiouli.dragon.communication.socket;

import java.io.IOException;
import java.net.Socket;

public class WebSocketHost extends SocketHost {

	public WebSocketHost(int port) throws IOException {
		super(port);
	}

	@Override
	protected void createConnection(Socket sock) throws IOException {
		SocketConnection conn = new WebSocketConnection(sock, connLstrs, msgLstrs);
		synchronized(connections) {
			connections.put(conn.getConnectionId(), conn);
		}
	}
}
