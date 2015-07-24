# JAX-RS Basic Auth
This artifact contains a client filter to apply an `Authorization` header with basic user/password credentials

# How To Use

Pull in the artifact via the following maven coordinates:

```
<groupId>com.vspglobal.ipa</groupId>
<artifactId>ipa-jaxrs-basicauth</artifactId>
<version>1.5</version>
```

Create a client with the filter registered as a provider:

```
ClientBuilder clientBuilder = ClientBuilder.newBuilder().register( new HttpBasicAuthFilter() );
```

Next, proceed as usual to create an `Invocation` and then apply the username and password properties to the invocation:

```
Invocation request = ...
HttpBasicAuthFilter.applyProperties(request, "myuser", "mypass");
```

That's it...then when you execute the invocation, the header will be added.

