package biz.belcorp.consultoras.feature.ficha.caminobrillante

import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.view.View
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.Analytics
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.feature.ficha.adapter.OfferOptionToneAdapter
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.TimeUtil
import biz.belcorp.consultoras.util.UXCamUtils
import biz.belcorp.consultoras.util.analytics.Ficha
import biz.belcorp.consultoras.util.anotation.AnalyticsCategoryType.FICHA
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.mobile.components.design.counter.Counter
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_ficha_base.*


/**
 * @author : Eduardo Chuquilin
 * @since : 2019-08-05
 */
class FichaCaminoBrillanteFragment : BaseFichaFragment() {

    override fun onResume() {

        super.onResume()
        refreshInit()

    }

    override fun setUser(user: User?) {

        super.setUser(user)
        (activity as? BaseFichaActivity)?.hideToolbarIcons(BaseFichaActivity.FichaMenuItems.SEARCH_ITEM)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UXCam.tagScreenName(UXCamUtils.FichaCaminoBrillanteFragmentName)
    }

    override fun getData() {

        isTimerEnabled = false
        syncTimer()
        presenter.getDataCaminoBrillante(type, cuv)

        nsvContent.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int,
                                               scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            addButtonVisible()
        }

    }

    override fun loadOffer(offer: Oferta) {

        simpleCounter.isEditable(false)

        this.oferta = offer
        this.type = offer.tipoOferta

        viewProduct.visibility = View.VISIBLE

        productoEncabezado.product = offer.nombreOferta?.trim()

        setClientPrice(offer)
        productoEncabezado.imageUrl = arrayOf(offer.imagenURL ?: "")


        /**
         * Start component
         */

        offer.componentes?.let { list ->
            vwComponentDivider.visibility = View.VISIBLE
            tvwTitleContenido.text = resources.getString(R.string.ficha_content)
            rvwOffersTone.adapter = OfferOptionToneAdapter(offer.vencido, list, offer.codigoEstrategia, context, this)

            if (list.size == 1) {
                vwComponentDivider2.visibility = View.GONE
            }
        }

        viewProduct.setValues(productoEncabezado)

        val sectionDesc = getOfferTypeForAnalytics(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))
        val subsection = calcularCarouselTypeSubSection(FICHA)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ficha.view(oferta, mUser, analytics)
        }

        presenter.getAnalytics(null, null, sectionDesc, subsection, null, null, onAnalyticsLoaded)

        simpleButton.isDisable(!offer.flagAgregar)

        setClientPrice(offer)

        showCounter(offer.flagCantidad)

        presenter.setTextTitlesByOfferType(offer.tipoOferta ?: "")

        lnlContent.visibility = View.VISIBLE

        Handler().postDelayed({
            addButtonVisible()
        }, 100)

    }

    private fun syncTimer(){

        if(!isTimerEnabled) {

            if (viewProduct.isShowTimer())
                viewProduct.stopProductTimer()

        } else {

            if (viewProduct.isShowTimer())
                viewProduct.setProductTime(TimeUtil.setServerTime(BuildConfig.TIME_SERVER))

        }

    }

    override fun addFromFicha() {

        val value = origenPedidoWeb ?: "0"

        presenter.agregar(oferta, simpleCounter.quantity, simpleCounter,
            DeviceUtil.getId(context), null, null, value)

    }

    override fun onAddComplete(quantity: Int, productCUV: ProductCUV, showImage: Boolean, message: String?, codeAlert: String?) {

        listener?.updateOrders(quantity)
        refreshInit()

        simpleCounter.changeQuantity(1)
        val messageDialog = message ?: getString(R.string.msj_offer_added_default)
        val image = ImageUtils.verifiedImageUrl(productCUV.fotoProducto,
            productCUV.fotoProductoSmall, productCUV.fotoProductoMedium)

        context?.let {
            val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
            val url = if (showImage) image else null  // Verifica que el flag este activo para mostrar la imagen
            showBottomDialog(it, messageDialog, url, colorText)
        }

        val currentLocation = null
        val currentSection = null
        val sectionDesc = getOfferTypeForAnalytics(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))
        val subsection = calcularCarouselTypeSubSection(GlobalConstant.EVENT_ACTION_FICHA)
        val originLocation = calcularOfferOriginTypeLocation(arguments?.getString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM))
        val originSection = calcularOffersOriginTypeSection(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ficha.addItemCarousel(mUser, productCUV, analytics)
        }

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

    }

    override fun addFromCarousel(quantity: Int, keyItem: String, counterView: Counter, typeCarousel: Int) {
        /* No es necesario */
    }

    override fun getScreenName(): String = GlobalConstant.SCREEN_FICHA_CAMINO_BRILLANTE

}
