package biz.belcorp.consultoras.feature.ficha

import android.content.Context
import android.util.Log
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.model.orders.OrderModelDataMapper
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.common.model.product.ProductItemModelDataMapper
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.service.impl.OfferService.Companion.OFFER_SERVICE_ONLINE
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.exception.ErrorFactory
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity.Companion.ACCESS_FROM_PASE_PEDIDO
import biz.belcorp.consultoras.domain.util.PromocionTypes
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaView
import biz.belcorp.consultoras.feature.home.fest.FestFragment
import biz.belcorp.consultoras.feature.home.fest.FestPresenter
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.consultoras.util.extensions.parallel
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil.Empty
import biz.belcorp.mobile.components.design.counter.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis


@PerActivity
class FichaPresenter @Inject
constructor(private val orderUseCase: OrderUseCase,
            private val origenMarcacionUseCase: OrigenMarcacionUseCase,
            private val userUseCase: UserUseCase,
            private val offerUseCase: OfferUseCase,
            private val menuUseCase: MenuUseCase,
            private val origenPedidoUseCase: OrigenPedidoUseCase,
            private val caminoBrillanteUseCase: CaminoBrillanteUseCase,
            private val festivalUseCase: FestivalUseCase,
            private val orderModelDataMapper: OrderModelDataMapper,
            private val clientModelDataMapper: ClienteModelDataMapper,
            private val productItemModelDataMapper: ProductItemModelDataMapper) : Presenter<BaseFichaView>, SafeLet {

    private var view: BaseFichaView? = null
    private var user: User? = null
    private val CONS_CANTIDAD_PREMIO = 1

    override fun attachView(view: BaseFichaView) {
        this.view = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.view = null
        this.origenMarcacionUseCase.dispose()
        this.menuUseCase.dispose()
        this.offerUseCase.dispose()
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.origenPedidoUseCase.dispose()
        this.caminoBrillanteUseCase.dispose()
        this.festivalUseCase.dispose()
    }

    internal fun initScreenTrack() {
        GlobalScope.launch(Dispatchers.IO) {
            user?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.initScreenTrack(it)
                }
            } ?: run {
                userUseCase.getUser()?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.initScreenTrack(it)
                    }
                }
            }
        }
    }

    fun getMenuActive(code1: String, code2: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                menuUseCase.getActive2(code1, code2)?.let { view?.onGetMenu(it) }
            } catch (e: Exception) {
                e.message?.let { BelcorpLogger.d(it) }
            }
        }
    }

    fun getData(type: String?, cuv: String?, accessFrom: Int?, shouldCallOnline: Boolean = false) {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {

            view?.imagesMaxFicha = orderUseCase.getImagesMaxFicha()

            userUseCase.getUser()?.let {
                user = it
                try {
                    it.campaing?.let { camp ->
                        OFFER_SERVICE_ONLINE = shouldCallOnline
                        offerUseCase.getFicha(camp.toInt(), type, cuv, it.countryMoneySymbol,
                            it.billingStartDate, it.codigoPrograma, it.consecutivoNueva, it.montoMaximoPedido,
                            it.consultoraNueva, it.numberOfCampaings?.toInt() ?: 0, it.consultantName, it.regionCode, it.zoneCode,
                            it.codigoSeccion, it.isUltimoDiaFacturacion, it.isPagoContado, it.billingEndDate)?.let { offer ->
                            if(accessFrom == ACCESS_FROM_PASE_PEDIDO){
                                if(type==OfferTypes.ODD){
                                    val request = createOfferRequest(type)
                                    val ofertas = offerUseCase.ofertasNoCache(request)
                                    val ofertaVencida = if (ofertas == null || ofertas.isEmpty())
                                        true
                                    else
                                        isOfertaVencida(cuv, ofertas)
                                    offer.apply {
                                        vencido = ofertaVencida
                                    }
                                }
                            }
                            var selloConfig: FestivalSello? = null
                            if (offer.flagFestival)
                                selloConfig = festivalUseCase.getSelloConfiguracion()


                            GlobalScope.launch(Dispatchers.Main) {
                                view?.setUser(it)
                                view?.hideLoading()
                                view?.setupSello(selloConfig)
                                view?.load(offer)
                                OFFER_SERVICE_ONLINE = false
                            }
                        } ?: GlobalScope.launch(Dispatchers.Main) {

                            view?.showError()
                            OFFER_SERVICE_ONLINE = false

                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.setUser(it)
                        view?.hideLoading()
                        view?.showError(true)
                        e.message?.let { msg ->
                            BelcorpLogger.d(msg)
                        }
                    }
                }
            }
        }
    }


    fun getDataComponent(component: Componente?) {

        GlobalScope.launch(Dispatchers.IO) {

            user?.let { usuario ->

                GlobalScope.launch(Dispatchers.Main) {

                    view?.setUser(usuario)
                    view?.imagesMaxFicha = orderUseCase.getImagesMaxFicha()
                    view?.load(component)

                }

            }

        }

    }

    fun getShareURL(pCuv: String?, type: String?, pMarcaID: Int?, marca: String?, oferta: Oferta) {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.getFichaShareURL(pCuv, type, pMarcaID, marca, oferta)
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    result?.let {
                        if (it.isNotEmpty())
                            view?.share(it)
                        else view?.showError(null)
                    } ?: view?.showError(null)
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    view?.showError(null)
                }
            }
        }
    }

    /**
     * Llamada en paralelo para los carruseles
     */

    fun getCarousels(flags: MutableList<Int?>, cuv: String, type: String, code: String, offer: Oferta) {
        GlobalScope.launch {
            val time = measureTimeMillis {

                flags.parallel {
                    when (it) {
                        CarouselType.DEFAULT -> getOffersByLeverAndSap(cuv, type, code) // Carrusel LAN, ODD, SR
                        CarouselType.UP_SELLING -> getUpsellingOffers(offer) // Carrusel UpSelling
                        CarouselType.CROSS_SELLING -> getCrossellingOffers(offer, false) // Carrusel CrossSelling
                        CarouselType.SUGERIDOS -> getCrossellingOffers(offer, true) // Carrusel Sugeridos
                        CarouselType.PROMO_PREMIO -> getOffersPromotions(cuv, CarouselType.PROMO_PREMIO, PromocionTypes.PROMOCION_PREMIO)
                        CarouselType.PROMO_CONDICION -> getOffersPromotions(cuv, CarouselType.PROMO_CONDICION, PromocionTypes.PROMOCION_CONDICIONES)
                    }
                }
            }

            BelcorpLogger.i("Time x get offers $time")
        }
    }

    /**
     * Obtiene ofertas de LAN
     */

    fun getOffersByLeverAndSap(currentCUV: String, leverType: String, codigoProducto: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                offerUseCase.getOffersByLeverAndSap(currentCUV, leverType,
                    codigoProducto, RevistaDigitalType.CON_RD_SUSCRITA_ACTIVA).let { offers ->
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.setCarouselOffers(offers, CarouselType.DEFAULT)
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    BelcorpLogger.d(e)
                    view?.setCarouselOffers(arrayListOf(), CarouselType.DEFAULT)
                }
            }
        }

    }

    fun  getAnalytics(currentLocation: String?, currentSection: String?, sectionType: String?,
                      subsection: String?, originLocation: String?, originSection:String?,
                      onAnalyticsLoaded: (analytics: Analytics) -> Unit) {

        GlobalScope.launch {

            try {

                val analytics = Analytics()

                val list = origenMarcacionUseCase.getValorLista(currentLocation
                    ?: Empty, currentSection ?: Empty)

                if (list == Empty)
                    analytics.list = GlobalConstant.NOT_AVAILABLE
                else
                    analytics.list = list

                analytics.dimension16 = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(sectionType))
                analytics.dimension17 = origenMarcacionUseCase.getValorSubseccion(subsection ?: Empty)

                val dimension18 = origenMarcacionUseCase.getValorLista(originLocation
                    ?: Empty, originSection ?: Empty)

                if (dimension18 == Empty)
                    analytics.dimension18 = GlobalConstant.NOT_AVAILABLE
                else
                    analytics.dimension18 = dimension18

                GlobalScope.launch(Dispatchers.Main) { onAnalyticsLoaded.invoke(analytics) }

            } catch (e: Exception) {

                Log.w(ANALYTICS_TAG, e.message)

            }

        }

    }

    /**
     * Obtiene ofertas de Upselling
     */

    fun getUpsellingOffers(oferta: Oferta) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.ofertasUpselling(oferta.cuv ?: "",
                    getCodes(oferta.cuv ?: "", oferta.componentes),
                    oferta.precioCatalogo ?: 0.0)
                GlobalScope.launch(Dispatchers.Main) {
                    view?.setCarouselOffers(result.filterNotNull(), CarouselType.UP_SELLING)
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    BelcorpLogger.d(e)
                    view?.setCarouselOffers(arrayListOf(), CarouselType.UP_SELLING)
                }
            }
        }
    }

    /**
     * Obtiene ofertas de Crossselling
     */

    fun getCrossellingOffers(oferta: Oferta, sugeridos: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                user?.let { usuario ->
                    val result = offerUseCase.ofertasCrosselling(oferta.tipoOferta ?: "",
                        usuario.campaing?.toInt() ?: 0,
                        oferta.cuv ?: "",
                        usuario.billingStartDate ?: "",
                        sugeridos) ?: listOf()
                    GlobalScope.launch(Dispatchers.Main) {
                        if (sugeridos)
                            view?.setCarouselOffers(result.filterNotNull(), CarouselType.SUGERIDOS)
                        else
                            view?.setCarouselOffers(result.filterNotNull(), CarouselType.CROSS_SELLING)
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    BelcorpLogger.d(e)
                    if (sugeridos)
                        view?.setCarouselOffers(arrayListOf(), CarouselType.SUGERIDOS)
                    else
                        view?.setCarouselOffers(arrayListOf(), CarouselType.CROSS_SELLING)

                }
            }
        }
    }

    private fun getCodes(cuv: String, componentes: List<Componente?>?): List<String> {
        val list = arrayListOf<String?>()
        componentes?.let {
            it.forEach { code ->
                code?.let { componente ->
                    var notBolsa = false
                    val duplicatedCUV = componente.codigoProducto == cuv
                    componente.nombreComercial?.let { nombreComercial ->
                        notBolsa = nombreComercial.isNotEmpty() &&
                            !nombreComercial.contains("bolsa", true)
                    }
                    if (notBolsa && !duplicatedCUV) {
                        list.add(componente.codigoProducto)
                    }
                }
            }
        }
        return list.filterNotNull()
    }

    //agrega una oferta
    fun agregar(oferta: Oferta, quantity: Int, counterView: Counter, identifier: String,
                codigo: String?, palanca: String?, valor: String?,
                editable: Boolean = false, id: Int = 0, clientID: Int = 0, reemplazarFestival: Boolean? = false,
                isPromotionDialog: Boolean = false) {
        if (isPromotionDialog) view?.showLoadingDialog() else view?.showLoading()

        val productCUV = ProductCUV().apply {
            cuv = oferta.cuv
            description = oferta.nombreOferta
            descripcionMarca = oferta.nombreMarca
            marcaId = oferta.marcaID
            precioCatalogo = oferta.precioCatalogo
            precioValorizado = oferta.precioValorizado
            fotoProducto = oferta.imagenURL
            tipoEstrategiaId = oferta.tipoEstrategiaID
            codigoEstrategia = oferta.codigoEstrategia?.toInt()
            estrategiaId = oferta.estrategiaID
            indicadorMontoMinimo = oferta.indicadorMontoMinimo
            limiteVenta = oferta.limiteVenta
            cantidad = quantity
            listaOpciones = ProductCUVOpcion.transformList(oferta.componentes)
            flagFestival = true
            flagPromocion = oferta.flagPromocion
            this.identifier = identifier

            this.flagFestival = false

            this.reemplazarFestival = reemplazarFestival
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                var valorOrigen = ""

                if (valor == null) {
                    if (palanca != null && codigo != null)
                        valorOrigen = origenPedidoUseCase.getValor(palanca, codigo).toString()
                } else {
                    valorOrigen = valor
                }

                productCUV.apply {
                    origenPedidoWeb = valorOrigen
                }

                val result = orderUseCase.insertarPedido(productCUV, identifier, editable, id, clientID, reemplazarFestival)

                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (isPromotionDialog) view?.hideLoadingDialog() else view?.hideLoading()

                        when (it.code) {
                            GlobalConstant.CODE_OK, GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> {
                                userUseCase.updateScheduler2()
                                it.data?.let { msgs ->
                                    view?.updateProlMessages(msgs)
                                }
                                view?.addComplete(quantity, productCUV, orderUseCase.getImageDialogEnabled(), it.message, it.code)
                                counterView.resetQuantity()
                            }
                            UpdateProductCode.ERROR_CRITERIO_PREMIO_FESTIVAL -> {

                                view?.validateFestCondition(oferta, quantity, counterView, identifier, codigo, palanca, valor, editable, id, clientID, true, it.message)

                            }
                            //implementar code = 2010:  indica premio del siguiente nivel
                            else -> {
                                view?.showError(it.message)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    if (isPromotionDialog) view?.hideLoadingDialog() else view?.hideLoading()
                    when (e) {
                        is NetworkErrorException -> view?.showError(false)
                        else -> {
                            e.message?.let {
                                BelcorpLogger.d(it)
                                view?.showError(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun addToCartPrize(item: ProductCUV, identifier: String, codigoOrigenPedidoWeb: String) {
        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            try {

                item.tipoPersonalizacion?.let { p ->
                    item.origenPedidoWeb = origenPedidoUseCase.getValor(p, codigoOrigenPedidoWeb).toString()
                }

                val result = orderUseCase.insertarPedido(item, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {

                        if (it.code == GlobalConstant.CODE_OK) {
                            userUseCase.updateScheduler2()

                            view?.addComplete(CONS_CANTIDAD_PREMIO, item, orderUseCase.getImageDialogEnabled(), it.message, it.code)


                        } else {

                        }
                        view?.hideLoading()
                    }
                }
            } catch (e: Exception) {

                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    if (e is VersionException)
                        view?.onVersionError(e.isRequiredUpdate, e.url)
                    else
                        e.message?.let {
                            BelcorpLogger.d(it)
                        }
                }
            }
        }
    }


    fun deleteItem(order: OrderModel?, item: ProductItem) {
        order?.let {
            view?.showLoading()

            val order = orderModelDataMapper.transform(it)

            order?.let { formattedOrder ->

                val orderListItem = productItemModelDataMapper.transform(item)

                orderListItem?.let { orderListItem ->

                    orderUseCase.deleteProduct(formattedOrder, orderListItem, OrderItemDeletedObserver())
                }
            }
        }
    }

    fun deleteItem(formattedOrder: FormattedOrder?, orderListItem: OrderListItem?) {

        if (formattedOrder != null && orderListItem != null) {

            view?.showLoading()
            orderUseCase.deleteProduct(formattedOrder, orderListItem, OrderItemDeletedObserver())

        }

    }

    private inner class OrderItemDeletedObserver : BaseObserver<BasicDto<Boolean>?>() {
        override fun onNext(t: BasicDto<Boolean>?) {
            t?.let {

                when (it.code) {
                    UpdateProductCode.OK -> {
                        view?.close()
                        // getOrder(CallOrderType.FROM_DELETE_ITEM)
                    }
                    else -> {
                        when (it.code) {
                            UpdateProductCode.ERROR_MONTO_MINIMO_RESERVADO -> view?.onProductNotAdded(t.message)
                        }
                    }
                }
                view?.hideLoading()
            }
        }

        override fun onError(exception: Throwable) {
            view?.hideLoading()
            if (view == null) return
            when (exception) {
                is VersionException -> view?.onVersionError(exception.isRequiredUpdate, exception.url)
                else -> view?.onError(ErrorFactory.create(exception))
            }
        }
    }

    fun getDataCaminoBrillante(type: String?, cuv: String?) {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            userUseCase.getUser()?.let {
                user = it
                try {
                    caminoBrillanteUseCase.getFichaProducto(type ?: "0", cuv
                        ?: "")?.let { response ->
                        GlobalScope.launch(Dispatchers.Main) {
                            view?.setUser(it)
                            view?.hideLoading()
                            response.data?.let {
                                view?.load(it)
                            } ?: view?.showError(true)
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.setUser(it)
                        view?.hideLoading()
                        view?.showError(true)
                        e.message?.let { msg ->
                            BelcorpLogger.d(msg)
                        }
                    }
                }
            }
        }
    }


    open inner class GetFormattedOrdersObserver(var callFrom: Int = 0)
        : BaseObserver<FormattedOrder?>() {

        override fun onNext(t: FormattedOrder?) {
            userUseCase.getLogin(GetLogin(t, callFrom))
        }

        override fun onError(exception: Throwable) {
            if (view == null) return
            when (exception) {
                is VersionException -> {
                    view?.hideLoading()
                    view?.onVersionError(exception.isRequiredUpdate, exception.url)
                }
                else -> view?.onFormattedOrderReceived(null, null, CallOrderType.FROM_INIT)
            }
        }
    }

    private inner class GetLogin(var formattedOrder: FormattedOrder?, var callFrom: Int) : BaseObserver<Login?>() {
        override fun onNext(t: Login?) {
            val orderModel = orderModelDataMapper.transform(formattedOrder)
            orderModel?.isPagoContado = formattedOrder?.pedidoValidado ?: false && t?.isPagoContado ?: false
            t?.montoMaximoDesviacion?.let {

                orderModel?.montoMaximoDesviacion = it.toBigDecimal()
            }
            view?.onFormattedOrderReceived(orderModel,
                clientModelDataMapper.transform(formattedOrder?.clientesDetalle)
                , callFrom)
        }
    }

    fun setTextTitlesByOfferType(typeOffer: String) {
        when (typeOffer) {
            OfferTypes.HV -> view?.setTextTitles(R.string.ficha_price_for_you, false, null, false, null, false)
            OfferTypes.CAT -> view?.setTextTitles(R.string.ficha_price_client_simple, false, null, false, null, false)
            OfferType.KIT_CAMINO_BRILLANTE -> view?.setTextTitles(R.string.ficha_price_for_you, false, R.string.ficha_price_normal, false, R.string.ficha_earn_up, false)
            OfferType.DEMOSTRADOR_CAMINO_BRILLANTE -> view?.setTextTitles(R.string.ficha_price_for_you, false, R.string.ficha_price, true, null, false)
            else -> view?.setTextTitles(R.string.ficha_price_for_you, false, R.string.ficha_price_client, false, R.string.ficha_earn_up, false)
        }
    }

    fun createOfferRequest(type: String): OfertaRequest {
        return OfertaRequest().apply {
            campaniaId = user?.campaing
            zonaId = user?.zoneID?.toInt()
            codigoZona = user?.zoneCode
            codigoRegion = user?.regionCode
            esSuscrita = user?.revistaDigitalSuscripcion == RevistaDigitalType.CON_RD_SUSCRITA_ACTIVA
            esActiva = user?.isRDEsActiva
            tieneMG = user?.isTieneMG
            diaInicio = user?.diaFacturacion
            fechaInicioFacturacion = user?.billingStartDate
            flagComponentes = true
            flagSubCampania = false
            flagIndividual = true
            tipo = type
        }
    }

    private fun isOfertaVencida(cuv: String?, ofertas: List<Oferta?>): Boolean {
        return cuv?.let { ofertas.none { it?.cuv == cuv } } ?: false
    }

    fun getDataPremio(type: String?, cuv: String?, accessFrom: Int?) {

        view?.showLoading()

        //var festivalResponse: FestivalResponse?

        GlobalScope.launch(Dispatchers.IO) {

            view?.imagesMaxFicha = orderUseCase.getImagesMaxFicha()

            userUseCase.getUser()?.let {
                user = it
                try {
                    it.campaing?.let { camp ->
                        offerUseCase.getFicha(camp.toInt(), type, cuv, it.countryMoneySymbol,
                            it.billingStartDate, it.codigoPrograma, it.consecutivoNueva, it.montoMaximoPedido,
                            it.consultoraNueva, it.numberOfCampaings?.toInt() ?: 0, it.consultantName, it.regionCode, it.zoneCode,
                            it.codigoSeccion, it.isUltimoDiaFacturacion, it.isPagoContado, it.billingEndDate)?.let { offer ->

                            if(accessFrom == ACCESS_FROM_PASE_PEDIDO){
                                if(type==OfferTypes.ODD){
                                    val request = createOfferRequest(type!!)
                                    val ofertas = offerUseCase.ofertasNoCache(request)
                                    //festivalResponse = offerUseCase.getOffersFestival()

                                    var ofertaVencida = if (ofertas == null || ofertas.isEmpty())
                                        true
                                    else
                                        isOfertaVencida(cuv, ofertas)
                                    offer.apply {
                                        vencido = ofertaVencida
                                    }
                                }
                            }

                            val filterList = mutableListOf(SearchFilter().apply {
                                nombreGrupo = FestPresenter.CATEGORY_GROUP_NAME
                                opciones = mutableListOf(SearchFilterChild().apply {
                                    idFiltro = FestFragment.FEST_CATEGORY_CODE
                                    nombreFiltro = FestFragment.FEST_CATEGORY_NAME
                                })
                            })

                            offerUseCase.getOffersFestival(FestFragment.DEFAULT_ORDER, FestFragment.DEFAULT_ORDER_TYPE, filterList)?.let { festivalResponse ->

                                offerUseCase.getFestivalProgress(cuv)?.let { listFestivalProgress ->
                                    orderUseCase.getOrdersFormatted(GetFormattedOrdersObserver())

                                    GlobalScope.launch(Dispatchers.Main) {
                                        view?.setUser(it)
                                        view?.hideLoading()
                                        offer?.let {
                                            view?.load(offer)
                                            view?.setDataAward(listFestivalProgress, offer, festivalResponse)
                                        } ?: view?.showError(true)
                                    }
                                }?.run {
                                    GlobalScope.launch(Dispatchers.Main) {
                                        view?.setUser(it)
                                        view?.hideLoading()
                                        offer?.let {
                                            view?.load(offer)
                                            view?.setDataAward(null, null, null)
                                        } ?: view?.showError(true)
                                    }
                                }

                            }

                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.setUser(it)
                        view?.hideLoading()
                        view?.showError(true)
                        e.message?.let { msg ->
                            BelcorpLogger.d(msg)
                        }
                    }
                }
            }
        }
    }

    fun saveRecentOffer(oferta: Oferta, type: String, context: Context){
        GlobalScope.launch (Dispatchers.IO) {
                val session = SessionManager.getInstance(context)
                val countMax = session.getCountMaxRecentSearch()
                val esCatalogo = type == OfferTypes.CAT
                val ofertaLocal = SearchRecentOffer(oferta.cuv, oferta.nombreOferta, oferta.precioCatalogo, oferta.precioValorizado, oferta.imagenURL, type, oferta.flagFestival, false, oferta.agotado, esCatalogo)
                offerUseCase.saveRecentOffer(ofertaLocal, countMax)
        }
    }

    fun getDataPromotion(cuvPromocion: String?, cuvCondicion: String?) {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.getOfferPromotion(cuvPromocion, PromocionTypes.PROMOCION_PREMIO)
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    result?.let { res ->
                        res.listaApoyo = res.listaApoyo?.sortedByDescending { it?.cuv == cuvCondicion }
                        view?.load(PromotionOfferModel.transform(res))
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.d(e)
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    view?.showError(R.string.error_promotion_message)
                }
            }
        }
    }

    fun getOffersPromotions(cuv: String?, typeCarousel: Int, type: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.getOfferPromotion(cuv, type)
                GlobalScope.launch(Dispatchers.Main) {
                    view?.setCarouselOffers(arrayListOf(), typeCarousel, result)
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    BelcorpLogger.d(e)
                    view?.setCarouselOffers(arrayListOf(), typeCarousel)
                }
            }
        }
    }


    companion object {
        const val ANALYTICS_TAG = "Belcorp-Analytics"
    }

}
