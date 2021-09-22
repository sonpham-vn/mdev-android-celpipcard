package com.example.celpipcard.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.celpipcard.R
import com.example.celpipcard.adapter.CardListAdapter
import com.example.celpipcard.model.CardData
import com.example.celpipcard.model.DatabaseHelper
import kotlinx.android.synthetic.main.card_list.*

class CardlistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.card_list)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        loadCardData()

        btnAdd.setOnClickListener {
            val intent = Intent(this, AddcardActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        loadCardData()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadCardData() {
        //query data
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val cardList: List<CardData> = databaseHelper.viewCard()

        //load data to viewlist
        val cardArrayContent = Array<String>(cardList.size){"null"}
        val cardArrayPretime = Array<Int?>(cardList.size){0}
        val cardArraySpktime = Array<Int?>(cardList.size){0}

        for((index, c) in cardList.withIndex()) {
            cardArrayContent[index] = c.CardContent.toString()
            cardArrayPretime[index] = c.PrepareTime
            cardArraySpktime[index] = c.SpeakTime
        }
        val cardListAdapter = CardListAdapter(
            this,
            cardArrayContent,
            cardArrayPretime,
            cardArraySpktime
        )
        cardListView.adapter = cardListAdapter
    }

}