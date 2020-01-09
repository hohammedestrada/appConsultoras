package biz.belcorp.consultoras.common.component.stories.bubble

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.widget.ImageView
import android.os.Build
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import android.support.annotation.RequiresApi
import android.view.View
import android.graphics.Shader
import android.graphics.BitmapShader
import android.graphics.RectF
import android.view.MotionEvent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.v7.widget.AppCompatImageView
import biz.belcorp.consultoras.R

class StorieAvatarView : AppCompatImageView {
    private val avatarDrawableRect = RectF()
    private val middleRect = RectF()
    private val borderRect = RectF()
    private val arcBorderRect = RectF()

    private val shaderMatrix = Matrix()
    private val bitmapPaint = Paint()
    private val middlePaint = Paint()
    private val borderPaint = Paint()
    private val circleBackgroundPaint = Paint()

    private var middleThickness = 0f
    private val avatarInset
        get() = distanceToBorder + Math.max(borderThickness.toFloat(), highlightedBorderThickness.toFloat())

    private var avatarDrawable: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private var drawableRadius = 0f
    private var middleRadius = 0f
    private var borderRadius = 0f

    private var animationArchesSparseness = 0f

    /**
     * The lenght (in degrees) available for the arches when animating.
     */
    var totalArchesDegreeArea = Defaults.ARCHES_DEGREES_AREA
        set(value) {
            field = value
            setup()
        }

    /**
     * The number of arches displayed across the border when animating.
     * This can be set to zero, if no secondary arches are wanted.
     */
    var numberOfArches = Defaults.NUMBER_OF_ARCHES
        set(value) {
            field = if (value <= 0) 1 else value
            setup()
        }
    /**
     * The lenght (in degrees) of each arch when animating.
     * Keep in mind that the arches may overlap if this value is too high
     * and [totalArchesDegreeArea] is too low.
     */
    var individualArcDegreeLenght = Defaults.INDIVIDUAL_ARCH_DEGREES_LENGHT
        set(value) {
            field = value
            setup()
        }

    /**
     * The color of the gap between the border and the avatar.
     * Default: [Color.TRANSPARENT]
     */
    var middleColor = Defaults.MIDDLE_COLOR
        set(value) {
            field = value
            setup()
        }
    /**
     * The border color.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [borderColorEnd] is set to the same value.
     */
    var borderColor = Defaults.BORDER_COLOR
        set(value) {
            field = value
            setup()
        }
    /**
     * The second color of the border gradient.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [borderColor] is set to the same value.
     */
    var borderColorEnd = Defaults.BORDER_COLOR
        set(value) {
            field = value
            setup()
        }
    /**
     * The border color when highlighted.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [highlightBorderColorEnd] is set to the same value.
     */
    var highlightBorderColor = Defaults.BORDER_COLOR_HIGHLIGHT
        set(value) {
            field = value
            setup()
        }
    /**
     * The second color of the border gradient when highlighted.
     * Remember: The border is colored using a gradient.
     * If you want a solid color, make sure that the [highlightBorderColor] is set to the same value.
     */
    var highlightBorderColorEnd = Defaults.BORDER_COLOR_HIGHLIGHT
        set(value) {
            field = value
            setup()
        }

    /**
     * The distance (in pixels) between the avatar and the border.
     * Keep in mind that as the [borderThickness] and [highlightedBorderThickness] may be different,
     * the highest value between them will be considered in order to keep a steady avatar when
     * switching between highlight modes.
     */
    var distanceToBorder = Defaults.DISTANCE_TO_BORDER
        set(value) {
            field = if (value <= 0) 1 else value
            setup()
        }
    /**
     * The border thickness (in pixels) when not highlighted
     */
    var borderThickness = Defaults.BORDER_THICKNESS
        set(value) {
            field = if (value <= 0) 1 else value
            setup()
        }
    /**
     * The border thickness (in pixels) when highlighted
     */
    var highlightedBorderThickness = Defaults.HIGHLIGHTED_BORDER_THICKNESS
        set(value) {
            field = if (value <= 0) 1 else value
            setup()
        }

    /**
     * The background color of the avatar.
     * Only visible if the image has any transparency.
     */
    var avatarBackgroundColor = Defaults.CIRCLE_BACKGROUND_COLOR
        set(value) {
            field = value
            setup()
        }

    /**
     * The default onClick and onLongClick manifestation is a simple scale/zoom bounce.
     * It must have a onClickListener or onLongClickListener in order to happen.
     * If you want to turn it off by any reason, just set this to false.
     */
    var shouldBounceOnClick = true

    private val spaceBetweenArches
        get() = totalArchesDegreeArea / (numberOfArches) - individualArcDegreeLenght

    private val currentAnimationArchesArea
        get() = animationArchesSparseness * totalArchesDegreeArea

    private var animationLoopDegrees = 0f
    private var isReadingAttributes = false

    /**
     * Set if this view should be highlighted or not.
     * If highlighted, the values of [highlightedBorderThickness], [highlightBorderColor] and [highlightBorderColorEnd] will apply
     * Otherwise, [borderThickness], [borderColor] and [borderColorEnd] will come into play.
     */
    var isHighlighted = false
        set(value) {
            field = value
            setup()
        }

    private val scaleAnimator = ValueAnimator.ofFloat(1f, 0.9f, 1f).apply {
        addUpdateListener {
            this@StorieAvatarView.scaleX = it.animatedValue as Float
            this@StorieAvatarView.scaleY = it.animatedValue as Float
        }
    }

    private val setupAnimator = Animation.getSetupAnimator().apply {
        addUpdateListener {
            animationArchesSparseness = it.animatedValue as Float
            invalidate()
        }
        addListener(onAnimationEnd {
            if (isReversedAnimating) {
                animationLoopDegrees = 0f
                isReversedAnimating = false
            }
        })
    }
    private val loopAnimator = Animation.getLoopAnimator().apply {
        addUpdateListener {
            animationLoopDegrees = it.animatedValue as Float
            invalidate()
        }
    }
    private val animatorSet = AnimatorSet().apply {
        playSequentially(setupAnimator, loopAnimator)
    }

    private var isReversedAnimating = false

    var isAnimating = false
        set(value) {
            if (value && !field) {
                if (isReversedAnimating) {
                    setupAnimator.reverse()
                }
                animatorSet.start()
            } else if (!value && field) {
                isReversedAnimating = true
                animatorSet.cancel()
                setupAnimator.reverse()
            }
            field = value
            setup()
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        readAttrs(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        readAttrs(attrs, defStyle)
        init()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }


    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    private fun readAttrs(attrs: AttributeSet, defStyle: Int = 0) {
        isReadingAttributes = true
        val a = context.obtainStyledAttributes(attrs, R.styleable.StorieBubble, defStyle, 0)

        avatarBackgroundColor = a.getColor(R.styleable.StorieBubble_sbb_circle_background_color,
            Defaults.CIRCLE_BACKGROUND_COLOR)

        distanceToBorder = a.getDimensionPixelSize(R.styleable.StorieBubble_sbb_distance_to_border, Defaults.DISTANCE_TO_BORDER)
        borderThickness = a.getDimensionPixelSize(R.styleable.StorieBubble_sbb_border_thickness, Defaults.BORDER_THICKNESS)
        highlightedBorderThickness = a.getDimensionPixelSize(R.styleable.StorieBubble_sbb_border_thickness_highlight, Defaults.HIGHLIGHTED_BORDER_THICKNESS)

        middleColor = a.getColor(R.styleable.StorieBubble_sbb_middle_color, Defaults.MIDDLE_COLOR)
        borderColor = a.getColor(R.styleable.StorieBubble_sbb_border_color, Defaults.BORDER_COLOR)
        borderColorEnd = a.getColor(R.styleable.StorieBubble_sbb_border_color_end, borderColor)
        highlightBorderColor = a.getColor(R.styleable.StorieBubble_sbb_border_highlight_color, Defaults.BORDER_COLOR_HIGHLIGHT)
        highlightBorderColorEnd = a.getColor(R.styleable.StorieBubble_sbb_border_highlight_color_end, highlightBorderColor)

        isHighlighted = a.getBoolean(R.styleable.StorieBubble_sbb_highlighted, Defaults.IS_HIGHLIGHTED)

        totalArchesDegreeArea = a.getFloat(R.styleable.StorieBubble_sbb_loading_arches_degree_area, Defaults.ARCHES_DEGREES_AREA)
        numberOfArches = a.getInt(R.styleable.StorieBubble_sbb_loading_arches, Defaults.NUMBER_OF_ARCHES)
        individualArcDegreeLenght = a.getFloat(R.styleable.StorieBubble_sbb_loading_arc_degree_lenght, Defaults.INDIVIDUAL_ARCH_DEGREES_LENGHT)

        a.recycle()
        isReadingAttributes = false
    }

    private fun init() {
        scaleType = Defaults.SCALE_TYPE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = OutlineProvider()
        }
        setup()
    }

    private fun setup() {
        if (isReadingAttributes) {
            return
        }
        if (width == 0 && height == 0) {
            return
        }

        avatarDrawable?.let{
            bitmapHeight = it.height
            bitmapWidth = it.width

            bitmapShader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            bitmapPaint.isAntiAlias = true
            bitmapPaint.shader = bitmapShader

            val currentBorderThickness = (if (isHighlighted)
                highlightedBorderThickness
            else borderThickness).toFloat()

            borderRect.set(calculateBounds())
            borderRadius = Math.min((borderRect.height() - currentBorderThickness) / 2.0f, (borderRect.width() - currentBorderThickness) / 2.0f)

            val currentBorderGradient = LinearGradient(0f, 0f, borderRect.width(), borderRect.height(),
                if (isHighlighted) highlightBorderColor else borderColor,
                if (isHighlighted) highlightBorderColorEnd else borderColorEnd,
                Shader.TileMode.CLAMP)
            borderPaint.apply {
                shader = currentBorderGradient
                strokeWidth = currentBorderThickness
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                style = Paint.Style.STROKE
            }

            avatarDrawableRect.set(borderRect)
            avatarDrawableRect.inset(avatarInset, avatarInset)
            middleThickness = (borderRect.width() - currentBorderThickness * 2 - avatarDrawableRect.width()) / 2

            middleRect.set(borderRect)
            middleRect.inset(currentBorderThickness + middleThickness / 2, currentBorderThickness + middleThickness / 2)

            middleRadius = Math.min(middleRect.height() / 2f, middleRect.width() / 2f)
            drawableRadius = Math.min(avatarDrawableRect.height() / 2.0f, avatarDrawableRect.width() / 2.0f)

            middlePaint.apply {
                style = Paint.Style.STROKE
                isAntiAlias = true
                color = middleColor
                strokeWidth = middleThickness
            }

            circleBackgroundPaint.apply {
                style = Paint.Style.FILL
                isAntiAlias = true
                color = avatarBackgroundColor
            }

            arcBorderRect.apply {
                set(borderRect)
                inset(currentBorderThickness / 2f, currentBorderThickness / 2f)
            }

            updateShaderMatrix()
            invalidate()

        }?:run {
            invalidate()
        }

    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        shaderMatrix.set(null)

        if (bitmapWidth * avatarDrawableRect.height() > avatarDrawableRect.width() * bitmapHeight) {
            scale = avatarDrawableRect.height() / bitmapHeight.toFloat()
            dx = (avatarDrawableRect.width() - bitmapWidth * scale) / 2f
        } else {
            scale = avatarDrawableRect.width() / bitmapWidth.toFloat()
            dy = (avatarDrawableRect.height() - bitmapHeight * scale) / 2f
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate((dx + 0.5f).toInt() + avatarDrawableRect.left, (dy + 0.5f).toInt() + avatarDrawableRect.top)

        bitmapShader?.setLocalMatrix(shaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(Defaults.COLORDRAWABLE_DIMENSION, Defaults.COLORDRAWABLE_DIMENSION, Defaults.BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Defaults.BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun initializeBitmap() {
        avatarDrawable = getBitmapFromDrawable(drawable)
        setup()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
    }

    private fun animateClick() {
        if (shouldBounceOnClick) scaleAnimator.start()
    }

    override fun performClick(): Boolean {
        animateClick()
        return super.performClick()
    }

    override fun performLongClick(): Boolean {
        animateClick()
        return super.performLongClick()
    }

    private fun inTouchableArea(x: Float, y: Float): Boolean {
        return Math.pow(x - borderRect.centerX().toDouble(), 2.0) + Math.pow(y - borderRect.centerY().toDouble(), 2.0) <= Math.pow(borderRadius.toDouble(), 2.0)
    }

    override fun onDraw(canvas: Canvas) {
        if (avatarDrawable == null) {
            return
        }
        if (avatarBackgroundColor != Color.TRANSPARENT) {
            canvas.drawCircle(avatarDrawableRect.centerX(), avatarDrawableRect.centerY(), drawableRadius, circleBackgroundPaint)
        }
        canvas.drawCircle(avatarDrawableRect.centerX(), avatarDrawableRect.centerY(), drawableRadius, bitmapPaint)
        if (middleThickness > 0) {
            canvas.drawCircle(middleRect.centerX(), middleRect.centerY(), middleRadius, middlePaint)
        }
        drawBorder(canvas)
    }

    private fun drawBorder(canvas: Canvas) {
        if (isAnimating || isReversedAnimating) {
            val totalDegrees = (270f + animationLoopDegrees) % 360
            drawArches(totalDegrees, canvas)
            val startOfMainArch = totalDegrees + (currentAnimationArchesArea)
            canvas.drawArc(arcBorderRect, startOfMainArch, 360 - currentAnimationArchesArea, false, borderPaint)
        } else {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)
        }
    }

    private fun drawArches(totalDegrees: Float, canvas: Canvas) {
        for (i in 0..numberOfArches) {
            val deg = totalDegrees + (spaceBetweenArches + individualArcDegreeLenght) * i * (animationArchesSparseness)
            canvas.drawArc(arcBorderRect, deg, individualArcDegreeLenght, false, borderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    /**
     * This section makes the elevation settings on Lollipop+ possible,
     * drawing a circlar shadow around the border
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private inner class OutlineProvider : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) = Rect().let {
            borderRect.roundOut(it)
            outline.setRoundRect(it, it.width() / 2.0f)
        }
    }
}
