package com.vspglobal.ipa.jaxrs.oauth2;


import com.vspglobal.ipa.domain.OAuth2Token;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.net.URI;

public class OAuth2BearerTokenFilter implements ClientRequestFilter,ClientResponseFilter {

	private static final String OAUTH2_TOKEN_ENDPOINT = "oauth2.token.endpoint";
	private static final String OAUTH2_TOKEN_PROVIDER = "oauth2.token.provider";
	private static final String OAUTH2_RUNAS_TOKEN_TYPE = "oauth2.runas.token_type";
	private static final String OAUTH2_RUNAS_TOKEN = "oauth2.runas.token";

	public OAuth2BearerTokenFilter() {
	}

	public static  void applyProperties(Invocation request, URI tokenEndpoint, AccessTokenProvider tokenProvider) {
		if(tokenEndpoint != null)
            request.property(OAUTH2_TOKEN_ENDPOINT, tokenEndpoint);
		if(tokenProvider != null)
            request.property(OAUTH2_TOKEN_PROVIDER, tokenProvider);
	}
	public static  void applyRunas(Invocation request, String token_type, String token) {
		request.property(OAUTH2_RUNAS_TOKEN_TYPE, token_type);
		request.property(OAUTH2_RUNAS_TOKEN, token);
	}

	public void filter(ClientRequestContext requestContext) throws IOException {
		AccessTokenProvider tokenProvider = (AccessTokenProvider) requestContext.getProperty(OAUTH2_TOKEN_PROVIDER);
		URI tokenEndpoint = (URI) requestContext.getProperty(OAUTH2_TOKEN_ENDPOINT);

		if(tokenProvider != null && tokenEndpoint != null) {
			OAuth2Token token = getToken(tokenProvider, tokenEndpoint);
			MultivaluedMap<String, Object> headers = requestContext.getHeaders();
            if(token != null && token.getToken() != null) {
				headers.add("Authorization", token.getToken_type() + " " + token.getToken());
			}

			String runasTokenType = (String) requestContext.getProperty(OAUTH2_RUNAS_TOKEN_TYPE);
			String runasToken = (String) requestContext.getProperty(OAUTH2_RUNAS_TOKEN);
			if(runasToken != null && runasTokenType != null) {
				headers.add("X-RunAs-Authorization", runasTokenType+" "+runasToken);
            }
		}
	}
	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		AccessTokenProvider tokenProvider = (AccessTokenProvider) requestContext.getProperty(OAUTH2_TOKEN_PROVIDER);
		URI tokenEndpoint = (URI) requestContext.getProperty(OAUTH2_TOKEN_ENDPOINT);
		if (responseContext.getStatus() == 401) {
			refreshToken(tokenProvider, tokenEndpoint);
			throw new WebApplicationException(responseContext.getStatus());
		}
	}

	private OAuth2Token refreshToken(AccessTokenProvider tokenProvider, URI tokenEndpoint) {
		if(tokenProvider == null)
			return null;

		tokenProvider.refresh(tokenEndpoint);
		return tokenProvider.get();
	}
	private OAuth2Token getToken(AccessTokenProvider tokenProvider, URI tokenEndpoint) {
		if(tokenProvider == null)
			return null;

		OAuth2Token token = tokenProvider.get();
		if(token == null || token.isExpired()) {
			return refreshToken(tokenProvider, tokenEndpoint);
		}

		return token;
	}

}
