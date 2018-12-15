package com.example.andre.playnews

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class News(
        @PrimaryKey var title: String,
        var description: String,
        var image: String,
        var resource: String
)