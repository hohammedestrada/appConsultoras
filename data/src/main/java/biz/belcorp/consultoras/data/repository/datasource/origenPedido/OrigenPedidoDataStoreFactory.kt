package biz.belcorp.consultoras.data.repository.datasource.origenPedido

import android.content.Context
import biz.belcorp.consultoras.data.BuildConfig
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.ApiService
import biz.belcorp.consultoras.data.repository.datasource.user.UserCloudDataStore
import biz.belcorp.consultoras.data.repository.datasource.user.UserDBDataStore
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStore
import javax.inject.Inject
import javax.inject.Singleton


/**
 *
 *
 *
 * @version 1.0
 * @since
 */
@Singleton
class OrigenPedidoDataStoreFactory
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
    fun create(): OrigenPedidoDataStore {
        return createDB()
    }


    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createDB(): OrigenPedidoDataStore {
        return OrigenPedidoDBDataStore(context)
    }
}
