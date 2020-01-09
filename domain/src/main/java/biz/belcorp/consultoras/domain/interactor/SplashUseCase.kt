package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.ConfigRepository
import io.reactivex.Observable
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Clase que define los casos de uso de un cliente
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-05-03
 */
class SplashUseCase
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param clienteRepository   Repositorio que tiene acceso al origen de datos
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 * @param sessionRepository   Repositorio que tiene acceso a la sesion
 */
@Inject
constructor(threadExecutor: ThreadExecutor
            , postExecutionThread: PostExecutionThread
            , private val configRepository: ConfigRepository) : UseCase(threadExecutor, postExecutionThread) {


    fun splash(observer: BaseObserver<ConfigReponse?>) {
        configRepository.get().onErrorResumeNext(askForExceptionsConfigGet()).map {

        }

    }

    private fun askForExceptionsConfigGet()
        : Function<Throwable, Observable<ConfigReponse?>> {
        return Function{ t ->
            when (t ) {
                is NetworkErrorException -> configRepository.getFromLocal()
                is NullPointerException -> Observable.error(NetworkErrorException())
                else  -> Observable.error(t)
            }
        }
    }

}
