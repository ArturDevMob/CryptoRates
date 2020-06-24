package com.arturdevmob.cryptorates.domain.models

import com.arturdevmob.cryptorates.data.utils.Utils

data class CoinRateDomainModel(
    val dbId: Long? = null,
    val fromSymbol: String,
    val toSymbol: String,
    val currentPrice: Double,
    val changeHour: Double,
    val change24Hour: Double,
    val imageUrl: String
) {
    // Считает процент, на сколько изменилась цена валюты за последний час
    val percentChangeHour = Utils.roundNumberToDecimalPlace(
        Utils.percentXFromY(changeHour, currentPrice), 2
    )

    // Считает процент, на сколько изменилась цена валюты за последние 24 часа
    val percentChange24Hour = Utils.roundNumberToDecimalPlace(
        Utils.percentXFromY(change24Hour, currentPrice), 2
    )

    fun priceHasRiseItHour(): Boolean {
        return percentChangeHour >= 0
    }

    fun priceHasRiseIt24Hour(): Boolean {
        return percentChange24Hour >= 0
    }
}