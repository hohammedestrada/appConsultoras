package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.ClientMovementEntityDataMapper
import biz.belcorp.consultoras.data.mapper.DebtEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.debt.DebtDataStoreFactory
import biz.belcorp.consultoras.domain.entity.ClientMovement
import biz.belcorp.consultoras.domain.entity.DebtRequest
import biz.belcorp.consultoras.domain.repository.DebtRepository
import io.reactivex.Observable

/**
 *
 */
@Singleton
class DebtDataRepository @Inject
internal constructor(private val debtDataStoreFactory: DebtDataStoreFactory,
                     private val debtEntityDataMapper: DebtEntityDataMapper,
                     private val clientMovementEntityDataMapper: ClientMovementEntityDataMapper)
    : DebtRepository {

    override fun uploadDebt(debt: DebtRequest?): Observable<ClientMovement?> {
        val debtDataStore = debtDataStoreFactory.createCloudDataStore()
        return debtDataStore.uploadDebt(debtEntityDataMapper.transform(debt))
            .map { clientMovementEntityDataMapper.transform(it) }
    }
}
