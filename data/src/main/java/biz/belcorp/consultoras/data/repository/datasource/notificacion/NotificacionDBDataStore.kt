package biz.belcorp.consultoras.data.repository.datasource.notificacion

import biz.belcorp.consultoras.data.entity.NotificacionEntity
import biz.belcorp.consultoras.data.entity.NotificacionEntity_Table
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.OrderBy
import io.reactivex.Observable
import java.util.*

class NotificacionDBDataStore : NotificacionDataStore {


    /**
     * Obtiene la lista de notificaciones por el id de consultora.
     */
    override fun listByConsultoraId(consultoraId: String?, dias: Int): Observable<List<NotificacionEntity?>?> {
        return Observable.create { emitter ->

            val milis = dias * 24 * 60 * 60 * 1000
            val interval = Date().time - milis

            try {
                val entities = (select
                    from NotificacionEntity::class
                    where (NotificacionEntity_Table.ConsultoraId.eq(consultoraId))
                    and (NotificacionEntity_Table.Fecha.greaterThan(Date(interval)))
                    orderBy (OrderBy.fromProperty(NotificacionEntity_Table.Fecha)
                    collate Collate.NOCASE).descending()
                    ).list

                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Obtiene la lista de notificaciones por el id de consultora.
     */
    override fun getByID(id: Int?): Observable<NotificacionEntity?> {
        return Observable.create { emitter ->
            try {
                val entity = (select
                    from NotificacionEntity::class
                    where (NotificacionEntity_Table.ID.eq(id))
                    orderBy (OrderBy.fromProperty(NotificacionEntity_Table.Fecha)
                    collate Collate.NOCASE).descending()
                    ).result

                entity?.let { emitter.onNext(it) } ?: run { emitter.onError(SqlException("")) }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    /**
     * Obtiene la lista de notificaciones por el id de consultora.
     */
    /*fun getByNotificacionId(notificacionId: Int): Observable<NotificacionEntity?> {
        return Observable.create { emitter ->
            try {
                val entity = (select
                    from NotificacionEntity::class
                    where (NotificacionEntity_Table.NotificationID eq notificacionId)
                    orderBy (OrderBy.fromProperty(NotificacionEntity_Table.Fecha)
                    collate Collate.NOCASE).descending()
                    ).result

                entity?.let { emitter.onNext(it) } ?: run { emitter.onError(SqlException("")) }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }*/

    /**
     * Elimina una notificacion por id.
     */
    override fun deleteByID(id: Int?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                getByID(id).map {
                    it.estado = -1
                    emitter.onNext(FlowManager.getModelAdapter(NotificacionEntity::class.java)
                        .save(it))

                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    /**
     * Guarda una notificación.
     */
    override fun save(notificacionEntity: NotificacionEntity): Observable<NotificacionEntity?> {
        return if (FlowManager.getModelAdapter(NotificacionEntity::class.java).save(notificacionEntity)) {
            getByID(notificacionEntity.id)
        } else {
            Observable.error(SqlException("No se encuentra la notificacion"))
        }
    }

    override fun updateEstadoByNotificationId(notificationId: Int, estado: Int): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {

                val entity = (select
                    from NotificacionEntity::class
                    where (NotificacionEntity_Table.NotificationID eq notificationId)
                    orderBy (OrderBy.fromProperty(NotificacionEntity_Table.Fecha)
                    collate Collate.NOCASE).descending()
                    ).result

                entity?.let {
                    entity.estado = estado
                    emitter.onNext(FlowManager.getModelAdapter(NotificacionEntity::class.java).save(it))
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))


            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    override fun getCountNotificationsWithoutLookingObservable(): Observable<Long> {
        return Observable.create { emitter ->

            try {
                emitter.onNext(
                    (select from NotificacionEntity::class where (NotificacionEntity_Table.Estado eq Constant.ESTADO__NO_VISTO)).queryList().count().toLong() //0 Es no isVisto, 1 es isVisto
                )
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }


        }

    }

    override suspend fun getCountNotificationsWithoutLooking(): Long {
        return (select from NotificacionEntity::class where (NotificacionEntity_Table.Estado eq Constant.ESTADO__NO_VISTO)).count //0 Es no isVisto, 1 es isVisto
    }

}
