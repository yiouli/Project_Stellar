package org.stellar.gameplat.service;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Test;
import org.stellar.gameplat.service.contract.IServiceContract;
import org.stellar.gameplat.service.contract.data.ServiceResponse;

import com.sun.net.httpserver.HttpExchange;

class SimpleService implements IServiceContract {

	static final String message = "hello";
	
	@Override
	public ServiceResponse handleRequest(HttpExchange httpExchange, String url,
			 String method, String reqBody, Hashtable<String, String> params) {
		return new ServiceResponse(200, message);
	}
}

public class ServiceLoaderTest {
	
	@Test
	public void testSimpleClass() {
		ServiceLoader.init();
		String className = "org.stellar.gameplat.service.SimpleService";
		IServiceContract service = ServiceLoader.instance().getServiceInstance(className);
		assertNotNull("service instance", service);
		ServiceResponse res = service.handleRequest(null, null, null, null, null);
		assertEquals("service response status", 200, res.status);
		assertEquals("service response body", SimpleService.message, res.body);
	}

	@Test
	public void testClassNotFound() {
		fail("Not yet implemented");
	}

	@Test
	public void testClassNotService() {
		fail("Not yet implemented");
	}

	@Test
	public void testNoDefaultConstructor() {
		fail("Not yet implemented");
	}

	@Test
	public void testAccessibility() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testNestedClass() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testInnerStaticClass() {
		fail("Not yet implemented");
	}
}
