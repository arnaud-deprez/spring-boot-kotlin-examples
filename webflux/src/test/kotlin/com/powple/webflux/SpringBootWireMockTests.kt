package com.powple.webflux

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringBootWireMockTests(@Autowired val client: WebTestClient) {

    companion object {
        val wireMockServer = WireMockServer(wireMockConfig().dynamicPort())
    }

    @TestConfiguration
    class TestContext {
        // Override the webClientBuilder
        @Bean
        fun webClientBuilder() =
            WebClient.builder()
                .filter { request, next ->
                    val filtered = ClientRequest.from(request)
                        .url(URI.create(wireMockServer.baseUrl()).resolve(request.url().path))
                        .build()

                    next.exchange(filtered)
                }
    }

    @BeforeAll
    internal fun beforeAll() {
        wireMockServer.start()
        configureFor(wireMockServer.port())
    }

    @AfterAll
    internal fun afterAll() {
        wireMockServer.stop()
    }

    @AfterEach
    internal fun afterEach() {
        reset()
    }

    @Test
    fun testApiV1Hello() {
        client.get()
            .uri("/api/v1/hello/world")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("Hello world!")
    }

    @Test
    fun testApiV2Hello() {
        client.get()
            .uri("/api/v2/hello/world")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("Hello world!")
    }

    @Test
    fun testV1Status() {
        val bodyResponse = """{"status": "UP"}"""

        //init server
        givenThat(get(urlEqualTo("/actuator/health"))
            .willReturn(
                aResponse().withStatus(200)
                    .withBody(bodyResponse)
            ))

        client.get()
            .uri("/api/v1/status/iam")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo(bodyResponse)

        //assert request on server
        verify(1, getRequestedFor(urlEqualTo("/actuator/health")))
    }

    @Test
    fun testV2Status() {
        val bodyResponse = """{"status": "UP"}"""

        //init server
        givenThat(get(urlEqualTo("/actuator/health"))
            .willReturn(
                aResponse().withStatus(200)
                    .withBody(bodyResponse)
            ))

        client.get()
            .uri("/api/v2/status/iam")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo(bodyResponse)

        //assert request on server
        verify(1, getRequestedFor(urlEqualTo("/actuator/health")))
    }
}
