package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.repository.datasource.offer.OfferDataStoreFactory
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.repository.OfferRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfferDataRepository @Inject
internal constructor(private val offerDataStoreFactory: OfferDataStoreFactory)
    : OfferRepository {

    override suspend fun ordenamientos(): Deferred<List<Ordenamiento?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { OrdenamientoEntity.transformList(cloudDataStore
                .ordenamientos().await()) }
        }

    }

    override suspend fun configuracion(listaOferta: String?, campaniaID: Int?,
                                       consecutivoNueva : Int?, codigoPrograma: String?, codigoRegion: String?, codigoZona: String?, zonaId: Int?, simbolo: String?)
        : Deferred<List<ConfiguracionPorPalanca?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async {
                GanaMasConfiguracionEntity.transform(cloudDataStore
                    .configuracion(listaOferta, campaniaID, consecutivoNueva, codigoPrograma, codigoRegion, codigoZona, zonaId, simbolo).await()) }
        }
    }

    override suspend fun ofertasDisponibles(campaniaID: Int?, diaInicio: Int?, esSuscrita: Boolean?, esActiva: Boolean?)
        : Deferred<List<String?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async {
                cloudDataStore.ofertasDisponibles(campaniaID, diaInicio, esSuscrita, esActiva).await()
            }
        }
    }

    override suspend fun ofertas(request: OfertaRequest?): Deferred<List<Oferta?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async {
                OfertaEntity.transformList(cloudDataStore
                    .ofertas(OfertaRequestEntity.transform(request)).await())
            }
        }
    }

    override suspend fun ofertasNoCache(request: OfertaRequest?): Deferred<List<Oferta?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { OfertaEntity.transformList(cloudDataStore
                .ofertasNoCache(OfertaRequestEntity.transform(request)).await()) }
        }
    }

    override suspend fun getOfertaAtp(request: OfertaAtpRequest?): Deferred<Oferta?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { OfertaEntity.transform(cloudDataStore
                .getOfertaAtp(OfertaAtpRequestEntity.transform(request)).await()) }
        }

    }

    override suspend fun categorias(): Deferred<List<Categoria?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { CategoriaEntity.transformList(cloudDataStore.categorias().await()) }
        }
    }

    override suspend fun categoriasInSearch(request: CategoriaRequestInSearch): Deferred<List<Categoria?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { CategoriaEntity.transformList(cloudDataStore.categoriasInSearch(CategoriaRequestInSearchEntity.transform(request)).await()) }
        }
    }

    override suspend fun filtros(conHijos: Boolean?): Deferred<List<GroupFilter?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { GroupFilterEntity.transformList(cloudDataStore.filtros(conHijos).await()) }
        }
    }

    override suspend fun ofertasXCategoria(request: SearchRequest?): Deferred<SearchResponse?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { SearchResponseEntity.transform(cloudDataStore.ofertasXCategoria(SearchRequestEntity.transform(request)).await()) }
        }
    }

    override suspend fun ofertasUpselling(request: UpSellingRequest?): Deferred<List<Oferta?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { OfertaEntity.transformList(cloudDataStore.ofertasUpselling(UpSellingRequestEntity.transform(request)).await())}
        }
    }

    override suspend fun ofertasCrosselling(tipo: String?, campaniaID: Int?, cuv: String?, fechaInicioFacturacion: String?, sugeridos: Boolean?): Deferred<List<Oferta?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { OfertaEntity.transformList(cloudDataStore.ofertasCrosselling(tipo, campaniaID, cuv, fechaInicioFacturacion, sugeridos).await())}
        }
    }
    override suspend fun getFicha(campaniaID: Int?, tipo: String?, cuv: String?, simbolo: String?,
                                  fechaInicioFacturacion: String?, codigoPrograma: String?, consecutivoNueva: Int?,
                                  montoMaximoPedido: Double?, consultoraNueva: Int?, nroCampanias: Int?,
                                  nombreConsultora: String?, codigoRegion: String?, codigoZona: String?,
                                  codigoSeccion: String?, esUltimoDiaFacturacion: Boolean?, pagoContado: Boolean?,
                                  fechaFinFacturacion: String?): Deferred<Oferta?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { OfertaEntity.transform(cloudDataStore.getFicha(campaniaID, tipo, cuv, simbolo,
                fechaInicioFacturacion, codigoPrograma, consecutivoNueva,
                montoMaximoPedido, consultoraNueva, nroCampanias,
                nombreConsultora, codigoRegion, codigoZona,
                codigoSeccion, esUltimoDiaFacturacion, pagoContado,
                fechaFinFacturacion).await()?.data) }
        }
    }

    override suspend fun getFichaShareURL(request: ShareOfertaRequest?): Deferred<String?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async { ShareOfertaEntity.getUrl(cloudDataStore
                .getFichaURL(ShareOfertaRequestEntity.transform(request)).await()) }
        }
    }

    override suspend fun saveRecentOffer(oferta: SearchRecentOffer, codigoConsultora: String, countryIso: String, max: Int) {
        val localDataStore = offerDataStoreFactory.createDB()
        localDataStore.saveRecentOffer(SearchRecentOfferEntity.transform(oferta, codigoConsultora, countryIso), codigoConsultora, countryIso, max)

    }

    override suspend fun getRecentOffers(codigoConsultora: String, countryIso: String, max: Int): List<SearchRecentOffer?>? {
        val localDataStore = offerDataStoreFactory.createDB()
        return SearchRecentOfferEntity.transformList(localDataStore.getRecentOffers(codigoConsultora, countryIso, max))
    }

    override suspend fun getOffersFestival(festivalRequest: FestivalRequest): BasicDto<FestivalResponse?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return FestivalResponseEntity.transformFestival(cloudDataStore.getOffersFestival(FestivalRequestEntity.transformToEntity(festivalRequest)).await())
    }

    override suspend fun getFestivalProgress(campaignId: Int?, nombreConsultora: String?, codigoPrograma: String?, consecutivoNueva: Int?, cuvFiltro: String?): Deferred<List<FestivalProgressResponse?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async {  FestivalProgressResponseEntity.transformList(cloudDataStore.getFestivalProgress(campaignId, nombreConsultora, codigoPrograma, consecutivoNueva, cuvFiltro).await()) }
        }
    }

    override suspend fun deleteAllRecentOffers(codigoConsultora: String, countryIso: String) {
        val localDataStore = offerDataStoreFactory.createDB()
        localDataStore.deleteAllRecentOffers(codigoConsultora, countryIso)
    }

    override suspend fun deleteByCuv(codigoConsultora: String, countryIso: String, cuv: String) {
        val localDataStore = offerDataStoreFactory.createDB()
        localDataStore.deleteByCuv(codigoConsultora, countryIso, cuv)
    }

    override suspend fun getOfertasRecomendadas(codigoCampania: String?): Deferred<List<Oferta?>?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope { async {
            OfertaEntity.transformList(cloudDataStore
                .getOfertasRecomendadas(codigoCampania?.toInt()).await())
        } }
    }

    override suspend fun getOfertaFinalConfiguracion(campaignId: Int?, simboloMoneda: String?): Deferred<ConfiguracionPremio?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return coroutineScope {
            async {
                ConfiguracionPremioEntity.transform(cloudDataStore.getOfertaFinalConfiguracion(campaignId, simboloMoneda).await().data)
            }
        }
    }

    override suspend fun saveOfertaFinal(codigoConsultora: String, countryIso: String, monto: Double) {
        val localDataStore = offerDataStoreFactory.createDB()
        localDataStore.saveOfertaFinal(OfertaFinalEstadoEntity.transform(codigoConsultora, countryIso, monto, 0), codigoConsultora, countryIso)
    }

    override suspend fun getOfertaFinal(codigoConsultora: String, countryIso: String): Boolean {
        val localDataStore = offerDataStoreFactory.createDB()
        return localDataStore.getOfertaFinal(codigoConsultora, countryIso)
    }

    override suspend fun deleteOfertaFinal(codigoConsultora: String, countryIso: String) {
        val localDataStore = offerDataStoreFactory.createDB()
        return localDataStore.deleteOfertaFinal(codigoConsultora, countryIso)

    }

    override suspend fun getMontoInicialPedido(codigoConsultora: String, countryIso: String): Double {
        val localDataStore = offerDataStoreFactory.createDB()
        return localDataStore.getMontoInicialPedido(codigoConsultora, countryIso)
    }

    override suspend fun updateEstadoPremioOfertaFinal(codigoConsultora: String, countryIso: String, estadoPremio: Int) {
        val localDataStore = offerDataStoreFactory.createDB()
        return localDataStore.updateEstadoPremio(codigoConsultora, countryIso, estadoPremio)
    }

    override suspend fun getEstadoPremioOfertaFinal(codigoConsultora: String, countryIso: String): Int {
        val localDataStore = offerDataStoreFactory.createDB()
        return localDataStore.getEstadoPremio(codigoConsultora, countryIso)
    }

    override suspend fun getPromotion(campaniaID: Int?, cuv: String?, tipo: Int?): BasicDto<PromotionResponse?> {
        val cloudDataStore = offerDataStoreFactory.createCloud()
        return PromotionResponseEntity.transformPromotion(cloudDataStore.getPromotion(campaniaID, cuv, tipo).await())
    }

}

