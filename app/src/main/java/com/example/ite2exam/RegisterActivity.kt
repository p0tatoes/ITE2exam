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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var _displaynameTextbox: EditText
    private lateinit var _emailTextbox: EditText
    private lateinit var _passwordTextbox: EditText
    private lateinit var _confirmPasswordTextbox: EditText
    private lateinit var _registerButton: Button

    private val TAG = "SandwichMaker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        db = Firebase.firestore

        _displaynameTextbox = findViewById(R.id.registerNameTextbox)
        _emailTextbox = findViewById(R.id.registerEmailTextbox)
        _passwordTextbox = findViewById(R.id.registerPasswordTextbox)
        _confirmPasswordTextbox = findViewById(R.id.registerConfirmPassTextbox)
        _registerButton = findViewById(R.id.registerRegisterButton)

        _registerButton.setOnClickListener {
            val password: String = _passwordTextbox.text.toString()
            val confirmPassword: String = _confirmPasswordTextbox.text.toString()

            if (isFilledUp() && isPasswordConfirmed(password, confirmPassword)) registerUser()
            else {
                Log.d(TAG, "registerUser: Registration Unsuccessful")
                val toast: Toast =
                    Toast.makeText(this, "Please fill up the fields correctly", Toast.LENGTH_LONG)
                toast.show()
            }

        }
    }

    // Returns true if all fields in the register form are filled, returns false otherwise
    private fun isFilledUp(): Boolean {
        val name: String = _displaynameTextbox.text.toString()
        val email: String = _emailTextbox.text.toString()
        val password: String = _passwordTextbox.text.toString()
        val confirmPassword: String = _confirmPasswordTextbox.text.toString()

        if (name == "") return false
        if (email == "") return false
        if (password == "") return false
        if (confirmPassword == "") return false

        return true
    }

    // Returns true if password and confirm password are the same, false otherwise
    private fun isPasswordConfirmed(password: String, confirmPassword: String): Boolean {
        Log.d(
            TAG,
            "isPasswordConfirmed: ${password} == ${confirmPassword} = ${password == confirmPassword} "
        )
        if (password == confirmPassword) return true

        return false
    }

    // Registers a user. Also stores display name linked to the user into the database
    private fun registerUser() {
        val name: String = _displaynameTextbox.text.toString()
        val email: String = _emailTextbox.text.toString()
        val password: String = _passwordTextbox.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "registerUser: Registration Successful")

                    val user: FirebaseUser = auth.currentUser!!
                    // Sets the user's display name when creating a password authenticated account
                    user.updateProfile(userProfileChangeRequest { displayName = name })

                    val newUser = hashMapOf(
                        "email" to user.email,
                        "display_name" to name
                    )
                    db.collection("users").add(newUser)
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                TAG,
                                "registerUser: User ${documentReference.id} added to database"
                            )
                        }.addOnFailureListener { e ->
                            Log.w(TAG, e)
                        }

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d(TAG, "registerUser: Registration Unsuccessful")
                }
            }
    }
}