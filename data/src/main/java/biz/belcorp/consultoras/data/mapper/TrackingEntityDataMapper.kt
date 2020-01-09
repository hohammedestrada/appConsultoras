package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.TrackingEntity
import biz.belcorp.consultoras.domain.entity.Tracking
import biz.belcorp.consultoras.domain.entity.TrackingDetail
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingEntityDataMapper @Inject
internal constructor(private val trackingDetailEntityDataMapper: TrackingDetailEntityDataMapper) {

    fun transform(input: Tracking?): TrackingEntity? {
        return input?.let {
            TrackingEntity().apply {
                id = it.id
                numeroPedido = it.numeroPedido
                campania = it.campania
                estado = it.estado
                fecha = it.fecha
                detalles = trackingDetailEntityDataMapper.transform(it.detalles)
            }
        }
    }

    fun transform(input: TrackingEntity?): Tracking? {
        return input?.let {
            Tracking().apply {
                id = it.id
                numeroPedido = it.numeroPedido
                campania = it.campania
                estado = it.estado
                fecha = it.fecha
                detalles = ArrayList<TrackingDetail>(trackingDetailEntityDataMapper.transform(it.detalles))
            }
        }
    }

    fun transform(input: Collection<Tracking?>?): List<TrackingEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<TrackingEntity>()
        }
    }

    fun transform(input: List<TrackingEntity?>?): Collection<Tracking?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Tracking>()
        }
    }
}
