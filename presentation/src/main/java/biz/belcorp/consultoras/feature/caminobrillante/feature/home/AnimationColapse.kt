package biz.belcorp.consultoras.feature.caminobrillante.feature.home

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout

object AnimationColapse {

    private var currHeight = 0

    fun expand(v: View) {

        currHeight = v.height

        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight - currHeight

        v.layoutParams.height = currHeight
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {

                v.layoutParams.height = if (interpolatedTime == 1f)
                    LinearLayout.LayoutParams.WRAP_CONTENT
                else
                    currHeight + (targetHeight * interpolatedTime).toInt()

                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = 1000
        v.startAnimation(a)
    }

    fun collapse(v: View, listener: onFinishAnimator?) {

        val initialHeight = Math.max(0, v.measuredHeight - currHeight)

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else currHeight + (initialHeight * (1 - interpolatedTime)).toInt()
                v.requestLayout()
                if (interpolatedTime == 1f && listener != null) {
                    listener.onFinished()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = 1000
        v.startAnimation(a)
    }

    interface onFinishAnimator {

        fun onFinished()

    }

}
