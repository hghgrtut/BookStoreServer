package com.example.server.parsers

abstract class Parser {
    abstract suspend fun parse(bookName: String): ParseObject?

    protected fun cheapest(prices: DoubleArray, refs: Array<String>, shop: String, currencyRate: Double = 1.0)
    : ParseObject? {
        var minPrice = Double.MAX_VALUE
        var minIndex = -1
        for (index in prices.indices) if (prices[index] < minPrice) {
            minPrice = prices[index]
            minIndex = index
        }
        if (minIndex == -1) return null
        minPrice *= currencyRate
        return ParseObject(shop, minPrice, refs[minIndex])
    }
}