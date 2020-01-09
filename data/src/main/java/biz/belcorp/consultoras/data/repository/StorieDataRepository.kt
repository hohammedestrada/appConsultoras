package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.mapper.StorieEntityMapper
import biz.belcorp.consultoras.data.repository.datasource.storie.StorieDataStoreFactory
import biz.belcorp.consultoras.domain.entity.ContenidoRequest
import biz.belcorp.consultoras.domain.repository.StorieRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StorieDataRepository
@Inject internal constructor(
    private var storieDataStoreFactory: StorieDataStoreFactory,
    private var storieEntityMapper: StorieEntityMapper)
    : StorieRepository {


    override fun updateEstadoContenido(contenidoRequest: ContenidoRequest): Observable<String?> {
        val storieDataStore = storieDataStoreFactory.createCloudDataStore()
        return storieDataStore.updateStateContenido(storieEntityMapper.transform(contenidoRequest))
    }

    override suspend fun updateEstadoContenidoCoroutine(contenidoRequest: ContenidoRequest): String? {
        val storieDataStore = storieDataStoreFactory.createCloudDataStore()
        return storieDataStore.updateStateContenidoCorroutine(storieEntityMapper.transform(contenidoRequest)).await()
    }
}
