![IPA](logo.png "IPA Logo")
<br/>
[![Build Status](https://travis-ci.org/vspglobal/ipa.svg)](https://travis-ci.org/vspglobal/ipa)
# (Improved Programming with APIs)
This project contains various modules to aid in the development and consumption of APIs.   Follow the link on each module for details on how to use.

**Module** | **Overview**
-----------|--------------
 [ipa-jaxrs-basicauth](ipa-jaxrs-basicauth) |  JAX-RS client filter to apply HTTP basic auth header on requests 
 [ipa-jaxrs-oauth2](ipa-jaxrs-oauth2) |  JAX-RS client filter to retrieve OAuth2 token and apply it as a header on requests 
 [ipa-jaxrs-hystrix](ipa-jaxrs-hystrix) | Bridge between JAX-RS and [Hystrix](https://github.com/Netflix/Hystrix) to provide circuit breakers, caching and metrics from JAX-RS client invocations
 [ipa-jaxrs-util](ipa-jaxrs-util) | Miscellaneous JAX-RS utilities
 [ipa-domain](ipa-domain) |  Contains domain objects that are shared by the various modules.  This artifact has no dependencies. 
 [ipa-test](ipa-test) |  Contains utilities for testing APIs.


# Support
Please [create an issue](https://github.com/vspglobal/ipa/issues) for any bugs you come across or feature requests.

# Contributing
[Pull requests](http://help.github.com/send-pull-requests) are welcome; see the [contributor guidelines](CONTRIBUTING.md) for details.

# License
IP is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

For additional information, see the [LICENSE](LICENSE) file.
