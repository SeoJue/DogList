package com.example.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.toolbox.ImageLoader
import com.example.doglist.databinding.ActivityDogBinding

class DogActivity : AppCompatActivity() {
    companion object{
        const val KEY_BREED="DogBreed"
        const val KEY_SIZE="DogSize"
        const val KEY_COAT="DogCoat"
        const val KEY_IMAGE="DogImage"
    }

    private lateinit var binding: ActivityDogBinding
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoader = MySingleton.getInstance(getApplication()).imageLoader

        val breed= intent.getStringExtra(KEY_BREED)
        val size = intent.getStringExtra(KEY_SIZE)
        val coat = intent.getStringExtra(KEY_COAT)

        binding.textBreed.text = breed
        binding.textSize.text = size
        binding.textCoat.text = coat
        binding.imageDog.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)
    }
}