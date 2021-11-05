package com.example.server.parsers.concrete

import com.example.server.parsers.*
import com.example.server.parsers.getHTML
import com.example.server.parsers.getRefs
import com.example.server.parsers.getText
import com.example.server.parsers.selectLimited

class BhvParser(private val currencyRate: Double) : Parser() {
    override suspend fun parse(bookName: String): List<ParseObject> {
        val html = "https://bhv.ru/?s=$bookName&post_type=product&type_aws=true&id=1&filter=1".getHTML()
        val prices = html.selectLimited("span[class=\"price\"] > ins")
            .map { it.text().substringBefore(' ').toDouble() * currencyRate }
        val links = html.getRefs("a[class=\"woocommerce-LoopProduct-link woocommerce-loop-product__link\"]:has(img)")
        val authors = html.getText("span[class=\"author\"]:not(:contains(,))") // take last, not first author
        val titles = html.getText("p[class=\"woocommerce-loop-product__title book-title-recomendation\"]")
        val pictures = html
            .selectLimited("img[class=\"attachment-woocommerce_thumbnail size-woocommerce_thumbnail wp-post-image lazyload\"]")
            .map { "https${it.attr("data-src").substringAfterLast("https")}" }
        return List(prices.size) { i -> ParseObject(prices[i], links[i], authors[i], titles[i], pictures[i]) }
    }
}