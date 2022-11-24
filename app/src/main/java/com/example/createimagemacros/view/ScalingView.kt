package com.example.createimagemacros.view

import android.view.ScaleGestureDetector
import android.view.View

/** add scaling view class for zooming in/out the view **/
class ScalingView(private val view: View) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private var scaleFactor = 1.0f
    // when a scale gesture is detected, use it to zoom in/out the view
    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        scaleFactor *= scaleGestureDetector.scaleFactor
        view.setScaleX(scaleFactor)
        view.setScaleY(scaleFactor)
        return true
    }
}