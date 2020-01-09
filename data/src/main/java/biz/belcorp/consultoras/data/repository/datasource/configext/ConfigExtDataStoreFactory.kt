package biz.belcorp.consultoras.data.repository.datasource.configext


import android.content.Context
import biz.belcorp.consultoras.data.net.service.impl.ConfigExtService

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.net.service.impl.ConfigService


@Singleton
class ConfigExtDataStoreFactory
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param context Contexto que necesita contar el SQLite para ser ejecutado
 */
@Inject
internal constructor(context: Context) {

    private val context: Context = context.applicationContext

    /**
     * Metodo que por defecto crea la clase que obtendra los datos
     *
     * @return class Clase que se encargara de ejecutar los metodos para obtener los datos
     */
    fun create(): ConfigExtDataStore {
        return createCloudDataStore()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el SQLite
     *
     * @return class Clase que se encargara de obtener los datos desde el SQLite
     */
    fun createDB(): ConfigExtDataStore {
        return ConfigExtDBDataStore(context)
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createCloudDataStore(): ConfigExtDataStore {
        val service = ConfigExtService(context)
        return ConfigExtCloudDataStore(service)
    }
}
