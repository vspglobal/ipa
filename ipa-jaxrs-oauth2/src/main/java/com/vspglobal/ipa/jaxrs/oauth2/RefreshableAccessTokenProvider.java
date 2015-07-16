package com.vspglobal.ipa.jaxrs.oauth2;

import com.vspglobal.ipa.domain.OAuth2Token;

import java.net.URI;


public class RefreshableAccessTokenProvider implements AccessTokenProvider {
	
	private final String[] scopes;
	private OAuth2Token accessToken;
	private final String client_id;
	private final String client_secret;
	
	public RefreshableAccessTokenProvider(
			String client_id, String client_secret, 
			String access_token, String refresh_token, 
			int expires_in, String... scopes) {
		this.scopes = scopes;
		
		this.client_id = client_id;
		this.client_secret = client_secret;
		
		this.accessToken = new OAuth2Token();
		this.accessToken.setAccess_token(access_token);
		this.accessToken.setRefresh_token(refresh_token);
		this.accessToken.setToken_type(OAuth2Token.TOKEN_TYPE_BEARER);
		this.accessToken.setExpires_in(expires_in);
	}
	
	public RefreshableAccessTokenProvider(
			String client_id, String client_secret, 
			OAuth2Token token) {
		
		if(token.getScopes() != null)
			this.scopes = token.getScopes().toArray(new String[token.getScopes().size()]);
		else
			this.scopes = null;
		
		this.client_id = client_id;
		this.client_secret = client_secret;
		
		this.accessToken = new OAuth2Token();
		this.accessToken.setAccess_token(token.getToken());
		this.accessToken.setRefresh_token(token.getRefresh_token());
		this.accessToken.setToken_type(token.getToken_type());
		this.accessToken.setExpires_in(token.getExpires_in());
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
			
			if(accessToken.getRefresh_token() == null || accessToken.getRefresh_token().isEmpty())
				throw new IllegalStateException("Unable to refresh without a refresh_token");
			
			this.accessToken = new AccessTokenRequester()
							.grant_type(GrantType.REFRESH_TOKEN)
							.refresh_token(this.accessToken.getRefresh_token())
							.scope(scope)
			                .tokenEndpoint(tokenEndpoint)
			                .client_id(client_id)
			                .client_secret(client_secret)
							.request();
	}


	
}
