import com.vspglobal.ipa.domain.OAuth2Token;
import com.vspglobal.ipa.jaxrs.oauth2.AccessTokenProvider;
import com.vspglobal.ipa.jaxrs.oauth2.ClientAccessTokenProvider;
import com.vspglobal.ipa.jaxrs.oauth2.OAuth2BearerTokenFilter;
import com.vspglobal.ipa.test.MockAPIServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by casele on 4/23/15.
 */
public class TestOAuth2BearerTokenFilter {
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

    private MockHome newHome() {
        MockHome home = new MockHome();
        home.setFoo("bar");
        return home;
    }

    private OAuth2Token newToken() {
        OAuth2Token tok = new OAuth2Token();
        tok.setToken("tok"+System.currentTimeMillis());
        tok.setToken_type(OAuth2Token.TOKEN_TYPE_BEARER);
        return tok;
    }

    @Test
    public void testOAuth2ClientCreds() {
        //setup
        OAuth2Token expectedToken = newToken();
        api.registerEntity("/as/oauth/token", expectedToken);

        MockHome expectedHome = newHome();
        api.registerEntity("/home", expectedHome);
        api.expectAuthorization("/home", expectedToken.getToken_type() + " " + expectedToken.getToken());


        // make sure token provider is empty first
        AccessTokenProvider tokenProvider = ClientAccessTokenProvider.get("test_client", "test_secret");

        //setup client
        Client client = ClientBuilder.newBuilder().register(new OAuth2BearerTokenFilter()).build();
        Invocation inv = client.target(api.resolve("/home")).request().buildGet();
        OAuth2BearerTokenFilter.applyProperties(inv,api.resolve("/as/oauth/token"), tokenProvider);
        Response resp =  inv.invoke();

        // assert
        Assert.assertNotNull(resp);
        Assert.assertThat("http status", resp.getStatus(), is(200));

        OAuth2Token tok = tokenProvider.get();
        Assert.assertNotNull("token", tok);
        Assert.assertThat("token.token",tok.getToken(),is(expectedToken.getToken()));
    }

    @Test
    public void testOAuth2ExpiredClientCreds() {
        //setup
        OAuth2Token expectedToken = newToken();
        api.registerEntity("/as/oauth/token", expectedToken);

        MockHome expectedHome = newHome();
        api.registerEntity("/home", expectedHome);
        api.expectAuthorization("/home", expectedToken.getToken_type() + " " + expectedToken.getToken());


        // make sure token provider is empty first
        AccessTokenProvider tokenProvider = ClientAccessTokenProvider.get("test_client", "test_secret");

        //setup client
        Client client = ClientBuilder.newBuilder().register(new OAuth2BearerTokenFilter()).build();
        Invocation inv = client.target(api.resolve("/home")).request().buildGet();
        OAuth2BearerTokenFilter.applyProperties(inv,api.resolve("/as/oauth/token"), tokenProvider);
        Response resp =  inv.invoke();

        // assert
        Assert.assertNotNull(resp);
        Assert.assertThat("http status", resp.getStatus(), is(200));

        OAuth2Token tok = tokenProvider.get();
        Assert.assertNotNull("token", tok);
        Assert.assertThat("token.token",tok.getToken(),is(expectedToken.getToken()));

        // expire the token
        tok.setExpires_in(0);

        // register next token
        OAuth2Token expectedToken2 = newToken();
        api.registerEntity("/as/oauth/token", expectedToken2);
        api.expectAuthorization("/home", expectedToken2.getToken_type()+" "+expectedToken2.getToken());

        //execute again
        Client client2 = ClientBuilder.newBuilder().register(new OAuth2BearerTokenFilter()).build();
        Invocation inv2 = client2.target(api.resolve("/home")).request().buildGet();
        OAuth2BearerTokenFilter.applyProperties(inv2,api.resolve("/as/oauth/token"), tokenProvider);
        Response resp2 =  inv2.invoke();

        Assert.assertNotNull(resp2);
        Assert.assertThat("http status", resp2.getStatus(), is(200));

        OAuth2Token tok2 = tokenProvider.get();
        Assert.assertNotNull("token2", tok2);
        Assert.assertThat("token2.token",tok2.getToken(),is(expectedToken2.getToken()));
    }

}
