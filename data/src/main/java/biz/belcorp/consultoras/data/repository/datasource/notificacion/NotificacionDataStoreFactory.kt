package biz.belcorp.consultoras.data.repository.datasource.notificacion


import android.content.Context
import biz.belcorp.consultoras.data.manager.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase de Config encargada de procesar los datos desde un servicio o base de datos
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class NotificacionDataStoreFactory
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param context Contexto que necesita contar el SQLite para ser ejecutado
 */
@Inject
internal constructor(context: Context, private val sessionManager: SessionManager) {

    private val context: Context = context.applicationContext


    /**
     * Metodo que por defecto crea la clase que obtendra los datos
     *
     * @return class Clase que se encargara de ejecutar los metodos para obtener los datos
     */
    fun create(): NotificacionDataStore {
        return NotificacionDBDataStore()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el SQLite
     *
     * @return class Clase que se encargara de obtener los datos desde el SQLite
     */
    fun createDB(): NotificacionDataStore {
        return NotificacionDBDataStore()
    }

}
