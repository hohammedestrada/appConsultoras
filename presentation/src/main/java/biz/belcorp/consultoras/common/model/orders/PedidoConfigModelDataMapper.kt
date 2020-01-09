package biz.belcorp.consultoras.common.model.orders

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.EscalaDescuento
import biz.belcorp.consultoras.domain.entity.MensajeMeta
import biz.belcorp.consultoras.domain.entity.PedidoConfig
import javax.inject.Inject

@PerActivity
class PedidoConfigModelDataMapper @Inject
internal constructor(private val escalaDescuentoModelDataMapper: EscalaDescuentoModelDataMapper,
                     private val mensajeMetaModelDataMapper: MensajeMetaModelDataMapper) {

    fun transform(input: PedidoConfig?): PedidoConfigModel? {
        return input?.let {
            PedidoConfigModel(escalaDescuentoModelDataMapper.transform(it.escalaDescuento),
                mensajeMetaModelDataMapper.transform(it.mensajeMeta))
        }
    }

    fun transform(input: PedidoConfigModel?): PedidoConfig? {
        return input?.let {
            PedidoConfig().apply{
                escalaDescuento = escalaDescuentoModelDataMapper.transform(it.escalaDescuento)
                    as List<EscalaDescuento?>?
                mensajeMeta = mensajeMetaModelDataMapper.transform(it.mensajeMeta) as List<MensajeMeta?>?
            }
        }
    }

}
