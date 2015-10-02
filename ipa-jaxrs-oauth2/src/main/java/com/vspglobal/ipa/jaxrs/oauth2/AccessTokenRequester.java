package com.vspglobal.ipa.jaxrs.oauth2;

import java.net.URI;
import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vspglobal.ipa.domain.OAuth2Token;
import com.vspglobal.ipa.jaxrs.basicauth.HttpBasicAuthFilter;
import com.vspglobal.ipa.jaxrs.util.BuilderDecorator;
import com.vspglobal.ipa.jaxrs.util.pool.ClientGenObjectPool;
import com.vspglobal.ipa.jaxrs.util.pool.ClientPoolConfig;
import com.vspglobal.ipa.jaxrs.util.pool.ClientPoolObjectFactory;

public class AccessTokenRequester {
	private Logger log = LoggerFactory.getLogger(getClass());
	

	private static final Object[] providers = new Object[] {new AccessTokenReader(), new HttpBasicAuthFilter()};
	private static BuilderDecorator builderDecorator = null;


	private Map<String,String> formparams = new Hashtable<String, String>();
	private String client_id;
	private String client_secret;
	private GrantType grant_type;

	
	private static ClientGenObjectPool clientPool = new ClientGenObjectPool(new ClientPoolObjectFactory(providers), new ClientPoolConfig());
	

	private URI tokenEndpoint;

	
	public AccessTokenRequester() {
	}

	public static void registerBuilderDecorator(BuilderDecorator builderDecorator) {
		AccessTokenRequester.builderDecorator = builderDecorator;
	}

	public AccessTokenRequester client_id(String client_id) {
		this.client_id = client_id;
		return this;
	}
	public AccessTokenRequester client_secret(String client_secret) {
		this.client_secret = client_secret;
		return this;
	}
	public AccessTokenRequester grant_type(GrantType grant_type) {
		this.grant_type = grant_type;
		return this;
	}
	public AccessTokenRequester token(String token) {
		this.formparams.put("token", token);
		return this;
	}
	public AccessTokenRequester code(String code) {
		this.formparams.put("code", code);
		return this;
	}
	public AccessTokenRequester redirect_uri(String redirect_uri) {
		this.formparams.put("redirect_uri", redirect_uri);
		return this;
	}
	public AccessTokenRequester refresh_token(String token) {
        if(token == null)
            throw new IllegalArgumentException("no refresh_token was provided!");
		this.formparams.put("refresh_token", token);
		return this;
	}
	public AccessTokenRequester scope(String scope) {
		this.formparams.put("scope", scope);
		return this;
	}
	public AccessTokenRequester username(String username) {
		this.formparams.put("username", username);
		return this;
	}
	public AccessTokenRequester validator_id(String validator_id) {
		this.formparams.put("validator_id", validator_id);
		return this;
	}
	public AccessTokenRequester assertion(String assertion) {
		this.formparams.put("assertion", assertion);
		return this;
	}
	public AccessTokenRequester password(String password) {
		this.formparams.put("password", password);
		return this;
	}

	public OAuth2Token request() {
		final String client_id = this.client_id;
		final String client_secret = this.client_secret;

		
		OAuth2Token tok;
		Response response=null;
		Client client  = null;
		try {
			long start = System.currentTimeMillis();
			client = clientPool.borrowObject();
			Invocation req;

			if(grant_type == GrantType.GET_TOKEN) {
				WebTarget webTarget = client.target(UriBuilder.fromUri(tokenEndpoint).path("/" + formparams.get("token")));
				Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
				if(builderDecorator != null) {
					builder = builderDecorator.decorate(builder);
				}
				req = builder.buildGet();

			} else {
				WebTarget webTarget = client.target(UriBuilder.fromUri(tokenEndpoint));
				Form form = new Form();
				for(Map.Entry<String, String> e : formparams.entrySet()) {
					form.param(e.getKey(), e.getValue());
				}
				
				if (client_id != null && !client_id.isEmpty() && (client_secret == null || client_secret.isEmpty())) {
					form.param("client_id", client_id);
				}
				
				form.param("grant_type", grant_type.toString());
				

				Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
				if(builderDecorator != null) {
					builder = builderDecorator.decorate(builder);
				}
				req = builder.buildPost(Entity.form(form));
			}


			if(client_id != null && !client_id.isEmpty() && client_secret != null && !client_secret.isEmpty()) {
				HttpBasicAuthFilter.applyProperties(req, client_id, client_secret);
			}

			response = req.invoke();

            tok = response.readEntity(MappableOAuth2Token.class);

			if(grant_type == GrantType.CLIENT_CREDENTIALS) {
				tok.setClient_id(client_id);
				tok.setScope(formparams.get("scope"));

			}
			
			log.info("GRANT_REQUEST("+this.grant_type.name()+"): resp_time="+(System.currentTimeMillis() - start));
		} catch (Exception e) {
			log.error("token generation error",e);
			tok = new OAuth2Token();
			tok.setError("invalid_request");
			tok.setError_description(e.toString());
			if(response != null) {
				response.close();
			}
		}finally{
			try {
				clientPool.returnObject(client);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Error in returning the object to pool", e );
			}
			
		}
		
		if(tok.getError() != null && tok.getError().length()>0) {
			log.error("Error requesting token: "+tok.getError()+"="+tok.getError_description());
		}
		
		return tok;
	}
	
	public AccessTokenRequester tokenEndpoint(URI tokenEndpoint) {
		this.tokenEndpoint= tokenEndpoint;
		return this;
	}
	
	
	
}
