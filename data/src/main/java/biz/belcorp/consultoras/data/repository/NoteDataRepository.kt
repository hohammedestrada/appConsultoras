package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.AnotacionEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.anotacion.AnotacionDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.country.CountryDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Anotacion
import biz.belcorp.consultoras.domain.repository.NoteRepository
import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class NoteDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param anotacionDataStoreFactory Clase encargada de obtener los datos
 * @param anotacionEntityDataMapper Clase encargade de realizar el parseo de datos
 */
@Inject
internal constructor(private val anotacionDataStoreFactory: AnotacionDataStoreFactory,
                     private val anotacionEntityDataMapper: AnotacionEntityDataMapper,
                     private val countryDataStoreFactory: CountryDataStoreFactory) : NoteRepository {

    override fun getById(id: Int?): Observable<Anotacion?> {
        val dataStore = anotacionDataStoreFactory.createDB()
        return dataStore[id].map { anotacionEntityDataMapper.transform(it) }
    }

    override fun listByClientLocalID(clientLocalId: Int?): Observable<Collection<Anotacion?>?> {
        val dataStore = anotacionDataStoreFactory.createDB()
        return dataStore.listByClientLocalID(clientLocalId)
            .map { anotacionEntityDataMapper.transform(it) }
    }

    override fun deleteByClientLocalID(clientLocalId: Int?): Observable<Boolean?> {
        val dataStore = anotacionDataStoreFactory.createDB()
        return dataStore.deleteByClientLocalID(clientLocalId)
    }

    override fun countByClient(countryISO: String?, clientLocalId: Int?): Observable<Boolean?> {
        val dataStore = anotacionDataStoreFactory.createDB()
        val countryDataStore = countryDataStoreFactory.createDB()
        return countryDataStore.find(countryISO)
            .flatMap {
                countryEntity -> dataStore.countByClient(countryEntity.maxNoteAmount, clientLocalId)
            }
    }
}
