package biz.belcorp.consultoras.data.repository.datasource.premio

import biz.belcorp.consultoras.data.entity.PremioFinalAgregaEntity
import biz.belcorp.consultoras.data.entity.PremioFinalEntity
import biz.belcorp.consultoras.data.entity.PremioFinalMetaEntity
import biz.belcorp.consultoras.data.net.service.IPremioService
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

class PremioCloudDataStore(private val service: IPremioService):PremioDataStore {

    override fun addPremio(body: PremioFinalAgregaEntity?): Deferred<ServiceDto<Any>?> {
        return service.addPremio(body)
    }

    override fun getOfertasFinales(codigoCampania: Int?): Deferred<List<PremioFinalEntity?>?> {
        return service.getOfertasFinales(codigoCampania)
    }

    override fun getMontoMeta(codigoCampania: Int?): Deferred<PremioFinalMetaEntity?>{
        return service.getMontoMeta(codigoCampania)
    }

}
