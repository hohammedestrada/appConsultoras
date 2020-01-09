package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.TrackingEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.tracking.TrackingDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Tracking
import biz.belcorp.consultoras.domain.repository.TrackingRepository
import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class TrackingDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param trackingDataStoreFactory Clase encargada de obtener los datos
 */
@Inject
internal constructor(private val trackingDataStoreFactory: TrackingDataStoreFactory,
                     private val trackingEntityDataMapper: TrackingEntityDataMapper)
    : TrackingRepository {

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(top: Int?): Observable<Collection<Tracking?>?> {
        val cloudDataStore = trackingDataStoreFactory.createCloudDataStore()
        val localDataStore = trackingDataStoreFactory.createDB()
        return cloudDataStore.get(top).flatMap { r1 -> localDataStore.save(r1)
            .map { trackingEntityDataMapper.transform(it) } }
    }

    override fun getFromLocal(top: Int?): Observable<Collection<Tracking?>?> {
        val localDataStore = trackingDataStoreFactory.createDB()
        return localDataStore.get(top).map { input -> trackingEntityDataMapper.transform(input) }
    }
}
