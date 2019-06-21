package com.powple.webflux

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfiguration {
    @Bean
    fun router(apiService: ApiService) = router {
        "/api/v2".nest {
            GET("/hello/{name}") { req ->
                apiService.hello(req.pathVariable("name"))
                    .flatMap { s -> ok().syncBody(s) }
            }
            GET("/status/{app}") { req ->
                apiService.status(req.pathVariable("app"))
                    .flatMap { s -> ok().syncBody(s) }
            }
        }
    }
}