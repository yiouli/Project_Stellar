package org.stellar.gameplat.service.contract;

import org.stellar.gameplat.service.contract.data.ServiceResponse;

public interface ILobbyService extends IServiceContract {

	public String host();
	public String[] participants();
	public boolean isReady();
	public ServiceResponse changeHost(String username, String password, int newHost);
	public ServiceResponse startGame(String username, String password);
	public ServiceResponse changeSetting(String username, String password, String gameParams);
}
