package biz.belcorp.consultoras.data.repository.datasource.params

import biz.belcorp.consultoras.data.entity.ParamsEntity
import io.reactivex.Observable

interface ParamsDataStore {
    fun get(): ParamsEntity?
    fun save(params: ParamsEntity?): Observable<Boolean?>
    suspend fun saveCoroutine(params: ParamsEntity?):Boolean?
}
