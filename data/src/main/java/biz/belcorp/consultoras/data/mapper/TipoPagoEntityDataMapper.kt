package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import javax.inject.Inject

class TipoPagoEntityDataMapper @Inject internal constructor() {

    fun transform(input: DataPagoConfigEntity.TipoPagoEntity?): PagoOnlineConfig.TipoPago? {
        return input?.let {
            PagoOnlineConfig.TipoPago().apply {
                codigo = input.codigo
                descripcion = input.descripcion
                estado = input.estado
                tipoId = input.tipoId
            }
        }
    }

    fun transform(input: List<DataPagoConfigEntity.TipoPagoEntity>?): Collection<PagoOnlineConfig.TipoPago?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<PagoOnlineConfig.TipoPago>()
        }
    }

}
