package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.AuthRepository
import dagger.internal.Preconditions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Clase abstracta encargada de procesar un caso de uso del negocio
 * esta clase guardara todas las ejecuciones realizadas y posteriormente
 * finalizara todas una vez estas han sido terminadas
 *
 * @version 1.0
 * @since 2017-04-14
 */
abstract class UseCase
/**
 * Constructor del caso de uso
 *
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 */
internal constructor(private val threadExecutor: ThreadExecutor,
                     private val postExecutionThread: PostExecutionThread) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    /**
     * Ejecuta el actual caso de uso
     *
     * @param observable [Observable]
     * @param observer   [DisposableObserver] which will be listening to the observable build
     * by observable method.
     */
    internal fun <T> execute(observable: Observable<T>, observer: DisposableObserver<T>) {
        Preconditions.checkNotNull(observer)

        val disposable = observable
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
                .subscribeWith(observer)

        addDisposable(disposable)
    }

    /**
     * Finaliza el actual [CompositeDisposable].
     */
    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    /**
     * Agrega el [CompositeDisposable].
     */
    private fun addDisposable(disposable: Disposable) {
        Preconditions.checkNotNull(disposable)
        Preconditions.checkNotNull(disposables)
        disposables.add(disposable)
    }

}
