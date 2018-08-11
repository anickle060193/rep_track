package com.adamnickle.reptrack.model.workout

import android.arch.persistence.room.*

@Entity(
        tableName = "exerciseSet",
        indices = [
            Index( "exerciseId", "order", unique = true )
        ],
        foreignKeys = [
            ForeignKey( entity = Exercise::class, parentColumns = [ "id" ], childColumns = [ "exerciseId" ], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE )
        ]
)
data class ExerciseSet(
        @PrimaryKey( autoGenerate = true ) var id: Long?,
        var completed: Boolean,
        var weight: Float,
        var repCount: Int,
        @ColumnInfo( index = true ) var exerciseId: Long,
        var order: Int,
        @ColumnInfo( index = true ) var deleted: Boolean,
        var rpe: Float?,
        var notes: String
)
{
    @Ignore
    constructor( weight: Float, repCount: Int, exerciseId: Long, order: Int ): this( null, false, weight, repCount, exerciseId, order, false, null, "" )

    fun idOrThrow(): Long
    {
        return id ?: throw IllegalArgumentException( "Exercise Set is unsaved" )
    }
}

class FullExerciseSet
{
    var id: Long = 0
    var completed: Boolean = false
    var weight: Float = 0.0f
    var repCount: Int = 0
    var order: Int = 0
    var deleted: Boolean = false
    var rpe: Float? = null
    var notes: String = ""

    fun toMap() = mapOf(
        "id" to id,
        "completed" to completed,
        "weight" to weight,
        "repCount" to repCount,
        "order" to order,
        "deleted" to deleted,
        "rpe" to rpe,
        "notes" to notes
    )
}