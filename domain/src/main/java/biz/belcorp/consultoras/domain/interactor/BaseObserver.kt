package biz.belcorp.consultoras.domain.interactor

import io.reactivex.observers.DisposableObserver

/**
 * Clase base disposable que se implementara la ejecucion de cualquier proceso
 * que necesita obtener los datos desde un repositorio externo o interno
 *
 * @version 1.0
 * @since 2017-04-14
 */
open class BaseObserver<T> : DisposableObserver<T>() {

    /**
     * Metodo que se ejecuta cuando la ejecucion fue exitosa
     *
     * @param t Generico que recibe un primitivo, objeto o una lista
     */
    override fun onNext(t: T) {
        // no-op by default.
    }

    /**
     * Metodo que se ejecuta cuando ha finalizado una ejecucion sea exitosa o erronea
     */
    override fun onComplete() {
        // no-op by default.
    }

    /**
     * Metodo que se ejecuta cuando la ejecucion devolvio error
     *
     * @param exception Excepcion que devolvera a la vista para que procesa la respuesta
     * de acuerdo al tipo de error recibido
     */
    override fun onError(exception: Throwable) {
        // no-op by default.
    }
}
