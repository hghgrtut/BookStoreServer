package com.example.plugins.retrofit.api

import com.example.plugins.retrofit.data.BankApiData
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

object BankApiImplementation {
    private val service = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://belarusbank.by/api/")
        .build()
        .create(BankApi::class.java)

    /** Belarusian rubles for one russian */
    suspend fun getCurs(): Double = service.getCurs()[0].rubToByn.toDouble() / 100
}


private interface BankApi {
    @GET("kurs_cards")
    suspend fun getCurs(): List<BankApiData>
}