package com.arturdevmob.cryptorates.data.sources.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
)