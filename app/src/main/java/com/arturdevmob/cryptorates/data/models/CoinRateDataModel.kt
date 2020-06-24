package com.arturdevmob.cryptorates.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arturdevmob.cryptorates.domain.models.CoinRateDomainModel

@Entity(tableName = "coin")
data class CoinRateDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val dbId: Long? = null,

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
    fun toDomainModel(): CoinRateDomainModel {
        return CoinRateDomainModel(
            dbId, fromSymbol, toSymbol, currentPrice, changeHour, change24Hour, imageUrl
        )
    }
}