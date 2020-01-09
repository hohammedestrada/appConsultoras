package biz.belcorp.consultoras.data.entity.navifest.mapper

import biz.belcorp.consultoras.data.entity.FestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigFestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfiguracionFestivalEntity

class ConfigFestivalCategoriaDataMapper {

    fun transform(input : List<FestivalCategoriaEntity?>?) : List<DBConfigFestivalCategoriaEntity?>?{
        return ArrayList<DBConfigFestivalCategoriaEntity>().apply {
            input?.let { l ->
                l.forEach {item ->
                    item?.let {
                        add(transform(it))
                    }
                }
            }
        }
    }


    fun transform(input : FestivalCategoriaEntity) : DBConfigFestivalCategoriaEntity{
        return DBConfigFestivalCategoriaEntity().apply {

            idFestivalCategoria = input.idFestivalCategoria
            idFestival = input.idFestival
            codigoCategoria = input.CodigoCategoria
            isActivo = input.Activo

        }
    }
}
