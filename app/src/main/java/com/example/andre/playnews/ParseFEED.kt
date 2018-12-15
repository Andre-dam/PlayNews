package com.example.andre.playnews

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

object ParserFEED {

    //Este metodo faz o parsing de RSS gerando objetos ItemRSS
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(rssFeed: String): Tdi {
        val factory = XmlPullParserFactory.newInstance()
        val xpp = factory.newPullParser()
        xpp.setInput(StringReader(rssFeed))
        xpp.nextTag()
        return readRss(xpp)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun readRss(parser: XmlPullParser): Tdi {
        var items=Tdi("","","")
        parser.require(XmlPullParser.START_TAG, null, "rss")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "channel") {
                items = (readChannel(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun readChannel(parser: XmlPullParser): Tdi{
        val items=Tdi("","","")
        parser.require(XmlPullParser.START_TAG, null, "channel")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name

            if (name == "title") {
                items.title = readText(parser)
            }else if(name == "description"){
                items.description = readText(parser)
            }else if(name == "image"){
                items.image_url = readImage(parser)
            }  else {
                skip(parser)
            }
        }
        return items
    }


    @Throws(XmlPullParserException::class, IOException::class)
    fun readImage(parser: XmlPullParser): String {

        var img: String = ""
        parser.require(XmlPullParser.START_TAG, null, "image")
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "url") {
                img = readData(parser, "url")
            } else {
                skip(parser)
            }
        }
        return img
    }


    @Throws(XmlPullParserException::class, IOException::class)
    fun readItem(parser: XmlPullParser): ItemRSS {
        var title: String? = null
        var link: String? = null
        var pubDate: String? = null
        var description: String? = null
        var img: String? = null
        parser.require(XmlPullParser.START_TAG, null, "item")
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "title") {
                title = readData(parser, "title")
            } else if (name == "link") {
                link = readData(parser, "link")
            } else if (name == "pubDate") {
                pubDate = readData(parser, "pubDate")
            } else if (name == "description") {
                val pair = readDesc(parser, "description")
                description = pair.first
                img = pair.second
                //description = readDescription(parser, "description")
            } else {
                skip(parser)
            }
        }
        return ItemRSS(title!!, link!!, pubDate!!, description!!, img!!)
    }



    @Throws(IOException::class, XmlPullParserException::class)
    fun readDesc(parser: XmlPullParser, tag: String): Pair<String,String> {
        parser.require(XmlPullParser.START_TAG, null, tag)
        var data = ""
        var img_link = ""
        while(parser.nextToken() != XmlPullParser.END_TAG){
            if(parser.eventType == XmlPullParser.TEXT){
                data = data + parser.text
            }else if(parser.eventType == XmlPullParser.CDSECT){
                val factory = XmlPullParserFactory.newInstance()
                val linkparse = factory.newPullParser()
                linkparse.setInput(parser.text.reader())

                while (linkparse.next() != XmlPullParser.END_DOCUMENT) {
                    if (linkparse.eventType == XmlPullParser.START_TAG) {
                        val tag = linkparse.name
                        if (tag == "img") {
                            img_link = linkparse.getAttributeValue(null,"src")
                        }
                    }
                }
            }
        }
        parser.require(XmlPullParser.END_TAG, null, tag)
        return Pair(data.trim(),img_link)
    }

    // Processa tags de forma parametrizada no feed.
    @Throws(IOException::class, XmlPullParserException::class)
    fun readData(parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val data = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, tag)
        return data
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result.trim()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    /**/

}