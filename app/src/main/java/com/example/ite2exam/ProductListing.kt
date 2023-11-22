package com.example.ite2exam

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ProductListing : AppCompatActivity() {

    private lateinit var _prodListingImageView: ImageView
    private lateinit var _prodListingNameView: TextView
    private lateinit var _prodListingPriceView: TextView
    private lateinit var _prodListingDescView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_listing)

        _prodListingImageView = findViewById(R.id.prodListingImageView)
        _prodListingNameView = findViewById(R.id.prodListingNameView)
        _prodListingPriceView = findViewById(R.id.prodListingPriceView)
        _prodListingDescView = findViewById(R.id.prodListingDescView)

        val prodListingIntent: Intent = intent
        val prodListingImage: String = prodListingIntent.getStringExtra("prodImage").toString()
        val prodListingName: String = prodListingIntent.getStringExtra("prodName").toString()
        val prodListingPrice: String = prodListingIntent.getStringExtra("prodPrice").toString()
        val prodListingDesc: String = prodListingIntent.getStringExtra("prodDescription").toString()

        Glide.with(this).load(prodListingImage).into(_prodListingImageView)
        _prodListingNameView.text = prodListingName
        _prodListingPriceView.text = "₱${prodListingPrice}"
        _prodListingDescView.text = prodListingDesc
    }
}