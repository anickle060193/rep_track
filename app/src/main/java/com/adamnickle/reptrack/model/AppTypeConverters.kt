package com.adamnickle.reptrack.model

import android.arch.persistence.room.TypeConverter
import java.util.*

object AppTypeConverters
{
    @TypeConverter
    @JvmStatic
    fun dateToLong( date: Date ) = date.time

    @TypeConverter
    @JvmStatic
    fun longToDate( long: Long ) = Date( long )
}