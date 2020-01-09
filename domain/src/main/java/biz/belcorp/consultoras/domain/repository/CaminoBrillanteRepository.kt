package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.caminobrillante.*
import io.reactivex.Observable

interface CaminoBrillanteRepository {

    fun getResumenConsultoraAsObservable(): Observable<NivelConsultoraCaminoBrillante?>
    fun getNivelConsultoraCaminoBrillanteAsObservable(): Observable<Int>
    suspend fun getResumenConsultora(): NivelConsultoraCaminoBrillante?
    suspend fun getNivelCaminoBrillanteById(idNivel: String): NivelCaminoBrillante?
    suspend fun getNivelesCaminoBrillante(): List<NivelCaminoBrillante>?
    suspend fun getLogros() : List<LogroCaminoBrillante>?
    suspend fun getResumenLogros(): LogroCaminoBrillante?
    suspend fun getPedidosPeriodoActual(): List<LogroCaminoBrillante.Indicador.Medalla>?
    suspend fun getKitsOfertas(): BasicDto<List<KitCaminoBrillante>>?
    suspend fun getDemostradoresOfertas(orden: String?, filtro: String?, inicio: Int?, cantidad: Int?): BasicDto<List<DemostradorCaminoBrillante>>?
    suspend fun getConfiguracionDemostrador() : BasicDto<ConfiguracionDemostrador>
    suspend fun getOfertasCarousel(): BasicDto<Carousel>?
    suspend fun getNivelesHistoricoCaminoBrillante(): List<NivelConsultoraCaminoBrillante>?
    suspend fun getFichaProducto(tipo: String, cuv: String): BasicDto<Oferta?>?
    suspend fun getNivelSiguiente() : NivelCaminoBrillante?
    suspend fun getNivelActual(): NivelCaminoBrillante?
    suspend fun updateAnimFlag(animRequest: AnimRequest): BasicDto<Boolean>
    suspend fun getNivelCampanaAnterior(): Int?

}
