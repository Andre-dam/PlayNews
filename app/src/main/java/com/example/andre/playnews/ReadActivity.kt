package com.example.andre.playnews

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.android.synthetic.main.content_read.*
import org.jsoup.Jsoup
import java.util.*

class ReadActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        setContentView(R.layout.activity_read)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            if (!tts!!.isSpeaking) {
                fab!!.setImageResource(R.drawable.ic_media_pause)
                fab!!.hide()
                fab!!.show()
                text = rss_content_body.text.toString()
                tts!!.apply {
                    setPitch(1F)
                    setSpeechRate(1.5F)
                    speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
                }
            }
            else {
                fab!!.setImageResource(R.drawable.ic_media_play)
                fab!!.hide()
                fab!!.show()
                tts!!.stop()
            }
        }
        val mImage = findViewById<ImageView>(R.id.image_bar_id)
        val rssContentTextBody = findViewById<TextView>(R.id.rss_content_body)
        val bundle = intent.extras
        if (bundle != null) {
            val readableItem = DownloadContent().execute(bundle.getString("link"), bundle.getString("url")).get()
            rssContentTextBody.text = readableItem.mBody
            if (readableItem.mImage != null && !"".equals(bundle.getString("url")))
                readableItem.mImage.into(mImage)
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class DownloadContent : AsyncTask<String, String, ReadableItem>() {

        override fun doInBackground(vararg params: String?): ReadableItem {
            var crop: RequestCreator? = null
            val body = Jsoup.connect(params[0]).get().body()
            if (params[1] != null && !"".equals(params[1])) {
                crop = Picasso.get().load(params[1]).resize(480, 180).centerCrop()
            }
            val bodyContent = body.getElementsByTag("p").text()
            return ReadableItem(crop, bodyContent)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set Pt-Br as language for tts
            val myLocale = Locale("pt", "BR")
            val result = tts!!.setLanguage(myLocale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                fab!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    override fun onDestroy() {
        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}
