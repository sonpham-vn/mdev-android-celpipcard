package com.example.celpipcard.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.celpipcard.R
import com.example.celpipcard.model.CardModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardModel =  CardModel(this)

        //Button Card List
        btncardlist.setOnClickListener {
            val intent = Intent(this, CardlistActivity::class.java)
            startActivity (intent)
        }

        //Button Practice
        btnpractice.setOnClickListener {
            val intent = Intent(this, PlaycardActivity::class.java)
            startActivity (intent)
        }

        //Button Download
        btndownload.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.dialog_title_download)
            builder.setMessage(R.string.dialog_message_download)

            builder.setPositiveButton("OK"){dialogInterface, which ->
                cardModel.downloadCard()
            }

            builder.setNegativeButton("Cancel", null)

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        //Button Backup
        btnbackup.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.dialog_title_backup)
            builder.setMessage(R.string.dialog_message_backup)

            builder.setPositiveButton("OK"){dialogInterface, which ->
                cardModel.backupCard()
            }

            builder.setNegativeButton("Cancel", null)

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        //Button Restore
        btnrestore.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.dialog_title_restore)
            builder.setMessage(R.string.dialog_message_restore)

            builder.setPositiveButton("OK"){dialogInterface, which ->
                cardModel.restoreCard()
            }

            builder.setNegativeButton("Cancel", null)

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }


    }
}