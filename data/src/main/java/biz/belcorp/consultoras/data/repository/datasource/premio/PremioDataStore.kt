package biz.belcorp.consultoras.data.repository.datasource.premio

import biz.belcorp.consultoras.data.entity.PremioFinalAgregaEntity
import biz.belcorp.consultoras.data.entity.PremioFinalEntity
import biz.belcorp.consultoras.data.entity.PremioFinalMetaEntity
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

interface PremioDataStore {

    fun getOfertasFinales(codigoCampania: Int?): Deferred<List<PremioFinalEntity?>?>

    fun addPremio(body: PremioFinalAgregaEntity?): Deferred<ServiceDto<Any>?>

    fun getMontoMeta(codigoCampania: Int?): Deferred<PremioFinalMetaEntity?>

}
