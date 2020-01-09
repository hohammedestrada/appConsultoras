package biz.belcorp.consultoras.data.repository.datasource.country

import biz.belcorp.consultoras.data.entity.CountryEntity
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos de una tarea
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-04-14
 */
interface CountryDataStore {

    /**
     * Metodo que obtiene el listado de paises
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(): Observable<List<CountryEntity?>?>

    /**
     * Metodo que obtiene el listado de paises por marca
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getByBrand(brand: Int?): Observable<List<CountryEntity?>?>

    /**
     * Metodo que busca una pais por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun find(id: Int?): Observable<CountryEntity?>

    /**
     * Metodo que busca una pais por iso
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun find(iso: String?): Observable<CountryEntity?>

    /**
     * Metodo que guarda una pais
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun save(entity: CountryEntity?): Observable<Boolean?>

    /**
     * Metodo que elimina una pais
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun delete(id: Int?): Observable<Boolean?>
}
