package biz.belcorp.consultoras.feature.ficha.premio

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.View
import android.widget.CheckBox
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.model.orders.OrderModelDataMapper
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.feature.ficha.adapter.OfferOptionToneAdapter
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment
import biz.belcorp.consultoras.feature.ficha.enriquecida.FichaEnriquecidaFragment
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.UXCamUtils
import biz.belcorp.consultoras.util.analytics.Ficha
import biz.belcorp.consultoras.util.anotation.FichaType
import biz.belcorp.consultoras.util.anotation.SearchOriginType
import biz.belcorp.consultoras.util.broadcast.FestBroadcast
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_ficha_base.*
import java.util.*
import javax.inject.Inject

class FichaPremioFragment : BaseFichaFragment() {


    @Inject
    lateinit var orderModelDataMapper: OrderModelDataMapper

    private var fichaEnriquecidaFragment: FichaEnriquecidaFragment? = null

    private var festival: FestivalResponse? = null
    private var productsConditionFestival: List<ProductCUV?>? = null

    private var order: OrderModel? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UXCam.tagScreenName(UXCamUtils.FichaPremioFragmentName)
    }

    override fun setupUI() {

        super.setupUI()


        if (fichaEnriquecidaFragment == null) {

            fichaEnriquecidaFragment = FichaEnriquecidaFragment.makeInstance(fromGanaMas)
            fichaEnriquecidaFragment?.initAdapter(requireContext())

        }

    }

    override fun getData() {

        when (fichaType) {

            FichaType.PRODUCT_SIMPLE -> { // Ficha Producto

                simpleCounter.visibility = View.GONE
                simpleButton.isDisable(true)

                presenter.getDataPremio(type, cuv, accessFrom)

                nsvContent.setOnScrollChangeListener { _: NestedScrollView?, _: Int,
                                                       _: Int, _: Int, _: Int ->
                    addButtonVisible()
                }

            }

            FichaType.PRODUCT_COMPONENT -> { // Ficha Componente

                presenter.getDataComponent(arguments?.getSerializable(BaseFichaActivity.EXTRA_PRODUCT_COMPONENT) as Componente)

            }

        }

    }

    override fun loadOffer(offer: Oferta) {

        this.oferta = offer

        productoEncabezado.brand = oferta.nombreMarca
        productoEncabezado.product = oferta.nombreOferta?.trim()
        //productoEncabezado.pum = oferta.pum

        if (type != OfferTypes.MG && type != OfferTypes.LMG)
            this.type = offer.tipoOferta

        /*    when (offer.codigoEstrategia) {

                StrategyCode.NORMAL, StrategyCode.PACKS -> simpleButton.isDisable(false)

                StrategyCode.TONOS -> simpleButton.isDisable(true)

                else -> simpleButton.isDisable(false)

            } */

        var showTimer = false
        var showTag = false
        var isUniqueOffer = false

        when (type) {

            OfferTypes.ODD -> showTimer = true

            OfferTypes.LAN -> showTag = true

            OfferTypes.HV -> {

                offer.componentes?.run {
                    forEach {
                        it?.secciones = Collections.emptyList()
                        it?.especificaciones = Collections.emptyList()
                    }
                }

                vwComponentDivider2.visibility = View.GONE

                if (offer.fichaProductoConfiguracion?.tieneNiveles == true)
                    isUniqueOffer = true

            }

        }

        updatePrices(offer, isUniqueOffer)

        if (offer.tipoPersonalizacion == OfferTypes.CAT)
            vwComponentDivider2.visibility = View.GONE // oculta separador de componentes


        val listImages = arrayListOf(offer.imagenURL ?: "")

        this.agotado = offer.agotado ?: false

        if (this.agotado) {
            setFichaAgotadaButtons()
        }


        /**
         * Start component
         */

        offer.componentes?.let { list ->
            vwComponentDivider.visibility = View.VISIBLE
            tvwTitleContenido.text = resources.getString(R.string.ficha_content)
            rvwOffersTone.adapter = OfferOptionToneAdapter(offer.vencido, list,
                offer.codigoEstrategia, context, this)

            if (list.size == 1) {

                productoEncabezado.productDetail = list[0]?.especificaciones?.filter {
                    it?.trim()?.isNotEmpty() ?: false
                }

                list[0]?.listaImagenURL?.forEach {
                    listImages.add(it ?: "")
                }

                fichaEnriquecidaFragment?.updateAdapter(list[0])

            }

        }

        productoEncabezado.imageUrl = listImages.toTypedArray()

        updateFichaHeader(true, showTimer, showTag)

        //Label de precio
        presenter.setTextTitlesByOfferType(type ?: "")

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            this.mUser?.let { u ->
                Ficha.view(offer, u, analytics)
            }
        }

        val sectionDesc = getOfferTypeForAnalytics(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))
        val subsection = calcularCarouselTypeSubSection(GlobalConstant.EVENT_ACTION_FICHA)
        val analytics = presenter.getAnalytics(null, null, sectionDesc, subsection, null, null, onAnalyticsLoaded)


        lnlContent.visibility = View.VISIBLE

        Handler().postDelayed({
            addButtonVisible()
        }, 100)

        // Extra Section

//        when (accessFrom) {
//
//            BaseFichaActivity.ACCESS_FROM_CAROUSEL_SUGERIDOS -> {
//
//                fichaEnriquecidaAdded = false
//                checkFichaEnriquecidaContent(requireContext())
//                setupExtraSection(carouselViewsList)
//
//            }
//
//            else -> {
//
//                safeLet(offer.cuv, type, offer.codigoProducto, offer.fichaProductoConfiguracion) { cuv, type, codigoProducto, configuracion ->
//
//                    carouselFlags.add(null) // Primer carrusel Flag (UpSelling / LAN)
//                    carouselFlags.add(null) // CrossSelling carrusel Flag
//                    carouselFlags.add(null) // Sugeridos carrusel Flag
//
//                    when {
//                        type == OfferTypes.LAN -> carouselFlags[0] = CarouselType.DEFAULT
//                        configuracion.tieneCarruselUpSelling == true -> carouselFlags[0] = CarouselType.UP_SELLING
//                        else -> listUpsellingOffers = arrayListOf()
//                    }
//
//                    if (configuracion.tieneCarruselCrossSelling == true) carouselFlags[1] = CarouselType.CROSS_SELLING
//                    else listCrossellingOffers = arrayListOf()
//
//                    if (configuracion.tieneCarruselSugeridos == true) carouselFlags[2] = CarouselType.SUGERIDOS
//                    else listSugeridosOffers = arrayListOf()
//
//                    presenter.getCarousels(carouselFlags, cuv, type, codigoProducto, offer)
//
//                }
//
//            }
//        }

    }

    override fun onFormattedOrderReceived(order: OrderModel?, clientModelList: List<ClienteModel?>?, callFrom: Int) {
        this.order = order
        Log.d("ORDER", "$order")
    }


    override fun setDataAward(listaFestivalProgressResponse: List<FestivalProgressResponse?>?, offer: Oferta?, festivalResponse: FestivalResponse?) {

        listaFestivalProgressResponse?.let {

            festival = festivalResponse

            if (it.isNotEmpty()) {

                val montoRestante = it[0]?.montoRestante ?: 0.0
                val flagPremioAgregado = it[0]?.flagPremioAgregado ?: false

                if (montoRestante > 0.0) {
                    viewProduct.setPrize(false, resources.getString(R.string.ficha_premio_status_unready), mUser?.countryMoneySymbol + " " + decimalFormat.format(montoRestante))

                } else {
                    viewProduct.setPrize(true, resources.getString(R.string.ficha_premio_status_ready), resources.getString(R.string.ficha_premio_message_ready))

                    simpleButton.isDisable(false)

                    if (flagPremioAgregado) {
                        simpleButton.setText(resources.getString(R.string.ficha_premio_eliminar))
                        simpleButton.addIconLeft(null)
                        context?.let { simpleButton.addBackgroundColor(ContextCompat.getColor(it, R.color.black)) }

                        deleteItem(offer)

                    } else {
                        simpleButton.setText(resources.getString(R.string.ficha_premio_agregar))

                        addItem(offer)
                    }

                }
            }
        }

    }

    private fun deleteItem(offer: Oferta?) {

        simpleButton.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {

                val productItem = order?.productosDetalle?.first { it?.cuv == offer?.cuv }

                productItem?.let {
                    presenter.deleteItem(order, it)
                }
            }
        }

    }

    private fun addItem(offer: Oferta?) {

        simpleButton.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {

                val keyItem = offer?.cuv ?: "0"

                addToCart(keyItem, 1)
            }
        }
    }

    private fun addToCart(keyItem: String, quantity: Int) {

        val iden = DeviceUtil.getId(activity)
        var festivalProduct: ProductCUV? = null
        val productAward = festival?.listAwards?.firstOrNull { it?.product?.cuv == keyItem }
        val agregado = festival?.listAwards?.firstOrNull { it?.flagPremioAgregado == true }?.flagPremioAgregado
            ?: false
        val producto = productAward?.product

        producto?.let {
            festivalProduct = FestivalProduct.transformFestivalProductToProductCUV(it)
        }
        festivalProduct?.reemplazarFestival = agregado


        if (agregado) {

            context?.let {
                BottomDialog.Builder(it)
                    .setIcon(R.drawable.ic_mano_error)
                    .setTitle(R.string.ficha_premio_eliminar_titulo)
                    .setTitleBold()
                    .setContent(R.string.ficha_premio_eliminar_contenido)
                    .setNegativeText(R.string.ficha_premio_eliminar_boton_cancelar)
                    .setPositiveText(R.string.ficha_premio_eliminar_boton_cambiar)
                    .onNegative(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .onPositive(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog, chbxConfirmacion: CheckBox) {

                            festivalProduct?.reemplazarFestival = true

                            festivalProduct?.let {

                                it.apply {
                                    flagFestival = true
                                    cantidad = quantity
                                    identifier = iden
                                }

                                it.let {
                                    presenter.addToCartPrize(it, iden, SearchOriginType.ORIGEN_LANDING_FEST_FICHA)
                                }
                            }

                        }
                    })
                    .setPositiveBackgroundColor(R.color.magenta)
                    .show()
            }

        } else {

            festivalProduct?.reemplazarFestival = false

            festivalProduct?.let {

                it.apply {
                    flagFestival = true
                    cantidad = quantity
                    identifier = iden
                }

                it.let {
                    presenter.addToCartPrize(it, iden, SearchOriginType.ORIGEN_LANDING_FEST_FICHA)
                }
            }
        }
    }

    override fun loadComponent(component: Componente?) {

        fichaEnriquecidaFragment?.updateAdapter(component)

        component?.let { comp ->

            productoEncabezado.brand = if (comp.nombreMarca.isNullOrEmpty()) marca else comp.nombreMarca
            productoEncabezado.product = comp.nombreComercial?.trim()
            productoEncabezado.productDetail = comp.especificaciones?.filter {
                it?.trim()?.isNotEmpty() ?: false
            }

            productoEncabezado.pum = comp.pum

            val listaImagenesComponente = ArrayList<String>().apply {

                comp.listaImagenURL?.forEach {
                    add(it ?: "")
                }

            }

            productoEncabezado.imageUrl = ArrayList<String>().apply {

                addAll(arrayOf(comp.imagenURL ?: "")) // 1ra imagen
                addAll(listaImagenesComponente) // resto de imagenes

            }.toTypedArray()

            if (comp.precioUnitario ?: 0.0 > 0) {
                val price = "$moneySymbol ${decimalFormat.format(comp.precioUnitario)}"
                productoEncabezado.labelPrice = getString(R.string.ficha_price_for_you)
                productoEncabezado.price = price
            }

            updateFichaHeader(true)

//            checkFichaEnriquecidaContent(requireContext())
//            setupExtraSection(carouselViewsList)

        }

    }

    override fun onAddComplete(quantity: Int, productCUV: ProductCUV, showImage: Boolean, message: String?, codeAlert: String?) {

        val messageDialog = message ?: getString(R.string.msj_offer_added_default)
        val image = ImageUtils.verifiedImageUrl(productCUV.fotoProducto,
            productCUV.fotoProductoSmall, productCUV.fotoProductoMedium)

        context?.let {
            val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
            val url = if (showImage) image else null  // Verifica que el flag este activo para mostrar la imagen
            showBottomDialog(it, messageDialog, url, colorText)
        }

        activity?.let { FestBroadcast.sendAddUpdateToCart(it) }
        listener?.updateOrders(quantity)
        refreshInit()

    }

    override fun addFromFicha() {

    }

    override fun addFromCarousel(quantity: Int, keyItem: String, counterView: Counter, typeCarousel: Int) {
        /* No es necesario */
    }

    override fun getScreenName(): String = GlobalConstant.SCREEN_FICHA_RESUMIDA

    override fun onProductNotAdded(message: String?) {
        message?.let {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
                    .setContent(it)
                    .setNeutralText(getString(R.string.msj_entendido))
                    .onNeutral(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .setNeutralBackgroundColor(R.color.magenta)
                    .show()
            }
        }
    }

    override fun onError(errorModel: ErrorModel) {
        processGeneralError(errorModel)
    }

    override fun close() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }
}


