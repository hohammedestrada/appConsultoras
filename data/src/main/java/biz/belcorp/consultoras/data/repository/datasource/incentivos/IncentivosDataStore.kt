package biz.belcorp.consultoras.data.repository.datasource.incentivos

import biz.belcorp.consultoras.data.entity.ConcursoEntity
import biz.belcorp.consultoras.data.entity.IncentivesRequestEntity
import io.reactivex.Observable

/**
 * Interface que implementa los metodos para obtener los datos de un cliente
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-03
 */
interface IncentivosDataStore {

    /**
     * Servicio de que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    val incentives: Observable<List<ConcursoEntity?>?>

    /**
     * Servicio de que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    operator fun get(entity: IncentivesRequestEntity?): Observable<List<ConcursoEntity?>?>

    /**
     * Servicio de que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getHistory(entity: IncentivesRequestEntity?): Observable<List<ConcursoEntity?>?>

    /**
     * Servicio de que obtiene los concursos por campaña
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getContest(contestCode: String?): Observable<ConcursoEntity?>

    /**
     * Servicio de que guarda los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun save(contests: List<ConcursoEntity?>?): Observable<String?>
}
