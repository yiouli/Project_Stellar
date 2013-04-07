package org.stellar.gameplat.service.contract;

public interface IResourceService extends IServiceContract {

	public String getContentType(String resourcePath);
	public String getResouce(String resourcePath);
}
