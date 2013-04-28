package org.stellar.gameplat.service.hub;

import java.io.IOException;

import org.stellar.gameplat.service.interactive.InteractiveService;

public class LobbyInteractive<PlayerType> extends InteractiveService<PlayerType> {

	public LobbyInteractive(int port) throws IOException {
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override

	protected PlayerType onConnect(int connectionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onOpen(PlayerType client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(PlayerType client, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDisconnect(PlayerType client) {
		// TODO Auto-generated method stub
		
	}

}
