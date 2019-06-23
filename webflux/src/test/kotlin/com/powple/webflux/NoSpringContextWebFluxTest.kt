package com.powple.webflux

import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.client.WebClient

class NoSpringContextWebFluxTest {

    @Test
    fun testController() {
        val controller = ApiRestController(ApiServiceImpl(WebClient.builder()))
        val client = WebTestClient.bindToController(controller).build()
        client.get()
            .uri("/api/v1/hello/foo")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("Hello foo!")
    }

    @Test
    fun testRouter() {
        val router = RouterConfiguration().router(ApiServiceImpl(WebClient.builder()))
        val client = WebTestClient.bindToRouterFunction(router).build()
        client.get()
            .uri("/api/v2/hello/foo")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("Hello foo!")
    }
}