package biz.belcorp.consultoras.data.entity.navifest.mapper

import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfiguracionFestivalEntity

class ConfiguracionFestivalDataMapper {

    fun transform(input : FestivalConfiguracionEntity) : DBConfiguracionFestivalEntity{
        return DBConfiguracionFestivalEntity().apply {
            idFestival = input.idFestival
            campaniaId = input.campaniaId
            isPremioGratis = input.PremioGratis
            tipo = input.Tipo
            isActivo = input.Activo
            titulo = input.Titulo
            descripcionCorta = input.DescripcionCorta
            descripcionLarga = input.DescripcionCorta
        }
    }
}
