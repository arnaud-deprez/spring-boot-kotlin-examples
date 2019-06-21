# Spring Webflux examples

Here is example on how to develop reactive web service with [Spring Webflux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html).

This currently involves:

* Annotated RestController 
* Functional endpoints
* Verify request and stub reply for WebClient
  * With [MockWebServer okhttp](https://github.com/square/okhttp/tree/master/mockwebserver)
  * With [WireMock](http://wiremock.org/)
  * TODO: a more advanced solution seems to be [MockServer](http://www.mock-server.com/)