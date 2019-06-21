package com.powple.webflux

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class ApiService(webClientBuilder: WebClient.Builder) {

    private val client = webClientBuilder.build()

    fun hello(name: String) =
        Mono.just("Hello $name!")

    fun status(app: String) =
        client.get()
            .uri("http://$app/actuator/health")
            .retrieve()
            .bodyToMono<String>()
}