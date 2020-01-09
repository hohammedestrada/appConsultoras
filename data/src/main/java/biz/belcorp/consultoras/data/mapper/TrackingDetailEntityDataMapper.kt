package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.TrackingDetailEntity
import biz.belcorp.consultoras.domain.entity.TrackingDetail

@Singleton
class TrackingDetailEntityDataMapper @Inject
internal constructor() {

    fun transform(input: TrackingDetail?): TrackingDetailEntity? {
        return input?.let {
            TrackingDetailEntity().apply {
                id = it.id
                etapa = it.etapa
                situacion = it.situacion
                fecha = it.fecha
                fechaFormatted = it.fechaFormateada
                isAlcanzado = it.alcanzado
                observacion = it.observacion
            }
        }
    }

    fun transform(input: TrackingDetailEntity?): TrackingDetail? {
        return input?.let {
            TrackingDetail().apply {
                id = it.id
                etapa = it.etapa
                situacion = it.situacion
                fecha = it.fecha
                fechaFormateada = it.fechaFormatted
                alcanzado = it.isAlcanzado
                observacion = it.observacion
            }
        }
    }

    fun transform(input: Collection<TrackingDetail?>?): List<TrackingDetailEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<TrackingDetailEntity>()
        }
    }

    fun transform(input: List<TrackingDetailEntity?>?): Collection<TrackingDetail>{
        return input?.let {
            it
                .map { it1 -> transform(it1) }.filterNotNull()
        } ?: run {
            emptyList<TrackingDetail>()
        }
    }
}
