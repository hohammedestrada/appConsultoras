package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.*

import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IProductService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Query

/**
 * Servicio de Producto
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-11-28
 */

class ProductService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 * @param accessToken Token de la session
 * @param appName Nombre del app
 * @param appCountry Pais de conexion
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IProductService {

    private val service: IProductService = RestApi.create(IProductService::class.java,
        accessToken, appName, appCountry)

    /**
     * Metodo que obtiene el listado de productos agotados
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getStockouts(@Query(value = "campania") campaing: Int?,
                              @Query(value = "zonaId") zoneId: Int?,
                              @Query(value = "cuv") cuv: String?,
                              @Query(value = "descripcion") description: String?)
        : Observable<List<ProductEntity?>?> {

        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getStockouts(campaing, zoneId, cuv, description)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene la personalización de la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getPersonalization(campaniaId: Int?): Observable<String?> {

        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getPersonalization(campaniaId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene el listado de productos filtrado a través del buscador
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun search(searchRequest: SearchRequestEntity): Observable<ServiceDto<SearchResponseEntity?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.search(searchRequest)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    /**
     * Metodo que obtiene los parametros para el ordenamiento
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getOrderByParameters(): Observable<ServiceDto<List<SearchOrderByResponseEntity?>?>?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getOrderByParameters()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun listaRegalos(campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?/*, AgregaRegalo: Boolean?*/):
        Observable<List<EstrategiaCarruselEntity?>?> {
        return Observable.create { emitter ->

            if (isThereInternetConnection) {
                service.listaRegalos(campaniaID, nroCampanias, codigoPrograma, consecutivoNueva/*, AgregaRegalo*/)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            it?.let { it1 ->
                                emitter.onNext(it1)
                            } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                        { error -> emitter.onError(getError(error)) },
                        { emitter.onComplete() })
            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun autoSaveGift(requestEntity: GiftSaveRequestEntity): Observable<Boolean?> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                service.autoSaveGift(requestEntity)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        it?.let { it1 ->
                            emitter.onNext(it1)
                        } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                    },
                        { error -> emitter.onError(getError(error)) },
                        { emitter.onComplete() })
            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

}
