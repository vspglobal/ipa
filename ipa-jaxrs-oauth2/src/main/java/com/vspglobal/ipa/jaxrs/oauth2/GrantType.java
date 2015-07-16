package com.vspglobal.ipa.jaxrs.oauth2;

public enum GrantType {
	AUTHORIZATION_CODE("authorization_code"),
	PASSWORD("password"),
	REFRESH_TOKEN("refresh_token"),
	CLIENT_CREDENTIALS("client_credentials"),
	GET_TOKEN("urn:vspglobal.com:oauth2:grant_type:get_token");

	private String val;
	private GrantType(String val) {
		this.val = val;
	}
	
	@Override
	public String toString() {
		return val;
	}
}
