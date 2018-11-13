package com.example.andre.playnews

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myToolbar = findViewById(R.id.actionbarid) as Toolbar
        setSupportActionBar(myToolbar)
    }
}
