package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.CountryEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.country.CountryDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Country
import biz.belcorp.consultoras.domain.repository.CountryRepository
import io.reactivex.Observable

/**
 * Clase de Pais encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class CountryDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param taskDataStoreFactory Clase encargada de obtener los datos
 * @param taskEntityDataMapper Clase encargade de realizar el parseo de datos
 */
@Inject
internal constructor(private val taskDataStoreFactory: CountryDataStoreFactory,
                     private val taskEntityDataMapper: CountryEntityDataMapper)
    : CountryRepository {

    /**
     * Metodo que obtiene el listado de paises
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(): Observable<List<Country?>?> {
        val dataStore = this.taskDataStoreFactory.create()
        return dataStore.get().map { this.taskEntityDataMapper.transformToDomainEntity(it) }
    }

    /**
     * Metodo que obtiene el listado de paises por marca
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getByBrand(brand: Int?): Observable<List<Country?>?> {
        val dataStore = this.taskDataStoreFactory.create()
        return dataStore.getByBrand(brand).map { this.taskEntityDataMapper.transformToDomainEntity(it) }
    }

    /**
     * Metodo que busca un pais por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun find(id: Int?): Observable<Country?> {
        val dataStore = this.taskDataStoreFactory.create()
        return dataStore.find(id).map { this.taskEntityDataMapper.transform(it) }
    }

    /**
     * Metodo que busca un pais por iso
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun find(iso: String?): Observable<Country?> {
        val dataStore = this.taskDataStoreFactory.create()
        return dataStore.find(iso).map { this.taskEntityDataMapper.transform(it) }
    }

    /**
     * Metodo que guarda un pais
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(entity: Country?): Observable<Boolean?> {
        val dataStore = this.taskDataStoreFactory.create()
        return dataStore.save(this.taskEntityDataMapper.transform(entity)!!).map { result -> result }
    }

    /**
     * Metodo que elimina un pais
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun delete(id: Int?): Observable<Boolean?> {
        val dataStore = this.taskDataStoreFactory.create()
        return dataStore.delete(id).map { result -> result }
    }

}
