package com.example.ite2exam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class AddItemActivity : AppCompatActivity() {

    private val TAG = "SandwichAdder"

    private lateinit var storageRef: StorageReference
    private lateinit var db: FirebaseFirestore

    private lateinit var _newProdNameTextbox: EditText
    private lateinit var _newProdDescTextbox: EditText
    private lateinit var _newProdPriceTextbox: EditText
    private lateinit var _addImageButton: Button
    private lateinit var _addProductButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        // Firebase Storage Instance
        storageRef = Firebase.storage.reference

        // Firestore Instance
        db = Firebase.firestore

        // Form inputs
        _newProdNameTextbox = findViewById(R.id.addNameTextbox)
        _newProdDescTextbox = findViewById(R.id.addDescriptionTextbox)
        _newProdPriceTextbox = findViewById(R.id.addPriceTextbox)
        _addImageButton = findViewById(R.id.addImageButton)
        _addProductButton = findViewById(R.id.addButton)

        _addProductButton.setOnClickListener {
            val newProdName: String = _newProdNameTextbox.text.toString()
            val newProdDesc: String = _newProdDescTextbox.text.toString()
            val newProdPrice: Number = _newProdPriceTextbox.text.toString().toInt()
            var imageURL: String =
                "https://firebasestorage.googleapis.com/v0/b/t2023it2-vapemart-cdbb4.appspot.com/o/uploads%2Fvape-mart-default-item.png?alt=media&token=ebab7481-1801-418c-b4fa-c844c90cb322"

            val newProductData = hashMapOf(
                "name" to newProdName,
                "description" to newProdDesc,
                "price" to newProdPrice,
                "images" to listOf<String>(imageURL)
            )

            if (newProdName != "" && newProdDesc != "" && newProdPrice != null) {
                db.collection("products").add(newProductData).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "New item added: ${it.result.id}")

                        val toProductsList: Intent = Intent(this, MainActivity::class.java)
                        startActivity(toProductsList)
                    } else Log.d(TAG, "Failed to add new item")
                }
            }
        }
    }

}