package com.example.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun Application.configureRouting() {

    val baseUrl = "https://by.wildberries.ru/catalog/"
    val end = "/detail.aspx?targetUrl=XS"

    routing {
        get(path = "/{bookId}") {
            val doc: Document = Jsoup.connect("$baseUrl${call.parameters["bookId"]}$end").get()
            val title: String = doc.select("span[data-link=\"text{:productCard^goodsName}\"]").text()
            val price: String = doc.select("span[class=\"price-block__final-price\"]")[0].text().removeSuffix(" ₽").replace(" ", "")
            call.respondText("{title: \"$title\", price: $price}")
        }
        get("/") {
            call.respondText("Hello World! To use this API, add wildberries goods id after slash")
        }
    }
}
