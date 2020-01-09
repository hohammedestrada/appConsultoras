package biz.belcorp.consultoras.data.repository.datasource.user

import android.content.Context
import android.util.Log
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import io.reactivex.Observable

class UserDBDataStore(val context: Context) : UserDataStore {


    override fun getWithObservable(): Observable<UserEntity?> {
        return Observable.create { emitter ->
            try {
                (select from UserEntity::class).result?.let {

                    emitter.onNext(it)
                } ?: emitter.onError(NullPointerException("usuario no encontrado"))
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override suspend fun getWithCoroutine(): UserEntity? {

        return (select from UserEntity::class).result

    }

    override fun get(): UserEntity? {
        return (select from UserEntity::class).result
    }

    override fun save(entity: UserEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                entity?.let {
                    Delete.tables(UserEntity::class.java)
                    emitter.onNext(it.save())
                    emitter.onComplete()
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun removeAll(): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                Delete().from(UserEntity::class.java).execute()
                Delete().from(FacebookProfileEntity::class.java).execute()
                Delete().from(UserDetailEntity::class.java).execute()
                Delete().from(ClienteEntity::class.java).execute()
                Delete().from(AnotacionEntity::class.java).execute()
                Delete().from(RecordatorioEntity::class.java).execute()
                Delete().from(ClientMovementEntity::class.java).execute()
                Delete().from(ConcursoEntity::class.java).execute()
                Delete().from(ContactoEntity::class.java).execute()
                Delete().from(PremioEntity::class.java).execute()
                Delete().from(PremioNuevasEntity::class.java).execute()
                Delete().from(NivelProgramaNuevaEntity::class.java).execute()
                Delete().from(CuponEntity::class.java).execute()
                Delete().from(AccountStateEntity::class.java).execute()
                Delete().from(ProductEntity::class.java).execute()
                Delete().from(ProductResponseEntity::class.java).execute()
                Delete().from(TrackingDetailEntity::class.java).execute()
                Delete().from(TrackingEntity::class.java).execute()
                Delete().from(NotificacionClienteEntity::class.java).execute()
                Delete().from(NotificacionRecordatorioEntity::class.java).execute()
                Delete().from(DeviceEntity::class.java).execute()

                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun updateAcceptances(terms: Boolean?, privacity: Boolean?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {

                (select from UserEntity::class).result?.apply {
                    isAceptaTerminosCondiciones = terms ?: false
                    isAceptaPoliticaPrivacidad = privacity ?: false
                    save()
                }
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun updateAcceptancesCreditAgreement(creditAgreement: Boolean?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {

                (select from UserEntity::class).result?.apply {
                    if (creditAgreement == true)
                        indicadorContratoCredito = 1
                    save()
                }
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun saveHybrisData(entity: HybrisDataEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                entity?.let {

                    val entitySelect = Select().from<HybrisDataEntity>(HybrisDataEntity::class.java)
                        .where(HybrisDataEntity_Table.trackingURL.eq(entity.trackingURL))
                        .querySingle()

                    if (null == entitySelect)
                        FlowManager.getModelAdapter<HybrisDataEntity>(HybrisDataEntity::class.java).save(entity)

                    emitter.onNext(true)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun updateMount(mount: Double): Boolean {
        var retorno = false
        mount.let {
            try {
                (select from UserEntity::class).result?.apply {
                    userResume!![5].value = mount
                    save()
                }
                retorno = true
            } catch (ex: Exception) {
                retorno = false
            }
            return retorno
        }
    }

}
