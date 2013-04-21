package org.yiouli.dragon.communication.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

import org.yiouli.dragon.communication.socket.IConnectionListener;
import org.yiouli.dragon.communication.socket.IMessageListener;
import org.yiouli.dragon.communication.socket.TerminationLstr;

public class WebSocketConnection extends RawSocketConnection {

	protected boolean handshaked;
	
	public WebSocketConnection() {
		throw new UnsupportedOperationException();
	}

	public WebSocketConnection(Socket sock,
			List<IConnectionListener> connListeners,
			List<IMessageListener> msgListeners) throws IOException {
		super(sock, connListeners, msgListeners);
		init(new WebSocketDataReceiver(connId, rin, msgListeners, new TerminationLstr(this)));
		handshaked = false;
	}

	private byte[] serializeData(Object msg) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(msg);
		  return bos.toByteArray();
		} finally {
		  out.close();
		  bos.close();
		}
	}
	
	@Override
	protected byte[] serialize(Object msg) throws IOException {
		if(handshaked) {
			boolean isText = (msg instanceof String);
			byte[] data = isText ? 
					((String)msg).getBytes("UTF8") : serializeData(msg);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int first = isText ? 129 : 130;
			bout.write(first);
			int len = data.length;
			if (len >= 65536) {
				ByteBuffer bf = ByteBuffer.allocate(4);
				bf.putInt(len);
				bout.write(new byte[4]);
				bout.write(bf.array());
			}
			else if (len >= 126) {
				ByteBuffer bf = ByteBuffer.allocate(2);
				bf.putShort((short)len);
				bout.write(bf.array());
			}
			else
				bout.write(len);
			bout.write(data);
			return bout.toByteArray();
		}
		else {
			handshaked = true;
			return ((String)msg).getBytes();
		}
	}

}
