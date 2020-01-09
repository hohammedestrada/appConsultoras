package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.AssociateRequestEntity
import biz.belcorp.consultoras.domain.entity.AssociateRequest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class AssociateRequestEntityDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(entity: AssociateRequestEntity?): AssociateRequest? {
        return entity?.let {
            AssociateRequest().apply {
                codigoUsuario = it.codigoUsuario
                claveSecreta = it.claveSecreta
                grandType = it.grandType
                tipoAutenticacion = it.tipoAutenticacion
                paisISO = it.paisISO
                idAplicacion = it.idAplicacion
                fotoPerfil = it.fotoPerfil
                login = it.login
                correo = it.correo
                nombres = it.nombres
                apellidos = it.apellidos
                linkPerfil = it.linkPerfil
                fechaNacimiento = it.fechaNacimiento
                genero = it.genero
                ubicacion = it.ubicacion
                proveedor = it.proveedor
                refreshToken = it.refreshToken
                authorization = it.authorization
            }
        }
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    fun transform(entity: AssociateRequest?): AssociateRequestEntity? {

        return entity?.let {
            return AssociateRequestEntity().apply {
                codigoUsuario = it.codigoUsuario
                claveSecreta = it.claveSecreta
                grandType = it.grandType
                tipoAutenticacion = it.tipoAutenticacion
                paisISO = it.paisISO
                idAplicacion = it.idAplicacion
                fotoPerfil = it.fotoPerfil
                login = it.login
                correo = it.correo
                nombres = it.nombres
                apellidos = it.apellidos
                linkPerfil = it.linkPerfil
                fechaNacimiento = it.fechaNacimiento
                genero = it.genero
                ubicacion = it.ubicacion
                proveedor = it.proveedor
                refreshToken = it.refreshToken
                authorization = it.authorization
            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    fun transform(list: Collection<AssociateRequestEntity?>?): List<AssociateRequest?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<AssociateRequest>()
        }
    }

}
