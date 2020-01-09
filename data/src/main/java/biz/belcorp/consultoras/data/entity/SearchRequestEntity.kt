package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.*
import com.google.gson.annotations.SerializedName

class SearchRequestEntity {

    @SerializedName("CampaniaID")
    var campaniaId: Int? = null
    @SerializedName("CodigoZona")
    var codigoZona: String? = ""
    @SerializedName("TextoBusqueda")
    var textoBusqueda: String? = ""
    @SerializedName("PersonalizacionesDummy")
    var personalizacionesDummy: String = ""
    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion: String = ""
    @SerializedName("Configuracion")
    var configuracion: SearchConfiguracionEntity? = null
    @SerializedName("Paginacion")
    var paginacion: SearchPaginacionEntity? = null
    @SerializedName("Orden")
    var orden: SearchOrdenEntity? = null
    @SerializedName("Filtros")
    var filtros: List<SearchFilterEntity?>? = mutableListOf<SearchFilterEntity>()

    companion object {

        fun transform(input: SearchRequest?): SearchRequestEntity? {
            input?.run {
                val newSearchRQEntity = SearchRequestEntity()
                newSearchRQEntity.campaniaId = campaniaId
                newSearchRQEntity.codigoZona = codigoZona
                newSearchRQEntity.textoBusqueda = textoBusqueda
                newSearchRQEntity.personalizacionesDummy = personalizacionesDummy
                newSearchRQEntity.fechaInicioFacturacion = fechaInicioFacturacion
                newSearchRQEntity.configuracion = SearchConfiguracionEntity.transform(configuracion)
                newSearchRQEntity.paginacion = SearchPaginacionEntity.transform(paginacion)
                newSearchRQEntity.orden = SearchOrdenEntity.transform(orden)
                newSearchRQEntity.filtros = SearchFilterEntity.transformList(filtros)
                return newSearchRQEntity
            }
            return null
        }

    }

}

class SearchConfiguracionEntity {

    @SerializedName("RDEsSuscrita")
    var rdEsSuscrita: Boolean? = null
    @SerializedName("RDEsActiva")
    var rdEsActiva: Boolean? = null
    @SerializedName("Lider")
    var lider: Int? = null
    @SerializedName("RDActivoMdo")
    var rdActivoMdo: Boolean? = null
    @SerializedName("RDTieneRDC")
    var rdTieneRDC: Boolean? = null
    @SerializedName("RDTieneRDI")
    var rdTieneRDI: Boolean? = null
    @SerializedName("RDTieneRDCR")
    var rdTieneRDCR: Boolean? = null
    @SerializedName("DiaFacturacion")
    var diaFacturacion: Int? = null
    @SerializedName("AgrupaPromociones")
    var agrupaPromociones: Boolean? = false

    companion object {

        fun transform(input: SearchConfiguracion?): SearchConfiguracionEntity? {
            input?.run {
                val newSearchEntity = SearchConfiguracionEntity()
                newSearchEntity.rdEsSuscrita = rdEsSuscrita
                newSearchEntity.rdEsActiva = rdEsActiva
                newSearchEntity.lider = lider
                newSearchEntity.rdActivoMdo = rdActivoMdo
                newSearchEntity.rdTieneRDC = rdTieneRDC
                newSearchEntity.rdTieneRDI = rdTieneRDI
                newSearchEntity.rdTieneRDCR = rdTieneRDCR
                newSearchEntity.diaFacturacion = diaFacturacion
                newSearchEntity.agrupaPromociones = agrupaPromociones
                return newSearchEntity
            }
            return null
        }

    }

}

class SearchPaginacionEntity {

    @SerializedName("NumeroPagina")
    var numeroPagina: Int? = null
    @SerializedName("Cantidad")
    var cantidad: Int? = null

    companion object {

        fun transform(input: SearchPaginacion?): SearchPaginacionEntity? {
            input?.run {
                val newSearchEntity = SearchPaginacionEntity()
                newSearchEntity.numeroPagina = numeroPagina
                newSearchEntity.cantidad = cantidad
                return newSearchEntity
            }
            return null
        }

    }
}

class SearchOrdenEntity {

    @SerializedName("Campo")
    var campo: String? = ""
    @SerializedName("Tipo")
    var tipo: String? = ""

    companion object {

        fun transform(input: SearchOrden?): SearchOrdenEntity? {
            input?.run {
                val newSearchEntity = SearchOrdenEntity()
                newSearchEntity.campo = campo
                newSearchEntity.tipo = tipo
                return newSearchEntity
            }
            return null
        }

    }

}

class SearchFilterEntity {
    @SerializedName("NombreGrupo")
    var nombreGrupo: String? = null
    @SerializedName("Opciones")
    var opciones: List<SearchFilterChildEntity?>? = null

    companion object {

        fun transformList(list: Collection<SearchFilter?>?): List<SearchFilterEntity?>? {
            return mutableListOf<SearchFilterEntity>().apply {
                list?.forEach {
                    it?.let { it1 -> SearchFilterEntity.transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: SearchFilter?): SearchFilterEntity? {
            input?.run {
                val newSearchEntity = SearchFilterEntity()
                newSearchEntity.nombreGrupo = nombreGrupo
                newSearchEntity.opciones = SearchFilterChildEntity.transformList(opciones)
                return newSearchEntity
            }
            return null
        }

        fun transformListEntity(list: Collection<SearchFilterEntity?>?): List<SearchFilter?>? {
            return mutableListOf<SearchFilter>().apply {
                list?.forEach {
                    it?.let { it1 -> SearchFilterEntity.transformEntity(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transformEntity(input: SearchFilterEntity?): SearchFilter? {
            input?.run {
                val newSearchFilter = SearchFilter()
                newSearchFilter.nombreGrupo = nombreGrupo
                newSearchFilter.opciones = SearchFilterChildEntity.transformListEntity(opciones)
                return newSearchFilter
            }
            return null
        }

    }

}

class SearchFilterChildEntity {
    @SerializedName("IdFiltro")
    var idFiltro: String? = null
    @SerializedName("NombreFiltro")
    var nombreFiltro: String? = null
    @SerializedName("Cantidad")
    var cantidad: Int? = null
    @SerializedName("Marcado")
    var marcado: Boolean? = false
    @SerializedName("Min")
    var min: Int? = 0
    @SerializedName("Max")
    var max: Int? = 0
    @SerializedName("IdSeccion")
    var idSeccion: String? = null

    companion object {

        fun transformList(list: List<SearchFilterChild?>?): List<SearchFilterChildEntity?>? {
            return mutableListOf<SearchFilterChildEntity>().apply {
                list?.forEach {
                    it?.let { it1 -> SearchFilterChildEntity.transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: SearchFilterChild?): SearchFilterChildEntity? {
            input?.run {
                val newSearchEntity = SearchFilterChildEntity()
                newSearchEntity.idFiltro = idFiltro
                newSearchEntity.nombreFiltro = nombreFiltro
                newSearchEntity.cantidad = cantidad
                newSearchEntity.marcado = marcado
                newSearchEntity.min = min
                newSearchEntity.max = max
                newSearchEntity.idSeccion = idSeccion
                return newSearchEntity
            }
            return null
        }

        fun transformListEntity(list: List<SearchFilterChildEntity?>?): List<SearchFilterChild?>? {
            return mutableListOf<SearchFilterChild>().apply {
                list?.forEach {
                    it?.let { it1 -> SearchFilterChildEntity.transformEntity(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transformEntity(input: SearchFilterChildEntity?): SearchFilterChild? {
            input?.run {
                val newSearchFilterChild = SearchFilterChild()
                newSearchFilterChild.idFiltro = idFiltro
                newSearchFilterChild.nombreFiltro = nombreFiltro
                newSearchFilterChild.cantidad = cantidad
                newSearchFilterChild.marcado = marcado
                newSearchFilterChild.min = min
                newSearchFilterChild.max = max
                newSearchFilterChild.idSeccion = idSeccion
                return newSearchFilterChild
            }
            return null
        }

    }

}


class PromotionEntity {

    @SerializedName("Detalle")
    var detalle: List<ProductCuvEntity?>? = null

    @SerializedName("Posicion")
    var posicion: Int? = null

}
