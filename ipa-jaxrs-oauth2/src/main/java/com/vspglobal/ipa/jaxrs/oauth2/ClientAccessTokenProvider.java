package com.vspglobal.ipa.jaxrs.oauth2;

import com.vspglobal.ipa.domain.OAuth2Token;

import java.net.URI;


public class ClientAccessTokenProvider implements AccessTokenProvider {
	private static ClientAccessTokenProvider CACHE;
	
	private final String client_id;
	private final String client_secret;
	private final String[] scopes;
	private OAuth2Token accessToken;
	
	private ClientAccessTokenProvider(String client_id, String client_secret, String... scopes) {
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.scopes = scopes;
	}
	
	public static ClientAccessTokenProvider get(String client_id, String client_secret, String... scopes) {
		boolean createNew = false;
		if(CACHE == null) {
			createNew = true;
		} else {
			if(!CACHE.client_id.equals(client_id)) {
				// client_id doesn't match...createNew
				createNew = true;
			} else {
				for(String requiredScope : scopes) {
					boolean found = false;
					for(String availScope : CACHE.scopes){
						if(availScope == requiredScope) {
							found = true;
							break;
						}
					}
					
					if(!found) {
						createNew = true;
						break;
					}
						
				}
			}
		}
		
		if(createNew)
			CACHE = new ClientAccessTokenProvider(client_id, client_secret, scopes);
		
		return CACHE;
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
			this.accessToken = new AccessTokenRequester()
			            .client_id(client_id)
			            .client_secret(client_secret)
			            .grant_type(GrantType.CLIENT_CREDENTIALS)
			            .scope(scope)
			            .tokenEndpoint(tokenEndpoint)
			            .request();
	}


	
}
