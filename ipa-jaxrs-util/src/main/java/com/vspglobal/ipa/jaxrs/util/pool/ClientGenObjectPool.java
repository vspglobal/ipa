package com.vspglobal.ipa.jaxrs.util.pool;

import javax.ws.rs.client.Client;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ClientGenObjectPool extends GenericObjectPool<Client> {

	public ClientGenObjectPool(PooledObjectFactory<Client> factory, GenericObjectPoolConfig config) {
		super(factory, config);
		// TODO Auto-generated constructor stub
	}

}
