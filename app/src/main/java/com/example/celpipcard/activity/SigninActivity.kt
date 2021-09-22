package com.example.celpipcard.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.example.celpipcard.R
import com.example.celpipcard.model.SigninModel
import kotlinx.android.synthetic.main.sign_in.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
        val signinModel = SigninModel ()
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.btnlogin)
        val loading = findViewById<ProgressBar>(R.id.loading)

        // Observe sign in status
        val statusObserver = Observer<String> { newStatus ->
            if (newStatus.equals("Success")) {
                loading.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity (intent)
            } else if (newStatus.equals("Error")) {
                loading.visibility = View.GONE
            }
        }
        signinModel.currentStatus.observe(this, statusObserver)

        //Button Signin
        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            signinModel.login(this,username.text.toString(), password.text.toString())
        }

        //Button Signup
        btnsignup.setOnClickListener{
            loading.visibility = View.VISIBLE
            signinModel.signup(this,username.text.toString(), password.text.toString())
        }

    }

}