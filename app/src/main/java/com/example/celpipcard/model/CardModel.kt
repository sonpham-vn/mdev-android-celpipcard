package com.example.celpipcard.model

import android.content.Context
import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class CardData(val Id: String? = null,
                    val OnId: String? = "",
                    val CardContent: String? = "",
                    val PrepareTime: Int? = 0,
                    val SpeakTime: Int? = 0 ): java.io.Serializable

@Serializable
data class CardDataBackup (
    val UserId: String,
    val CardArray: List<CardData>): java.io.Serializable


class CardModel(context: Context)  {

    private val mContext : Context =  context
    val sharedPref = context.getSharedPreferences("sharedpref",Context.MODE_PRIVATE)
    val userId = sharedPref.getString("userid", "")

    fun downloadCard() {
        //fetching cards json from server
        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.get("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/get-cards",
                object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                // called when response HTTP status is "200 OK"
                val strResponse = String(response)
                try {
                    val cardList = Json.decodeFromString<List<CardData>>(strResponse)
                    // Write data to DB
                    val databaseHelper: DatabaseHelper = DatabaseHelper(mContext)
                    var index = 0
                    var status: Long = 0
                    for (c in cardList) {
                        if (!databaseHelper.isCardExisted(c.OnId.toString())) {
                            status = databaseHelper.insertCard(cardList[index])
                            if (status <= -1) {
                                break
                            }
                        }
                        index++
                    }

                    if (status > -1) {
                        Toast.makeText(mContext, "Record update successful!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(mContext, "Record update failed!", Toast.LENGTH_LONG).show()
                    }
                } catch (e: SerializationException) { null }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                val errorResponse = String(errorResponse);
                Toast.makeText(mContext, "Server error! $errorResponse",Toast.LENGTH_LONG).show()
            }

        })
    }

    fun backupCard() {
        // get all cards from db
        val databaseHelper: DatabaseHelper= DatabaseHelper(mContext)
        val cardList: List<CardData> = databaseHelper.viewCard()
        // build params
        val params = RequestParams()
        params.put("UserId", userId)
        params.put("CardArray", Json.encodeToString(cardList))
        params.setUseJsonStreamer(true)
        //post to server
        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.post("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/put-backup", params,
            object : AsyncHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                    // called when response HTTP status is "200 OK"
                    val strResponse = String(response)
                    Toast.makeText(mContext, "Backup successful!",Toast.LENGTH_LONG).show()
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    val errorResponse = String(errorResponse);
                    Toast.makeText(mContext, "Server error! $errorResponse",Toast.LENGTH_LONG).show()
                }

            })
    }

    fun restoreCard() {
        //get data from server
        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.get("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/get-backup?UserId=$userId",
            object : AsyncHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                    // called when response HTTP status is "200 OK"
                    val strResponse = String(response)
                    // parse json
                    try {
                        val backupData = Json.decodeFromString<CardDataBackup>(strResponse)
                        // write data to db
                        val cardList = backupData.CardArray
                        val databaseHelper: DatabaseHelper = DatabaseHelper(mContext)
                        var index = 0
                        var status: Long = 0
                        //delete all current data of user
                        val deleteStatus = databaseHelper.deleteAllCard()
                        // put new cards
                        for (c in cardList) {
                            status = databaseHelper.insertCard(cardList[index])
                            if (status <= -1) {
                                break
                            }
                            index++
                        }
                        if (status > -1) {
                            Toast.makeText(mContext, "Record update successful!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(mContext, "Record update failed!", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: SerializationException) { null }
                }

                override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    val errorResponse = String(errorResponse);
                    Toast.makeText(mContext, "Server error! $errorResponse",Toast.LENGTH_LONG).show()
                }

            })
    }



}