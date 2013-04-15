package org.yiouli.dragon.communication.socket;

import java.io.IOException;
import java.net.Socket;

public class HttpSocketHost extends SocketHost {

	public HttpSocketHost(int port) throws IOException {
		super(port);
	}

	@Override
	protected void createConnection(Socket sock) throws IOException {
		/*RawSocketConnection conn = new RawSocketConnection(sock, connLstrs, msgLstrs);
		conn.init(new WebSocketDataReceiver(conn.getConnectionId(), 
						conn.rin, msgLstrs, new TerminationLstr(conn)));*/
		SocketConnection conn = new WebSocketConnection(sock, connLstrs, msgLstrs);
		synchronized(connections) {
			connections.put(conn.getConnectionId(), conn);
		}
	}
}
