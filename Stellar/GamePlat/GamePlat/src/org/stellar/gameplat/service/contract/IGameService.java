package org.stellar.gameplat.service.contract;

public interface IGameService extends IServiceContract {

	public boolean isReady();
	public void start(String gameParams);
	public String getGameView();
}
