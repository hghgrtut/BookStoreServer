package com.example.server

import com.example.server.parsers.Parser
import com.example.server.parsers.concrete.LabirintParser
import com.example.server.parsers.concrete.OzParser
import com.google.gson.Gson

object Server {

    suspend fun getPrices(bookName: String): String {
        val response = listOf<Parser>(OzParser, LabirintParser).mapNotNull { it.parse(bookName) }
        return Gson().toJson(response)
    }
}