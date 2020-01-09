package biz.belcorp.consultoras.data.repository.datasource.facebook

import biz.belcorp.consultoras.data.entity.FacebookProfileEntity
import io.reactivex.Observable

interface FacebookDataStore {

    fun get(): Observable<FacebookProfileEntity?>

    fun save(entity: FacebookProfileEntity?): Observable<Boolean?>

}
