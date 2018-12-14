package com.example.andre.playnews


class ItemRSS(val title: String, val link: String, val pubDate: String, val description: String, val img: String) {

    override fun toString(): String {
        return title
    }
}