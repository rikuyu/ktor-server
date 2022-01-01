package com.example

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

object JsonService {
    @KtorExperimentalLocationsAPI
    fun Routing.bookRoute(){
        route("/book"){
            @Location("/detail/{bookId}")
            data class BookLocation(val bookId: Long)
            get<BookLocation>{ request ->
                val response = BookResponse(
                    id = request.bookId,
                    title = "Ktor入門",
                    author = "ktorman"
                )
                call.respond(response)
            }
        }
    }

    data class BookResponse(
        val id: Long,
        val title: String,
        val author: String
    )
}