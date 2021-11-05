package com.example.server.parsers

abstract class Parser {
    abstract suspend fun parse(bookName: String): List<ParseObject>

}