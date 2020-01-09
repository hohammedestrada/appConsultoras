package biz.belcorp.consultoras.data.repository.datasource.order

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.service.impl.OrderService

/**
 * Clase de Order encargada de procesar los datos desde un servicio o base de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-10-30
 */
@Singleton
class OrderDataStoreFactory
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param context Contexto que necesita contar el SQLite para ser ejecutado
 */
@Inject
constructor(private val context: Context, private val sessionManager: SessionManager) {

    /**
     * Metodo que por defecto crea la clase que obtendra los datos
     *
     * @return class Clase que se encargara de ejecutar los metodos para obtener los datos
     */
    fun create(): OrderDataStore {
        return createCloud()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createCloud(): OrderDataStore {
        return OrderCloudDataStore(OrderService(context, sessionManager.accessToken,
                sessionManager.appName, sessionManager.country))
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el SQLite
     *
     * @return class Clase que se encargara de obtener los datos desde el SQLite
     */
    fun createDB(): OrderDataStore {
        return OrderDBDataStore(context)
    }

}
