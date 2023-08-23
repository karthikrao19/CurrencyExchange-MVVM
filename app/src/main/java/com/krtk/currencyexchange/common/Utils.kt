package com.krtk.currencyexchange.common


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun displayTimeStamp(): Long {
        val timestamp = System.currentTimeMillis()
        val formattedTimestamp = formatTimeStamp(timestamp)
       // Log.i("displayTimeStamp***", timestamp.toString())
        return timestamp
    }

    fun formatTimeStamp(timestamp: Long): String {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
       // Log.i("formatTimeStamp***", simpleDateFormat.format(Date(timestamp)))

        return simpleDateFormat.format(Date(timestamp))
    }

    //The isWithin30Minutes function appears to be a utility function that checks whether a given timestamp
    // is within the last 30 minutes relative to the current time.
    fun isWithin30Minutes(timestamp: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val thirtyMinutesInMillis = 30 * 60 * 1000 // 30 minutes in milliseconds
        //Log.i("isWithin30Minutes***", (currentTimeMillis - timestamp <= thirtyMinutesInMillis).toString())
        // Check if the difference between the current time and the given timestamp is within 30 minutes
        return currentTimeMillis - timestamp <= thirtyMinutesInMillis
    }


}