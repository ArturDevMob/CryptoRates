package com.arturdevmob.cryptorates.data.sources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coinEntities: MutableList<CoinEntity>)

    @Query("SELECT * FROM coin")
    fun getAllCoins(): MutableList<CoinEntity>

    @Query("DELETE FROM coin")
    fun removeAll()
}