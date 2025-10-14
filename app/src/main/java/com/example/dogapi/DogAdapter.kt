package com.example.dogapi


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class DogAdapter(private val dogs: List<Dog>) :
    RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dogImageView: ImageView = itemView.findViewById(R.id.dogImageView)
        val breedTextView: TextView = itemView.findViewById(R.id.breedTextView)
        val countTextView: TextView = itemView.findViewById(R.id.countTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dog_item, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogs[position]

        // Data 1: Breed Name
        holder.breedTextView.text = dog.breed

        // Data 2: Dog Number
        holder.countTextView.text = "Item: #${dog.dogNumber}"

        // Data 3: Image
        Glide.with(holder.itemView.context)
            .load(dog.imageUrl)
            .into(holder.dogImageView)
    }

    override fun getItemCount() = dogs.size
}