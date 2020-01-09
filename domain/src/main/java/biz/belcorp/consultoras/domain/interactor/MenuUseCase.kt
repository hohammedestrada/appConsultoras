package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.Ordenamiento
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.consultoras.domain.repository.MenuRepository
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 * Clase que define los casos de usºo de una menu
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-04-14
 */
class MenuUseCase
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param menuRepository      Repositorio que tiene acceso al origen de datos
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 */
@Inject
constructor(private val menuRepository: MenuRepository, private val authRepository: AuthRepository,
            threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Metodo que obtiene el listado de menus por país
     *
     * @param paisISO  ISO de pais para obtener el menu
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    operator fun get(paisISO: String?, campaign: String, revistaDigital: Int?, observer: BaseObserver<Boolean?>) {
        execute(this.menuRepository.get(paisISO, campaign, revistaDigital), observer)
    }

    /**
     * Metodo que obtiene el listado de menus desde la bd
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun getFromDatabase(pos: Int?, observer: BaseObserver<List<Menu?>?>) {
        execute(this.menuRepository.getFromDatabase(pos), observer)
    }

    /**
     * Obtiene un menu por codigo desde la bd
     *
     * @param code es el codigo del menu buscado
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun get(code: String?, observer: BaseObserver<Menu?>){
        execute(this.menuRepository.getMenuByCode(code), observer)
    }

    /**
     * Obtiene un menu por codigo desde la bd
     *
     * @param code1 es el codigo del menu buscado
     * @param code2 es el codigo del menu buscado
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun getActive(code1: String?, code2: String?, observer: BaseObserver<Menu?>){
        execute(this.menuRepository.getActive(code1, code2), observer)
    }

    suspend fun getActive2(code1: String?, code2: String?): Menu? {
        return this.menuRepository.getActive2(code1, code2)
    }

    /**
     * Metodo que obtiene y guarda el listado de menus por país
     *
     * @param paisISO  ISO de pais para obtener el menu
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun getAndSave(paisISO: String, campaign: String, revistaDigital: Int, observer: BaseObserver<Boolean?>) {
        execute(this.menuRepository.get(paisISO, campaign, revistaDigital)
                .onErrorResumeNext(askForExceptions(paisISO, campaign, revistaDigital)), observer)
    }

    fun updateVisibleAndOrden(menuList: List<Menu?>?, observer: BaseObserver<Boolean?>) {
        execute(this.menuRepository.updateVisibleAndOrden(menuList), observer)
    }

    /** */

    private fun askForExceptions(paisISO: String, campaign: String, revistaDigital: Int): Function<Throwable, Observable<Boolean>> {
        return Function{ t ->
            when (t) {
                is ExpiredSessionException -> authRepository.refreshToken()
                    .flatMap {
                        this.menuRepository.get(paisISO, campaign, revistaDigital)
                    }
                else -> Observable.error(t)
            }
        }
    }
}
