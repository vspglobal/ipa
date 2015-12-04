# JAX-RS Hystrix
Bridge between JAX-RS and [Hystrix](https://github.com/Netflix/Hystrix) to provide circuit breakers, caching and metrics from JAX-RS client invocations

# How To Use

Pull in the artifact via the following maven coordinates:

```
<groupId>com.vspglobal.ipa</groupId>
<artifactId>ipa-jaxrs-hystrix</artifactId>
<version>1.6.3</version>
```

Next, [configure](https://github.com/Netflix/Hystrix/wiki/Configuration) the Hystrix setter:

```
HystrixCommand.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MyCommandGroupName"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("MyCommandName"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter());
```

Then you'll want to create an `Invocation.Builder` like normal and then decorate it with `HystrixInvocation`:

```
Invocation.Builder req =   ClientBuilder.newBuilder().build()
                                        .target(myUri).buildGet();
                                        
req = HystrixInvocation.builder( req );
```

Then just use the Invocation.Builder like usual.
