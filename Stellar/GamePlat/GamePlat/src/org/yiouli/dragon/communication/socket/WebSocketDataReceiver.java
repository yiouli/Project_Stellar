package org.yiouli.dragon.communication.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

import org.yiouli.dragon.communication.socket.IMessageListener;
import org.yiouli.dragon.framework.common.ICommand;

public class WebSocketDataReceiver extends RawDataReceiver {

	private final String lb = System.getProperty("line.separator");
	private boolean handshaked = false;
	
	public WebSocketDataReceiver(int connectionId, InputStream rin,
			LinkedList<IMessageListener> listeners, ICommand<Boolean> terminationLstr) {
		super(connectionId, rin, listeners, terminationLstr);
	}

	private String readHandshakeMessage() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(rin));
		StringBuffer sb = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null && !line.isEmpty()) {
			sb.append(line);
			sb.append(lb);
		}
		return sb.toString();
	}
	
	protected byte[] join(ArrayList<Frame> frames) {
		int len = 0;
		for(Frame f : frames)
			len += f.data.length;
		byte[] ret = new byte[len];
		int offset = 0, i = 0;
		while (offset < len) {
			byte[] src = frames.get(i++).data;
			System.arraycopy(src, 0, ret, offset, src.length);
			offset += src.length;
		}
		return ret;
	}
	
	protected int getLength(int n) throws IOException {
		byte[] b = read(n);
		ByteBuffer bf = ByteBuffer.wrap(b);
		return bf.getInt();
	}
	
	protected byte[] read(int n) throws IOException {
		byte[] ret = new byte[n];
		if (rin.read(ret) != n)
			throw new IOException("Unexpected end of stream");
		return ret;
	}
	
	protected byte[] unmask(byte[] mask, byte[] data) {
		for (int i = 0; i < data.length; i++)
			data[i] = (byte) (data[i] ^ mask[i%mask.length]);
		return data;
	}
	
	//return false if the frame is invalid
	protected Frame readFrame() throws IOException {
		Frame frame = new Frame();
		int first = rin.read();
		frame.fin = 128 == (128 & first);	//leading bit 1 means fin
		if ((112 & first) != 0)	//rsv non-zero
			throw new IOException("rsv non-zero");
		int type = (15 & first);
		if (type > 2)
			throw new IOException("unsupported data type");
		frame.isText = type == 1;
		int second = rin.read();
		if ((second & 128) != 128)	//not masked
			throw new IOException("mask bit not set");
		int len = (second & 127);
		if (len == 126) //next 2 bytes as len
			len = getLength(2);
		else if (len == 127) {	//next 8 bytes, for simplicity, don't support len > int.max
			if (getLength(4) != 0)
				throw new IOException("max supported data size is 2,147,483,647 bytes");
			len = getLength(4);
			if (len < 0)
				throw new IOException("max supported data size is 2,147,483,647 bytes");
		}
		byte[] mask = read(4);
		byte[] maskedData = read(len);
		frame.data = unmask(mask, maskedData);
		return frame;
	}
	
	protected Object readData() throws IOException {
		ArrayList<Frame> frames = new ArrayList<Frame>();
		Frame frame;
		do {
			frame = readFrame();
			frames.add(frame);
		} while(!frame.fin);
		byte[] data = join(frames);
		if (frames.get(0).isText)
			return new String(data, "US-ASCII");
		return data;
	}
	
	@Override
	protected Object readMessage() throws IOException {
		Object msg;
		if(handshaked)
			msg = readData();
		else {
			msg = readHandshakeMessage();
			handshaked = true;
		}
		if(msg == null)
			throw new IOException("End of Stream");
		return msg;
	}

	private static class Frame {
		boolean fin;
		boolean isText; //is text, otherwise is binary
		byte[] data;
	}
}
