import com.vspglobal.ipa.domain.OAuth2Token;
import com.vspglobal.ipa.jaxrs.oauth2.AccessTokenReader;
import com.vspglobal.ipa.jaxrs.oauth2.MappableOAuth2Token;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by casele on 6/9/15.
 */
public class TestAccessTokenReader {


    @Test
    public void readWithAdditionalAttributes() throws IOException {
        AccessTokenReader atr = new AccessTokenReader();

        InputStream in = getClass().getResourceAsStream("provider_token.json");

        OAuth2Token tok = atr.readFrom(MappableOAuth2Token.class,null,null,null,null,in);

        assertNotNull("Token",tok);

        assertThat("Token principal",tok.getPrincipal(),is("11111/test@test.com"));

        assertThat("Token user_type",(String)tok.getAdditionalAttributes().get("user_type"),is("AFFILIATE"));

        assertThat("Token practice_tax_id",((Map<String,String>)tok.getAdditionalAttributes().get("provider_constraint")).get("provider_tax_id"),is("111111111"));
    }
}
