package biz.belcorp.consultoras.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import java.text.DecimalFormat

class AdapterPagoHelper{
    companion object {
        fun calculateFinalAmmount(deuda: Double?, porcentaje: Double): String? {
            val factor = 100.0 //este factor es los decimales para redondear
            val cal = Math.round( ((deuda!! * porcentaje) / 100)* factor)/factor
            return (cal + deuda).toString()
        }

        fun calculatePercentajeAmmount(deuda: Double?, porcentaje: Double, iso: String): Double? {
            return (Math.round(deuda!!*porcentaje)/100.0)
        }

        fun openPdfTerms(urlTerminos: String, context: Context) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlTerminos))
                context.startActivity(browserIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context.applicationContext, R.string.legal_activity_not_found, Toast.LENGTH_SHORT).show()
                BelcorpLogger.w("openPdfTerms", ex)
            }
        }

        fun showItem(linear: LinearLayout, flecha: ImageView){
            if(linear.visibility == View.VISIBLE) {
                flecha.animate().rotation(0f)
                linear.visibility = View.GONE
            }
            else{
                flecha.animate().rotation(90f)
                linear.visibility = View.VISIBLE
            }
        }

        fun hideItems(lnrContinue: LinearLayout, showing: Boolean) {
            if(showing) lnrContinue.visibility=View.VISIBLE else lnrContinue.visibility=View.GONE
        }

    }
}

