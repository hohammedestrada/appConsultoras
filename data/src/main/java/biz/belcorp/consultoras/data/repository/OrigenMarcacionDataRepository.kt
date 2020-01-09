package biz.belcorp.consultoras.data.repository


import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.repository.datasource.origenmarcacion.OrigenMarcacionDataStoreFactory
import biz.belcorp.consultoras.domain.repository.OrigenMarcacionRepository


@Singleton
class OrigenMarcacionDataRepository @Inject
internal constructor(private val OrigenPedidoDataStoreFactory: OrigenMarcacionDataStoreFactory )
    : OrigenMarcacionRepository {



    override suspend fun getValor(tipo: String, codigo: String): String? {
        return this.OrigenPedidoDataStoreFactory.create().getValor(tipo, codigo)
    }


    override suspend fun getValorPalanca(codigo: String): String? {
        return this.OrigenPedidoDataStoreFactory.create().getValorPalanca(codigo)
    }


    override suspend fun getValorSubseccion(codigo: String): String? {
        return this.OrigenPedidoDataStoreFactory.create().getValorSubseccion(codigo)
    }

}
