package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ContactoEntity
import biz.belcorp.consultoras.domain.entity.Contacto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactoEntityDataMapper @Inject
internal constructor() {

    fun transform(input: Contacto?): ContactoEntity? {
        return input?.let {
            ContactoEntity().apply{
                contactoClienteID = it.contactoClienteID
                clienteID = it.clienteID
                tipoContactoID = it.tipoContactoID
                valor = it.valor
                estado = it.estado
                id = it.id
            }
        }
    }

    fun transform(input: ContactoEntity?): Contacto? {
        return input?.let {
            Contacto().apply{
                contactoClienteID = it.contactoClienteID
                clienteID = it.clienteID
                tipoContactoID = it.tipoContactoID
                valor = it.valor
                estado = it.estado
                id = it.id
            }
        }
    }

    fun transform(input: Collection<Contacto?>?): List<ContactoEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ContactoEntity>()
        }
    }

    fun transform(input: List<ContactoEntity?>?): Collection<Contacto?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<Contacto>()
        }
    }

}
