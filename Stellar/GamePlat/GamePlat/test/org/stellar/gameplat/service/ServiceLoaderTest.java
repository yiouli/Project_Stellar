package org.stellar.gameplat.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.stellar.gameplat.service.contract.IServiceContract;

class SimpleService implements IServiceContract {

	static final String message = "hello";
	
	@Override
	public String handleRequest(String url, String method, String reqBody) {
		return message;
	}
}

public class ServiceLoaderTest {
	
	@Test
	public void testSimpleClass() {
		ServiceLoader.init();
		String className = "org.stellar.gameplat.service.SimpleService";
		IServiceContract service = ServiceLoader.instance().getServiceInstance(className);
		assertNotNull("service instance", service);
		assertEquals("call service", SimpleService.message, service.handleRequest(null, null, null));
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
