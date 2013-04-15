package org.yiouli.dragon.communication.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.yiouli.dragon.framework.common.ICommand;

public class WebSocketDataReceiver extends RawDataReceiver {

	private final BufferedReader reader;
	private final String lb = System.getProperty("line.separator");
	private boolean handshaked = false;
	
	public WebSocketDataReceiver(int connectionId, InputStream rin,
			List<IMessageListener> listeners, ICommand<Boolean> terminationLstr) {
		super(connectionId, rin, listeners, terminationLstr);
		reader = new BufferedReader(new InputStreamReader(rin));
	}

	private String readHandshakeMessage() throws IOException {
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null && !line.isEmpty()) {
			sb.append(line);
			sb.append(lb);
		}
		return sb.toString();
	}
	
	@Override
	protected Object readMessage() throws IOException {
		String msg;
		if(handshaked)
			msg = reader.readLine();
		else {
			msg = readHandshakeMessage();
			handshaked = true;
		}
		if(msg == null)
			throw new IOException("End of Stream");
		return msg;
	}

}
