package biz.belcorp.consultoras.data.repository.datasource.params

import biz.belcorp.consultoras.data.entity.ParamsEntity
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import io.reactivex.Observable

class ParamsDBDataStore : ParamsDataStore {

    override fun get(): ParamsEntity? {
        return (select from ParamsEntity::class).querySingle()
    }

    override fun save(params: ParamsEntity?): Observable<Boolean?> {

        return Observable.create { emitter ->
            try {
                Delete().from(ParamsEntity::class.java).execute()
                params?.let {
                    val result = params.save()
                    emitter.onNext(result)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    override suspend fun saveCoroutine(params: ParamsEntity?): Boolean? {
        Delete().from(ParamsEntity::class.java).execute()
       return params?.save()
    }
}
