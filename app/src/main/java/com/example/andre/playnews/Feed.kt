package com.example.andre.playnews

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap

@Entity
data class Feed(
    @PrimaryKey var name: String,
                var description: String,
                var image: String
)