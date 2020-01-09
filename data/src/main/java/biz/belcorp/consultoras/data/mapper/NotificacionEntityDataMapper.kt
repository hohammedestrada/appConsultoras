package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.NotificacionEntity
import biz.belcorp.consultoras.domain.entity.Notificacion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificacionEntityDataMapper @Inject
internal constructor()// EMPTY
{

    fun transform(input: Notificacion?): NotificacionEntity? {
        return input?.let {
            NotificacionEntity().apply{
                id = it.id
                codigo = it.codigo
                consultoraId = it.consultoraId
                descripcion = it.descripcion
                emoji = it.emoji
                estado = it.estado
                fecha = it.fecha
                notificationId = it.notificationId
            }
        }
    }

    fun transform(input: NotificacionEntity?): Notificacion? {
        return input?.let {
            Notificacion().apply{
                id = it.id
                codigo = it.codigo
                consultoraId = it.consultoraId
                descripcion = it.descripcion
                emoji = it.emoji
                estado = it.estado
                fecha = it.fecha
                notificationId = it.notificationId
            }
        }
    }

    fun transformToEntityList(input: List<Notificacion?>?): List<NotificacionEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<NotificacionEntity>()
        }
    }

    fun transformToList(input: List<NotificacionEntity?>?): List<Notificacion?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Notificacion>()
        }
    }
}
