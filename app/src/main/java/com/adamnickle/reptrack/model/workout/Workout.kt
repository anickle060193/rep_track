package com.adamnickle.reptrack.model.workout

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.adamnickle.reptrack.model.AppTypeConverters
import com.adamnickle.reptrack.utils.extensions.toShortString
import java.util.*

@Entity
@TypeConverters( AppTypeConverters::class )
data class Workout(
        @PrimaryKey( autoGenerate = true ) var id: Long?,
        var name: String,
        var date: Date
)
{
    @Ignore
    constructor( name: String, date: Date ): this( null, name, date )

    @Ignore
    constructor( date: Date ): this( date.toShortString(), date )

    @Ignore
    constructor(): this( Date() )
}