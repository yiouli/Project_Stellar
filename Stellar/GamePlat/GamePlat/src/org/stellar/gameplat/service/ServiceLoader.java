package org.stellar.gameplat.service;

import java.util.Hashtable;

import org.stellar.gameplat.service.contract.IServiceContract;

/**
 * Dynamically create service instances and initialize them. Singleton instance
 * for each service class.
 * @author leoSU
 *
 */
public class ServiceLoader {

	private Hashtable<String, IServiceContract> services;
	
	private static ServiceLoader instance;
	
	private ServiceLoader() {
		services = new Hashtable<String, IServiceContract>();
	}

	public static void init() {
		if (instance != null)
			throw new IllegalStateException("already initalized");
		instance = new ServiceLoader();
	}
	
	public static ServiceLoader instance() {
		if(instance == null)
			throw new IllegalStateException("Not initialized, call init() first");
		return instance;
	}
	
	public IServiceContract getServiceInstance(String className) {
		if(services.containsKey(className))
			return services.get(className);
		try {
			Class<?> serviceClass = Class.forName(className);
			IServiceContract ret = (IServiceContract)serviceClass.newInstance();
			services.put(className, ret);
			return ret;
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
}
