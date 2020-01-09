package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.data.entity.ProductoMasivo
import biz.belcorp.consultoras.domain.OrigenTipoCodigo
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.entity.campaignInformation.InfoCampaign
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.*
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

/**
 *
 */
class OrderUseCase
/**
 * Constructor del caso de uso
 *
 * @param orderRepository     Repository
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 * @param authRepository      Session Repository
 */
@Inject
constructor(private val orderRepository: OrderRepository
            , private val authRepository: AuthRepository
            , private val sessionRepository: SessionRepository
            , private val userRepository: UserRepository
            , private val caminoBrillanteRepository: CaminoBrillanteRepository
            , private val origenPedidoRepository: OrigenPedidoRepository
            , threadExecutor: ThreadExecutor
            , postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Obtener mis pedidos.
     */
    fun get(campaign: String?, pdfActive: Boolean, observer: DisposableObserver<Collection<MyOrder?>?>) {
        execute(this.orderRepository.get(campaign, pdfActive)
            .onErrorResumeNext(askForExceptions(campaign, pdfActive)), observer)
    }

    /**
     * Obtener mis pedidos.
     */
    fun getOrders(campaign: String?, pdfActive: Boolean, observer: DisposableObserver<Collection<MyOrder?>?>) {
        execute(this.orderRepository.get(campaign, pdfActive)
            .onErrorResumeNext(askForExceptions(campaign, pdfActive)), observer)
    }

    suspend fun getOrders(): PedidoGetResponse? {
        var user = userRepository.getWithCoroutines()
        return user?.let {

            var nivelCaminoBrillante = caminoBrillanteRepository.getResumenConsultora()?.nivel?.toIntOrNull()
                ?: 0

            orderRepository.getOrders(user.campaing?.toInt()
                , user.codigoPrograma
                , user.consecutivoNueva
                , user.montoMaximoPedido
                , user.consultoraNueva
                , user.numberOfCampaings?.toInt()
                , nivelCaminoBrillante
                , user.primerNombre
                , user.regionCode
                , user.zoneCode
                , user.codigoSeccion)
        }
    }

    /**
     * Obtener pdf.
     */
    fun paqueteDocumentario(numeroPedido: String?, observer: BaseObserver<BasicDto<String>?>) {
        execute(this.orderRepository.paqueteDocumentario(numeroPedido), observer)
    }

    fun updateSchedule(observer: DisposableObserver<Boolean>) {
        execute(this.sessionRepository.updateSchedule(), observer)
    }

    /**
     * Agrega un pedido.
     */
    fun addOrder(order: Order, observer: BaseObserver<BasicDto<Boolean>?>) {
        val onlineObservable = orderRepository.addOrder(order)
        execute(onlineObservable.onErrorResumeNext(askForAddOrderException(order)), observer)
    }

    /**
     * Inserta un pedido con corrutinas.
     */
    suspend fun insertarPedido(productCUV: ProductCUV, ip: String,
                               editable: Boolean = false, id: Int = 0, clientID: Int = 0, reemplazarFestival: Boolean? = false
                               , origen: OrigenTipoCodigo? = null)
        : BasicDto<Collection<MensajeProl?>?>? {

        var user = userRepository.getWithCoroutines()
        val consultora = caminoBrillanteRepository.getResumenConsultora()
        var codigoOrigenPedido: Int? = 0

        origen?.let {
            codigoOrigenPedido = origenPedidoRepository.getValor(it.tipo ?: "", it.codigo ?: "")
                ?: 0
        }

        return user?.let {
            orderRepository.insertarPedido(PedidoInsertRequest().apply {
                this.aceptacionConsultoraDA = it.consultantAcceptDA
                this.campaniaId = it.campaing?.toInt()
                this.cantidad = productCUV?.cantidad
                this.clienteDescripcion = it.consultantName
                this.campaniaId = it.campaing?.toInt()
                this.codigoPrograma = it.codigoPrograma
                this.codigosConcursos = it.codigosConcursos
                this.consecutivoNueva = it.consecutivoNueva
                this.consultoraNueva = it.consultoraNueva
                this.fechaFinFacturacion = it.billingEndDate
                this.fechaInicioFacturacion = it.billingStartDate
                this.origenPedidoWebCarrusel = if (origen == null) (productCUV.origenPedidoWeb?.toIntOrNull()
                    ?: 0) else codigoOrigenPedido
                this.identifier = productCUV?.identifier
                this.ipUsuario = ip
                this.montoMaximoPedido = it.montoMaximoPedido
                this.producto = productCUV
                this.nivelCaminoBrillante = consultora?.nivel?.toIntOrNull() ?: 0

                // Cambio para Pedido Reservado
                this.segmentoInternoID = user.segmentoInternoID
                this.montoMinimoPedido = user.montoMinimoPedido
                this.isValidacionAbierta = user.isValidacionAbierta
                this.isZonaValida = user.isZonaValida
                this.isValidacionInteractiva = user.isValidacionInteractiva
                this.isDiaProl = user.isDayProl
                this.codigoZona = user.zoneCode
                this.codigoRegion = user.regionCode
                this.isUsuarioPrueba = user.isUserTest
                this.simbolo = user.countryMoneySymbol

                this.isSugerido = false
                this.clienteID = clientID

                // Cambio para Modificar con Ficha Resumida
                this.isEditable = editable
                this.orderID = id

            })
        }
    }

    suspend fun getImageDialogEnabled(): Boolean {
        return sessionRepository.imageDialogEnabled()
    }

    suspend fun getFlagForTesting(): Boolean{
        return sessionRepository.flagForTesting()
    }
  
    suspend fun getFlagExpandedSearchviewForTesting(): Boolean{
        return sessionRepository.flagExpandedSearchviewForTesting()
    }
      
    suspend fun getFlagOrderConfigurableLever(): String{
        return sessionRepository.flagOrderConfigurableLever()
    }

    suspend fun getABTestingBonificaciones(): String{
        return sessionRepository.getABTestingBonificaciones()
    }

    suspend fun getImagesMaxFicha(): Long? {
        return sessionRepository.imagesMaxFicha()
    }

    /**
     * Obtiene el Flag Habilita el carrusel de promociones en el desplegable del buscador
     */
    fun getPromotionGroupList(): Boolean? {
        return sessionRepository.promotionGroupListEnabled()
    }

    /*suspend fun getPromotionGroupListEnable(): Boolean? {
        return sessionRepository.promotionGroupListEnabled()
    }*/

    /**z
     * Remover la reserva de un pedido.
     */
    fun removeReservation(order: Order, observer: BaseObserver<BasicDto<Boolean>?>) {
        val onlineObservable = orderRepository.undoReservation(order)
        execute(onlineObservable.onErrorResumeNext(askForUndoReservationException(order)), observer)
    }

    fun updateProduct(pedidoID: Int, identifier: String, orderListItem: OrderListItem, observer: DisposableObserver<BasicDto<Collection<MensajeProl?>?>?>) {
        val updateProductObserver = orderRepository.updateProduct(pedidoID, identifier, orderListItem)
            .flatMap { b -> sessionRepository.updateSchedule().map { b } }
        execute(updateProductObserver.onErrorResumeNext(
            askforUpdateProductException(pedidoID, identifier, orderListItem))
            , observer)
    }

    fun deleteProduct(order: FormattedOrder, orderListItem: OrderListItem, observer: BaseObserver<BasicDto<Boolean>?>) {
        val deleteProductObserver = orderRepository.deleteProduct(order, orderListItem)
            .flatMap { b -> sessionRepository.updateSchedule().map { b } }
        execute(deleteProductObserver.onErrorResumeNext(
            askforDeleteProductException(order, orderListItem))
            , observer)
    }

    fun insercionMasivaPedido(productos: List<ProductoMasivo>, identifier: String, observer
    : BaseObserver<BasicDto<List<ProductoMasivo>>?>) {
        val onlineObservable = orderRepository.insercionMasivaPedido(productos, identifier)
        execute(onlineObservable.onErrorResumeNext(askForAddInsercionMasivaException(productos, identifier)), observer)
    }

    fun reservaPedido(order: FormattedOrder, observer: BaseObserver<BasicDto<FormattedOrderReserveResponse>?>) {
        val reservaPedidoObserver = orderRepository.reservaPedido(order)
        execute(reservaPedidoObserver.onErrorResumeNext(
            askforReservaPedidoException(order))
            , observer)
    }

    fun reserve(order: FormattedOrder, observer: BaseObserver<BasicDto<ReservaResponse>?>) {
        val reservaPedidoObserver = orderRepository.reserve(order)
        execute(reservaPedidoObserver.onErrorResumeNext(askforReserveException(order)), observer)
    }

    fun deshacerReserva(observer: BaseObserver<BasicDto<Boolean>?>) {
        val deshacerReservaObserver = orderRepository.deshacerReserva()
        execute(deshacerReservaObserver.onErrorResumeNext(
            askforDeshacerReservaException())
            , observer)
    }

    /**
     * Buscar CUV
     */
    fun searchCUV(cuv: String, observer: BaseObserver<BasicDto<ProductCUV>?>) {
        val ordersObservable = orderRepository.searchCUV(cuv)
        execute(ordersObservable.onErrorResumeNext(askForProductCUVListException(cuv)), observer)
    }

    suspend fun searchCUV(cuv: String): BasicDto<ProductCUV>? {
        return orderRepository.searchCUVCoroutine(cuv)
    }

    fun saveProductCUVToPreview(productCUV: ProductCUV?, observer: BaseObserver<BasicDto<Collection<MensajeProl?>?>?>) {
        val savedProductCUVObservable = orderRepository.addProductCUV(productCUV)
            .flatMap { b -> sessionRepository.updateSchedule().map { b } }
        execute(savedProductCUVObservable.onErrorResumeNext(askForSavedProductCUVException(productCUV)), observer)
    }

    fun saveGiftSelected(item: EstrategiaCarrusel?, identificador: String, observer: BaseObserver<BasicDto<Collection<MensajeProl?>?>?>) {

        val savedProductCUVObservable = orderRepository.addGift(item, identificador)
            .flatMap { b -> sessionRepository.updateSchedule().map { b } }
        execute(savedProductCUVObservable.onErrorResumeNext(askForGiftSaved(item, identificador)), observer)

    }

    /**
     * Config Pedido
     */
    fun config(observer: BaseObserver<PedidoConfig?>) {
        val ordersObservable = orderRepository.config()
        execute(ordersObservable.onErrorResumeNext(askForConfigException()), observer)
    }

    /**
     * Kit Inicio
     */
    fun kitInicio(observer: BaseObserver<BasicDto<Boolean>?>) {
        val ordersObservable = orderRepository.kitInicio()
        execute(ordersObservable.onErrorResumeNext(askForKitInicioException()), observer)
    }

    /**
     * kit de socias
     */


    suspend fun kitSociaEmpresaria(): BasicDto<Boolean>?{
        return this.orderRepository.kitSocias()
    }


    /**
     * Estrategia Carrusel
     */
    fun estrategiaCarrusel(observer: DisposableObserver<Collection<EstrategiaCarrusel?>?>) {//estrategiaCarrusel(observer: BaseObserver<Collection<EstrategiaCarrusel?>?>) {
        val ordersObservable = orderRepository.estrategiaCarrusel()
        execute(ordersObservable.onErrorResumeNext(askForEstrategiaCarruselException()), observer)
    }

    /**
     * Obtener lista de productos de del pedido
     */
    fun getOrdersFormatted(observer: BaseObserver<FormattedOrder?>) {
        val ordersObservable = orderRepository.getOrdersFormatted()
        this.execute(ordersObservable.onErrorResumeNext(askForOrdersFormattedException()), observer)
    }

    /**
     * Agregar una oferta del carrusel
     */
    fun addOfferCarrusel(item: EstrategiaCarrusel?, identifier: String, observer: BaseObserver<BasicDto<Collection<MensajeProl?>?>?>) {
        val observable = orderRepository.addOfferCarrusel(item, identifier)
            .flatMap { b -> sessionRepository.updateSchedule().map { b } }
        execute(observable.onErrorResumeNext(askForAddOfferCarruselException(item, identifier)), observer)
    }

    /**
     * Oferta Final
     */
    fun getFinalOfferList(response: ReservaResponse,
                          observer: BaseObserver<BasicDto<OfertaFinalResponse>?>) {
        val observable = orderRepository.getFinalOfferList(response)
        execute(observable
            .onErrorResumeNext(askForGetFinalOfferListException(response)),
            observer)
    }

    /**
     * Agregar una oferta final del carrusel
     */
    fun addFinalOffer(item: OfertaFinal?, identifier: String, observer: BaseObserver<BasicDto<Collection<MensajeProl?>?>?>) {
        val observable = orderRepository.addFinalOffer(item, identifier)
            .flatMap { b -> sessionRepository.updateSchedule().map { b } }
        execute(observable.onErrorResumeNext(askForAddFinalOfferException(item, identifier)), observer)
    }

    /**
     * Obtener la lista de reemplazos sugeridos por producto
     */
    fun getSuggestedReplacement(cuv: String, observer: BaseObserver<Collection<ProductCUV?>?>) {
        val ordersObservable = orderRepository.getSuggestedReplacements(cuv)
        execute(ordersObservable.onErrorResumeNext(askForGetSuggestedReplacementException(cuv)), observer)
    }


    /**
     * Obtener la lista de ofertas por producto
     */
    fun getRelatedOffers(cuv: String, codigoProducto: String, minimoResultados: Int?, maximoResultados: Int?, caracteresDescripcion: Int?, observer: BaseObserver<RelatedOfferResponse?>) {
        val ordersObservable = orderRepository.getRelatedOffers(cuv, codigoProducto, minimoResultados, maximoResultados, caracteresDescripcion)
        execute(ordersObservable.onErrorResumeNext(askForGetRelatedOffersException(cuv, codigoProducto, minimoResultados, maximoResultados, caracteresDescripcion)), observer)
    }

    /**
     * Back order
     */
    fun backOrder(cuv: String?, pedidoID: Int?, pedidoDetalleID: Int?, identifier: String?, observer: BaseObserver<BasicDto<Boolean>?>) {
        val ordersObservable = orderRepository.backOrder(cuv, pedidoID, pedidoDetalleID, identifier)
        execute(ordersObservable.onErrorResumeNext(askForBackOrderException(cuv, pedidoID, pedidoDetalleID, identifier)), observer)
    }

    /**
     * Agregar producto desde el buscador
     */
    fun addSearchedProduct(product: ProductCUV?, identifier: String?, observer: BaseObserver<BasicDto<Collection<MensajeProl?>?>?>) {
        val observable = orderRepository.addSearchedProduct(product, identifier)
            .flatMap { b -> sessionRepository.updateSchedule().map { b } }
        execute(observable.onErrorResumeNext(askForAddSearchedProductException(product, identifier)), observer)
    }

    suspend fun getPedidosPendientes(campaniaId : String?) : PedidoPendiente?{
        return orderRepository.getPedidosPendientes(campaniaId)
    }

    suspend fun getInfoCampanias(campaniaActual: String?, cantidadAnterior: Int?, cantidadProxima: Int?): InfoCampaign?{
        return orderRepository.getInfoCampanias(campaniaActual, cantidadAnterior, cantidadProxima)
    }

    /**
     * Actualizar Dni de la persona que recogera el pedido por ti
     */

    suspend fun updateDni(updateDniRequest: UpdateDniRequest?): BasicDto<String>? {
        return orderRepository.updateDni(updateDniRequest)
    }

    /**
     * Actualizar el estado del check de multipedido
     */

    suspend fun updateState(request: UpdateMultipedidoState) : String{
        return orderRepository.updateStateMultipedido(request)
    }

    /**
     * Obtiene la lista condiciones de las promociones por cuvs
     */
    suspend fun getConditions(cuvs: String): List<ConditionsGetResponse?>? {
        val user = userRepository.getWithCoroutines()
        return orderRepository.getConditions(user?.campaing, cuvs)
        //return orderRepository.getConditions("201913", "00001")
    }

    /**
     * Fun privates
     */

    private fun askForAddOrderException(order: Order): Function<Throwable, Observable<BasicDto<Boolean>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.addOrder(order) }
                else -> Observable.error<BasicDto<Boolean>>(t)
            }
        }
    }

    private fun askForAddInsercionMasivaException(productos: List<ProductoMasivo>, identifier: String): Function<Throwable, Observable<BasicDto<List<ProductoMasivo>>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.insercionMasivaPedido(productos, identifier) }
                else -> Observable.error<BasicDto<List<ProductoMasivo>>>(t)
            }
        }
    }

    private fun askForUndoReservationException(order: Order): Function<Throwable, Observable<BasicDto<Boolean>>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.undoReservation(order) }
                else -> Observable.error<BasicDto<Boolean>>(t)
            }
        }
    }

    private fun askforUpdateProductException(idPedido: Int, identificador: String, orderListItem: OrderListItem): Function<Throwable, Observable<BasicDto<Collection<MensajeProl?>?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.updateProduct(idPedido, identificador, orderListItem) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askforDeleteProductException(order: FormattedOrder, orderListItem: OrderListItem): Function<Throwable, Observable<BasicDto<Boolean>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.deleteProduct(order, orderListItem) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askforReservaPedidoException(order: FormattedOrder)
        : Function<Throwable, Observable<BasicDto<FormattedOrderReserveResponse>>?> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.reservaPedido(order) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askforReserveException(order: FormattedOrder)
        : Function<Throwable, Observable<BasicDto<ReservaResponse>>?> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.reserve(order) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askforDeshacerReservaException(): Function<Throwable, Observable<BasicDto<Boolean>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.deshacerReserva() }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForProductCUVListException(cuv: String): Function<Throwable, Observable<BasicDto<ProductCUV>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.searchCUV(cuv) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForConfigException(): Function<Throwable, Observable<PedidoConfig?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.config() }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForKitInicioException(): Function<Throwable, Observable<BasicDto<Boolean>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.kitInicio() }
                else -> Observable.error<BasicDto<Boolean>>(t)
            }
        }
    }

    private fun askForEstrategiaCarruselException(): Function<Throwable, Observable<Collection<EstrategiaCarrusel?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.estrategiaCarrusel() }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForSavedProductCUVException(productCUV: ProductCUV?): Function<Throwable, Observable<BasicDto<Collection<MensajeProl?>?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.addProductCUV(productCUV) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForGiftSaved(item: EstrategiaCarrusel?, identifier: String?): Function<Throwable, Observable<BasicDto<Collection<MensajeProl?>?>?>> {
        return Function { t ->

            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap {
                        identifier?.let { orderRepository.addGift(item, it) }
                    }
                else -> Observable.error(t)
            }

        }
    }

    private fun askForAddOfferCarruselException(item: EstrategiaCarrusel?, identifier: String)
        : Function<Throwable, Observable<BasicDto<Collection<MensajeProl?>?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.addOfferCarrusel(item, identifier) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForAddFinalOfferException(item: OfertaFinal?, identifier: String)
        : Function<Throwable, Observable<BasicDto<Collection<MensajeProl?>?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.addFinalOffer(item, identifier) }
                else -> Observable.error(t)
            }
        }
    }


    /** RefreshToken */

    private fun getWithRefreshToken(campaign: String?, pdfActive: Boolean): Observable<Collection<MyOrder?>?> {
        val observable = authRepository.refreshToken()
            .flatMap { login -> this.orderRepository.get(login.campaing, pdfActive) }
        return observable.onErrorResumeNext(askForExceptions(campaign, pdfActive))
    }

    private fun getOffline(campaign: String?): Observable<Collection<MyOrder?>?> {
        return this.orderRepository.getFromLocal(campaign)
    }

    private fun askForExceptions(campaign: String?, pdfActive: Boolean)
        : Function<Throwable, Observable<Collection<MyOrder?>?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> getOffline(campaign)
                is ExpiredSessionException -> getWithRefreshToken(campaign, pdfActive)
                else -> Observable.error(t)
            }
        }
    }

    /** getOrdersFormatted */

    private fun askForOrdersFormattedException(): Function<Throwable, Observable<FormattedOrder?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.getOrdersFormatted() }
                else -> Observable.error(t)
            }
        }
    }

    /** getFinalOfferList */

    private fun askForGetFinalOfferListException(response: ReservaResponse): Function<Throwable,
        Observable<BasicDto<OfertaFinalResponse>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.getFinalOfferList(response) }
                else -> Observable.error(t)
            }
        }
    }

    /** getSuggestedReplacements */

    private fun askForGetSuggestedReplacementException(cuv: String): Function<Throwable,
        Observable<Collection<ProductCUV?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.getSuggestedReplacements(cuv) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForGetRelatedOffersException(cuv: String, codigoProducto: String, minimoResultados: Int?, maximoResultados: Int?, caracteresDescripcion: Int?): Function<Throwable,
        Observable<RelatedOfferResponse?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.getRelatedOffers(cuv, codigoProducto, minimoResultados, maximoResultados, caracteresDescripcion) }
                else -> Observable.error(t)
            }
        }
    }

    /** backOrder */

    private fun askForBackOrderException(cuv: String?, pedidoID: Int?, pedidoDetalleID: Int?
                                         , identifier: String?): Function<Throwable, Observable<BasicDto<Boolean>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { orderRepository.backOrder(cuv, pedidoID, pedidoDetalleID, identifier) }
                else -> Observable.error(t)
            }
        }
    }

    /** addSearchedProduct */

    private fun askForAddSearchedProductException(product: ProductCUV?, identifier: String?)
        : Function<Throwable, Observable<BasicDto<Collection<MensajeProl?>?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap {
                        orderRepository.addSearchedProduct(product, identifier)
                            .flatMap { b -> sessionRepository.updateSchedule().map { _ -> b } }
                    }
                else -> Observable.error(t)
            }
        }
    }

}
