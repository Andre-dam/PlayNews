package com.example.andre.playnews

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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
import android.util.LruCache
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import com.example.andre.playnews.ParserRSS
import com.example.andre.playnews.R.attr.height
import org.xmlpull.v1.XmlPullParser
import java.io.*
import java.net.HttpURLConnection
import java.net.ResponseCache
import java.net.URL
import java.nio.charset.StandardCharsets

data class news(var resume: String){
    lateinit var image: Bitmap
    var title = ""
    var resource = ""
}

data class feed(var resume: String){
    lateinit var image: Bitmap
    var title = ""
    var resource = ""
}

class MainActivity : AppCompatActivity() {

    val myFeed = ArrayList<news>()
    val myExploration = ArrayList<Tdi>()

    lateinit var myAdapter: RecyclerAdapter
    lateinit var myAdapter2: RecyclerAdapter2

    lateinit var mMemoryCache: LruCache<String, Bitmap>
    lateinit var db:MyRoomDB

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id==R.id.action_refresh){
            Log.i("DebugTAG", "reffersh")
            myFeed.clear()
            DownloadRSS().execute("http://leopoldomt.com/if1001/g1brasil.xml")
        }else if(id==R.id.action_add){
            val myint = Intent(this,addActivity::class.java)
            startActivity(myint)
            Log.i("DebugTAG", "add")
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(this, MyRoomDB::class.java,"mydb").build()

        val myToolbar = findViewById<Toolbar>(R.id.actionbarid)
        setSupportActionBar(myToolbar)

        val vp_adapter = TabAdapter(supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.viewpagerid)
        viewPager.adapter = vp_adapter

        val tablayout = findViewById<TabLayout>(R.id.tabid)
        tablayout.setupWithViewPager(viewPager)

        //Criação da cache
        val maxmemory = (Runtime.getRuntime().maxMemory() /1024).toInt()
        val cacheSize = maxmemory/8

        mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
        Log.i("DebugTAG","cache size: "+cacheSize)
        Log.i("DebugTAG","cache size: "+mMemoryCache.size())
        //END - Criação da cache

    }


    inner class DownloadRSS(): AsyncTask<String, String, ArrayList<Pair<String,String>>>() {
        override fun doInBackground(vararg params: String?): ArrayList<Pair<String,String>> {
            val url = URL(params[0])
            val urlConnection = url.openConnection() as HttpURLConnection
            val toDownload = ArrayList<Pair<String,String>>()
            try {
                val resp = BufferedInputStream(urlConnection.inputStream)
                val lines = BufferedReader(InputStreamReader(resp, StandardCharsets.UTF_8)).readText()

                val result = ParserRSS.parse(lines)
                val tdi = ParserFEED.parse(lines)
                //getBitmapFromURL(tdi.image_url, tdi.title)
                toDownload.add(Pair(tdi.image_url, tdi.title))

                db.MyDAO().insertAll(Feed(tdi.title,tdi.description,tdi.image_url))
                myExploration.add(tdi)

                val img = BitmapFactory.decodeResource(resources,R.mipmap.news)
                for(element in result){
                    //Log.i("DebugTAG",element.title)
                    //Log.i("DebugTAG",element.description)
                    //Log.i("DebugTAG",element.link)
                    //Log.i("DebugTAG",element.pubDate)
                    //Log.i("DebugTAG",element.img)
                    Log.i("DebugTAG","\n")

                    //getBitmapFromURL(element.img, element.title)
                    toDownload.add(Pair(element.img, element.title))
                    val query_temp = news(element.description)
                    query_temp.resource = tdi.title
                    query_temp.image = img
                    query_temp.title = element.title

                    db.MyDAO().insertAlll(News(element.title,element.description,element.title, tdi.title))
                    myFeed.add(query_temp)
                }
                return toDownload
            }catch (e: IOException){
                Log.i("DebugTAG",e.toString())
                return toDownload
            }
        }
        override fun onPostExecute(result: ArrayList<Pair<String,String>>) {
            myAdapter.notifyDataSetChanged()
            myAdapter2.notifyDataSetChanged()

            for(i in result){
                downloadIMG().execute(i)
            }
        }
    }
    inner class downloadIMG(vararg params: String):AsyncTask<Pair<String,String>, String, Boolean>(){

        fun getResizedBitmap(bm: Bitmap, scalefactor: Float, key:String): ByteArray {
            val width = bm.getWidth();
            val height = bm.getHeight();
            val matrix = Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scalefactor, scalefactor);

            // "RECREATE" THE NEW BITMAP
            val resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);

            mMemoryCache.put(key,resizedBitmap)
            Log.i("DebugTAG","cache size: "+mMemoryCache.size())
            val stream = ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            val byteArray = stream.toByteArray();
            bm.recycle();

            return byteArray
        }
        private fun getBitmapFromURL(src: String, key: String){
            try {
                val url = java.net.URL(src)
                val connection = url
                        .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bm = getResizedBitmap(BitmapFactory.decodeStream(input),0.3F,key)

                val fos = openFileOutput(key, Context.MODE_PRIVATE) //Utiliza o proprio titulo como chave
                fos.write(bm)
                fos.close()
                Log.i("DebugTAG","Baixou, key :"+key)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: IllegalStateException){
                e.printStackTrace()
            }
        }

        override fun doInBackground(vararg params: Pair<String, String>): Boolean {
            getBitmapFromURL(params[0].first,params[0].second)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            myAdapter.notifyDataSetChanged()
            myAdapter2.notifyDataSetChanged()
            super.onPostExecute(result)
        }
    }

}
