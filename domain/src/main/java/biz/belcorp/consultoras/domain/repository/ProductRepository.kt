package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import io.reactivex.Observable


interface ProductRepository {

    /**
     * Metodo que obtiene la lista de productos agotados
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getStockouts(request: ProductSearchRequest?): Observable<Collection<Product?>?>

    /**
     * Metodo que obtiene la lista de productos agotados
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getStockoutsLocal(request: ProductSearchRequest?): Observable<Collection<Product?>?>

    /**
     * Metodo que obtiene la personalización dummy de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getPersonalization(campaingId: Int): Observable<String?>

    /**
     * Metodo de búsqueda de productos a través de texto
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun search(request: SearchRequest?): Observable<BasicDto<SearchResponse?>>
    /**
     * Obtener parametros de ordenamiento para la lista
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getOrderByParameters(): Observable<BasicDto<Collection<SearchOrderByResponse?>?>?>

    /**
     * Metodo para retornar la lista de regalos disponibles.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    fun getListGift(campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?/*, autoGuardado: Boolean*/): Observable<Collection<EstrategiaCarrusel?>?>

    fun autoGuardoDelRegalo(request: GiftAutoSaverRequest): Observable<Boolean?>
}
