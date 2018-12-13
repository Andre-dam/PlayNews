package com.example.andre.playnews

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TabAdapter(val fmanager: FragmentManager): FragmentPagerAdapter(fmanager){

    val fragments = listOf(MyFeedFragment(),ExploreFragment(), DownloadsFragment())
    val titles = listOf("MY FEED","EXPLORE","DOWNLOADS")

    override fun getItem(p0: Int): Fragment {
        return this.fragments.get(p0)
    }

    override fun getCount(): Int {
        return this.fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return this.titles.get(position)
    }
}