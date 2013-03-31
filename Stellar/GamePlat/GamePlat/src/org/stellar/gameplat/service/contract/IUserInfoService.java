package org.stellar.gameplat.service.contract;

import org.stellar.gameplat.service.contract.data.ServiceResponse;
import org.stellar.gameplat.service.contract.data.UserInfo;

public interface IUserInfoService extends IServiceContract {
	
	public ServiceResponse addUserInfo(UserInfo info);
	public ServiceResponse getUserInfo(String username, String password);
	public ServiceResponse setUserInfo(UserInfo info);
}
