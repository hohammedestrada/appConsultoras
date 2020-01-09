package biz.belcorp.consultoras.data.repository.datasource.accountstate

import android.content.Context

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete

import biz.belcorp.consultoras.data.entity.AccountStateEntity
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
internal class AccountStateDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(private val context: Context) : AccountStateDataStore {

    /**
     * Metodo que obtiene el listado de incentivos desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(entityList: List<AccountStateEntity?>?): Observable<List<AccountStateEntity?>?> {
        return Observable.create { emitter ->
            try {
                Delete.tables(AccountStateEntity::class.java)
                entityList?.let {
                    it.forEach { accountStateEntity ->
                        accountStateEntity?.let { it1 ->
                            FlowManager.getModelAdapter(AccountStateEntity::class.java).insert(it1)
                        } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                    }
                    emitter.onNext(it)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun get(): Observable<List<AccountStateEntity?>?> {
        return Observable.create { emitter ->
            try {
                val entities = (select from AccountStateEntity::class).list
                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }
}
