package com.vspglobal.ipa.jaxrs.oauth2;

import com.vspglobal.ipa.domain.OAuth2Token;

import java.net.URI;


public class AuthorizationCodeAccessTokenProvider implements AccessTokenProvider {
	
	private OAuth2Token accessToken;
	private final String client_id;
	private final String client_secret;
	private final String code;
	private final String redirect_uri;
	
	public AuthorizationCodeAccessTokenProvider(String client_id, String client_secret, String code, String redirect_uri) {
		
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.code = code;
		this.redirect_uri = redirect_uri;
	}
	
	@Override
	public OAuth2Token get() {
		return accessToken;
	}
	
	@Override
	public void refresh(URI tokenEndpoint) {
			if(accessToken == null) {
				this.accessToken = new AccessTokenRequester()
				                .tokenEndpoint(tokenEndpoint)
								.grant_type(GrantType.AUTHORIZATION_CODE)
								.code(this.code)
								.client_id(client_id)
								.client_secret(client_secret)
								.redirect_uri(redirect_uri)
								.request();
			} else {
				this.accessToken = new AccessTokenRequester()
								.grant_type(GrantType.REFRESH_TOKEN)
								.refresh_token(this.accessToken.getRefresh_token())
				                .tokenEndpoint(tokenEndpoint)
								.client_id(client_id)
								.client_secret(client_secret)
								.request();
			}
			
	}


	
}
