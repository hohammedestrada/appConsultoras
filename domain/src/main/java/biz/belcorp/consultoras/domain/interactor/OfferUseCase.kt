package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.*
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.OfferRepository
import biz.belcorp.consultoras.domain.repository.UserRepository
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.join
import javax.inject.Inject

class OfferUseCase @Inject
constructor(private val offerRepository: OfferRepository,
            private val userRepository: UserRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    suspend fun configuracion(diaInicio: Int?, campaniaID: Int?, consecutivoNueva: Int?,
                              codigoPrograma: String?, codigoRegion: String?, codigoZona: String?,
                              zonaId: Int?, simbolo: String?, esSuscrita: Boolean?, esActiva: Boolean?): List<ConfiguracionPorPalanca?>? {
        val listaOferta = offerRepository.ofertasDisponibles(campaniaID, diaInicio, esSuscrita, esActiva).await()

        listaOferta.join(",")?.let {

            return offerRepository.configuracion(it, campaniaID, consecutivoNueva,
                codigoPrograma, codigoRegion, codigoZona, zonaId, simbolo).await()
        } ?: run {
            throw GanaMasNoConfigException()
        }
    }

    suspend fun ordenamientos(): List<Ordenamiento?>? {
        return offerRepository.ordenamientos().await()
    }

    suspend fun configuracion(): List<ConfiguracionPorPalanca?> {
        val user = userRepository.getWithCoroutines()
        return configuracion(user?.diaFacturacion, user?.campaing?.toInt(), user?.consecutivoNueva,
            user?.codigoPrograma, user?.regionCode, user?.zoneCode, user?.zoneID?.toInt(),
            user?.countryMoneySymbol, user?.isRDEsSuscrita, user?.isRDEsActiva)
            ?.let { it } ?: throw GanaMasNoConfigException()
    }

    suspend fun ofertas(request: OfertaRequest?): List<Oferta?>? {
        return offerRepository.ofertas(request).await()
    }

    suspend fun ofertasNoCache(request: OfertaRequest?): List<Oferta?>? {
        return offerRepository.ofertasNoCache(request).await()
    }

    suspend fun categorias(): List<Categoria?>? {
        val user = userRepository.getWithCoroutines()

        user.let {
            return offerRepository.categorias().await()
        }
    }

    suspend fun categoriasInSearch(): List<Categoria?>? {
        val user = userRepository.getWithCoroutines()

        user.let {
            return offerRepository.categoriasInSearch(
                CategoriaRequestInSearch().apply {
                    it?.campaing?.let { campaniaId = it }
                    codigoConsultora = it?.consultantCode
                    codigoZona = it?.zoneCode
                    perzonalizaciones = it?.personalizacionesDummy
                    lider = it?.esLider
                    rdesSuscrita = it?.isRDEsSuscrita
                    rdesActiva = it?.isRDEsActiva
                    rdActivoModo = it?.isRDActivoMdo
                    rdTieneRdc = it?.isRDTieneRDC
                    rdTieneRdi = it?.isRDTieneRDI
                    rdTieneRdcr = it?.isRDTieneRDCR
                    diaFacturacion = it?.diaFacturacion

                }
            ).await()
        }
    }

    suspend fun filtros(conHijos: Boolean): List<GroupFilter?>? {
        return offerRepository.filtros(conHijos).await()
    }

    suspend fun ofertasXCategoria(request: SearchRequest?): SearchResponse? {
        return offerRepository.ofertasXCategoria(request).await()
    }

    suspend fun ofertasXCategoria(order: String, orderType: String, filterList: List<SearchFilter>, numPage: Int? = null, rows: Int? = null, resultEmpty: Boolean = false): List<ProductCUV?> {
        val product = createSearchProduct(order, orderType, filterList, numPage, rows)?.productos
        return if (product != null && product.isNotEmpty()) {
            product.toMutableList()
        } else {
            if (resultEmpty) {
                emptyList<ProductCUV?>()
            } else {
                throw GanaMasNoOffersByCategoriesException()
            }
        }
    }

    suspend fun ofertasUpselling(cuv: String, codigos: List<String>, precioCat: Double): List<Oferta?> {
        val ofertas = createUpsellingOfertas(cuv, codigos, precioCat)
        ofertas?.let {
            if (it.isNotEmpty())
                return it
        }
        throw GanaMasNoOffersByCategoriesException()
    }

    suspend fun ofertasCrosselling(tipo: String?, campaniaID: Int?, cuv: String?, fechaInicioFacturacion: String?, sugeridos: Boolean? = false): List<Oferta?>? {
        return offerRepository.ofertasCrosselling(tipo, campaniaID, cuv, fechaInicioFacturacion, sugeridos).await()
    }

    suspend fun ofertasXFiltros(order: String, orderType: String, filterList: List<SearchFilter>): List<ProductCUV?> {
        val product = createSearchProduct(order, orderType, filterList)?.productos
        return if (product != null && product.isNotEmpty()) {
            product.toMutableList()
        } else {
            throw GanaMasNoOffersByFiltersException()
        }
    }

    suspend fun getFicha(campaniaID: Int?, tipo: String?, cuv: String?, simbolo: String?,
                         fechaInicioFacturacion: String?, codigoPrograma: String?, consecutivoNueva: Int?,
                         montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                         nombreConsultora: String?, codigoRegion: String?, codigoZona: String?,
                         codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                         fechaFinFacturacion: String?): Oferta? {
        return offerRepository.getFicha(campaniaID, tipo, cuv, simbolo, fechaInicioFacturacion,
            codigoPrograma, consecutivoNueva, montoMaximoPedido, consultoraNueva, nroCampanias,
            nombreConsultora, codigoRegion, codigoZona, codigoSeccion, esUltimoDiaFacturacion,
            pagoContado, fechaFinFacturacion).await()
    }

    suspend fun getFichaShareURL(pCuv: String?, type: String?, pMarcaID: Int?, marca: String?, oferta: Oferta): String? {
        val user = userRepository.getWithCoroutines()

        val result = offerRepository.getFichaShareURL(
            ShareOfertaRequest().apply {
                campaniaID = user?.campaing?.toInt()
                cuv = pCuv
                tipoPersonalizacion = type
                imagenUrl = oferta.imagenURL
                marcaId = pMarcaID
                nombreMarca = marca
                nombreOferta = oferta.nombreOferta
            }
        ).await()

        return result
    }

    suspend fun getOfertaAtp() : Oferta? {
        val user = userRepository.getWithCoroutines()

        val result = offerRepository.getOfertaAtp(
            OfertaAtpRequest().apply {
                campaniaId = user?.campaing?.toInt()
                codigoPrograma = user?.codigoPrograma
                consecutivoNueva = user?.consecutivoNueva
                montoMaximoPedido = user?.montoMaximoPedido
                consultoraNueva = user?.consultoraNueva
                nroCampanias = user?.numberOfCampaings?.toInt()
                codigoSeccion = user?.codigoSeccion
                codigoRegion = user?.regionCode
                codigoZona = user?.zoneCode
                zonaId = user?.zoneID?.toInt()
            }
        ).await()

        return if (result != null && result.componentes?.isNotEmpty() == true) {
            result
        } else {
            throw ArmaTuPackNoExisteProductosException()
        }
    }

    suspend fun ofertasXPalanca(config: ConfiguracionPorPalanca, request: OfertaRequest, shouldCallOnline: Boolean = false): List<Oferta?>? {
        request.tipo = config.tipoOferta
        val offerResult = if (shouldCallOnline)
            ofertasNoCache(request)
        else
            ofertas(request)

        val ofertas = setOrderOffer(request.tipo, offerResult)

        if(ofertas?.isEmpty() == true)
            throw GanaMasNoOffersByCategoriesException()
        return setOrderOffer(request.tipo, offerResult)
    }

    fun setOrderOffer(palanca: String?, list: List<Oferta?>?): List<Oferta?>? {
        list?.let {itemList ->
            if (palanca == OfferTypes.LAN) {
                var listInput = itemList.toMutableList()
                val listIndividual = itemList.filter { item -> item?.flagIndividual == true }
                var listResultLan = arrayListOf<Oferta>()
                for (item in listIndividual) {
                    val listGroup = listInput.filter { offer -> offer?.codigoProducto == item?.codigoProducto }.filterNotNull()
                    if (listGroup.isNotEmpty()) {
                        listResultLan.addAll(listGroup)
                        listInput.removeAll(listGroup)
                    }
                }
                if(listInput.isNotEmpty()) listResultLan.addAll(listInput.filterNotNull())

                return listResultLan
            }
        }
        return list
    }

    // para Buscador por Categoria / Filtros(Marca y Precios) / Ordenamiento
    suspend fun createSearchProduct(order: String, orderType: String, filterList: List<SearchFilter>, numPage: Int? = null, rows: Int? = null): SearchResponse? {
        val user = userRepository.getWithCoroutines()

        val result = offerRepository.ofertasXCategoria(
            SearchRequest().apply {
                campaniaId = user?.campaing?.toInt()
                codigoZona = user?.zoneCode
                textoBusqueda = ""
                personalizacionesDummy = user?.personalizacionesDummy ?: ""
                fechaInicioFacturacion = user?.billingStartDate ?: ""
                configuracion = SearchConfiguracion().apply {
                    rdEsSuscrita = user?.isRDEsSuscrita
                    rdEsActiva = user?.isRDEsActiva
                    lider = user?.lider
                    rdActivoMdo = user?.isRDActivoMdo
                    rdTieneRDC = user?.isRDTieneRDC
                    rdTieneRDI = user?.isRDTieneRDI
                    rdTieneRDCR = user?.isRDTieneRDCR
                    diaFacturacion = user?.diaFacturacion
                    agrupaPromociones = false
                }
                paginacion = SearchPaginacion().apply {
                    numeroPagina = numPage ?: 0
                    cantidad = rows ?: 1000
                }
                orden = SearchOrden().apply {
                    campo = order
                    tipo = orderType
                }
                filtros = filterList
            }).await()

        return result
    }

    // para Upselling
    suspend fun createUpsellingOfertas(id: String, codigos: List<String>, precioCat: Double): List<Oferta?>? {
        val user = userRepository.getWithCoroutines()

        user?.let {

            return offerRepository.ofertasUpselling(
                UpSellingRequest().apply {
                    campaniaId = it.campaing?.toInt()
                    codigoProducto = codigos
                    precioCatalogo = precioCat
                    codigoZona = it.zoneCode
                    personalizacionesDummy = it.personalizacionesDummy ?: ""
                    cuv = id
                    fechaInicioFacturacion = it.billingStartDate ?: ""
                    configuracion = UpSellingConfiguracion().apply {
                        lider = it.lider
                        rdEsActiva = it.isRDEsActiva
                        rdActivoMdo = it.isRDActivoMdo
                        rdTieneRDC = it.isRDTieneRDC
                        rdTieneRDI = it.isRDTieneRDI
                        rdTieneRDCR = it.isRDTieneRDCR
                        diaFacturacion = it.diaFacturacion
                    }
                }).await()
        }

        return listOf()
    }


    suspend fun createCrossellingOfertas(tipo: String?,
                                         campaniaId: Int?,
                                         cuv: String?,
                                         fechaInicioFacturacion: String?,
                                         sugeridos: Boolean?): List<Oferta?>? {
        val user = userRepository.getWithCoroutines()

        user?.let {

            return offerRepository.ofertasCrosselling(tipo, campaniaId, cuv, fechaInicioFacturacion, sugeridos).await()
        }

        return listOf()
    }

    //para la ficha, no tocar
    suspend fun getOffersByLeverAndSap(currentCUV: String, leverType: String, sap: String,
                                       revistaDigitalSuscripcion: Int): List<Oferta> {
        val user = userRepository.getWithCoroutines()
        val configs = configuracion(user?.diaFacturacion, user?.campaing?.toInt(),
            user?.consecutivoNueva, user?.codigoPrograma, user?.regionCode, user?.zoneCode,
            user?.zoneID?.toInt(), user?.countryMoneySymbol, user?.isRDEsSuscrita, user?.isRDEsActiva)
        val config = configs?.firstOrNull { it?.tipoOferta == leverType }

        return config?.let { conf ->
            val request = OfertaRequest().apply {
                campaniaId = user?.campaing
                zonaId = user?.zoneID?.toInt()
                codigoZona = user?.zoneCode
                codigoRegion = user?.regionCode
                esSuscrita = user?.revistaDigitalSuscripcion == revistaDigitalSuscripcion
                esActiva = user?.isRDEsActiva
                tieneMG = user?.isTieneMG
                diaInicio = user?.diaFacturacion
                fechaInicioFacturacion = user?.billingStartDate
                flagComponentes = true
                flagSubCampania = false
                flagIndividual = true
            }
            val ofertas = ofertasXPalanca(conf, request)

            when (leverType) {

                OfferTypes.LAN -> {
                    ofertas?.filterNotNull()?.filter { it.codigoProducto == sap && it.cuv != currentCUV }
                }

                OfferTypes.SR, OfferTypes.ODD -> {
                    ofertas?.filterNotNull()?.filter { it.cuv != currentCUV }
                }

                else -> arrayListOf()

            }?.asSequence()?.filterNotNull()?.filter { it.esSubCampania == false }?.toList()


        }?.let { it } ?: arrayListOf()

    }

    suspend fun saveRecentOffer(offer: SearchRecentOffer, max: Int) {
        val user = userRepository.getWithCoroutines()

        user?.let {
            user?.consultantCode?.let { codigoConsultora ->
                user?.countryISO?.let { countryIso ->
                    offerRepository.saveRecentOffer(offer, codigoConsultora, countryIso, max)
                }
            }
        }
    }

    suspend fun getRecentOffers(max: Int): List<SearchRecentOffer?>? {
        val user = userRepository.getWithCoroutines()
        user?.let {
            it.consultantCode?.let { codigoConsultora ->
                it.countryISO?.let { countryIso ->
                    return offerRepository.getRecentOffers(codigoConsultora, countryIso, max)

                }
            }

        }
        return null
    }

    suspend fun deleteAllRecentOffers() {
        val user = userRepository.getWithCoroutines()
        user?.let {
            it.consultantCode?.let { codigoConsultora ->
                it.countryISO?.let { countryIso ->
                    return offerRepository.deleteAllRecentOffers(codigoConsultora, countryIso)

                }
            }
        }
    }

    suspend fun saveOfertaFinal(monto: Double) {
        val user = userRepository.getWithCoroutines()

        user?.let {
            user?.consultantCode?.let { codigoConsultora ->
                user?.countryISO?.let { countryIso ->
                    offerRepository.saveOfertaFinal(codigoConsultora, countryIso, monto)
                }
            }
        }
    }

    suspend fun getMontoInicialPedido(): Double {
        val user = userRepository.getWithCoroutines()
        var montoInicial = 0.0

        user?.let {
            user?.consultantCode?.let { codigoConsultora ->
                user?.countryISO?.let { countryIso ->
                    montoInicial = offerRepository.getMontoInicialPedido(codigoConsultora, countryIso)
                }
            }
        }
        return montoInicial
    }


    suspend fun getOfertaFinal(): Boolean {
        val user = userRepository.getWithCoroutines()
        var mostrarExperiencia = true

        user?.let {
            user?.consultantCode?.let { codigoConsultora ->
                user?.countryISO?.let { countryIso ->
                    mostrarExperiencia = offerRepository.getOfertaFinal(codigoConsultora, countryIso)
                }
            }
        }
        return mostrarExperiencia
    }

    suspend fun getEstadoPremioOfertaFinal(): Int {
        val user = userRepository.getWithCoroutines()

        user?.let { mUser ->

            val consultantCode = mUser.consultantCode
            val countryIso = mUser.countryISO

            if (consultantCode != null && countryIso != null)
                return offerRepository.getEstadoPremioOfertaFinal(consultantCode, countryIso)

        }
        return 0
    }

    suspend fun updateEstadoPremioOfertaFinal(estadoPremio: Int) {
        val user = userRepository.getWithCoroutines()

        user?.let {
            user?.consultantCode?.let { codigoConsultora ->
                user?.countryISO?.let { countryIso ->
                    offerRepository.updateEstadoPremioOfertaFinal(codigoConsultora, countryIso, estadoPremio)
                }
            }
        }
    }

    suspend fun deleteOfertaFinal() {
        val user = userRepository.getWithCoroutines()

        user?.let {
            user?.consultantCode?.let { codigoConsultora ->
                user?.countryISO?.let { countryIso ->
                    offerRepository.deleteOfertaFinal(codigoConsultora, countryIso)
                }
            }
        }
    }

    suspend fun deleteByCuv(cuv: String) {
        val user = userRepository.getWithCoroutines()
        user?.let {
            it.consultantCode?.let { codigoConsultora ->
                it.countryISO?.let { countryIso ->
                    return offerRepository.deleteByCuv(codigoConsultora, countryIso, cuv)

                }
            }
        }
    }

    suspend fun getOffersFestival(order: String, orderType: String, filterList: List<SearchFilter>): FestivalResponse? {
        val user = userRepository.getWithCoroutines()
        val starDay = 0

        return user?.let {

            val festivalRequest = FestivalRequest(it.isMostrarBuscador,
                SearchRequest().apply {
                    campaniaId = it.campaing?.toInt()
                    codigoZona = it.zoneCode
                    textoBusqueda = ""
                    personalizacionesDummy = it.personalizacionesDummy ?: ""
                    fechaInicioFacturacion = it.billingStartDate ?: ""
                    configuracion = SearchConfiguracion().apply {
                        rdEsSuscrita = it.isRDEsSuscrita
                        rdEsActiva = it.isRDEsActiva
                        lider = it.lider
                        rdActivoMdo = it.isRDActivoMdo
                        rdTieneRDC = it.isRDTieneRDC
                        rdTieneRDI = it.isRDTieneRDI
                        rdTieneRDCR = it.isRDTieneRDCR
                        diaFacturacion = it.diaFacturacion
                    }
                    paginacion = SearchPaginacion().apply {
                        numeroPagina = 0
                        cantidad = 1000
                    }
                    orden = SearchOrden().apply {
                        campo = order
                        tipo = orderType
                    }
                    filtros = filterList
                }, it.zoneID?.toInt(),
                it.regionCode,
                starDay
            )

            val festival = offerRepository.getOffersFestival(festivalRequest).data

            if (festival?.listAwards?.isNotEmpty() == true || festival?.listConditions?.isNotEmpty() == true) {
                return festival
            } else {
                throw FestivalNoOffersException()
            }
        }
    }

    suspend fun getFestivalProgress(): List<FestivalProgressResponse?>? {
        val user = userRepository.getWithCoroutines()
        return user?.let {
            return offerRepository.getFestivalProgress(it.campaing?.toInt(), it.primerNombre, it.codigoPrograma, it.consecutivoNueva, null).await()
        }
    }

    suspend fun getFestivalProgress(cuvFiltro: String?): List<FestivalProgressResponse?>? {
        val user = userRepository.getWithCoroutines()
        return user?.let {
            return offerRepository.getFestivalProgress(it.campaing?.toInt(), it.primerNombre, it.codigoPrograma, it.consecutivoNueva, cuvFiltro).await()
        }
    }

    suspend fun getOfferPromotion(cuv: String?, type: Int?): PromotionResponse? {
        val user = userRepository.getWithCoroutines()

        val promo = offerRepository.getPromotion(user?.campaing?.toInt(), cuv, type).data
        if (promo?.producto != null && promo.listaApoyo?.isNotEmpty() == true) {
            return promo
        } else {
            throw PromocionesNoOffersException()
        }
    }

    suspend fun getOfertasRecomendadas(codigoCampania: String?): List<Oferta?>? {
        return offerRepository.getOfertasRecomendadas(codigoCampania).await()
    }

}
