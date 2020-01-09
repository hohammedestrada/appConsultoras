package biz.belcorp.consultoras.feature.ficha.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import biz.belcorp.mobile.components.design.carousel.promotion.CarouselPromotion
import biz.belcorp.mobile.components.offers.MiniOffer
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.MultiPromotion
import java.text.DecimalFormat
import java.util.*


object FichaCarouselsHelper : SafeLet {

    fun generateSpaceDivider(context: Context) : View {
        val divider = View(context)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(12f, context).toInt())
        divider.layoutParams = lp
        divider.background = ColorDrawable(context.resources.getColor(R.color.gray_1))
        return divider
    }

    fun generateLineDivider(context: Context) : View {
        val divider = View(context)
        val margin16 = dpToPx(16f, context).toInt()
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(1f, context).toInt())
        lp.leftMargin = margin16
        lp.rightMargin = margin16
        divider.layoutParams = lp
        divider.background = ColorDrawable(context.resources.getColor(R.color.gray_3))
        return divider
    }

    fun generateCarousel(context: Context) : MiniOffer {
        val miniOffer = MiniOffer(context)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        miniOffer.layoutParams = lp
        return miniOffer
    }

    fun generatePromotion(context: Context) : CarouselPromotion {
        val carouselPromotion = CarouselPromotion(context)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        carouselPromotion.layoutParams = lp
        return carouselPromotion
    }

    fun generateConditionPromotion(context: Context) : MultiPromotion {
        val conditionPromotion = MultiPromotion(context)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        conditionPromotion.layoutParams = lp
        return conditionPromotion
    }

    fun generateDetailTitle(context: Context) : TextView {
        val stylesHelper = StylesHelper(context)
        val typefaceBold = ResourcesCompat.getFont(context, R.font.lato_bold)
        val titleSize = context.resources.getDimension(R.dimen.mini_multi_default_title_size)
        val margin16 = dpToPx(16f, context).toInt()
        val margin20 = dpToPx(20f, context).toInt()

        val title = TextView(context)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        title.layoutParams = lp
        title.setPadding(margin16, margin20, margin16, 0)
        stylesHelper.updateTextViewStyle(title, typefaceBold, Color.BLACK, titleSize)
        return title
    }

    fun generateDetailRecycler(context: Context) : RecyclerView {
        val recycler = RecyclerView(context)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        recycler.layoutParams = lp
        return recycler
    }

    fun generateFrameLayout(context: Context) : FrameLayout {
        val frameLayout = FrameLayout(context)
        frameLayout.id = R.id.ficha_enriquecida_frame_id
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        frameLayout.layoutParams = lp
        return frameLayout
    }

    private fun dpToPx(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun transformOfferList(context: Context, symbol: String,
                           df: DecimalFormat, offers: List<Oferta?>): ArrayList<OfferModel> {

        val list = arrayListOf<OfferModel>()
        val imageHelper = ImagesHelper(context)

        for (offer in offers) {

            offer?.run {

                var showAmount = true

                if (tipoOferta == OfferTypes.HV) {
                    showAmount = false
                }

                safeLet(cuv, nombreMarca ?: "", nombreOferta, precioValorizado,
                    precioCatalogo, imagenURL, configuracionOferta?.imgFondoApp
                    ?: "", configuracionOferta?.colorTextoApp ?: "", marcaID
                    ?: 0) { cuv2, nombreMarca, nombreOferta, precioValorizado, precioCatalogo,
                            imagenURL, imagenFondo, colorTexto, marcaID ->
                    list.add(Offer.transform(cuv2, nombreMarca, nombreOferta,
                        formatWithMoneySymbol(symbol, df, precioCatalogo.toString()),
                        formatWithMoneySymbol(symbol, df, precioValorizado.toString()),
                        imagenURL, tipoOferta ?: OfferTypes.LAN, flagEligeOpcion ?: false, marcaID,
                        imageHelper.getResolutionURL(imagenFondo), colorTexto, showAmount, null, null, agotado))
                }

            }

        }

        return list
    }

    private fun formatWithMoneySymbol(symbol: String, df: DecimalFormat, price: String): String {
        return "$symbol ${df.format(price.toBigDecimal())}"
    }

}
