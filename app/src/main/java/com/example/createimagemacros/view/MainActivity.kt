package com.example.createimagemacros.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.createimagemacros.addStrictMode
import com.example.createimagemacros.changeBackGroundColor
import com.example.createimagemacros.databinding.ActivityMainBinding
import com.example.createimagemacros.getBitmapFromView
import com.example.createimagemacros.getImageUrlToShare

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val SELECT_PICTURE = 200

    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addStrictMode()
        
        val resultLauncher = registerForActivityResult()

        scaleGestureDetector = ScaleGestureDetector(this, ScalingView(binding.marocsImage))

        binding.marocsImage.setOnTouchListener(ViewTouchListener(scaleGestureDetector))

        binding.selectImageButton.setOnClickListener { imageChooser(resultLauncher) }

        binding.changeBackgroundButton.setOnClickListener { changeBackGroundColor(binding.root) }

        addTextChangedListener(binding.marocsText)

        binding.editTextButton.setOnClickListener {
            binding.marocsText.visibility = View.VISIBLE
            binding.marocsText.requestFocus()
        }

        binding.shareButton.setOnClickListener {
            val bitmap = getBitmapFromView(binding.macros)
            shareMacrosImage(bitmap)
        }
    }

    // added text listener on text changes
    private fun addTextChangedListener(editText: EditText) {
        editText.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                addTouchListener()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                editText.setBackgroundResource(android.R.color.black);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editText.setBackgroundResource(android.R.color.transparent);
            }
        })
    }

    private fun registerForActivityResult() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            onActivityResult(SELECT_PICTURE, result)
        }
    }

    private fun addTouchListener() {
        binding.marocsText.setOnTouchListener(ViewTouchListener(scaleGestureDetector))
        binding.marocsText.clearFocus()
    }

    // select image from gallery
    private fun imageChooser(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        resultLauncher.launch(intent)
    }

    // add sharing component for the macrosImage
    private fun shareMacrosImage(bitmap: Bitmap) {
        val uri = getImageUrlToShare(bitmap, this)
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        // setting type to image
        intent.type = "image/png"

        val chooser = Intent.createChooser(intent, "Share")
        chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(chooser)
    }

     fun onActivityResult(requestCode: Int, result: ActivityResult) {
           if (result.resultCode == RESULT_OK) {
               val intent = result.data
               if (requestCode == SELECT_PICTURE) {
                   val selectImageUri = intent?.data
                   selectImageUri?.let {
                       binding.marocsImage.setImageURI(selectImageUri)
                   }
               }
           }
    }
}