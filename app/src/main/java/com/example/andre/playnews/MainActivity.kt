package com.example.andre.playnews

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar

data class news(var resume: String){
    lateinit var image: Bitmap
    var resource = ""
}

class MainActivity : AppCompatActivity() {

    val myFeed = ArrayList<news>()
    lateinit var myAdapter: RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myToolbar = findViewById<Toolbar>(R.id.actionbarid)
        setSupportActionBar(myToolbar)
        val query_temp = news("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nulla at risus. Quisque purus magna, auctor et, sagittis ac, posuere eu, lectus. Nam mattis, felis ut adipiscing")
        query_temp.image = BitmapFactory.decodeResource(resources,R.mipmap.news)
        query_temp.resource = "BBC News"
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)
        myFeed.add(query_temp)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerid)
        myAdapter = RecyclerAdapter(this,myFeed)
        recyclerView.adapter = RecyclerAdapter(this,myFeed)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
