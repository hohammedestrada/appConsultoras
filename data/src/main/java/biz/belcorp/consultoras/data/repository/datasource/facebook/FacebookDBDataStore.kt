package biz.belcorp.consultoras.data.repository.datasource.facebook

import biz.belcorp.consultoras.data.entity.FacebookProfileEntity
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.select
import io.reactivex.Observable

class FacebookDBDataStore : FacebookDataStore {

    override fun get(): Observable<FacebookProfileEntity?> {
        return Observable.create { emitter ->
            try {
                var entity = (select from FacebookProfileEntity::class).result

                if (null == entity) {
                    entity = FacebookProfileEntity()
                }

                emitter.onNext(entity)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun save(entity: FacebookProfileEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                entity?.let {
                    emitter.onNext(entity.save())
                    emitter.onComplete()
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }
}
