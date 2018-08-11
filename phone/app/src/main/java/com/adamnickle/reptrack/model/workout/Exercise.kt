package com.adamnickle.reptrack.model.workout

import android.arch.persistence.room.*

@Entity(
    indices = [
        Index( "workoutId", "order", unique = true )
    ],
    foreignKeys = [
        ForeignKey( entity = Workout::class, parentColumns = [ "id" ], childColumns = [ "workoutId" ], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE )
    ]
)
data class Exercise(
        @PrimaryKey( autoGenerate = true ) var id: Long?,
        var name: String,
        @ColumnInfo( index = true ) var workoutId: Long,
        var order: Int,
        @ColumnInfo( index = true ) var deleted: Boolean
)
{
    @Ignore
    constructor( name: String, workoutId: Long, order: Int ): this( null, name, workoutId, order, false )

    fun idOrThrow(): Long = id ?: throw IllegalStateException( "${Exercise::class} is unsaved" )
}

class FullExercise
{
    var id: Long = 0
    lateinit var name: String
    var order: Int = 0
    var deleted: Boolean = false

    @Relation( parentColumn = "id", entityColumn = "exerciseId", entity = ExerciseSet::class )
    lateinit var sets: List<FullExerciseSet>

    fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "order" to order,
        "deleted" to deleted,
        "sets" to sets.sortedBy { set -> set.order }.map { set -> set.toMap() }
    )
}