package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.LogInCallback
import com.parse.ParseException
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if there's a user logged in
        // If there is, start MainActivity
//        if (ParseUser.getCurrentUser() != null) {
//            startMainActivity()
//        }

        setUpButtons()
    }

    private fun setUpButtons() {
        val username = findViewById<EditText>(R.id.etUsername).text
        val password = findViewById<EditText>(R.id.etPassword).text
        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            signUp(username.toString(), password.toString())
        }
        findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            signIn(username.toString(), password.toString())
        }
    }

    private fun signUp(username: String, password: String) {
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.username = username
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully created a new account
                Toast.makeText(this, "User ${user.username} has been created.", Toast.LENGTH_LONG).show()
                startMainActivity()
            } else {
                Toast.makeText(this, "Error signing up", Toast.LENGTH_LONG).show()
                Log.e("peter", "LoginActivity signUp: $e", )
            }
        }
    }

    private fun signIn(username: String, password: String) {
        ParseUser.logInInBackground(username, password, object: LogInCallback {
            override fun done(user: ParseUser?, e: ParseException?) {
                if (user != null) {
                    Log.i("peter", "LoginActivity signIn done: Successfully logged in user")
                    Toast.makeText(this@LoginActivity, "Signed in user ${user.username}", Toast.LENGTH_LONG).show()
                    startMainActivity()
                } else {
                    Log.e("peter", "LoginActivity signIn done: $e", )
                    Toast.makeText(this@LoginActivity, "Error signing in", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun startMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}