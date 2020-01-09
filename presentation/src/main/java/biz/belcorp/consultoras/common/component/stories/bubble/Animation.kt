package biz.belcorp.consultoras.common.component.stories.bubble

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator


object Animation {
    private const val DURATION = 2000L

    fun getLoopAnimator() = ValueAnimator.ofFloat(0f, 360f).apply {
        repeatCount = ValueAnimator.INFINITE
        duration = DURATION
        interpolator = LinearInterpolator()
    }!!

    fun getSetupAnimator() =
            ValueAnimator.ofFloat(0f, 1f).apply {
                interpolator = LinearInterpolator()
            }!!
}
