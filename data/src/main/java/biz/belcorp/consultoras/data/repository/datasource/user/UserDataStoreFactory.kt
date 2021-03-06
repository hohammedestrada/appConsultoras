package biz.belcorp.consultoras.data.repository.datasource.user

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.BuildConfig
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.ApiService

/**
 * Clase de Login encargada de procesar los datos desde un servicio o base de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class UserDataStoreFactory
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param context Contexto que necesita contar el SQLite para ser ejecutado
 */
@Inject
internal constructor(context: Context, private val sessionManager: ISessionManager) {

    private val context: Context = context.applicationContext

    /**
     * Metodo que por defecto crea la clase que obtendra los datos
     *
     * @return class Clase que se encargara de ejecutar los metodos para obtener los datos
     */
    fun create(): UserDataStore {
        return createDB()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createCloudDataStore(): UserDataStore {
        val service = ApiService(context, BuildConfig.BASE_API, sessionManager.appName, sessionManager.country)

        return UserCloudDataStore(service)
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createDB(): UserDataStore {
        return UserDBDataStore(context)
    }
}
