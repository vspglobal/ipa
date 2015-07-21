package com.vspglobal.ipa.jaxrs.hystrix;

import static org.hamcrest.CoreMatchers.is;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.vspglobal.ipa.test.MockAPIServer;

/**
 * Created by casele on 4/28/15.
 */
public class TestHystrixInvocation {
    // server
    private MockAPIServer api;

    @Before
    public void setupServer() throws Exception {
        api = new MockAPIServer();
        api.start();
    }

    @After
    public void cleanupServer() throws Exception {
        api.stop();
    }

    private HystrixCommand.Setter getSetter(String cmd, Integer requestTimeoutInSeconds) {
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

    @Test
    public void test404() {
        try {
            //setup client
            Client client = ClientBuilder.newBuilder().build();
            Invocation inv = client.target(api.resolve("/home")).request().buildGet();
            Response resp =  HystrixInvocation.build(inv, getSetter("test404",null)).invoke();

            Assert.fail("Should have throws WebApplicationException");
        } catch (WebApplicationException wae) {
            Assert.assertThat("status",wae.getResponse().getStatus(),is(404));
        } catch (Exception ex) {
            Assert.fail("Unexpected Exception: "+ex);
            ex.printStackTrace();
        }
    }

    @Test
    public void test500() {
        api.registerStatus("/force500", 500);

        try {
            Client client = ClientBuilder.newBuilder().build();
            Invocation inv = client.target(api.resolve("/force500")).request().buildGet();
            Response resp =  HystrixInvocation.build(inv, getSetter("test500",null)).invoke();

            Assert.fail("Should have throws WebApplicationException");
        } catch (WebApplicationException wae) {
            Assert.assertThat("status",wae.getResponse().getStatus(),is(500));
        } catch (Exception ex) {
            Assert.fail("Unexpected Exception: "+ex);
            ex.printStackTrace();
        }
    }

    @Test
    public void testTimeout() {
        //setup
        api.registerEntity("/timeout", "foo")
                .withDelayInMillis(3000);

        try {
            Client client = ClientBuilder.newBuilder().build();
            Invocation inv = client.target(api.resolve("/timeout")).request().buildGet();
            Response resp =  HystrixInvocation.build(inv, getSetter("testTimeout",2)).invoke();

            Assert.fail("Should have throws WebApplicationException");
        } catch (WebApplicationException wae) {
            Assert.assertThat("status",wae.getResponse().getStatus(),is(504));
        } catch (Exception ex) {
            Assert.fail("Unexpected Exception: "+ex);
            ex.printStackTrace();
        }
    }

    @Test
    public void testCircuitBreakerAndRecovery() {
        //setup
        api.registerEntity("/circuitBreaker", "foo")
                .withDelayInMillis(2000);

        for(int i=0; i<10; i++) {

            try {
                Client client = ClientBuilder.newBuilder().build();
                Invocation inv = client.target(api.resolve("/circuitBreaker")).request().buildGet();
                Response resp =  HystrixInvocation.build(inv, getSetter("testCircuit",1)).invoke();

                Assert.fail("Should have throws WebApplicationException");
            } catch (WebApplicationException wae) {
                if(i == 0) {
                    Assert.assertThat("status-"+i, wae.getResponse().getStatus(), is(504));
                    Assert.assertThat("message-"+i, wae.getMessage(), is("HTTP 504 Gateway Timeout"));
                } else {
                    Assert.assertThat("status-"+i, wae.getResponse().getStatus(), is(503));
                    Assert.assertThat("message-"+i, wae.getMessage(), is("HTTP 503 Service Unavailable"));
                }

            } catch (Exception ex) {
                Assert.fail("Unexpected Exception: "+ex);
                ex.printStackTrace();
            }
        }

        // wait for recovery
        try {
            Thread.currentThread().sleep(5000);
        } catch (Exception ex) {}

        // test for recovery
        api.withDelayInMillis(500);

        Client client = ClientBuilder.newBuilder().build();
        Invocation inv = client.target(api.resolve("/circuitBreaker")).request().buildGet();
        Response resp =  HystrixInvocation.build(inv, getSetter("testCircuit",1)).invoke();

        Assert.assertNotNull(resp);
        Assert.assertThat("status",resp.getStatus(),is(200));
    }


    @Test
    public void testHome() {
        //setup
        api.registerEntity("/home", "blah");

        //execute
        Client client = ClientBuilder.newBuilder().build();
        Invocation inv = client.target(api.resolve("/home")).request().buildGet();
        Response resp =  HystrixInvocation.build(inv, getSetter("testHome",null)).invoke();

        // assert
        Assert.assertNotNull(resp);
        Assert.assertThat("status",resp.getStatus(),is(200));
    }
    @Test
    public void testHomeAsync() {
        //setup
        api.registerEntity("/home", "blah");

        //execute
        Client client = ClientBuilder.newBuilder().build();
        Invocation inv = client.target(api.resolve("/home")).request().buildGet();
        Future<Response> respF =  HystrixInvocation.build(inv, getSetter("testHomeAsync",null)).submit();

        // assert
        try {
            Response resp = respF.get();
            Assert.assertNotNull(resp);
            Assert.assertThat("status",resp.getStatus(),is(200));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
