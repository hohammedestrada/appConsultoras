package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import javax.inject.Inject

import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.ProductRepository
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver

/**
 * Clase que define los casos de uso de Producto
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-11-28
 */
class ProductUseCase @Inject
constructor(threadExecutor: ThreadExecutor
            , postExecutionThread: PostExecutionThread
            , private val productRepository: ProductRepository
            , private val authRepository: AuthRepository) : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Obtiene la lista de productos agotados.
     */
    fun getStockouts(params: ProductSearchRequest, observer: DisposableObserver<Collection<Product?>?>) {
        execute(this.productRepository.getStockouts(params)
            .onErrorResumeNext(askForExceptions(params)), observer)
    }

    /** */

    private fun askForExceptions(params: ProductSearchRequest): Function<Throwable, Observable<Collection<Product?>?>> {
        return Function { t ->
            when (t) {
                is NetworkErrorException -> this.productRepository.getStockoutsLocal(params)
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.productRepository.getStockouts(params) }
                    .onErrorResumeNext(askForExceptions(params))
                else -> Observable.error(t)
            }
        }
    }

    private fun askForExceptionListProduct(campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?)
        : Function<Throwable, Observable<Collection<EstrategiaCarrusel?>?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.productRepository.getListGift(campaniaID, nroCampanias, codigoPrograma, consecutivoNueva) }
                    .onErrorResumeNext(askForExceptionListProduct(campaniaID, nroCampanias, codigoPrograma, consecutivoNueva))
                else -> Observable.error(t)
            }
        }
    }


    private fun askForException(request: GiftAutoSaverRequest): Function<Throwable, Observable<Boolean?>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken().flatMap {
                    this.productRepository.autoGuardoDelRegalo(request)
                }.onErrorResumeNext(askForException(request))

                else -> Observable.error(t)
            }

        }
    }

    /**
     * Búscador de productos por filtro de texto.
     *
     */
    fun search(params: SearchRequest, observer: DisposableObserver<BasicDto<SearchResponse?>>) {
        execute(this.productRepository.search(params)
            .onErrorResumeNext(askForSearchExceptions(params)), observer)
    }

    /**
     * Parametros a mostrar en la opcion de ordenamiento
     *
     */
    fun getOrderByParameters(observer: DisposableObserver<BasicDto<Collection<SearchOrderByResponse?>?>?>) {
        execute(this.productRepository.getOrderByParameters()
            .onErrorResumeNext(askForSearchOrderByExceptions()), observer)
    }

    fun getListProductGift(observer: DisposableObserver<Collection<EstrategiaCarrusel?>?>, campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?) {

        execute(this.productRepository.getListGift(campaniaID, nroCampanias, codigoPrograma, consecutivoNueva)
            .onErrorResumeNext(askForExceptionListProduct(campaniaID, nroCampanias, codigoPrograma, consecutivoNueva/*, autoGuardado*/)), observer)

    }

    fun saveAutoGift(observer: DisposableObserver<Boolean?>,request: GiftAutoSaverRequest) {
        execute(
            this.productRepository.autoGuardoDelRegalo(request)
                .onErrorResumeNext(askForException(request)), observer
        )
    }

    /** */

    private fun askForSearchExceptions(params: SearchRequest): Function<Throwable,
        Observable<BasicDto<SearchResponse?>>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.productRepository.search(params) }
                else -> Observable.error(t)
            }
        }
    }

    private fun askForSearchOrderByExceptions(): Function<Throwable,
        Observable<BasicDto<Collection<SearchOrderByResponse?>?>>> {
        return Function { t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap { this.productRepository.getOrderByParameters() }
                else -> Observable.error(t)
            }
        }
    }

}
