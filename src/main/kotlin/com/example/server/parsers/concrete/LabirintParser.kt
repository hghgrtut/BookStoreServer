package com.example.server.parsers.concrete

import com.example.server.parsers.*
import com.example.server.parsers.getHTML
import com.example.server.parsers.getRefs

class LabirintParser(private val currencyRate: Double) : Parser() {
    override suspend fun parse(bookName: String): List<ParseObject> {
        val html = "https://www.labirint.ru/search/$bookName/?stype=0".getHTML()

        if (html.select("div[class=\"search-error\"]").isNotEmpty()) return emptyList()

        val prices = html.getPrices("span[class=\"price-val\"]", currencyRate)
        val links = html.getRefs("a[class=\"product-title-link\"]")
        val authors = html.getText("div[class=\"product-author\"]")
        val titles = html.getText("span[class=\"product-title\"]")
        val pictures = html.getPictures("img[class=\"book-img-cover\"]")
        return List (prices.size) { i -> ParseObject(prices[i], links[i], authors[i], titles[i], pictures[i]) }
    }
}