package com.arturdevmob.cryptorates.data.sources.network

import com.google.gson.JsonObject
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoServices {
    companion object {
        private const val DEFAULT_COUNT_LIMIT_COINS = 100
    }

    @GET("top/totaltoptiervolfull")
    fun getNameTopCoins(
        @Query("limit") limit: Int = DEFAULT_COUNT_LIMIT_COINS,
        @Query("tsym") toSymbol: String
    ): Flowable<JsonObject>

    @GET("pricemultifull")
    fun getCoins(
        @Query("fsyms") fromSymbol: String,
        @Query("tsyms") toSymbol: String
    ): Flowable<JsonObject>
}