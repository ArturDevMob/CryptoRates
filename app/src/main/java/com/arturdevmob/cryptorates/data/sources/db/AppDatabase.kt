package com.arturdevmob.cryptorates.data.sources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arturdevmob.cryptorates.data.models.CoinRateDataModel

@Database(entities = [CoinRateDataModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}