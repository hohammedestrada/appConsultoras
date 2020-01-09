package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.Anotacion
import io.reactivex.Observable

interface NoteRepository {

    fun getById(id: Int?): Observable<Anotacion?>

    fun listByClientLocalID(clientLocalId: Int?): Observable<Collection<Anotacion?>?>

    fun deleteByClientLocalID(clientLocalId: Int?): Observable<Boolean?>

    fun countByClient(countryISO: String?, clientLocalId: Int?): Observable<Boolean?>

}
