package biz.belcorp.consultoras.data.mapper

import java.util.ArrayList
import java.util.Collections

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.CatalogoEntity
import biz.belcorp.consultoras.domain.entity.Catalogo
import biz.belcorp.consultoras.domain.entity.CatalogoWrapper

@Singleton
class CatalogoEntityDataMapper @Inject
internal constructor() {

    fun transform(input: Catalogo?): CatalogoEntity? {
        return input?.let {
            CatalogoEntity().apply {
                marcaId = it.marcaId
                marcaDescripcion = it.marcaDescripcion
                urlImagen = it.urlImagen
                urlCatalogo = it.urlCatalogo
                titulo = it.titulo
                descripcion = it.descripcion
                urlDescargaEstado = it.urlDescargaEstado
                it.campaniaId?.let {c ->
                    campaniaID = Integer.parseInt(c)
                }
            }
        }
    }

    fun transform(input: CatalogoEntity?): Catalogo? {
        return input?.let {
            Catalogo().apply {
                marcaId = it.marcaId
                marcaDescripcion = it.marcaDescripcion
                urlImagen = it.urlImagen
                urlCatalogo = it.urlCatalogo
                titulo = it.titulo
                descripcion = it.descripcion
                urlDescargaEstado = it.urlDescargaEstado
                it.campaniaID?.let {c ->
                    campaniaId = c.toString()
                }
            }
        }
    }

    fun transform(input: Collection<Catalogo?>?): List<CatalogoEntity?>? {
        return input?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<CatalogoEntity>()
        }
    }

    fun transformCollection(input: List<CatalogoEntity?>?): ArrayList<CatalogoWrapper?>? {

        val catalogoWrappers = ArrayList<CatalogoWrapper?>()
        if (null == input || input.isEmpty()) return catalogoWrappers

        Collections.sort(input) { o1, o2 ->
            val x = o1?.campaniaID
            val y = o2?.campaniaID
            if(x!= null && y != null){
                x.compareTo(y)
            } else {
                0
            }
        }

        input.getCampanias().forEach { campaniaID ->
            transform(input.getCatalogos(campaniaID))?.let { catalogoWrappers.add(it) }
        }

        return catalogoWrappers
    }

    private fun List<CatalogoEntity?>.getCatalogos(id: Int): ArrayList<CatalogoEntity>{
        val list = ArrayList<CatalogoEntity>()
        for( a in this){ if(a?.campaniaID == id) list.add(a)}
        return list
    }

    private fun List<CatalogoEntity?>.getCampanias(): ArrayList<Int>{
        val list = ArrayList<Int>()
        for( a in this){ if(!list.existeCampania(a?.campaniaID)) list.add(a?.campaniaID!!)}
        return list
    }

    private fun List<Int>.existeCampania(id: Int?): Boolean{
        for (a in this) if (a == id) return true
        return false
    }

    fun transform(input: List<CatalogoEntity>?): CatalogoWrapper? {
        val output = CatalogoWrapper()

        input?.let {
            if (it.isEmpty()) return output
        }

        output.catalogoEntities = ArrayList()
        output.campaignName = input?.get(0)?.campaniaID!!.toString()

        for (entity in input) {
            val model = transform(entity)
            model?.let{
                if (it.marcaDescripcion == "Revista")
                    output.magazineEntity = it
                else
                    (output.catalogoEntities as ArrayList<Catalogo>).add(it)
            }
        }

        return output
    }
}
