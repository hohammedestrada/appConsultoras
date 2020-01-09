package biz.belcorp.consultoras.data.mapper
import biz.belcorp.consultoras.domain.PremioFinalAgrega
import biz.belcorp.consultoras.data.entity.PremioFinalMetaEntity
import biz.belcorp.consultoras.data.entity.PremioFinalAgregaEntity
import biz.belcorp.consultoras.data.entity.PremioFinalEntity
import biz.belcorp.consultoras.domain.entity.PremioFinal
import biz.belcorp.consultoras.domain.entity.PremioFinalMeta
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PremioDataMapper @Inject
internal constructor() {

    fun transformToPremio(input: List<PremioFinalEntity?>?): List<PremioFinal?>? {
        return  input?.let {list->
            ArrayList<PremioFinal>().apply{
                list.forEach {item ->
                    val transformed = transformnPremioDB(item)
                    transformed?.let { add(it) }
                }
            }
        }
    }

    fun transformToMeta(input: PremioFinalMetaEntity?): PremioFinalMeta? {
        return input?.let {
            PremioFinalMeta().apply {
                montoPedido = it.montoPedido
                gapMinimo = it.gapMinimo
                gapMaximo = it.gapMaximo
                gapAgregar = it.gapAgregar
                montoMeta = it.montoMeta
                tipoRango = it.tipoRango
            }

        }
    }

    fun transformnPremioDB(input: PremioFinalEntity?): PremioFinal? {
        return input?.let {
            PremioFinal().apply {
                upSellingDetalleId = it.upSellingDetalleId
                cuv = it.cuv
                nombre = it.nombre
                descripcion = it.descripcion
                imagen = it.imagen
                stock = it.stock
                orden = it.orden
                activo = it.activo
                upSellingId = it.upSellingId
                stockActual = it.stockActual
                seleccionado = it.seleccionado
                habilitado = it.habilitado
            }
        }
    }


    fun transformAgregaPremio(input: PremioFinalAgrega?): PremioFinalAgregaEntity? {

        return input?.let {
            PremioFinalAgregaEntity().apply {
                campaniaId=it.campaniaId
                montoPedido=it.montoPedido
                gapMinimo=it.gapMinimo
                gapMaximo=it.gapMaximo
                gapAgregar=it.gapAgregar
                montoMeta=it.montoMeta
                cuv=it.cuv
                tipoRango=it.tipoRango
                montoPedidoFinal=it.montoPedidoFinal
                upSellingDetalleId=it.upSellingDetalleId
            }
        }
    }

}
