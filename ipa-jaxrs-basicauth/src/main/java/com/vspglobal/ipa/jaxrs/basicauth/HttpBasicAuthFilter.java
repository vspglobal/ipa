package com.vspglobal.ipa.jaxrs.basicauth;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Provider
public class HttpBasicAuthFilter implements ClientRequestFilter {

	private static final String HTTP_BASIC_USERNAME = "http.basic.username";
	private static final String HTTP_BASIC_PASSWORD = "http.basic.password";

	public HttpBasicAuthFilter() {
	}

	public static  void applyProperties(Invocation request, String username, String password) {
		request.property(HTTP_BASIC_USERNAME, username);
		request.property(HTTP_BASIC_PASSWORD, password);
	}

	public void filter(ClientRequestContext requestContext) throws IOException {
		String username = (String) requestContext .getProperty(HTTP_BASIC_USERNAME);
		String password = (String) requestContext .getProperty(HTTP_BASIC_PASSWORD);
		if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
			MultivaluedMap<String, Object> headers = requestContext.getHeaders();
			final String basicAuthentication = getBasicAuthentication(username,password);
			headers.add("Authorization", basicAuthentication);
		}

	}

	private String getBasicAuthentication(String username, String password) {
		String token = username + ":" + password;
		try {
			return "Basic "
					+ DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Cannot encode with UTF-8", ex);
		}
	}
}
