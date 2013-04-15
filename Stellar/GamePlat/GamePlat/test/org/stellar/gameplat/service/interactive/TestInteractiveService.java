package org.stellar.gameplat.service.interactive;

import java.io.IOException;
import java.util.HashSet;

public class TestInteractiveService extends InteractiveService<String> {

	private HashSet<String> users;
	private int userIdx;
	
	public TestInteractiveService(int port) throws IOException {
		super(port);
		users = new HashSet<String>();
		userIdx = 0;
	}

	@Override
	protected String onConnect() {
		String u = "user"+String.valueOf(userIdx);
		users.add(u);
		System.out.println(u + " created!");
		return u;
	}

	@Override
	protected void onOpen(String user) {
		try {
			push(user);
		} catch(Exception ex) {
			System.err.println("Fail to push : "+ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	protected void onMessage(String user, String message) {
		try {
			push(user + " : "+message);
		} catch(Exception ex) {
			System.err.println("Fail to push : "+ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	protected void onDisconnect(String user) {
		System.out.println(user + " disconnected!");
	}
	
	public static void main(String[] args) throws IOException {
		TestInteractiveService serv = new TestInteractiveService(20500);
		serv.start();
		System.out.println("Service started...");
	}
}
