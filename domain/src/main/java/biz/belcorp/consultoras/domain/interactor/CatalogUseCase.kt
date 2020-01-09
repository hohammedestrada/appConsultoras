package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.CatalogoWrapper
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.CatalogRepository
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 *
 */
class CatalogUseCase
/**
 * Constructor del caso de uso
 *
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 * @param catalogRepository   Repositorio para catalogo
 * @param authRepository      Repositorio para Auth
 */
@Inject
constructor(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread,
            private val catalogRepository: CatalogRepository
            , private val authRepository: AuthRepository) : UseCase(threadExecutor, postExecutionThread) {

    operator fun get(user: User?, maximumCampaign: Int?, observer: BaseObserver<List<CatalogoWrapper?>?>) {
        val observableService = this.catalogRepository[user, maximumCampaign]
        execute(observableService
                .onErrorResumeNext(askForExceptions(user, maximumCampaign)), observer)
    }

    suspend fun getCorroutine(user: User?, maximumCampaign: Int?, mostrarSiguieteAnterior: Boolean = true ):List<CatalogoWrapper?>?{
        return catalogRepository.getCorroutine(user, maximumCampaign,mostrarSiguieteAnterior)
    }

    private fun askForExceptions(user: User?, maximumCampaign: Int?)
        : Function<Throwable, Observable<List<CatalogoWrapper?>?>> {
        return Function{ t ->
            when (t) {
                is NetworkErrorException -> this.catalogRepository
                    .getLocal(user, maximumCampaign)
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap {
                        this.catalogRepository[user, maximumCampaign]
                    }
                else -> Observable.error(t)
            }
        }
    }

    suspend fun getUrlDescarga(descripcion : String) : String?{
        return catalogRepository.getUrlDescarga(descripcion)
    }

    fun getObservableUrlDescarga(descripcion : String, observer: BaseObserver<String?>) {
        execute(this.catalogRepository.getObservableUrlDescarga(descripcion), observer)
    }
}
