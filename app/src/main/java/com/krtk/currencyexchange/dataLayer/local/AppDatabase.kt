package com.krtk.currencyexchange.dataLayer.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.dataLayer.model.TimeStamp


//Create an abstract class that extends RoomDatabase and includes an abstract method to return the DAO.
// This class will serve as the database holder and the main access point for the underlying connection
// to your app's persisted, relational data.
@Database(entities = [CurrencyRate::class, TimeStamp::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
}

