package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.HybrisDataEntityDataMapper
import biz.belcorp.consultoras.data.mapper.LoginEntityDataMapper
import biz.belcorp.consultoras.data.mapper.UserEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStoreFactory
import biz.belcorp.consultoras.domain.entity.HybrisData
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.repository.UserRepository
import biz.belcorp.consultoras.domain.util.Constant
import biz.belcorp.library.notification.TipoIngreso
import io.reactivex.Observable

@Singleton
class UserDataRepository @Inject
internal constructor(private val userDataStoreFactory: UserDataStoreFactory,
                     private val userEntityDataMapper: UserEntityDataMapper,
                     private val loginEntityDataMapper: LoginEntityDataMapper,
                     private val hybrisDataEntityDataMapper: HybrisDataEntityDataMapper)
    : UserRepository {

    override fun getWithObservable(): Observable<User?> {
        val dataStore = this.userDataStoreFactory.create()
        return dataStore.getWithObservable().map { userEntityDataMapper.transform(it) }
    }

    override suspend fun getWithCoroutines(): User? {
        val dataStore = this.userDataStoreFactory.create()
        return userEntityDataMapper.transform(dataStore.get())
    }

    override fun getLogin(): Observable<Login?> {
        val dataStore = this.userDataStoreFactory.create()
        return dataStore.getWithObservable().map {
            userEntityDataMapper.transformToLogin(it)
        }
    }

    override fun save(login: Login?): Observable<Boolean?> {
        val dataStore = this.userDataStoreFactory.create()
        login?.tipoIngreso = TipoIngreso.ESTANDAR
        return dataStore.save(loginEntityDataMapper.transformUser(login)!!)
    }

    override fun removeAll(): Observable<Boolean?> {
        val dataStore = this.userDataStoreFactory.create()
        return dataStore.removeAll()
    }

    override fun updateAcceptances(terms: Boolean?, privacity: Boolean?): Observable<Boolean?> {
        val dataStore = this.userDataStoreFactory.create()
        return dataStore.updateAcceptances(terms, privacity)
    }

    override fun updateAcceptancesCreditAgreement(creditAgreement: Boolean?): Observable<Boolean?> {
        val dataStore = this.userDataStoreFactory.create()
        return dataStore.updateAcceptancesCreditAgreement(creditAgreement)
    }

    override fun saveHybrisData(hybrisData: HybrisData?): Observable<Boolean?> {
        val dataStore = userDataStoreFactory.create()
        return dataStore.saveHybrisData(hybrisDataEntityDataMapper.transform(hybrisData)!!)
    }

    override fun updateTipoIngreso(pushNotification: String): Observable<Boolean?> {
        val dataStore = this.userDataStoreFactory.create()
        return dataStore.getWithObservable().flatMap {
            it.tipoIngreso = pushNotification
            dataStore.save(it)
        }
    }

    override fun checkGanaMasNativo(): Observable<Boolean?> {
        val dataStore = this.userDataStoreFactory.create()
        return dataStore.getWithObservable().map { it.isGanaMasNativo }
    }

}
