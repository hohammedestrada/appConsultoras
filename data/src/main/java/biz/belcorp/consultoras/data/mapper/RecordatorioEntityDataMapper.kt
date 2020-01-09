package biz.belcorp.consultoras.data.mapper

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.RecordatorioEntity
import biz.belcorp.consultoras.domain.entity.Recordatorio

/**
 * Clase encarga de realizar el mapeo de la entidad tarea(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */

@Singleton
class RecordatorioEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: RecordatorioEntity?): Recordatorio? {
        return entity?.let {
            Recordatorio().apply {
                id = it.id
                recordatorioID = it.recordatorioId
                clienteID = it.clienteID
                clienteLocalID = it.clienteLocalID
                fecha = it.fecha
                descripcion = it.descripcion
                sincronizado = it.sincronizado
                estado = it.estado
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param recordatorio Entidad de dominio
     * @return object Entidad
     */
    fun transform(recordatorio: Recordatorio?): RecordatorioEntity? {
        return recordatorio?.let {
            RecordatorioEntity().apply {
                id = it.id
                recordatorioId = it.recordatorioID
                clienteID = it.clienteID
                clienteLocalID = it.clienteLocalID
                fecha = it.fecha
                descripcion = it.descripcion
                sincronizado = it.sincronizado
                estado = it.estado
            }
        }
    }

    fun transform(input: Collection<Recordatorio?>?): List<RecordatorioEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<RecordatorioEntity>()
        }
    }

    fun transform(input: List<RecordatorioEntity?>?): Collection<Recordatorio?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Recordatorio>()
        }
    }

    fun transformLocal(input1: Collection<RecordatorioEntity?>?, input2: Collection<RecordatorioEntity?>?): List<RecordatorioEntity?>? {
        val output = ArrayList<RecordatorioEntity?>()

        input1?.let {
            output.addAll(input1)

            input2?.forEach { entity2 ->
                output.indices.forEach { i ->
                    val entity1 = output[i]

                    if (entity1?.recordatorioId == entity2?.recordatorioId || entity1?.fecha == entity2?.fecha) {
                        output[i]?.recordatorioId = entity2?.recordatorioId
                        output[i]?.clienteID = entity2?.clienteID
                        output[i]?.sincronizado = 1
                        output[i]?.estado = entity2?.estado
                    }
                }
            }

            return output
        } ?: run {
            return emptyList()
        }

    }

}
