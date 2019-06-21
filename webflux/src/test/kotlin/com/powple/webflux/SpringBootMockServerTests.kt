package com.powple.webflux

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.WebClient

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringBootMockServerTests(@Autowired val client: WebTestClient) {

    companion object {
        val mockWebServer = MockWebServer()
    }

    @AfterAll
    internal fun afterAll() {
        mockWebServer.shutdown()
    }

    @TestConfiguration
    class TestContext {
        // Override the webClientBuilder
        @Bean
        fun webClientBuilder() =
            WebClient.builder()
                .filter { request, next ->
                    val filtered = ClientRequest.from(request)
                        .url(mockWebServer.url("/").uri())
                        .build()

                    next.exchange(filtered)
                }
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
    fun testApiV1Status() {
        val bodyResponse = """{"status": "UP"}"""
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(bodyResponse)
        )

        client.get()
            .uri("/api/v1/status/iam")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo(bodyResponse)

        val recordedRequest = mockWebServer.takeRequest()
        assertAll("recordedRequest", {
            assertEquals("GET", recordedRequest.method)
        })
    }

    @Test
    fun testApiV2Status() {
        val bodyResponse = """{"status": "UP"}"""
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(bodyResponse)
        )

        client.get()
            .uri("/api/v2/status/iam")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo(bodyResponse)

        val recordedRequest = mockWebServer.takeRequest()
        assertAll("recordedRequest", {
            assertEquals("GET", recordedRequest.method)
        })
    }
}
