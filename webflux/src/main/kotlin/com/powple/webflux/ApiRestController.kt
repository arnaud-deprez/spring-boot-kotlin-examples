package com.powple.webflux

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1")
class ApiRestController(@Autowired private val apiService: ApiService) {

    @GetMapping("/hello/{name}")
    @ResponseBody
    fun hello(@PathVariable name: String) = apiService.hello(name)

    @GetMapping("/status/{app}")
    @ResponseBody
    fun status(@PathVariable app: String) = apiService.status(app)
}