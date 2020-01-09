package biz.belcorp.consultoras.data.repository.datasource.premio

import android.content.Context
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.PremioService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PremioDataStoreFactory

    @Inject
    constructor(private val context: Context, private val sessionManager: ISessionManager) {

    /**
     * Metodo que por defecto crea la clase que obtendra los datos
     *
     * @return class Clase que se encargara de ejecutar los metodos para obtener los datos
     */
    fun create(): PremioDataStore {
        return createCloud()
    }

    /**
     * Metodo que crea la clase que obtendra los datos desde el Servicio
     *
     * @return class Clase que se encargara de obtener los datos desde el Servicio
     */
    fun createCloud(): PremioDataStore {
        return PremioCloudDataStore(PremioService(context, sessionManager.accessToken,
            sessionManager.appName, sessionManager.country))
    }

    //fun createDB() : PremioDataStore{
    //return FestivalDBDataSore()
    //}

    }
