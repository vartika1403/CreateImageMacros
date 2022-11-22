package com.example.createimagemacros.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.createimagemacros.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity(),View.OnTouchListener {
    private lateinit var binding: ActivityMainBinding
    private val SELECT_PICTURE = 200
    private var xDelta:Float = 0.0F
    private var yDelta:Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImageButton.setOnClickListener { imageChooser() }

        binding.changeBackgroundButton.setOnClickListener { changeBackGroundColor() }

        binding.editTextButton.setOnClickListener { binding.marocsText.visibility = View.VISIBLE
        binding.marocsText.setOnTouchListener(this)}
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        val x = event?.rawX?.toInt()
        val y = event?.rawY?.toInt()

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (x != null)
                    xDelta = view?.x?.minus(x) ?: xDelta
                if (y != null)
                    yDelta = view?.y?.minus(y) ?: yDelta
            }
            MotionEvent.ACTION_MOVE -> {
               view?.animate()?.x(event.rawX + xDelta)?.y(event.rawY + yDelta)?.setDuration(0)?.start()
            }
            else -> { return false}
        }
        return true
    }
}