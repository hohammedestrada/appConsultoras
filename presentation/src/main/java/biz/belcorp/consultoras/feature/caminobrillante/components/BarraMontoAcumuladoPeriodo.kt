package biz.belcorp.consultoras.feature.caminobrillante.components

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.text.Html
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.barra_monto_acumulado_periodo.view.*
import kotlin.math.floor


class BarraMontoAcumuladoPeriodo(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.barra_monto_acumulado_periodo, this)
    }

    fun setProgressBarra(progress: Double) {
        val progressInt = Math.floor(progress).toInt()
        progress_bar.animateProgress(2000, progressInt)
    }

    fun isVisibleIndicador(visible: Boolean) {
        ivwIndicadorMinimo.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setColorIndicador(resIdColor: Int?) {
        resIdColor?.let {
            ivwIndicadorMinimo.setColorFilter(ContextCompat.getColor(context, it))
            ivwIcon1.setColorFilter(ContextCompat.getColor(context, it))
        }
    }

    fun setPorcentajeIndicadorMontoMinimo(porcentaje: Double) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.setHorizontalBias(R.id.ivwIndicadorMinimo, percentageToBias(porcentaje))
        constraintSet.applyTo(this)
    }

    fun isVisibleFlag(visible: Boolean) {
        ivwFlag.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setTextSiguienteNivel(nivel: String) {
        tvwSiguienteNivel.text = nivel
    }

    fun setTextMontoSiguienteNivel(monto: String) {
        tvwMontoSgteNivel.text = monto
    }

    fun isVisibleMontoSiguienteNivel(visible: Boolean) {
        tvwMontoSgteNivel.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setPorcentajeIndicadorMontoAcumulado(porcentaje: Double) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.setHorizontalBias(R.id.ivwIndicadorActual, percentageToBias(porcentaje))
        constraintSet.applyTo(this)
        when(porcentaje) {
            in 0.0..5.0 -> setPorcentajeTextMontoAcumulado(20.0)
        }
    }

    fun setPorcentajeTextMontoAcumulado(porcentaje: Double) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.setHorizontalBias(R.id.tvwMontoAcumulado, percentageToBias(porcentaje))
        constraintSet.applyTo(this)
    }

    fun setTextMontoAcumulado(text: String) {
        tvwMontoAcumulado.text = text
    }

    fun isVisibleTextMontoAcumulado(visible: Boolean) {
        tvwMontoAcumulado.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun visibilityIcon1(visibility: Int) {
        ivwIcon1.visibility = visibility
    }

    fun setText1(text: String) {
        val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else Html.fromHtml(text)

        tvw1.text = spanned
    }

    fun setText1Bold(bold: Boolean) {
        tvw1.setTypeface(tvw1.typeface, if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

    fun isVisibleText1(visible: Boolean) {
        tvw1.visibility = if (visible) View.VISIBLE else View.GONE
    }


    fun setAlignementText1(alignment: Int?) {
        alignment?.let {
            tvw1.textAlignment = it
        }
    }

    fun visibilityIcon2(visibility: Int) {
        ivwIcon2.visibility = visibility
    }

    fun setText2(text: String) {
        val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else Html.fromHtml(text)

        tvw2.text = spanned
    }

    fun setAlignementText2(alignment: Int?) {
        alignment?.let {
            tvw2.textAlignment = it
        }
    }

    fun isVisibleText2(visible: Boolean) {
        tvw2.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setText3(text: String) {
        val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else Html.fromHtml(text)

        tvw3.text = spanned
    }

    private fun percentageToBias(percentage: Double): Float {
        return (floor(percentage) / 100).toFloat()
    }

}
