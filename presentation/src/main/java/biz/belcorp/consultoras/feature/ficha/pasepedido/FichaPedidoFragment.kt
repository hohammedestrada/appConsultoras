package biz.belcorp.consultoras.feature.ficha.pasepedido

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlDataMapper
import biz.belcorp.consultoras.common.model.product.ComponentItem
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.feature.ficha.adapter.OfferOptionToneAdapter
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.HomeActivity.Companion.KIT_NUEVA_CUV
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersFragment
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersFragment.Companion.IS_FROM_KIT_NUEVA
import biz.belcorp.consultoras.util.*
import biz.belcorp.consultoras.util.analytics.Ficha
import biz.belcorp.consultoras.util.anotation.FichaType
import biz.belcorp.consultoras.util.anotation.SearchOriginType
import biz.belcorp.consultoras.util.broadcast.FestBroadcast
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.design.counter.Counter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_ficha_base.*
import kotlinx.android.synthetic.main.survey_dialog_fragment.*
import java.util.*

class FichaPedidoFragment : BaseFichaFragment() {

    private var quantityChanged = false
    private var clientChanged = false
    private var tonesChanged = false

    private var clientID: Int = 0
    private var selloConfig: FestivalSello? = null

    var imageSelectedType: String = biz.belcorp.library.util.StringUtil.Empty

    override fun init() {
        super.init()

        simpleButton.setText(getString(R.string.ficha_update))
        simpleCounter.changeQuantity(orderItem?.cantidad ?: 1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UXCam.tagScreenName(UXCamUtils.FichaPedidoFragmentName)
    }

    override fun getData() {

        when (fichaType) {

            FichaType.PRODUCT_SIMPLE -> { // Ficha Producto

                presenter.getData(type, cuv, accessFrom)

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
        productoEncabezado.pum = oferta.pum

        if (type != OfferTypes.MG && type != OfferTypes.LMG)
            this.type = offer.tipoOferta

        simpleButton.isDisable(!isKitNueva)

        lnlClient.visibility = View.VISIBLE

        if (isKitNueva) {

            showCounter(false)
            lnlClient.visibility = View.GONE

            setRemoveButton()

            simpleButton.buttonClickListener = object : Button.OnClickListener {

                override fun onClick(view: View) {

                    orderItem?.let {

                        presenter.deleteItem(productOrder, it)

                    }

                }

            }

        }

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
            setupButtonsFichaResumidaAgotada()
        }

        /**
         * Start component
         */

        offer.componentes?.let { list ->
            vwComponentDivider.visibility = View.VISIBLE
            tvwTitleContenido.text = resources.getString(R.string.ficha_content)
            loadComponentsList(list)
            rvwOffersTone.adapter = OfferOptionToneAdapter(offer.vencido, list,
                offer.codigoEstrategia, context, this)

            if (list.size == 1) {

                productoEncabezado.productDetail = list[0]?.especificaciones?.filter {
                    it?.trim()?.isNotEmpty() ?: false
                }

                list[0]?.listaImagenURL?.forEach {
                    listImages.add(it ?: "")
                }

            }

        }

        productoEncabezado.imageUrl = listImages.toTypedArray()

        if (!imageSelectedType.isEmpty()) {
            changeMainImage(imageSelectedType)
        }



        if (offer.flagFestival) {
            viewProduct.setTagText(getString(R.string.tag_festival))
            context?.let {
                viewProduct.setTagBackground(ContextCompat.getColor(it, R.color.gana_mas_festival_start), ContextCompat.getColor(it, R.color.gana_mas_festival_end), 3)
            }

            selloConfig?.let { config ->
                config.selloTexto?.let { viewProduct.setTagText(it) }
                config.selloColorTexto?.let {
                    if (StringUtil.isHexColor(it))
                        viewProduct.setTagTextColor(Color.parseColor(it))
                }
                safeLet(config.selloColorInicio, config.selloColorFin, config.selloColorDireccion) { colorInicio, colorFin, orientacion ->
                    viewProduct.setTagBackground(colorInicio, colorFin, orientacion)
                }
            }
            showTag = true
        }

        updateFichaHeader(true, showTimer, showTag)

        //Label de precio
        presenter.setTextTitlesByOfferType(type ?: "")

        val sectionDesc = getOfferTypeForAnalytics(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))
        val subsection = calcularCarouselTypeSubSection(GlobalConstant.EVENT_ACTION_FICHA)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ficha.view(offer, mUser, analytics)
        }

        presenter.getAnalytics(null, null, sectionDesc, subsection, null, null, onAnalyticsLoaded)

        lnlContent.visibility = View.VISIBLE

        Handler().postDelayed({
            addButtonVisible()
        }, 100)

        if (type == OfferTypes.ODD) {

            if (offer.vencido) {
                viewProduct.showTimer(false)
                simpleCounter.isEditable(false)
                simpleCounter.isEnable(false)
                quantityChanged = true
                tonesChanged = true
            }

        }

        orderItem?.let {

            this.clientID = it.clienteID ?: 0
            it.nombreCliente?.let { nombre ->
                bsClient.setContent(if (nombre.isNotEmpty()) nombre else AddOrdersFragment.CLIENT_DEFAULT_NAME)
            } ?: bsClient.setContent(AddOrdersFragment.CLIENT_DEFAULT_NAME)

        }

    }

    private fun loadComponentsList(list: List<Componente?>) {

        val components = list.filterNotNull()

        orderItem?.let { pedido ->
            for (component in components) {
                val opciones = component.opciones?.filterNotNull() ?: listOf()
                opciones.forEach { opcion ->
                    val cantidad = existsTone(opcion.cuv, pedido.components)
                    if (cantidad > 0) {
                        opcion.selected = true
                        opcion.cantidad = (cantidad / (pedido.cantidad ?: 1))
                        opcion.imagenURL?.let {
                            imageSelectedType = it
                        }

                    }
                }
            }
        }

    }

    override fun loadComponent(component: Componente?) {

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

        }

    }

    private fun existsTone(cuv: String?, orderComponents: List<ComponentItem>): Int {

        for (orderComponent in orderComponents) {
            if (orderComponent.cuv == cuv)
                return orderComponent.cantidad ?: 0
        }

        return 0

    }

    override fun addFromFicha() {

        type?.let { type ->

            val adapter = rvwOffersTone.adapter as OfferOptionToneAdapter
            val offer = oferta.copy()
            offer.componentes = adapter.getTonesSelected()

            var codigo = ""
            val palanca = if (type == OfferTypes.OPM) OfferTypes.RD else type

            origenPedidoWebFrom?.let { origenPedidoWebFrom ->
                codigo = getFichaOrderOrigin(origenPedidoWebFrom, fichaOfferType)
            }

            // Ficha Catálogo
            if (type == OfferTypes.CAT && marcaID != null)
                codigo = SearchOriginType.ORIGEN_BUSCADOR_FICHA + Belcorp.getBrandOrigenById(marcaID!!)

            val orderID = orderItem?.conjuntoID ?: 0

            presenter.agregar(offer, simpleCounter.quantity, simpleCounter,
                DeviceUtil.getId(context), codigo, palanca, null, true, orderID, this.clientID)

        }

    }

    override fun onAddComplete(quantity: Int, productCUV: ProductCUV, showImage: Boolean, message: String?, codeAlert: String?) {

        checkChanges()

        val returnIntent = Intent()
        returnIntent.putExtra(BaseFichaActivity.EXTRA_MESSAGE_ADDING, message)
        returnIntent.putExtra(BaseFichaActivity.EXTRA_CODE_ALERT, codeAlert)

        mensajes?.let { messages ->
            if (messages.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putParcelableArrayList(BaseFichaActivity.EXTRA_MENSAJE_PROL,
                    MensajeProlDataMapper().transformToDomainModel(messages) as ArrayList<out Parcelable>)
                returnIntent.putExtra(BaseFichaActivity.EXTRA_BUNDLE, bundle)
            }
        }

        activity?.let { FestBroadcast.sendAddUpdateToCart(it) }
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()

    }

    private fun checkChanges() {

        if (quantityChanged || clientChanged || tonesChanged)
            Ficha.modify(quantityChanged, clientChanged, tonesChanged)

    }

    override fun onComplete(clienteModel: ClienteModel) {

        super.onComplete(clienteModel)
        this.clientID = clienteModel.clienteID ?: 0
        clientChanged = clienteModel.clienteID != orderItem?.clienteID
        checkForModifyButton()

    }

    private fun checkForModifyButton() {

        orderItem?.let {
            simpleButton.isDisable(!(quantityChanged || clientChanged || tonesChanged))
            if (this.agotado) {
                if (!simpleButton.disabled)
                    restoreButtonsFichaResumidaAgotada()
                else
                    setupButtonsFichaResumidaAgotada()
            }
        }

    }

    private fun restoreButtonsFichaResumidaAgotada() {

        simpleButton.setText(getString(R.string.ficha_update))
        simpleButton.addIconLeft(R.drawable.ic_comp_add_to_cart)
        simpleButton.addIconRight(-1)
        simpleButton.isDisable(false)

    }

    private fun setupButtonsFichaResumidaAgotada() {

        simpleButton.addTextColorDisable(resources.getColor(R.color.white))
        simpleButton.addColorDisable(resources.getColor(R.color.gray_4))
        simpleButton.setText(getString(R.string.ficha_btn_text_agotado))
        simpleButton.addIconLeft(-1)
        simpleButton.addIconRight(-1)
        simpleButton.isDisable(true)

    }

    override fun checkOptionsForPasePedido() {

        orderItem?.let {
            tonesChanged = checkForTonesChanged(it)
            checkForModifyButton()
        }

    }

    override fun changeMainImage(imagenURL: String?) {
        context?.let { c ->
            imagenURL?.let {
                val myCall: (url: String) -> Unit = { url -> setProductTypePhoto(url) }
                ImageUtils.setProductTypePhoto(c, view?.findViewById(R.id.rltMain) as RelativeLayout, it, myCall)
            }
        }
    }

    private fun checkForTonesChanged(pedido: ProductItem): Boolean {

        rvwOffersTone.adapter?.let { tonesAdapter ->

            val componentesIniciales = pedido.components
            val nuevaLista = (tonesAdapter as OfferOptionToneAdapter).getTonesSelected()

            componentesIniciales.forEach { componente ->

                nuevaLista.forEach {

                    it.opciones?.forEach { opcion ->

                        if (componente.cuv == opcion?.cuv) {
                            if (componente.cantidad != opcion?.cantidad)
                                return true
                        } else
                            return true

                    }

                }

            }

            return false

        }

        return false

    }

    override fun onQuantityChange(quantity: Int) {

        orderItem?.cantidad?.let { cantidadInicial ->

            if (this.agotado && simpleCounter.quantity > cantidadInicial) {
                simpleCounter.changeQuantity(cantidadInicial)
                showError(getString(R.string.error_excedido_message))
            }
            quantityChanged = simpleCounter.quantity != orderItem?.cantidad

        }
        checkForModifyButton()

    }

    override fun onClientBoxClick() {

        Ficha.changeClient()
        listener?.showClients()

    }

    override fun addFromCarousel(quantity: Int, keyItem: String, counterView: Counter, typeCarousel: Int) {
        /* No es necesario */
    }

    override fun close() {

        val returnIntent = Intent()
        returnIntent.putExtra(BaseFichaActivity.EXTRA_MESSAGE_ADDING, getString(R.string.msj_offer_removed_default))
        returnIntent.putExtra(BaseFichaActivity.EXTRA_CODE_ALERT, GlobalConstant.CODE_OK)

        mensajes?.let { messages ->
            if (messages.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putParcelableArrayList(BaseFichaActivity.EXTRA_MENSAJE_PROL,
                    MensajeProlDataMapper().transformToDomainModel(messages) as ArrayList<out Parcelable>)
                returnIntent.putExtra(BaseFichaActivity.EXTRA_BUNDLE, bundle)
            }
        }

        if (isKitNueva) {

            IS_FROM_KIT_NUEVA = true
            KIT_NUEVA_CUV = null

        } else
            IS_FROM_KIT_NUEVA = null

        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()

    }

    override fun getScreenName(): String = GlobalConstant.SCREEN_FICHA_RESUMIDA

    override fun getOfferTypeForFicha(type: String): String {

        return if (this.type == OfferTypes.LAN) OfferTypes.LAN
        else type

    }

    override fun setupSelloFestival(selloConfig: FestivalSello?) {
        this.selloConfig = selloConfig
    }

}
