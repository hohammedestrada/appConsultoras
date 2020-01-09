package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.Anotacion
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.NoteRepository
import io.reactivex.observers.DisposableObserver

/**
 * Clase que define los casos de uso de un nota
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-05-03
 */
class NoteUseCase @Inject
constructor(private val noteRepository: NoteRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Obtiene la nota requerida.
     */
    fun getById(id: Int?, observer: DisposableObserver<Anotacion?>) {
        execute(noteRepository.getById(id), observer)
    }

    /**
     * Obtiene la lista de notas por cliente local id.
     */
    fun listByClientLocalID(id: Int?, observer: DisposableObserver<Collection<Anotacion?>?>) {
        execute(noteRepository.listByClientLocalID(id), observer)
    }

    /**
     * Elimina la lista de notas por cliente local id.
     */
    fun deleteByClientLocalID(id: Int?, observer: DisposableObserver<Boolean?>) {
        execute(noteRepository.deleteByClientLocalID(id), observer)
    }

    /**
     * Obtiene la cantidad de notas por cliente local id.
     */
    fun countByClientLocalID(countryISO: String, id: Int?, observer: DisposableObserver<Boolean?>) {
        execute(noteRepository.countByClient(countryISO, id), observer)
    }

}
