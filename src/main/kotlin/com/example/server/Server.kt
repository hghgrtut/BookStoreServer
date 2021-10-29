package com.example.server

import com.example.server.parsers.Parser
import com.example.server.parsers.concrete.LabirintParser
import com.example.server.parsers.concrete.OzParser
import com.google.gson.Gson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object Server {

    suspend fun getPrices(bookName: String): String {
        val response = listOf<Parser>(OzParser, LabirintParser).map { it.parse(bookName) }
        return Gson().toJson(response)
    }

    private fun Element.getBelarusianPrice(): Double? =
        text().substringBefore(" ").replace(',','.').toDoubleOrNull()

    private fun String.getHTML(): Document = Jsoup.connect(this)
        .userAgent("Mozilla/5.0 (Linux; Android 10; Redmi Note 7 Build/QKQ1.190910.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/89.0.4389.105 Mobile Safari/537.36 GSA/12.10.7.23.arm64")
        .get()
}