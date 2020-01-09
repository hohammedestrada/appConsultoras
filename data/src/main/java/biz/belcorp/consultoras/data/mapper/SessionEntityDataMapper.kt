package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.SessionEntity
import biz.belcorp.consultoras.data.entity.AppEntity
import biz.belcorp.consultoras.domain.entity.App
import biz.belcorp.consultoras.domain.entity.Session

@Singleton
class SessionEntityDataMapper @Inject
internal constructor() {

    fun transform(entity: SessionEntity?): Session? {
        return entity?.let {
            Session().apply {
                countrySIM = it.countrySIM
                authType = it.authType
                username = it.username
                password = it.password
                email = it.email
                country = it.country
                tokenType = it.tokenType
                accessToken = it.accessToken
                refreshToken = it.refreshToken
                expiresIn = it.expiresIn
                issued = it.issued
                expires = it.expires
                isTutorial = it.isTutorial
                isLogged = it.isLogged
                started = it.started
                updated = it.updated
                isAceptaTerminosCondiciones = it.isAceptaTerminosCondiciones
                ordersCount = it.ordersCount
            }
        }
    }

    fun transform(entity: Session?): SessionEntity? {
        return entity?.let {
            SessionEntity().apply {
                countrySIM = it.countrySIM
                authType = it.authType
                username = it.username
                password = it.password
                email = it.email
                country = it.country
                tokenType = it.tokenType
                accessToken = it.accessToken
                refreshToken = it.refreshToken
                expiresIn = it.expiresIn
                issued = it.issued
                expires = it.expires
                isTutorial = it.isTutorial
                isLogged = it.isLogged
                started = it.started
                updated = it.updated
                isAceptaTerminosCondiciones = it.isAceptaTerminosCondiciones
                ordersCount = it.ordersCount
            }
        }
    }

    fun transform(entity: AppEntity?): App? {
        return entity?.let {
            App().apply {
                aplicacion = it.aplicacion
                pais = it.pais
                so = it.so
                version = it.version
                minimaVersion = it.minimaVersion
                fechaLanzamiento = it.fechaLanzamiento
                fechaActualizacion = it.fechaActualizacion
                isRequiereActualizacion = it.isRequiereActualizacion
                tipoDescarga = it.tipoDescarga
                url = it.url
            }
        }
    }

    fun transform(entity: App?): AppEntity? {
        return entity?.let {
            AppEntity().apply {
                aplicacion = it.aplicacion
                pais = it.pais
                so = it.so
                version = it.version
                minimaVersion = it.minimaVersion
                fechaLanzamiento = it.fechaLanzamiento
                fechaActualizacion = it.fechaActualizacion
                isRequiereActualizacion = it.isRequiereActualizacion
                tipoDescarga = it.tipoDescarga
                url = it.url
            }
        }
    }
}
