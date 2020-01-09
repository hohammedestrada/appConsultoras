package biz.belcorp.consultoras.data.repository.datasource.festival

import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.entity.FestivalSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigBannerSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigFestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfiguracionFestivalEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.exception.NotImplementedException
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.result
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import kotlinx.coroutines.Deferred

class FestivalDBDataSore : FestivalDataStore {


    override fun getConfiguracionFestival(): DBConfiguracionFestivalEntity? {
        return (select from DBConfiguracionFestivalEntity::class).queryList().firstOrNull()
    }

    override fun getConfiguracionBannerSello(): DBConfigBannerSelloEntity? {
        return (select from DBConfigBannerSelloEntity::class).queryList().firstOrNull()
    }

    override fun getConfiguracionCategorias(): List<DBConfigFestivalCategoriaEntity?> {
        return (select from DBConfigFestivalCategoriaEntity::class).queryList()
    }


    override fun getConfiguracion(grantype: Int): Deferred<FestivalConfiguracionEntity?> {
        throw NotImplementedException(Constant.NOT_IMPLEMENTED)
    }

    override fun getLocalConfiguracion(): DBConfiguracionFestivalEntity? {
        val localResult = (select from  DBConfiguracionFestivalEntity::class).result

        return localResult
    }

    override fun saveConfigurationFestival(input: DBConfiguracionFestivalEntity?) {
        input?.let {
            Delete.table(DBConfiguracionFestivalEntity::class.java)
            FlowManager.getModelAdapter(DBConfiguracionFestivalEntity::class.java).insert(it)
        }
    }

    override fun saveConfigurationFestivalBannerSello(input: DBConfigBannerSelloEntity?) {
        input?.let {
            Delete.table(DBConfigBannerSelloEntity::class.java)
            FlowManager.getModelAdapter(DBConfigBannerSelloEntity::class.java).insert(it)
        }
    }

    override fun saveConfigurationCategory(input: List<DBConfigFestivalCategoriaEntity?>?) {
        input?.let {list ->
            Delete.table(DBConfigFestivalCategoriaEntity::class.java)

            list.filterNotNull().let {
                it.forEach {item ->
                    FlowManager.getModelAdapter(DBConfigFestivalCategoriaEntity::class.java).insert(item)
                }
            }
        }
    }

    override fun getSelloConfiguracion(): FestivalSelloEntity? {
        val result = (select from DBConfigBannerSelloEntity::class).queryList()
        result.firstOrNull {selloEntity ->
            return FestivalSelloEntity().apply {
                selloColorInicio = selloEntity.selloColorInicio
                selloColorFin = selloEntity.selloColorFin
                selloColorDireccion = selloEntity.selloColorDireccion
                selloTexto = selloEntity.selloTexto
                selloColorTexto = selloEntity.selloColorTexto
            }
        }
        return null
    }
}
