package org.stellar.gameplat.service.contract;

import org.stellar.gameplat.service.contract.data.ServiceResponse;

public interface IServiceContract {

	public ServiceResponse handleRequest(String url, String method, String reqBody);
}