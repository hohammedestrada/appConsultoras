package biz.belcorp.consultoras.data.repository.datasource.caminobrillante

import biz.belcorp.consultoras.data.entity.OfertaEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.*
import biz.belcorp.consultoras.domain.entity.caminobrillante.LogroCaminoBrillante
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

interface CaminoBrillanteDataStore {

    fun saveNivelesCaminoBrillante(cargarCaminoBrillante: Boolean, nivelesCaminoBrillante: List<NivelCaminoBrillanteEntity>?): Observable<Boolean>

    fun saveNivelesConsultora(cargarCaminoBrillante: Boolean, nivelesConsultora: List<NivelConsultoraCaminoBrillanteEntity>?): Observable<Boolean>

    fun saveLogrosCaminoBrillante(cargarCaminoBrillante: Boolean, logroCaminoBrillanteEntity: LogroCaminoBrillanteEntity?, logrosCaminoBrillanteEntity: List<LogroCaminoBrillanteEntity>?): Observable<Boolean>

    suspend fun getResumenConsultora(): Deferred<NivelConsultoraCaminoBrillanteEntity?>

    fun getResumenConsultoraAsObservable(): Observable<NivelConsultoraCaminoBrillanteEntity?>

    fun getPeriodoCaminoBrillante(): Observable<Int>

    fun getNivelConsultoraCaminoBrillanteAsObservable(): Observable<Int>

    fun getPuntajeAcumuladoAsObservable(): Observable<Int>

    suspend fun getLogros(): Deferred<List<LogroCaminoBrillanteEntity>?>

    suspend fun getResumenLogros(): Deferred<LogroCaminoBrillanteEntity?>

    suspend fun getPedidosPeriodoActual(): Deferred<List<LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity>>

    suspend fun getNivelesCaminoBrillante(): Deferred<List<NivelCaminoBrillanteEntity>?>

    suspend fun getNivelCaminoBrillanteById(idNivel: String): Deferred<NivelCaminoBrillanteEntity?>

    fun getDemostradoresOfertas(campaniaID: Int, idNivel: Int, orden: String?, filtro: String?, inicio: Int?, cantidad: Int?): Deferred<ServiceDto<List<DemostradorCaminoBrillanteEntity>>?>

    fun getKitsOfertas(campaniaID: Int, idNivel: Int): Deferred<ServiceDto<List<KitCaminoBrillanteEntity>>?>

    fun getConfiguracionDemostrador(): Deferred<ServiceDto<ConfiguracionDemostradorEntity>>

    fun getOfertasCarousel(campaniaId: Int, nivelId: Int): Deferred<ServiceDto<CarouselEntity>>

    suspend fun getNivelesHistoricoCaminoBrillante(): Deferred<List<NivelConsultoraCaminoBrillanteEntity>?>

    fun getFichaProducto(tipo: String,campaniaId: Int, cuv: String, nivelId: Int): Deferred<ServiceDto<OfertaEntity?>>

    suspend fun getSiguienteNivelCaminoBrillante(id: Int): Deferred<NivelCaminoBrillanteEntity?>

    suspend fun getNivelConsultoraCaminoBrillante(): Deferred<Int?>

    suspend fun updatelagAnim(animRequestUpdate: AnimRequestUpdate): Deferred<ServiceDto<Boolean>>

    suspend fun getNivelCampanaAnterior() : Deferred<Int?>

}
