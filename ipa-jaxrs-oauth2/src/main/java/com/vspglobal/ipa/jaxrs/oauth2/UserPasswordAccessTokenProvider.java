package com.vspglobal.ipa.jaxrs.oauth2;

import com.vspglobal.ipa.domain.OAuth2Token;

import java.net.URI;


public class UserPasswordAccessTokenProvider implements AccessTokenProvider {
	
	private String username;
	private String password;
	
	private final String client_id;
	private final String client_secret;
	private final String[] scopes;
	private OAuth2Token accessToken;
	
	private UserPasswordAccessTokenProvider(String client_id, String client_secret, String username, String password, String... scopes) {
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.username = username;
		this.password = password;
		this.scopes = scopes;
	}
	
	public static UserPasswordAccessTokenProvider get(String client_id, String client_secret, String username, String password, String... scopes) {
		return new UserPasswordAccessTokenProvider(client_id, client_secret, username, password, scopes);
	}
	
	@Override
	public OAuth2Token get() {
		return accessToken;
	}
	
	@Override
	public void refresh(URI tokenEndpoint) {
			String scope = null;
			if(scopes != null) {
				StringBuilder scopeBuilder = new StringBuilder();
				for(String as : scopes) {
					if(scopeBuilder.length()> 0)
						scopeBuilder.append(" ");
					scopeBuilder.append(as.toString());
				}
				scope = scopeBuilder.toString();
			}
			
			if(this.accessToken == null) {
				this.accessToken = new AccessTokenRequester()
				            .client_id(client_id)
				            .client_secret(client_secret)
				            .grant_type(GrantType.PASSWORD)
				            .username(username)
				            .password(password)
				            .scope(scope)
				            .tokenEndpoint(tokenEndpoint)
				            .request();
			} else {
				this.accessToken = new AccessTokenRequester()
					            .client_id(client_id)
					            .client_secret(client_secret)
								.grant_type(GrantType.REFRESH_TOKEN)
								.refresh_token(this.accessToken.getRefresh_token())
				                .tokenEndpoint(tokenEndpoint)
								.request();
			}
	}


	
}
