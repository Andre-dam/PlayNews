package com.example.andre.playnews

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

class MyFeedFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.myfeed_fragment,container,false)

        val superact = activity as MainActivity
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerid)

        superact.myAdapter = RecyclerAdapter(superact,superact.myFeed, superact.mMemoryCache)

        recyclerView.adapter = superact.myAdapter
        recyclerView.layoutManager = LinearLayoutManager(superact)
        return v
    }

    override fun onStart() {
        super.onStart()
        bankUpdate().execute()
    }

    inner class bankUpdate(): AsyncTask<String, String, Boolean>(){
        val superact = activity as MainActivity
        override fun doInBackground(vararg params: String?): Boolean {


            val news_list = superact.db.MyDAO().getAllNews()

            val img = BitmapFactory.decodeResource(resources,R.mipmap.news)
            superact.myFeed.clear()
            for(i in news_list){
                val temp = news("")
                temp.image = img
                temp.resource = i.resource
                temp.resume = i.description
                temp.title = i.title
                superact.myFeed.add(temp)
            }
            return true
        }
        override fun onPostExecute(result: Boolean?) {
            superact.myAdapter.notifyDataSetChanged()
        }
    }
}