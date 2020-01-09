package biz.belcorp.consultoras.data.repository.datasource.origenPedido

import android.content.Context
import biz.belcorp.consultoras.data.entity.*


import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.select


class OrigenPedidoDBDataStore(val context: Context) : OrigenPedidoDataStore {

    override fun get(tipo: String, codigo: String): OrigenPedidoWebEntity? {
        val query = Transformar((select from OrigenPedidoWebLocalEntity::class).queryList())

        var res = query?.filter { it1 -> it1?.tipoOferta == tipo }
            ?.firstOrNull()

        res.let {
            it?.listaOrigenPedidoWeb = it?.listaOrigenPedidoWeb?.filter { it1 -> it1?.codigo == codigo }
        }

        return res
    }

    override fun getValor(tipo: String, codigo: String): Int? {

        val result = (select from OrigenPedidoWebLocalEntity::class).queryList()

        val query = Transformar(result)
        val res = query.filter { it1 -> it1.tipoOferta == tipo }
            .firstOrNull()?.listaOrigenPedidoWeb?.filter { it1 -> it1?.codigo == codigo }
            ?.firstOrNull()?.valor

        return res
    }

    fun Transformar(lista: List<OrigenPedidoWebLocalEntity>): List<OrigenPedidoWebEntity> {

        var lstOrigenPedidoWeb = ArrayList<OrigenPedidoWebEntity>()

        lista.groupBy { t -> t.tipoOferta }.toList().forEach {

            var contenidoGrupo = ArrayList<listaOrigenPedidoWebEntity>().apply {
                lista.filter { t -> t.tipoOferta == it.first }.forEach { it2 ->
                    it2.run {
                        var item = listaOrigenPedidoWebEntity()
                        item.codigo = it2.codigo
                        item.valor = it2.valor
                        return@run item
                    }.let { it3 -> add(it3) }
                }
            }

            lstOrigenPedidoWeb.add(OrigenPedidoWebEntity().apply {
                tipoOferta = it.first
                listaOrigenPedidoWeb = contenidoGrupo
            })
        }

        return lstOrigenPedidoWeb
    }
}


