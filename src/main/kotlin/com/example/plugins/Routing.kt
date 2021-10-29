package com.example.plugins

import com.example.server.Server
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {
    val api = "/api"

    routing {
        get(path = "$api/book") { call.respondText(Server.getPrices(call.request.queryParameters["bookName"]!!)) }
        get(path = "/") {
            call.respondText(
                "Hello World! To use this API, see documentation on https://github.com/hghgrtut/BookStoreServer") }
    }
}
