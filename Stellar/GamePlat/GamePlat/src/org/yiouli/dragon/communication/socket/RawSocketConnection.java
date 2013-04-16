package org.yiouli.dragon.communication.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.yiouli.dragon.communication.socket.IConnectionListener;
import org.yiouli.dragon.communication.socket.IMessageListener;
import org.yiouli.dragon.communication.socket.SocketConnection;

public abstract class RawSocketConnection extends SocketConnection {

	protected RawDataReceiver receiver;
	protected InputStream rin;
	protected OutputStream rout;
	
	public RawSocketConnection() {
		super();
	}
	
	public RawSocketConnection(Socket sock,
			List<IConnectionListener> connListeners, 
			List<IMessageListener> msgListeners) throws IOException {
		super(sock, connListeners, msgListeners);
	}
	
	//client need to explicitly call init before using other methods
	public void init(RawDataReceiver receiver) throws IOException {
		this.receiver = receiver;
		init(sock);
	}
	
	@Override
	protected void init(Socket sock) throws IOException {
		this.sock = sock;
		rin = sock.getInputStream();
		rout = sock.getOutputStream();
		//work around for init call from super constructor
		if(receiver == null)
			return;
		if(receiver.isAlive())
			throw new IllegalArgumentException("Receiver thread already running");
		receiver.start();
		synchronized(connListeners) {
			for(IConnectionListener connLstr : connListeners)
				if(!connLstr.handleConnection(connId, true))
					break;
		}
	}
	
	protected abstract byte[] serialize(Object msg) throws IOException;
	
	@Override
	public void sendMessage(Object msg) throws IOException {
		rout.write(serialize(msg));
		rout.flush();
		synchronized(msgListeners) {
			for(IMessageListener listener : msgListeners)
				if(!listener.handleMessage(0, msg, false))
					break;
		}
	}
}
