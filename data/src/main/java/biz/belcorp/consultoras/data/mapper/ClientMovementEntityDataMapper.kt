package biz.belcorp.consultoras.data.mapper

import java.math.BigDecimal
import java.text.ParseException
import java.util.ArrayList
import java.util.Date
import java.util.HashMap

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.ClientMovementEntity
import biz.belcorp.consultoras.domain.entity.ClientMovement
import biz.belcorp.library.annotation.DatetimeFormat
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DateUtil

@Singleton
class ClientMovementEntityDataMapper @Inject
internal constructor(private val productMovementEntityDataMapper: ProductMovementEntityDataMapper) {

    fun transform(input: ClientMovement?): ClientMovementEntity? {
        return input?.let {
            ClientMovementEntity().apply {
                movementID = it.movementID
                clientID = it.clientID
                id = it.id
                clienteLocalID = it.clienteLocalID
                clientCode = it.clientCode
                amount = it.amount
                movementType = it.type
                description = it.description
                campaingCode = it.campaing
                note = it.note
                date = it.date
                saldo = it.saldo
                sincronizado = it.sincronizado
                estado = it.estado
                code = it.code
                message = it.message
                productList = productMovementEntityDataMapper.transform(it.productMovements)
            }
        }
    }

    fun transform(input: ClientMovementEntity?): ClientMovement? {
        return input?.let {
            ClientMovement().apply {
                movementID = it.movementID
                clientID = it.clientID
                id = it.id
                clienteLocalID = it.clienteLocalID
                sincronizado = it.sincronizado
                clientCode = it.clientCode
                amount = it.amount
                type = it.movementType
                description = it.description
                campaing = it.campaingCode
                note = it.note
                date = it.date
                saldo = it.saldo
                estado = it.estado
                code = it.code
                message = it.message
                productMovements = productMovementEntityDataMapper.transformResponse(it.productList)
            }
        }
    }

    fun transform(input: Collection<ClientMovement?>?): List<ClientMovementEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ClientMovementEntity>()
        }
    }

    fun transform(input: List<ClientMovementEntity?>?): Collection<ClientMovement?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ClientMovement>()
        }
    }

    fun transformLocal(input1: Collection<ClientMovementEntity?>?,
                       input2: Collection<ClientMovementEntity?>?): List<ClientMovementEntity?>? {
        val output = ArrayList<ClientMovementEntity?>()

        input1?.let {
            output.addAll(input1)

            input2?.let {
                for (entity2 in input2) {
                    for (i in output.indices) {
                        val entity1 = output[i]

                        if (entity1?.movementType == "A" && entity1.amount!! > BigDecimal.ZERO)
                            entity1.amount = entity1.amount!!.negate()

                        if (null != entity2?.code && entity2.code == "0000"
                            && (entity1?.movementID == entity2.movementID || compareMovements(entity1, entity2))) {
                            output[i]?.movementID = entity2.movementID
                            output[i]?.clientID = entity2.clientID
                            output[i]?.clientCode = entity2.clientCode
                            output[i]?.amount = entity2.amount
                            output[i]?.sincronizado = 1
                            output[i]?.estado = entity2.estado
                        }
                    }
                }
                return output
            }?: run {
                return emptyList()
            }

        } ?: run {
            return emptyList()
        }


    }

    private fun compareMovements(entity1: ClientMovementEntity?, entity2: ClientMovementEntity?): Boolean {

        if (null == entity1 || null == entity2)
            return false

        val map1 = getMap(entity1)
        val map2 = getMap(entity2)

        return map1 == map2
    }

    private fun getMap(entity: ClientMovementEntity): Map<Date, BigDecimal> {
        val map = HashMap<Date?, BigDecimal?>()
        val date = if (entity.date != null) entity.date else ""

        try {
            val dateFormat = DateUtil.convertEngFechaToDate(date, DatetimeFormat.ISO_8601)
            map[dateFormat] = entity.amount
        } catch (e: ParseException) {
            BelcorpLogger.w("getMap", "getMap", e.message)
        }

        return map as Map<Date, BigDecimal>
    }

}
