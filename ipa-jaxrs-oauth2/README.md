# JAX-RS OAuth 2.0
JAX-RS client filter to retrieve OAuth2 token and apply it as a header on requests 

# How To Use

Pull in the artifact via the following maven coordinates:

```
<groupId>com.vspglobal.ipa</groupId>
<artifactId>ipa-jaxrs-oauth2</artifactId>
<version>1.4</version>
```

First you'll need to create a token provider based on which OAuth 2.0 grant type is needed.  Currently, only client credentials, resource owner credentials and authorization code are supported.

```
AccessTokenProvider myTokenProvider = ClientAccessTokenProvider.get("my_clientid", "xxxxxxxxxxx-secret-xxxxxxxxx", "scope1", "scope2");
```

Then apply the token URI and tokenProvider to the request.  The filter will then make sure it has a valid token (generating one if not or refreshing if expired) and then adding the token as an `Authorization` header.

```
    OAuth2BearerTokenFilter.applyProperties(request, myTokenUri, myTokenProvider);
```
