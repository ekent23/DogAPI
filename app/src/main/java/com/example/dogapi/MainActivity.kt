package com.example.dogapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dogRecyclerView: RecyclerView
    private lateinit var dogList: MutableList<Dog>
    private lateinit var dogAdapter: DogAdapter

    private val beagleUrl = "https://dog.ceo/api/breed/beagle/images"
    private val breedName = "Beagle"
    private val maxDogs = 100 // cap the number of dogs displayed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dogList = mutableListOf()
        dogRecyclerView = findViewById(R.id.dogRecyclerView)
        dogAdapter = DogAdapter(dogList)
        dogRecyclerView.adapter = dogAdapter
        dogRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchDogImages()
    }

    private fun fetchDogImages() {
        val client = AsyncHttpClient()

        client.get(beagleUrl, object : JsonHttpResponseHandler() { // <-- Using beagleUrl
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val imageUrls: JSONArray = json.jsonObject.getJSONArray("message")

                    val limit = minOf(imageUrls.length(), maxDogs)

                    for (i in 0 until limit) {
                        val imageUrl = imageUrls.getString(i)
                        val dog = Dog(
                            imageUrl = imageUrl,
                            breed = breedName.uppercase(Locale.getDefault()),
                            dogNumber = i + 1
                        )
                        dogList.add(dog)
                    }]
                    dogAdapter.notifyDataSetChanged()

                } catch (e: Exception) {
                    Log.e("API_PARSE_ERROR", "Error parsing JSON array: ${e.message}")
                    Toast.makeText(this@MainActivity, "Failed to parse Beagle data.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("API_ERROR", "API Request Failed: $statusCode", throwable)
                Toast.makeText(this@MainActivity, "Failed to load Beagle list.", Toast.LENGTH_LONG).show()
            }
        })
    }
}
