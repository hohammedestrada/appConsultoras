package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.entity.FestivalSelloEntity
import biz.belcorp.consultoras.data.mapper.FestivalConfiguracionDataMapper
import biz.belcorp.consultoras.data.repository.datasource.festival.FestivalDataStoreFactory
import biz.belcorp.consultoras.domain.entity.FestivalBanner
import biz.belcorp.consultoras.domain.entity.FestivalCategoria
import biz.belcorp.consultoras.domain.entity.FestivalConfiguracion
import biz.belcorp.consultoras.domain.entity.FestivalSello
import biz.belcorp.consultoras.domain.repository.FestivalRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FestivaDataRepository @Inject
internal constructor(private val festivalDataStoreFactory: FestivalDataStoreFactory,
                     private val festivalConfiguracionDataMapper: FestivalConfiguracionDataMapper)
    : FestivalRepository {

    override suspend fun getLocalConfiguracion(): FestivalConfiguracion? {
        val dataStore = this.festivalDataStoreFactory.createDB()

        var configuracion:FestivalConfiguracion? = festivalConfiguracionDataMapper.transformDbToConfiguracion(dataStore.getConfiguracionFestival())
        val banner: FestivalBanner?=festivalConfiguracionDataMapper.transformDbToBanner(dataStore.getConfiguracionBannerSello())
        val categorias:List<FestivalCategoria?> =festivalConfiguracionDataMapper.transformDbToCategoria(dataStore.getConfiguracionCategorias())

        configuracion?.let {
            it.Banner=banner
            it.Categoria=categorias
            it.Sello = getSelloConfiguracion()
        }

        return configuracion
    }

    override suspend fun getConfiguracion(grantype: Int): Deferred<FestivalConfiguracion?> {
        val dataStore = this.festivalDataStoreFactory.createCloud()
        val db = this.festivalDataStoreFactory.createDB()

        return coroutineScope {
            async {
                val response : FestivalConfiguracionEntity? = dataStore.getConfiguracion(grantype).await()

                response?.let {
                    db.saveConfigurationFestival(festivalConfiguracionDataMapper.transformFestivalCOnfiguration(it))
                    db.saveConfigurationFestivalBannerSello(festivalConfiguracionDataMapper.transformBannerSelloDB(it.Banner, it.Sello))
                    db.saveConfigurationCategory(festivalConfiguracionDataMapper.transformListCategoriaDB(it.Categoria))

                    festivalConfiguracionDataMapper.transform(it)
                }
            }
        }
    }

    override suspend fun getSelloConfiguracion(): FestivalSello? {
        val db = this.festivalDataStoreFactory.createDB()
        return FestivalSelloEntity.transform(db.getSelloConfiguracion())

    }

    /*override suspend fun getLocalConfiguracion(): FestivalConfiguracion? {
        val db = this.festivalDataStoreFactory.createDB()
        return festivalConfiguracionDataMapper.transform(festivalConfiguracionDataMapper.transformFestivalCOnfiguration(db.getLocalConfiguracion()))
    }*/
}
