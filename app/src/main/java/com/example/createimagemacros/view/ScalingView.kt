package com.example.createimagemacros.view

import android.view.ScaleGestureDetector
import android.view.View

class ScalingView(private val view: View) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private var scaleFactor = 1.0f
    // when a scale gesture is detected, use it to resize the image
    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        scaleFactor *= scaleGestureDetector.scaleFactor
        view.setScaleX(scaleFactor)
        view.setScaleY(scaleFactor)
        return true
    }
}