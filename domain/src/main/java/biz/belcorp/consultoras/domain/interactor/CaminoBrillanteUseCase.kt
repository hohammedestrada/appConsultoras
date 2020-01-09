package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.caminobrillante.*
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.CaminoBrillanteRepository
import com.sun.org.apache.xpath.internal.operations.Bool
import javax.inject.Inject

class CaminoBrillanteUseCase @Inject
constructor(private val caminoBrillanteRepository: CaminoBrillanteRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    fun getResumenConsultoraAsObserver(observer: BaseObserver<NivelConsultoraCaminoBrillante?>) {
        execute(this.caminoBrillanteRepository.getResumenConsultoraAsObservable(), observer)
    }

    fun getNivelConsultoraAsObserver(observer: BaseObserver<Int>) {
        execute(this.caminoBrillanteRepository.getNivelConsultoraCaminoBrillanteAsObservable(), observer)
    }

    suspend fun getResumenConsultora() : NivelConsultoraCaminoBrillante? {
        return this.caminoBrillanteRepository.getResumenConsultora()
    }

    suspend fun getLogros() : List<LogroCaminoBrillante>? {
        return this.caminoBrillanteRepository.getLogros()
    }

    suspend fun getPedidosPeriodoActual() : List<LogroCaminoBrillante.Indicador.Medalla>?{
        return this.caminoBrillanteRepository.getPedidosPeriodoActual()
    }

    suspend fun getResumenLogros() : LogroCaminoBrillante? {
        return this.caminoBrillanteRepository.getResumenLogros()
    }

    suspend fun getNivelesCaminoBrillante() : List<NivelCaminoBrillante>? {
        return this.caminoBrillanteRepository.getNivelesCaminoBrillante()
    }

    suspend fun getNivelCaminoBrillanteById(idNivel: String) : NivelCaminoBrillante? {
        return this.caminoBrillanteRepository.getNivelCaminoBrillanteById(idNivel)
    }

    suspend fun getKitsOfertas() : BasicDto<List<KitCaminoBrillante>>? {
        return this.caminoBrillanteRepository.getKitsOfertas()
    }

    suspend fun getDemostradoresOfertas(orden: String?, filtro: String?, inicio: Int?, cantidad: Int?) : BasicDto<List<DemostradorCaminoBrillante>>? {
        return this.caminoBrillanteRepository.getDemostradoresOfertas(orden, filtro, inicio, cantidad)
    }

    suspend fun getConfiguracionDemostrador() : BasicDto<ConfiguracionDemostrador> {
        return this.caminoBrillanteRepository.getConfiguracionDemostrador()
    }

    suspend fun getOfertasCarousel(): BasicDto<Carousel>? {
        return this.caminoBrillanteRepository.getOfertasCarousel()
    }

    suspend fun getNivelesHistoricoCaminoBrillante() : List<NivelConsultoraCaminoBrillante>? {
        return this.caminoBrillanteRepository.getNivelesHistoricoCaminoBrillante()
    }

    suspend fun getFichaProducto(tipo: String, cuv: String): BasicDto<Oferta?>? {
        return this.caminoBrillanteRepository.getFichaProducto(tipo, cuv)
    }

    suspend fun getNivelActualCaminoBrillante(): NivelCaminoBrillante? {
        return this.caminoBrillanteRepository.getNivelActual()
    }

    suspend fun getNivelCamapanaAnterior() : Int? {
        return this.caminoBrillanteRepository.getNivelCampanaAnterior()
    }

    suspend fun getNivelSiguienteCaminoBrillante(): NivelCaminoBrillante? {
        return this.caminoBrillanteRepository.getNivelSiguiente()
    }

    suspend fun updateAnimFlag(animRequest: AnimRequest) : BasicDto<Boolean> {
        return this.caminoBrillanteRepository.updateAnimFlag(animRequest)
    }

}
