package com.krtk.currencyexchange.dataLayer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.krtk.currencyexchange.dataLayer.model.Currency
import com.krtk.currencyexchange.dataLayer.model.CurrencyRate
import com.krtk.currencyexchange.dataLayer.model.TimeStamp

//Create a DAO interface that includes methods to interact with the database,
// such as inserting, updating, and querying data.
@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(rates: List<CurrencyRate>)

    @Query("SELECT * FROM rates_table")
    suspend fun getCurrentExchangeRatesDB(): List<CurrencyRate>

    @Query("SELECT symbol FROM rates_table")
    suspend fun getCurrentExchangeSymbol(): List<Currency>

    @Update()
    suspend fun updateRates(rates: CurrencyRate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeStamp(timeStamp: TimeStamp)

    @Query("SELECT * FROM timestamps")
    suspend fun getTimeStamp(): TimeStamp
}