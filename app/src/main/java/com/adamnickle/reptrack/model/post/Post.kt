package com.adamnickle.reptrack.model.post

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Post(
        val userId: Int,
        @field:PrimaryKey val id: Int,
        val title: String,
        val body: String )