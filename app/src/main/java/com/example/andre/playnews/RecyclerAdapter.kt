package com.example.andre.playnews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class RecyclerAdapter(val mContext: Context, val mFeed: ArrayList<news>):RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_listitem, p0, false))
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image_rf = itemView.findViewById<ImageView>(R.id.imageid)
        val resource_rf = itemView.findViewById<TextView>(R.id.resourceid)
        val resume_rf = itemView.findViewById<TextView>(R.id.resumetextid)
    }

    override fun onBindViewHolder(p0: RecyclerAdapter.MyViewHolder, p1: Int) {
        //MOCK
        p0.image_rf.setImageBitmap(mFeed[p1].image)
        p0.resource_rf.text = mFeed[p1].resource
        p0.resume_rf.text = mFeed[p1].resume
    }

    override fun getItemCount(): Int {
        return mFeed.size
    }
}