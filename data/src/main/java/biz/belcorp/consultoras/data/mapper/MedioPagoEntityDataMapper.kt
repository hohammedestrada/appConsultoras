package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import javax.inject.Inject

class MedioPagoEntityDataMapper @Inject internal constructor() {

    fun transform(input: DataPagoConfigEntity.MedioPagoEntity?): PagoOnlineConfig.MedioPago? {
        return input?.let {
            PagoOnlineConfig.MedioPago().apply {
                codigo = input.codigo
                descripcion = input.descripcion
                estado = input.estado
                medioId = input.medioId
                orden = input.orden
                rutaIcono = input.rutaIcono
            }
        }
    }

    fun transform(input: List<DataPagoConfigEntity.MedioPagoEntity>?): Collection<PagoOnlineConfig.MedioPago?>? {
        return input?.let {
            it.map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<PagoOnlineConfig.MedioPago>()
        }
    }

}
