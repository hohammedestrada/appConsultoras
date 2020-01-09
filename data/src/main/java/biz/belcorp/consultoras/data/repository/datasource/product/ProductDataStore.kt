package biz.belcorp.consultoras.data.repository.datasource.product

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable

/**
 * Interface que implementa los metodos de producto
 *
 * @version 1.0
 * @since 2017-11-28
 */
interface ProductDataStore {

    /**
     * Servicio de que retorna una nota por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getStockouts(data: ProductSearchRequestEntity?): Observable<List<ProductEntity?>?>

    /**
     * Servicio de que retorna una nota por id
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun saveStockouts(data: List<ProductEntity?>?): Observable<List<ProductEntity?>?>

    /**
     * Servicio que obtiene la personalización dummy de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getPersonalization(campaingId: Int?): Observable<String?>

    /**
     * Servicio de búsqueda que retorna la lista de productos filtrado por un texto
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun search(data: SearchRequestEntity?): Observable<ServiceDto<SearchResponseEntity?>?>

    /**
     * Servicio que obtiene los parametros de ordenamiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getOrderByParameters(): Observable<ServiceDto<List<SearchOrderByResponseEntity?>?>?>

    /**
     * Servicio que obtiene la lista de los regalos disponibles
     *
     * @return observable que se ejecutara en un hilo al diferente al principal
     */

    fun getlistGift(campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?): Observable<List<EstrategiaCarruselEntity?>?>


    fun autoGuardarRegalo(request:GiftSaveRequestEntity): Observable<Boolean?>
}
