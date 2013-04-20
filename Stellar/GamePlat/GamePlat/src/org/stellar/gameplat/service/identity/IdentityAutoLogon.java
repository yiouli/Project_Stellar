package org.stellar.gameplat.service.identity;

import java.io.IOException;

import org.stellar.gameplat.service.contract.data.UserInfo;
import org.stellar.gameplat.service.identity.userinfostore.UserInfoClient;

import com.sun.net.httpserver.HttpExchange;

public class IdentityAutoLogon {

	public static final String aliasKey = "alias";
	private UserInfoClient userInfo;
	
	public IdentityAutoLogon() {
		userInfo = new UserInfoClient();
	}

	/**
	 * Find existing record for client IP, create default identity if not found.
	 * Client's name returned and set as "alias" attribute.
	 * @param httpExchange
	 * @return
	 * @throws IOException
	 */
	public UserInfo getIdentity(HttpExchange httpExchange) throws IOException {
		String username = httpExchange.getRemoteAddress().toString();
		UserInfo info = userInfo.getUserInfo(username, null);
		if (info == null) {
			info = new UserInfo();
			info.username = username;
			info.userinfo = "Guest";
			userInfo.setUserInfo(info);
		}
		httpExchange.setAttribute(aliasKey, info.userinfo);
		return info;
	}
	
	public void changeAlias(HttpExchange httpExchange) throws IOException {
		UserInfo info = getIdentity(httpExchange);
		Object alias = httpExchange.getAttribute(aliasKey);
		if(alias == null || !(alias instanceof String))
			throw new IllegalArgumentException("alias attribute in HttpExchange");
		info.userinfo = (String) alias;
		userInfo.setUserInfo(info);
	}
}
