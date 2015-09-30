package com.vspglobal.ipa.jaxrs.oauth2.pool;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vspglobal.ipa.jaxrs.basicauth.HttpBasicAuthFilter;
import com.vspglobal.ipa.jaxrs.oauth2.AccessTokenReader;

public class ClientPoolObjectFactory extends BasePooledObjectFactory<Client> {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final Object[] providers = new Object[] {new AccessTokenReader(), new HttpBasicAuthFilter()};

	@Override
	public Client create() throws Exception {
		log.info("***** Creating Client Pool ");
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
