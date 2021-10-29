package com.example.retrofit.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BankApiData(@Json(name = "RUBCARD_out") val rubToByn: String)