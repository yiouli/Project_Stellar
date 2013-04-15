package org.stellar.gameplat.service.contract;

import java.util.Collection;

public interface IServiceInteractive<Client> extends IServiceContract {

	public String getEndpointAddress();
	public Collection<Client> getClients();
}
