package biz.belcorp.consultoras.data.repository.datasource.recordatory

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.RecordatoryService

/**
 *
 */
@Singleton
class RecordatoryDataStoreFactory
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
    fun create(): RecordatoryDataStore {
        return createCloudDataStore()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createCloudDataStore(): RecordatoryDataStore {
        val service = RecordatoryService(context,
                sessionManager.accessToken, sessionManager.appName,
                sessionManager.country)

        return RecordatoryCloudDataStore(service)
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde la BD
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createDBDataStore(): RecordatoryDataStore {
        return RecordatoryDBDataStore()
    }
}
