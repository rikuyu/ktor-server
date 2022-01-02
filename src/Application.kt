package com.example

import com.example.model.Member
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.request.*
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

    DatabaseService.apply {
        val url = environment.config.property("ktor.database.url").getString()
        val driver = environment.config.property("ktor.database.driver").getString()
        val user = environment.config.property("ktor.database.user").getString()
        val password = environment.config.property("ktor.database.password").getString()

        dbConnect(url, driver, user, password)
    }

    routing {
        route("member") {
            get("/all") {
                val response = DatabaseService.getAllMembers()
                call.respond(response)
            }
            get<GetMemberLocation> { param ->
                val id = param.id
                val member = DatabaseService.getMember(id)
                if (member != null) {
                    call.respond(member)
                    return@get
                }
                call.respond(HttpStatusCode.InternalServerError)
            }
            post("/register") {
                val request = call.receive<Member>()
                DatabaseService.insertMember(request.name, request.age)
                call.respondText("name = ${request.name} age = ${request.age}")
            }
            post("/delete") {
                call.parameters["id"]?.toIntOrNull()?.let { id ->
                    DatabaseService.deleteMember(id)
                    call.respondText("delete member id=$id")
                    return@post
                }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

@KtorExperimentalLocationsAPI
@Location("/{id}")
data class GetMemberLocation(val id: Int)