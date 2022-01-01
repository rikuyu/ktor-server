package com.example

import com.example.JsonService.bookRoute
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

@KtorExperimentalLocationsAPI
fun main(args: Array<String>) {
    val server = embeddedServer(Netty, commandLineEnvironment(args))
    server.start(true)
}

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(Locations)
    install(ContentNegotiation) {
        jackson()
    }

    routing {
        bookRoute()
    }

    DatabaseService.apply {
        val url = environment.config.property("ktor.database.url").getString()
        val driver = environment.config.property("ktor.database.driver").getString()
        val user = environment.config.property("ktor.database.user").getString()
        val password = environment.config.property("ktor.database.password").getString()

        dbConnect(url, driver, user, password)
        dsl()
    }
}