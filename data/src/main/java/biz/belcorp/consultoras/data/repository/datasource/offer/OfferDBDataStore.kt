package biz.belcorp.consultoras.data.repository.datasource.offer

import android.content.Context
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.Delete
import kotlinx.coroutines.Deferred

class OfferDBDataStore(private val context: Context) : OfferDataStore {

    override fun ordenamientos(): Deferred<List<OrdenamientoEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun configuracion(listaOferta: String?, campaniaID: Int?, consecutivoNueva: Int?,
                               codigoPrograma: String?, codigoRegion: String?, codigoZona: String?, zonaId: Int?, simbolo: String?)
        : Deferred<GanaMasConfiguracionEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun ofertasDisponibles(campaniaID: Int?, diaInicio: Int?, esSuscrita: Boolean?, esActiva: Boolean?): Deferred<List<String?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun ofertas(request: OfertaRequestEntity?): Deferred<List<OfertaEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun ofertasNoCache(request: OfertaRequestEntity?): Deferred<List<OfertaEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOfertaAtp(request: OfertaAtpRequestEntity?): Deferred<OfertaEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun categorias(): Deferred<List<CategoriaEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun categoriasInSearch(request: CategoriaRequestInSearchEntity?): Deferred<List<CategoriaEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun filtros(conHijos: Boolean?): Deferred<List<GroupFilterEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun ofertasXCategoria(request: SearchRequestEntity?): Deferred<SearchResponseEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun ofertasUpselling(request: UpSellingRequestEntity?): Deferred<List<OfertaEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getFicha(campaniaID: Int?, tipo: String?, cuv: String?, simbolo: String?,
                          fechaInicioFacturacion: String?, codigoPrograma: String?, consecutivoNueva: Int?,
                          montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                          nombreConsultora: String?, codigoRegion: String?, codigoZona: String?,
                          codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                          fechaFinFacturacion: String?): Deferred<FichaProductoEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun ofertasCrosselling(tipo: String?, campaniaID: Int?, cuv: String?, fechaInicioFacturacion: String?, sugeridos: Boolean?): Deferred<List<OfertaEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getFichaURL(request: ShareOfertaRequestEntity?): Deferred<ShareOfertaEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveRecentOffer(offer: SearchRecentOfferEntity, codigoConsultora: String, countryIso: String, max: Int) {
        val offersRest = (select from SearchRecentOfferEntity::class where (SearchRecentOfferEntity_Table.codigoConsultora notEq codigoConsultora)
            or (SearchRecentOfferEntity_Table.countryIso notEq countryIso)).queryList()

        val offers = (select from SearchRecentOfferEntity::class where (SearchRecentOfferEntity_Table.codigoConsultora eq codigoConsultora)
            and (SearchRecentOfferEntity_Table.countryIso eq countryIso)).queryList()
        val newOffers = arrayListOf<SearchRecentOfferEntity>()

        val length = offers.size

        if (max <= 0) {
            offers.clear()
        } else if (length < max) {
            newOffers.add(offer)
            val existOffer = offers.filter { it.cuv == offer.cuv }.isNotEmpty()
            if (existOffer) {
                newOffers.addAll(offers.filter { it.cuv != offer.cuv })
            } else {
                newOffers.addAll(offers)
            }
        } else if (length == max) {
            newOffers.add(offer)
            val existOffer = offers.filter { it.cuv == offer.cuv }.isNotEmpty()
            if (existOffer) {
                newOffers.addAll(offers.filter { it.cuv != offer.cuv })
            } else {
                offers.removeAt(length - 1)
                newOffers.addAll(offers)
            }
        } else {
            newOffers.add(offer)
            val existOffer = offers.filter { it.cuv == offer.cuv }.isNotEmpty()
            if (existOffer) {
                var contador = 1
                for (i in 0..length) {
                    if (contador == max)
                        break

                    if (offers[i].cuv != offer.cuv) {
                        contador++
                        newOffers.add(offers[i])
                    }
                }
            } else {
                var contador = 1
                for (i in 0..length) {
                    if (contador == max)
                        break

                    contador++
                    newOffers.add(offers[i])
                }
            }
        }

        Delete.tables(SearchRecentOfferEntity::class.java)
        FlowManager.getModelAdapter(SearchRecentOfferEntity::class.java).saveAll(newOffers)
        FlowManager.getModelAdapter(SearchRecentOfferEntity::class.java).saveAll(offersRest)
    }

    override fun getRecentOffers(codigoConsultora: String, countryIso: String, max: Int): List<SearchRecentOfferEntity> {
        val offers = (select from SearchRecentOfferEntity::class where (SearchRecentOfferEntity_Table.codigoConsultora eq codigoConsultora)
            and (SearchRecentOfferEntity_Table.countryIso eq countryIso)).queryList()
        val offersMax = arrayListOf<SearchRecentOfferEntity>()

        if (offers.size > max) {
            for (i in 1..max) {
                offersMax.add(offers[i - 1])
            }
        } else
            offersMax.addAll(offers)
        return offersMax
    }

    override fun deleteAllRecentOffers(codigoConsultora: String, countryIso: String) {
        val offersRest = (select from SearchRecentOfferEntity::class where (SearchRecentOfferEntity_Table.codigoConsultora notEq codigoConsultora)
            or (SearchRecentOfferEntity_Table.countryIso notEq countryIso)).queryList()
        Delete.tables(SearchRecentOfferEntity::class.java)
        FlowManager.getModelAdapter(SearchRecentOfferEntity::class.java).saveAll(offersRest)
    }

    override fun deleteByCuv(codigoConsultora: String, countryIso: String, cuv: String) {
        val offers = (select from SearchRecentOfferEntity::class where (SearchRecentOfferEntity_Table.codigoConsultora eq codigoConsultora)
            and (SearchRecentOfferEntity_Table.countryIso eq countryIso)).queryList()

        val offersRest = (select from SearchRecentOfferEntity::class where (SearchRecentOfferEntity_Table.codigoConsultora notEq codigoConsultora)
            or (SearchRecentOfferEntity_Table.countryIso notEq countryIso)).queryList()

        Delete.tables(SearchRecentOfferEntity::class.java)


        val newOffers = arrayListOf<SearchRecentOfferEntity>()
        offers.forEachIndexed { index, ofertaLocalEntity ->
            if (ofertaLocalEntity.cuv != cuv) {
                newOffers.add(ofertaLocalEntity)
            }
        }
        FlowManager.getModelAdapter(SearchRecentOfferEntity::class.java).saveAll(newOffers)
        FlowManager.getModelAdapter(SearchRecentOfferEntity::class.java).saveAll(offersRest)
    }

    override fun getOffersFestival(festivalRequestEntity: FestivalRequestEntity): Deferred<ServiceDto<FestivalResponseEntity?>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getFestivalProgress(campaignId: Int?, nombreConsultora: String?, codigoPrograma: String?, consecutivoNueva: Int?, cuvFiltro: String?): Deferred<List<FestivalProgressResponseEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOfertaFinalConfiguracion(campaniaID: Int?, simboloMoneda: String?): Deferred<ServiceDto<ConfiguracionPremioEntity?>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOfertasRecomendadas(codigoCampania: Int?): Deferred<List<OfertaEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }



    override fun saveOfertaFinal(ofertaFinalEstado: OfertaFinalEstadoEntity, codigoConsultora: String, countryIso: String) {
        val ofertaFinalConsultora = (select from OfertaFinalEstadoEntity::class where (OfertaFinalEstadoEntity_Table.codigoConsultora eq codigoConsultora)
            and (OfertaFinalEstadoEntity_Table.countryIso eq countryIso)).querySingle()

        ofertaFinalConsultora?.let{
            FlowManager.getModelAdapter(OfertaFinalEstadoEntity::class.java).delete(it)
        }

        FlowManager.getModelAdapter(OfertaFinalEstadoEntity::class.java).save(ofertaFinalEstado)
    }

    override fun getOfertaFinal(codigoConsultora: String, countryIso: String) : Boolean{
        val ofertaFinalConsultora = (select from OfertaFinalEstadoEntity::class where (OfertaFinalEstadoEntity_Table.codigoConsultora eq codigoConsultora)
            and (OfertaFinalEstadoEntity_Table.countryIso eq countryIso)).querySingle()

        ofertaFinalConsultora?.let{
            return true
        }

        return false
    }

    override fun deleteOfertaFinal(codigoConsultora: String, countryIso: String) {
        val ofertaFinalConsultora = (select from OfertaFinalEstadoEntity::class where (OfertaFinalEstadoEntity_Table.codigoConsultora eq codigoConsultora)
            and (OfertaFinalEstadoEntity_Table.countryIso eq countryIso)).querySingle()
        ofertaFinalConsultora?.let{
            FlowManager.getModelAdapter(OfertaFinalEstadoEntity::class.java).delete(it)
        }
    }


    override fun getMontoInicialPedido(codigoConsultora: String, countryIso: String): Double {
        val ofertaFinalConsultora = (select from OfertaFinalEstadoEntity::class where (OfertaFinalEstadoEntity_Table.codigoConsultora eq codigoConsultora)
            and (OfertaFinalEstadoEntity_Table.countryIso eq countryIso)).querySingle()

        var montoInicial = 0.0

        ofertaFinalConsultora?.let{
            montoInicial = it.montoInicial ?: 0.0
        }

        return montoInicial
    }

    override fun updateEstadoPremio(codigoConsultora: String, countryIso: String, estadoPremio: Int) {
        val ofertaFinalEstado = (select from OfertaFinalEstadoEntity::class where (OfertaFinalEstadoEntity_Table.codigoConsultora eq codigoConsultora)
            and (OfertaFinalEstadoEntity_Table.countryIso eq countryIso)).querySingle()

        ofertaFinalEstado?.let {
            it.estadoPremio = estadoPremio
            FlowManager.getModelAdapter(OfertaFinalEstadoEntity::class.java).update(it)
        }
    }

    override fun getEstadoPremio(codigoConsultora: String, countryIso: String): Int {
        val ofertaFinalEstado = (select from OfertaFinalEstadoEntity::class where (OfertaFinalEstadoEntity_Table.codigoConsultora eq codigoConsultora)
            and (OfertaFinalEstadoEntity_Table.countryIso eq countryIso)).querySingle()
        var estadoPremio : Int = 0
        ofertaFinalEstado?.estadoPremio?.let {
            estadoPremio = it
        }
        return estadoPremio
    }

    override fun getPromotion(campaniaID: Int?, cuv: String?, tipo: Int?): Deferred<ServiceDto<PromotionResponseEntity?>> {
        throw UnsupportedClassVersionError(Constant.NOT_IMPLEMENTED)
    }

}
