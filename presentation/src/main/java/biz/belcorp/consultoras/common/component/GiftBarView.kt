package biz.belcorp.consultoras.common.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.gifbarview.view.*
import android.widget.TextView
import android.text.Spannable
import android.text.style.ImageSpan
import android.text.SpannableStringBuilder
import biz.belcorp.consultoras.util.GlobalConstant


class GiftBarView : LinearLayoutCompat {

    var listenergift: onClickGift? = null
    var holder: GiftHolder? = null
    var tagTextColor : Int = Color.BLACK

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        addView(LayoutInflater.from(context).inflate(R.layout.gifbarview, this, false))
        imgGifbar.setOnClickListener {
            imgGifbar.isClickable = false
            imgGifbar.isEnabled = false

            listenergift?.openActivityGift()

            imgGifbar.isClickable = true
            imgGifbar.isEnabled = true
        }

        tagTextColor = ResourcesCompat.getColor(resources, R.color.warm_grey, null)
    }

    fun setGiftPosition(progress: Double, showAnimation: Boolean) {
        val porcentaje = progress.toFloat() / getProgressBar().max
        imgGifbar.visibility = View.VISIBLE

        setBias(R.id.imgGifbar, cnrtGiftbar, porcentaje)
        desplazarView(porcentaje, imgGifbar)

        setBias(R.id.ripple, cnrtGiftbar, porcentaje)
        desplazarView(porcentaje, ripple)

        adjustSizeBar(porcentaje)

        if (showAnimation) {
            ripple.startRippleAnimation()
            ripple.visibility = View.VISIBLE
        } else {
            ripple.stopRippleAnimation()
            ripple.visibility = View.GONE
        }
    }

    fun showRippleAnimation(showAnimation: Boolean) {
        if (showAnimation) {
            ripple.startRippleAnimation()
            ripple.visibility = View.VISIBLE
        } else {
            ripple.stopRippleAnimation()
            ripple.visibility = View.GONE
        }
    }


    fun setMessage(message: String?) {
        when (message) {
            null -> {
                tvwProgress.text = ""
                tvwProgress.visibility = View.GONE
            }
            else -> {
                if (message.contains(GlobalConstant.EMPIEZA_AGREGAR_PRODUCTOS)) {
                    textWithImage(message)
                } else if (message.contains(GlobalConstant.EMPIEZA_AGREGAR_PRODUCTO_NO_REGALO)) {
                    textWithImage(message)
                } else {
                    tvwProgress.text = message
                }
                tvwProgress.visibility = View.VISIBLE
            }
        }
    }

    fun updateTextColor(color:String?){
        color?.apply {
            tvwProgress.setTextColor(Color.parseColor(this))
            tagTextColor = Color.parseColor(this)
        }

    }

    fun setDefaultTextColor(){
        tagTextColor = ResourcesCompat.getColor(resources, R.color.warm_grey, null)
        tvwProgress.setTextColor(tagTextColor)
    }


    private fun textWithImage(mensaje: String) {
        var msj = "$mensaje  "
        val ssb = SpannableStringBuilder(msj)
        var imagenSpan = ImageSpan(context, R.drawable.ic_smile)
        ssb.setSpan(imagenSpan, mensaje.length, mensaje.length + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        tvwProgress.setText(ssb, TextView.BufferType.SPANNABLE)

    }

    private fun adjustSizeBar(percentage: Float) {
        val newLayoutParams = borderProgressBar.layoutParams as ConstraintLayout.LayoutParams
        when {
            percentage >= 0.95F -> {
                newLayoutParams.leftMargin = 0
                newLayoutParams.rightMargin = 29
            }
            percentage <= 0.05F -> {
                newLayoutParams.leftMargin = 29
                newLayoutParams.rightMargin = 0
            }
            else -> {
                newLayoutParams.leftMargin = 0
                newLayoutParams.rightMargin = resources.getDimension(R.dimen.margin_addOrderContainer).toInt()
            }
        }
        borderProgressBar.layoutParams = newLayoutParams
    }

    fun hideGift(visible: Boolean = false) {
        if (visible) {
            groupripplegift.visibility = View.VISIBLE
        } else {
            groupripplegift.visibility = View.GONE
        }
    }

    fun updateTag(label: String, progress: Float): View? {
        val percentage = progress / getProgressBar().max.toFloat()
        if (holder != null) {
            cnrtContainerTag.removeAllViews()
            holder = null
        }
        createTag(label, percentage)
        return null
    }

    fun addTag(label: String, progress: Float): View? {
        val percentage = progress / getProgressBar().max.toFloat()
        createTag(label, percentage)
        return null
    }

    @SuppressLint("RestrictedApi")
    fun createTag(text: String, percentage: Float) {

        //creacion del texto del tag
        val tTextTag = android.support.v7.widget.AppCompatTextView(context, null, R.style.FontLatoRegular)
        tTextTag.id = ViewCompat.generateViewId()
        tTextTag.text = text
        tTextTag.includeFontPadding = false
        tTextTag.setPadding(0, 0, -3, 0)
        tTextTag.setTextColor(tagTextColor)
        tTextTag.setAutoSizeTextTypeUniformWithConfiguration(1, 100, 2,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        tTextTag.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        //creacion del "palito" que va encima del texto
        val indicator = View(context)
        indicator.id = ViewCompat.generateViewId()
        indicator.layoutParams = LayoutParams(2, 12)
        indicator.setBackgroundColor(tagTextColor)

        cnrtContainerTag.addView(indicator)
        cnrtContainerTag.addView(tTextTag)

        val constraintSet = ConstraintSet()
        constraintSet.clone(cnrtContainerTag)

        //anclando el palito
        constraintSet.connect(indicator.id, ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)

        constraintSet.connect(indicator.id, ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0) //TENIA MARGEN 10

        constraintSet.connect(indicator.id, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)

        constraintSet.connect(indicator.id, ConstraintSet.BOTTOM,
            tTextTag.id, ConstraintSet.TOP, 0)

        constraintSet.applyTo(cnrtContainerTag)

        //anclando el texto
        constraintSet.clone(cnrtContainerTag)

        constraintSet.connect(tTextTag.id, ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)

        constraintSet.connect(tTextTag.id, ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)

        constraintSet.connect(tTextTag.id, ConstraintSet.TOP,
            indicator.id, ConstraintSet.BOTTOM, 0)

        constraintSet.connect(tTextTag.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)


        constraintSet.applyTo(cnrtContainerTag)

        //Ubica los items en porcentaje
        setBias(indicator.id, cnrtContainerTag, percentage)
        setBias(tTextTag.id, cnrtContainerTag, percentage)

        holder = GiftHolder()
        holder?.apply {
            this.tTextTag = tTextTag
            this.indicator = indicator
            this.constraintSet = constraintSet
        }

    }

    private fun setBias(view: Int, constraint: ConstraintLayout, percentage: Float, horizontal: Boolean = true) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraint)
        if (horizontal) {
            constraintSet.setHorizontalBias(view, percentage)
        } else {
            constraintSet.setVerticalBias(view, percentage)
        }
        constraintSet.applyTo(constraint)
    }

    fun getProgressBar(): ProgressBar {
        return seekbarPuntosBar
    }


    private fun desplazarView(porcentajeReal: Float, view: View) {
        val anchoImagen = (view.layoutParams.width)
        var escala = (anchoImagen * 0.01)
        var tasa = escala * (porcentajeReal * 100)
        var desplazamiento = tasa - (anchoImagen / 2)
        view.translationX = desplazamiento.toFloat()
    }

    class GiftHolder {
        var tTextTag: android.support.v7.widget.AppCompatTextView? = null
        var indicator: View? = null
        var constraintSet: ConstraintSet? = null
    }

    interface onClickGift {
        fun openActivityGift()
    }
}
