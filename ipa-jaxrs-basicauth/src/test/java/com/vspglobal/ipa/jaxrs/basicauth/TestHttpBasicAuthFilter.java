package com.vspglobal.ipa.jaxrs.basicauth;

import com.vspglobal.ipa.test.MockAPIServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by casele on 4/23/15.
 */
public class TestHttpBasicAuthFilter {
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
    @Test
    public void testBasicAuthFail() throws UnsupportedEncodingException {
        String user = "testuser";
        String pass = "testpass";
        String auth = DatatypeConverter.printBase64Binary((user + ":" + pass).getBytes("UTF-8"));

        //setup serve
        MockHome expectedHome = new MockHome();
        expectedHome.setFoo("bar");

        api.registerEntity("/home", expectedHome);
        api.expectAuthorization("/home", "Basic " + auth);

        //setup client
        Client client = ClientBuilder.newBuilder().register(new HttpBasicAuthFilter()).build();
        Invocation inv = client.target(api.resolve("/home")).request().buildGet();
        HttpBasicAuthFilter.applyProperties(inv,user,"badpass");
        Response resp =  inv.invoke();

        Assert.assertNotNull(resp);
        Assert.assertThat("http status", resp.getStatus(), is(401));

    }

    @Test
    public void testBasicAuthSuccess() throws UnsupportedEncodingException {
        String user = "testuser";
        String pass = "testpass";
        String auth = DatatypeConverter.printBase64Binary((user + ":" + pass).getBytes("UTF-8"));

        //setup serve
        MockHome expectedHome = new MockHome();
        expectedHome.setFoo("bar");

        api.registerEntity("/home", expectedHome);
        api.expectAuthorization("/home", "Basic " + auth);

        //setup client
        Client client = ClientBuilder.newBuilder().register(new HttpBasicAuthFilter()).build();
        Invocation inv = client.target(api.resolve("/home")).request().buildGet();
        HttpBasicAuthFilter.applyProperties(inv,user,pass);
        Response resp =  inv.invoke();

        Assert.assertNotNull(resp);
        Assert.assertThat("http status", resp.getStatus(), is(200));
    }

    @Test
    public void testBasicAuthNone() throws UnsupportedEncodingException {

        //setup serve
        MockHome expectedHome = new MockHome();
        expectedHome.setFoo("bar");

        api.registerEntity("/home", expectedHome);

        //setup client
        Client client = ClientBuilder.newBuilder().register(new HttpBasicAuthFilter()).build();
        Invocation inv = client.target(api.resolve("/home")).request().buildGet();
        Response resp =  inv.invoke();

        Assert.assertNotNull(resp);
        Assert.assertThat("http status", resp.getStatus(), is(200));
    }

}
