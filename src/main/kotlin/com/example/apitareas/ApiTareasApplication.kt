package com.example.apitareas

import com.example.apitareas.security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class ApiTareasApplication

fun main(args: Array<String>) {
	runApplication<ApiTareasApplication>(*args)
}
