package biz.belcorp.consultoras.data.repository.datasource.festival

import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.entity.FestivalSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigBannerSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigFestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfiguracionFestivalEntity
import kotlinx.coroutines.Deferred

interface FestivalDataStore {

    fun getConfiguracionCategorias(): List<DBConfigFestivalCategoriaEntity?>

    fun getConfiguracionBannerSello(): DBConfigBannerSelloEntity?

    fun getConfiguracionFestival(): DBConfiguracionFestivalEntity?

    fun getConfiguracion(grantype: Int): Deferred<FestivalConfiguracionEntity?>

    fun getLocalConfiguracion(): DBConfiguracionFestivalEntity?

    fun saveConfigurationFestival(input : DBConfiguracionFestivalEntity?)

    fun saveConfigurationFestivalBannerSello(input : DBConfigBannerSelloEntity?)

    fun saveConfigurationCategory (input : List<DBConfigFestivalCategoriaEntity?>?)

    fun getSelloConfiguracion():  FestivalSelloEntity?
}
