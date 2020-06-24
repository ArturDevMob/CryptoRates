package com.arturdevmob.cryptorates.data.sources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arturdevmob.cryptorates.data.models.CoinRateDataModel

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coinEntities: List<CoinRateDataModel>)

    @Query("SELECT * FROM coin")
    fun getAllCoins(): List<CoinRateDataModel>

    @Query("DELETE FROM coin")
    fun removeAll()
}