package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.mapper.*
import biz.belcorp.consultoras.data.repository.datasource.account.AccountDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.caminobrillante.CaminoBrillanteDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.cliente.ClienteDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.order.OrderDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStoreFactory
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.entity.campaignInformation.InfoCampaign
import biz.belcorp.consultoras.domain.entity.campaignInformation.InfoCampaignDetail
import biz.belcorp.consultoras.domain.repository.OrderRepository
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import io.reactivex.Observable
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase de OrderDataRepository encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-10-30
 */
@Singleton
class OrderDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param orderDataStoreFactory Clase encargada de obtener los datos
 * @param orderEntityDataMapper Clase encargada de mapear los datos
 */
@Inject
internal constructor(private val orderDataStoreFactory: OrderDataStoreFactory,
                     private val orderEntityDataMapper: OrderEntityDataMapper,
                     private val userDataStoreFactory: UserDataStoreFactory,
                     private val caminoBrillanteDataStoreFactory: CaminoBrillanteDataStoreFactory,
                     private val accountDataStoreFactory: AccountDataStoreFactory,
                     private val clienteDataStoreFactory: ClienteDataStoreFactory,
                     private val clienteEntityDataMapper: ClienteEntityDataMapper,
                     private val reservaResponseEntityDataMapper: ReservaResponseEntityDataMapper,
                     private val ofertaFinalEntityDataMapper: OfertaFinalEntityDataMapper,
                     private val basicDtoDataMapper: BasicDtoDataMapper)
    : OrderRepository {


    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override operator fun get(campaign: String?, pdfActive: Boolean)
        : Observable<Collection<MyOrder?>?> {
        val cloudDataStore = orderDataStoreFactory.createCloud()
        val localDataStore = orderDataStoreFactory.createDB()
        return cloudDataStore.get(campaign, pdfActive).flatMap { r1 ->
            localDataStore.save(r1).map { myOrderEntities ->
                orderEntityDataMapper.transform(myOrderEntities)
            }
        }
    }

    override fun paqueteDocumentario(numeroPedido: String?): Observable<BasicDto<String>?> {
        val cloudDataStore = orderDataStoreFactory.createCloud()
        return cloudDataStore.paqueteDocumentario(numeroPedido)
            .map { basicDtoDataMapper.transformString(it) }
    }

    override fun getFromLocal(campaign: String?): Observable<Collection<MyOrder?>?> {
        val localDataStore = orderDataStoreFactory.createDB()
        return localDataStore.get(campaign, true).map { myOrderEntities ->
            orderEntityDataMapper.transform(myOrderEntities)
        }
    }

    /**
     * Servicio que agrega un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun addOrder(order: Order?): Observable<BasicDto<Boolean>?> {
        val store = orderDataStoreFactory.create()
        return store.addOrder(orderEntityDataMapper.transform(order))
            .map { basicDtoDataMapper.transformBoolean(it as ServiceDto<Boolean>) }
    }

    /**
     * Servicio que deshace la reserva de un pedido
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun undoReservation(body: Order?): Observable<BasicDto<Boolean>?> {
        val store = orderDataStoreFactory.create()
        return store.undoReservation(orderEntityDataMapper.transform(body))
            .map { basicDtoDataMapper.transformBoolean(it as ServiceDto<Boolean>) }
    }

    override fun searchCUV(cuv: String): Observable<BasicDto<ProductCUV>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()
        val caminoBrillanteDataStore = caminoBrillanteDataStoreFactory.createDB()

        return caminoBrillanteDataStore.getNivelConsultoraCaminoBrillanteAsObservable().flatMap { nivel ->
            userDataStore.getWithObservable().flatMap {
                orderDataStore.searchCUV(it.campaing?.toInt(), cuv,
                    it.numberOfCampaings?.toInt(), it.consultantAssociateId, it.codigoPrograma,
                    it.consecutivoNueva, it.zoneID?.toInt(), it.regionID?.toInt(), nivel)
            }.map { orderEntityDataMapper.transformSearchCUV(it) }
        }

    }

    override suspend fun searchCUVCoroutine(cuv: String): BasicDto<ProductCUV>? {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()
        val caminoBrillanteDataStore = caminoBrillanteDataStoreFactory.createDB()


        val nivel = caminoBrillanteDataStore.getNivelConsultoraCaminoBrillante().await()

        userDataStore.getWithCoroutine()?.run{
                    orderDataStore.searchCUVCoroutine(campaing?.toInt(), cuv,
                        numberOfCampaings?.toInt(), consultantAssociateId, codigoPrograma,
                        consecutivoNueva, zoneID?.toInt(), regionID?.toInt(), nivel).await().
                        let{ product ->

                            return orderEntityDataMapper.transformSearchCUV(product)
                        }
        }

        return BasicDto<ProductCUV>()

    }

    override fun config(): Observable<PedidoConfig?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.config(it.campaing?.toInt(), it.regionCode, it.zoneCode)
                .map { b -> orderEntityDataMapper.transform(b.barra) }
        }
    }

    override fun addOfferCarrusel(item: EstrategiaCarrusel?, identificador: String?): Observable<BasicDto<Collection<MensajeProl?>?>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.addOfferCarrusel(PedidoInsertRequestEntity().apply {
                campaniaId = it.campaing?.toInt()
                cantidad = item?.cantidad
                ipUsuario = getIPorIdentifier(identificador)
                identifier = identificador
                aceptacionConsultoraDA = it.consultantAcceptDA
                montoMaximoPedido = it.montoMaximoPedido
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                codigosConcursos = it.codigosConcursos
                nroCampanias = it.numberOfCampaings?.toInt()
                producto = ProductCuvEntity().apply {
                    cuv = item?.cuv
                    estrategiaId = item?.estrategiaID
                    tipoEstrategiaId = item?.tipoEstrategiaID
                    flagNueva = item?.flagNueva
                    indicadorMontoMinimo = item?.indicadorMontoMinimo
                    marcaId = item?.marcaID
                }

            }).map { r ->
                reservaResponseEntityDataMapper
                    .transformUpResponse(r as ServiceDto<List<MensajeProlEntity?>?>)
            }
        }
    }

    override fun addGift(item: EstrategiaCarrusel?, identificador: String)
        : Observable<BasicDto<Collection<MensajeProl?>?>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()
        return userDataStore.getWithObservable().flatMap {

            orderDataStore.addGift(PedidoInsertRequestEntity().apply {

                campaniaId = it.campaing?.toInt()
                cantidad = 1
                ipUsuario = getIPorIdentifier(identificador)
                clienteID = 0
                clienteDescripcion = ""
                identifier = identificador
                codigosConcursos = it.codigosConcursos
                aceptacionConsultoraDA = it.consultantAcceptDA
                montoMaximoPedido = it.montoMaximoPedido
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                consultoraNueva = it.consultoraNueva
                isSugerido = false
                producto = ProductCuvEntity().apply {
                    cuv = item?.cuv
                    precioCatalogo = item?.precioValorizado?.toDouble() ?: 0.0
                    tipoEstrategiaId = item?.tipoEstrategiaID
                    flagNueva = item?.flagNueva
                    tipoOfertaSisId = 0
                    configuracionOfertaId = 0
                    indicadorMontoMinimo = item?.indicadorMontoMinimo
                    marcaId = item?.marcaID
                    estrategiaId = item?.estrategiaID
                }
                origenPedidoWebCarrusel = Constant.CODIGO_ORIGEN_PEDIDO_REGALO

            }).map { r ->
                reservaResponseEntityDataMapper.transformUpResponse(r as ServiceDto<List<MensajeProlEntity?>?>)
            }
        }
    }


    override fun kitInicio(): Observable<BasicDto<Boolean>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.kitInicio(KitInicioRequest().apply {
                campaniaId = it.campaing?.toInt()
                nroCampanias = it.numberOfCampaings?.toInt()
                consultoraNueva = it.consultoraNueva
                consecutivoNueva = it.consecutivoNueva
                isDiaPROL = it.isDayProl
                fechaInicioFacturacion = it.billingStartDate
                horaInicioNoFacturable = it.horaInicioNoFacturable
                horaCierreNoFacturable = it.horaCierreNoFacturable
                horaInicio = it.horaInicio
                horaFin = it.horaFinPortal
                codigoPrograma = it.codigoPrograma
            })
        }.map { basicDtoDataMapper.transformBoolean(it as ServiceDto<Boolean>) }
    }


    override suspend fun kitSocias(): BasicDto<Boolean>? {
        val orderDataStore = orderDataStoreFactory.createCloud()
        val userDataStore = userDataStoreFactory.createDB()

        val usuario = userDataStore.getWithCoroutine()

        val respuesta = orderDataStore.kitSociasEmpresarias(
            KitSociaEmpresariaRequestEntity().apply {
                campaniaId = Integer.valueOf(usuario?.campaing?:StringUtil.Empty)
                codigoPrograma = usuario?.codigoPrograma
                consecutivoNueva = usuario?.consecutivoNueva
            })

        val res = respuesta.await()

        return basicDtoDataMapper.transformBoolean(res as ServiceDto<Boolean>)

    }


    override fun estrategiaCarrusel(): Observable<Collection<EstrategiaCarrusel?>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.estrategiaCarrusel(it.campaing?.toInt(), it.numberOfCampaings?.toInt(),
                it.consultantAssociateId, it.billingStartDate, it.billingEndDate, it.codigoPrograma,
                it.consecutivoNueva!!.toInt(), it.regionCode, it.zoneID!!.toInt(),
                it.countryMoneySymbol, it.isRDTieneRDC, it.isRDEsSuscrita, it.isRDEsActiva,
                it.isRDActivoMdo, it.isUserTest, it.isTieneMG, it.zoneCode, true)
        }.map { orderEntityDataMapper.transform(it) }
    }

    override fun addProductCUV(productCUV: ProductCUV?): Observable<BasicDto<Collection<MensajeProl?>?>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.insertPedido(PedidoInsertRequestEntity().apply {
                aceptacionConsultoraDA = it.consultantAcceptDA
                campaniaId = it.campaing?.toInt()
                cantidad = productCUV?.cantidad
                clienteDescripcion = it.consultantName
                clienteID = productCUV?.clienteId
                campaniaId = it.campaing?.toInt()
                codigoPrograma = it.codigoPrograma
                codigosConcursos = it.codigosConcursos
                consecutivoNueva = it.consecutivoNueva
                consultoraNueva = it.consultoraNueva
                fechaFinFacturacion = it.billingEndDate
                fechaInicioFacturacion = it.billingStartDate
                identifier = productCUV?.identifier
                ipUsuario = getIPorIdentifier(productCUV?.identifier)
                montoMaximoPedido = it.montoMaximoPedido
                producto = orderEntityDataMapper.productCuvEntityDataMapper.transform(productCUV)
                isSugerido = productCUV?.isSugerido
            }).map { r ->
                reservaResponseEntityDataMapper.transformUpResponse(r as ServiceDto<List<MensajeProlEntity?>?>)
            }
        }
    }


    override suspend fun insertarPedido(request: PedidoInsertRequest): BasicDto<Collection<MensajeProl?>?>? {
        val x = orderDataStoreFactory.create()
            .insertarPedido(PedidoInsertRequestEntity.transform(request)).await() as ServiceDto<List<MensajeProlEntity?>?>
        return reservaResponseEntityDataMapper.transformUpResponse(x)
    }

    override fun updateProduct(idPedido: Int?, identificador: String?,
                               orderListItem: OrderListItem)
        : Observable<BasicDto<Collection<MensajeProl?>?>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.updatePedido(PedidoUpdateRequest().apply {
                pedidoID = idPedido
                pedidoDetalleID = orderListItem.id
                campaniaId = it.campaing?.toInt()
                cantidad = orderListItem.cantidad
                clienteID = orderListItem.clienteID
                clienteDescripcion = orderListItem.nombreCliente
                identifier = identificador
                ipUsuario = getIPorIdentifier(identificador)
                codigosConcursos = it.codigosConcursos
                aceptacionConsultoraDA = it.consultantAcceptDA
                montoMaximoPedido = it.montoMaximoPedido
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                cuv = orderListItem.cuv
                precioCatalogo = orderListItem.precioUnidad
                tipoEstrategiaID = orderListItem.tipoEstrategiaID
                tipoOfertaSisID = orderListItem.tipoOfertaSisID
                setID = orderListItem.setID

                // Cambio para Pedido Reservado
                segmentoInternoID = it.segmentoInternoID
                montoMinimoPedido = it.montoMinimoPedido
                isValidacionAbierta = it.isValidacionAbierta
                isZonaValida = it.isZonaValida
                isValidacionInteractiva = it.isValidacionInteractiva
                isDiaProl = it.isDayProl
                codigoZona = it.zoneCode
                codigoRegion = it.regionCode
                isUsuarioPrueba = it.isUserTest
                simbolo = it.countryMoneySymbol

            }).map { r ->
                reservaResponseEntityDataMapper.transformUpResponse(r as ServiceDto<List<MensajeProlEntity?>?>)
            }

        }
    }

    override fun deleteProduct(order: FormattedOrder, orderListItem: OrderListItem): Observable<BasicDto<Boolean>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.deletePedido(PedidoDeleteRequest().apply {
                campaniaId = it.campaing?.toInt()
                aceptacionConsultoraDA = it.consultantAcceptDA
                pedidoID = order.pedidoID
                pedidoDetalleID = orderListItem.id
                tipoOfertaSisID = orderListItem.tipoOfertaSisID
                cuv = orderListItem.cuv
                cantidad = orderListItem.cantidad
                observacionPROL = orderListItem.observacionPROL
                identifier = order.identifier
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                zonaValida = it.isZonaValida
                codigosConcursos = it.codigosConcursos
                nroCampanias = it.numberOfCampaings?.toInt()
                setID = orderListItem.setID

                // Cambio para Pedido Reservado
                segmentoInternoID = it.segmentoInternoID
                montoMinimoPedido = it.montoMinimoPedido
                montoMaximoPedido = it.montoMaximoPedido
                consultoraNueva = it.consultoraNueva
                isValidacionAbierta = it.isValidacionAbierta
                isValidacionInteractiva = it.isValidacionInteractiva
                isDiaProl = it.isDayProl
                codigoZona = it.zoneCode
                codigoRegion = it.regionCode
                codigoSeccion = it.codigoSeccion
                isUsuarioPrueba = it.isUserTest
                simbolo = it.countryMoneySymbol
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate

                reemplazarFestival = orderListItem.reemplazarFestival
            }).map { r ->
                reservaResponseEntityDataMapper.transformDeleteResponse(r as ServiceDto<Boolean>)

            }
        }
    }

    override fun insercionMasivaPedido(productoList: List<ProductoMasivo>, identifier: String): Observable<BasicDto<List<ProductoMasivo>>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val orderDBDataStore = orderDataStoreFactory.createDB()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.insercionMasivaPedido(PedidoMasivoRequest().apply {
                campaniaId = it.campaing?.toInt()
                ipUsuario = identifier
                this.identifier = identifier
                codigosConcursos = it.codigosConcursos
                aceptacionConsultoraDA = it.consultantAcceptDA
                montoMaximoPedido = BigDecimal(it.montoMaximoPedido.toString())
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                consultoraNueva = it.consultoraNueva
                usuarioPrueba = it.isUserTest
                nombreConsultora = it.consultantName
                simbolo = it.countryMoneySymbol
                codigoRegion = it.regionCode
                codigoZona = it.zoneCode
                nroCampanias = it.numberOfCampaings?.toInt()
                consultoraAsociada = it.consultoraAsociada
                regionID = it.regionID?.toInt()
                zonaID = it.zoneID?.toInt()
                codigoSeccion = it.codigoSeccion

                productos = orderEntityDataMapper.transformToProductoMasivoEntityList(productoList)
            })
                .flatMap { serviceDTO -> orderDBDataStore.guardarProductoMasivoList(serviceDTO) }
                .map {
                    reservaResponseEntityDataMapper
                        .transformToProductoMasivo(it)
                }
        }
    }

    override fun reservaPedido(order: FormattedOrder): Observable<BasicDto<FormattedOrderReserveResponse>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.reservaPedido(PedidoReservaRequest().apply {
                campaniaId = it.campaing?.toInt()
                aceptacionConsultoraDA = it.consultantAcceptDA
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                diasAntes = it.diasAntes
                horaInicioNoFacturable = it.horaInicioNoFacturable
                horaCierreNoFacturable = it.horaCierreNoFacturable
                horaInicio = it.horaInicio
                horaFin = it.endTime
                segmentoInternoID = it.segmentoInternoID
                isPROLSinStock = it.isProlSinStock
                montoMinimoPedido = it.montoMinimoPedido?.toBigDecimal()
                montoMaximoPedido = it.montoMaximoPedido?.toBigDecimal()
                consultoraNueva = it.consultoraNueva
                isValidacionAbierta = it.isValidacionAbierta
                isValidacionInteractiva = it.isValidacionInteractiva
                isZonaValida = it.isZonaValida
                codigosConcursos = it.codigosConcursos
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                isDiaPROL = it.isDayProl
                codigoZona = it.zoneCode
                montoMaximoDesviacion = it.montoMaximoDesviacion?.toBigDecimal()
            }).map { r ->
                reservaResponseEntityDataMapper
                    .transformReservaResponse(order, r as ServiceDto<ReservaResponseEntity>)
            }
        }
    }

    override fun reserve(order: FormattedOrder): Observable<BasicDto<ReservaResponse>?> {

        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.reservaPedido(PedidoReservaRequest().apply {
                campaniaId = it.campaing?.toInt()
                aceptacionConsultoraDA = it.consultantAcceptDA
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                diasAntes = it.diasAntes
                horaInicioNoFacturable = it.horaInicioNoFacturable
                horaCierreNoFacturable = it.horaCierreNoFacturable
                horaInicio = it.horaInicio
                horaFin = it.endTime
                segmentoInternoID = it.segmentoInternoID
                isPROLSinStock = it.isProlSinStock
                montoMinimoPedido = it.montoMinimoPedido?.toBigDecimal()
                montoMaximoPedido = it.montoMaximoPedido?.toBigDecimal()
                consultoraNueva = it.consultoraNueva
                isValidacionAbierta = it.isValidacionAbierta
                isValidacionInteractiva = it.isValidacionInteractiva
                isZonaValida = it.isZonaValida
                codigosConcursos = it.codigosConcursos
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
                isDiaPROL = it.isDayProl
                codigoZona = it.zoneCode
                montoMaximoDesviacion = it.montoMaximoDesviacion?.toBigDecimal()
            }).map { r ->
                reservaResponseEntityDataMapper
                    .transformReservaResponse(r as ServiceDto<ReservaResponseEntity>)
            }
        }
    }

    override fun deshacerReserva(): Observable<BasicDto<Boolean>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.deshacerPedido(
                it.campaing?.toInt(),
                it.indicadorGPRSB,
                it.codigoPrograma,
                it.consecutivoNueva,
                it.consultoraAsociada
            ).map { r -> basicDtoDataMapper.transformBoolean(r as ServiceDto<Boolean>) }
        }
    }

    override suspend fun getOrders(campaniaID: Int?,
                                   codigoPrograma: String?,
                                   consecutivoNueva: Int?,
                                   montoMaximoPedido: Double?,
                                   consultoraNueva: Int?,
                                   nroCampanias: Int?,
                                   nivelCaminoBrillante: Int?,
                                   nombreConsultora: String?,
                                   codigoRegion: String?,
                                   codigoZona: String?,
                                   codigoSeccion: String?): PedidoGetResponse? {
        val orderStore = orderDataStoreFactory.create()
        return orderEntityDataMapper.transform(orderStore.getOrders(campaniaID,
            codigoPrograma,
            consecutivoNueva,
            montoMaximoPedido,
            consultoraNueva,
            nroCampanias,
            nivelCaminoBrillante,
            nombreConsultora,
            codigoRegion,
            codigoZona,
            codigoSeccion).await())
    }

    override fun getOrdersFormatted(): Observable<FormattedOrder?> {
        val orderStore = orderDataStoreFactory.create()
        val clientDataStore = clienteDataStoreFactory.createDB()
        val userStore = userDataStoreFactory.createDB()
        val caminoBrillanteDataStore = caminoBrillanteDataStoreFactory.createDB()

        var ue: UserEntity? = null

        var listFormatted: FormattedOrder? = null
        return userStore.getWithObservable().flatMap { user ->
            ue = user

            caminoBrillanteDataStore.getNivelConsultoraCaminoBrillanteAsObservable().flatMap { nivel ->
                orderStore.getOrdersList(user.campaing?.toInt()
                    , user.codigoPrograma
                    , user.consecutivoNueva
                    , user.montoMaximoPedido
                    , user.consultoraNueva
                    , user.numberOfCampaings?.toInt()
                    , nivel
                    , user.consultantName
                    , user.regionCode
                    , user.zoneCode
                    , user.codigoSeccion
                    , user.isUltimoDiaFacturacion
                    , user.isPagoContado
                    , user.billingStartDate
                    , user.billingEndDate
                    , user.isMultipedido)
            }

        }.map {
            listFormatted = orderEntityDataMapper.transformFormatted(it)
            Observable.just(listFormatted)
        }.flatMap {
            val clientList = getClientsID(listFormatted?.productosDetalle)
            clientDataStore.getClientesByListIds(clientList)
                .map { l ->

                    val clientsTransform = clienteEntityDataMapper.transform(l)

                    val listClients: ArrayList<Cliente> = ArrayList()

                    if (isConsultantOrder(listFormatted?.productosDetalle)) {
                        val clientEntity = Cliente()
                        clientEntity.id = 0
                        clientEntity.clienteID = 0
                        clientEntity.alias = ue?.alias
                        clientEntity.nombres = "PARA MÍ"
                        clientEntity.estado = 1

                        listClients.add(clientEntity)
                    }

                    listClients.addAll(clientsTransform as Collection<Cliente>)

                    listClients.forEach { c1 ->
                        val finalList = ArrayList<OrderListItem>()
                        listFormatted?.productosDetalle?.forEach { orderListItem ->
                            orderListItem?.clienteID?.let {
                                if (it == c1.clienteID) {
                                    finalList.add(orderListItem)
                                }
                            }
                        }
                        c1.orderList = finalList
                    }

                    listFormatted?.clientesDetalle = listClients
                    listFormatted?.isDiaProl = ue?.isDayProl

                    clientsTransform
                }
        }.map { listFormatted }
    }

    override fun getFinalOfferList(response: ReservaResponse): Observable<BasicDto<OfertaFinalResponse>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.getFinalOfferList(PedidoOfertaFinalRequest().apply {
                campaniaId = it.campaing?.toInt()
                nroCampanias = it.numberOfCampaings?.toInt()
                isValidacionInteractiva = it.isValidacionInteractiva
                isZonaValida = it.isZonaValida
                fechaInicioFacturacion = it.billingStartDate
                montoMinimoPedido = it.montoMinimoPedido?.toBigDecimal()
                resultadoReserva = reservaResponseEntityDataMapper.transform(response)
                codigoPrograma = it.codigoPrograma
                consecutivoNueva = it.consecutivoNueva
            }).map { r ->
                ofertaFinalEntityDataMapper
                    .transformResponse(r as ServiceDto<OfertaFinalResponseEntity>)
            }
        }
    }

    override fun addFinalOffer(item: OfertaFinal?, identificador: String?): Observable<BasicDto<Collection<MensajeProl?>?>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.addFinalOffer(PedidoOfertaFinalInsertRequest().apply {
                campaniaId = it.campaing?.toInt()
                nroCampanias = it.numberOfCampaings?.toInt()
                cantidad = item?.cantidad
                indicadorGPRSB = it.indicadorGPRSB
                consecutivoNueva = it.consecutivoNueva
                consultoraAsociada = it.consultoraAsociada
                codigoPrograma = it.codigoPrograma
                ipUsuario = getIPorIdentifier(identificador)
                identifier = identificador
                aceptacionConsultoraDA = it.consultantAcceptDA
                montoMaximoPedido = it.montoMaximoPedido
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                consultoraNueva = it.consultoraNueva
                codigosConcursos = it.codigosConcursos
                producto = ofertaFinalEntityDataMapper.transform(item)
            }).map { r ->
                reservaResponseEntityDataMapper.transformUpResponse(r as ServiceDto<List<MensajeProlEntity?>?>)
            }
        }
    }

    /**
     * Servicio que lista los reemplazos sugeridos de un producto por cuv
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getSuggestedReplacements(cuv: String?): Observable<Collection<ProductCUV?>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.getSuggestedReplacements(it.campaing?.toInt(), cuv,
                it.numberOfCampaings?.toInt(), it.consultoraAsociada)
                .map { r ->
                    orderEntityDataMapper.transformSuggestedReplacement(r)
                }
        }
    }

    override fun getRelatedOffers(pcuv: String?, pcodigoProduct: String?, pminimoResultados: Int?, pmaximoResultados: Int?, pcaracteresDescripcion: Int?): Observable<RelatedOfferResponse?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {

            orderDataStore.getRelatedOffers(
                RelatedOfferRequest().apply {
                    campaniaId = it.campaing?.toInt()
                    codigoZona = it.zoneCode
                    cuv = pcuv
                    codigoProducto = pcodigoProduct
                    maximoResultados = pmaximoResultados
                    minimoResultados = pminimoResultados
                    caracteresDescripcion = pcaracteresDescripcion
                    personalizaciones = it.personalizacionesDummy ?: ""
                    fechaInicioFacturacion = it.billingStartDate ?: ""
                    configuracion = SearchConfiguracionEntity().apply {
                        rdEsSuscrita = it.isRDEsSuscrita
                        rdEsActiva = it.isRDEsActiva
                        lider = it.lider
                        rdActivoMdo = it.isRDActivoMdo
                        rdTieneRDC = it.isRDTieneRDC
                        rdTieneRDI = it.isRDTieneRDI
                        rdTieneRDCR = it.isRDTieneRDCR
                        diaFacturacion = it.diaFacturacion
                    }
                }).map { r -> orderEntityDataMapper.transformRelatedOffers(r) }
        }
    }

    override fun backOrder(cuv: String?, pedidoID: Int?, pedidoDetalleID: Int?,
                           identifier: String?): Observable<BasicDto<Boolean>?> {
        val orderDataStore = orderDataStoreFactory.create()
        val userDataStore = userDataStoreFactory.createDB()

        return userDataStore.getWithObservable().flatMap {

            orderDataStore.backOrder(BackOrderRequest().apply {
                aceptacionConsultoraDA = it.consultantAcceptDA
                campaniaId = it.campaing?.toInt()
                codigoPrograma = it.codigoPrograma
                this.pedidoDetalleID = pedidoDetalleID
                this.pedidoID = pedidoID
                this.identifier = identifier
                this.cuv = cuv

            }).map { r -> basicDtoDataMapper.transformBoolean(r as ServiceDto<Boolean>) }
        }
    }



    override fun addSearchedProduct(product: ProductCUV?, identifier: String?): Observable<BasicDto<Collection<MensajeProl?>?>?> {
        val userDataStore = userDataStoreFactory.createDB()
        val orderDataStore = orderDataStoreFactory.create()

        return userDataStore.getWithObservable().flatMap {
            orderDataStore.addSearchedProduct(ProductCuvSearchRequestEntity().apply {
                campaniaId = it.campaing?.toInt()
                nroCampanias = it.numberOfCampaings?.toInt()
                cantidad = product?.cantidad
                limiteVenta = product?.limiteVenta
                consecutivoNueva = it.consecutivoNueva
                consultoraAsociada = it.consultoraAsociada
                codigoPrograma = it.codigoPrograma
                origenPedidoWeb = product?.origenPedidoWeb
                ipUsuario = getIPorIdentifier(identifier)
                this.identifier = identifier
                usuarioPrueba = it.isUserTest
                aceptacionConsultoraDA = it.consultantAcceptDA
                nombre = it.consultantName
                consultoraNueva = it.consultoraNueva
                codigosConcursos = it.codigosConcursos
                codigoZona = it.zoneCode
                cuv = product?.cuv
                marcaID = product?.marcaId
                descripcion = product?.description
                precioCatalogo = BigDecimal(product?.precioCatalogo ?: 0.0)
                tipoPersonalizacion = product?.tipoPersonalizacion
                estrategiaId = product?.estrategiaId
                montoMaximoPedido = it.montoMaximoPedido
                fechaInicioFacturacion = it.billingStartDate
                fechaFinFacturacion = it.billingEndDate
                tipoEstrategiaId = product?.tipoEstrategiaId

            }).map { r -> basicDtoDataMapper.transformStringResponse(r as ServiceDto<List<MensajeProlEntity?>?>) }
        }
    }

    override suspend fun updateDni(updateDniRequest: UpdateDniRequest?): BasicDto<String>? {
        val cloudDataStore = orderDataStoreFactory.createCloud()
        return basicDtoDataMapper.transformString(cloudDataStore.updateDni(UpdateDniRequestEntity.transform(updateDniRequest)).await() as ServiceDto<String>)
    }

    override suspend fun getPedidosPendientes(campaniaId: String?): PedidoPendiente? {
        val orderDataStore = orderDataStoreFactory.create()

        val r = orderDataStore.getPedidosPendientes(campaniaId)?.await()

        return PedidoPendiente().apply {
                pedidoPendiente = r?.pedidoPendiente
            }
    }

    override suspend fun getInfoCampanias(campaniaActual: String?, cantidadAnterior: Int?, cantidadProxima: Int?): InfoCampaign? {
        val orderDataStore = orderDataStoreFactory.create()

        val r = orderDataStore.getInfoCampanias(campaniaActual, cantidadAnterior, cantidadProxima).await()

        return InfoCampaign().apply {
            r?.let{order ->
                Campania = order.Campania

                FechaFacturacion = order.FechaFacturacion

                FechaPago = order.FechaPago

                CampaniaAnterior = ArrayList<InfoCampaignDetail>().apply {
                    order.CampaniaAnterior?.let { ant->
                        ant.forEach {detail ->
                            add(
                                InfoCampaignDetail().apply {
                                    Campania = detail.Campania

                                    FechaFacturacion = detail.FechaFacturacion

                                    FechaPago = detail.FechaPago
                                })
                        }
                    }
                }

                CampaniaSiguiente = ArrayList<InfoCampaignDetail>().apply {
                    order.CampaniaSiguiente?.let { sig ->
                        sig.forEach {detail ->
                            add(
                                InfoCampaignDetail().apply {
                                    Campania = detail.Campania

                                    FechaFacturacion = detail.FechaFacturacion

                                    FechaPago = detail.FechaPago
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override suspend fun updateStateMultipedido(request: UpdateMultipedidoState?): String {
        val orderDataStore = orderDataStoreFactory.create()
        val state = if(request?.state == true) 1 else 0
        return orderDataStore.updateStateMultipedido(
            UpdateMultipedidoStateEntity(request?.pedidoId ?: 0, state)).await()
    }

    /** private functions */

    private fun getClientsID(list: List<OrderListItem?>?): List<Int> {
        return ArrayList<Int>().apply {
            list?.forEach { it ->
                it?.clienteID?.let {
                    add(it)
                }
            }
        }.distinct()
    }

    private fun isConsultantOrder(list: List<OrderListItem?>?): Boolean {
        list?.forEach { it ->
            it?.clienteID?.let {
                if (it == 0) return true
            }
        }
        return false
    }

    private fun getIPorIdentifier(identifier: String?): String {
        var ip = NetworkUtil.getIPAddress(true)
        if (ip.isNullOrBlank()) ip = identifier
        return ip
    }

    override suspend fun getConditions(campaing: String?, cuvs: String): List<ConditionsGetResponse?>? {
        val orderStore = orderDataStoreFactory.create()
        return orderEntityDataMapper.transformConditions(orderStore.getConditions(campaing, cuvs).await())
    }
}
