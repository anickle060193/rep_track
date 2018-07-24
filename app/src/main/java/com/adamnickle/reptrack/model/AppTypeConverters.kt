package com.adamnickle.reptrack.model

import android.arch.persistence.room.TypeConverter
import java.util.*

object AppTypeConverters
{
    @TypeConverter
    fun dateToLong( date: Date ) = date.time

    @TypeConverter
    fun longToDate( long: Long ) = Date( long )
}