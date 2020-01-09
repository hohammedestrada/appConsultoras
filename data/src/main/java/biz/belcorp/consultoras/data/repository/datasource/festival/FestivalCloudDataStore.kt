package biz.belcorp.consultoras.data.repository.datasource.festival

import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.entity.FestivalSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigBannerSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigFestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfiguracionFestivalEntity
import biz.belcorp.consultoras.data.net.service.IFestivalService
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.exception.NotImplementedException
import kotlinx.coroutines.Deferred

class FestivalCloudDataStore(private val service: IFestivalService): FestivalDataStore {
    override fun getConfiguracionCategorias(): List<DBConfigFestivalCategoriaEntity?> {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun getConfiguracionBannerSello(): DBConfigBannerSelloEntity {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun getConfiguracionFestival(): DBConfiguracionFestivalEntity {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun getConfiguracion(grantype: Int): Deferred<FestivalConfiguracionEntity?> {
        return service.getConfiguracion(grantype)
    }

    override fun getLocalConfiguracion() : DBConfiguracionFestivalEntity? {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveConfigurationFestival(input: DBConfiguracionFestivalEntity?) {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveConfigurationFestivalBannerSello(banner: DBConfigBannerSelloEntity?) {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun saveConfigurationCategory(input: List<DBConfigFestivalCategoriaEntity?>?) {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun getSelloConfiguracion(): FestivalSelloEntity? {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }
}
