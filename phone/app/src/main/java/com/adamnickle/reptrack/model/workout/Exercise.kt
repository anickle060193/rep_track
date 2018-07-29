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
}