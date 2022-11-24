package com.example.createimagemacros.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.createimagemacros.changeBackGroundColor
import com.example.createimagemacros.databinding.ActivityMainBinding
import com.example.createimagemacros.getBitmapFromView
import com.example.createimagemacros.getImageToShare

class MainActivity : AppCompatActivity(),View.OnTouchListener {
    private lateinit var binding: ActivityMainBinding
    private val SELECT_PICTURE = 200
    private var xDelta:Float = 0.0F
    private var yDelta:Float = 0.0F
    private var mScaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        
        val resultLauncher = registerForActivityResult()

        binding.marocsImage.setOnTouchListener(this)
        mScaleGestureDetector = ScaleGestureDetector(this, ScalingView(binding.marocsImage))

        binding.selectImageButton.setOnClickListener { imageChooser(resultLauncher) }

        binding.changeBackgroundButton.setOnClickListener { changeBackGroundColor(binding.root) }

        binding.marocsText.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d("vartika", "text changes afterText")

                addTouchListener()
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

        binding.shareButton.setOnClickListener {
            val bitmap = getBitmapFromView(binding.macros)
            shareImageAndText(bitmap)
        }
    }

    private fun registerForActivityResult() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            onActivityResult(SELECT_PICTURE, result)
        }
    }

    private fun addTouchListener() {
        binding.marocsText.setOnTouchListener(this)
        binding.marocsText.clearFocus()
    }

    // select image from gallery
    private fun imageChooser(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        resultLauncher.launch(intent)
    }

    private fun shareImageAndText(bitmap: Bitmap) {
        val uri = getImageToShare(bitmap, this)
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image")

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        // setting type to image
        intent.type = "image/png"

        val chooser = Intent.createChooser(intent, "Chooser Title")
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
}