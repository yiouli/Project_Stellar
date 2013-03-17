package org.stellar.gameplat.service.contract;

public interface IServiceContract {

	public ServiceResponse handleRequest(String url, String method, String reqBody);
}
