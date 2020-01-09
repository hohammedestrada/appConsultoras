package biz.belcorp.consultoras.data.repository.datasource.incentivos

import android.content.Context
import biz.belcorp.consultoras.data.entity.*

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select

import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.*
import io.reactivex.Observable

/**
 * Clase de Incentivos encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
internal class IncentivosDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(val context: Context?) : IncentivosDataStore {

    override val incentives: Observable<List<ConcursoEntity?>?>
        get() = Observable.create { emitter ->
            try {
                val entities = (select from ConcursoEntity::class).list
                emitter.onNext(entities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }

    /**
     * Metodo que obtiene el listado de incentivos desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(entity: IncentivesRequestEntity?): Observable<List<ConcursoEntity?>?> {
        return Observable.create { emitter ->
            try {
                var entities: List<ConcursoEntity>
                val from = (select from ConcursoEntity::class)

                entity?.let {

                    entities = if (entity.tipoConcurso != null) {
                        (from where ConcursoEntity_Table.TipoConcurso.eq(entity.tipoConcurso)).list
                    } else {
                        from.queryList()
                    }
                    emitter.onNext(entities)
                }

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun getHistory(entity: IncentivesRequestEntity?): Observable<List<ConcursoEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    /**
     * Función que obtiene un concurso por código desde la bd
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getContest(contestCode: String?): Observable<ConcursoEntity?> {
        return Observable.create { emitter ->
            try {
                contestCode?.let {
                    val entity = Select()
                        .from(ConcursoEntity::class.java)
                        .where(ConcursoEntity_Table.CodigoConcurso.eq(contestCode))
                        .querySingle()

                    entity?.let { it1 ->
                        emitter.onNext(it1)
                    } ?: emitter.onError(NullPointerException("concurso no encontrado"))
                }?: emitter.onError(NullPointerException("contest code is null"))

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Función que guarda los concursos en la bd
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(contests: List<ConcursoEntity?>?): Observable<String?> {
        return Observable.create { emitter ->
            try {
                Delete.tables(PremioEntity::class.java, NivelEntity::class.java,
                    PremioNuevasEntity::class.java, CuponEntity::class.java,
                    NivelProgramaNuevaEntity::class.java, ConcursoEntity::class.java)

                contests?.filterNotNull()?.forEach { concursoEntity ->
                    val id = FlowManager.getModelAdapter(ConcursoEntity::class.java).insert(concursoEntity).toInt()
                    saveNiveles(id, concursoEntity.niveles)
                    saveNivelesProgramaNuevas(id, concursoEntity.nivelesProgramaNuevas)
                }

                emitter.onNext("OK")
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }


    /** */

    private fun saveNiveles(concursoId: Int?, niveles: List<NivelEntity?>?) {
        if (null == niveles) return
        niveles.let {
            it.filterNotNull().forEach { entity ->
                entity.concursoLocalId = concursoId
                val id = FlowManager.getModelAdapter(NivelEntity::class.java).insert(entity).toInt()
                savePremios(id, entity.premios)
            }
        }
    }

    private fun savePremios(id: Int?, premios: List<PremioEntity?>?) {
        if (null == premios) return
        premios.let {
            it.filterNotNull().forEach { entity ->
                entity.nivelLocalId = id
                FlowManager.getModelAdapter(PremioEntity::class.java).insert(entity)
            }
        }
    }

    private fun saveNivelesProgramaNuevas(concursoId: Int?, niveles: List<NivelProgramaNuevaEntity?>?) {
        niveles?.let {
            it.filterNotNull().forEach { entity ->
                entity.concursoLocalId = concursoId
                val id = FlowManager.getModelAdapter(NivelProgramaNuevaEntity::class.java).insert(entity).toInt()

                savePremiosNuevas(id, entity.premiosNuevas)
                saveCupones(id, entity.cupones)
            }
        }
    }

    private fun savePremiosNuevas(id: Int?, premiosNuevas: List<PremioNuevasEntity?>?) {
        premiosNuevas?.let {
            premiosNuevas.filterNotNull().forEach { entity ->
                entity.nivelProgramaLocalId = id
                FlowManager.getModelAdapter(PremioNuevasEntity::class.java).insert(entity)
            }
        }
    }

    private fun saveCupones(id: Int?, cupones: List<CuponEntity?>?) {
        cupones?.let {
            cupones.filterNotNull().forEach { entity ->
                entity.nivelProgramaLocalId = id
                FlowManager.getModelAdapter(CuponEntity::class.java).insert(entity)
            }
        }
    }

}
