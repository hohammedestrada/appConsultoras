package biz.belcorp.consultoras.feature.caminobrillante.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.widget.ProgressBar
import biz.belcorp.consultoras.R

class CustomProgressBar : ProgressBar {

    private var mBackgroundColor = Color.GRAY
    private var mProgressColor = Color.BLUE

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init() {
        progressDrawable = ResourcesCompat.getDrawable(resources, R.drawable.rounded_progress_bar_horizontal, null) as LayerDrawable?
        setProgressColors(mBackgroundColor, mProgressColor)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        setUpStyleable(context, attrs)
        progressDrawable = ResourcesCompat.getDrawable(resources, R.drawable.rounded_progress_bar_horizontal, null) as LayerDrawable?
        setProgressColors(mBackgroundColor, mProgressColor)
    }

    private fun setUpStyleable(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar)

        mBackgroundColor = typedArray.getColor(R.styleable.CustomProgressBar_backgroundColor, Color.YELLOW)
        mProgressColor = typedArray.getColor(R.styleable.CustomProgressBar_progressColor, Color.BLUE)

        typedArray.recycle()
    }

    fun setProgressColors(backgroundColor: Int, progressColor: Int) {
        val layerDrawable = progressDrawable as LayerDrawable
        val gradientDrawable = layerDrawable.findDrawableByLayerId(android.R.id.background) as GradientDrawable
        gradientDrawable.setColor(backgroundColor)

        val scaleDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress) as ScaleDrawable
        val progressGradientDrawable = scaleDrawable.drawable as GradientDrawable
        progressGradientDrawable.setColor(progressColor)
        progressDrawable = layerDrawable

    }

    fun animateProgress(animationDuration: Int, to: Int) {
        val progressBarAnimation = ProgressBarAnimation(this, progress.toFloat(), to.toFloat())
        progressBarAnimation.duration = animationDuration.toLong()
        startAnimation(progressBarAnimation)
    }

}
