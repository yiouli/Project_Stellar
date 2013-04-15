package org.yiouli.dragon.communication.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class WebSocketConnection extends RawSocketConnection {

	private final String lb = System.getProperty("line.separator");
	
	public WebSocketConnection() {
		throw new UnsupportedOperationException();
	}

	public WebSocketConnection(Socket sock,
			List<IConnectionListener> connListeners,
			List<IMessageListener> msgListeners) throws IOException {
		super(sock, connListeners, msgListeners);
		init(new WebSocketDataReceiver(connId, rin, msgListeners, new TerminationLstr(this)));
	}

	@Override
	protected String serialize(Object msg) {
		return msg.toString() + lb;
	}

}
