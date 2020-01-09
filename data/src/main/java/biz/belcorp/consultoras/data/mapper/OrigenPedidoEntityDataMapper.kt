package biz.belcorp.consultoras.data.mapper


import biz.belcorp.consultoras.data.entity.ListadoOrigenesPedidoWebEntity
import biz.belcorp.consultoras.data.entity.OrigenPedidoWebEntity
import biz.belcorp.consultoras.data.entity.listaOrigenPedidoWebEntity

import biz.belcorp.consultoras.domain.entity.ListadoOrigenesPedidoWeb
import biz.belcorp.consultoras.domain.entity.OrigenPedidoWeb
import biz.belcorp.consultoras.domain.entity.listaOrigenPedidoWeb
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class OrigenPedidoEntityDataMapper @Inject
internal constructor() {

    fun transform(list: List<OrigenPedidoWebEntity?>?): List<OrigenPedidoWeb?>? {
        return list?.let {
             it.map { it1 -> transform(it1) }
                 .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<OrigenPedidoWeb>()
        }
    }

    fun transform(entity: OrigenPedidoWebEntity?): OrigenPedidoWeb? {
        return entity?.let{
            OrigenPedidoWeb().apply {
                tipoOferta = it.tipoOferta
                listaOrigenPedidoWeb = transformOrigen(it.listaOrigenPedidoWeb)
            }
        }
    }

    fun transformOrigen(list: List<listaOrigenPedidoWebEntity?>?): List<listaOrigenPedidoWeb?>? {
        return list?.let {
            it.map { it1 -> transform2(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<listaOrigenPedidoWeb>()
        }
    }

    fun transform2(entity: listaOrigenPedidoWebEntity?): listaOrigenPedidoWeb? {
        return entity?.let{
            listaOrigenPedidoWeb().apply {
                codigo = it.codigo
                valor   = it.valor

            }
        }
    }


}
