package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.Concurso
import biz.belcorp.consultoras.domain.entity.IncentivesRequest
import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener la data desde la capa de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */

interface IncentivosRepository {

    val incentives: Observable<Collection<Concurso?>?>

    fun getIncentives(params: IncentivesRequest?): Observable<Collection<Concurso?>?>

    fun getContest(contestCode: String?): Observable<Concurso?>

}
