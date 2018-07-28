package com.adamnickle.reptrack.model.workout

import android.arch.persistence.room.*
import com.adamnickle.reptrack.model.AppTypeConverters
import com.adamnickle.reptrack.utils.extensions.toShortString
import java.util.*

@Entity
@TypeConverters( AppTypeConverters::class )
data class Workout(
        @PrimaryKey( autoGenerate = true ) var id: Long?,
        var name: String,
        var date: Date,
        @ColumnInfo( index = true ) var deleted: Boolean
)
{
    @Ignore
    constructor( name: String, date: Date ): this( null, name, date, false )

    @Ignore
    constructor( date: Date ): this( date.toShortString(), date )

    @Ignore
    constructor(): this( Date() )
}