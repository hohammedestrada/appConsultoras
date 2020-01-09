package biz.belcorp.consultoras.data.mapper

import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.ClienteEntity
import biz.belcorp.consultoras.data.entity.ContactoEntity
import biz.belcorp.consultoras.data.entity.DeudorRequestEntity
import biz.belcorp.consultoras.domain.entity.Anotacion
import biz.belcorp.consultoras.domain.entity.ClientMovement
import biz.belcorp.consultoras.domain.entity.Cliente
import biz.belcorp.consultoras.domain.entity.Contacto
import biz.belcorp.consultoras.domain.entity.Deudor
import biz.belcorp.consultoras.domain.entity.Recordatorio

@Singleton
class ClienteEntityDataMapper @Inject
internal constructor(private val contactoEntityDataMapper: ContactoEntityDataMapper,
                     private val anotacionEntityDataMapper: AnotacionEntityDataMapper,
                     private val recordatorioEntityDataMapper: RecordatorioEntityDataMapper,
                     private val movementEntityDataMapper: ClientMovementEntityDataMapper) {

    fun transform(input: Cliente?): ClienteEntity? {

        return input?.let { input ->
            ClienteEntity().apply {
                origen = input.origen
                clienteID = input.clienteID
                apellidos = input.apellidos
                nombres = input.nombres
                alias = input.alias
                foto = input.foto
                fechaNacimiento = input.fechaNacimiento
                sexo = input.sexo
                documento = input.documento
                favorito = input.favorito
                estado = input.estado
                totalDeuda = input.totalDeuda
                tipoRegistro = input.tipoRegistro
                tipoContactoFavorito = input.tipoContactoFavorito
                contactoEntities = contactoEntityDataMapper.transform(input.contactos)
                anotacionEntities = anotacionEntityDataMapper.transform(input.anotaciones)
                recordatorioEntities = recordatorioEntityDataMapper.transform(input.recordatorios)
                movimientoEntities = movementEntityDataMapper.transform(input.movements)
                id = input.id
                sincronizado = input.sincronizado
                cantidadProductos = input.cantidadProductos
                cantidadPedido = input.cantidadPedido
                montoPedido = input.montoPedido
                codigoRespuesta = input.codigoRespuesta
                mensajeRespuesta = input.mensajeRespuesta
            }
        }
    }

    fun transform(input: ClienteEntity?): Cliente? {
        return input?.let { input ->
            Cliente().apply {
                origen = input.origen
                clienteID = input.clienteID
                apellidos = input.apellidos
                nombres = input.nombres
                alias = input.alias
                foto = input.foto
                fechaNacimiento = input.fechaNacimiento
                sexo = input.sexo
                documento = input.documento
                favorito = input.favorito
                estado = input.estado
                totalDeuda = input.totalDeuda
                tipoRegistro = input.tipoRegistro
                tipoContactoFavorito = input.tipoContactoFavorito
                contactos = contactoEntityDataMapper.transform(input.contactoEntities) as List<Contacto>
                anotaciones = anotacionEntityDataMapper.transform(input.anotacionEntities) as List<Anotacion?>?
                recordatorios = recordatorioEntityDataMapper.transform(input.recordatorioEntities) as List<Recordatorio>
                movements = movementEntityDataMapper.transform(input.movimientoEntities) as List<ClientMovement>
                id = input.id
                sincronizado = input.sincronizado
                cantidadProductos = input.cantidadProductos
                cantidadPedido = input.cantidadPedido
                montoPedido = input.montoPedido
                codigoRespuesta = input.codigoRespuesta
                mensajeRespuesta = input.mensajeRespuesta
            }
        }
    }

    fun transform(input: DeudorRequestEntity?): Cliente? {
        var output: Cliente? = null

        if (null != input) {
            output = Cliente()
            output.clienteID = input.clienteID
            output.totalDeuda = input.totalDeuda
        }
        return output
    }

    fun transform(input: Collection<Cliente?>?): List<ClienteEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ClienteEntity>()
        }
    }


    fun transform(input1: Collection<ClienteEntity?>?, input2: Collection<Deudor?>?): List<Cliente?>? {
        val output = transformCollection(input1)

        if (null == input1) {
            return emptyList()
        }

        input2?.filterNotNull()?.forEach { entity2 ->
            output?.let {
                output.indices
                    .filter { it1 -> output[it1]?.clienteID == entity2.clienteID }
                    .forEach { it1 -> output[it1]?.totalDeuda = entity2.totalDeuda }
            }
        }

        return output
    }


    fun transformCombine(input1: List<ClienteEntity?>?, input2: List<DeudorRequestEntity?>?): List<ClienteEntity?>? {
        val output = ArrayList<ClienteEntity?>()

        if (null == input1) {
            return emptyList()
        }

        output.addAll(input1.filterNotNull())

        input2?.let {
            it.filterNotNull().forEach { entity2 ->
                output.let { _ ->
                    output.indices
                        .filter { it1 -> null == output[it1]?.error
                                    && output[it1]?.clienteID == entity2.clienteID }
                        .forEach { it1 ->  output[it1]?.totalDeuda = entity2.totalDeuda }
                }

            }
        }


        return output
    }

    fun transform(input: List<ClienteEntity?>?): List<Cliente?>? {
        val output = ArrayList<Cliente>()

        if (null == input) {
            return emptyList()
        }

        input.forEach {
            val model = transform(it)
            model?.let { it1 -> output.add(it1) }

        }
        return output
    }

    private fun transformCollection(input: Collection<ClienteEntity?>?): List<Cliente?>?{
        val output = ArrayList<Cliente>()

        if (null == input) {
            return emptyList()
        }

        input.forEach {
            val model = transform(it)
            model?.let { it1 -> output.add(it1) }
        }

        return output
    }

    //Deudor

    private fun transformD(input: DeudorRequestEntity?): Deudor? {

        input?.let {
            val output = Deudor()
            output.clienteID = input.clienteID
            output.totalDeuda = input.totalDeuda
            input.recordatorio?.let { _ ->
                val oRecordatorio = Recordatorio()
                oRecordatorio.id = input.recordatorio!!.clienteID
                oRecordatorio.recordatorioID = input.recordatorio!!.recordatorioId
                oRecordatorio.fecha = input.recordatorio!!.fecha
                oRecordatorio.estado = input.recordatorio!!.estado
                output.recordatorio = oRecordatorio
                return output
            }
        }
        return null
    }

    fun transformD(input: List<DeudorRequestEntity?>?): Collection<Deudor?>? {

        if (null == input) {
            return emptyList()
        }
        return input
            .map { transformD(it) }
            .filter { it != null }
    }

    fun transformLocal(input1: Collection<ClienteEntity?>?, input2: Collection<ClienteEntity?>?): List<ClienteEntity?>? {
        val output = ArrayList<ClienteEntity>()

        if (null == input1) return emptyList()

        output.addAll(input1.filterNotNull())

        input2?.filterNotNull()?.forEach { entity2 ->
            for (i in output.indices) {
                val entity1 = output[i]

                if (null != entity2.codigoRespuesta && entity2.codigoRespuesta == "0000"
                        && (entity1.clienteID == entity2.clienteID || compareContacts(entity1, entity2))) {
                    output[i].clienteID = entity2.clienteID
                    output[i].sincronizado = 1

                    output[i].contactoEntities = updateContactList(entity1.contactoEntities, entity2.clienteID!!)

                    output[i].anotacionEntities = anotacionEntityDataMapper.transformLocal(entity1.anotacionEntities, entity2.anotacionEntities)

                    output[i].recordatorioEntities = recordatorioEntityDataMapper.transformLocal(entity1.recordatorioEntities, entity2.recordatorioEntities)

                    output[i].movimientoEntities = movementEntityDataMapper.transformLocal(entity1.movimientoEntities, entity2.movimientoEntities)
                }
            }
        }

        return output
    }

    private fun compareContacts(clienteEntity1: ClienteEntity, clienteEntity2: ClienteEntity): Boolean {

        if (null == clienteEntity1.contactoEntities || null == clienteEntity2.contactoEntities)
            return false

        val listContact1 = clienteEntity1.contactoEntities
        val listContact2 = clienteEntity2.contactoEntities

        if (listContact1!!.size != listContact2!!.size)
            return false

        val mapContact1 = getMapContact(listContact1)
        val mapContact2 = getMapContact(listContact2)

        return mapContact1 == mapContact2
    }

    private fun getMapContact(listContact: List<ContactoEntity?>?): Map<Int?, String?> {
        val mapContact = HashMap<Int?, String?>()
        listContact?.let {
            for (it in listContact)
                it?.let {
                    mapContact[it.tipoContactoID] = it.valor
                }
        }
        return mapContact
    }


    private fun updateContactList(list: List<ContactoEntity?>?, clientID: Int): List<ContactoEntity?>? {

        val listUpdate = ArrayList<ContactoEntity?>()

        list?.let {
            listUpdate.addAll(list)
            for (j in list.indices) {
                listUpdate[j]?.clienteID = clientID
            }
        }


        return listUpdate
    }


    fun transform(clientID: Int, input: List<ClienteEntity?>?): List<Cliente?>? {
        val output = ArrayList<Cliente>()
        val outputTemp = ArrayList<Cliente>()

        if (null == input) {
            return emptyList()
        }

        input.forEach {
            val model = transform(it)
            model?.let { it1 ->
                if (model.clienteID == clientID)
                    output.add(it1)
                else
                    outputTemp.add(it1)
            }
        }

        if (!outputTemp.isEmpty()) output.addAll(outputTemp)

        return output
    }

}
