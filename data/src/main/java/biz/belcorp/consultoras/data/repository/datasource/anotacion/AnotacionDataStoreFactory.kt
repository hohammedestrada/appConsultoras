package biz.belcorp.consultoras.data.repository.datasource.anotacion

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.AnotacionService

/**
 * Clase de Incentivo encargada de procesar los datos desde un servicio o base de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class AnotacionDataStoreFactory
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
    fun create(): AnotacionDataStore {
        return createDB()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el SQLite
     *
     * @return class Clase que se encargara de obtener los datos desde el SQLite
     */
    fun createDB(): AnotacionDataStore {
        return AnotacionDBDataStore(context)
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createCloudDataStore(): AnotacionDataStore {
        val service = AnotacionService(context, sessionManager.accessToken,
                sessionManager.appName, sessionManager.country)
        return AnotacionCloudDataStore(service)
    }

}
