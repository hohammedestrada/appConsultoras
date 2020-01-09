package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.ConfigEntityDataMapper
import biz.belcorp.consultoras.data.mapper.ConfigResponseEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.config.ConfigDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.session.SessionDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Config
import biz.belcorp.consultoras.domain.entity.ConfigReponse
import biz.belcorp.consultoras.domain.repository.ConfigRepository
import io.reactivex.Observable

/**
 * Clase de Configuracion encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Singleton
class ConfigDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param configDataStoreFactory Clase encargada de obtener los datos
 */
@Inject
internal constructor(private val configDataStoreFactory: ConfigDataStoreFactory,
                     private val sessionDataStoreFactory: SessionDataStoreFactory,
                     private val configResponseEntityDataMapper: ConfigResponseEntityDataMapper,
                     private val configEntityDataMapper: ConfigEntityDataMapper) : ConfigRepository {

    /**
     * Metodo que obtiene el listado de paises
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(): Observable<ConfigReponse?> {
        val dataStore = this.configDataStoreFactory.create()
        val localDataStore = this.configDataStoreFactory.createDB()

        var res = dataStore.get().flatMap<ConfigReponse> {
            configResponseEntity ->
            dataStore.get()
            localDataStore.save(configResponseEntity)
            .map<ConfigReponse> {
                this.configResponseEntityDataMapper.transform(configResponseEntity) } }
        return res
    }

    /**
     * Metodo que obtiene el listado de paises de la bd local
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getFromLocal(): Observable<ConfigReponse?> {
        val dataStore = this.configDataStoreFactory.createDB()
        val res = dataStore.get().map(configResponseEntityDataMapper::transform)
        return  res
    }

    override fun save(appName: String): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.saveAppName(appName)
    }

    override fun save(config: Config?): Observable<Boolean?> {
        val dataStore = this.configDataStoreFactory.createDB()
        return dataStore.save(this.configEntityDataMapper.transform(config)).map { result -> result }
    }

}
