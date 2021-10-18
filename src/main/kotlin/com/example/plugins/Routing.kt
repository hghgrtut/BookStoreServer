package com.example.plugins

import com.example.plugins.retrofit.api.BankApiImplementation
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

fun Application.configureRouting() {

    fun Element.getBelarusianPrice(): Double? = text().substringBefore(" ").replace(',','.').toDoubleOrNull()

    fun String.getHTML(): Document = Jsoup.connect(this)
        .userAgent("Mozilla/5.0 (Linux; Android 10; Redmi Note 7 Build/QKQ1.190910.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/89.0.4389.105 Mobile Safari/537.36 GSA/12.10.7.23.arm64")
        .get()

    val api = "/api"

    val NOT_PRESENTED_DOUBLE = 999999999.0

    routing {
        get(path = "$api/book") {
            val bookName = call.request.queryParameters["bookName"]
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
                    val priceInRussian =
                        pricesLabirint
                        .map { it.text().substringBefore(" ₽").replace(" ", "").toDoubleOrNull() }
                        .minOf { it ?: NOT_PRESENTED_DOUBLE }
                    priceInRussian * BankApiImplementation.getCurs()
                } else { NOT_PRESENTED_DOUBLE }

            val jsonParams = HashMap<String, Any?>()
            jsonParams["bookName"] = bookName
            if (priceOz != NOT_PRESENTED_DOUBLE) jsonParams["priceOz"] = priceOz
            if (priceLabirint != NOT_PRESENTED_DOUBLE) jsonParams["priceLabirint:"] = priceLabirint
            call.respondText(JSONObject(jsonParams).toString())
        }
        get("/") {
            call.respondText("Hello World! To use this API, see documentation in source code.")
        }
    }
}
