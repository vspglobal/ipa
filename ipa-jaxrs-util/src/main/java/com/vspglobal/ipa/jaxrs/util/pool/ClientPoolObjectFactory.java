package com.vspglobal.ipa.jaxrs.util.pool;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ClientPoolObjectFactory extends BasePooledObjectFactory<Client> {

	private static  Object[] providers = new Object[] {};

	
	public ClientPoolObjectFactory(Object[] providersArg ) {
		providers = providersArg;
		
	}
		
	@Override
	public Client create() throws Exception {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		if(providers != null) {
			for(Object provider: providers) {
				clientBuilder = clientBuilder.register(provider);
			}
		}
		return  clientBuilder.build();
	}

	@Override
	public PooledObject<Client> wrap(Client client) {
		 return new DefaultPooledObject<Client>(client);
	}

}
