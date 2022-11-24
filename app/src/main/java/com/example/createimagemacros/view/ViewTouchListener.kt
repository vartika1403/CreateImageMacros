package com.example.createimagemacros.view

import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

/** ViewTouch Listener class for scaling on touch of view and moving view around **/
class ViewTouchListener(val scaleGestureDetector: ScaleGestureDetector?): View.OnTouchListener {
    private var xDelta:Float = 0.0F
    private var yDelta:Float = 0.0F

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            scaleGestureDetector?.onTouchEvent(event)
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