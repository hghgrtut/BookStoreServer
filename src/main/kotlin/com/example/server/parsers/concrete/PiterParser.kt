package com.example.server.parsers.concrete

import com.example.server.parsers.*
import com.example.server.parsers.getHTML

class PiterParser(private val currencyRate: Double) : Parser() {
    override suspend fun parse(bookName: String): List<ParseObject> {
        val html = "https://www.piter.com/collection/all?q=$bookName".getHTML()
        val pricesRaw = html.getText("div[class=\"buyzone clearfix\"]")
        val prices = pricesRaw.map { it.split(' ')[1].toDoubleOrNull()?.times(currencyRate) }
        val links = html.getRefs("div[class=\"products-list\"] a[title]")
        val authors = html.getText("div[class=\"products-list\"] span[class=\"author\"]")
        val titles = html.getText("div[class=\"products-list\"] span[class=\"title\"]")
        val pictures = html.selectLimited("div[class=\"products-list\"] img").map { it.attr("src") }
        return List (prices.size) { i -> ParseObject(prices[i], links[i], authors[i], titles[i], pictures[i]) }
    }
}