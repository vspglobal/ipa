package com.vspglobal.ipa.jaxrs.util.pool;

import javax.ws.rs.client.Client;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;

public class ClientSofRefPool extends SoftReferenceObjectPool<Client> {

	public ClientSofRefPool(PooledObjectFactory<Client> factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}
	
	

}
