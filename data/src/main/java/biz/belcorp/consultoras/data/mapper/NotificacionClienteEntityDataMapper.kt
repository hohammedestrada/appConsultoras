package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.NotificacionClienteEntity
import biz.belcorp.consultoras.domain.entity.NotificacionCliente

@Singleton
class NotificacionClienteEntityDataMapper @Inject
internal constructor(private val clienteEntityDataMapper: ClienteEntityDataMapper) {

    fun transform(input: NotificacionCliente?): NotificacionClienteEntity? {
        return input?.let {
            NotificacionClienteEntity().apply {
                id = it.id
                estado = it.estado
                year = it.year
                clienteLocalID = it.clienteLocalID
                clienteEntity = clienteEntityDataMapper.transform(it.cliente)
            }
        }
    }

    fun transform(input: NotificacionClienteEntity?): NotificacionCliente? {
        return input?.let {
            NotificacionCliente().apply {
                id = it.id
                estado = it.estado
                year = it.year
                clienteLocalID = it.clienteLocalID
                cliente = clienteEntityDataMapper.transform(it.clienteEntity)
            }
        }
    }

    fun transform(list: List<NotificacionClienteEntity?>?): List<NotificacionCliente?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<NotificacionCliente>()
        }
    }

}
