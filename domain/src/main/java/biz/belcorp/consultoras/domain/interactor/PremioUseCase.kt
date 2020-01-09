package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.PremioFinalAgrega
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.OfferRepository
import biz.belcorp.consultoras.domain.repository.PremioRepository
import javax.inject.Inject


class PremioUseCase
@Inject
constructor(private val offerRepository: OfferRepository,
            private val premioRepository: PremioRepository,
            threadExecutor: ThreadExecutor
            , postExecutionThread: PostExecutionThread)
    : UseCase(threadExecutor, postExecutionThread) {

    suspend fun getOfertasFinales(codigoCampania: Int?): List<PremioFinal?>? {
        return premioRepository.getOfertasFinales(codigoCampania).await()
    }

    suspend fun addPremio(body: PremioFinalAgrega?): BasicDto<Collection<MensajeProl?>?>? {
        return premioRepository.addPremio(body)
    }

    suspend fun getMontoMeta(codigoCampania: Int?): PremioFinalMeta? {
        return premioRepository.getMontoMeta(codigoCampania).await()
    }

    suspend fun getOfertaFinalConfiguracion(campaignId: String?, simboloMoneda: String?): ConfiguracionPremio? {
        return offerRepository.getOfertaFinalConfiguracion((campaignId
            ?: "0").toInt(), simboloMoneda).await()
    }



}
