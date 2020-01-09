package biz.belcorp.consultoras.data.repository.datasource.offer

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred

interface OfferDataStore {

    fun ordenamientos(): Deferred<List<OrdenamientoEntity?>?>

    fun configuracion(listaOferta: String?, campaniaID: Int?, consecutivoNueva : Int?,
                      codigoPrograma: String?, codigoRegion: String?, codigoZona: String?, zonaId: Int?, simbolo: String?): Deferred<GanaMasConfiguracionEntity?>

    fun ofertasDisponibles(campaniaID: Int?, diaInicio: Int?, esSuscrita: Boolean?, esActiva: Boolean?): Deferred<List<String?>?>

    fun ofertas(request: OfertaRequestEntity?): Deferred<List<OfertaEntity?>?>

    fun ofertasNoCache(request: OfertaRequestEntity?): Deferred<List<OfertaEntity?>?>

    fun getOfertaAtp(request: OfertaAtpRequestEntity?) : Deferred<OfertaEntity?>

    fun categorias() : Deferred<List<CategoriaEntity?>?>

    fun categoriasInSearch(request: CategoriaRequestInSearchEntity?) : Deferred<List<CategoriaEntity?>?>

    fun filtros(conHijos: Boolean?) : Deferred<List<GroupFilterEntity?>?>

    fun ofertasXCategoria(request: SearchRequestEntity?): Deferred<SearchResponseEntity?>

    fun ofertasUpselling(request: UpSellingRequestEntity?): Deferred<List<OfertaEntity?>?>

    fun ofertasCrosselling(tipo: String?, campaniaID: Int?, cuv: String?, fechaInicioFacturacion: String?, sugeridos: Boolean?): Deferred<List<OfertaEntity?>?>

    fun getFicha(campaniaID: Int?, tipo: String?, cuv: String?, simbolo: String?,
                 fechaInicioFacturacion: String?, codigoPrograma: String?, consecutivoNueva: Int?,
                 montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                 nombreConsultora: String?, codigoRegion: String?, codigoZona: String?,
                 codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                 fechaFinFacturacion: String?): Deferred<FichaProductoEntity?>

    fun getFichaURL(request: ShareOfertaRequestEntity?): Deferred<ShareOfertaEntity?>

    fun saveRecentOffer(offer: SearchRecentOfferEntity, codigoConsultora: String, countryIso: String, max: Int)

    fun getRecentOffers(codigoConsultora: String, countryIso: String, max: Int): List<SearchRecentOfferEntity>

    fun deleteAllRecentOffers(codigoConsultora: String, countryIso: String)

    fun deleteByCuv(codigoConsultora: String, countryIso: String, cuv: String)

    fun getOffersFestival(festivalRequestEntity: FestivalRequestEntity): Deferred<ServiceDto<FestivalResponseEntity?>>

    fun getFestivalProgress(campaignId: Int?, nombreConsultora: String?, codigoPrograma: String?, consecutivoNueva: Int?, cuvFiltro: String?): Deferred<List<FestivalProgressResponseEntity?>?>

    fun getOfertaFinalConfiguracion(campaniaID: Int?, simboloMoneda :String?) : Deferred<ServiceDto<ConfiguracionPremioEntity?>>

    fun getOfertasRecomendadas(codigoCampania: Int?): Deferred<List<OfertaEntity?>?>

    fun saveOfertaFinal(ofertaFinalEstado: OfertaFinalEstadoEntity, codigoConsultora: String, countryIso: String)

    fun getOfertaFinal(codigoConsultora: String, countryIso: String): Boolean

    fun deleteOfertaFinal(codigoConsultora: String, countryIso: String)

    fun getMontoInicialPedido(codigoConsultora: String, countryIso: String): Double

    fun updateEstadoPremio(codigoConsultora: String, countryIso: String, estadoPremio: Int)

    fun getEstadoPremio(codigoConsultora: String, countryIso: String) : Int

    fun getPromotion(campaniaID: Int?, cuv: String?, tipo: Int?): Deferred<ServiceDto<PromotionResponseEntity?>>

}
