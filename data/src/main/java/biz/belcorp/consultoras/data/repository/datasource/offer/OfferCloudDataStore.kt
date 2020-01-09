package biz.belcorp.consultoras.data.repository.datasource.offer

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.net.service.IOfferService
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred
import retrofit2.http.Query

class OfferCloudDataStore(private val service: IOfferService) : OfferDataStore {


    override fun ordenamientos(): Deferred<List<OrdenamientoEntity?>?> {
        return service.listaOrden()
    }


    override fun configuracion(@Query("listaOferta") listaOferta: String?,
                               @Query("campaniaID") campaniaID: Int?,
                               @Query("consecutivoNueva") consecutivoNueva: Int?,
                               @Query("codigoPrograma") codigoPrograma: String?,
                               @Query("codigoRegion") codigoRegion: String?,
                               @Query("codigoZona") codigoZona: String?,
                               @Query("zonaId") zonaId: Int?,
                               @Query("simbolo") simbolo: String?): Deferred<GanaMasConfiguracionEntity?> {
        return service.configuracion(listaOferta, campaniaID, consecutivoNueva, codigoPrograma, codigoRegion, codigoZona, zonaId, simbolo)
    }

    override fun ofertasDisponibles(campaniaID: Int?, diaInicio: Int?, esSuscrita: Boolean?, esActiva: Boolean?): Deferred<List<String?>?> {
        return service.ofertasDisponibles(campaniaID, diaInicio, esSuscrita, esActiva)
    }

    override fun ofertas(request: OfertaRequestEntity?): Deferred<List<OfertaEntity?>?> {
        return service.ofertas(request?.tipo,
            request?.campaniaId,
            request?.zonaId,
            request?.codigoZona,
            request?.codigoRegion,
            request?.esSuscrita,
            request?.esActiva,
            request?.tieneMG,
            request?.diaInicio,
            request?.fechaInicioFacturacion,
            request?.codigoPrograma,
            request?.consecutivoNueva,
            request?.montoMaximoPedido,
            request?.consultoraNueva,
            request?.nroCampanias,
            request?.nombreConsultora,
            request?.codigoSeccion,
            request?.esUltimoDiaFacturacion,
            request?.pagoContado,
            request?.fechaFinFacturacion,
            /*INI ABT GAB-11*/
            request?.variantea,
            request?.varianteb,
            request?.variantec
            /*END ABT GAB-11*/
        )
    }

    override fun ofertasNoCache(request: OfertaRequestEntity?): Deferred<List<OfertaEntity?>?> {
        return service.ofertasNoCache(request?.tipo,
            request?.campaniaId,
            request?.zonaId,
            request?.codigoZona,
            request?.codigoRegion,
            request?.esSuscrita,
            request?.esActiva,
            request?.tieneMG,
            request?.diaInicio,
            request?.fechaInicioFacturacion,
            request?.codigoPrograma,
            request?.consecutivoNueva,
            request?.montoMaximoPedido,
            request?.consultoraNueva,
            request?.nroCampanias,
            request?.nombreConsultora,
            request?.codigoSeccion,
            request?.esUltimoDiaFacturacion,
            request?.pagoContado,
            request?.fechaFinFacturacion,
            request?.variantea,
            request?.varianteb,
            request?.variantec
            )
    }

    override fun getOfertaAtp(request: OfertaAtpRequestEntity?): Deferred<OfertaEntity?> {
        return service.getOfertaAtp(
            request?.campaniaId,
            request?.codigoPrograma,
            request?.consecutivoNueva,
            request?.montoMaximoPedido,
            request?.consultoraNueva,
            request?.nroCampanias,
            request?.codigoSeccion,
            request?.codigoRegion,
            request?.codigoZona,
            request?.zonaId)
    }

    override fun categorias(): Deferred<List<CategoriaEntity?>?> {
        return service.categorias()
    }

    override fun categoriasInSearch(request: CategoriaRequestInSearchEntity?): Deferred<List<CategoriaEntity?>?> {
        return service.categoriasInSearch(request?.campaniaId,
            request?.codigoConsultora,
            request?.codigoZona,
            request?.perzonalizaciones,
            request?.lider,
            request?.rdesSuscrita,
            request?.rdesActiva,
            request?.rdActivoModo,
            request?.rdTieneRdc,
            request?.rdTieneRdi,
            request?.rdTieneRdcr,
            request?.diaFacturacion)
    }

    override fun filtros(conHijos: Boolean?): Deferred<List<GroupFilterEntity?>?> {
        return service.filtros(conHijos)
    }

    override fun ofertasXCategoria(request: SearchRequestEntity?): Deferred<SearchResponseEntity?> {
        return service.ofertasXCategoria(request!!)
    }

    override fun ofertasUpselling(request: UpSellingRequestEntity?): Deferred<List<OfertaEntity?>?> {
        return service.ofertasUpselling(request!!)
    }

    override fun ofertasCrosselling(tipo: String?, campaniaID: Int?, cuv: String?, fechaInicioFacturacion: String?, sugeridos: Boolean?): Deferred<List<OfertaEntity?>?> {
        return service.ofertasCrosselling(tipo, campaniaID, cuv, fechaInicioFacturacion, sugeridos)
    }

    override fun getFicha(campaniaID: Int?, tipo: String?, cuv: String?, simbolo: String?,
                          fechaInicioFacturacion: String?, codigoPrograma: String?, consecutivoNueva: Int?,
                          montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                          nombreConsultora: String?, codigoRegion: String?, codigoZona: String?,
                          codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                          fechaFinFacturacion: String?): Deferred<FichaProductoEntity?> {
        return service.getFicha(campaniaID, tipo, cuv, simbolo, fechaInicioFacturacion,
            codigoPrograma, consecutivoNueva, montoMaximoPedido, consultoraNueva, nroCampanias,
            nombreConsultora, codigoRegion, codigoZona, codigoSeccion, esUltimoDiaFacturacion, pagoContado,
            fechaFinFacturacion)
    }

    override fun getFichaURL(request: ShareOfertaRequestEntity?): Deferred<ShareOfertaEntity?> {
        return service.getFichaURL(
            request?.campaniaID,
            request?.cuv,
            request?.tipoPersonalizacion,
            request?.imagenUrl,
            request?.marcaId,
            request?.nombreMarca,
            request?.nombreOferta)
    }

    override fun saveRecentOffer(offer: SearchRecentOfferEntity, codigoConsultora: String, countryIso: String, max: Int) {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getRecentOffers(codigoConsultora: String, countryIso: String, max: Int): List<SearchRecentOfferEntity> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun deleteAllRecentOffers(codigoConsultora: String, countryIso: String) {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun deleteByCuv(codigoConsultora: String, countryIso: String, cuv: String) {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOffersFestival(festivalRequestEntity: FestivalRequestEntity): Deferred<ServiceDto<FestivalResponseEntity?>> {
        return service.getOffersFestival(festivalRequestEntity)
    }

    override fun getFestivalProgress(campaignId: Int?, nombreConsultora: String?, codigoPrograma: String?, consecutivoNueva: Int?, cuvFiltro: String?): Deferred<List<FestivalProgressResponseEntity?>?> {
        return service.getFestivalProgress(campaignId, nombreConsultora, codigoPrograma, consecutivoNueva, cuvFiltro)
    }

    override fun getOfertasRecomendadas(codigoCampania: Int?): Deferred<List<OfertaEntity?>?> {
        return service.getOfertasRecomendadas(codigoCampania)
    }

    override fun getOfertaFinalConfiguracion(campaniaID: Int?, simboloMoneda: String?): Deferred<ServiceDto<ConfiguracionPremioEntity?>> {
        return service.getOfertaFinalConfiguracion(campaniaID, simboloMoneda)
    }

    override fun saveOfertaFinal(ofertaFinalEstado: OfertaFinalEstadoEntity, codigoConsultora: String, countryIso: String) {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOfertaFinal(codigoConsultora: String, countryIso: String): Boolean {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun deleteOfertaFinal(codigoConsultora: String, countryIso: String) {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getMontoInicialPedido(codigoConsultora: String, countryIso: String): Double {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun updateEstadoPremio(codigoConsultora: String, countryIso: String, estadoPremio: Int) {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getEstadoPremio(codigoConsultora: String, countryIso: String): Int {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getPromotion(campaniaID: Int?, cuv: String?, tipo: Int?): Deferred<ServiceDto<PromotionResponseEntity?>> {
        return service.getOfferPromotion(campaniaID, cuv, tipo)
    }

}
