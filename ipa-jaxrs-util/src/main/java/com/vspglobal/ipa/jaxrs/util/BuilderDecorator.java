package com.vspglobal.ipa.jaxrs.util;

import javax.ws.rs.client.Invocation;

/**
 * Created by casele on 5/13/15.
 */
public interface BuilderDecorator {
    Invocation.Builder decorate(Invocation.Builder builder);
}
