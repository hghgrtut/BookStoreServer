package com.example.server.parsers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

private const val MAX_BOOKS_PER_PARSER = 10

internal fun Document.getPrices(priceTag: String, currencyRate: Double = 1.0): List<Double?> = getText(priceTag).map {
    it.replace(" ", "")
        .substringBefore('р')
        .substringBefore('₽')
        .replace(',', '.')
        .toDoubleOrNull()?.times(currencyRate)
}

internal fun Document.getText(tag: String) = selectLimited(tag).map { it.text() }

internal fun Document.getPictures(tag: String) = selectLimited(tag).map { it.attr("data-src") }

internal fun Document.getPicturesSrc(tag: String) = selectLimited(tag).map { it.attr("abs:src") }

internal fun Document.getRefs(linkTag: String) = selectLimited(linkTag).map { it.attr("abs:href") }

internal fun Document.selectLimited(tag: String) = select(tag).asIterable().take(MAX_BOOKS_PER_PARSER)

internal fun String.getHTML(): Document = Jsoup.connect(this).userAgent(USER_AGENT).get()