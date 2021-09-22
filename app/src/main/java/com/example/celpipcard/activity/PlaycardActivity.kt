package com.example.celpipcard.activity

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer.OnChronometerTickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.celpipcard.R
import com.example.celpipcard.model.CardData
import com.example.celpipcard.model.DatabaseHelper
import kotlinx.android.synthetic.main.play_card.*


class PlaycardActivity : AppCompatActivity(){

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.play_card)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        //Load card list from db
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val cardList: List<CardData> = databaseHelper.viewCard()

        if (cardList.size <= 1)
            btnPlay.isEnabled = false

        if (cardList.isNotEmpty()) {
            var randomCard = cardList.random()
            contentText.text = randomCard.CardContent.toString()
            spktimeText.text = "Speak time: "
            pretimeText.text = "Prepare time: "
            view_timer_spk.isCountDown = true
            view_timer.isCountDown = true
            view_timer.base = SystemClock.elapsedRealtime() + (randomCard.PrepareTime?.times(1000) ?: 0)
            view_timer_spk.base = SystemClock.elapsedRealtime() + (randomCard.SpeakTime?.times(1000) ?: 0)
            view_timer.start()

            btnPlay.setOnClickListener {
                val currentCard = randomCard
                while (currentCard == randomCard)
                    randomCard = cardList.random()
                contentText.text = randomCard.CardContent.toString()
                spktimeText.text = "Speak time: "
                pretimeText.text = "Prepare time: "
                view_timer.base = SystemClock.elapsedRealtime() + (randomCard.PrepareTime?.times(1000) ?: 0)
                view_timer_spk.base = SystemClock.elapsedRealtime() + (randomCard.SpeakTime?.times(1000) ?: 0)
                view_timer.start()
            }

            view_timer.onChronometerTickListener = OnChronometerTickListener { chronometer ->
                val time = SystemClock.elapsedRealtime() - view_timer.base
                if (time>=0) {
                    view_timer.stop()
                    view_timer_spk.base = SystemClock.elapsedRealtime() + (randomCard.SpeakTime?.times(1000) ?: 0)
                    view_timer_spk.start()
                }
            }
            view_timer_spk.onChronometerTickListener = OnChronometerTickListener { chronometer ->
                val time = SystemClock.elapsedRealtime() - view_timer_spk.base
                if (time>=0) {
                    view_timer_spk.stop()
                }
            }

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}