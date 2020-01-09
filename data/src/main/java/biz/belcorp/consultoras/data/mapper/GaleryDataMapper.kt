package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.gallery.ListadoImagenEntity
import biz.belcorp.consultoras.data.entity.gallery.GalleryResponseEntity
import biz.belcorp.consultoras.data.entity.gallery.FiltroGaleriaEntity
import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.domain.entity.gallery.ListadoImagen
import biz.belcorp.consultoras.domain.entity.gallery.GalleryResponse
import biz.belcorp.consultoras.domain.entity.gallery.FiltroGaleria

@Singleton
class GaleryDataMapper @Inject
internal constructor() {

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: ListadoImagenEntity?): ListadoImagen? {
        return input?.let {
            ListadoImagen().apply {
                titulo  = it.Titulo
                nombreArchivo = it.NombreArchivo
                urlImagenThumb = it.UrlImagenThumb
                urlImagenVisualiza = it.UrlImagenVisualiza
                urlImagenDescarga = it.UrlImagenDescarga
                categoria = it.Categoria
                orden = it.Orden
                seccion = it.Seccion
            }
        }
    }

    fun transform(input : FiltroGaleriaEntity?) : FiltroGaleria?{
        return input?.let {
            FiltroGaleria().apply {
                Codigo = it.Codigo
                Descripcion = it.Descripcion
                Tipo = it.Tipo
                Orden = it.Orden
                Activo = it.Activo
                IdPadre = it.IdPadre
                CodigoPadre = it.CodigoPadre
                OrdenPadre = it.OrdenPadre
                Otros = it.Otros
                OtrosAdd = it.OtrosAdd
                EsSeccion = it.EsSeccion
                EsExcluyente = it.EsExcluyente
            }
        }
    }

    fun transformImg(input: ArrayList<ListadoImagenEntity?>?) : ArrayList<ListadoImagen?>?{
        return ArrayList<ListadoImagen?>().apply {
            input?.let {inp ->
                inp?.forEach {item ->
                    item?.let {
                        add(transform(it))
                    }
                }
            }
        }
    }

    fun transformTab(input: ArrayList<FiltroGaleriaEntity?>?) : ArrayList<FiltroGaleria?>?{
        return ArrayList<FiltroGaleria?>().apply {
            input?.let {inp ->
                inp?.forEach {item ->
                    item?.let {
                        add(transform(it))
                    }
                }
            }
        }
    }

    fun transform(input : GalleryResponseEntity?) : GalleryResponse?{
        return input?.let {
            GalleryResponse().apply {
                filtroGaleria = transformTab(it.tabsGaleria)
                listadoImagen = transformImg(it.listadoImagen)
            }
        }
    }
}
