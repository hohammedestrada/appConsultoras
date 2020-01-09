package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.FestivalBannerEntity
import biz.belcorp.consultoras.data.entity.FestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.entity.FestivalSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigBannerSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigFestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfiguracionFestivalEntity
import biz.belcorp.consultoras.domain.entity.FestivalSello
import biz.belcorp.consultoras.domain.entity.FestivalBanner
import biz.belcorp.consultoras.domain.entity.FestivalCategoria
import biz.belcorp.consultoras.domain.entity.FestivalConfiguracion
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FestivalConfiguracionDataMapper@Inject
internal constructor()  {



    fun transformDbToConfiguracion( entity: DBConfiguracionFestivalEntity?): FestivalConfiguracion? {
        return entity?.let {
            FestivalConfiguracion().apply {
                idFestival = it.idFestival
                campaniaId = it.campaniaId
                PremioGratis = it.isPremioGratis
                Tipo = it.tipo
                Activo = it.isActivo
                Titulo = it.titulo
                DescripcionCorta = it.descripcionCorta
                DescripcionLarga = it.descripcionLarga
//                Banner =  transformBanner(it.Banner)
//                Sello = transformSello(it.Sello)
//                Categoria = transformCategoriaList(it.Categoria)
            }
        }
    }


    fun transformDbToBanner( entity: DBConfigBannerSelloEntity?): FestivalBanner? {
        return entity?.let {
            FestivalBanner().apply {
                bannerImgDesktop = it.bannerImgDesktop
                bannerImgMobile = it.bannerImgMobile
                bannerImgProducto = it.bannerImgProducto
                bannerFondoColorInicio = it.bannerFondoColorInicio
                bannerFondoColorFin = it.bannerFondoColorFin
                bannerFondoColorDireccion = it.bannerFondoColorDireccion
                bannerColorTexto = it.bannerColorTexto
                bannerDescripcion = it.bannerDescripcion
            }
        }
    }


    fun transformDbToCategoria( entity: List<DBConfigFestivalCategoriaEntity?>): List<FestivalCategoria?> {
        val listCategoria = mutableListOf<FestivalCategoria>()
        entity?.map {
            listCategoria.add(FestivalCategoria().apply {
                idFestivalCategoria = it?.idFestivalCategoria
                idFestival = it?.idFestival
                CodigoCategoria = it?.codigoCategoria
                Activo = it?.isActivo
            })
        }
        return listCategoria
    }


    fun transform( entity: FestivalConfiguracionEntity?): FestivalConfiguracion? {
        return entity?.let {
            FestivalConfiguracion().apply {
                idFestival = it.idFestival
                campaniaId = it.campaniaId
                PremioGratis = it.PremioGratis
                Tipo = it.Tipo
                Activo = it.Activo
                Titulo = it.Titulo
                DescripcionCorta = it.DescripcionCorta
                DescripcionLarga = it.DescripcionLarga
                Banner =  transformBanner(it.Banner)
                Sello = transformSello(it.Sello)
                Categoria = transformCategoriaList(it.Categoria)
            }
        }
    }

    private fun transformCategoriaList(list: List<FestivalCategoriaEntity?>?) : List<FestivalCategoria>{
        val listCategoria = mutableListOf<FestivalCategoria>()
        list?.map {
            listCategoria.add(FestivalCategoria().apply {
                idFestivalCategoria = it?.idFestivalCategoria
                idFestival = it?.idFestival
                CodigoCategoria = it?.CodigoCategoria
                Activo = it?.Activo
            })
        }
        return listCategoria
    }

    private fun transformBanner(input: FestivalBannerEntity?) : FestivalBanner?{

        return input?.let {
            FestivalBanner().apply {
                bannerImgDesktop = it.bannerImgDesktop
                bannerImgMobile = it.bannerImgMobile
                bannerImgProducto = it.bannerImgProducto
                bannerFondoColorInicio = it.bannerFondoColorInicio
                bannerFondoColorFin = it.bannerFondoColorFin
                bannerFondoColorDireccion = it.bannerFondoColorDireccion
                bannerColorTexto = it.bannerColorTexto
                bannerDescripcion = it.bannerDescripcion
            }
        }
    }

    private fun transformSello(input: FestivalSelloEntity?) : FestivalSello?{
        return input?.let {
            FestivalSello().apply {
                selloColorInicio = it.selloColorInicio
                selloColorFin = it.selloColorFin
                selloColorDireccion = it.selloColorDireccion
                selloTexto = it.selloTexto
                selloColorTexto = it.selloColorTexto
            }
        }
    }

    fun transformBannerSelloDB(banner : FestivalBannerEntity?, sello : FestivalSelloEntity?) : DBConfigBannerSelloEntity?{
        return banner?.let {b ->
            sello?.let {s ->
                DBConfigBannerSelloEntity().apply {
                    bannerImgDesktop = b.bannerImgDesktop
                    bannerImgMobile = b.bannerImgMobile
                    bannerImgProducto = b.bannerImgProducto
                    bannerFondoColorInicio = b.bannerFondoColorInicio
                    bannerFondoColorFin = b.bannerFondoColorFin
                    bannerFondoColorDireccion = b.bannerFondoColorDireccion
                    bannerColorTexto = b.bannerColorTexto
                    bannerDescripcion = b.bannerDescripcion

                    selloColorInicio = s.selloColorInicio
                    selloColorFin = s.selloColorFin
                    selloColorDireccion = s.selloColorDireccion
                    selloTexto = s.selloTexto
                    selloColorTexto = s.selloColorTexto
                }
            }
        }
    }

    fun transformListCategoriaDB(input : List<FestivalCategoriaEntity?>?) : List<DBConfigFestivalCategoriaEntity?>?{
        return input?.let {list ->
            ArrayList<DBConfigFestivalCategoriaEntity>().apply {
                list.forEach {item ->
                    var transformed = transformCategoriaDB(item)

                    transformed?.let {
                        add(it)
                    }
                }
            }
        }
    }

    fun transformFestivalCOnfiguration(input : FestivalConfiguracionEntity?) : DBConfiguracionFestivalEntity?{

        return input?.let {
            DBConfiguracionFestivalEntity().apply {
                idFestival = it.idFestival
                campaniaId = it.campaniaId
                isPremioGratis = it.PremioGratis
                tipo = it.Tipo
                isActivo = it.Activo
                titulo = it.Titulo
                descripcionCorta = it.DescripcionCorta
                descripcionLarga = it.DescripcionLarga
            }
        }
    }

    fun transformFestivalCOnfiguration(input : DBConfiguracionFestivalEntity?) : FestivalConfiguracionEntity?{

        return input?.let {
            FestivalConfiguracionEntity().apply {
                idFestival = it.idFestival
                campaniaId = it.campaniaId
                PremioGratis= it.isPremioGratis
                Tipo = it.tipo
                Activo = it.isActivo
                Titulo = it.titulo
                DescripcionCorta = it.descripcionCorta
                DescripcionLarga = it.descripcionLarga
            }
        }
    }

    private fun transformCategoriaDB(input : FestivalCategoriaEntity?) : DBConfigFestivalCategoriaEntity?{
        return input?.let {
            DBConfigFestivalCategoriaEntity().apply {
                idFestivalCategoria = it.idFestivalCategoria
                idFestival = it.idFestival
                codigoCategoria = it.CodigoCategoria
                isActivo = it.Activo
            }
        }
    }

}
