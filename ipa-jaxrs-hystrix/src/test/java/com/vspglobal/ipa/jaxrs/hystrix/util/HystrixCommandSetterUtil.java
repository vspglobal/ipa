package com.vspglobal.ipa.jaxrs.hystrix.util;

import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class HystrixCommandSetterUtil {
	
	public static HystrixCommand.Setter getSetter(String cmd, Integer requestTimeoutInSeconds){
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        if(requestTimeoutInSeconds != null) {
            commandProperties.withExecutionTimeoutInMilliseconds((int) TimeUnit.MILLISECONDS.convert(requestTimeoutInSeconds, TimeUnit.SECONDS));
        }
        
        HystrixCommand.Setter setter =
                HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RestInvocation"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey(cmd))
                        .andCommandPropertiesDefaults(commandProperties);
        return setter;
	}

}
