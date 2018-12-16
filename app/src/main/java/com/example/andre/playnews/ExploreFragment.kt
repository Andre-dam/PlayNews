package com.example.andre.playnews

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ExploreFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.explore_fragment,container,false)

        val superact = activity as MainActivity
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerid2)

        superact.myAdapter2 = RecyclerAdapter2(superact,superact.myExploration, superact.mMemoryCache)

        recyclerView.adapter = superact.myAdapter2
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


            val news_list = superact.db.MyDAO().getAllFeed()

            val img = BitmapFactory.decodeResource(resources,R.mipmap.news)
            superact.myExploration.clear()
            for(i in news_list){
                val temp = Tdi("","","")
                temp.image_url = i.image
                temp.description = i.description
                temp.title = i.name

                superact.myExploration.add(temp)
            }
            return true
        }
        override fun onPostExecute(result: Boolean?) {
            superact.myAdapter2.notifyDataSetChanged()
        }
    }
}