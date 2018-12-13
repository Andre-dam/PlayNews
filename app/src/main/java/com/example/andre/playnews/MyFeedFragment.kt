package com.example.andre.playnews

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MyFeedFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.myfeed_fragment,container,false)

        val superact = activity as MainActivity
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerid)

        superact.myAdapter = RecyclerAdapter(superact,superact.myFeed)

        recyclerView.adapter = superact.myAdapter
        recyclerView.layoutManager = LinearLayoutManager(superact)
        return v
    }
}