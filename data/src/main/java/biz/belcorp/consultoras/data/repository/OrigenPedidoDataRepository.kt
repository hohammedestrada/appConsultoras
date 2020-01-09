package biz.belcorp.consultoras.data.repository


import javax.inject.Inject
import javax.inject.Singleton
import biz.belcorp.consultoras.data.mapper.OrigenPedidoEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.origenPedido.OrigenPedidoDataStoreFactory
import biz.belcorp.consultoras.domain.entity.OrigenPedidoWeb
import biz.belcorp.consultoras.domain.repository.OrigenPedidoRepository


@Singleton
class OrigenPedidoDataRepository @Inject
internal constructor(private val OrigenPedidoDataStoreFactory: OrigenPedidoDataStoreFactory,
                     private val OrigenPedidoEntityDataMapper: OrigenPedidoEntityDataMapper)
    : OrigenPedidoRepository {

    override suspend fun get(tipo: String, codigo: String): OrigenPedidoWeb? {
        val res = this.OrigenPedidoDataStoreFactory.create().get(tipo, codigo)
        return OrigenPedidoEntityDataMapper.transform(res)
    }

    override suspend fun getValor(tipo: String, codigo: String): Int? {
        return this.OrigenPedidoDataStoreFactory.create().getValor(tipo, codigo)
    }
}
