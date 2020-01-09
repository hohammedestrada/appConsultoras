package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SearchRequesEntityDataMapper @Inject
internal constructor(private val productCuvEntityDataMapper: ProductCuvEntityDataMapper) {

    fun transform(input: SearchRequest?): SearchRequestEntity? {
        return input?.let {
            SearchRequestEntity().apply {
                campaniaId = it.campaniaId
                codigoZona = it.codigoZona
                textoBusqueda = it.textoBusqueda
                personalizacionesDummy = it.personalizacionesDummy
                fechaInicioFacturacion = it.fechaInicioFacturacion
                configuracion = SearchConfiguracionEntity().apply {
                    rdEsSuscrita = it.configuracion?.rdEsSuscrita
                    rdEsActiva = it.configuracion?.rdEsActiva
                    lider = it.configuracion?.lider
                    rdActivoMdo = it.configuracion?.rdActivoMdo
                    rdTieneRDC = it.configuracion?.rdTieneRDC
                    rdTieneRDI = it.configuracion?.rdTieneRDI
                    rdTieneRDCR = it.configuracion?.rdTieneRDCR
                    diaFacturacion = it.configuracion?.diaFacturacion
                    agrupaPromociones = it.configuracion?.agrupaPromociones
                }
                paginacion = SearchPaginacionEntity().apply {
                    numeroPagina = it.paginacion?.numeroPagina
                    cantidad = it.paginacion?.cantidad
                }
                orden = SearchOrdenEntity().apply {
                    campo = it.orden?.campo
                    tipo = it.orden?.tipo
                }
                filtros = transformSearchFilter(it.filtros)
            }
        }
    }

    fun transformSearchResult(input: ServiceDto<SearchResponseEntity?>?): BasicDto<SearchResponse?>? {
        return input?.let {
            BasicDto<SearchResponse?>().apply {
                code = it.code
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<SearchResponseEntity>() {}.type
                try {
                    val epe: SearchResponseEntity = Gson().fromJson(json, type)
                    data = SearchResponse().apply {
                        total = epe.total
                        productos = productCuvEntityDataMapper.transform(epe.productos)
                        filtros = transformFilter(epe.filtros)
                        promocion =  Promotion()
                        promocion!!.detalle = productCuvEntityDataMapper.transform(epe.promocion?.detalle)
                        promocion!!.posicion = epe.promocion?.posicion
                    }


                } catch (e: Exception) {
                    BelcorpLogger.d(e.message)
                }
                message = it.message
            }
        }
    }

    fun transformOrderByResult(input: ServiceDto<List<SearchOrderByResponseEntity?>?>?): BasicDto<Collection<SearchOrderByResponse?>?>? {
        return input?.let {
            BasicDto<Collection<SearchOrderByResponse?>?>().apply {
                code = it.code
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<List<SearchOrderByResponseEntity?>?>() {}.type
                try {
                    val epe: List<SearchOrderByResponseEntity> = Gson().fromJson(json, type)
                    data = productCuvEntityDataMapper.transformOrderBy(epe)
                } catch (e: Exception) {
                    BelcorpLogger.d(e.message)
                }
                message = it.message
            }
        }
    }

    fun transformSearchFilter(filters: List<SearchFilter?>?) : List<SearchFilterEntity>?{
        val lista = mutableListOf<SearchFilterEntity>()
        filters?.forEach {
            lista.add(SearchFilterEntity().apply {
                nombreGrupo = it?.nombreGrupo
                opciones = transformSearchChildFilter(it?.opciones)
            })
        }
        return lista
    }

    fun transformSearchChildFilter(childs: List<SearchFilterChild?>?) : List<SearchFilterChildEntity>?{
        val lista = mutableListOf<SearchFilterChildEntity>()
        childs?.forEach {
            lista.add(SearchFilterChildEntity().apply {
                idFiltro = it?.idFiltro
                cantidad = it?.cantidad
                marcado = it?.marcado
                nombreFiltro = it?.nombreFiltro
                max = it?.max
                min = it?.min
                idSeccion = it?.idSeccion
            })
        }
        return lista
    }

    fun transform(input: SearchFilterEntity?): SearchFilter? {
        return input?.let {
            SearchFilter().apply {
                nombreGrupo = it.nombreGrupo
                opciones = transformFilterChilds(it.opciones)
            }
        }
    }

    fun transform(input: SearchFilterChildEntity?): SearchFilterChild? {
        return input?.let {
            SearchFilterChild().apply {
                idFiltro = it.idFiltro
                cantidad = it.cantidad
                marcado = it.marcado
                max = it.max
                min = it.min
                nombreFiltro = it.nombreFiltro
            }
        }
    }

    fun transformFilter(input: List<SearchFilterEntity?>?): List<SearchFilter?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
                emptyList<SearchFilter>()
            }
    }

    fun transformFilterChilds(input: List<SearchFilterChildEntity?>?): List<SearchFilterChild?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
                emptyList<SearchFilterChild>()
            }
    }


}
