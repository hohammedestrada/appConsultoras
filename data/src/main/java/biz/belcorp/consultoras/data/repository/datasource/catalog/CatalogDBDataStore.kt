package biz.belcorp.consultoras.data.repository.datasource.catalog

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete

import biz.belcorp.consultoras.data.entity.CatalogoEntity
import biz.belcorp.consultoras.data.entity.UserCatalogoRequestEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Select
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

/**
 *
 */
internal class CatalogDBDataStore : CatalogDataStore {

    override fun get(request: UserCatalogoRequestEntity?): Observable<List<CatalogoEntity?>?> {
        return Observable.create { emitter ->
            try {
                val entities = (select from CatalogoEntity::class).list
                emitter.onNext(entities)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(SqlException(e.cause))
            }
        }
    }

    override suspend fun getWithCoroutine(request: UserCatalogoRequestEntity?): List<CatalogoEntity?>?{
        return (select from CatalogoEntity::class).list
    }

    override fun save(entities: List<CatalogoEntity?>?): Observable<List<CatalogoEntity?>?> {
        return Observable.create { emitter ->
            try {
                entities?.let {
                    Delete().from(CatalogoEntity::class.java).execute()
                    FlowManager.getModelAdapter(CatalogoEntity::class.java).saveAll(entities)

                    emitter.onNext(entities)
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                emitter.onError(SqlException(e.cause))
            }
        }
    }

    override suspend fun saveCoroutine(entities: List<CatalogoEntity?>?): List<CatalogoEntity?>? {
        Delete().from(CatalogoEntity::class.java).execute()
        entities?.let { FlowManager.getModelAdapter(CatalogoEntity::class.java).saveAll(it) }
        return entities
    }


    override fun hasData(): Boolean {
        return Select().from(CatalogoEntity::class.java).hasData()
    }

    override fun getUrlDescarga(descripcion: String?): Deferred<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getObservableUrlDescarga(descripcion: String?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}
