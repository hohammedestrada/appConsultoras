package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.Config
import biz.belcorp.consultoras.domain.entity.ConfigReponse
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.ConfigRepository
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 * Clase que define los casos de uso de Configuracion
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-04-14
 */

class ConfigUseCase
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param repository    Repositorio que tiene acceso al origen de datos
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 */
@Inject
constructor(private val repository: ConfigRepository, threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Metodo que obtiene el listado de paises
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    operator fun get(observer: BaseObserver<ConfigReponse?>) {

        execute(this.repository.get()
            .onErrorResumeNext(askForExceptions()), observer)
    }

    fun getOffline(observer: BaseObserver<ConfigReponse?>) {
        execute(this.repository.getFromLocal()
            .onErrorResumeNext(askForExceptions()), observer)
    }

    /**
     * Metodo que guarda el listado de paises
     *
     * @param config   Configuracion de la aplicacion
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun save(config: Config, observer: BaseObserver<Boolean?>) {
        execute(this.repository.save(config), observer)
    }

    fun saveAppName(appName: String, observer: BaseObserver<Boolean>) {
        execute(this.repository.save(appName), observer)
    }

    /**
     *
     */

    private fun getFromLocal(): Observable<ConfigReponse?> {
        return this.repository.getFromLocal().onErrorResumeNext(askForExceptions())
    }

    private fun askForExceptions()
        : Function<Throwable, Observable<ConfigReponse?>> {
        return Function{ t ->
            when (t ) {
                is NetworkErrorException -> getFromLocal()
                is NullPointerException -> Observable.error(NetworkErrorException())
                else  -> Observable.error(t)
            }
        }
    }
}
