package org.stellar.gameplat.service.contract;

public interface IUserInfoService extends IServiceContract {

	public String addUserInfo(String userInfoJson);
	public String getUserInfo(String username, String password);
	public String setUserInfo(String userInfoJson);
}
