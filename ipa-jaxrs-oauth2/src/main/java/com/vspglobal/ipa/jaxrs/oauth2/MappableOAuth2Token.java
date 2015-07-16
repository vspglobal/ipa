package com.vspglobal.ipa.jaxrs.oauth2;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.vspglobal.ipa.domain.OAuth2Token;

import java.util.Map;

/**
 * Created by casele on 6/9/15.
 */
public class MappableOAuth2Token  extends OAuth2Token{

    // "any getter" needed for serialization
    @JsonAnyGetter
    public Map<String,Object> any() {
        return getAdditionalAttributes();
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        getAdditionalAttributes().put(name,value);
    }
}
