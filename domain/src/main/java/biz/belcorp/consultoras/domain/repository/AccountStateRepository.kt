package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.AccountState
import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener la data desde la capa de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */

interface AccountStateRepository {

    val fromLocal: Observable<Collection<AccountState?>?>

    fun get(): Observable<Collection<AccountState?>?>

}
