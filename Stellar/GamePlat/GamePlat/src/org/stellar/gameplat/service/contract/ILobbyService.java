package org.stellar.gameplat.service.contract;

import org.stellar.gameplat.service.contract.data.ServiceResponse;

public interface ILobbyService extends IServiceContract {

	public int[] getLobbyIds();
	public ServiceResponse lobby(int lobbyId);
	public String host(int lobbyId);
	public String[] participants(int lobbyId);
	public boolean isReady(int lobbyId);
	public ServiceResponse changeHost(int lobbyId, String username, String password, int newHost);
	public ServiceResponse startGame(int lobbyId, String username, String password);
	public ServiceResponse changeGameParams(int lobbyId, String username, String password, String gameParams);
}
