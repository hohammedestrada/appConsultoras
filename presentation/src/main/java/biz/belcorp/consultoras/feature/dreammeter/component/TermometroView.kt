package biz.belcorp.consultoras.feature.dreammeter.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import java.math.BigDecimal
import kotlin.math.roundToInt

class TermometroView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {

    private val TOTAL_PROGRESS = 0.8
    private val DURATION_ANIMATION = 3000

    private var withAnimation = true

    private var currentProgress = 0.0F

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val grayFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0.0f) })

    private var listener: Listener? = null

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2F
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (withAnimation) {
                val heightProgress = height.times(1.0f - currentProgress).roundToInt()
                if (heightProgress > 0) {
                    it.save()
                    it.clipRect(0, 0, width, heightProgress)
                    val oldFilter = colorFilter
                    colorFilter = grayFilter
                    super.onDraw(it)
                    colorFilter = oldFilter
                    it.restore()
                }
            }
            drawLines(it, width, height)
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun drawLines(canvas: Canvas, width: Int, heightReal: Int) {
        val height: Int = (heightReal.toFloat() * TOTAL_PROGRESS).toInt()
        val difHeigth = Math.max(0, heightReal - height)
        val w = width.toFloat() * 0.40F
        val x0: Float = width - w
        val x1: Float = width.toFloat()
        val y0fix = height.toFloat() - 2F / 2F
        for (x in 0..10) {
            var y0 = (x / 10F) * height.toFloat()
            y0 = Math.min(y0, y0fix)
            y0 = Math.max(y0, 2F / 2F)
            canvas.drawLine(x0, y0 + difHeigth, x1, y0 + difHeigth, paint)
        }
    }

    fun setTotalProgress(withAnimation: Boolean, totalProgress: BigDecimal) {
        var progress = totalProgress.div(100.toBigDecimal()).toFloat()
        if (progress < 1F) {
            progress = (progress * TOTAL_PROGRESS).toFloat()
        } else if (progress > 1F) {
            progress = 1F
        }

        if (withAnimation) {
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    super.applyTransformation(interpolatedTime, t)
                    currentProgress = (progress * interpolatedTime)
                    invalidate()
                    if (interpolatedTime == 1f)
                        listener?.onFinishedProgress(progress >= 1F)
                }
            }
            animation.duration = progress.times(DURATION_ANIMATION).toLong()
            startAnimation(animation)
        } else {
            this.withAnimation = withAnimation
            invalidate()
        }
    }


    interface Listener {
        fun onFinishedProgress(isComplete: Boolean)
    }

}
