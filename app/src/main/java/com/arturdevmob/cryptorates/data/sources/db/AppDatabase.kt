package com.arturdevmob.cryptorates.data.sources.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CoinEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}