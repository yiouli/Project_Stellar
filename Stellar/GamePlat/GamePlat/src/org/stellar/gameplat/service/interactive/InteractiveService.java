package org.stellar.gameplat.service.interactive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.yiouli.dragon.communication.socket.IConnectionListener;
import org.yiouli.dragon.communication.socket.IMessageListener;
import org.yiouli.dragon.communication.socket.WebSocketHost;
import org.yiouli.dragon.framework.exception.AggregatedException;

/**
 * String based webSocket service
 * @author leoSU
 *
 * @param <Client>
 */
public abstract class InteractiveService<Client> {

	private class ConnectionHandler implements IConnectionListener {

		@Override
		public boolean handleConnection(int connectionId, boolean isOnConnect) {
			if(isOnConnect) {
				addClient(connectionId);
			}
			else {
				onDisconnect(idToClient.get(connectionId));
				removeClient(connectionId);
			}
			return true;
		}
	}
	
	private class ActionHandler implements IMessageListener {
		
		@Override
		public boolean handleMessage(int connectionId, Object msg,
				boolean isIncoming) {
			if(isIncoming) {
				onMessage(idToClient.get(connectionId), (String)msg);
			}
			return true;
		}
	}
	
	protected final int port;
	private final WebSocketHost host;
	private Hashtable<Integer, Client> idToClient;
	private Hashtable<Client, Integer> clientToId;	
	
	public InteractiveService(int port) throws IOException {
		this.port = port;
		idToClient = new Hashtable<Integer, Client>();
		clientToId = new Hashtable<Client, Integer>();
		host = new WebSocketHost(port);
		host.addConnectionListener(new ConnectionHandler());
		host.addMessageListener(new ActionHandler());
	}
	
	private void addClient(int connectionId) {
		Client client = onConnect();
		idToClient.put(connectionId, client);
		clientToId.put(client, connectionId);
		onOpen(client);
	}
	
	private void removeClient(int connectionId) {
		clientToId.remove(idToClient.remove(connectionId));
	}
	
	public int getPort() {
		return port;
	}

	public int getClientCount() {
		return idToClient.size();
	}
	
	public ArrayList<Client> getClients() {
		ArrayList<Client> ret = new ArrayList<Client>(idToClient.size());
		for(Client client : clientToId.keySet())
			ret.add(client);
		return ret;
	}
	
	public void start() {
		host.start();
	}
	
	public void stop() throws IOException {
		host.shutdown();
	}
	
	public boolean isRunning() {
		return host.isAlive();
	}

	protected void push(String message) throws AggregatedException {
		host.sendToAll(message);
	}
	
	protected void pushToOthers(Client client, String message) throws AggregatedException {
		host.sendToOthers(clientToId.get(client), message);
	}
	
	protected void push(Client client, String message) throws IOException {
		int connId = clientToId.get(client);
		host.sendMessage(connId, message);
	}
	
	protected void remove(Client client) throws IOException {
		int connId = clientToId.get(client);
		host.removeConnection(connId);
	}
	
	protected abstract Client onConnect();
	
	protected abstract void onOpen(Client client);
	
	protected abstract void onMessage(Client client, String message);
	
	protected abstract void onDisconnect(Client client);
	
}
