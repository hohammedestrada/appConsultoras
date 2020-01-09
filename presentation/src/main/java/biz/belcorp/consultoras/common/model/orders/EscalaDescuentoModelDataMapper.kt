package biz.belcorp.consultoras.common.model.orders

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.EscalaDescuento
import javax.inject.Inject

@PerActivity
class EscalaDescuentoModelDataMapper @Inject
internal constructor() {

    fun transform(input: EscalaDescuento?): EscalaDescuentoModel? {
        return input?.let {
            EscalaDescuentoModel(it.montoDesde, it.montoHasta, it.porDescuento,
                it.tipoParametriaOfertaFinal, it.precioMinimo, it.algoritmo)
        }
    }

    fun transform(input: EscalaDescuentoModel?): EscalaDescuento? {
        return input?.let {
            EscalaDescuento().apply{
                montoDesde = it.montoDesde
                montoHasta = it.montoHasta
                porDescuento = it.porDescuento
                tipoParametriaOfertaFinal = it.tipoParametriaOfertaFinal
                precioMinimo = it.precioMinimo
                algoritmo = it.algoritmo
            }
        }
    }

    fun transform(input: Collection<EscalaDescuento?>?): List<EscalaDescuentoModel?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<EscalaDescuentoModel>()
        }
    }

    fun transform(input: List<EscalaDescuentoModel?>?): Collection<EscalaDescuento?>? {
        return input?.let {
            it
                .map { transform(it) }
                .filter { null != it }
        } ?: run {
            emptyList<EscalaDescuento>()
        }
    }

}
