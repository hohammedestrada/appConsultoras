package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.FacebookEntityProfileDataMapper
import biz.belcorp.consultoras.data.repository.datasource.facebook.FacebookDataStoreFactory
import biz.belcorp.consultoras.domain.entity.FacebookProfile
import biz.belcorp.consultoras.domain.repository.FacebookRepository
import io.reactivex.Observable

@Singleton
class FacebookDataRepository @Inject
internal constructor(private val facebookDataStoreFactory: FacebookDataStoreFactory,
                     private val facebookEntityProfileDataMapper: FacebookEntityProfileDataMapper)
    : FacebookRepository {

    override fun get(): Observable<FacebookProfile?> {
        val dataStore = this.facebookDataStoreFactory.create()
        return dataStore.get().map { facebookEntityProfileDataMapper.transform(it) }
    }

    override fun save(facebookProfile: FacebookProfile?): Observable<Boolean?> {
        val dataStore = this.facebookDataStoreFactory.create()
        return dataStore.save(facebookEntityProfileDataMapper.transform(facebookProfile)!!)
            .map { result -> result }
    }
}
