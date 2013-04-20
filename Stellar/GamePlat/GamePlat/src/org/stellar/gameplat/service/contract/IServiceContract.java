package org.stellar.gameplat.service.contract;

import java.util.Hashtable;

import org.stellar.gameplat.service.contract.data.ServiceResponse;

import com.sun.net.httpserver.HttpExchange;

public interface IServiceContract {

	/**
	 * 
	 * @param url
	 * @param method
	 * @param reqBody
	 * @param params is parameters extracted from url, according to UrlMapping config,
	 * 		  plus http request headers. If UrlMapping parameters will override request header
	 * 		  if there's key conflict. If UrlMapping have mutiple value with same key(should be avoid)
	 *        the later appeared parameter value will be taken.
	 * @return
	 */
	public ServiceResponse handleRequest(HttpExchange httpExchange, String url, 
			String method, String reqBody, Hashtable<String, String> params);
}
