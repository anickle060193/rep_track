package com.adamnickle.reptrack.model.workout

import android.arch.persistence.room.*

@Entity(
        indices = [
            Index( "exerciseSetId", "time", unique = true )
        ],
        foreignKeys = [
            ForeignKey( entity = ExerciseSet::class, parentColumns = [ "id" ], childColumns = [ "exerciseSetId" ], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE )
        ]
)
data class ExerciseSetAccel(
        @PrimaryKey( autoGenerate = true ) var id: Long?,
        var x: Float,
        var y: Float,
        var z: Float,
        @ColumnInfo( index = true ) var time: Long,
        var exerciseSetId: Long
)
{
    @Ignore
    constructor( x: Float, y: Float, z: Float, time: Long, exerciseSetId: Long ): this( null, x, y, z, time, exerciseSetId )

    fun idOrThrow(): Long = id ?: throw IllegalStateException( "${ExerciseSetAccel::class} is unsaved" )
}