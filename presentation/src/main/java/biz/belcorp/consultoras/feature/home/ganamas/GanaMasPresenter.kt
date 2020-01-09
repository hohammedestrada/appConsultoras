package biz.belcorp.consultoras.feature.home.ganamas

import android.util.Log
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.ganamas.ConfigABTestingBonificaciones
import biz.belcorp.consultoras.common.model.ganamas.ConfigFlagAbTesting
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.GanaMasNoConfigException
import biz.belcorp.consultoras.domain.exception.GanaMasNoOffersByCategoriesException
import biz.belcorp.consultoras.domain.exception.GanaMasNoOffersByFiltersException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.HV
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasErrorFragment
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.AddedAlertType
import biz.belcorp.consultoras.util.anotation.OfferTypeCode.KIT_NUEVA_CODE
import biz.belcorp.consultoras.util.anotation.RevistaDigitalType
import biz.belcorp.consultoras.util.extensions.parallel
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil.Empty
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel
import biz.belcorp.mobile.components.design.filter.model.FilterModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.system.measureTimeMillis


@PerActivity
class GanaMasPresenter @Inject
constructor(
    private val festivalUseCase: FestivalUseCase,
    private val orderUseCase: OrderUseCase,
    private val userUseCase: UserUseCase,
    private val offerUseCase: OfferUseCase,
    private val accountUseCase: AccountUseCase,
    private val origenPedidoUseCase: OrigenPedidoUseCase,
    private val origenMarcacionUseCase: OrigenMarcacionUseCase,
    private val loginModelDataMapper: LoginModelDataMapper) : Presenter<GanaMasView>, SafeLet {

    private val ORDERTYPE_ASC = "ASC"
    private val ORDERTYPE_DESC = "DESC"
    private val ORDER_DEFECTO = "ORDEN"
    private val ORDER_GANANCIA = "GANANCIA"
    private val ORDER_PRECIO = "PRECIO"

    val LAYOUT_ORDER = 1
    val LAYOUT_FILTER = 2

    private var ganaMasView: GanaMasView? = null
    private var user: User? = null
    var addFilterCat: Boolean = true

    override fun attachView(view: GanaMasView) {
        ganaMasView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.ganaMasView = null
        this.festivalUseCase.dispose()
        this.orderUseCase.dispose()
        this.userUseCase.dispose()
        this.offerUseCase.dispose()
        this.accountUseCase.dispose()
    }

    fun getUser() {
        GlobalScope.launch {
            userUseCase.getUser()?.let {
                user = it
                GlobalScope.launch(Dispatchers.Main) {
                    ganaMasView?.setUser(it)
                }
            }
        }
    }

    fun getImageEnabled() {
        GlobalScope.launch {
            orderUseCase.getImageDialogEnabled()?.let {
                ganaMasView?.setImageEnabled(it)
            }
        }
    }

    fun getFlagHideViewForTesting() {
        GlobalScope.launch {
            orderUseCase.getFlagForTesting()?.let {
                ganaMasView?.setFlatHideViewForABTesting(it)
                getUser()
            }
        }
    }

    fun getFlagExpandedSearchviewForTesting() {
        GlobalScope.launch {
            orderUseCase.getFlagExpandedSearchviewForTesting()?.let {
                ganaMasView?.setExpandedSearchviewForABTesting(it)
            }
        }
    }

    fun getFlagOrderConfigurableLever() {
        GlobalScope.launch {
            orderUseCase.getFlagOrderConfigurableLever()?.let {
                ganaMasView?.setFlagOrderConfigurableLever(it)
            }
        }
    }

    var flagABTestingBonificaciones: String? = null
    fun getABTestingBonificaciones() {
        GlobalScope.launch {
            orderUseCase.getABTestingBonificaciones()?.let {
                flagABTestingBonificaciones = it
            }
        }
    }

    private fun configuracionABTestingBonificaciones(): ConfigABTestingBonificaciones {
        if (!flagABTestingBonificaciones.isNullOrEmpty()) {
            val gson = Gson()
            val itemType = object : TypeToken<ConfigABTestingBonificaciones>() {}.type
            val itemListRemote = gson.fromJson<ConfigABTestingBonificaciones>(flagABTestingBonificaciones, itemType)

            return itemListRemote
        }
        return ConfigABTestingBonificaciones()
    }

    fun createOfferRequest(type: String): OfertaRequest {

        var codPrograma: String? = null
        var conNueva: Int? = null
        var monMaxPed: Double? = null
        var consNueva: Int? = null
        var nroCamp: Int? = null
        var nomCons: String? = null
        var codSecc: String? = null
        var ultDiaFac: Boolean? = null
        var pagCont: Boolean? = null
        var fechaFin: String? = null

        if (type == HV) {
            codPrograma = user?.codigoPrograma
            conNueva = user?.consecutivoNueva
            monMaxPed = user?.montoMaximoPedido
            consNueva = user?.consultoraNueva
            nroCamp = user?.numberOfCampaings?.toInt()
            nomCons = user?.consultantName
            codSecc = user?.codigoSeccion
            ultDiaFac = user?.isUltimoDiaFacturacion
            pagCont = user?.isPagoContado
            fechaFin = user?.billingEndDate

        }

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
            flagFestival = false
            tipo = type
            codigoPrograma = codPrograma
            consecutivoNueva = conNueva
            montoMaximoPedido = monMaxPed
            consultoraNueva = consNueva
            nroCampanias = nroCamp
            nombreConsultora = nomCons
            codigoSeccion = codSecc
            esUltimoDiaFacturacion = ultDiaFac
            pagoContado = pagCont
            fechaFinFacturacion = fechaFin

        }
    }


    /**
     * Obtiene la lista de ordenamientos
     **/

    fun getOrders() {
        GlobalScope.launch {
            try {
                val result = offerUseCase.ordenamientos()
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        //ganaMasView?.hideLoading()
                        if (it.isNotEmpty()) {
                            ganaMasView?.setOrdenamientos(it)
                        }
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    ganaMasView?.setFilterOrderLabel(false, LAYOUT_ORDER)
                }
            }
        }
    }


    fun getFestivalConfiguracion() {


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = festivalUseCase.getLocalConfiguration()
                GlobalScope.launch(Dispatchers.Main) {
                    // view?.hideLoading()
                    result?.let {
                        ganaMasView?.setFestivalConfiguracion(result)
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                }
            }
        }
    }

    /**
     *  1. Obtiene los productos de cada oferta
     */
    fun getOffers(config: List<ConfiguracionPorPalanca>?) {
        ganaMasView?.hideLoading()

        GlobalScope.launch {
            val time = measureTimeMillis {
                val filters = config ?: listOf()

                filters.parallel {
                    if (it.tipoOferta == null || it.tipoOferta.isNullOrEmpty()) return@parallel

                    // No necesita obtener ofertas para palancas con Banner
                    when (it.tipoOferta) {
                        OfferTypes.PN, OfferTypes.DP, OfferTypes.ATP -> return@parallel
                        else -> BelcorpLogger.i("Esta Palanca no tiene un Banner")
                    }

                    it.tipoOferta?.let { type ->

                        val request = createOfferRequest(type)

                        try {

                            val ofertas: List<Oferta?>? = if (type == HV)
                                offerUseCase.ofertasNoCache(request)
                            else
                                offerUseCase.ofertas(request)

                            if (ofertas == null || ofertas.isEmpty())
                                GlobalScope.launch(Dispatchers.Main) { ganaMasView?.removeOffer(type) }
                            else {
                                ofertas.filterNotNull().forEach { offer -> offer.tipoOferta = it.tipoOferta }
                                GlobalScope.launch(Dispatchers.Main) {
                                    ganaMasView?.setOffers(type, ofertas)
                                }
                            }
                        } catch (e: Exception) {
                            GlobalScope.launch(Dispatchers.Main) {
                                when (e) {
                                    // Añadir un mensaje cuando viene por esta excepcion
                                    is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                    else -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                }
                                ganaMasView?.removeOffer(type)
                                BelcorpLogger.d(e.message)
                            }
                        }

                    }
                }
            }

            BelcorpLogger.i("Time x get offers $time")
        }
    }

    /**
     *  1. Obtiene las categories de las ofertas
     */

    fun getCategories() {
        user?.let {
            ganaMasView?.showLoading()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val result = offerUseCase.categorias()
                    var isCountryGanaMas = false
                    accountUseCase.getCodePaisGanaMas()?.let {
                        if (it == COUNTRY_GANAMAS)
                            isCountryGanaMas = true
                    }

                    result?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            if (it.isNotEmpty())
                                ganaMasView?.setCategories(it, isCountryGanaMas)
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        when (e) {
                            // Debe mostrar error aqui?
                            //is GanaMasNoCategoriesException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CATEGORY)
                            //is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                            //else -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                        }
                        ganaMasView?.hideLoading()
                        e.message?.let { BelcorpLogger.d(it) }
                    }
                }
            }
        }
    }

    /**
     *  1. Obtiene las filtros de las ofertas
     */
    fun getFiltros(conHijos: Boolean) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val result = offerUseCase.filtros(conHijos)
                    result?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            if (it.isNotEmpty())
                                ganaMasView?.setFilters(it)
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        ganaMasView?.hideFilters()
                        e.message?.let { BelcorpLogger.d(it) }
                    }
                }
            }
        }
    }

    /**
     *  1. Obtiene las ofertas por categoria
     */
    fun getOfferByCategory(categoryCode: String, categoryName: String, order: String, orderType: String) {
        user?.let {
            ganaMasView?.showLoading()
            GlobalScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    try {
                        val result = offerUseCase.ofertasXCategoria(order, orderType, createSearchByProductGanaMas(categoryCode, categoryName))
                        GlobalScope.launch(Dispatchers.Main) {
                            ganaMasView?.hideLoading()
                            ganaMasView?.setOffersBySearch(result)
                        }
                    } catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            when (e) {
                                is GanaMasNoOffersByCategoriesException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CATEGORY)
                                is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                else -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                            }
                            ganaMasView?.hideLoading()
                            BelcorpLogger.d(e)
                            e.message?.let { BelcorpLogger.d(it) }
                        }
                    }
                }
                BelcorpLogger.i("Time x get offers by accordion $time")
            }
        }
    }

    /**
     *  1. Obtiene las ofertas por filtros(Marcas y Precios)
     */
    fun getOfferByFilter(categoryCode: String, categoryName: String, order: String, orderType: String, groups: ArrayList<CategoryFilterModel>?) {
        user?.let {
            ganaMasView?.showLoading()
            GlobalScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    try {
                        val filterList = createSearchByProductGanaMas(categoryCode, categoryName)
                        groups?.filter {
                            it.list.any {
                                it
                                it.checked
                            }
                        }?.forEach {
                            filterList.add(SearchFilter().apply {
                                nombreGrupo = it.key
                                opciones = (it.list as List<FilterModel>).filter { it.checked }.map {
                                    SearchFilterChild().apply {
                                        idFiltro = it.codigo
                                        min = it.minValue.toInt()
                                        max = it.maxValue.toInt()
                                        nombreFiltro = it.name
                                        idSeccion = it.idSeccion
                                    }
                                }
                            })
                        }

                        val result = offerUseCase.ofertasXFiltros(order, orderType, filterList)
                        GlobalScope.launch(Dispatchers.Main) {
                            ganaMasView?.hideLoading()
                            ganaMasView?.setOffersBySearch(result)
                        }
                    } catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            when (e) {
                                is GanaMasNoOffersByFiltersException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_FILTER)
                                is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                else -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                            }
                            ganaMasView?.hideLoading()
                            BelcorpLogger.d(e)
                            e.message?.let { BelcorpLogger.d(it) }
                        }
                    }
                }
                BelcorpLogger.i("Time x get offers by filter $time")
            }
        }
    }

    /**
     *  1. Obtiene las ofertas de GanaMas en el Buscador
     */
    private fun createSearchByProductGanaMas(categoryCode: String, categoryName: String): MutableList<SearchFilter> {
        val list = mutableListOf(SearchFilter().apply {
            nombreGrupo = "Secciones"
            opciones = mutableListOf(SearchFilterChild().apply {
                idFiltro = "sec-gana"
                nombreFiltro = "Gana+ / Ofertas"
            })
        })

        if (addFilterCat) {
            list.add(SearchFilter().apply {
                nombreGrupo = "Categorías"
                opciones = mutableListOf(SearchFilterChild().apply {
                    idFiltro = categoryCode
                    nombreFiltro = categoryName
                })
            })
        }

        return list
    }

    /**
     * Obtiene ofertas por palanca
     */

    fun getOffersByLever(config: ConfiguracionPorPalanca, typeLever: String) {
        ganaMasView?.showLoading()
        GlobalScope.launch {

            val request = createOfferRequest(typeLever)
            try {

                val configBonificacion = this@GanaMasPresenter.configuracionABTestingBonificaciones()
                request.variantea = configBonificacion.variantea
                request.varianteb = configBonificacion.varianteb
                request.variantec = configBonificacion.variantec

                val result = offerUseCase.ofertasXPalanca(config, request, typeLever == HV)

                result?.filter { it?.flagBonificacion ?: false }?.forEach {
                //result?.forEach {
                    //it?.flagBonificacion = true
                    it?.varianteA = configBonificacion.variantea
                    it?.varianteB = configBonificacion.varianteb
                    it?.varianteC = configBonificacion.variantec
                }

                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        result.filterNotNull().forEach { offer -> offer.tipoOferta = config.tipoOferta }
                        ganaMasView?.hideLoading()
                        ganaMasView?.onOffersByLeverResponse(it, typeLever)
                        ganaMasView?.setBannerSubcampaniaPalanca(typeLever)
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    when (e) {
                        // Añadir un mensaje cuando viene por esta excepcion
                        is GanaMasNoOffersByCategoriesException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CATEGORY)
                        is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                        else -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                    }
                    ganaMasView?.hideLoading()
                    e.message?.let { BelcorpLogger.d(it) }
                }
            }
        }
    }


    fun setOrderOffer(resultResponse: List<Oferta?>?, order: String, orderType: String): List<Oferta?>? {
        //var result:List<Oferta?>? =resultResponse
        if (!(orderType.equals(ORDERTYPE_ASC) && order.equals(ORDER_DEFECTO))) {
            when (orderType) {
                ORDERTYPE_ASC -> {
                    return resultResponse?.sortedWith(compareBy {
                        when (order) {
                            ORDER_GANANCIA -> {
                                it?.ganancia
                            }
                            ORDER_PRECIO -> {
                                it?.precioCatalogo
                            }
                            else -> {
                                it?.nombreOferta
                            }
                        }
                    })
                }
                ORDERTYPE_DESC -> {
                    return resultResponse?.sortedWith(compareByDescending {
                        when (order) {
                            ORDER_GANANCIA -> {
                                it?.ganancia
                            }
                            ORDER_PRECIO -> {
                                it?.precioCatalogo
                            }
                            else -> {
                                it?.nombreOferta
                            }
                        }
                    })
                }
                else -> {
                }
            }
        }
        return resultResponse
    }


    /**
     *  1. Obtiene las palancas que cuentan con ofertas
     *  2. Obtiene la cofniguracion de cada palanca
     */

    fun getConfiguracion(internetConexion: Boolean) {
        user?.let {
            ganaMasView?.showLoading()
            GlobalScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    try {
                        it.campaing?.let { campaing ->
                            var result: List<ConfiguracionPorPalanca?>? = null

                            val time = measureTimeMillis {

                                result = offerUseCase.configuracion(it.diaFacturacion, campaing.toInt(), it.consecutivoNueva, it.codigoPrograma, it.regionCode, it.zoneCode, it.zoneID?.toInt(), it.countryMoneySymbol, it.isRDEsSuscrita, it.isRDEsActiva)
                            }

                            BelcorpLogger.i("Time x servicio transcurrido $time")

                            result?.let {

                                var flagATP: Boolean? = false
                                if (internetConexion == true) {
                                    it.forEach { item ->
                                        if (item?.tipoOferta == OfferTypes.ATP) {
                                            val orderResponse = orderUseCase.getOrders()
                                            flagATP = orderResponse?.isTieneArmaTuPack
                                        }
                                    }
                                }
                                GlobalScope.launch(Dispatchers.Main) {
                                    ganaMasView?.setConfig(it, flagATP)

                                    val configuracionSubcampania: SubCampaniaConfiguracion? = it.firstOrNull { configuracion ->
                                        configuracion?.tipoOferta == OfferTypes.SR
                                    }?.subCampaniaConfiguracion

                                    configuracionSubcampania?.let { it2 ->

                                        if (!it2.bannerTextoTitulo.isNullOrEmpty())
                                            ganaMasView?.setSubCampaniaConfiguracion(it2)

                                    }

                                }
                            }
                        }
                    } catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            when (e) {
                                // Añadir un mensaje cuando viene por esta excepcion
                                is GanaMasNoConfigException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
                                is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                is IOException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                else -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                            }
                            ganaMasView?.hideLoading()
                            e.message?.let { BelcorpLogger.d(it) }
                        }
                    }
                }

                BelcorpLogger.i("Time x config transcurrido $time")
            }
        }
    }

    /**
     * Agrega una oferta recibiendo un objeto Oferta como entrada
     */
    fun agregar(oferta: Oferta, quantity: Int, counterView: Counter, identifier: String, origenPedidoWebCarrusel: String) {
        ganaMasView?.showLoading()

        val productCUV = getAddRequest(oferta, origenPedidoWebCarrusel, quantity, identifier)

        productCUV?.let { producto ->

            GlobalScope.launch(Dispatchers.IO) {
                try {

                    val result = orderUseCase.insertarPedido(producto, identifier)
                    result?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            ganaMasView?.hideLoading()
                            if (it.code == GlobalConstant.CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO) {
                                val codeAlert = when (it.code) {
                                    GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> AddedAlertType.FESTIVAL
                                    else -> AddedAlertType.DEFAULT
                                }
                                userUseCase.updateScheduler2()
                                ganaMasView?.onOfferAdded(quantity, producto, it.message, codeAlert,
                                    oferta.codigoTipoOferta == KIT_NUEVA_CODE)
                                counterView.resetQuantity()
                            } else {
                                ganaMasView?.onOfferNotAdded(it.message)
                            }
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        when (e) {
                            is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                            else -> {
                                ganaMasView?.hideLoading()
                                e.message?.let {
                                    BelcorpLogger.d(it)
                                    ganaMasView?.onOfferNotAdded(it)
                                }
                            }
                        }
                    }
                    // Funcion en caso se de un error
                }
            }
        }
    }

    /**
     * Agrega una oferta recibiendo un objeto ProductoCUV como entrada
     */
    fun agregar(oferta: ProductCUV, counterView: Counter, identifier: String, origenPedidoWebCarrusel: String) {
        ganaMasView?.showLoading()

        val productCUV = getAddRequest(oferta, oferta.origenPedidoWeb, counterView.quantity, identifier)


        productCUV?.let { producto ->

            GlobalScope.launch(Dispatchers.IO) {
                try {

                    var valorOrigen = ""
                    var palanca = oferta.tipoPersonalizacion

                    if (oferta.isMaterialGanancia != null) {

                        if (oferta.tipoPersonalizacion == OfferTypes.OPM) {

                            palanca = when (oferta.isMaterialGanancia) {
                                false -> {
                                    OfferTypes.RD
                                }
                                true -> {
                                    OfferTypes.MG
                                }
                                else -> oferta.tipoPersonalizacion
                            }
                        }
                    }

                    producto.apply {
                        tipoPersonalizacion = palanca
                    }

                    if (origenPedidoWebCarrusel.isNotEmpty()) {
                        producto?.tipoPersonalizacion?.let {
                            valorOrigen = origenPedidoUseCase.getValor(it, origenPedidoWebCarrusel).toString()
                        }
                    } else {
                        valorOrigen = origenPedidoWebCarrusel
                    }


                    producto.apply {
                        origenPedidoWeb = valorOrigen
                    }


                    oferta.indicadorMontoMinimo = 1
                    val result = orderUseCase.insertarPedido(producto, identifier)
                    result?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            ganaMasView?.hideLoading()
                            if (it.code == GlobalConstant.CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO) {
                                val codeAlert = when (it.code) {
                                    GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> AddedAlertType.FESTIVAL
                                    else -> AddedAlertType.DEFAULT
                                }
                                counterView.resetQuantity()
                                producto.cantidad?.let { it2 ->
                                    userUseCase.updateScheduler2()
                                    ganaMasView?.onOfferAdded(it2, producto, it.message, codeAlert)
                                    counterView.resetQuantity()
                                }
                            } else {
                                ganaMasView?.onOfferNotAdded(it.message)
                            }
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        when (e) {
                            is NetworkErrorException -> ganaMasView?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                            else -> {
                                ganaMasView?.hideLoading()
                                e.message?.let {
                                    BelcorpLogger.d(it)
                                    ganaMasView?.onOfferNotAdded(it)
                                }
                            }
                        }
                    }
                    // Funcion en caso se de un error
                }
            }
        }
    }

    private fun getAddRequest(oferta: Any, origenPedidoWebCarrusel: String?, quantity: Int, id: String): ProductCUV? {
        if (oferta is Oferta)
            return ProductCUV().apply {
                cuv = oferta.cuv
                description = oferta.nombreOferta
                descripcionMarca = oferta.nombreMarca
                marcaId = oferta.marcaID
                precioCatalogo = oferta.precioCatalogo
                precioValorizado = oferta.precioValorizado
                fotoProducto = oferta.imagenURL
                origenPedidoWeb = origenPedidoWebCarrusel
                tipoEstrategiaId = oferta.tipoEstrategiaID
                codigoEstrategia = oferta.codigoEstrategia?.toInt()
                estrategiaId = oferta.estrategiaID
                indicadorMontoMinimo = oferta.indicadorMontoMinimo
                limiteVenta = oferta.limiteVenta
                cantidad = quantity
                identifier = id
                isSugerido = false
                clienteId = 0
                tipoPersonalizacion = oferta.tipoOferta
                pedido = oferta.pedido
            }
        else if (oferta is ProductCUV)
            return ProductCUV().apply {
                cuv = oferta.cuv
                description = oferta.description
                descripcionMarca = oferta.descripcionMarca
                marcaId = oferta.marcaId
                precioCatalogo = oferta.precioCatalogo
                precioValorizado = oferta.precioValorizado
                fotoProducto = oferta.fotoProductoSmall
                tipoEstrategiaId = oferta.tipoEstrategiaId
                codigoEstrategia = oferta.codigoEstrategia
                estrategiaId = oferta.estrategiaId
                indicadorMontoMinimo = oferta.indicadorMontoMinimo
                limiteVenta = oferta.limiteVenta
                cantidad = quantity
                identifier = id
                isSugerido = false
                clienteId = 0
                tipoPersonalizacion = oferta.tipoPersonalizacion
            }
        return null
    }

    /**
     * Screen track
     */

    internal fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            ganaMasView?.initScreenTrack(loginModelDataMapper.transform(t))
        }
    }

    /**
     * Refresh data
     */

    fun refreshData() {
        accountUseCase.refreshData2(RefreshDataObserver(), true)
    }

    private inner class RefreshDataObserver : BaseObserver<Boolean?>() {
        override fun onNext(t: Boolean?) {
            ganaMasView?.hideLoading()
            t?.let {
                if (t) {
                    ganaMasView?.restartFragment()
                }
            }
        }

        override fun onError(exception: Throwable) {
            ganaMasView?.hideLoading()
            super.onError(exception)
        }
    }

    fun getAnalytics(currentLocation: String?, currentSection: String?, sectionType: String?,
                     subsection: String?, originLocation: String?, originSection: String?,
                     onAnalyticsLoaded: (analytics: Analytics) -> Unit) {

        GlobalScope.launch {

            try {

                val analytics = Analytics()

                val list = origenMarcacionUseCase.getValorLista(currentLocation
                    ?: Empty, currentSection ?: Empty)

                if (list.isEmpty())
                    analytics.list = GlobalConstant.NOT_AVAILABLE
                else
                    analytics.list = list

                analytics.dimension16 = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(sectionType))

                val subSect = origenMarcacionUseCase.getValorSubseccion(subsection ?: Empty)

                if (subSect.isEmpty())
                    analytics.dimension17 = GlobalConstant.SUBSECCION_NULL
                else
                    analytics.dimension17 = subSect

                val dimension18 = origenMarcacionUseCase.getValorLista(originLocation
                    ?: Empty, originSection ?: Empty)

                if (dimension18.isEmpty())
                    analytics.dimension18 = GlobalConstant.NOT_AVAILABLE
                else
                    analytics.dimension18 = dimension18

                GlobalScope.launch(Dispatchers.Main) { onAnalyticsLoaded.invoke(analytics) }

            } catch (e: Exception) {

                Log.w(ANALYTICS_TAG, e.message)

            }

        }

    }

    companion object {

        const val ANALYTICS_TAG = "Belcorp-Analytics"
        const val COUNTRY_GANAMAS = "RD"

    }

}
