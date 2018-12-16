package com.example.andre.playnews

import android.app.Activity
import android.widget.LinearLayout

open class FloatingActivity:Activity() {
    fun setContentView(layoutResID: Int, verticalDP: Int, horizontalDP: Int) {
        var v = LinearLayout(this)

        var paddingDp = verticalDP
        var density = this.getResources().getDisplayMetrics().density
        var paddingPixel = (paddingDp * density).toInt()

        paddingDp = horizontalDP
        density = this.getResources().getDisplayMetrics().density
        var paddingPixelh = (paddingDp * density).toInt()
        v.setPadding(paddingPixelh,paddingPixel,paddingPixelh,paddingPixel);

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)

        super.setContentView(v,lp)
        layoutInflater.inflate(layoutResID,v)
    }
}