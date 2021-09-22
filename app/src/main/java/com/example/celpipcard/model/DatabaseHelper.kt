package com.example.celpipcard.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "CardDB"
        private val TABLE_CARDS = "Cards"
        private val KEY_ID = "cardid"
        private val KEY_ONID = "onid"
        private val KEY_USERID = "userid"
        private val KEY_CONTENT = "content"
        private val KEY_PRETIME = "pretime"
        private val KEY_SPKTIME = "spktime"
    }
    val sharedPref = context.getSharedPreferences("sharedpref",Context.MODE_PRIVATE)
    val userId = sharedPref.getString("userid", "")

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CARD_TABLE = ("CREATE TABLE " + TABLE_CARDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERID + " TEXT,"
                + KEY_ONID + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_PRETIME + " INTERGER,"
                + KEY_SPKTIME + " INTERGER" + ")")
        db?.execSQL(CREATE_CARD_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //create new db when upgrade //not used
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS)
        onCreate(db)
    }


    //method to insert data
    fun insertCard(card: CardData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_USERID, userId)
        contentValues.put(KEY_ONID, card.OnId)
        contentValues.put(KEY_CONTENT, card.CardContent)
        contentValues.put(KEY_PRETIME, card.PrepareTime )
        contentValues.put(KEY_SPKTIME, card.SpeakTime )
        // Inserting Row
        val success = db.insert(TABLE_CARDS, null, contentValues)
        db.close()
        return success
    }

    //method to read data
    fun viewCard():List<CardData>{
        val cardList:ArrayList<CardData> = ArrayList<CardData>()
        val selectQuery = "SELECT  * FROM $TABLE_CARDS WHERE UserId='$userId'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return ArrayList()
        }

        // parse querydata to data class array
        var id: String
        var onid: String
        var content: String
        var preTime: Int
        var spkTime: Int
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex(KEY_ID))
                onid = cursor.getString(cursor.getColumnIndex(KEY_ONID))
                content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT))
                preTime = cursor.getInt(cursor.getColumnIndex(KEY_PRETIME))
                spkTime = cursor.getInt(cursor.getColumnIndex(KEY_SPKTIME))
                val card= CardData(Id = id, OnId = onid,
                    CardContent = content, PrepareTime = preTime, SpeakTime = spkTime)
                cardList.add(card)
            } while (cursor.moveToNext())
        }
        return cardList
    }


    //check card is existed to avoid duplicate
    fun isCardExisted(checkOnid: String): Boolean{
        val selectQuery = "SELECT  * FROM $TABLE_CARDS WHERE $KEY_ONID = '$checkOnid' AND $KEY_USERID = '$userId'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return true
        }
        if (cursor.moveToFirst()) {
            return true
        }
        return false
    }



    //method to delete data
    fun deleteAllCard():Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_USERID, userId) // CardData UserId
        // Deleting Row
        val success = db.delete(TABLE_CARDS, "$KEY_USERID='$userId'",null)
        db.close()
        return success
    }
}  