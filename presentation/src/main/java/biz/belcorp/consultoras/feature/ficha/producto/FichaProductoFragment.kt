package biz.belcorp.consultoras.feature.ficha.producto

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.promotion.PromotionDetailModel
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.domain.util.StrategyCode
import biz.belcorp.consultoras.feature.ficha.adapter.OfferOptionToneAdapter
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment
import biz.belcorp.consultoras.feature.ficha.enriquecida.FichaEnriquecidaFragment
import biz.belcorp.consultoras.feature.ficha.util.FichaCarouselsHelper
import biz.belcorp.consultoras.feature.ficha.util.FichaCarouselsHelper.generateFrameLayout
import biz.belcorp.consultoras.feature.ficha.util.FichaCarouselsHelper.transformOfferList
import biz.belcorp.consultoras.feature.ficha.util.PromotionModelMapper
import biz.belcorp.consultoras.feature.home.HomeActivity.Companion.KIT_NUEVA_CUV
import biz.belcorp.consultoras.feature.ofertafinal.OfertaFinalActivity.Companion.BROADCAST_PROGRESS_ACTION
import biz.belcorp.consultoras.feature.ofertafinal.OfertaFinalLandingFragment
import biz.belcorp.consultoras.util.*
import biz.belcorp.consultoras.util.analytics.Ficha
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.consultoras.util.broadcast.FestBroadcast
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil.Empty
import biz.belcorp.mobile.components.core.custom.CustomVerticalNestedScrollView
import biz.belcorp.mobile.components.design.carousel.promotion.CarouselPromotion
import biz.belcorp.mobile.components.design.carousel.promotion.model.PromotionModel
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.offers.MiniOffer
import biz.belcorp.mobile.components.offers.MultiPromotion
import biz.belcorp.mobile.components.offers.MultiPromotionItem
import biz.belcorp.mobile.components.offers.model.OfferDuoModel
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.promotion.Promotion
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_ficha_base.*
import java.util.*
import kotlin.collections.ArrayList


class FichaProductoFragment : BaseFichaFragment() {

    /**
     * Extra Section (Upselling / Crosselling / Sugeridos / Ficha Enriquecida)
     */

    private val carouselFlags: ArrayList<Int?> = arrayListOf()

    private var listUpsellingOffers: ArrayList<Oferta>? = null
    private var listCrossellingOffers: ArrayList<Oferta>? = null
    private var listSugeridosOffers: ArrayList<Oferta>? = null

    private val carouselViewsList = arrayListOf<View>()

    private var carouselUpselling: MiniOffer? = null
    private var carouselCrossselling: MiniOffer? = null
    private var carouselSugeridos: MiniOffer? = null

    private var fichaEnriquecidaFragment: FichaEnriquecidaFragment? = null
    private var fichaEnriquecidaContainer: FrameLayout? = null

    private var hasAddedKitNueva: Boolean = false

    /**
     * Promotions
     */
    private var flagIsProductPromocion = false
    private var flagIsFichaIgualCuvPromocion = false
    private var productPromocion: PromotionOfferModel? = null
    private var productCondicionPromociones: PromotionOfferModel? = null
    private var componentePromocion: Promotion? = null
    private var promotionMapper = PromotionModelMapper()

    /**
     * Add / Update Product
     */

    private var flagAddCarousel = false
    private var flagFromAdd: String = FichaFromAdd.ADD_DEFAULT
    private var fichaEnriquecidaAdded = false
    private var fichaPromocionesAdded = false

    private var selloConfig: FestivalSello? = null
    private var typeImageUrl: String? = ""

    private var adapter: OfferOptionToneAdapter? = null

    private val btnDisableWithClic = "disableWithClic"

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

                tieneCompartir = (type != OfferTypes.HV) && (accessFrom != BaseFichaActivity.ACCESS_FROM_CAROUSEL_SUGERIDOS)

                listener?.showShare(tieneCompartir)

                presenter.getData(type, cuv, accessFrom, true)

                nsvContent.setOnScrollChangeListener { _: NestedScrollView?, _: Int,
                                                       _: Int, _: Int, _: Int ->
                    addButtonVisible()
                }

            }

            FichaType.PRODUCT_COMPONENT -> { // Ficha Componente

                tieneCompartir = false
                listener?.showShare(tieneCompartir)

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

        if (isKitNueva) {

            showCounter(false)

            if (hasAddedKitNueva || offer.pedido != null)
                setRemoveButton()
            else
                restoreSimpleButton()

        }

        when (offer.codigoEstrategia) {

            StrategyCode.NORMAL, StrategyCode.PACKS -> simpleButton.isDisable(false)

            StrategyCode.TONOS -> disableWithClicSimpleButton()


            else -> simpleButton.isDisable(false)

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


        val listImages = arrayListOf(offer.imagenURL ?: Empty)

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
            adapter = OfferOptionToneAdapter(offer.vencido, list,
                offer.codigoEstrategia, context, this)

            rvwOffersTone.adapter = adapter

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

        /**
         * Start promotion
         *
         **/

        promotionMapper.df = decimalFormat
        promotionMapper.symbol = moneySymbol

        productPromocion = null
        productCondicionPromociones = null

        if (!oferta.cuv.isNullOrEmpty() && !oferta.cuvPromocion.isNullOrEmpty()) {
            flagIsFichaIgualCuvPromocion = oferta.cuv == oferta.cuvPromocion
        }

        if (flagIsFichaIgualCuvPromocion) {
            setupPromotionTag()
            showTag = true
        }

        productoEncabezado.imageUrl = listImages.toTypedArray()

        if (offer.flagFestival) {
            viewProduct.setTagText(getString(R.string.tag_festival))
            context?.let {
                viewProduct.setTagBackground(ContextCompat.getColor(it, R.color.gana_mas_festival_start), ContextCompat.getColor(it, R.color.gana_mas_festival_end), 3)
            }

            selloConfig?.let { config ->
                config.selloTexto?.let { if (it.isNotEmpty()) viewProduct.setTagText(it) }
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
        presenter.setTextTitlesByOfferType(type ?: Empty)

        val sectionDesc = getOfferTypeForAnalytics(type)
        val subsection = calcularCarouselTypeSubSection(GlobalConstant.EVENT_ACTION_FICHA)

        val onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ficha.view(offer, mUser, analytics)
        }

        presenter.getAnalytics(null, null, sectionDesc, subsection, null, null, onAnalyticsLoaded)

        lnlContent.visibility = View.VISIBLE

        Handler().postDelayed({
            addButtonVisible()
        }, 100)

        // Extra Section

        when (accessFrom) {

            BaseFichaActivity.ACCESS_FROM_CAROUSEL_SUGERIDOS -> {

                fichaEnriquecidaAdded = false
                checkFichaEnriquecidaContent(requireContext())
                setupExtraSection(carouselViewsList)

            }


            else -> {

                safeLet(offer.cuv, type, offer.codigoProducto, offer.fichaProductoConfiguracion) { cuv, type, codigoProducto, configuracion ->

                    carouselFlags.add(null) // Primer carrusel Flag (UpSelling / LAN)
                    carouselFlags.add(null) // CrossSelling carrusel Flag
                    carouselFlags.add(null) // Sugeridos carrusel Flag
                    carouselFlags.add(null) // Promo Premio
                    carouselFlags.add(null) // Promo Condicion

                    when {
                        configuracion.tieneCarruselUpSelling == true -> carouselFlags[0] = CarouselType.UP_SELLING
                        type == OfferTypes.SR -> carouselFlags[0] = CarouselType.DEFAULT
                        type == OfferTypes.ODD -> carouselFlags[0] = CarouselType.DEFAULT
                        type == OfferTypes.LAN -> carouselFlags[0] = CarouselType.DEFAULT
                        else -> listUpsellingOffers = arrayListOf()
                    }

                    if (configuracion.tieneCarruselCrossSelling == true) carouselFlags[1] = CarouselType.CROSS_SELLING
                    else listCrossellingOffers = arrayListOf()

                    if (configuracion.tieneCarruselSugeridos == true) carouselFlags[2] = CarouselType.SUGERIDOS
                    else listSugeridosOffers = arrayListOf()

                    if (offer.agotado != true) {
                        when {
                            configuracion.tienePromocion == true -> carouselFlags[3] = CarouselType.PROMO_PREMIO
                            configuracion.tienePromocion == false && offer.codigoEstrategia != StrategyCode.TONOS -> carouselFlags[4] = CarouselType.PROMO_CONDICION
                            else -> {
                                productPromocion = PromotionOfferModel()
                                productCondicionPromociones = PromotionOfferModel()
                            }
                        }
                    } else {
                        productPromocion = PromotionOfferModel()
                        productCondicionPromociones = PromotionOfferModel()
                    }

                    if (carouselFlags.any { it != null }) {
                        if (!checkCarouselLists())
                            presenter.getCarousels(carouselFlags, cuv, type, codigoProducto, offer)
                    } else {
                        fichaEnriquecidaAdded = false
                        checkFichaEnriquecidaContent(requireContext())
                        setupExtraSection(carouselViewsList)
                    }
                }

            }
        }

        when (accessFrom) {
            BaseFichaActivity.ACCESS_FROM_BUSCADOR_LANDING, BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLEGABLE -> {
                context?.let { context ->
                    type?.let { presenter.saveRecentOffer(oferta, it, context) }
                }
            }
        }
    }

    fun checkForRestoreSimpleButton(hasDeleteItem: Boolean) {

        if (isKitNueva && hasDeleteItem)
            restoreSimpleButton()
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

            checkFichaEnriquecidaContentComponent(requireContext())
            setupExtraSection(carouselViewsList)

        }

    }

    override fun loadPromotion(promotion: PromotionOfferModel?) {
        this.productPromocion = promotion

        showPopupPromotion()
    }


    private fun setupPromotionTag() {
        viewProduct.setTagText(getString(R.string.ficha_promotion))
        context?.let {
            viewProduct.setTagBackground(ContextCompat.getColor(it, R.color.gana_mas_promotion_start), ContextCompat.getColor(it, R.color.gana_mas_promotion_end), 3)
            viewProduct.setTagTextColor(ContextCompat.getColor(it, R.color.gana_mas_promotion_text))
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

    override fun updateCarouselOffers(offers: List<Oferta>, type: Int, promotion: PromotionResponse?) {
        fichaEnriquecidaAdded = false
        fichaPromocionesAdded = false

        when (type) {
            CarouselType.DEFAULT -> {
                listUpsellingOffers = arrayListOf()
                listUpsellingOffers?.addAll(offers)
            }
            CarouselType.UP_SELLING -> {
                listUpsellingOffers = arrayListOf()
                listUpsellingOffers?.addAll(offers)
            }
            CarouselType.CROSS_SELLING -> {
                listCrossellingOffers = arrayListOf()
                listCrossellingOffers?.addAll(offers)
            }
            CarouselType.SUGERIDOS -> {
                listSugeridosOffers = arrayListOf()
                listSugeridosOffers?.addAll(offers)
            }
            CarouselType.PROMO_PREMIO -> {
                promotion?.let {
                    productPromocion = PromotionOfferModel.transform(it)
                } ?: run { productPromocion = PromotionOfferModel() }
            }
            CarouselType.PROMO_CONDICION -> {
                promotion?.let {
                    productCondicionPromociones = PromotionOfferModel.transform(it)
                } ?: run { productCondicionPromociones = PromotionOfferModel() }
            }
        }

        checkCarouselLists()

    }

    private fun checkCarouselLists(): Boolean {

        if (listUpsellingOffers != null &&
            listCrossellingOffers != null &&
            listSugeridosOffers != null &&
            (productPromocion != null || productCondicionPromociones != null)) { // parallel finished

            showOffers()
            scrollToView()

            nsvContent.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    nsvContent.startScrollerTask()
                }
                false
            }

            nsvContent.setOnScrollStoppedListener(object : CustomVerticalNestedScrollView.OnScrollStoppedListener {
                override fun onScrollStopped(position: Int) {
                    trackCarousels()
                }
            })

            nsvContent?.let { scrollView ->
                scrollView.post {
                    scrollView.startScrollerTask()
                }
            }

            return true

        }

        return false

    }

    private fun showOffers() {
        val mContext = requireContext()
        var carouselCount = 0

        carouselViewsList.clear()

        if (listUpsellingOffers?.isNotEmpty() == true) { // añadir upselling
            val upsellingDivider = FichaCarouselsHelper.generateSpaceDivider(mContext)
            val carouselType = if (type == OfferTypes.LAN) CarouselType.DEFAULT else CarouselType.UP_SELLING
            carouselUpselling = getGenericCarousel(mContext, carouselType, listUpsellingOffers)

            carouselUpselling?.let {
                carouselViewsList.add(upsellingDivider)
                carouselViewsList.add(it)
            }

            carouselCount++

            checkPromocionesContent(mContext)
            checkFichaEnriquecidaContent(mContext)
        }

        if (listCrossellingOffers?.isNotEmpty() == true) { // añadir crossselling
            val crosssellingDivider = FichaCarouselsHelper.generateSpaceDivider(mContext)
            carouselCrossselling = getGenericCarousel(mContext, CarouselType.CROSS_SELLING, listCrossellingOffers)

            carouselCrossselling?.let {
                carouselViewsList.add(crosssellingDivider)
                carouselViewsList.add(it)
            }

            carouselCount++

            checkPromocionesContent(mContext)
            checkFichaEnriquecidaContent(mContext)
        }

        if (listSugeridosOffers?.isNotEmpty() == true) { // añadir sugeridos
            val sugeridosDivider = if (carouselCount < 2) FichaCarouselsHelper.generateSpaceDivider(mContext) else FichaCarouselsHelper.generateLineDivider(mContext)
            carouselSugeridos = getGenericCarousel(mContext, CarouselType.SUGERIDOS, listSugeridosOffers)

            carouselSugeridos?.let {
                carouselViewsList.add(sugeridosDivider)
                carouselViewsList.add(it)
            }

            checkPromocionesContent(mContext)
            checkFichaEnriquecidaContent(mContext)
        }

        checkPromocionesContent(mContext)
        checkFichaEnriquecidaContent(mContext)

        setupExtraSection(carouselViewsList)

    }

    private fun scrollToView() {
        when (accessFrom) {
            BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_PREMIO -> {
                val bannerView = AndroidUtils.getChildViewsByClass(lnlExtraSection, CarouselPromotion::class.java)
                bannerView.firstOrNull()?.let {
                    lnlExtraSection.post {
                        ObjectAnimator.ofInt(nsvContent, "scrollY", lnlExtraSection.top + it.bottom).setDuration(500).start()
                    }
                }
            }
            BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_CONDICION -> {
                val carouselView = AndroidUtils.getChildViewsByClass(lnlExtraSection, MultiPromotion::class.java)
                carouselView.firstOrNull()?.let {
                    lnlExtraSection.post {
                        ObjectAnimator.ofInt(nsvContent, "scrollY", lnlExtraSection.top + it.bottom).setDuration(500).start()
                    }
                }
            }
        }
    }

    private fun getGenericCarousel(context: Context, type: Int, list: ArrayList<Oferta>?): MiniOffer {
        val carouselOffers = FichaCarouselsHelper.generateCarousel(context)

        list?.let { offers ->

            carouselOffers.tag = type

            carouselOffers.listener = this
            carouselOffers.updateTitle(getCarouselTitle(type))
            carouselOffers.updateItemPlaceHolder(resources.getDrawable(R.drawable.ic_container_placeholder))

            if (offers.size == 1) carouselOffers.updateSimpleOfert()

            carouselOffers.setProducts(transformOfferList(context, moneySymbol, decimalFormat, offers))

            offers.forEach {
                it.cuv?.let { cuv -> this.ofertaMap.put(cuv, it) }
            }

        }

        return carouselOffers
    }

    private fun checkFichaEnriquecidaContent(context: Context) {

        if (!fichaEnriquecidaAdded && fichaEnriquecidaFragment?.hasData() == true) {

            if (fichaEnriquecidaContainer == null)
                fichaEnriquecidaContainer = generateFrameLayout(context)

            if (fichaEnriquecidaFragment?.isAdded == true) {

                childFragmentManager.beginTransaction()
                    .show(fichaEnriquecidaFragment)
                    .commit()

            } else {

                fichaEnriquecidaContainer?.let { container ->

                    childFragmentManager.beginTransaction()
                        .add(container.id, fichaEnriquecidaFragment, FichaEnriquecidaFragment.TAG)
                        .commitAllowingStateLoss()

                }

            }

            fichaEnriquecidaContainer?.let { container ->
                val existeEnriquecida = carouselViewsList.any { it.id == container.id }
                if (!existeEnriquecida) {
                    carouselViewsList.add(container)
                    fichaEnriquecidaAdded = true
                }
            }
        }
    }

    private fun checkPromocionesContent(context: Context) {
        if (!fichaPromocionesAdded) {
            if (productPromocion?.producto != null && productPromocion?.listaApoyo?.isNotEmpty() == true) {
                genericBannerPromotion(context)
            }

            if (productCondicionPromociones?.producto != null && productCondicionPromociones?.listaApoyo?.isNotEmpty() == true) {
                genericCarouselPromoCondicion(context)
            }
        }
    }

    /**
     * Producto promocion con sus condiciones
     */
    private fun genericBannerPromotion(context: Context) {
        productPromocion?.let { promo ->

            val vwSectionDivider = FichaCarouselsHelper.generateSpaceDivider(context)
            val carouselPromotion = FichaCarouselsHelper.generatePromotion(context)

            //region message promotion
            val textPromotion = if (flagIsFichaIgualCuvPromocion) {
                context.getString(R.string.promotion_text_item_ficha1)
            } else {
                context.getString(R.string.promotion_text_item_ficha2, promo.producto?.descripcionCortada, formatWithMoneySymbol((promo.producto?.precioVenta
                    ?: 0).toString()))
            }
            //endregion

            val promotionBanner = ArrayList<PromotionModel>()
            promotionBanner.add(PromotionModel(
                oferta.tipoOferta,
                oferta.cuv,
                textPromotion,
                oferta.imagenURL ?: biz.belcorp.library.util.StringUtil.Empty
            ))

            carouselPromotion.showLimit = 1
            carouselPromotion.setTitleHead(resources.getString(R.string.promotion_title_banner))
            carouselPromotion.setImageDefault(ContextCompat.getDrawable(context, R.drawable.ic_container_placeholder))
            carouselPromotion.loadPromotion(promotionBanner.toList())

            carouselPromotion.listener = (object : CarouselPromotion.Listener {
                override fun onClick(obj: PromotionModel) {

                    promo.producto?.let {
                        Ficha.clickBannerPromotion(it)
                    }

                    showPopupPromotion()
                }
            })

            carouselViewsList.add(vwSectionDivider)
            carouselViewsList.add(carouselPromotion)

            fichaPromocionesAdded = true
        }
    }


    /**
     * Producto condicion con sus promociones(premios) asociados
     */
    private fun genericCarouselPromoCondicion(context: Context) {
        productCondicionPromociones?.let { promo ->

            val vwSectionDivider = FichaCarouselsHelper.generateSpaceDivider(context)
            val conditionPromotion = FichaCarouselsHelper.generateConditionPromotion(context)

            val listOfferDuo = promotionMapper.transformToOfferDuoModel(promo.producto, promo.listaApoyo)

            val conditionPromotionListener = object : MultiPromotionItem.ButtonListener {
                override fun onOfferClickAdd(item: OfferModel, quantity: Int, counterView: Counter) {
                    promo.producto?.let { prod ->
                        if (prod.cuv == item.key) {
                            flagIsProductPromocion = false
                            agregarDesdePromociones(promo, prod, item.key, quantity, counterView, false)
                        }
                    }
                }

                override fun onPromotionClickAdd(item: OfferModel, quantity: Int, counterView: Counter) {
                    promo.listaApoyo?.firstOrNull { cond -> cond?.cuv == item.key }?.let { pd ->
                        flagIsProductPromocion = true
                        agregarDesdePromociones(promo, pd, item.key, quantity, counterView, false)
                    }
                }

                override fun onSeeMoreClickAdd(item: OfferDuoModel) {
                    promo.listaApoyo?.firstOrNull { it1 -> it1?.cuv == item.promotion.key }?.apply {
                        presenter.getDataPromotion(cuv, promo.producto?.cuv)
                    }
                }
            }
            conditionPromotion.setTitleHead(resources.getString(R.string.promotion_title_banner))
            conditionPromotion.setSubTitleHead(context.getString(R.string.promotion_title_condition, oferta.nombreOferta?.trim()))
            conditionPromotion.setPlaceholderMulti(resources.getDrawable(R.drawable.ic_container_placeholder))
            conditionPromotion.setValues(listOfferDuo, conditionPromotionListener)

            carouselViewsList.add(vwSectionDivider)
            carouselViewsList.add(conditionPromotion)

            fichaPromocionesAdded = true
        }
    }

    private fun checkFichaEnriquecidaContentComponent(context: Context) {

        carouselViewsList.clear()

        if (fichaEnriquecidaFragment?.hasData() == true) {

            if (fichaEnriquecidaContainer == null)
                fichaEnriquecidaContainer = generateFrameLayout(context)

            if (fichaEnriquecidaFragment?.isAdded == true) {

                childFragmentManager.beginTransaction()
                    .show(fichaEnriquecidaFragment)
                    .commit()

            } else {

                fichaEnriquecidaContainer?.let { container ->

                    childFragmentManager.beginTransaction()
                        .add(container.id, fichaEnriquecidaFragment, FichaEnriquecidaFragment.TAG)
                        .commitAllowingStateLoss()

                }

            }

            fichaEnriquecidaContainer?.let { container ->

                carouselViewsList.add(container)
                fichaEnriquecidaAdded = true

            }

        }

    }

    private fun trackCarousels() {

        val screenRect = Rect()

        nsvContent?.let {

            it.getGlobalVisibleRect(screenRect)

            val upsellingElement = carouselUpselling?.getVisibleElement()
            val crosssellingElement = carouselCrossselling?.getVisibleElement()
            val sugeridosElement = carouselSugeridos?.getVisibleElement()

            upsellingElement?.let { element ->
                if (elementIsVisible(element, screenRect))
                    carouselUpselling?.trackVisibleElement()
            }

            crosssellingElement?.let { element ->
                if (elementIsVisible(element, screenRect))
                    carouselCrossselling?.trackVisibleElement()
            }

            sugeridosElement?.let { element ->
                if (elementIsVisible(element, screenRect))
                    carouselSugeridos?.trackVisibleElement()
            }

        }

    }

    private fun showPopupPromotion() {
        productPromocion?.let { promo ->

            val promotionOffer = promotionMapper.transformPromotion(promo.producto)
            val conditionsOffer = promotionMapper.transformListPromotion(promo.listaApoyo)

            val placeholderImage = resources.getDrawable(R.drawable.ic_container_placeholder)

            promotionOffer?.let {
                componentePromocion = Promotion.Builder(requireContext())
                    .setTitleCondition(resources.getString(R.string.promotion_title_conditions))
                    .setTitlePromotion(resources.getString(R.string.promotion_title_promotion))
                    .setPlaceholderCarrusel(placeholderImage)
                    .setImageDefault(placeholderImage)
                    .setImage(it.imageURL)
                    .activeButtonAdd(true)
                    .setPromotionOffer(it)
                    .setConditionOfferList(conditionsOffer)
                    .setPromotionConditionListener(object : Promotion.PromotionConditionListener {
                        override fun didPressedItem(item: OfferModel, pos: Int) {

                        }

                        override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter) {
                            promo.listaApoyo?.firstOrNull { cond -> cond?.cuv == keyItem }?.let { pd ->
                                flagIsProductPromocion = false
                                agregarDesdePromociones(promo, pd, keyItem, quantity, counterView, true)
                            }
                        }

                        override fun didPressedItemButtonSelection(item: OfferModel, pos: Int) {

                        }

                        override fun didPressedItemButtonShowOffer(item: OfferModel, pos: Int) {

                        }

                        override fun impressionItems(typeLever: String, list: ArrayList<OfferModel>) {

                        }
                    })
                    .setPromotionOfferListener(object : Promotion.PromotionOfferListener {
                        override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter) {
                            promo.producto?.let { pd ->
                                flagIsProductPromocion = true
                                agregarDesdePromociones(promo, pd, keyItem, quantity, counterView, true)
                            }
                        }
                    })
                    .show()
            }

        }
    }

    private fun getCarouselTitle(typeCarousel: Int): String {

        var simpleTitleFormat = ""
        var packTitle = ""

        when (typeCarousel) {

            CarouselType.DEFAULT -> {
                simpleTitleFormat = getString(R.string.offers_default_title)
                packTitle = simpleTitleFormat
            }
            CarouselType.UP_SELLING -> {
                simpleTitleFormat = String.format(getString(R.string.offers_upselling_title), oferta.nombreOferta
                    ?: "")
                packTitle = getString(R.string.offers_upselling_title_pack)
            }
            CarouselType.CROSS_SELLING -> {
                simpleTitleFormat = String.format(getString(R.string.offers_crossselling_title), oferta.nombreOferta
                    ?: "")
                packTitle = getString(R.string.offers_crossselling_title_pack)
            }
            CarouselType.SUGERIDOS -> {
                simpleTitleFormat = String.format(getString(R.string.offers_sugeridos_title), oferta.nombreOferta
                    ?: "")
                packTitle = getString(R.string.offers_sugeridos_title_pack)
            }
        }

        val simpleComponente = oferta.componentes == null || oferta.componentes?.size!! <= 1
        var factorCuadre = false

        oferta.componentes?.let {
            factorCuadre = it.size == 1 && it[0]?.factorRepeticion == 1
        }

        return if (simpleComponente && factorCuadre) simpleTitleFormat
        else packTitle

    }

    private fun agregarDesdePromociones(promotion: PromotionOfferModel, promociondetail: PromotionDetailModel, keyItem: String, quantity: Int, counterView: Counter, isPromotionDialog: Boolean) {

        val offerPromotion = PromotionDetailModel.transforToOffer(promociondetail)

        if (flagIsProductPromocion) offerPromotion?.flagPromocion = true

        val palanca = offerPromotion?.tipoOferta ?: ""

        var codigoOrigen = if (!flagIsProductPromocion) OffersOriginType.ORIGEN_CONDICION_PROMOCION_FICHA_CARRUSEL else OffersOriginType.ORIGEN_CONTENEDOR_PROMOCION_FICHA

        if (palanca == OfferTypes.CAT && offerPromotion?.marcaID != null) {
            codigoOrigen += Belcorp.getBrandOrigenById(offerPromotion?.marcaID!!)
        }

        offerPromotion?.let {
            flagFromAdd = FichaFromAdd.ADD_PROMOTION
            presenter.agregar(it, quantity, counterView, DeviceUtil.getId(context), codigoOrigen, palanca, null, isPromotionDialog = isPromotionDialog)
        }

    }

    override fun addFromFicha() {

        val anim = ObjectAnimator.ofInt(nsvContent, "scrollY", rvwOffersTone.bottom)
        anim.setDuration(300)

        when (simpleButton.getTag()?.toString() ?: Empty) {

            btnDisableWithClic -> {

                val offer = adapter?.items

                var recorrerLista: Boolean = true

                offer?.forEachIndexed { index, comp ->
                    comp?.opciones?.forEach {
                        if (!comp.selected && recorrerLista) {
                            comp.indicarFaltaSeleccion = true
                            adapter?.notifyDataSetChanged()
                            recorrerLista = false
                            anim.start()
                        }
                    }
                }
            }

            else -> type?.let { type ->

                val adapter = rvwOffersTone.adapter as OfferOptionToneAdapter
                val offer = oferta.copy()
                offer.componentes = adapter.getTonesSelected()

                var value = Empty
                var codigo = Empty
                var palanca = if (type == OfferTypes.OPM) OfferTypes.RD else type

                if (flagIsFichaIgualCuvPromocion) offer.flagPromocion = true

                when (accessFrom) {

                    BaseFichaActivity.ACCESS_FROM_BUSCADOR_LANDING -> {
                        codigo = if (type == OfferTypes.CAT && marcaID != null) {
                            SearchOriginType.ORIGEN_BUSCADOR_LANDING_FICHA + Belcorp.getBrandOrigenById(marcaID!!)
                        } else {
                            SearchOriginType.ORIGEN_BUSCADOR_LANDING_FICHA
                        }
                    }

                    BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_PREMIO,
                    BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_CONDICION,
                    BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLEGABLE -> {
                        codigo = if (type == OfferTypes.CAT && marcaID != null) {
                            SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE_FICHA + Belcorp.getBrandOrigenById(marcaID!!)
                        } else {
                            SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE_FICHA
                        }
                    }

                    BaseFichaActivity.ACCESS_FROM_GANAMAS -> {
                        origenPedidoWebFrom?.let { origenPedidoWebFrom ->
                            codigo = getFichaOrderOrigin(origenPedidoWebFrom, fichaOfferType)
                        }
                    }

                    BaseFichaActivity.ACCESS_FROM_PEDIDO_GANASMAS -> {
                        value = origenPedidoWeb ?: "0"
                    }

                    BaseFichaActivity.ACCESS_FROM_CAROUSEL_SUGERIDOS -> {
                        origenPedidoWebFrom?.let { origenPedidoWebFrom ->
                            codigo = getFichaOrderOrigin(origenPedidoWebFrom, fichaOfferType)
                        }
                    }

                    BaseFichaActivity.ACCESS_FROM_LANDING_FEST -> {
                        codigo = if (type == OfferTypes.CAT && marcaID != null) {
                            SearchOriginType.ORIGEN_LANDING_FESTIVAL + Belcorp.getBrandOrigenById(marcaID!!)
                        } else {
                            SearchOriginType.ORIGEN_LANDING_FESTIVAL
                        }
                    }

                    BaseFichaActivity.ACCESS_FROM_LANDING_OFERTA_FINAL -> {
                        codigo = SearchOriginType.ORIGEN_LANDING_FICHA_OFERTA_FINAL
                        palanca = OfertaFinalLandingFragment.TYPE
                    }

                }

                flagFromAdd = FichaFromAdd.ADD_DEFAULT
                flagAddCarousel = false

                if (value.isNotEmpty())
                    presenter.agregar(offer, simpleCounter.quantity, simpleCounter,
                        DeviceUtil.getId(context), null, null, value)
                else
                    presenter.agregar(offer, simpleCounter.quantity, simpleCounter,
                        DeviceUtil.getId(context), codigo, palanca, null)

            }

        }


    }

    override fun addFromCarousel(quantity: Int, keyItem: String, counterView: Counter, typeCarousel: Int) {

        if (NetworkUtil.isThereInternetConnection(context())) {
            ofertaMap[keyItem]?.let {

                val typeOffer: String? = if (type == OfferTypes.LAN) OfferTypes.LAN else it.tipoOferta

                typeOffer?.let { offerType ->

                    var codigo = ""
                    val palanca = if (offerType == OfferTypes.OPM) OfferTypes.RD else offerType

                    fichaOfferTypeCarousel = typeCarousel

                    when (accessFrom) {

                        BaseFichaActivity.ACCESS_FROM_BUSCADOR_LANDING -> {

                            codigo = getCarouselOrderOrigin(
                                SearchOriginType.ORIGEN_BUSCADOR_LANDING_FICHA_CARRUSEL,
                                fichaOfferTypeCarousel)

                        }

                        BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_PREMIO,
                        BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_CONDICION,
                        BaseFichaActivity.ACCESS_FROM_BUSCADOR_DESPLEGABLE -> {

                            codigo = getCarouselOrderOrigin(
                                SearchOriginType.ORIGEN_BUSCADOR_DESPLEGABLE_FICHA_CARRUSEL,
                                fichaOfferTypeCarousel)

                        }

                        BaseFichaActivity.ACCESS_FROM_GANAMAS -> {

                            origenPedidoWebFrom?.let { origenPedidoWebFrom ->
                                codigo = getCarouselOrderOrigin(origenPedidoWebFrom, fichaOfferTypeCarousel)
                            }

                        }

                        BaseFichaActivity.ACCESS_FROM_PEDIDO_GANASMAS -> {

                            origenPedidoWebFrom?.let { origenPedidoWebFrom ->
                                codigo = getCarouselOrderOrigin(origenPedidoWebFrom, fichaOfferTypeCarousel)
                            }

                        }
                    }

                    flagAddCarousel = true
                    flagFromAdd = FichaFromAdd.ADD_CAROUSEL
                    presenter.agregar(it, quantity, counterView, DeviceUtil.getId(context), codigo, palanca, null)

                }
            }
        } else {
            showNetworkError()
        }

    }

    override fun onAddComplete(quantity: Int, productCUV: ProductCUV, showImage: Boolean, message: String?, codeAlert: String?) {

        simpleCounter.changeQuantity(1)

        when (codeAlert) {
            GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> {
                context?.let { itContext ->
                    showBottomDialogAction(itContext, message) {
                        val extras = Bundle()
                        goToFest(extras)
                    }
                }
            }
            GlobalConstant.CODE_OK -> {
                val messageDialog = message ?: getString(R.string.msj_offer_added_default)
                val image = ImageUtils.verifiedImageUrl(productCUV.fotoProducto,
                    productCUV.fotoProductoSmall, productCUV.fotoProductoMedium)

                context?.let {
                    val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
                    var url = if (showImage) image else null

                    if (!this.typeImageUrl.isNullOrEmpty()) {
                        url = this.typeImageUrl
                    }

                    showBottomDialog(it, messageDialog, url, colorText)
                }
            }
        }

        val flagAddCarousel = flagFromAdd == FichaFromAdd.ADD_CAROUSEL
        val location = if (!flagAddCarousel) arguments?.getString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM) else OffersOriginType.ORIGEN_CONTENEDOR_FICHA_CARRUSEL
        val section = if (!flagAddCarousel) arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER) else fichaOfferTypeCarousel.toString()
        val subsect = if (!flagAddCarousel) GlobalConstant.EVENT_ACTION_FICHA else GlobalConstant.CARRUSEL
        val oriLocation = if (!flagAddCarousel) "" else arguments?.getString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM)
        val oriSection = if (!flagAddCarousel) "" else arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER)

        val currentLocation = calcularOfferOriginTypeLocation(location)
        val currentSection = calcularOffersOriginTypeSection(section)
        val sectionDesc = getOfferTypeForAnalytics(arguments?.getString(BaseFichaActivity.EXTRA_TYPE_OFFER))
        val subsection = calcularCarouselTypeSubSection(subsect)
        val originLocation = calcularOfferOriginTypeLocation(oriLocation)
        val originSection = calcularOffersOriginTypeSection(oriSection)

        var onAnalyticsLoaded: (analytics: Analytics) -> Unit = { analytics ->
            Ficha.addItemCarousel(mUser, productCUV, analytics)
        }

        when (flagFromAdd) {
            FichaFromAdd.ADD_CAROUSEL -> {
                onAnalyticsLoaded = { analytics ->
                    Ficha.addItemCarousel(mUser, productCUV, analytics)
                }
            }
            FichaFromAdd.ADD_PROMOTION -> {
                if (flagIsProductPromocion) {
                    onAnalyticsLoaded = { analytics ->
                        mUser?.let { Ficha.addProductPromotion(it, productCUV) }
                    }
                }
            }
            FichaFromAdd.ADD_DEFAULT -> {
                onAnalyticsLoaded = { analytics ->
                    mUser?.let { Ficha.addItemCarousel(it, productCUV, analytics) }
                }
            }
        }

        hasAddedKitNueva = isKitNueva

        if (isKitNueva)
            KIT_NUEVA_CUV = productCUV.cuv

        presenter.getAnalytics(currentLocation, currentSection, sectionDesc, subsection,
            originLocation, originSection, onAnalyticsLoaded)

        activity?.let { FestBroadcast.sendAddUpdateToCart(it) }
        listener?.updateOrders(quantity, isKitNueva)
        requireActivity().sendBroadcast(Intent(BROADCAST_PROGRESS_ACTION))
        refreshInit()

    }

    override fun close() {

        if (isKitNueva)
            KIT_NUEVA_CUV = null

        hasAddedKitNueva = false
        listener?.updateOrders(-1, isKitNueva)
        val colorText = ContextCompat.getColor(requireContext(), R.color.lograste_puntaje)
        showBottomDialog(requireContext(), getString(R.string.msj_offer_removed_default), null, colorText)
        refreshInit()

    }

    override fun showLoadingDialog() {
        componentePromocion?.showLoading()
    }

    override fun hideLoadingDialog() {
        componentePromocion?.hideLoading()
    }

    override fun getScreenName(): String {

        return when (accessFrom) {
            BaseFichaActivity.ACCESS_FROM_CAROUSEL_SUGERIDOS -> GlobalConstant.SCREEN_FICHA_RESUMIDA
            else -> GlobalConstant.SCREEN_GANA_MAS_DETAIL
        }

    }

    override fun getOfferTypeForFicha(type: String): String {

        return if (this.type == OfferTypes.LAN) OfferTypes.LAN
        else type

    }

    override fun getVisibleOfertas(list: java.util.ArrayList<OfferModel>, type: Int): List<Oferta> {

        return when (type) {
            CarouselType.DEFAULT, CarouselType.UP_SELLING -> listUpsellingOffers?.filter { exists(it.cuv, list) }
                ?: arrayListOf()
            CarouselType.CROSS_SELLING -> listCrossellingOffers?.filter { exists(it.cuv, list) }
                ?: arrayListOf()
            CarouselType.SUGERIDOS -> listSugeridosOffers?.filter { exists(it.cuv, list) }
                ?: arrayListOf()
            else -> arrayListOf()
        }

    }

    override fun setupSelloFestival(selloConfig: FestivalSello?) {
        this.selloConfig = selloConfig
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UXCam.tagScreenName(UXCamUtils.FichaProductoFragmentName)
    }

}
