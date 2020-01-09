package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.DebtEntity
import biz.belcorp.consultoras.domain.entity.DebtRequest

/**
 *
 */
@Singleton
class DebtEntityDataMapper @Inject
internal constructor() {

    fun transform(debtRequest: DebtRequest?): DebtEntity? {
        return debtRequest?.let {
            DebtEntity().apply {
                codigoCampania = it.codigoCampania
                clienteLocalID = it.clienteLocalID
                clienteID = it.clienteID
                descripcion = it.descripcion
                fecha = it.fecha
                monto = it.monto
                nota = it.nota
                tipoMovimiento = it.tipoMovimiento
            }
        }
    }
}
