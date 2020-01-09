package biz.belcorp.consultoras.feature.home.tutorial

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide

import java.util.Calendar

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.animation.ripple.RippleView
import biz.belcorp.consultoras.util.Constant
import biz.belcorp.consultoras.util.CountryUtil
import biz.belcorp.consultoras.util.anotation.HolydayType
import biz.belcorp.consultoras.util.toHtml
import biz.belcorp.library.annotation.Country
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.view_holiday.view.*
import java.text.MessageFormat

/**
 * @author andres.escobar on 2/11/2017.
 */
class Holiday : LinearLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.view_holiday, this)
        ripple_view.stopRipple()
    }

    fun postInit(consultantName: String?, type: Int, countryISO: String?, vararg params: String) {

        val calendar = Calendar.getInstance()

        when (type) {
            HolydayType.BIRTHDAY -> {
                Glide.with(context).load(R.drawable.ic_torta).into(ivw_holiday)
                tvw_holiday_1.text = "¡FELIZ CUMPLEAÑOS " + consultantName?.toUpperCase() + "!"
                tvw_holiday_2.text = "Esperamos que tengas un día lleno de alegrías"
                tvw_holiday_3.visibility = View.GONE
            }
            HolydayType.CONSULTANT_DAY -> {
                Glide.with(context).load(R.drawable.ic_copas).into(ivw_holiday)
                tvw_holiday_1.text = "¡FELIZ DÍA DE LA CONSULTORA " + consultantName?.toUpperCase() + "!"
                tvw_holiday_2.text = "Tú nos inspiras, ¡celebremos juntos!"
                tvw_holiday_3.visibility = View.GONE
            }
            HolydayType.ANNIVERSARY -> {
                Glide.with(context).load(R.drawable.ic_confeti_white).into(ivw_holiday)
                tvw_holiday_1.text = "¡FELIZ ANIVERSARIO " + consultantName?.toUpperCase() + "!"
                tvw_holiday_2.text = "Nos encanta que seas parte de nuestra familia y que juntos logremos siempre más"
                tvw_holiday_3.visibility = View.GONE
            }
            HolydayType.SIXTH -> {
                Glide.with(context).load(R.drawable.ic_confeti_white).into(ivw_holiday)
                tvw_holiday_1.text = "¡FELICITACIONES " + consultantName?.toUpperCase() + "!"
                tvw_holiday_2.text = "Lograste completar tus primeros 6 pedidos, ¡Este es solo el primero de muchos logros juntos!"
                tvw_holiday_3.visibility = View.GONE
            }
            HolydayType.BELCORP_ORDER_RESERVAR -> {
                Glide.with(context).load(R.drawable.ic_confeti_white).into(ivw_holiday)
                tvw_holiday_1.text = ""
                tvw_holiday_2.text = "Reservaste tu pedido con éxito"
                tvw_holiday_3.visibility = View.GONE
            }
            HolydayType.BELCORP_ORDER_GUARDAR -> {
                Glide.with(context).load(R.drawable.ic_confeti_white).into(ivw_holiday)
                tvw_holiday_1.text = ""
                tvw_holiday_2.text = "Guardaste tu pedido con éxito"
                tvw_holiday_3.visibility = View.GONE
            }
            HolydayType.POSTULANT -> {
                Glide.with(context).load(R.drawable.ic_confeti_white).into(ivw_holiday)
                tvw_holiday_1.text = "ESTÁS A UN PASO DE SER\nCONSULTORA " + Constant.BRAND_FOCUS_NAME
                tvw_holiday_2.text = "Estamos generando tu Código de\nConsultant. Mientras tanto prueba nuestro\nAPP " + Constant.BRAND_FOCUS_NAME + " Conmigo"
                tvw_holiday_3.visibility = View.GONE
            }
            HolydayType.BELCORP_FIFTY -> {
                ripple_view.setDefaultRippleColor(ContextCompat.getColor(context, R.color.home_belcorp_fifty_ripple_color))
                ripple_view.setDefaultRippleToColor(ContextCompat.getColor(context, R.color.home_belcorp_fifty_ripple_color_second))
                tvw_holiday_1.setTextColor(ContextCompat.getColor(context, R.color.home_belcorp_fifty_color))
                tvw_holiday_1.text = "¡CELEBREMOS JUNTOS!"
                tvw_holiday_2.text = "Tú eres la protagonista de esta historia.\nEres nuestro ejemplo e inspiración."

                when (countryISO) {
                    Country.DO -> tvw_holiday_3.visibility = View.GONE
                    Country.BO -> {

                        //Init : 20 de Junio del 2018
                        val calendarBO = Calendar.getInstance()
                        calendarBO.set(Calendar.YEAR, 2018)
                        calendarBO.set(Calendar.MONTH, Calendar.JUNE)
                        calendarBO.set(Calendar.DAY_OF_MONTH, 20)
                        calendarBO.set(Calendar.HOUR, 0)
                        calendarBO.set(Calendar.MINUTE, 0)

                        //Expire : 01 de Julio del 2018
                        val belcorpBO = Calendar.getInstance()
                        belcorpBO.set(Calendar.YEAR, 2018)
                        belcorpBO.set(Calendar.MONTH, Calendar.JULY)
                        belcorpBO.set(Calendar.DAY_OF_MONTH, 1)
                        belcorpBO.set(Calendar.HOUR, 23)
                        belcorpBO.set(Calendar.MINUTE, 59)

                        if (calendarBO.get(Calendar.YEAR) == belcorpBO.get(Calendar.YEAR) && calendar.after(calendarBO) && calendar.before(belcorpBO)) {
                            tvw_holiday_3.text = String.format(context.getString(R.string.home_fifty_hashtag), CountryUtil().getHashtagBelcorp50Years(countryISO))
                            tvw_holiday_3.visibility = View.VISIBLE
                        } else {
                            tvw_holiday_3.visibility = View.GONE
                        }
                    }
                    else -> {

                        //Init : 18 de Junio del 2018
                        val calendarDE = Calendar.getInstance()
                        calendarDE.set(Calendar.YEAR, 2018)
                        calendarDE.set(Calendar.MONTH, Calendar.JUNE)
                        calendarDE.set(Calendar.DAY_OF_MONTH, 18)
                        calendarDE.set(Calendar.HOUR, 0)
                        calendarDE.set(Calendar.MINUTE, 0)

                        //Expire : 01 de Julio del 2018
                        val belcorpDE = Calendar.getInstance()
                        belcorpDE.set(Calendar.YEAR, 2018)
                        belcorpDE.set(Calendar.MONTH, Calendar.JULY)
                        belcorpDE.set(Calendar.DAY_OF_MONTH, 1)
                        belcorpDE.set(Calendar.HOUR, 23)
                        belcorpDE.set(Calendar.MINUTE, 59)

                        if (calendarDE.get(Calendar.YEAR) == belcorpDE.get(Calendar.YEAR) && calendar.after(calendarDE) && calendar.before(belcorpDE)) {
                            tvw_holiday_3.text = String.format(context.getString(R.string.home_fifty_hashtag), CountryUtil().getHashtagBelcorp50Years(countryISO
                                ?: ""))
                            tvw_holiday_3.visibility = View.VISIBLE
                        } else {
                            tvw_holiday_3.visibility = View.GONE
                        }
                    }
                }
            }
            else -> {
            }
        }
    }

    fun initLayoutParams() {
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        layoutParams = params
    }

    fun start() {
        ripple_view.startRipple()
    }
}
