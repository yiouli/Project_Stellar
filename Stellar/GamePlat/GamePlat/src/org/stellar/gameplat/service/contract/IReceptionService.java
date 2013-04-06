package org.stellar.gameplat.service.contract;

public interface IReceptionService extends IServiceContract {

	public boolean join(String username, String password, String gameId, String gameParams); 
	//public boolean cancel(String username, String password, String gameId);
}
