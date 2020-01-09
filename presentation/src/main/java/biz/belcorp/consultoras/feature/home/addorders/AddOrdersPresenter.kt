package biz.belcorp.consultoras.feature.home.addorders

import android.content.Context
import android.util.Log
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper
import biz.belcorp.consultoras.common.model.error.DtoModelMapper
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlDataMapper
import biz.belcorp.consultoras.common.model.orders.*
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.common.model.product.ProductItemModelDataMapper
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.domain.util.PromocionTypes
import biz.belcorp.consultoras.exception.ErrorFactory
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasPresenter
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil.getIPAddress
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.design.counter.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import javax.inject.Inject

@PerActivity
class AddOrdersPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val orderUseCase: OrderUseCase,
    private val menuUseCase: MenuUseCase,
    private val premioUseCase: PremioUseCase,
    private val accountUseCase: AccountUseCase,
    private val loginModelDataMapper: LoginModelDataMapper,
    private val dtoModelMapper: DtoModelMapper,
    private val clientModelDataMapper: ClienteModelDataMapper,
    private val reserveResponseModelDataMapper: ReserveResponseModelDataMapper,
    private val pedidoConfigModelDataMapper: PedidoConfigModelDataMapper,
    private val orderModelDataMapper: OrderModelDataMapper,
    private val productItemModelDataMapper: ProductItemModelDataMapper,
    private val ofertaFinalModelDataMapper: OfertaFinalModelDataMapper,
    private val sessionUseCase: SessionUseCase,
    private val origenMarcacionUseCase: OrigenMarcacionUseCase,
    private val offerUseCase: OfferUseCase,
    private val festivalUseCase: FestivalUseCase,
    private val origenPedidoUseCase: OrigenPedidoUseCase)
    : Presenter<AddOrdersView>, SafeLet {

    private var addOrdersView: AddOrdersView? = null
    var hasGiftProlError: Boolean = false
    var dataUsuario: User? = null
    var newEmail: String? = null
    var configuracionPremio: ConfiguracionPremio? = null
    var updateMailOF: Boolean = false

    var flagOfertaFinal: Boolean = false

    private var orderResponse: BasicDto<FormattedOrderReserveResponse>?  = null
    private var onMontoIncentivo: Double? = null

    override fun attachView(view: AddOrdersView) {
        addOrdersView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.menuUseCase.dispose()
        this.accountUseCase.dispose()
        this.sessionUseCase.dispose()
        this.origenMarcacionUseCase.dispose()
        this.addOrdersView = null
    }

    /** functions */

    fun getPedidosPendientesStart() {
        GlobalScope.launch {
            try {

                val response = orderUseCase.getPedidosPendientes(dataUsuario?.campaing)

                response?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (it.pedidoPendiente != 0) {
                            addOrdersView?.showPedidosPendientesOnStart(it.pedidoPendiente)
                            addOrdersView?.showButtonPendingOrder(it.pedidoPendiente)
                            addOrdersView?.showAlertPendingOrders(it.pedidoPendiente)
                        } else {
                            addOrdersView?.hideButtonPendingOrder()
                        }
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    fun getPedidosPendientes(order: OrderModel?, flagOfertaFinal: Boolean) {
        addOrdersView?.showLoading()
        GlobalScope.launch {
            try {
                val response = orderUseCase.getPedidosPendientes(dataUsuario?.campaing)

                response?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (it.pedidoPendiente ?: 0 > 0) {
                            if (order?.isDiaProl ?: false) {
                                addOrdersView?.hideLoading()
                                addOrdersView?.showPedidosPendientesBloqueante(it.pedidoPendiente)
                            } else {
                                addOrdersView?.hideLoading()
                                addOrdersView?.showPedidosPendientesNoBloqueante(it.pedidoPendiente)
                            }
                        } else {
                            reservar(order, flagOfertaFinal)
                        }


                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    fun data() {
        addOrdersView?.showLoading()
        getUser()
        accountUseCase.getConfig(UserConfigAccountCode.DIGITAL_CATALOG, GetConfigPedidosPendientes())
        accountUseCase.getConfig(UserConfigAccountCode.CAMINO_BRILLANTE, GetCaminoBrillanteConfig())
        getConfigFest()
    }

    private fun getUser(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = userUseCase.getUser()

                user?.let {
                    launch(Dispatchers.Main) {
                        addOrdersView?.setData(user)
                    }

                    val documento = it.numeroDocumento?.let { nDoc ->
                        if (nDoc.isEmpty())
                            "0"
                        else
                            it.numeroDocumento
                    } ?: kotlin.run { "0" }

                    val request = ResumenRequest().apply {
                        campaing = Integer.parseInt(it.campaing?: StringUtil.Empty)
                        codeRegion = it.regionCode
                        codeSection = it.codigoSeccion
                        codeZone = it.zoneCode
                        idContenidoDetalle = 0
                        indConsulDig = (it.indicadorConsultoraDigital).toString()
                        numeroDocumento = documento
                        primerNombre = it.primerNombre
                        primerApellido = it.primerApellido
                        fechaNacimiento = it.fechaNacimiento
                        correo = it.email
                    }

                    val resumeConfig = accountUseCase.getResumenConfig(request)
                    val isShowDetail = find(GlobalConstant.TAG_PEDIDO_POR_TERCEROS, resumeConfig.data)

                    if (user.indicadorConsultoraDigital == 1 && isShowDetail != null) {
                        launch(Dispatchers.Main) {
                            addOrdersView?.showOptionReceiver()
                        }

                        val orderResponse = orderUseCase.getOrders()

                        orderResponse?.let {
                            launch(Dispatchers.Main) {
                                addOrdersView?.showDetailOptionReceiver(
                                    it.recogerDNI ?: StringUtil.Empty, it.recogerNombre ?: StringUtil.Empty)
                            }
                        }
                    }

                    val updateMail = sessionUseCase.isUpdateMail(it.consultantCode)
                    updateMailOF = updateMail && (dataUsuario?.actualizacionDatos == 1)

                    dataUsuario = it

                }
                orderUseCase.updateSchedule(BaseObserver())
            } catch (ex : Exception){
                BelcorpLogger.e(TAG, ex.message, ex)
                launch(Dispatchers.Main) {
                    addOrdersView?.hideLoading()
                }
            }
        }
    }

    private fun find(tag: String, listaResumen: Collection<ContenidoResumen>?): ContenidoResumen? {
        if (listaResumen != null) {
            for (item in listaResumen) {
                if (item.codigoResumen == tag) {
                    return item
                }
            }
        }
        return null
    }

    private inner class GetCaminoBrillanteConfig : BaseObserver<Collection<UserConfigData?>?>() {
        override fun onNext(t: Collection<UserConfigData?>?) {
            t?.let {
                onMontoIncentivo = it.filter { c -> c?.code == CaminoBrillanteType.FLAG_CB_MONTO_INCENTIVO }.firstOrNull()?.value1?.toDoubleOrNull()
                onMontoIncentivo?.let {
                    addOrdersView?.setMontoIncentivo(it)
                }
            }
        }
    }

    fun searchPopUpDuoPerfecto(){
        GlobalScope.launch(Dispatchers.IO) {
            val existe = accountUseCase.checkContenidoResumenIfExist(GlobalConstant.POPUP,GlobalConstant.DUOPERFECTO)
            GlobalScope.launch(Dispatchers.Main) {
                if(existe){
                    addOrdersView?.showPopUpDuoRecordatorio()
                }
            }
        }
    }
    fun clearMessagesPopUp() {
        addOrdersView?.showLoading()
        sessionUseCase.saveStatusGiftAnimation(true, GiftObserverSetStatus(false))
        sessionUseCase.saveStatusGiftToolTip(true, GiftObserverSetStatus(false))
    }

    fun getIsShowedAnimationGift() {
        sessionUseCase.getIsShowedAnimationGift(GiftGetObserver())
    }

    fun getIsShowedToolTipGift() {
        sessionUseCase.getIsShowedToolTipGift(GiftGetIsShowedToolTip())
    }

    fun setStatusIsShowingGiftAnimation(status: Boolean) {
        addOrdersView?.showLoading()
        sessionUseCase.saveStatusGiftAnimation(status, GiftObserverSetStatus(true))
    }

    fun setToolTipDeletedGift(status: Boolean) {
        sessionUseCase.saveStatusGiftToolTip(status, GiftObserverSetStatus(true))
    }

    fun kitInicio() {
        addOrdersView?.showLoading()
        orderUseCase.kitInicio(KitInicioObserver())
    }

    fun getKits(user: User) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            orderUseCase.kitSociaEmpresaria()
                        } catch (e: Exception) {
                            Log.v("COROUT", "ERROR")
                        }

                    }.join()
                    if (!user.esConsultoraOficina && ((user.consultoraNueva == 1 || user.consultoraNueva == 7)
                            || (user.consecutivoNueva == 1 || user.consecutivoNueva == 2))) {
                        kitInicio()
                    } else {
                        getOrder(CallOrderType.FROM_INIT)
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        addOrdersView?.hideLoading()
                    }
                }
            }
        } catch (ex: Exception) {

        }
    }

    fun getOrder(callFrom: Int) {
        addOrdersView?.showLoading()
        orderUseCase.getOrdersFormatted(GetFormattedOrdersObserver(callFrom))
    }

    fun config(callFrom: Int) {
        orderUseCase.config(ConfigObserver(callFrom))
        if (callFrom == CallOrderType.FROM_INIT) getOfertasRecomendadas()
    }

    fun deleteAllProduct(order: OrderModel?, context: Context?) {
        addOrdersView?.showLoading()

        order?.let {
            order.identifier = DeviceUtil.getId(context)
            orderUseCase.deleteProduct(orderModelDataMapper.transform(order)!!,
                OrderListItem().apply {
                    id = 0
                    cuv = ""
                    cantidad = 0
                    observacionPROL = ""
                }, OrderDeletedObserver())
        }
    }

    fun initTrack(tipoTrack: Int) {
        userUseCase[UserPropertyTrackObserver(tipoTrack)]
    }

    fun trackBackPressed() {
        userUseCase[UserBackPressedObserver()]
    }

    fun reservar(order: OrderModel?, flagOfertaFinal: Boolean) {
        addOrdersView?.showLoading()
        this.flagOfertaFinal = flagOfertaFinal
        order?.let {
            orderUseCase.reservaPedido(orderModelDataMapper.transform(order)!!, ReservaPedidoObserver())
        }
    }

    fun deleteItem(order: OrderModel?, item: ProductItem) {
        addOrdersView?.showLoading()
        order?.let {
            orderUseCase.deleteProduct(orderModelDataMapper.transform(it)!!,
                productItemModelDataMapper.transform(item)!!, OrderItemDeletedObserver())
        }
    }


    fun getOffers() {
          addOrdersView?.showLoading()
          orderUseCase.estrategiaCarrusel(GetOffersObserver())
    }

    private fun getOfertasRecomendadas() {
        addOrdersView?.showLoading()
        GlobalScope.launch {
            dataUsuario?.let { user ->
                user.campaing?.let { campaign ->
                    try {
                        val result = offerUseCase.getOfertasRecomendadas(campaign)
                        result?.let {
                            if (it.isNullOrEmpty())
                                getOffers()
                            else {
                                GlobalScope.launch(Dispatchers.Main) {
                                    result?.let {
                                        addOrdersView?.hideLoading()
                                        addOrdersView?.setOffersRecomended(it.filterNotNull())
                                    }
                                }
                            }
                        }
                    }catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            addOrdersView?.hideLoading()
                        }
                    }
                }
            }
        }
    }

    fun saveOfertaFinal(monto: Double) {
        GlobalScope.launch {
            offerUseCase.saveOfertaFinal(monto)
        }

    }


    fun getOfertaFinal() {
        GlobalScope.launch(Dispatchers.IO) {
            val hasOfertaFinal = offerUseCase.getOfertaFinal()
            val estadoPremio = offerUseCase.getEstadoPremioOfertaFinal()
            withContext(Dispatchers.Main) {
                addOrdersView?.mostrarExperiencia(hasOfertaFinal, estadoPremio)
            }
        }
    }

    fun getOfferTitle(code: String) {
        menuUseCase.get(code, GetOfferTitleObserver())
    }

    fun backOrder(cuv: String?, pedidoID: Int?, pedidoDetalleID: Int?, identifier: String?) {
        addOrdersView?.showLoading()
        orderUseCase.backOrder(cuv, pedidoID, pedidoDetalleID, identifier, BackOrderObserver())
    }

    fun getTooltipDuoType(substring: String): Int {
        return when (substring) {
            GlobalConstant.DUOPERFECTO_CODE_AGREGASTE -> {
                1
            }
            GlobalConstant.DUOPERFECTO_CODE_COMPLETASTE -> {
                2
            }
            else -> {
                4
            }
        }
    }

    fun updateConfigPedidosPendientes() {
        accountUseCase.getConfigActiveWithUpdate(UserConfigAccountCode.DIGITAL_CATALOG,
            GetConfigPedidosPendientes())
    }

    fun getMontoIncentivo() {
        accountUseCase.getConfigActiveWithUpdate(UserConfigAccountCode.CAMINO_BRILLANTE,
            GetCaminoBrillanteConfig())
    }

    fun showOffers() {
        userUseCase.checkGanaMasNativo(OffersObserver())
    }

    fun updateDni(isActivate: Boolean, dni: String = "", nameCollect: String = "") {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val pedidoID = orderUseCase.getOrders()?.pedidoID
                val campaing = userUseCase.getUser()?.campaing?.toInt()
                val ip = getIPAddress(true)

                val updateDni = UpdateDniRequest(campaing, pedidoID, dni, nameCollect, ip)
                val result = orderUseCase.updateDni(updateDni)
                GlobalScope.launch(Dispatchers.Main) {
                    if (isActivate) {
                        result?.let {
                            addOrdersView?.updateDniSuccessful()
                        }
                    } else {
                        addOrdersView?.hideLoading()
                        addOrdersView?.disabledReceiverOption()
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    addOrdersView?.hideLoading()
                }
                e.message?.let { msg ->
                    BelcorpLogger.d(msg)
                }
            }
        }
    }

    fun getNameListAnalytics(codigoUbicacion: String, codigoSeccion: String) {

        GlobalScope.launch {
            try {
                val nameList = origenMarcacionUseCase.getValorLista(codigoUbicacion, codigoSeccion)

                GlobalScope.launch(Dispatchers.Main) {
                    addOrdersView?.setNameListAnalytics(nameList)
                }
            } catch (e: Exception) {

            }
        }
    }

    fun getImageEnabled() {
        GlobalScope.launch {
            orderUseCase.getImageDialogEnabled()?.let {
                addOrdersView?.setImageEnabled(it)
            }
        }
    }

    fun agregar(oferta: EstrategiaCarrusel, quantity: Int, counterView: Counter, identifier: String,
                origenPedidoWebCarrusel: String, addedFromPopoutPromotion: Boolean? = false) {
        addOrdersView?.showLoading()
        val productCUV = ProductCUV().apply {
            cuv = oferta.cuv
            description = oferta.descripcionMarca
            descripcionMarca = oferta.descripcionMarca
            marcaId = oferta.marcaID
            precioCatalogo = oferta.precioFinal?.toDouble()
            precioValorizado = oferta.precioValorizado?.toDouble()
            fotoProducto = oferta.fotoProductoSmall
            origenPedidoWeb = origenPedidoWebCarrusel
            tipoEstrategiaId = oferta.tipoEstrategiaID
            codigoEstrategia = oferta.codigoEstrategia?.toInt()
            estrategiaId = oferta.estrategiaID
            indicadorMontoMinimo = oferta.indicadorMontoMinimo
            limiteVenta = 0
            cantidad = quantity
            flagPromocion = oferta.flagPromocion
            this.identifier = identifier
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = orderUseCase.insertarPedido(productCUV, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        addOrdersView?.hideLoading()
                        if (it.code == GlobalConstant.CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO) {
                            val codeAlert = when (it.code) {
                                GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> AddedAlertType.FESTIVAL
                                else -> AddedAlertType.DEFAULT
                            }
                            userUseCase.updateScheduler2()
                            addOrdersView?.onProductAdded(quantity, productCUV, it.message, codeAlert)
                            counterView.resetQuantity()

                            if ((addedFromPopoutPromotion ?: false)) {
                                getOrder(CallOrderType.FROM_ADD_ITEM)
                            }

                        } else {
                            addOrdersView?.onProductNotAdded(it.message)
                        }
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    addOrdersView?.hideLoading()
                    when (e) {
                        is NetworkErrorException -> addOrdersView?.showError(false)
                        else -> {
                            e.message?.let {
                                BelcorpLogger.d(it)
                                addOrdersView?.onProductNotAdded(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun agregar(oferta: Oferta, quantity: Int, counterView: Counter, identifier: String, palanca: String, codigo: String) {
        addOrdersView?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            val opw = origenPedidoUseCase.getValor(palanca, codigo)
            val productCUV = getAddRequest(oferta, opw.toString(), quantity, identifier)
            productCUV?.let { producto ->
                try {
                    val result = orderUseCase.insertarPedido(producto, identifier)
                    result?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            addOrdersView?.hideLoading()
                            if (it.code == GlobalConstant.CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO) {
                                val codeAlert = when (it.code) {
                                    GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> AddedAlertType.FESTIVAL
                                    else -> AddedAlertType.DEFAULT
                                }
                                userUseCase.updateScheduler2()
                                addOrdersView?.onProductAdded(quantity, producto, it.message, codeAlert)
                                counterView.resetQuantity()
                            } else {
                                addOrdersView?.onProductNotAdded(it.message)
                            }
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        when (e) {
                            is NetworkErrorException -> addOrdersView?.showError(false)
                            else -> {
                                addOrdersView?.hideLoading()
                                e.message?.let {
                                    BelcorpLogger.d(it)
                                    addOrdersView?.onProductNotAdded(it)
                                }
                            }
                        }
                    }
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

    fun updateConsultEmail(newEmail: String, dialog: Boolean) {
        this.newEmail = newEmail
        accountUseCase.enviarCorreo(newEmail, UpdateEmailObserver(dialog))
    }

    fun deshacerReserva() {
        addOrdersView?.showLoading()
        orderUseCase.deshacerReserva(DeshacerReservaObserver())
    }

    fun updateStateMultiOrder(state: Boolean) {
        addOrdersView?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val pedidoID = orderUseCase.getOrders()?.pedidoID ?: 0
                orderUseCase.updateState(UpdateMultipedidoState(pedidoID, state))
                GlobalScope.launch(Dispatchers.Main) {
                    if (state) {
                        addOrdersView?.showTooltipOn()
                    } else {
                        addOrdersView?.showTooltipOff()
                    }
                    addOrdersView?.hideLoading()
                }
            } catch (ex: Exception) {
                BelcorpLogger.e(ex)
                GlobalScope.launch(Dispatchers.Main) {
                    addOrdersView?.hideLoading()
                }
            }
        }
    }

    fun showMessageProl(respuestaProl : Collection<MensajeProl?>?){
        respuestaProl?.run {
            if(isNotEmpty()){
                var mensajesProlModel = MensajeProlDataMapper().transformToDomainModel(this)
                var cuvProducto : String? = StringUtil.Empty
                var mensajeProl : String = StringUtil.Empty
                var imageProl : String = StringUtil.Empty
                mensajesProlModel.forEach { mensaje ->
                    if( mensaje.code?.equals(PROL_MESSAGE_CODE) == false){
                        cuvProducto =  mensaje.code
                        imageProl = mensaje.image ?: StringUtil.Empty

                    }

                    mensajeProl += mensaje.message

                }

                cuvProducto?.let{ code ->

                    GlobalScope.launch(Dispatchers.IO) {

                        try{

                            orderUseCase.searchCUV(code)?.let{ producto ->
                                GlobalScope.launch(Dispatchers.Main){
                                when{

                                    producto.code ==  SearchCUVCode.OK ->{


                                        producto.data?.let { item ->
                                            addOrdersView?.onMensajeProl(imageProl,mensajeProl,item)
                                        }
                                    }

                                    producto.code == SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_ESIKA
                                        || producto.code == SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_LBEL
                                    -> {
                                        addOrdersView?.hideLoading()
                                        genericOrderResult()
                                    }
                                    producto.code == SearchCUVCode.ERROR_PRODUCTO_NOEXISTE -> {
                                        addOrdersView?.hideLoading()
                                        genericOrderResult()
                                    }
                                    producto.code == SearchCUVCode.ERROR_PRODUCTO_SUGERIDO ||
                                        producto.code == SearchCUVCode.ERROR_PRODUCTO_AGOTADO -> {
                                        producto.data?.let { d ->
                                            addOrdersView?.hideLoading()
                                            genericOrderResult()
                                        }
                                    }
                                    else -> {
                                        addOrdersView?.hideLoading()
                                        genericOrderResult()
                                    }


                                }
                                }


                            }?:run{
                                genericOrderResult()
                            }


                        }catch (ex : Exception){
                            BelcorpLogger.e(ex)
                        }
                    }

                }?:run{

                    genericOrderResult()
                }
            }
        }

    }

    fun getConfigFest() {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                userUseCase.getUser()?.let { user ->
                    user.campaing?.toInt()?.let { campaing ->
                        festivalUseCase.getConfiguracion(campaing)?.let { config ->
                            addOrdersView?.setConfigFest(config)
                        }
                    }
                }
            } catch (ex: Exception) {
                BelcorpLogger.e(ex)
            }
        }
    }

    fun insertarUnicoProducto(product: ProductCUV){

        orderUseCase.deshacerReserva(DeshacerReservaTombolaObserver(product))
    }

    fun insertHomologado(product: ProductCUV, identifier: String){

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result =  orderUseCase.insertarPedido(product, identifier,
                    false, -1, product.clienteId ?: 0)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        addOrdersView?.hideLoading()
                        when(it.code) {
                            SearchCUVCode.OK -> {
                                userUseCase.updateScheduler2()
                                addOrdersView?.onProductAdded(product.cantidad!!, product, it.message, AddedAlertType.DEFAULT)
                                kotlinx.coroutines.delay(2500)
                                addOrdersView?.reserveOrder()
                            }
                            else -> {
                                addOrdersView?.reserveOrder()
                            }
                        }
                    }
                }
            }catch (e: Exception){
                GlobalScope.launch(Dispatchers.Main) {
                    addOrdersView?.hideLoading()
                    when(e){
                        is VersionException -> addOrdersView?.onVersionError(e.isRequiredUpdate, e.url)
                        else -> {
                            addOrdersView?.reserveOrder()
                        }
                    }
                }

            }
        }
    }

    fun genericOrderResult(){
        orderResponse?.let { r ->
            if (r.code == GlobalConstant.CODE_OK) {
                addOrdersView?.onOrderReserved(reserveResponseModelDataMapper.transform(r.data?.reservaResponse), r.message
                    ?: StringUtil.Empty, flagOfertaFinal, configuracionPremio)
            } else {
                r.message?.let {
                    addOrdersView?.onOrderError(it, orderModelDataMapper.transform(r.data?.formattedOrder))
                }
            }
        }
    }

    /** observers */

    private inner class DeshacerReservaTombolaObserver(val product: ProductCUV) : BaseObserver<BasicDto<Boolean>?>() {
        override fun onNext(t: BasicDto<Boolean>?) {
            addOrdersView?.hideLoading()
            t?.let {
                if (it.code == GlobalConstant.CODE_OK) {
                    product.cantidad = 1
                    val identifier = DeviceUtil.getId(addOrdersView?.context())
                    product.identifier = identifier
                    product.clienteId = 0
                    product.clienteLocalId = 0

                    insertHomologado(product,identifier)

                } else {
                    genericOrderResult()
                }
            }

        }
    }

    private inner class DeshacerReservaObserver : BaseObserver<BasicDto<Boolean>?>() {
        override fun onNext(t: BasicDto<Boolean>?) {
            addOrdersView?.hideLoading()
            t?.let {
                if (it.code == GlobalConstant.CODE_OK) {
                    addOrdersView?.onUndoReserve()
                } else {
                    it.message?.let {
                        //addOrdersView?.d(it)
                    }
                }
            }

        }
    }

    private inner class OffersObserver : BaseObserver<Boolean?>() {
        override fun onNext(t: Boolean?) {
            addOrdersView?.showOffers(t ?: false)
        }
    }

    private inner class UpdateMailObserver : BaseObserver<Boolean>() {

        override fun onNext(updateMail: Boolean) {
            updateMailOF = updateMail && (dataUsuario?.actualizacionDatos == 1)
        }
    }

    private inner class GetConfigPedidosPendientes : BaseObserver<Collection<UserConfigData?>?>() {

        override fun onNext(t: Collection<UserConfigData?>?) {
            t?.let {
                if (it.isNotEmpty()) {
                    addOrdersView?.setPedidosPendientes(ArrayList(it))
                }
            }
        }
    }

    private inner class ConfigObserver(var callFrom: Int = 0) : BaseObserver<PedidoConfig?>() {

        override fun onNext(t: PedidoConfig?) {
            t?.let {
                addOrdersView?.showConfig(pedidoConfigModelDataMapper.transform(it))
                if (callFrom != CallOrderType.FROM_INIT) addOrdersView?.hideLoading()
            }

        }

        override fun onError(exception: Throwable) {
            super.onError(exception)

            if (addOrdersView == null) return

            addOrdersView?.hideLoading()
            if (exception is VersionException) {
                addOrdersView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                addOrdersView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    private inner class KitInicioObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {
            if (null != t) {
                if (t.code == KitInicioCode.OK || t.code == KitInicioCode.ERROR_ADD_KIT) {
                    orderUseCase.getOrdersFormatted(GetFormattedOrdersObserver())
                } else {
                    addOrdersView?.hideLoading()
                    addOrdersView?.onOrderError(dtoModelMapper.transform(t))
                }
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)

            if (addOrdersView == null) return

            addOrdersView?.hideLoading()
            if (exception is VersionException) {
                addOrdersView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                addOrdersView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    private inner class UserPropertyTrackObserver(private val tipoTrack: Int) : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                addOrdersView?.initTrack(loginModelDataMapper.transform(it), tipoTrack)
            }
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            addOrdersView?.trackBackPressed(loginModelDataMapper.transform(t))
        }
    }

    private inner class ReservaPedidoObserver : BaseObserver<BasicDto<FormattedOrderReserveResponse>?>() {
        override fun onNext(t: BasicDto<FormattedOrderReserveResponse>?) {
            addOrdersView?.hideLoading()

            t?.let {
                t.data?.let {
                    orderResponse = t
                    val respuestaProl = it.reservaResponse?.mensajesProl
                    showMessageProl(respuestaProl)

                    GlobalScope.launch(Dispatchers.IO) {
                        premioUseCase.getOfertaFinalConfiguracion(dataUsuario?.campaing, dataUsuario?.countryMoneySymbol)?.let { configuracionPremio ->
                            this@AddOrdersPresenter.configuracionPremio = configuracionPremio
                        }
                        withContext(Dispatchers.Main){
                            if(respuestaProl?.isEmpty() == true) {
                                t.let { r ->
                                    if (r.code == GlobalConstant.CODE_OK) {
                                        addOrdersView?.onOrderReserved(reserveResponseModelDataMapper.transform(r.data?.reservaResponse), r.message
                                            ?: StringUtil.Empty, flagOfertaFinal, configuracionPremio)
                                    } else {
                                        r.message?.let {
                                            addOrdersView?.onOrderError(it, orderModelDataMapper.transform(r.data?.formattedOrder))
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            } ?: run {
                addOrdersView?.hideLoading()
            }

        }

        override fun onError(exception: Throwable) {
            if (addOrdersView == null) return
            addOrdersView?.hideLoading()
            when (exception) {
                is VersionException -> {
                    addOrdersView?.onVersionError(exception.isRequiredUpdate, exception.url)
                }
                else -> addOrdersView?.onError(ErrorFactory.create(exception))
            }
        }

        fun responseFilter(data: ReservaResponse?): ReservaResponse {

            var res = ReservaResponse()

            if (null != data) {
                res = ReservaResponse().copy(data.reserva, data.codigoMensaje, data.informativas, data.montoEscala,
                    data.montoTotal, data.observaciones, data.mensajesProl)
                res.observaciones = res.observaciones?.filter { s ->
                    s?.caso != ObservationOrderCase.BACK_ORDER
                }
            }
            return res
        }
    }

    private inner class BackOrderObserver : BaseObserver<BasicDto<Boolean>?>() {
        override fun onNext(t: BasicDto<Boolean>?) {
            t?.let {
                t.code?.let {
                    if (it == GlobalConstant.CODE_OK) {
                        getOrder(CallOrderType.FROM_ADD_ITEM)
                    } else {
                        BelcorpLogger.d("Error", t.message)
                        addOrdersView?.hideLoading()
                        addOrdersView?.onOrderError(dtoModelMapper.transform(t))
                    }
                }
            } ?: run {

                addOrdersView?.hideLoading()
            }
        }

        override fun onError(exception: Throwable) {
            if (addOrdersView == null) return
            addOrdersView?.hideLoading()
            when (exception) {
                is VersionException -> {
                    addOrdersView?.onVersionError(exception.isRequiredUpdate, exception.url)
                }
                else -> addOrdersView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    open inner class GetFormattedOrdersObserver(var callFrom: Int = 0)
        : BaseObserver<FormattedOrder?>() {

        override fun onNext(t: FormattedOrder?) {
            userUseCase.getLogin(GetLogin(t, callFrom))
        }

        override fun onError(exception: Throwable) {
            if (addOrdersView == null) return
            when (exception) {
                is VersionException -> {
                    addOrdersView?.hideLoading()
                    addOrdersView?.onVersionError(exception.isRequiredUpdate, exception.url)
                }
                else -> addOrdersView?.onFormattedOrderReceived(null, null, CallOrderType.FROM_INIT)
            }
        }
    }

    private inner class GetLogin(var formattedOrder: FormattedOrder?, var callFrom: Int) : BaseObserver<Login?>() {
        override fun onNext(t: Login?) {

            if (t?.isMultipedido == true) {
                if (formattedOrder?.activaMultiPedido == true) {
                    val isFacturarFm = formattedOrder?.facturarPedidoFM ?: false
                    addOrdersView?.showOptionMultiOrderEnabled(isFacturarFm)
                    if (isFacturarFm) {
                        addOrdersView?.showTooltipOn()
                    } else {
                        addOrdersView?.showTooltipOff()
                    }
                } else {
                    addOrdersView?.showOptionMultiOrderDisabled()
                }
            }

            val orderModel = orderModelDataMapper.transform(formattedOrder)

            orderModel?.isPagoContado = formattedOrder?.pedidoValidado ?: false && t?.isPagoContado ?: false
            t?.montoMaximoDesviacion?.let {

                orderModel?.montoMaximoDesviacion = it.toBigDecimal()
            }
            addOrdersView?.onFormattedOrderReceived(orderModel,
                clientModelDataMapper.transform(formattedOrder?.clientesDetalle)
                , callFrom)
        }
    }

    private inner class OrderItemDeletedObserver : BaseObserver<BasicDto<Boolean>?>() {
        override fun onNext(t: BasicDto<Boolean>?) {
            t?.let {

                when (it.code) {
                    UpdateProductCode.OK -> {
                        initTrack(AddOrdersFragment.EVENT_TRACK_ELIMINAR_PRODUCTO)
                        getOrder(CallOrderType.FROM_DELETE_ITEM)
                    }
                    UpdateProductCode.ERROR_MONTO_MINIMO_RESERVADO -> {
                        addOrdersView?.onProductNotAdded(t.message)
                    }
                    UpdateProductCode.ERROR_CRITERIO_PREMIO_FESTIVAL -> {
                        addOrdersView?.onProductFestNotEliminated(t.message)
                    }
                    else -> {
                        when (it.code) {
                            UpdateProductCode.ERROR_MONTO_MINIMO_RESERVADO -> addOrdersView?.onProductNotAdded(t.message)
                            UpdateProductCode.ERROR_PRODUCTO_ASOCIADO -> addOrdersView?.onProductNotAdded(t.message)
                            // Preguntar que poner en el else
                        }
                    }
                }
                addOrdersView?.hideLoading()
            }
        }

        override fun onError(exception: Throwable) {
            addOrdersView?.hideLoading()
            if (addOrdersView == null) return
            when (exception) {
                is VersionException -> addOrdersView?.onVersionError(exception.isRequiredUpdate, exception.url)
                else -> addOrdersView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    private inner class OrderDeletedObserver : BaseObserver<BasicDto<Boolean>?>() {
        override fun onNext(t: BasicDto<Boolean>?) {
            addOrdersView?.hideLoading()
            t?.let {
                if (it.code == UpdateProductCode.OK) {
                    data()
                }
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)

            if (addOrdersView == null) return

            addOrdersView?.hideLoading()
            if (exception is VersionException) {
                addOrdersView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                addOrdersView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    private inner class GetOffersObserver : BaseObserver<Collection<EstrategiaCarrusel?>?>() {
        override fun onNext(t: Collection<EstrategiaCarrusel?>?) {
            addOrdersView?.hideLoading()
            addOrdersView?.onOffersReceived(t as ArrayList<EstrategiaCarrusel>)
        }

        override fun onError(exception: Throwable) {
            addOrdersView?.hideLoading()
            BelcorpLogger.d(TAG, "ERROR!", exception)
        }
    }

    private inner class GetOfferTitleObserver : BaseObserver<Menu?>() {
        override fun onNext(t: Menu?) {
            addOrdersView?.setOfferTitle(t?.descripcion!!)
        }
    }


    inner class GiftGetObserver : BaseObserver<Boolean>() {
        override fun onNext(t: Boolean) {
            addOrdersView?.getIsAnimatedShowed(t)

        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            addOrdersView?.getIsAnimatedShowed(false)
        }
    }

    inner class GiftGetIsShowedToolTip : BaseObserver<Boolean>() {
        override fun onNext(t: Boolean) {
            addOrdersView?.getIsShowedToolTip(t)
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            addOrdersView?.getIsShowedToolTip(false)
        }
    }

    inner class GiftObserverSetStatus(private var hide: Boolean) : BaseObserver<Boolean>() {


        override fun onNext(t: Boolean) {
            if (hide)
                addOrdersView?.hideLoading()
            super.onNext(t)
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            addOrdersView?.hideLoading()
        }

    }

    private inner class UpdateEmailObserver(val openDialog: Boolean) : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {

            if (t?.code.equals(GlobalConstant.CODE_OK)) {
                addOrdersView?.hideLoadingDialog()
                if (openDialog) {
                    sessionUseCase.saveStatusUpdateMail(dataUsuario?.consultantCode, object : BaseObserver<Boolean>() {})
                    updateMailOF = false
                    addOrdersView?.onEmailUpdated(newEmail)
                }
            } else {
                t?.message?.let {
                    addOrdersView?.hideLoadingDialog()
                    addOrdersView?.onError(it)
                    BelcorpLogger.e(it)
                }
            }

        }

        override fun onError(exception: Throwable) {

            exception?.message?.let {
                addOrdersView?.hideLoadingDialog()
                addOrdersView?.onError(it)
                BelcorpLogger.e(it)
            }
        }

    }

    fun getAnalytics(tipoPersonalizacion: String?, onAnalyticsLoaded: (descriptionPalanca: String) -> Unit) {

        GlobalScope.launch {

            try {
                var descriptionPalanca = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(tipoPersonalizacion))

                if (descriptionPalanca.isNullOrEmpty()) descriptionPalanca = GlobalConstant.NOT_AVAILABLE

                GlobalScope.launch(Dispatchers.Main) { onAnalyticsLoaded.invoke(descriptionPalanca) }

            } catch (e: Exception) {

                Log.w(GanaMasPresenter.ANALYTICS_TAG, e.message)

            }

        }

    }

    fun getAnalyticsCarouselOPM(list: List<EstrategiaCarrusel?>?, onAnalyticsLoaded: (listResult: List<EstrategiaCarrusel>) -> Unit) {
        val typePalanca = OfferTypes.OPT // RD, OPM se buscan como OPT en la tabla

        GlobalScope.launch {

            try {
                var descriptionPalanca = origenMarcacionUseCase.getValorPalanca(typePalanca)

                if (descriptionPalanca.isEmpty()) descriptionPalanca = GlobalConstant.NOT_AVAILABLE

                list?.forEach { it?.nombrePersonalizacion =  descriptionPalanca }

                GlobalScope.launch(Dispatchers.Main) {
                    onAnalyticsLoaded.invoke(list?.filterNotNull() ?: arrayListOf())
                }

            } catch (e: Exception) {

                Log.w(GanaMasPresenter.ANALYTICS_TAG, e.message)

            }

        }

    }

    fun saveEstadoPremio(estadoPremio: Int) {
        GlobalScope.launch {
            offerUseCase.updateEstadoPremioOfertaFinal(estadoPremio)
        }
    }

    fun loadPromotion(cuvPromocion: String?) {
        addOrdersView?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.getOfferPromotion(cuvPromocion, PromocionTypes.PROMOCION_PREMIO)
                GlobalScope.launch(Dispatchers.Main) {
                    addOrdersView?.hideLoading()
                    result?.let { res ->
                        addOrdersView?.loadPromotion(PromotionOfferModel.transform(res))
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.d(e)
                GlobalScope.launch(Dispatchers.Main) {
                    addOrdersView?.hideLoading()
                    //addOrdersView?.showError(false)
                    addOrdersView?.showError(R.string.error_promotion_message)
                }
            }
        }
    }

    companion object {
        private const val TAG = "AddOrdersPresenter"
        const val PROL_MESSAGE_CODE = "CX0"
    }

}
