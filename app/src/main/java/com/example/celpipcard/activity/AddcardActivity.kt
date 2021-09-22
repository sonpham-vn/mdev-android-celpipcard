package com.example.celpipcard.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.celpipcard.R
import com.example.celpipcard.adapter.CardListAdapter
import com.example.celpipcard.model.CardData
import com.example.celpipcard.model.DatabaseHelper
import kotlinx.android.synthetic.main.add_card.*

class AddcardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_card)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)


        btnAddCard.setOnClickListener {
            val databaseHelper: DatabaseHelper = DatabaseHelper(this)
            val newCard = CardData (CardContent = u_content.text.toString(),
                PrepareTime = u_pretime.text.toString().toInt(),
                SpeakTime = u_spktime.text.toString().toInt()
            )
            val status = databaseHelper.insertCard(newCard)
            if (status > -1) {
                Toast.makeText(this,"Record update successful!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Record update failed!", Toast.LENGTH_LONG).show()
            }
            onBackPressed()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}