package org.yiouli.dragon.communication.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashSet;

import org.yiouli.dragon.communication.socket.IMessageListener;
import org.yiouli.dragon.communication.socket.SocketConnection;
import org.yiouli.dragon.communication.socket.SocketHost;
import org.yiouli.dragon.framework.exception.AggregatedException;

public class WebSocketHost extends SocketHost {

	private HashSet<Integer> upgraded;
	
	public WebSocketHost(int port) throws IOException {
		super(port);
		upgraded = new HashSet<Integer>();
		addMessageListener(new WebSocketHandshaker());
	}

	@Override
	protected void createConnection(Socket sock) throws IOException {
		SocketConnection conn = new WebSocketConnection(sock, connLstrs, msgLstrs);
		synchronized(connections) {
			connections.put(conn.getConnectionId(), conn);
		}
	}

	@Override
	public boolean addConnectionListener(IConnectionListener listener) {
		return super.addConnectionListener(new ConnectionLstrFilter(listener));
	}
	
	@Override
	public void sendToAll(Object msg) throws AggregatedException {
		AggregatedException aex = null;
		synchronized(connections) {
			for(Enumeration<SocketConnection> e = connections.elements(); e.hasMoreElements(); ) {
				try {
					e.nextElement().sendMessage(msg);
				} catch(IOException ex) {
					if(aex == null)
						aex = new AggregatedException();
					aex.addInnerException(ex);
				}
			}
		}
		if(aex != null)
			throw aex;
	}

	@Override	
	public void sendToOthers(int connId, Object msg) throws AggregatedException {
		AggregatedException aex = null;
		synchronized(connections) {
			for(Integer id : connections.keySet()) {
				try {
					if(id != connId)
						connections.get(id).sendMessage(msg);	
				} catch(IOException ex) {
					if(aex == null)
						aex = new AggregatedException();
					aex.addInnerException(ex);
				}
			}
		}
		if(aex != null)
			throw aex;
	}
	
	private class WebSocketHandshaker implements IMessageListener {

		@Override
		public boolean handleMessage(int connectionId, Object msg,
				boolean isIncoming) {
			if(isIncoming && !upgraded.contains(connectionId)) {
				//if not upgraded to web sock, block other message handlers
				//check if is websocket update
				//if not return invalid operation
				//if is web socket handshake request, return response, unblock
				String req = (String)msg;
				WebSocketExchange exchange = new WebSocketExchange();
				try {
					if(exchange.isUpgradeRequest(req)) {
						sendMessage(connectionId, exchange.successResponse());
						upgraded.add(connectionId);
						for(IConnectionListener listener : connLstrs)
							listener.handleConnection(connectionId, true);
					}
					else
						removeConnection(connectionId);
				} catch (IOException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
			return true;
		}

		@Override
		public boolean isValid() {
			return true;
		}
	}
	
	private class ConnectionLstrFilter implements IConnectionListener {
		
		private IConnectionListener connectionListener;
		ConnectionLstrFilter(IConnectionListener connectionListener) {
			if (connectionListener == null)
				throw new NullPointerException();
			this.connectionListener = connectionListener;
		}
		
		@Override
		public boolean handleConnection(int connectionId, boolean isOnConnect) {
			if (!upgraded.contains(connectionId))
				return false;
			return connectionListener.handleConnection(connectionId, isOnConnect);
		}

		@Override
		public boolean isValid() {
			return true;
		}
	}
}
