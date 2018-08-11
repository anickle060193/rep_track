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

    fun idOrThrow(): Long = id ?: throw IllegalStateException( "${Workout::class} is unsaved" )
}

@TypeConverters( AppTypeConverters::class )
class FullWorkout
{
    var id: Long = 0
    lateinit var name: String
    lateinit var date: Date
    var deleted: Boolean = false

    @Relation(parentColumn = "id", entityColumn = "workoutId", entity = Exercise::class )
    lateinit var exercises: List<FullExercise>

    fun toMap() = mapOf(
        "id" to id,
        "id" to id,
        "name" to name,
        "date" to date,
        "deleted" to deleted,
        "exercises" to exercises.sortedBy { exercise -> exercise.order }.map { exercise -> exercise.toMap() }
    )
}