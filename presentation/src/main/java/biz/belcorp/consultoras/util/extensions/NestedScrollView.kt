package biz.belcorp.consultoras.util.extensions

import android.annotation.SuppressLint
import android.support.v4.widget.NestedScrollView
import android.view.MotionEvent

interface NestedViewScrollChangedListener {
    fun onScrollStart()

    fun onScrollStop()
}

@SuppressLint("ClickableViewAccessibility")
fun NestedScrollView.onScrollStateChanged(listener: NestedViewScrollChangedListener) {
    this.setOnTouchListener { _, event ->
        when(event.action) {
            MotionEvent.ACTION_SCROLL, MotionEvent.ACTION_MOVE -> {
                handler.postDelayed({
                    listener.onScrollStart()
                }, 100)
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                handler.postDelayed({
                    listener.onScrollStop()
                }, 400)
            }
        }
        false // Don't consume touch events
    }
}
