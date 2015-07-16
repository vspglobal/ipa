import com.vspglobal.ipa.domain.OAuth2Token;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by casele on 4/28/15.
 */
public class TestOAuth2Token {
    @Test
    public void testExpires() throws InterruptedException {
        OAuth2Token tok = new OAuth2Token();

        Assert.assertTrue("scope is expired", tok.isExpired());

        tok.setExpires_in(10);

        Assert.assertFalse("scope is not expired", tok.isExpired());

        Thread.currentThread().sleep(5000);

        Assert.assertTrue("scope is expired", tok.isExpired());
    }
    @Test
    public void testScopeEmpty() {
        OAuth2Token tok = new OAuth2Token();
        Assert.assertThat("scope", tok.getScopes().size(), is(0));
    }

    @Test
    public void testScopes() {
        OAuth2Token tok = new OAuth2Token();
        tok.setScope("foo bar baz");
        Assert.assertThat("scope", tok.getScopes().size(), is(3));

        Assert.assertTrue("scope has foo", tok.getScopes().contains("foo"));
        Assert.assertTrue("scope has bar", tok.getScopes().contains("bar"));
        Assert.assertTrue("scope has baz", tok.getScopes().contains("baz"));
    }
}
