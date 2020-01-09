package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import javax.inject.Inject

class MetodoPagoDataMapper @Inject internal constructor() {

    fun transform(input: DataPagoConfigEntity.MetodoPagoEntity?): PagoOnlineConfig.MetodoPago? {
        return input?.let {
            PagoOnlineConfig.MetodoPago().apply {
                medioPagoDetalleId = input.medioPagoDetalleId
                medioPagoId = input.medioPagoId
                descripcion = input.descripcion
                orden = input.orden
                tipoVisualizacion = input.tipoVisualizacion
                termCondicion = input.termCondicion
                tipoPasarela = input.tipoPasarela
                regExpreTarjeta = input.regExpreTarjeta
                tipoTarjeta = input.tipoTarjeta
                input.porcentajeGastosAdministrativos.let {
                    porcentajeGastosAdministrativos = it ?: 0.0
                }
                pagoEnLineaGastosLabel = input.pagoEnLineaGastosLabel
                montoMinimoPago = input.montoMinimoPago
                estado = input.estado
            }
        }
    }

    fun transform(input: List<DataPagoConfigEntity.MetodoPagoEntity>?): Collection<PagoOnlineConfig.MetodoPago?>? {
        return input?.let {
            it.map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run { emptyList<PagoOnlineConfig.MetodoPago>() }
    }

}
