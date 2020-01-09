package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IOfferService
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred


class OfferService(context: Context, accessToken: AccessToken?, appName: String?,
                   appCountry: String?)
    : BaseService(context), IOfferService {


    override fun listaOrden(): Deferred<List<OrdenamientoEntity?>?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.listaOrden()
    }

    private val service: IOfferService = RestApi.createWithCache(context, IOfferService::class.java, accessToken, appName, appCountry)

    private val serviceNoCache: IOfferService = RestApi.create(IOfferService::class.java, accessToken, appName, appCountry)

    private val sessionManager = SessionManager.getInstance(context)

    override fun configuracion(listaOferta: String?, campaniaID: Int?, consecutivoNueva: Int?,
                               codigoPrograma: String?, codigoRegion: String?, codigoZona: String?,
                               zonaId: Int?, simbolo: String?)
        : Deferred<GanaMasConfiguracionEntity?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.configuracion(listaOferta, campaniaID, consecutivoNueva, codigoPrograma, codigoRegion, codigoZona, zonaId, simbolo)
    }

    override fun ofertasDisponibles(campaniaID: Int?, diaInicio: Int?, esSuscrita: Boolean?,
                                    esActiva: Boolean?): Deferred<List<String?>?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.ofertasDisponibles(campaniaID, diaInicio, esSuscrita, esActiva)
    }

    override fun categorias(): Deferred<List<CategoriaEntity?>?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.categorias()
    }

    override fun categoriasInSearch(campaniaId: String?,
                                    codigoConsultora: String?,
                                    codigoZona: String?,
                                    perzonalizaciones: String?,
                                    lider: Int?,
                                    rdesSuscrita: Boolean?,
                                    rdesActiva: Boolean?,
                                    rdActivoModo: Boolean?,
                                    rdTieneRdc: Boolean?,
                                    rdTieneRdi: Boolean?,
                                    rdTieneRdcr: Boolean?,
                                    diaFacturacion: Int?): Deferred<List<CategoriaEntity?>?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.categoriasInSearch(campaniaId, codigoConsultora, codigoZona, perzonalizaciones, lider, rdesSuscrita, rdesActiva, rdActivoModo, rdTieneRdc, rdTieneRdi, rdTieneRdcr, diaFacturacion)
    }

    override fun filtros(conHijos: Boolean?): Deferred<List<GroupFilterEntity?>?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.filtros(conHijos)
    }

    override fun ofertasXCategoria(searchRequest: SearchRequestEntity): Deferred<SearchResponseEntity?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.ofertasXCategoria(searchRequest)
    }

    override fun ofertasUpselling(upSellingRequest: UpSellingRequestEntity): Deferred<List<OfertaEntity?>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.ofertasUpselling(upSellingRequest)
    }

    override fun ofertasCrosselling(
        tipo: String?,
        campaniaId: Int?,
        cuv: String?,
        fechaInicioFacturacion: String?,
        sugeridos: Boolean?
    ): Deferred<List<OfertaEntity?>?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.ofertasCrosselling(tipo, campaniaId, cuv, fechaInicioFacturacion, sugeridos)
    }

    override fun ofertas(
        tipo: String?,
        campaniaId: String?,
        zonaId: Int?,
        codigoZona: String?,
        codigoRegion: String?,
        esSuscrita: Boolean?,
        esActiva: Boolean?,
        tieneMG: Boolean?,
        diaInicio: Int?,
        fechaInicioFacturacion: String?,
        codigoPrograma: String?,
        consecutivoNueva: Int?,
        montoMaximoPedido: Double?,
        consultoraNueva: Int?,
        nroCampanias: Int?,
        nombreConsultora: String?,
        codigoSeccion: String?,
        esUltimoDiaFacturacion: Boolean?,
        pagoContado: Boolean?,
        fechaFinFacturacion: String?,
        /*INI ABT GAB-11*/
        variantea: Boolean?,
        varianteb: Boolean?,
        variantec: Boolean?
        /*END ABT GAB-11*/
    ): Deferred<List<OfertaEntity?>?> {
        if (!isThereInternetConnection && sessionManager.getApiCacheEnabled() == false)
            throw NetworkErrorException()
        else
            return service.ofertas(tipo, campaniaId, zonaId, codigoZona, codigoRegion, esSuscrita
                , esActiva, tieneMG, diaInicio, fechaInicioFacturacion, codigoPrograma, consecutivoNueva,
                montoMaximoPedido, consultoraNueva, nroCampanias, nombreConsultora, codigoSeccion,
                esUltimoDiaFacturacion, pagoContado, fechaFinFacturacion,
                /*INI ATB GAB-11*/
                variantea,
                varianteb,
                variantec
                /*INI ATB GAB-11*/
            )
    }

    override fun ofertasNoCache(tipo: String?, campaniaId: String?, zonaId: Int?, codigoZona: String?,
                                codigoRegion: String?, esSuscrita: Boolean?, esActiva: Boolean?,
                                tieneMG: Boolean?, diaInicio: Int?, fechaInicioFacturacion: String?,
                                codigoPrograma: String?, consecutivoNueva: Int?, montoMaximoPedido: Double?,
                                consultoraNueva: Int?, nroCampanias: Int?, nombreConsultora: String?,
                                codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                                fechaFinFacturacion: String?,

        /*INI ABT GAB-11*/
                                variantea: Boolean?,
                                varianteb: Boolean?,
                                variantec: Boolean?
        /*END ABT GAB-11*/

    ): Deferred<List<OfertaEntity?>?> {
        return serviceNoCache.ofertas(tipo, campaniaId, zonaId, codigoZona, codigoRegion, esSuscrita
            , esActiva, tieneMG, diaInicio, fechaInicioFacturacion, codigoPrograma, consecutivoNueva,
            montoMaximoPedido, consultoraNueva, nroCampanias, nombreConsultora, codigoSeccion,
            esUltimoDiaFacturacion, pagoContado, fechaFinFacturacion,

            /*INI ABT GAB-11*/
            variantea,
            varianteb,
            variantec
            /*END ABT GAB-11*/
        )
    }

    override fun getOfertaAtp(
        campaniaId: Int?,
        codigoPrograma: String?,
        consecutivoNueva: Int?,
        montoMaximoPedido: Double?,
        consultoraNueva: Int?,
        nroCampanias: Int?,
        codigoSeccion: String?,
        codigoRegion: String?,
        codigoZona: String?,
        zonaId: Int?
    ): Deferred<OfertaEntity?> {
        return serviceNoCache.getOfertaAtp(
            campaniaId,
            codigoPrograma,
            consecutivoNueva,
            montoMaximoPedido,
            consultoraNueva,
            nroCampanias,
            codigoSeccion,
            codigoRegion,
            codigoZona,
            zonaId
        )
    }

    override fun getFicha(campaniaId: Int?, tipo: String?, cuv: String?, simbolo: String?,
                          fechaInicioFacturacion: String?, codigoPrograma: String?, consecutivoNueva: Int?,
                          montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                          nombreConsultora: String?, codigoRegion: String?, codigoZona: String?,
                          codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                          fechaFinFacturacion: String?)
        : Deferred<FichaProductoEntity?> {
        return if (!isThereInternetConnection)
            throw NetworkErrorException()
        else if (OFFER_SERVICE_ONLINE)
            serviceNoCache.getFicha(campaniaId, tipo, cuv, simbolo, fechaInicioFacturacion,
                codigoPrograma, consecutivoNueva, montoMaximoPedido, consultoraNueva, nroCampanias,
                nombreConsultora, codigoRegion, codigoZona, codigoSeccion, esUltimoDiaFacturacion,
                pagoContado, fechaFinFacturacion)
        else
            service.getFicha(campaniaId, tipo, cuv, simbolo, fechaInicioFacturacion,
                codigoPrograma, consecutivoNueva, montoMaximoPedido, consultoraNueva, nroCampanias,
                nombreConsultora, codigoRegion, codigoZona, codigoSeccion, esUltimoDiaFacturacion,
                pagoContado, fechaFinFacturacion)

    }

    override fun getFichaURL(campaniaID: Int?, cuv: String?, tipoPersonalizacion: String?,
                             imagenUrl: String?, marcaId: Int?, nombreMarca: String?,
                             nombreOferta: String?): Deferred<ShareOfertaEntity?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.getFichaURL(campaniaID, cuv, tipoPersonalizacion, imagenUrl, marcaId, nombreMarca, nombreOferta)
    }

    override fun getOffersFestival(festivalRequestEntity: FestivalRequestEntity): Deferred<ServiceDto<FestivalResponseEntity?>> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return serviceNoCache.getOffersFestival(festivalRequestEntity)
    }


    override fun getFestivalProgress(campaignId: Int?, nombreConsultora: String?, codigoPrograma: String?, consecutivoNueva: Int?, cuvFiltro: String?): Deferred<List<FestivalProgressResponseEntity?>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return serviceNoCache.getFestivalProgress(campaignId, nombreConsultora, codigoPrograma, consecutivoNueva, cuvFiltro)
    }


    /**
     * OFERTA FINAL
     */
    override fun getOfertaFinalConfiguracion(campaignId: Int?, simboloMoneda: String?): Deferred<ServiceDto<ConfiguracionPremioEntity?>> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return serviceNoCache.getOfertaFinalConfiguracion(campaignId, simboloMoneda)
    }

    override fun getOfertasRecomendadas(codigoCampania: Int?): Deferred<List<OfertaEntity?>?> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return serviceNoCache.getOfertasRecomendadas(codigoCampania)
    }

    companion object {
        var OFFER_SERVICE_ONLINE: Boolean = false
    }

    override fun getOfferPromotion(campaniaID: Int?, cuv: String?, tipo: Int?): Deferred<ServiceDto<PromotionResponseEntity?>> {
        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return serviceNoCache.getOfferPromotion(campaniaID, cuv, tipo)
    }

}
