package com.example.server.parsers.concrete

import com.example.server.parsers.*
import com.example.server.parsers.getHTML
import com.example.server.parsers.getPrice

object OzParser : Parser() {
    override suspend fun parse(bookName: String): ParseObject? {
        val html = "https://oz.by/search/?c=1101523&q=$bookName".getHTML()
        val prices: DoubleArray = html.select("span[class=\"item-type-card__btn\"]")
            .map { it.getPrice() ?: Double.MAX_VALUE }
            .toDoubleArray()
        val refs = html.select("a[class=\"item-type-card__link\"]").getRefs()
        return cheapest(prices, refs, OZ)
    }
}