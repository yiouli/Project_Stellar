package org.stellar.gameplat.service.contract;

public interface ILobbyService extends IServiceContract {

	public String host();
	public String[] participants();
	public boolean isReady();
	public String startGame(String username, String password);
	public boolean changeSetting(String username, String password, String gameParams);
}
