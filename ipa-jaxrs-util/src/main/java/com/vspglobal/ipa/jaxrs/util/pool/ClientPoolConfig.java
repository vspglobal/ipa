package com.vspglobal.ipa.jaxrs.util.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ClientPoolConfig extends GenericObjectPoolConfig{


		
	private static final int DEFAULT_MAX_TOTAL = 512;
	private static final int DEFAULT_MAX_IDLE = 50;
	private String pool_prefix_name= GenericObjectPoolConfig.DEFAULT_JMX_NAME_PREFIX;
	

	public ClientPoolConfig() {
		super();
		this.setJmxEnabled(true);
		this.setMaxIdle(Integer.getInteger("restClient.maxIdle", DEFAULT_MAX_IDLE));
		this.setMinIdle(Integer.getInteger("restClient.minIdle", GenericObjectPoolConfig.DEFAULT_MIN_IDLE));		
		this.setMaxTotal(Integer.getInteger("restClient.maxTotal", DEFAULT_MAX_TOTAL));
	}
	
	public ClientPoolConfig(String jmxNamePrefix ) {
		this();
		pool_prefix_name = jmxNamePrefix;
	}
	
	
	
	@Override
	 public String getJmxNamePrefix(){
		 return pool_prefix_name;
	 }
	

}
