package com.vspglobal.ipa.jaxrs.hystrix;
import static org.hamcrest.CoreMatchers.is;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.vspglobal.ipa.jaxrs.hystrix.util.HystrixCommandSetterUtil;
import com.vspglobal.ipa.test.MockAPIServer;

public class TestInvocationCommand {
    // server
    private MockAPIServer api;
    private Client client; 

    @Before
    public void setupServer() throws Exception {
        api = new MockAPIServer();
        api.start();
        client = ClientBuilder.newBuilder().build();
    }

    @After
    public void cleanupServer() throws Exception {
        api.stop();
    }

    @Test
    public void test404() {
        try {
            Invocation inv = client.target(api.resolve("/home")).request().buildGet();
            InvocationCommand command = new InvocationCommand(HystrixCommandSetterUtil.getSetter("test404",null), inv);
            command.execute();

            Assert.fail("Should have throws HystrixBadRequestException");
        } catch (HystrixBadRequestException hystrixException) {
        	WebApplicationException wae = (WebApplicationException) hystrixException.getCause();
        	Assert.assertThat("status",wae.getResponse().getStatus(),is(404));
        } catch (Exception ex) {
            Assert.fail("Unexpected Exception: "+ex);
            ex.printStackTrace();
        }
    }
    
    @Test
    public void test400() {
    	api.registerStatus("/home", 400);
    	
        try {
		    Invocation inv = client.target(api.resolve("/home")).request().buildGet();
		    InvocationCommand command = new InvocationCommand(HystrixCommandSetterUtil.getSetter("test400",null), inv);
		    command.execute();
		    Assert.fail("Should have throws HystrixBadRequestException");
        
        } catch (HystrixBadRequestException hystrixException) {
        	WebApplicationException wae = (WebApplicationException) hystrixException.getCause();
        	Assert.assertThat("status",wae.getResponse().getStatus(),is(400));
        } catch (Exception ex) {
            Assert.fail("Unexpected Exception: "+ex);
            ex.printStackTrace();
        }
    }
    
    @Test
    public void test500() {
        api.registerStatus("/500error", 500);

        try {
            Invocation inv = client.target(api.resolve("/500error")).request().buildGet();
            InvocationCommand command = new InvocationCommand(HystrixCommandSetterUtil.getSetter("500error",null), inv);
            command.execute();
            
            Assert.fail("Should have throws HystrixRuntimeException");
        } catch (HystrixRuntimeException hystrixRuntimeException) {
        	WebApplicationException wae = (WebApplicationException) hystrixRuntimeException.getCause();
            Assert.assertThat("status",wae.getResponse().getStatus(),is(500));
        } catch (Exception ex) {
            Assert.fail("Unexpected Exception: "+ex);
            ex.printStackTrace();
        }
    }

}
