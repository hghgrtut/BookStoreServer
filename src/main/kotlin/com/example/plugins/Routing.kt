package com.example.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.lang.StringBuilder

fun Application.configureRouting() {

    fun Element.getBelarusianPrice(): Double? = text().substringBefore(" ").replace(',','.').toDoubleOrNull()

    fun String.getHTML(): Document = Jsoup.connect(this).get()

    val api = "/api"

    val NOT_PRESENTED = 999999999
    val NOT_PRESENTED_DOUBLE = 999999999.0

    routing {
        get(path = "$api/book={bookName}") {
            val bookName = call.parameters["bookName"]
            val oz = "https://oz.by/search/?q=$bookName".getHTML()
            val priceOz = run {
                val priceIfSingleBookOnOz =
                    oz.select("span[class=\"b-product-control__text b-product-control__text_main\"]")
                if (priceIfSingleBookOnOz.isNotEmpty()) priceIfSingleBookOnOz[0].getBelarusianPrice() else null
            } ?: oz.select("span[class=\"item-type-card__btn\"]")
                .map { it.getBelarusianPrice() ?: NOT_PRESENTED_DOUBLE }
                .minOf { it }

            val labirint = "https://www.labirint.ru/search/$bookName/?stype=0".getHTML()
            val pricesLabirint = labirint.select("span[class=\"price-val\"]")
            val priceLabirint =
                if (pricesLabirint.isNotEmpty()) {
                    pricesLabirint
                        .map { it.text().substringBefore(" ₽").replace(" ", "").toIntOrNull() }
                        .minOf { it ?: NOT_PRESENTED }
                } else { NOT_PRESENTED }

            val response = StringBuilder("{bookName: \"$bookName\"")
            if (priceOz != NOT_PRESENTED_DOUBLE) response.append(", priceOz: $priceOz")
           if (priceLabirint != NOT_PRESENTED) response.append(", priceLabirint: $priceLabirint")
            call.respondText("$response}")
        }
        get("/") {
            call.respondText("Hello World! To use this API, see documentation in source code.")
        }
    }
}
