package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import kotlinx.coroutines.Deferred

interface OfferRepository {

    suspend fun ordenamientos(): Deferred<List<Ordenamiento?>?>

    suspend fun configuracion(listaOferta: String?, campaniaID: Int?, consecutivoNueva : Int?,
                              codigoPrograma: String?, codigoRegion: String?, codigoZona: String?, zonaId: Int?, simbolo: String?): Deferred<List<ConfiguracionPorPalanca?>?>

    suspend fun ofertasDisponibles(campaniaID: Int?, diaInicio: Int?, esSuscrita: Boolean?, esActiva: Boolean?): Deferred<List<String?>?>

    suspend fun ofertas(request: OfertaRequest?): Deferred<List<Oferta?>?>

    suspend fun ofertasNoCache(request: OfertaRequest?): Deferred<List<Oferta?>?>

    suspend fun getOfertaAtp(request: OfertaAtpRequest?) : Deferred<Oferta?>

    suspend fun getFicha(campaniaID: Int?, tipo: String?, cuv: String?, simbolo: String?,
                         fechaInicioFacturacion: String?, codigoPrograma: String?, consecutivoNueva: Int?,
                         montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                         nombreConsultora: String?, codigoRegion: String?, codigoZona: String?,
                         codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                         fechaFinFacturacion: String?): Deferred<Oferta?>

    suspend fun getFichaShareURL(request: ShareOfertaRequest?) : Deferred<String?>

    suspend fun categorias(): Deferred<List<Categoria?>?>

    suspend fun categoriasInSearch(request: CategoriaRequestInSearch): Deferred<List<Categoria?>?>

    suspend fun filtros(conHijos: Boolean?): Deferred<List<GroupFilter?>?>

    suspend fun ofertasXCategoria(request: SearchRequest?) : Deferred<SearchResponse?>

    suspend fun ofertasUpselling(request: UpSellingRequest?) : Deferred<List<Oferta?>?>

    suspend fun ofertasCrosselling(tipo: String?, campaniaID: Int?, cuv: String?,
                                   fechaInicioFacturacion: String?, sugeridos: Boolean?) : Deferred<List<Oferta?>?>

    suspend fun saveRecentOffer(offer: SearchRecentOffer, codigoConsultora: String, countryIso: String, max: Int)

    suspend fun getRecentOffers(codigoConsultora: String, countryIso: String, max: Int): List<SearchRecentOffer?>?

    suspend fun deleteAllRecentOffers(codigoConsultora: String, countryIso: String)

    suspend fun deleteByCuv(codigoConsultora: String, countryIso: String, cuv: String)

    suspend fun getOffersFestival(festivalRequest: FestivalRequest) : BasicDto<FestivalResponse?>

    suspend fun getFestivalProgress(campaignId: Int?, nombreConsultora: String?, codigoPrograma: String?, consecutivoNueva: Int?, cuvFiltro: String?) : Deferred<List<FestivalProgressResponse?>?>

    suspend fun getPromotion(campaniaID: Int?, cuv: String?, tipo: Int?): BasicDto<PromotionResponse?>

    suspend fun getOfertaFinalConfiguracion(campaignId: Int?, simboloMoneda: String?) : Deferred<ConfiguracionPremio?>

    suspend fun getOfertasRecomendadas(codigoCampania: String?): Deferred<List<Oferta?>?>

    suspend fun saveOfertaFinal(codigoConsultora: String, countryIso: String, monto: Double)

    suspend fun getOfertaFinal(codigoConsultora: String, countryIso: String): Boolean

    suspend fun deleteOfertaFinal(codigoConsultora: String, countryIso: String)

    suspend fun getMontoInicialPedido(codigoConsultora: String, countryIso: String): Double

    suspend fun updateEstadoPremioOfertaFinal(codigoConsultora: String, countryIso: String, estadoPremio: Int)

    suspend fun getEstadoPremioOfertaFinal(codigoConsultora: String, countryIso: String) : Int

}
