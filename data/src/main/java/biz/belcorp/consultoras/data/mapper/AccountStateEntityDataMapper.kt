package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.AccountStateEntity
import biz.belcorp.consultoras.domain.entity.AccountState

@Singleton
class AccountStateEntityDataMapper @Inject
internal constructor() {

    fun transform(input: AccountState?): AccountStateEntity? {
        return input?.let {
            AccountStateEntity().apply {
                id = it.id
                fechaRegistro = it.fechaRegistro
                descripcionOperacion = it.descripcionOperacion
                montoOperacion = it.montoOperacion
                cargo = it.cargo
                abono = it.abono
            }
        }
    }

    fun transform(input: AccountStateEntity?): AccountState? {

        return input?.let {
            AccountState().apply {
                id = it.id
                fechaRegistro = it.fechaRegistro
                descripcionOperacion = it.descripcionOperacion
                montoOperacion = it.montoOperacion
                cargo = it.cargo
                abono = it.abono
            }
        }
    }

    fun transform(input: Collection<AccountState?>?): List<AccountStateEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter {it1 -> null != it1 }
        } ?: run {
            emptyList<AccountStateEntity>()
        }
    }

    fun transform(input: List<AccountStateEntity?>?): Collection<AccountState?>? {
        return input?.let {
            return it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<AccountState>()
        }
    }
}
