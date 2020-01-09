package biz.belcorp.consultoras.data.repository.datasource.catalog

import biz.belcorp.consultoras.data.entity.CatalogoEntity
import biz.belcorp.consultoras.data.entity.UserCatalogoRequestEntity
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

interface CatalogDataStore {
    operator fun get(request: UserCatalogoRequestEntity?): Observable<List<CatalogoEntity?>?>
    suspend fun getWithCoroutine(request: UserCatalogoRequestEntity?): List<CatalogoEntity?>?
    fun save(entities: List<CatalogoEntity?>?): Observable<List<CatalogoEntity?>?>
    suspend fun saveCoroutine(entities: List<CatalogoEntity?>?):List<CatalogoEntity?>?
    fun hasData(): Boolean
    fun getUrlDescarga(descripcion: String?) : Deferred<String?>
    fun getObservableUrlDescarga(descripcion: String?):Observable<String?>
}
