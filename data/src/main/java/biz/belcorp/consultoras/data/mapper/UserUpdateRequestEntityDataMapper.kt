package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.PasswordUpdateRequestEntity

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.UserEntity
import biz.belcorp.consultoras.data.entity.UserUpdateRequestEntity
import biz.belcorp.consultoras.domain.entity.PasswordUpdateRequest
import biz.belcorp.consultoras.domain.entity.UserUpdateRequest

@Singleton
class UserUpdateRequestEntityDataMapper @Inject
internal constructor() {

    fun transform(entity: UserUpdateRequestEntity?): UserUpdateRequest? {
        return entity?.let {
            UserUpdateRequest().apply {
                email = it.email
                phone = it.phone
                mobile = it.mobile
                otherPhone = it.otherPhone
                isAcceptContract = it.isAcceptContract
                sobreNombre = it.sobrenombre
                nombreArchivo = it.nombreArchivo
                tipoArchivo = it.tipoArchivo
            }
        }
    }

    fun transform(entity: UserUpdateRequest?): UserUpdateRequestEntity? {
        return entity?.let {
            UserUpdateRequestEntity().apply {
                email = it.email
                phone = it.phone
                mobile = it.mobile
                otherPhone = it.otherPhone
                isAcceptContract = it.isAcceptContract
                sobrenombre = it.sobreNombre
                nombreArchivo = it.nombreArchivo
                tipoArchivo = it.tipoArchivo
                notificacionesWhatsapp = it.isNotificacionesWhatsapp
                activaNotificacionesWhatsapp = it.isActivaNotificaconesWhatsapp
            }
        }
    }

    fun transformUser(entity: UserUpdateRequest?): UserEntity? {

        return entity?.let {
            UserEntity().apply {
                email = it.email
                phone = it.phone
                mobile = it.mobile
                otherPhone = it.otherPhone
                isAceptaTerminosCondiciones = it.isAcceptContract
                alias = it.sobreNombre
                isNotificacionesWhatsapp = it.isNotificacionesWhatsapp
                isShowCheckWhatsapp = it.isActivaNotificaconesWhatsapp


            }
        }

    }

    fun transform(entity: PasswordUpdateRequest?): PasswordUpdateRequestEntity? {
        return entity?.let {
            PasswordUpdateRequestEntity().apply {
                anteriorContrasenia = it.anteriorContrasenia
                nuevaContrasenia = it.nuevaContrasenia
            }
        }
    }
}
