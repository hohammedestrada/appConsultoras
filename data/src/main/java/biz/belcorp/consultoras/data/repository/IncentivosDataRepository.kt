package biz.belcorp.consultoras.data.repository

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.ConcursoEntity
import biz.belcorp.consultoras.data.mapper.ConcursoEntityDataMapper
import biz.belcorp.consultoras.data.mapper.IncentivesRequestEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.incentivos.IncentivosDataStoreFactory
import biz.belcorp.consultoras.domain.entity.Concurso
import biz.belcorp.consultoras.domain.entity.IncentivesRequest
import biz.belcorp.consultoras.domain.repository.IncentivosRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 * Clase de Client encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-05-03
 */
@Singleton
class IncentivosDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param incentivosDataStoreFactory Clase encargada de obtener los datos
 */
@Inject
internal constructor(private val incentivosDataStoreFactory: IncentivosDataStoreFactory,
                     private val concursoEntityDataMapper: ConcursoEntityDataMapper,
                     private val incentivesRequestEntityDataMapper: IncentivesRequestEntityDataMapper)
    : IncentivosRepository {

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override val incentives: Observable<Collection<Concurso?>?>
        get() {
            val localDataStore = incentivosDataStoreFactory.createDB()
            return localDataStore.incentives.map { concursoEntityDataMapper.transform(it) }
        }

    /**
     * Servicio de tipo GET que obtiene a los incentivos
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getIncentives(params: IncentivesRequest?): Observable<Collection<Concurso?>?> {
        val entityRequest = incentivesRequestEntityDataMapper.transform(params)

        val cloudDataStore = incentivosDataStoreFactory.createCloudDataStore()
        val localDataStore = incentivosDataStoreFactory.createDB()

        val observableIncentives = cloudDataStore[entityRequest]
                .onErrorReturn { throwable ->
                    val list = ArrayList<ConcursoEntity>()
                    val entity = ConcursoEntity()
                    entity.error = throwable
                    list.add(entity)
                    list
                }

        val observableHistory = cloudDataStore.getHistory(entityRequest)
                .onErrorReturn { throwable ->
                    val list = ArrayList<ConcursoEntity>()
                    val entity = ConcursoEntity()
                    entity.error = throwable
                    list.add(entity)
                    list
                }

        val zip = Observable.zip(
            observableIncentives
            , observableHistory
            , BiFunction<List<ConcursoEntity?>?, List<ConcursoEntity?>?, List<ConcursoEntity?>?>
                { concursoEntities, concursoEntities2 ->
                    concursoEntityDataMapper.transformCombine(
                            ArrayList<ConcursoEntity>(concursoEntities), ArrayList<ConcursoEntity>(concursoEntities2))
                })

        return zip.flatMap { c ->
            when {
                !c.isEmpty() && null != c[0]?.error -> Observable.error(c[0]?.error)
                else -> localDataStore.save(c).map { concursoEntityDataMapper.transform(c) }
            }
        }

    }

    /**
     * Servicio de tipo GET que obtiene el concurso por el fcódigo
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getContest(contestCode: String?): Observable<Concurso?> {
        val localDataStore = incentivosDataStoreFactory.createDB()
        return localDataStore.getContest(contestCode).map { concursoEntityDataMapper.transform(it) }
    }
}
