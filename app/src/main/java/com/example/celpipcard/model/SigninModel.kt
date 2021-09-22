package com.example.celpipcard.model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class User (val UserId: String, val UserName: String): java.io.Serializable

class SigninModel()  {

    val currentStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun login(context: Context, username: String, password: String) {
        val params = RequestParams()
        params.put("UserName", username)
        params.put("Password", password)
        params.setUseJsonStreamer(true)

        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.post("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/sign-in",
                params, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                // called when response HTTP status is "200 OK"
                val strResponse = String(response)
                try {
                    val userInfo = Json.decodeFromString<User>(strResponse)
                    val sharedPref = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                            ?: return
                    with(sharedPref.edit()) {
                        putString("userid", userInfo.UserId.toString())
                        apply()
                    }

                    currentStatus.setValue("Success")
                } catch (e: SerializationException) { null }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                val errorResponse = String(errorResponse)
                Toast.makeText(context, "Server error! $errorResponse", Toast.LENGTH_LONG).show()
                currentStatus.setValue("Error")
            }

        })




    }

    fun signup(context: Context, username: String, password: String) {
        // can be launched in a separate asynchronous job
        val params = RequestParams()
        val mContext = context
        params.put("UserName", username)
        params.put("Password", password)
        params.setUseJsonStreamer(true)

        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.post("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/sign-up", params, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                // called when response HTTP status is "200 OK"
                val strResponse = String(response)
                try {
                    val userInfo = Json.decodeFromString<User>(strResponse)
                    val sharedPref = mContext.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                            ?: return
                    with(sharedPref.edit()) {
                        putString("userid", userInfo.UserId.toString())
                        apply()
                    }

                    currentStatus.setValue("Success")
                } catch (e: SerializationException) { null }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                val errorResponse = String(errorResponse)
                Toast.makeText(mContext, "Server error! $errorResponse", Toast.LENGTH_LONG).show()
                currentStatus.setValue("Error")
            }

        })


    }

}