package com.example.andre.playnews

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
@Database(entities = arrayOf(Feed::class,News::class), version=1)
abstract class MyRoomDB:RoomDatabase(){
    abstract fun MyDAO(): MyDAO
}