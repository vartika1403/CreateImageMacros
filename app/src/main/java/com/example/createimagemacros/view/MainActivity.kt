package com.example.createimagemacros.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.createimagemacros.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity(),View.OnTouchListener {
    private lateinit var binding: ActivityMainBinding
    private val SELECT_PICTURE = 200
    private var xDelta:Float = 0.0F
    private var yDelta:Float = 0.0F
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        binding.selectImageButton.setOnClickListener { imageChooser() }

        binding.changeBackgroundButton.setOnClickListener { changeBackGroundColor() }

        binding.marocsText.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d("vartika", "text changes afterText")

                addTouchListner()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("vartika", "text changes beforeText")
                binding.marocsText.setBackgroundResource(android.R.color.black);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("vartika", "text changes onText")
                binding.marocsText.setBackgroundResource(android.R.color.transparent);

            }
        })

        binding.editTextButton.setOnClickListener {
            binding.marocsText.visibility = View.VISIBLE
            binding.marocsText.requestFocus()
        }
        binding.marocsImage.setOnTouchListener(this)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addTouchListner() {
        binding.marocsText.setOnTouchListener(this)
        binding.marocsText.clearFocus()
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
        if (event != null) {
            mScaleGestureDetector?.onTouchEvent(event)
        }
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

    inner class ScaleListener : SimpleOnScaleGestureListener() {
        // when a scale gesture is detected, use it to resize the image
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            binding.marocsImage.setScaleX(mScaleFactor)
            binding.marocsImage.setScaleY(mScaleFactor)
            return true
        }
    }
}