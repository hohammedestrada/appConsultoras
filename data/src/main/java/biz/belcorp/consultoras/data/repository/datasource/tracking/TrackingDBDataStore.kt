package biz.belcorp.consultoras.data.repository.datasource.tracking

import android.content.Context

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select

import biz.belcorp.consultoras.data.entity.TrackingDetailEntity
import biz.belcorp.consultoras.data.entity.TrackingEntity
import biz.belcorp.library.sql.exception.SqlException
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
internal class TrackingDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(val context: Context) : TrackingDataStore {

    /**
     * Metodo que obtiene el listado de incentivos desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(list: List<TrackingEntity?>?): Observable<List<TrackingEntity?>?> {
        return Observable.create { emitter ->
            try {
                list?.let {
                    Delete.tables(TrackingDetailEntity::class.java, TrackingEntity::class.java)
                    list.requireNoNulls().forEach { trackingEntity ->
                        val id = FlowManager.getModelAdapter(TrackingEntity::class.java).insert(trackingEntity).toInt()
                        saveDetails(id, trackingEntity.detalles)
                    }
                    emitter.onNext(list)
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun get(top: Int?): Observable<List<TrackingEntity?>?> {
        return Observable.create { emitter ->
            try {
                val entities: List<TrackingEntity>
                val from = Select().from(TrackingEntity::class.java)
                entities = from.queryList()
                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /** */

    private fun saveDetails(id: Int?, list: List<TrackingDetailEntity?>?) {
        if (null == list) return

        for (entity in list.filterNotNull()) {
            entity.trackingDetailId = id
            FlowManager.getModelAdapter(TrackingDetailEntity::class.java).insert(entity)
        }
    }
}
