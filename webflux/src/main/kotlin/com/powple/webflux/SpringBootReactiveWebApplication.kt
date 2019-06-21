package com.powple.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class SpringBootReactiveWebExampleApplication {
	@Bean
	fun apiService(wcBuilder: WebClient.Builder) = ApiService(wcBuilder)
}

fun main(args: Array<String>) {
	runApplication<SpringBootReactiveWebExampleApplication>(*args)
}
