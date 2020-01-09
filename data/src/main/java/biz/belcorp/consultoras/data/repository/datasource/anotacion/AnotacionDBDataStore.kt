package biz.belcorp.consultoras.data.repository.datasource.anotacion

import android.content.Context

import com.raizlabs.android.dbflow.config.FlowManager

import biz.belcorp.consultoras.data.entity.AnotacionEntity
import biz.belcorp.consultoras.data.entity.AnotacionEntity_Table
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.*
import io.reactivex.Observable

/**
 * Clase de Anotacion encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
class AnotacionDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(val context: Context) : AnotacionDataStore {

    override fun get(id: Int?): Observable<AnotacionEntity?> {
        return Observable.create { emitter ->
            try {
                id?.let {
                    val entity = (select
                        from AnotacionEntity::class
                        where (AnotacionEntity_Table.ID eq id)
                        ).result
                    entity?.let{ it1 ->
                        emitter.onNext(it1)
                    } ?: emitter.onError(NullPointerException("Anotacion no encontrada"))
                }?: emitter.onError(NullPointerException("id is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun listByClientLocalID(clientLocalID: Int?): Observable<List<AnotacionEntity?>?> {
        return Observable.create { emitter ->
            try {
                clientLocalID?.let {
                    val list = (select
                        from AnotacionEntity::class
                        where (AnotacionEntity_Table.ClienteLocalID eq clientLocalID)
                        ).list

                    emitter.onNext(list)
                } ?: emitter.onError(NullPointerException("clientLocalID is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Servicio de tipo POST que guarda una anotacion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(anotacionEntity: AnotacionEntity?): Observable<AnotacionEntity?> {
        return Observable.create { emitter ->
            try {
                anotacionEntity?.let {
                    val anotacionLocalID = FlowManager.getModelAdapter(AnotacionEntity::class.java)
                        .insert(anotacionEntity).toInt()
                    val response = (select
                        from AnotacionEntity::class
                        where (AnotacionEntity_Table.ID eq anotacionLocalID)
                        ).result

                    emitter.onNext(response!!)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))


            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun update(anotacionEntity: AnotacionEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                anotacionEntity?.let {
                    val update = FlowManager.getModelAdapter(AnotacionEntity::class.java)
                        .update(anotacionEntity)
                    emitter.onNext(update)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun delete(anotacionEntity: AnotacionEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                anotacionEntity?.let {
                    emitter.onNext(FlowManager.getModelAdapter(AnotacionEntity::class.java)
                        .delete(anotacionEntity))
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun deleteByClientLocalID(clientLocalID: Int?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                clientLocalID?.let {
                    val list = (select
                        from AnotacionEntity::class
                        where (AnotacionEntity_Table.ClienteLocalID eq clientLocalID)
                        ).list

                    FlowManager.getModelAdapter(AnotacionEntity::class.java)
                        .deleteAll(list)
                    emitter.onNext(true)
                } ?: emitter.onError(NullPointerException("clientLocalID is null"))

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun countByClient(maxCount: Int?, clientLocalId: Int?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                clientLocalId?.let {
                    var band = false

                    val list = (select
                        from AnotacionEntity::class
                        where (AnotacionEntity_Table.ClienteLocalID eq clientLocalId)
                        and (AnotacionEntity_Table.Estado notEq -1)
                        ).list

                    maxCount?.let {it1 ->
                        if (list.size < it1) band = true
                        emitter.onNext(band)
                    }
                }
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }
}
