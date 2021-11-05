package com.example.server

import com.example.retrofit.api.BankApiImplementation
import com.example.server.parsers.ParseObject
import com.example.server.parsers.Parser
import com.example.server.parsers.concrete.*
import com.google.gson.Gson

object Server {

    suspend fun getPrices(bookName: String): String {
        val results: MutableList<ParseObject> = mutableListOf()
        val curs = BankApiImplementation.getCurs()
        listOf<Parser>(AlpinaParser(curs), BhvParser(curs), LabirintParser(curs), OzParser, PiterParser(curs)).forEach {
            results.addAll(it.parse(bookName))
        }
        return Gson().toJson(results)
    }
}