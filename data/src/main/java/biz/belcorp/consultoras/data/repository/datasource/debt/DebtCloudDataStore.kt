package biz.belcorp.consultoras.data.repository.datasource.debt

import biz.belcorp.consultoras.data.entity.ClientMovementEntity
import biz.belcorp.consultoras.data.entity.DebtEntity
import biz.belcorp.consultoras.data.net.service.IDebtService
import io.reactivex.Observable

/**
 *
 */
class DebtCloudDataStore(private val iDebtService: IDebtService) : DebtDataStore {

    override fun uploadDebt(debtEntity: DebtEntity?): Observable<ClientMovementEntity?> {
        debtEntity?.let {
            return iDebtService.uploadDebt(debtEntity, debtEntity.clienteID.toString())
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }
}
