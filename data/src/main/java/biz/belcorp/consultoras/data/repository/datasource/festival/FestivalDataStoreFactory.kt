package biz.belcorp.consultoras.data.repository.datasource.festival

import android.content.Context
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.net.service.impl.FestivalService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FestivalDataStoreFactory

    @Inject
    constructor(private val context: Context, private val sessionManager: ISessionManager) {

        /**
         * Metodo que por defecto crea la clase que obtendra los datos
         *
         * @return class Clase que se encargara de ejecutar los metodos para obtener los datos
         */
        fun create(): FestivalDataStore {
            return createCloud()
        }

        /**
         * Metodo que crea la clase que obtendra los datos desde el Servicio
         *
         * @return class Clase que se encargara de obtener los datos desde el Servicio
         */
        fun createCloud(): FestivalDataStore {
            return FestivalCloudDataStore(FestivalService(context, sessionManager.accessToken,
                sessionManager.appName, sessionManager.country))
        }

        fun createDB() : FestivalDataStore{
            return FestivalDBDataSore()
        }

    }

