package com.arturdevmob.cryptorates.data.sources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.arturdevmob.cryptorates.data.utils.Utils

@Entity(tableName = "coin")
data class CoinEntity(
    @PrimaryKey
    @ColumnInfo(name = "FROMSYMBOL")
    val fromSymbol: String,

    @ColumnInfo(name = "TOSYMBOL")
    val toSymbol: String,

    @ColumnInfo(name = "PRICE")
    val currentPrice: Double,

    @ColumnInfo(name = "CHANGEHOUR")
    val changeHour: Double,

    @ColumnInfo(name = "CHANGE24HOUR")
    val change24Hour: Double,

    @ColumnInfo(name = "IMAGEURL")
    val imageUrl: String
) {

    @Ignore
    val percentChangeHour = getPercentFromChangeHour()

    @Ignore
    val percentChange24Hour = getPercentFromChange24Hour()

    // Считает процент, на сколько изменилась цена валюты за последний час
    private fun getPercentFromChangeHour(): Double {
        return Utils.roundNumberToDecimalPlace(
            Utils.percentXFromY(changeHour, currentPrice), 2
        )
    }

    // Считает процент, на сколько изменилась цена валюты за последние 24 часа
    private fun getPercentFromChange24Hour(): Double {
        return Utils.roundNumberToDecimalPlace(
            Utils.percentXFromY(change24Hour, currentPrice), 2
        )
    }

    fun priceHasRiseItHour(): Boolean {
        return getPercentFromChangeHour() >= 0
    }

    fun priceHasRiseIt24Hour(): Boolean {
        return getPercentFromChange24Hour() >= 0
    }
}