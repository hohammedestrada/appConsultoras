package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.MensajeProlEntity
import biz.belcorp.consultoras.data.entity.PremioEntity
import biz.belcorp.consultoras.data.entity.PremioFinalAgregaEntity
import biz.belcorp.consultoras.data.entity.PremioFinalEntity
import biz.belcorp.consultoras.data.mapper.BasicDtoDataMapper
import biz.belcorp.consultoras.data.mapper.MensajeEntity
import biz.belcorp.consultoras.data.mapper.PremioDataMapper
import biz.belcorp.consultoras.data.mapper.PremioResponseEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.premio.PremioDataStoreFactory
import biz.belcorp.consultoras.domain.PremioFinalAgrega
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.consultoras.domain.entity.PremioFinal
import biz.belcorp.consultoras.domain.entity.PremioFinalMeta
import biz.belcorp.consultoras.domain.repository.PremioRepository
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.consultoras.data.entity.PremioFinalMetaEntity
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PremioDataRepository @Inject
internal constructor(private val premioFinalDataStoreFactory: PremioDataStoreFactory,
                     private val premioDataMapper: PremioDataMapper,
                     private val premioResponseEntityDataMapper: PremioResponseEntityDataMapper)
    : PremioRepository {

    override suspend fun getOfertasFinales(codigoCampania: Int?): Deferred<List<PremioFinal?>?> {
        val dataStore = this.premioFinalDataStoreFactory.createCloud()
        return coroutineScope {
            async {

                val response : List<PremioFinalEntity?>? = dataStore.getOfertasFinales(codigoCampania).await()
                premioDataMapper.transformToPremio(response)

            }
        }
    }

    override suspend fun addPremio(body: PremioFinalAgrega?): BasicDto<Collection<MensajeProl?>?>? {

        val x = premioFinalDataStoreFactory.create()
            .addPremio(  premioDataMapper.transformAgregaPremio(body)).await() as ServiceDto<List<MensajeEntity?>?>

        return premioResponseEntityDataMapper.transformUpResponse(x)

    }


    override suspend fun getMontoMeta(codigoCampania: Int?): Deferred<PremioFinalMeta?> {

        val dataStore = this.premioFinalDataStoreFactory.createCloud()

        return coroutineScope {
            async {
                val response: PremioFinalMetaEntity? = dataStore.getMontoMeta(codigoCampania).await()
                premioDataMapper.transformToMeta(response)
            }
        }
    }


}



