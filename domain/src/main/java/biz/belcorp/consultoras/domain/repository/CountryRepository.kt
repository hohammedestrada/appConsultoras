package biz.belcorp.consultoras.domain.repository

import io.reactivex.Observable
import biz.belcorp.consultoras.domain.entity.Country

/**
 * Interface de CountryRepository
 *
 * @version 1.0
 * @since 2017-04-14
 */

interface CountryRepository {
    /**
     * Metodo que obtiene el listado de paises
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun get(): Observable<List<Country?>?>

    /**
     * Metodo que obtiene el listado de paises por marca
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getByBrand(brand: Int?): Observable<List<Country?>?>

    /**
     * Metodo que busca un pais por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun find(id: Int?): Observable<Country?>

    /**
     * Metodo que busca un pais por iso
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun find(iso: String?): Observable<Country?>

    /**
     * Metodo que guarda una pais
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun save(entity: Country?): Observable<Boolean?>

    /**
     * Metodo que elimina un pais
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun delete(id: Int?): Observable<Boolean?>

}
