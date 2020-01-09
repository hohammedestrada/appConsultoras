package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.PremioFinalAgrega
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.consultoras.domain.entity.PremioFinal
import biz.belcorp.consultoras.domain.entity.PremioFinalMeta
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

interface PremioRepository{
    suspend fun getOfertasFinales(codigoCampania: Int?): Deferred<List<PremioFinal?>?>

    suspend fun addPremio(body: PremioFinalAgrega?): BasicDto<Collection<MensajeProl?>?>?

    suspend fun getMontoMeta(codigoCampania: Int?): Deferred<PremioFinalMeta?>
}
