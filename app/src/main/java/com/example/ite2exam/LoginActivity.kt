package com.example.ite2exam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var _emailTextbox: EditText
    private lateinit var _passwordTextbox: EditText
    private lateinit var _loginButton: Button
    private lateinit var _registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        _emailTextbox = findViewById(R.id.emailTextbox)
        _passwordTextbox = findViewById(R.id.passwordTextbox)
        _loginButton = findViewById(R.id.loginButton)
        _registerButton = findViewById(R.id.registerButton)

        _loginButton.setOnClickListener {
            var loginEmail: String = _emailTextbox.text.toString()
            var loginPassword: String = _passwordTextbox.text.toString()

            signInUser(loginEmail, loginPassword)
        }

        _registerButton.setOnClickListener {
            changeActivity(RegisterActivity::class.java)
        }
    }

    // Checks if there is a user logged in when activity starts. Used to redirect to the Main activity after successful registration
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            changeActivity(MainActivity::class.java)
        }
    }

    private fun signInUser(email: String, password: String) {
        val TAG = "SandwichCheck"
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInUser: Sign in successful")
                    _emailTextbox.text.clear()
                    _passwordTextbox.text.clear()
                    changeActivity(MainActivity::class.java)
                } else {
                    Log.d(TAG, "signInUser: Sign in failed")
                    val toast = Toast.makeText(
                        this,
                        "Login Unsuccessful. Check if your Email and Password are correct",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Login Unsuccessful. Check if your Email and Password are correct",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun changeActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }
}