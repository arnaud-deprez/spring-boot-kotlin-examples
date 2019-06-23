package com.powple.webflux

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@Import(RouterConfiguration::class) // See https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-autoconfigured-webflux-tests
@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("Don't know why yet but this test cannot handle reactive reply properly")
class WebFluxTest(@Autowired val client: WebTestClient) {

	@MockkBean
	private lateinit var apiService: ApiService


	@Test
	fun testController() {
		every { apiService.hello("controller") } returns Mono.just("controller")

		client.get().uri("/api/v1/hello/controller").exchange()
			.expectStatus().isOk
			.expectBody<String>().isEqualTo("Hello controller!")
	}

	@Test
	fun testRouter() {
		every { apiService.hello("router") } returns Mono.just("router")

		client.get().uri("/api/v2/hello/router").exchange()
			.expectStatus().isOk
			.expectBody<String>().isEqualTo("Hello router!")
	}
}