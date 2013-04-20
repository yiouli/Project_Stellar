package org.stellar.gameplat.service.contract;

import com.sun.net.httpserver.HttpExchange;

public interface IReceptionService extends IServiceContract {

	public boolean accept(HttpExchange httpExchange, String ticket);
	public boolean accept(HttpExchange httpExchange, String gameId, String gameParams); 
	//public boolean cancel(String username, String password, String gameId);
}
