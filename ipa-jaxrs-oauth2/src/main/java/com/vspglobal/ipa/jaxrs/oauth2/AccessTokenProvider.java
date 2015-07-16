package com.vspglobal.ipa.jaxrs.oauth2;

import com.vspglobal.ipa.domain.OAuth2Token;
import com.vspglobal.ipa.jaxrs.util.BuilderDecorator;

import java.net.URI;

public interface AccessTokenProvider {
	public OAuth2Token get();

	public void refresh(URI tokenEndpoint);
	
}
