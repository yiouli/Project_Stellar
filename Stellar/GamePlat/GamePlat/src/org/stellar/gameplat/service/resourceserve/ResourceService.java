package org.stellar.gameplat.service.resourceserve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Hashtable;

import org.stellar.gameplat.service.contract.IResourceService;
import org.stellar.gameplat.service.contract.data.ServiceResponse;
import org.stellar.gameplat.service.webexchange.ResponseGenerator;

public class ResourceService implements IResourceService {

	public static final String FOLDER = "resource";
	public final FileNameMap contentTypeMap;
	private final String lb = System.getProperty("line.separator");
	
	public ResourceService() {
		contentTypeMap = URLConnection.getFileNameMap();
	}
	
	private void setContentType(String resourcePath, Hashtable<String, String> headers) {
		String type = getContentType(resourcePath);
		if(type != null)
			headers.put("Content-Type", type);
	}
	
	@Override
	public ServiceResponse handleRequest(String url, String method,
			String reqBody, Hashtable<String, String> params) {
		String resourcePath = FOLDER+ "/"+params.get("pagePath");
		String content = getResouce(resourcePath);
		if(content == null)
			return ResponseGenerator.serviceResponse(404, "Resource not found");
		ServiceResponse res = ResponseGenerator.serviceResponse(200, null);
		res.body = content;
		setContentType(resourcePath, res.headers);
		return res;
	}

	@Override
	public String getContentType(String resourcePath) {
		return contentTypeMap.getContentTypeFor(resourcePath);
	}
	
	@Override
	public String getResouce(String pagePath) {
		File f = new File(pagePath);
		if(!f.exists())
			return null;
		try {
			BufferedReader fr = new BufferedReader(new FileReader(f));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = fr.readLine())!=null) {
				sb.append(line);
				sb.append(lb);
			}
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
		ResourceService serv = new ResourceService();
		System.out.println(serv.getResouce("resource/test/hello.html"));
	}

	
}
