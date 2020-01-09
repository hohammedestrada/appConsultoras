package biz.belcorp.consultoras.data.repository.datasource.api

import android.content.Context
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import io.reactivex.Observable


class ApiDBDataStore(val context: Context) : ApiDataStore {

    override val device: Observable<DeviceEntity?>
        get() = Observable.create { emitter ->
            try {
                (select from DeviceEntity::class).result?.let {
                    emitter.onNext(it)
                } ?: emitter.onError(NullPointerException("device no encontrado"))
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }

    override fun saveDevice(entity: DeviceEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                entity?.let {
                    Delete.tables(DeviceEntity::class.java)
                    FlowManager.getModelAdapter(DeviceEntity::class.java).insert(entity)
                    emitter.onNext(true)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

}
