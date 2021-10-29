package com.example.server.parsers.concrete

import com.example.retrofit.api.BankApiImplementation
import com.example.server.parsers.*
import com.example.server.parsers.LABIRINT
import com.example.server.parsers.getHTML
import com.example.server.parsers.getRefs

object LabirintParser : Parser() {
    override suspend fun parse(bookName: String): ParseObject? {
        val labirint = "https://www.labirint.ru/search/$bookName/?stype=0".getHTML()
        if (labirint.select("div[class=\"search-error\"]").isNotEmpty()) return null

        val prices = labirint.select("span[class=\"price-val\"]")
            .map { it.text().substringBefore(" ₽").replace(" ", "").toDoubleOrNull() ?: Double.MAX_VALUE }
            .toDoubleArray()
        val refs = labirint.select("a[class=\"product-title-link\"]").getRefs()
        return cheapest(prices, refs, LABIRINT, BankApiImplementation.getCurs())
    }
}