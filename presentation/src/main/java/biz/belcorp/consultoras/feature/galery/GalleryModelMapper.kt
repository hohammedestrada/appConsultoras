package biz.belcorp.consultoras.feature.galery

import biz.belcorp.consultoras.domain.entity.gallery.ListadoImagen
import biz.belcorp.consultoras.domain.entity.gallery.FiltroGaleria

class GalleryModelMapper {
    fun transform(input: FiltroGaleria): FiltroGaleriaModel {
        return FiltroGaleriaModel().apply {
            Codigo = input.Codigo
            Descripcion = input.Descripcion
            Tipo = input.Tipo
            Orden = input.Orden
            Activo = input.Activo
            IdPadre = input.IdPadre
            CodigoPadre = input.CodigoPadre
            OrdenPadre = input.OrdenPadre
            Otros = input.Otros
            OtrosAdd = input.OtrosAdd
            EsSeccion = input.EsSeccion
            EsExcluyente = input.EsExcluyente
        }
    }

    fun transform(input: ListadoImagen): ListadoImagenModel {
        return ListadoImagenModel().apply {
            titulo = input.titulo
            nombreArchivo = input.nombreArchivo
            urlImagenThumb = input.urlImagenThumb
            urlImagenVisualiza = input.urlImagenVisualiza
            urlImagenDescarga = input.urlImagenDescarga
            categoria = input.categoria
            orden = input.orden
            seccion = input.seccion
        }
    }

    fun transformFiltros(input : ArrayList<FiltroGaleria?>) : ArrayList<FiltroGaleriaModel>{
        return ArrayList<FiltroGaleriaModel>().apply{
            input.forEach {t ->
                t?.let {
                    add(transform(it))
                }
            }
        }
    }

    fun transformFiles(input : ArrayList<ListadoImagen>) : ArrayList<ListadoImagenModel>{
        return ArrayList<ListadoImagenModel>().apply {
            input.forEach {t ->
                t.let {
                    add(transform(it))
                }
            }
        }
    }

}
