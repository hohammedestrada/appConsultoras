package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.AccountStateEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.accountstate.AccountStateDataStoreFactory
import biz.belcorp.consultoras.domain.entity.AccountState
import biz.belcorp.consultoras.domain.repository.AccountStateRepository
import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class AccountStateDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param accountStateDataStoreFactory Clase encargada de obtener los datos
 */
@Inject
internal constructor(private val accountStateDataStoreFactory: AccountStateDataStoreFactory,
                     private val accountStateEntityDataMapper: AccountStateEntityDataMapper)
    : AccountStateRepository {

    override val fromLocal: Observable<Collection<AccountState?>?>
        get() {
            val localDataStore = accountStateDataStoreFactory.createDB()
            return localDataStore.get()
                .map { accountStateEntities -> accountStateEntityDataMapper.transform(accountStateEntities) }
        }

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(): Observable<Collection<AccountState?>?> {
        val cloudDataStore = accountStateDataStoreFactory.createCloudDataStore()
        val localDataStore = accountStateDataStoreFactory.createDB()
        return cloudDataStore.get().flatMap { r1 ->
            localDataStore.save(r1)
                    .map { accountStateEntities -> accountStateEntityDataMapper.transform(accountStateEntities) }
        }
    }
}
