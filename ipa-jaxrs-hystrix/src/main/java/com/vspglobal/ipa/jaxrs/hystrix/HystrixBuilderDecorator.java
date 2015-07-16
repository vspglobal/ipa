package com.vspglobal.ipa.jaxrs.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.vspglobal.ipa.jaxrs.util.BuilderDecorator;

import javax.ws.rs.client.Invocation;

/**
 * Created by casele on 5/13/15.
 */
public class HystrixBuilderDecorator implements BuilderDecorator {
    private final HystrixCommand.Setter setter;

    public HystrixBuilderDecorator(HystrixCommand.Setter setter) {
        this.setter = setter;
    }

    @Override
    public Invocation.Builder decorate(Invocation.Builder builder) {
        return HystrixInvocation.builder(builder,setter);
    }
}
