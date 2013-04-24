package org.stellar.gameplat.service.hub;

import java.io.IOException;
import java.util.ArrayList;

import org.stellar.gameplat.service.interactive.InteractiveService;

class Connection<PlayerType> {
	final int connectionId;
	PlayerType player;
	
	Connection(int connectionId) {
		this.connectionId = connectionId;
		player = null;
	}
}

public abstract class LobbyInteractiveService<PlayerType> 
						extends InteractiveService<Connection<PlayerType>> {

	private final Lobby<PlayerType> lobby;
	
	public LobbyInteractiveService(Lobby<PlayerType> lobby, int port) throws IOException {
		super(port);
		this.lobby = lobby;
	}

	@Override
	protected Connection<PlayerType> onConnect(int connectionId) {
		return new Connection<PlayerType>(connectionId);
	}

	protected abstract PlayerType identify(Connection<PlayerType> client, String message);
	
	@Override
	protected final void onMessage(Connection<PlayerType> client, String message) {
		//try get player's identity from the connection
		if (client.player == null) {
			PlayerType player = identify(client, message);
			ArrayList<PlayerType> players = lobby.getPlayers();
			if(players.contains(player))
				client.player = player;
			else
				try {
					remove(client);
				} catch (IOException e) {
					System.out.println("Fail to remove player : "+e.getMessage());
					System.out.println(client.player.toString());
					e.printStackTrace();
				}
		}
		else {
			
		}
	}

	@Override
	protected void onDisconnect(Connection<PlayerType> client) {
		// TODO Auto-generated method stub
		
	}

}
