package biz.belcorp.consultoras.data.repository.datasource.catalog

import android.util.Log
import biz.belcorp.consultoras.data.entity.CatalogoEntity
import biz.belcorp.consultoras.data.entity.UserCatalogoRequestEntity
import biz.belcorp.consultoras.data.net.service.ICatalogService
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

/**
 *
 */
class CatalogCloudDataStore internal constructor(private val service: ICatalogService)
    : CatalogDataStore {
    override suspend fun saveCoroutine(entities: List<CatalogoEntity?>?):List<CatalogoEntity?>? {
        throw UnsupportedOperationException()
    }

    override fun get(request: UserCatalogoRequestEntity?)
        : Observable<List<CatalogoEntity?>?> {
        request?.let {
            return service[request.campania, request.codigoZona, request.topLast, request.topNext,
                request.campaignNumber, request.isShowCurrent, request.isBrillante]
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override suspend fun getWithCoroutine(peticion: UserCatalogoRequestEntity?): List<CatalogoEntity?>? {
       return peticion?.let {request->
            service.getCoroutine(request.campania, request.codigoZona, request.topLast, request.topNext,
                request.campaignNumber, request.isShowCurrent, request.isBrillante).await()
        }?:kotlin.run {throw NullPointerException()}
    }

    override fun save(entities: List<CatalogoEntity?>?): Observable<List<CatalogoEntity?>?> {
        throw UnsupportedOperationException()
    }

    override fun hasData(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun getUrlDescarga(descripcion: String?): Deferred<String?> {
        return service.getUrlDescarga(descripcion)
    }

    override fun getObservableUrlDescarga(descripcion: String?): Observable<String?> {
        return service.getObservableUrlDescarga(descripcion)
    }
}
