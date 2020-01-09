package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import javax.inject.Inject

class EstadoCuentaEntityDataMapper @Inject internal constructor() {

    fun transform(estadoCuenta: DataPagoConfigEntity.EstadoCuentaEntity?): PagoOnlineConfig.EstadoCuenta? {
        return estadoCuenta?.let {
            PagoOnlineConfig.EstadoCuenta().apply {
                deuda = it.deuda
                deudaFormateada = it.deudaFormateada
            }
        }
    }
}
