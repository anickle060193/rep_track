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
        @ColumnInfo( index = true ) var deleted: Boolean
)
{
    @Ignore
    constructor( weight: Float, repCount: Int, exerciseId: Long, order: Int ): this( null, false, weight, repCount, exerciseId, order, false )
}