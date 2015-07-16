package com.vspglobal.ipa.domain;


import java.io.Serializable;
import java.util.*;

public class OAuth2Token implements Serializable {
	public static String TOKEN_TYPE_BEARER = "Bearer";
	/**
	 * 
	 */
	private static final long serialVersionUID = 6683127979219332164L;
	
	private String access_token;
	private String refresh_token;
	private String token_type;
	private long expiration;
	private String client_id;
	
	private String error;
	private String error_description;
	
	private String scope;
	private final List<String> scopes = new LinkedList<String>();
	
	
	private String principal;
	private Map<String,Object> additionalAttributes;


	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}


	public String getPrincipal() {
		return principal;
	}
	
	public void setPrincipal(String principal) {
		this.principal= principal;
	}

	
	public String toString() {
		if(this.getError() != null && !this.getError().isEmpty()) {
			return "error: "+this.getError()+" error_description:"+this.getError_description();
		} else {
			StringBuilder b = new StringBuilder();
			
			b.append("client_id: ").append(this.client_id)
			 .append(" scope:[").append(this.getScope()).append("]")
			 .append(" expires_in:").append(this.getExpires_in())
			 .append(" principal:").append(getPrincipal());

			return b.toString();
		}
	}



	
	public List<String> getScopes() {
		return this.scopes;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
		
		this.scopes.clear();
		if(scope != null) {
			for(String s : this.scope.split("\\s+")) {
				try {
					scopes.add(s);
				} catch (Exception ex) {}
			}
		}
	}
	
	public String getToken() {
		return access_token;
	}
	
	public void setToken(String access_token) {
		this.access_token = access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public int getExpires_in() {
		return (int) Math.max(0, (this.expiration - System.currentTimeMillis())/1000);
	}
	public void setExpires_in(int expires_in) {
		this.expiration = System.currentTimeMillis() + (expires_in*1000);
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public boolean isExpired() {
		return getExpires_in() <= 5; // about to expire...
	}

	public Map<String,Object> getAdditionalAttributes() {
		if(this.additionalAttributes == null) {
			this.additionalAttributes = new HashMap<String, Object>();
		}
		return additionalAttributes;
	}

	public void setAdditionalAttributes(Map<String,Object> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}


}
