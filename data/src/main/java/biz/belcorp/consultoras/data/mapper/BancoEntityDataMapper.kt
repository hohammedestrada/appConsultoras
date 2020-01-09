package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import javax.inject.Inject

class BancoEntityDataMapper @Inject internal constructor() {

    fun transform(input: DataPagoConfigEntity.BancoEntity?): PagoOnlineConfig.Banco? {
        return input?.let {
            PagoOnlineConfig.Banco().apply {
                id = it.id
                banco = it.banco
                urlWeb = it.urlWeb
                urlIcono = it.urlIcono
                packageApp = it.packageApp
                estado = it.estado
            }
        }
    }

    fun transform(input: List<DataPagoConfigEntity.BancoEntity>?): Collection<PagoOnlineConfig.Banco?>? {
        return input?.let {
            it.map { it1 -> transform(it1) }
                .filter { it1 -> it1 != null }
        } ?: run {
            emptyList<PagoOnlineConfig.Banco>()
        }
    }
}
