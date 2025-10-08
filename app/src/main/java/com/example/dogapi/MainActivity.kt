package com.example.dogapi

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // dogs seen counter
    private var dogCount = 0
    private lateinit var dogImageView: ImageView
    private lateinit var breedTextView: TextView
    private lateinit var countTextView: TextView
    private lateinit var newEntryButton: Button
    private lateinit var appTitleTextView: TextView
    private val randomUrl = "https://dog.ceo/api/breeds/image/random"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dogImageView = findViewById(R.id.dogImageView)
        breedTextView = findViewById(R.id.breedTextView)
        countTextView = findViewById(R.id.countTextView)
        newEntryButton = findViewById(R.id.newEntryButton)
        appTitleTextView = findViewById(R.id.appTitleTextView) // Initialize the header

        newEntryButton.setOnClickListener { loadDogImage() }
        loadDogImage()
    }

    private fun loadDogImage() {
        val client = AsyncHttpClient()

        client.get(randomUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                dogCount++

                val imageUrl = json.jsonObject.getString("message")

                // get breed name from url
                val breedPath = imageUrl.substringAfterLast("/breeds/").substringBeforeLast("/")
                val cleanBreedName = breedPath.replace("-", " ").uppercase(Locale.getDefault())


                runOnUiThread {
                    breedTextView.text = "Breed: $cleanBreedName"
                    countTextView.text = "Dogs Seen: $dogCount"
                    Glide.with(this@MainActivity)
                        .load(imageUrl)
                        .into(dogImageView)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("DOG_API_ERROR", "API Request Failed: $response", throwable)
                Toast.makeText(this@MainActivity, "Failed to load dog data.", Toast.LENGTH_SHORT).show()
                breedTextView.text = "Breed: ERROR LOADING"
                countTextView.text = "Dogs Seen: $dogCount (Error)"
            }
        })
    }
}