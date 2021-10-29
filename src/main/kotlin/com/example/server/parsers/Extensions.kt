package com.example.server.parsers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

internal fun Element.getPrice() = text().substringBefore(" ").replace(',','.').toDoubleOrNull()
internal fun Elements.getRefs() = this.map { it.attr("abs:href") }.toTypedArray()

internal fun String.getHTML(): Document = Jsoup.connect(this).userAgent(USER_AGENT).get()