package biz.belcorp.consultoras.data.repository.datasource.offer

import android.content.Context
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.service.impl.OfferService
import biz.belcorp.consultoras.data.net.service.impl.OrderService
import biz.belcorp.consultoras.data.repository.datasource.order.OrderCloudDataStore
import biz.belcorp.consultoras.data.repository.datasource.order.OrderDBDataStore
import biz.belcorp.consultoras.data.repository.datasource.order.OrderDataStore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OfferDataStoreFactory
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
    fun create(): OfferDataStore {
        return createCloud()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createCloud(): OfferDataStore {
        return OfferCloudDataStore(OfferService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country))
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el SQLite
     *
     * @return class Clase que se encargara de obtener los datos desde el SQLite
     */
    fun createDB(): OfferDataStore {
        return OfferDBDataStore(context)
    }
    
}
