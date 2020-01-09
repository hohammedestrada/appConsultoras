package biz.belcorp.consultoras.data.repository.datasource.caminobrillante

import biz.belcorp.consultoras.data.entity.GroupFilterEntity
import biz.belcorp.consultoras.data.entity.OfertaEntity
import biz.belcorp.consultoras.data.entity.OrdenamientoEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.*
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.Delete
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class CaminoBrillanteDBDataStore : CaminoBrillanteDataStore {

    override suspend fun getResumenConsultora(): Deferred<NivelConsultoraCaminoBrillanteEntity?> {
        return coroutineScope {
            async {
                (select from NivelConsultoraCaminoBrillanteEntity::class where NivelConsultoraCaminoBrillanteEntity_Table.isActual.eq(true)).result
            }
        }
    }

    override fun getResumenConsultoraAsObservable(): Observable<NivelConsultoraCaminoBrillanteEntity?> {
        return Observable.create { emitter ->
            try {
                val resumen = (select from NivelConsultoraCaminoBrillanteEntity::class where NivelConsultoraCaminoBrillanteEntity_Table.isActual.eq(true)).result
                resumen?.let {
                    emitter.onNext(it)
                } ?: emitter.onNext(NivelConsultoraCaminoBrillanteEntity())
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override fun getPuntajeAcumuladoAsObservable(): Observable<Int> {
        return Observable.create { emitter ->
            try {
                val nivel = (select from NivelCaminoBrillanteEntity::class where NivelCaminoBrillanteEntity_Table.CodigoNivel.eq("6")).result
                nivel?.let {
                    emitter.onNext(it.puntajeAcumulado ?: 0)
                } ?: emitter.onNext(0)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override fun getNivelConsultoraCaminoBrillanteAsObservable(): Observable<Int> {
        return Observable.create { emitter ->
            try {
                val resumen = (select from NivelConsultoraCaminoBrillanteEntity::class where NivelConsultoraCaminoBrillanteEntity_Table.isActual.eq(true)).result
                resumen?.let {
                    emitter.onNext(it.nivel?.toIntOrNull() ?: 0)
                } ?: emitter.onNext(0)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override suspend fun getNivelesCaminoBrillante(): Deferred<List<NivelCaminoBrillanteEntity>?> {
        return coroutineScope {
            async {
                val niveles = (select from NivelCaminoBrillanteEntity::class).list

                niveles.forEach {
                    val beneficios = (select from NivelCaminoBrillanteEntity.BeneficioCaminoBrillanteEntity::class where BeneficioCaminoBrillanteEntity_Table.CodigoNivel.eq(it.codigoNivel)).list
                    it.beneficios = beneficios
                }

                niveles
            }
        }
    }

    override suspend fun getNivelCaminoBrillanteById(idNivel: String): Deferred<NivelCaminoBrillanteEntity?> {
        return coroutineScope {
            async {
                val nivel = (select from NivelCaminoBrillanteEntity::class where NivelCaminoBrillanteEntity_Table.CodigoNivel.eq(idNivel)).result

                nivel?.let { nivel ->
                    val beneficios = (select from NivelCaminoBrillanteEntity.BeneficioCaminoBrillanteEntity::class where BeneficioCaminoBrillanteEntity_Table.CodigoNivel.eq(nivel.codigoNivel)).list
                    nivel.beneficios = beneficios
                }

                nivel
            }
        }
    }

    override suspend fun getResumenLogros(): Deferred<LogroCaminoBrillanteEntity?> {
        return coroutineScope {
            async {
                val logro = (select from LogroCaminoBrillanteEntity::class where (LogroCaminoBrillanteEntity_Table.Id.eq("RESUMEN"))).result

                logro?.let {
                    val indicadores = (select from LogroCaminoBrillanteEntity.IndicadorEntity::class where (IndicadorEntity_Table.IdLogro.eq(it.id))).list

                    indicadores.forEach { indicador ->
                        val medallas = (select from LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity::class where MedallaEntity_Table.IdIndicador.eq(indicador.id)).list
                        indicador.medallas = medallas
                    }
                    it.indicadores = indicadores
                }
                logro
            }
        }
    }

    override suspend fun getLogros(): Deferred<List<LogroCaminoBrillanteEntity>?> {
        return coroutineScope {
            async {
                val logros = (select from LogroCaminoBrillanteEntity::class where (LogroCaminoBrillanteEntity_Table.Id.notEq("RESUMEN"))).list

                logros.forEach { it ->
                    val indicadores = (select from LogroCaminoBrillanteEntity.IndicadorEntity::class where (IndicadorEntity_Table.IdLogro.eq(it.id))).list
                    indicadores.forEach {
                        val medallas = (select from LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity::class where MedallaEntity_Table.IdIndicador.eq(it.id)).list
                        it.medallas = medallas
                    }
                    it.indicadores = indicadores
                }
                logros
            }
        }
    }

    override suspend fun getPedidosPeriodoActual(): Deferred<List<LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity>> {
        return coroutineScope {
            async {
                val constancias = (select from LogroCaminoBrillanteEntity.IndicadorEntity::class where (IndicadorEntity_Table.IdLogro.eq("CONSTANCIA_DETALLADA"))).list
                val idPeriodoActual = constancias.maxBy { it.codigo?.toIntOrNull() ?: 0 }?.id ?: -1
                val pedidos = (select from LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity::class where MedallaEntity_Table.IdIndicador.eq(idPeriodoActual)).list
                pedidos
            }
        }
    }

    override fun saveLogrosCaminoBrillante(cargarCaminoBrillante: Boolean, logroCaminoBrillanteEntity: LogroCaminoBrillanteEntity?, logrosCaminoBrillanteEntity: List<LogroCaminoBrillanteEntity>?): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                if (cargarCaminoBrillante) {
                    Delete.tables(LogroCaminoBrillanteEntity::class.java)
                    Delete.tables(LogroCaminoBrillanteEntity.IndicadorEntity::class.java)
                    Delete.tables(LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity::class.java)

                    val logros = mutableListOf<LogroCaminoBrillanteEntity>()

                    logroCaminoBrillanteEntity?.let {
                        logros.add(it)
                    }

                    logrosCaminoBrillanteEntity?.let {
                        logros.addAll(it)

                        FlowManager.getModelAdapter(LogroCaminoBrillanteEntity::class.java).saveAll(logros)

                        logros.forEach { logro ->
                            logro.indicadores?.let { indicadores ->

                                indicadores.forEach { indicador ->
                                    indicador.idLogro = logro.id

                                    val idIndicador: Long = FlowManager.getModelAdapter(LogroCaminoBrillanteEntity.IndicadorEntity::class.java).insert(indicador)

                                    indicador.medallas?.let { medallas ->
                                        medallas.forEach { medalla ->
                                            medalla.let {
                                                it.idIndicador = idIndicador
                                                FlowManager.getModelAdapter(LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity::class.java).save(it)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override fun saveNivelesCaminoBrillante(cargarCaminoBrillante: Boolean, nivelesCaminoBrillante: List<NivelCaminoBrillanteEntity>?): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                if (cargarCaminoBrillante) {

                    Delete.tables(NivelCaminoBrillanteEntity::class.java)
                    Delete.tables(NivelCaminoBrillanteEntity.BeneficioCaminoBrillanteEntity::class.java)

                    nivelesCaminoBrillante?.let { niveles ->
                        FlowManager.getModelAdapter(NivelCaminoBrillanteEntity::class.java).saveAll(niveles)

                        niveles.forEach { nivel ->
                            nivel.beneficios?.let {

                                it.forEach { beneficio ->
                                    beneficio.codigoNivel = nivel.codigoNivel
                                }

                                FlowManager.getModelAdapter(NivelCaminoBrillanteEntity.BeneficioCaminoBrillanteEntity::class.java).saveAll(it)
                            }
                        }
                    }
                }
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override fun saveNivelesConsultora(cargarCaminoBrillante: Boolean, nivelesConsultora: List<NivelConsultoraCaminoBrillanteEntity>?): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                if (cargarCaminoBrillante) {
                    Delete.tables(NivelConsultoraCaminoBrillanteEntity::class.java)

                    nivelesConsultora?.let {
                        FlowManager.getModelAdapter(NivelConsultoraCaminoBrillanteEntity::class.java).saveAll(it)
                    }
                }
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override fun getDemostradoresOfertas(campaniaID: Int, idNivel: Int, orden: String?, filtro: String?, inicio: Int?, cantidad: Int?): Deferred<ServiceDto<List<DemostradorCaminoBrillanteEntity>>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getKitsOfertas(campaniaID: Int, nivelId: Int): Deferred<ServiceDto<List<KitCaminoBrillanteEntity>>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getConfiguracionDemostrador(): Deferred<ServiceDto<ConfiguracionDemostradorEntity>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOfertasCarousel(campaniaId: Int, nivelId: Int): Deferred<ServiceDto<CarouselEntity>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getFichaProducto(tipo: String, campaniaId: Int, cuv: String, nivelId: Int): Deferred<ServiceDto<OfertaEntity?>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getNivelesHistoricoCaminoBrillante(): Deferred<List<NivelConsultoraCaminoBrillanteEntity>?> {
        return coroutineScope {
            async {
                (select from NivelConsultoraCaminoBrillanteEntity::class).orderBy(NivelConsultoraCaminoBrillanteEntity_Table.Campania, true).list
            }
        }
    }

    override suspend fun getSiguienteNivelCaminoBrillante(id: Int): Deferred<NivelCaminoBrillanteEntity?> {
        return coroutineScope {
            async {
                val siguienteNivel = id + 1
                (select from NivelCaminoBrillanteEntity::class).where(NivelCaminoBrillanteEntity_Table.CodigoNivel.eq(siguienteNivel.toString())).result
            }
        }
    }

    override suspend fun getNivelConsultoraCaminoBrillante(): Deferred<Int?> {
        return coroutineScope {
            async {
                val resumen = (select from NivelConsultoraCaminoBrillanteEntity::class where NivelConsultoraCaminoBrillanteEntity_Table.isActual.eq(true)).result
                resumen?.nivel?.toIntOrNull()
            }
        }
    }

    override suspend fun updatelagAnim(animRequestUpdate: AnimRequestUpdate): Deferred<ServiceDto<Boolean>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getPeriodoCaminoBrillante(): Observable<Int> {
        return Observable.create { emitter ->
            try {
                val resumen = (select from NivelConsultoraCaminoBrillanteEntity::class where NivelConsultoraCaminoBrillanteEntity_Table.isActual.eq(true)).result
                resumen?.let {
                    emitter.onNext(resumen.periodoCae?.toIntOrNull() ?: 0)
                } ?: emitter.onNext(0)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
        }
    }

    override suspend fun getNivelCampanaAnterior(): Deferred<Int?> {
        return coroutineScope {
            async {
                val nivelCampanaAnterior = (select from NivelConsultoraCaminoBrillanteEntity::class).limit(1).offset(1).orderBy(NivelConsultoraCaminoBrillanteEntity_Table.Campania, false).result?.nivel?.toIntOrNull()
                nivelCampanaAnterior
            }
        }
    }

}
