package org.stellar.gameplat.service.pageserve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.stellar.gameplat.service.contract.IPageService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;
import org.stellar.gameplat.service.webexchange.ResponseGenerator;

public class PageService implements IPageService {

	@Override
	public ServiceResponse handleRequest(String url, String method,
			String reqBody, Hashtable<String, String> params) {
		String pagePath = "page/"+params.get("pagePath");
		String content = getPage(pagePath);
		if(content == null)
			return ResponseGenerator.serviceResponse(404, "Page not found");
		ServiceResponse res = ResponseGenerator.serviceResponse(200, null);
		res.body = content;
		res.headers.put("Content-Type", "text/html");
		return res;
	}

	@Override
	public String getPage(String pagePath) {
		File f = new File(pagePath);
		if(!f.exists())
			return null;
		try {
			BufferedReader fr = new BufferedReader(new FileReader(f));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = fr.readLine())!=null)
				sb.append(line);
			fr.close();
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	//----test stub-----------------------------------
	
	public static void main(String[] args) {
		PageService serv = new PageService();
		System.out.println(serv.getPage("page/hello.html"));
	}
	
}
