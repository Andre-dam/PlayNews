package com.example.andre.playnews

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.Log.i
import com.example.andre.playnews.ParserRSS.parse
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

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
        /*val query_temp = news("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eget ligula eu lectus lobortis condimentum. Aliquam nonummy auctor massa. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nulla at risus. Quisque purus magna, auctor et, sagittis ac, posuere eu, lectus. Nam mattis, felis ut adipiscing")
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
    */
        val vp_adapter = TabAdapter(supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.viewpagerid)
        viewPager.adapter = vp_adapter

        val tablayout = findViewById<TabLayout>(R.id.tabid)
        tablayout.setupWithViewPager(viewPager)
        //val recyclerView = findViewById<RecyclerView>(R.id.recyclerid)
        //myAdapter = RecyclerAdapter(this,myFeed)
        //recyclerView.adapter = RecyclerAdapter(this,myFeed)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        DownloadRSS().execute("http://leopoldomt.com/if1001/g1brasil.xml")
    }

    override fun onStart() {
        super.onStart()

    }
    inner class DownloadRSS(): AsyncTask<String, String, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            val url = URL(params[0])
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val resp = BufferedInputStream(urlConnection.inputStream)
                val lines = BufferedReader(InputStreamReader(resp, StandardCharsets.UTF_8)).readText()
                val RSS_list = parse(lines)


                val img = BitmapFactory.decodeResource(resources,R.mipmap.news)
                val rsc = "G1"

                for(element in RSS_list){
                    Log.i("DebugTAG",element.title)
                    Log.i("DebugTAG",element.description)
                    Log.i("DebugTAG",element.link)
                    Log.i("DebugTAG",element.pubDate)
                    Log.i("DebugTAG","\n")

                    val query_temp = news(element.description)
                    query_temp.resource = rsc
                    query_temp.image = img
                    myFeed.add(query_temp)
                }


                //Log.i("DebugTAG",lines)

                return true
            }catch (e: IOException){
                Log.i("DebugTAG",e.toString())
                return false
            }
        }
        override fun onPostExecute(result: Boolean) {
            //if(result) {
            myAdapter.notifyDataSetChanged()
            //}
        }
    }


}
