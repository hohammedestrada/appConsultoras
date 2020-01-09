package biz.belcorp.consultoras.data.mapper

import java.text.ParseException
import java.util.ArrayList
import java.util.Date
import java.util.HashMap

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.AnotacionEntity
import biz.belcorp.consultoras.domain.entity.Anotacion
import biz.belcorp.library.annotation.DatetimeFormat
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DateUtil

@Singleton
class AnotacionEntityDataMapper @Inject
internal constructor() {

    fun transform(input: Anotacion?): AnotacionEntity? {
        return input?.let {
            AnotacionEntity().apply {
                anotacionID = it.anotacionID
                descripcion = it.descripcion
                estado = it.estado
                clienteID = it.clienteID
                fecha = it.fecha
                id = it.id
                clienteLocalID = it.clienteLocalID
                sincronizado = it.sincronizado
            }
        }
    }

    fun transform(input: AnotacionEntity?): Anotacion? {
        return input?.let {
            Anotacion().apply {
                anotacionID = it.anotacionID
                descripcion = it.descripcion
                estado = it.estado
                clienteID = it.clienteID
                fecha = it.fecha
                id = it.id
                clienteLocalID = it.clienteLocalID
                sincronizado = it.sincronizado
            }
        }
    }

    fun transform(input: Collection<Anotacion?>?): List<AnotacionEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<AnotacionEntity>()
        }
    }

    fun transform(input: List<AnotacionEntity?>?): Collection<Anotacion?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Anotacion>()
        }
    }

    fun transformLocal(input1: Collection<AnotacionEntity?>?, input2: Collection<AnotacionEntity?>?): List<AnotacionEntity?>? {
        val output = ArrayList<AnotacionEntity?>()


        input1?.let {

            output.addAll(input1)

            if (input2 != null) {
                for (entity2 in input2) {
                    for (i in output.indices) {
                        val entity1 = output[i]

                        if (entity1?.anotacionID == entity2?.anotacionID || compareAnotations(entity1, entity2)) {
                            output[i]?.anotacionID = entity2?.anotacionID
                            output[i]?.clienteID = entity2?.clienteID
                            output[i]?.sincronizado = 1
                            output[i]?.estado = entity2?.estado
                        }
                    }
                }
            }
            return output

        } ?: run{
            return emptyList()
        }


    }

    private fun compareAnotations(noteEntity1: AnotacionEntity?, noteEntity2: AnotacionEntity?): Boolean {

        if (null == noteEntity1 || null == noteEntity2)
            return false

        val map1 = getMapNote(noteEntity1)
        val map2 = getMapNote(noteEntity2)

        return map1 == map2
    }

    private fun getMapNote(note: AnotacionEntity): Map<String, Date> {
        val map = HashMap<String, Date>()
        val date = if (note.fecha != null) note.fecha else ""

        try {
            val dateFormat = DateUtil.convertEngFechaToDate(date, DatetimeFormat.RFC_3339)
            map[note.descripcion!!] = dateFormat
        } catch (e: ParseException) {
            BelcorpLogger.w("getMapNote", "getMapNote", e.message)
        }

        return map
    }

}
