package com.example.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun Application.configureRouting() {
    val api = "/api"

    routing {
        get(path = "$api/book={bookName}") {
            val searchPage: Document = Jsoup.connect("https://yandex.by/search/?text=${call.parameters["bookName"]}+вайлдберриз").get()
            val bookUrl = searchPage.select("a[class=\"Link Link_theme_outer Path-Item link path__item organic__greenurl\"]")[0].attr("href")
            val bookPage: Document = Jsoup.connect("$bookUrl").get()
            val title: String = bookPage.select("span[data-link=\"text{:productCard^goodsName}\"]").text()
            val price: String = bookPage.select("span[class=\"price-block__commission-current-price\"]")[0].text().removeSuffix(" ₽").replace(" ", "")
            call.respondText("{title: \"$title\", price: $price}")
        }
        get("/") {
            call.respondText("Hello World! To use this API, see documentation in source code.")
        }
    }
}
