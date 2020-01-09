package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.Country
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.CountryRepository
import biz.belcorp.consultoras.domain.repository.SessionRepository

/**
 * Clase que define los casos de uso de una tarea
 * para cumplir lo descrito en la historia de usuario
 *
 * @version 1.0
 * @since 2017-04-14
 */
class CountryUseCase
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param countryRepository   Repositorio que tiene acceso al origen de datos
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 */
@Inject
constructor(private val sessionRepository: SessionRepository,
            private val countryRepository: CountryRepository, threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    /**
     * Metodo que obtiene el listado de paises
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    operator fun get(observer: BaseObserver<List<Country?>?>) {
        execute(this.countryRepository.get(), observer)
    }

    /**
     * Metodo que obtiene el listado de paises por marca
     *
     * @param brand    marca
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun getByBrand(brand: Int, observer: BaseObserver<List<Country?>?>) {
        execute(this.countryRepository.getByBrand(brand), observer)
    }

    /**
     * Metodo que obtiene una pais guardado en session
     *
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun find(observer: BaseObserver<Country?>) {
        execute(this.sessionRepository.countrySIM
                .flatMap { iso -> this.countryRepository.find(iso) }, observer)
    }

    /**
     * Metodo que obtiene una pais por id
     *
     * @param id       id de una pais
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun find(id: Int, observer: BaseObserver<Country?>) {
        execute(this.countryRepository.find(id), observer)
    }

    /**
     * Metodo que obtiene una pais por iso
     *
     * @param iso      iso de una pais
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun find(iso: String, observer: BaseObserver<Country?>) {
        execute(this.countryRepository.find(iso), observer)
    }

    /**
     * Metodo que guarda un pais
     *
     * @param entity   Entidad de dominio pais
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun save(entity: Country, observer: BaseObserver<Boolean?>) {
        execute(this.countryRepository.save(entity), observer)
    }

    /**
     * Metodo que elimina una pais
     *
     * @param id       id de una pais
     * @param observer Observer en donde se obtendra la respuesta cuando se haya ejecutado el observable
     */
    fun delete(id: Int, observer: BaseObserver<Boolean?>) {
        execute(this.countryRepository.delete(id), observer)
    }

}
