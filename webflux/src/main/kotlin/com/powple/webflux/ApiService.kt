package com.powple.webflux

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

interface ApiService {
    fun hello(name: String): Mono<String>
    fun status(app: String): Mono<String>
}

class ApiServiceImpl(webClientBuilder: WebClient.Builder): ApiService {

    private val client = webClientBuilder.build()

    override fun hello(name: String) =
        Mono.just("Hello $name!")

    override fun status(app: String) =
        client.get()
            .uri("http://$app/actuator/health")
            .retrieve()
            .bodyToMono<String>()
}