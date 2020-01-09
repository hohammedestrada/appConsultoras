package biz.belcorp.consultoras.data.entity.navifest.mapper

import biz.belcorp.consultoras.data.entity.FestivalBannerEntity
import biz.belcorp.consultoras.data.entity.FestivalSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigBannerSelloEntity

class ConfigBannerSelloDataMapper {

    fun transform(banner : FestivalBannerEntity, sello : FestivalSelloEntity) : DBConfigBannerSelloEntity{
        return DBConfigBannerSelloEntity().apply {

            bannerImgDesktop = banner.bannerImgDesktop
            bannerImgMobile = banner.bannerImgMobile
            bannerImgProducto = banner.bannerImgProducto
            bannerFondoColorInicio = banner.bannerFondoColorInicio
            bannerFondoColorFin = banner.bannerFondoColorFin
            bannerFondoColorDireccion = banner.bannerFondoColorDireccion
            bannerColorTexto = banner.bannerColorTexto
            bannerDescripcion = banner.bannerDescripcion

            selloColorInicio = sello.selloColorInicio
            selloColorFin = sello.selloColorFin
            selloColorDireccion = sello.selloColorDireccion
            selloTexto = sello.selloTexto
            selloColorTexto = sello.selloColorTexto
        }
    }
}
