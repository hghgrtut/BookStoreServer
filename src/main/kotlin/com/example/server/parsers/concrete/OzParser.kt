package com.example.server.parsers.concrete

import com.example.server.parsers.*
import com.example.server.parsers.getHTML

object OzParser : Parser() {
    override suspend fun parse(bookName: String): List<ParseObject> {
        val html = "https://oz.by/search/?c=1101523&q=$bookName".getHTML()
        val prices = html.getPrices("span[class=\"item-type-card__btn\"]")
        val links = html.getRefs("a[class=\"item-type-card__link\"]")
        val authors = html.getText("p[class=\"item-type-card__info\"]")
        val titles = html.getText("p[class=\"item-type-card__title\"]")
        val pictures = html.getPictures("img[class=\"viewer-type-list__img\"]")
        return List (prices.size) { i -> ParseObject(prices[i], links[i], authors[i], titles[i], pictures[i]) }
    }
}