package biz.belcorp.consultoras.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference

class CenteredImageSpan : ImageSpan {
    private var mDrawableRef: WeakReference<Drawable>? = null

    private var initialDescent = 0
    private var extraSpace = 0


    // Redefined locally because it is a private member from DynamicDrawableSpan
    private val cachedDrawable: Drawable?
        get() {
            val wr = mDrawableRef
            var d: Drawable? = null

            if (wr != null)
                d = wr.get()

            if (d == null) {
                d = drawable
                mDrawableRef = WeakReference(d)
            }

            return d
        }
    constructor(context: Context, drawableRes: Int) : super(context, drawableRes) {}

    constructor(drawableRes: Drawable, verticalAlignment: Int) : super(drawableRes, verticalAlignment) {}


    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        val d = cachedDrawable
        val rect = d?.bounds

        if (fm != null) {
            // Centers the text with the ImageSpan

            rect?.let {
                if (it.bottom - (fm.descent - fm.ascent) >= 0) {
                    // Stores the initial descent and computes the margin available
                    initialDescent = fm.descent
                    extraSpace = rect.bottom - (fm.descent - fm.ascent)
                }

                fm.descent = extraSpace / 2 + initialDescent
                fm.bottom = fm.descent

                fm.ascent = -it.bottom + fm.descent
                fm.top = fm.ascent
            }


        }

        return rect?.right?:0
    }

    override fun draw(canvas: Canvas, text: CharSequence,
                      start: Int, end: Int, x: Float,
                      top: Int, y: Int, bottom: Int, paint: Paint) {
        val b = cachedDrawable
        canvas.save()
        b?.let {
            var transY = bottom - it.bounds.bottom
            // this is the key
            transY -= paint.fontMetricsInt.descent / 2 - 2

            canvas.translate(x, transY.toFloat())
            it.draw(canvas)
            canvas.restore()
        }

    }
}
