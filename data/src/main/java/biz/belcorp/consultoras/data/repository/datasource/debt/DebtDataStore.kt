package biz.belcorp.consultoras.data.repository.datasource.debt

import biz.belcorp.consultoras.data.entity.ClientMovementEntity
import biz.belcorp.consultoras.data.entity.DebtEntity
import io.reactivex.Observable

/**
 *
 */
interface DebtDataStore {

    fun uploadDebt(debtEntity: DebtEntity?): Observable<ClientMovementEntity?>
}
