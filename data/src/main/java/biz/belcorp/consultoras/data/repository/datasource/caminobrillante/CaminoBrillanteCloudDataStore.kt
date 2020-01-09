package biz.belcorp.consultoras.data.repository.datasource.caminobrillante

import biz.belcorp.consultoras.data.entity.OfertaEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.*
import biz.belcorp.consultoras.data.net.service.ICaminoBrillante
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

/**
 *
 */
class CaminoBrillanteCloudDataStore internal constructor(private val service: ICaminoBrillante)
    : CaminoBrillanteDataStore {

    override fun getNivelConsultoraCaminoBrillanteAsObservable(): Observable<Int> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getNivelCaminoBrillanteById(idNivel: String): Deferred<NivelCaminoBrillanteEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getNivelesCaminoBrillante(): Deferred<List<NivelCaminoBrillanteEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getResumenLogros(): Deferred<LogroCaminoBrillanteEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getResumenConsultora(): Deferred<NivelConsultoraCaminoBrillanteEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getResumenConsultoraAsObservable(): Observable<NivelConsultoraCaminoBrillanteEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveNivelesConsultora(cargarCaminoBrillante: Boolean, nivelesConsultora: List<NivelConsultoraCaminoBrillanteEntity>?): Observable<Boolean> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveLogrosCaminoBrillante(cargarCaminoBrillante: Boolean, logroCaminoBrillanteEntity: LogroCaminoBrillanteEntity?, logrosCaminoBrillanteEntity: List<LogroCaminoBrillanteEntity>?): Observable<Boolean> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getPedidosPeriodoActual(): Deferred<List<LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity>> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveNivelesCaminoBrillante(cargarCaminoBrillante: Boolean, nivelesCaminoBrillante: List<NivelCaminoBrillanteEntity>?): Observable<Boolean> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getDemostradoresOfertas(campaniaID: Int, idNivel: Int, orden: String?, filtro: String?, inicio: Int?, cantidad: Int?): Deferred<ServiceDto<List<DemostradorCaminoBrillanteEntity>>?> {
        return service.getDemostradoresOfertas(campaniaID, idNivel, orden, filtro, inicio, cantidad)
    }

    override fun getKitsOfertas(campaniaID: Int, idNivel: Int): Deferred<ServiceDto<List<KitCaminoBrillanteEntity>>?> {
        return service.getKitsOfertas(campaniaID, idNivel)
    }

    override fun getConfiguracionDemostrador(): Deferred<ServiceDto<ConfiguracionDemostradorEntity>> {
        return service.getConfiguracionDemostrador()
    }

    override suspend fun getNivelesHistoricoCaminoBrillante(): Deferred<List<NivelConsultoraCaminoBrillanteEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOfertasCarousel(campaniaId: Int, nivelId: Int): Deferred<ServiceDto<CarouselEntity>> {
        return service.getOfertasCarousel(campaniaId, nivelId)
    }

    override fun getFichaProducto(tipo: String, campaniaId: Int, cuv: String, nivelId: Int): Deferred<ServiceDto<OfertaEntity?>> {
        return service.getFichaProducto(tipo, campaniaId, nivelId, cuv)
    }

    override suspend fun getNivelConsultoraCaminoBrillante(): Deferred<Int?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getSiguienteNivelCaminoBrillante(id: Int): Deferred<NivelCaminoBrillanteEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getLogros(): Deferred<List<LogroCaminoBrillanteEntity>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun updatelagAnim(animRequestUpdate: AnimRequestUpdate): Deferred<ServiceDto<Boolean>> {
        return service.updateFlagAnim(animRequestUpdate)
    }

    override fun getPuntajeAcumuladoAsObservable(): Observable<Int> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getPeriodoCaminoBrillante(): Observable<Int> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override suspend fun getNivelCampanaAnterior(): Deferred<Int?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}
