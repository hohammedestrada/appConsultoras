package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.FacebookProfile
import io.reactivex.Observable

interface FacebookRepository {

    fun get(): Observable<FacebookProfile?>

    fun save(facebookProfile: FacebookProfile?): Observable<Boolean?>
}
