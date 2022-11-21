package com.example.createimagemacros.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.createimagemacros.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val SELECT_PICTURE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImageButton.setOnClickListener { imageChooser() }

        binding.changeBackgroundButton.setOnClickListener { changeBackGroundColor() }
    }

    // change random background color
    private fun changeBackGroundColor() {
        val random = Random
        val color = Color.argb(255, random.nextInt(256), random.nextInt(256),
            random.nextInt(256))
        binding.root.setBackgroundColor(color)
    }

    // select image from gallery
    private fun imageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
           if (resultCode == RESULT_OK) {
               if (requestCode == SELECT_PICTURE) {
                   val selectImageUri = intent?.data
                   selectImageUri?.let {
                       binding.marocsImage.setImageURI(selectImageUri)
                   }
               }
           }
    }
}