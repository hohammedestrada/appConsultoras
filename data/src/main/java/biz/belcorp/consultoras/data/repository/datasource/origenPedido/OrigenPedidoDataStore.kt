package biz.belcorp.consultoras.data.repository.datasource.origenPedido

import biz.belcorp.consultoras.data.entity.OrigenPedidoWebEntity



interface OrigenPedidoDataStore {
    fun get(tipo: String, codigo: String): OrigenPedidoWebEntity?
    fun getValor(tipo: String, codigo: String): Int?
}
