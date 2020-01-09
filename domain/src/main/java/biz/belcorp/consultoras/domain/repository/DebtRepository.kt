package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.ClientMovement
import biz.belcorp.consultoras.domain.entity.DebtRequest
import io.reactivex.Observable

/**
 *
 */
interface DebtRepository {

    fun uploadDebt(debt: DebtRequest?): Observable<ClientMovement?>
}
