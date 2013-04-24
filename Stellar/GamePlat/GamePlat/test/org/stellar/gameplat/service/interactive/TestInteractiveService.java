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
	protected String onConnect(int connectionId) {
		String user = "user"+String.valueOf(userIdx++);
		users.add(user);
		System.out.println(user + " created!");
		return user;
	}

	@Override
	protected void onOpen(String user) {
		try {
			push(user, user);
			push(user + " joined!");
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
		try {
			System.out.println(user + " disconnected!");
			push(user + " left!");
		} catch(Exception ex) {
			System.err.println("Fail to push : "+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		TestInteractiveService serv = new TestInteractiveService(20500);
		serv.start();
		System.out.println("Service started...");
	}
}
