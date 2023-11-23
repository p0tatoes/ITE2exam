package com.example.ite2exam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var _welcomeTextView: TextView
    private lateinit var _signoutButton: Button
    private lateinit var _productsListLayout: LinearLayout

    // back press event callback; just finishes the activity
    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d("SandwichViewer", "handleOnBackPressed: Uhhhh")
            auth.signOut()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firestore Instance
        db = Firebase.firestore

        // Firebase Authentication Instance
        auth = Firebase.auth
        val currentUser: FirebaseUser = auth.currentUser!!

        // UI Elements
        _productsListLayout = findViewById(R.id.productListLayout)
        _welcomeTextView = findViewById(R.id.welcomeTextView)
        _signoutButton = findViewById(R.id.signoutButton)

        // Retrieves registered display name
        val name = currentUser.displayName
        _welcomeTextView.text = "Welcome, ${name}"

        // Handles back press events
        onBackPressedDispatcher.addCallback(this, backPressCallback)

        // Signout button function
        _signoutButton.setOnClickListener {
            auth.signOut()
            finish()
        }

        // Retrieves all items in the firestore database
        db.collection("products").get().addOnSuccessListener { results ->
            for (product in results) {
                val prodName = product.data["name"].toString()
                val prodPrice = product.data["price"].toString()
                val arrProdImages = product.get("images") as List<String>
                val prodImageURL = arrProdImages[0]
                val prodDescription = product.data["description"].toString()

                // Populates res/product_card.xml interface with product information
                val productItemView = layoutInflater.inflate(R.layout.product_card, null)

                val prodImageView: ImageView = productItemView.findViewById(R.id.prodImageView)
                val prodNameView: TextView = productItemView.findViewById(R.id.prodNameTextView)
                val prodPriceView: TextView =
                    productItemView.findViewById(R.id.prodPriceTextView)

                prodNameView.text = prodName
                prodPriceView.text = "₱${prodPrice}"
                Glide.with(this).load(prodImageURL).into(prodImageView)

                // Adds an event listener for click a product in the list. Opens a new activity containing all product details of the clicked product
                productItemView.setOnClickListener {
                    val toProductListing: Intent = Intent(this, ProductListing::class.java)
                    toProductListing.putExtra("prodName", prodName)
                    toProductListing.putExtra("prodPrice", prodPrice)
                    toProductListing.putExtra("prodImage", prodImageURL)
                    toProductListing.putExtra("prodDescription", prodDescription)
                    startActivity(toProductListing)
                }

                _productsListLayout.addView(productItemView)
            }
        }
    }
}