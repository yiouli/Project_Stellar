package org.yiouli.dragon.communication.socket;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.yiouli.dragon.framework.common.ICommand;
import org.yiouli.dragon.framework.logging.EventMessage;
import org.yiouli.dragon.framework.logging.Logging;

public abstract class RawDataReceiver extends MessageReceiver {

	protected final InputStream rin;
	
	RawDataReceiver(int connectionId, InputStream rin, 
			LinkedList<IMessageListener> listeners, 
			ICommand<Boolean> terminationLstr) {
		super(connectionId, null, listeners, terminationLstr);
		if(rin == null)
			throw new IllegalArgumentException();
		this.rin = rin;
	}
	
	protected boolean check() {
		return msgListeners != null && terminationLstr != null;
	}
	
	protected abstract Object readMessage() throws IOException;
	
	@Override
	public void run() {
		assert check() && rin != null;
		try {
			while(true) {
				Object msg = readMessage();
				synchronized(msgListeners) {
					for(IMessageListener listener : msgListeners)
						if(!listener.handleMessage(connectionId, msg, true))
							break;
				}
			}
		}
		catch(IOException ex) {
			try {
				terminationLstr.execute(false);
			} catch(Exception terminateEx) {
				terminateEx.printStackTrace();
			}
			Logging.logMessage(new EventMessage(this.toString() + " socket input stream closed"));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
