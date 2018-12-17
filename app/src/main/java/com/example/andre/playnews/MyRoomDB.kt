package com.example.andre.playnews

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration

@Database(entities = arrayOf(Feed::class,News::class), version=2)
abstract class MyRoomDB:RoomDatabase(){
    abstract fun MyDAO(): MyDAO
}