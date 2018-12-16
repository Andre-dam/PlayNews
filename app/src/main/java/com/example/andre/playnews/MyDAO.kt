package com.example.andre.playnews

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface MyDAO{
    @Query("SELECT * from Feed")
    fun getAllFeed(): List<Feed>

    @Query("SELECT * from News")
    fun getAllNews(): List<News>



    @Insert(onConflict = REPLACE)
    fun insertAll(feed: Feed)

    @Insert(onConflict = REPLACE)
    fun insertAlll(new: News)
}