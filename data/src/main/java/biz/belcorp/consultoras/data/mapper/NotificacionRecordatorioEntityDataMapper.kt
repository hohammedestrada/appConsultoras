package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.NotificacionRecordatorioEntity
import biz.belcorp.consultoras.domain.entity.NotificacionRecordatorio

@Singleton
class NotificacionRecordatorioEntityDataMapper @Inject
internal constructor(private val recordatorioEntityDataMapper: RecordatorioEntityDataMapper,
                     private val clienteEntityDataMapper: ClienteEntityDataMapper) {

    fun transform(input: NotificacionRecordatorio?): NotificacionRecordatorioEntity? {
        return input?.let{
            NotificacionRecordatorioEntity().apply {
                id = it.id
                estado = it.estado
                recordatorioLocalID = it.recordatorioLocalID
                recordatorioEntity = recordatorioEntityDataMapper.transform(it.recordatorio)
                clienteEntity = clienteEntityDataMapper.transform(it.cliente)
            }
        }
    }

    fun transform(input: NotificacionRecordatorioEntity?): NotificacionRecordatorio? {
        return input?.let{
            NotificacionRecordatorio().apply {
                id = it.id
                estado = it.estado
                recordatorioLocalID = it.recordatorioLocalID
                recordatorio = recordatorioEntityDataMapper.transform(it.recordatorioEntity)
                cliente = clienteEntityDataMapper.transform(it.clienteEntity)
            }
        }
    }

    fun transform(list: List<NotificacionRecordatorioEntity?>?): List<NotificacionRecordatorio?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<NotificacionRecordatorio>()
        }
    }

}
