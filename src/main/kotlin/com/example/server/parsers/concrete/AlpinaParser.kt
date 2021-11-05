package com.example.server.parsers.concrete

import com.example.server.parsers.*
import com.example.server.parsers.getHTML
import com.example.server.parsers.getRefs
import com.example.server.parsers.getText
import com.example.server.parsers.selectLimited
import org.jsoup.nodes.Element

class AlpinaParser(private val currencyRate: Double) : Parser() {
    override suspend fun parse(bookName: String): List<ParseObject> {
        val html = "https://alpinabook.ru/catalog/search/?q=$bookName".getHTML()
        val cardInfos = html.selectLimited("div[class=\"b-book-v w-book-hover book-card bgColor-element gtm-book-card js-book-card _gray\"]")
        val prices = cardInfos.map { it.attr("data-book-price").toDoubleOrNull()?.times(currencyRate) }
        val links = html.getRefs("a[class=\"gtm-book-card__link\"]")
        val authors = html.getText("span[itemprop=\"author\"]")
        val titles = cardInfos.map { it.attr("data-book-name") }
        val pictures = html.getPicturesSrc("img[title]")
        return List(prices.size) { i -> ParseObject(prices[i], links[i], authors[i], titles[i], pictures[i]) }
    }
}