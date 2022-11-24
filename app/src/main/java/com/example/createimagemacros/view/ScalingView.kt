package com.example.createimagemacros.view

import android.view.ScaleGestureDetector
import android.view.View

class ScalingView(private val view: View) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private var mScaleFactor = 1.0f
    // when a scale gesture is detected, use it to resize the image
    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        mScaleFactor *= scaleGestureDetector.scaleFactor
        view.setScaleX(mScaleFactor)
        view.setScaleY(mScaleFactor)
        return true
    }
}