package com.example.andre.playnews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.io.FileNotFoundException
import android.util.LruCache


class RecyclerAdapter(val mContext: Context, val mFeed: ArrayList<news>, val mMemoryCache: LruCache<String,Bitmap>):RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_listitem, p0, false))
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image_rf = itemView.findViewById<ImageView>(R.id.imageid)
        val resource_rf = itemView.findViewById<TextView>(R.id.resourceid)
        val resume_rf = itemView.findViewById<TextView>(R.id.resumetextid)
    }

    override fun onBindViewHolder(p0: RecyclerAdapter.MyViewHolder, p1: Int) {

        var bitmap = mMemoryCache.get(mFeed[p1].title)
        if (bitmap != null) {
            Log.i("DebugTAG","Cache HIT")
            p0.image_rf.setImageBitmap(bitmap)
        } else {
            try {
                Log.i("DebugTAG","File")
                val fol = mContext.openFileInput(mFeed[p1].title)
                bitmap = BitmapFactory.decodeStream(fol)
                p0.image_rf.setImageBitmap(bitmap)
                mMemoryCache.put(mFeed[p1].title,bitmap)
                fol.close()
            }catch (e: FileNotFoundException){
                p0.image_rf.setImageBitmap(mFeed[p1].image)
            }catch (e: IllegalStateException) {
                p0.image_rf.setImageBitmap(mFeed[p1].image)
            }
        }

        p0.resource_rf.text = mFeed[p1].resource
        p0.resume_rf.text = mFeed[p1].title
    }

    override fun getItemCount(): Int {
        return mFeed.size
    }
}