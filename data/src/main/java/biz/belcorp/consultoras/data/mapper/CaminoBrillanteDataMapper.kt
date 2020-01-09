package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.FiltroEntity
import biz.belcorp.consultoras.data.entity.GroupFilterEntity
import biz.belcorp.consultoras.data.entity.OfertaEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.*
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.Ordenamiento
import biz.belcorp.consultoras.domain.entity.caminobrillante.*
import biz.belcorp.library.net.dto.ServiceDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaminoBrillanteDataMapper @Inject
internal constructor() {

    fun transformLogros(logros: List<LogroCaminoBrillanteEntity>): List<LogroCaminoBrillante> {
        val output = mutableListOf<LogroCaminoBrillante>()
        logros.forEach {
            output.add(transformLogro(it))
        }
        return output
    }

    fun transformLogro(logro: LogroCaminoBrillanteEntity): LogroCaminoBrillante {
        return LogroCaminoBrillante().apply {
            id = logro.id
            titulo = logro.titulo
            descripcion = logro.descripcion
            indicadores = transformIndicadores(logro.indicadores)
        }
    }

    fun transformIndicadores(indicadores: List<LogroCaminoBrillanteEntity.IndicadorEntity>?): List<LogroCaminoBrillante.Indicador>? {
        val output = mutableListOf<LogroCaminoBrillante.Indicador>()
        indicadores?.forEach {
            output.add(transformIndicador(it))
        }
        return output
    }

    fun transformIndicador(indicador: LogroCaminoBrillanteEntity.IndicadorEntity): LogroCaminoBrillante.Indicador {
        return LogroCaminoBrillante.Indicador().apply {
            id = indicador.id
            codigo = indicador.codigo
            idLogro = indicador.idLogro
            titulo = indicador.titulo
            descripcion = indicador.descripcion
            medallas = transformMedallas(indicador.medallas)
        }
    }

    fun transformMedallas(medallas: List<LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity>?): List<LogroCaminoBrillante.Indicador.Medalla>? {
        val output = mutableListOf<LogroCaminoBrillante.Indicador.Medalla>()
        medallas?.forEach {
            output.add(transformMedalla(it))
        }
        return output
    }

    private fun transformMedalla(medalla: LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity): LogroCaminoBrillante.Indicador.Medalla {
        return LogroCaminoBrillante.Indicador.Medalla().apply {
            id = medalla.id
            tipo = medalla.tipo
            titulo = medalla.titulo
            subtitulo = medalla.subtitulo
            valor = medalla.valor
            isDestacado = medalla.isDestacado
            isEstado = medalla.isEstado
            modalTitulo = medalla.modalTitulo
            modalDescripcion = medalla.modalDescripcion
        }
    }

    fun transformNivelesCaminoBrillante(niveles: List<NivelCaminoBrillanteEntity>): List<NivelCaminoBrillante>? {
        val output = mutableListOf<NivelCaminoBrillante>()
        niveles.forEach {
            output.add(transformNivelCaminoBrillante(it))
        }
        return output
    }

    fun transformNivelCaminoBrillante(nivel: NivelCaminoBrillanteEntity): NivelCaminoBrillante {
        return NivelCaminoBrillante().apply {
            id = nivel.id
            codigoNivel = nivel.codigoNivel
            descripcionNivel = nivel.descripcionNivel
            montoMinimo = nivel.montoMinimo
            montoMaximo = nivel.montoMaximo
            isTieneOfertasEspeciales = nivel.isTieneOfertasEspeciales
            montoFaltante = nivel.montoFaltante
            urlImagenNivel = nivel.urlImagenNivel
            enterateMas = nivel.enterateMas
            enterateMasParam = nivel.enterateMasParam
            puntaje = nivel.puntaje
            puntajeAcumulado = nivel.puntajeAcumulado
            mensaje = nivel.mensaje

            nivel.beneficios?.let {
                beneficios = transformBeneficios(it)
            }
        }
    }

    fun transformBeneficios(beneficios: List<NivelCaminoBrillanteEntity.BeneficioCaminoBrillanteEntity>): List<NivelCaminoBrillante.BeneficioCaminoBrillante> {
        val output = mutableListOf<NivelCaminoBrillante.BeneficioCaminoBrillante>()
        beneficios.forEach {
            output.add(transformBeneficio(it))
        }
        return output
    }

    fun transformBeneficio(beneficio: NivelCaminoBrillanteEntity.BeneficioCaminoBrillanteEntity): NivelCaminoBrillante.BeneficioCaminoBrillante {
        return NivelCaminoBrillante.BeneficioCaminoBrillante().apply {
            codigoNivel = beneficio.codigoNivel
            codigoBeneficio = beneficio.codigoBeneficio
            nombreBeneficio = beneficio.nombreBeneficio
            descripcion = beneficio.descripcion
            urlIcono = beneficio.urlIcono
        }
    }

    fun transformNivelConsultora(nivelConsultora: NivelConsultoraCaminoBrillanteEntity): NivelConsultoraCaminoBrillante? {
        return NivelConsultoraCaminoBrillante().apply {
            id = nivelConsultora.id
            periodoCae = nivelConsultora.periodoCae
            campania = nivelConsultora.campania
            nivel = nivelConsultora.nivel
            montoPedido = nivelConsultora.montoPedido
            fechaIngreso = nivelConsultora.fechaIngreso
            kitSolicitado = nivelConsultora.kitSolicitado
            gananciaCampania = nivelConsultora.gananciaCampania
            gananciaPeriodo = nivelConsultora.gananciaPeriodo
            gananciaAnual = nivelConsultora.gananciaAnual
            isFlagSeleccionMisGanancias = nivelConsultora.isFlagSeleccionMisGanancias
            indCambioNivel = nivelConsultora.indCambioNivel
            mensajeCambioNivel = nivelConsultora.mensajeCambioNivel
        }
    }

    fun transformNivelConsultora(input: List<NivelConsultoraCaminoBrillanteEntity>?): List<NivelConsultoraCaminoBrillante>? {
        val output = mutableListOf<NivelConsultoraCaminoBrillante>()
        input?.forEach {
            transformNivelConsultora(it)?.let { it1 -> output.add(it1) }
        }
        return output
    }

    fun transformKitsCaminoBrillante(input: ServiceDto<List<KitCaminoBrillanteEntity>>?): BasicDto<List<KitCaminoBrillante>>? {
        return input?.let {
            BasicDto<List<KitCaminoBrillante>>().apply {
                code = it.code
                data = transformKitsCaminoBrillante(it.data)
                message = it.message
            }
        } ?: null
    }

    fun transformKitsCaminoBrillante(kitsCaminoBrillanteEntity: List<KitCaminoBrillanteEntity>?): List<KitCaminoBrillante>? {
        val output = mutableListOf<KitCaminoBrillante>()
        kitsCaminoBrillanteEntity?.forEach {
            output.add(transformKitCaminoBrillante(it))
        }
        return output
    }

    fun transformKitCaminoBrillante(kit: KitCaminoBrillanteEntity): KitCaminoBrillante {
        return KitCaminoBrillante().apply {
            estrategiaId = kit.estrategiaId
            codigoEstrategia = kit.codigoEstrategia
            codigoKit = kit.codigoKit
            codigoSap = kit.codigoSap
            cuv = kit.cuv
            descripcionCUV = kit.descripcionCUV
            descripcionCortaCUV = kit.descripcionCortaCUV
            marcaID = kit.marcaID
            descripcionMarca = kit.descripcionMarca
            codigoNivel = kit.codigoNivel
            descripcionNivel = kit.descripcionNivel
            precioValorizado = kit.precioValorizado
            precioCatalogo = kit.precioCatalogo
            ganancia = kit.ganancia
            fotoProductoSmall = kit.fotoProductoSmall
            fotoProductoMedium = kit.fotoProductoMedium
            tipoEstrategiaID = kit.tipoEstrategiaID
            origenPedidoWebFicha = kit.origenPedidoWebFicha
            flagSeleccionado = kit.flagSeleccionado
            flagDigitable = kit.flagDigitable
            flagHabilitado = kit.flagHabilitado
        }
    }

    fun transformDemostradoresCaminoBrillante(input: ServiceDto<List<DemostradorCaminoBrillanteEntity>>?): BasicDto<List<DemostradorCaminoBrillante>>? {
        return input?.let {
            BasicDto<List<DemostradorCaminoBrillante>>().apply {
                code = it.code
                data = transformDemostradoresCaminoBrillante(it.data)
                message = it.message
            }
        } ?: null
    }

    fun transformDemostradoresCaminoBrillante(demostradorCaminoBrillanteEntity: List<DemostradorCaminoBrillanteEntity>?): List<DemostradorCaminoBrillante> {
        val output = mutableListOf<DemostradorCaminoBrillante>()
        demostradorCaminoBrillanteEntity?.forEach {
            output.add(transformDemostradorCaminoBrillante(it))
        }
        return output
    }

    fun transformDemostradorCaminoBrillante(demostrador: DemostradorCaminoBrillanteEntity): DemostradorCaminoBrillante {
        return DemostradorCaminoBrillante().apply {
            estrategiaID = demostrador.estrategiaID
            codigoEstrategia = demostrador.codigoEstrategia
            cuv = demostrador.cuv
            descripcionCUV = demostrador.descripcionCUV
            descripcionCortaCUV = demostrador.descripcionCortaCUV
            marcaID = demostrador.marcaID
            descripcionMarca = demostrador.descripcionMarca
            precioCatalogo = demostrador.precioCatalogo
            precioValorizado = demostrador.precioValorizado
            precioFinal = demostrador.precioFinal
            ganancia = demostrador.ganancia
            fotoProductoSmall = demostrador.fotoProductoSmall
            fotoProductoMedium = demostrador.fotoProductoMedium
            tipoEstrategiaID = demostrador.tipoEstrategiaID
            origenPedidoWebFicha = demostrador.origenPedidoWebFicha
        }
    }

    fun transformConfiguracionDemostrador(entity: ServiceDto<ConfiguracionDemostradorEntity>): BasicDto<ConfiguracionDemostrador> {
        return BasicDto<ConfiguracionDemostrador>().apply {
            code = entity.code
            message = entity.message
            data = ConfiguracionDemostrador().apply {
                filtros = GroupFilterEntity.transformList(entity.data.filtros)
                ordenamientos = transformGroupsFiltersToOrdenamientos(entity.data.ordenamientos)
            }
        }
    }

    fun transformGroupsFiltersToOrdenamientos(list: List<GroupFilterEntity>?): List<Ordenamiento?>? {
        return mutableListOf<Ordenamiento>().apply {
            list?.forEach {
                it.filtros?.forEach {
                    it?.let { it1 -> transformGroupFilterToOrdenamiento(it1) }?.let { it2 -> add(it2) }
                }
            }
        }.toList()
    }

    fun transformGroupFilterToOrdenamiento(filtro: FiltroEntity): Ordenamiento {
        return Ordenamiento(null, null, null, filtro.codigo, filtro.descripcion)
    }

    fun transformCarousel(it: ServiceDto<CarouselEntity>?): BasicDto<Carousel>? {
        return BasicDto<Carousel>().apply {
            code = it?.code
            message = it?.message
            data = Carousel().apply {
                verMas = it?.data?.verMas
                items = transformOfertasCarousel(it?.data?.items)
            }
        }
    }

    fun transformOfertasCarousel(items: List<CarouselEntity.OfertaCarouselEntity>?): List<Carousel.OfertaCarousel>? {
        return mutableListOf<Carousel.OfertaCarousel>().apply {
            items?.forEach {
                add(Carousel.OfertaCarousel().apply {
                    tipoOferta = it.tipoOferta
                    estrategiaID = it.estrategiaID
                    codigoEstrategia = it.codigoEstrategia
                    tipoEstrategiaID = it.tipoEstrategiaID
                    CUV = it.CUV
                    descripcionCUV = it.descripcionCUV
                    descripcionCortaCUV = it.descripcionCortaCUV
                    marcaID = it.marcaID
                    codigoMarca = it.codigoMarca
                    descripcionMarca = it.descripcionMarca
                    codigoNivel = it.codigoNivel
                    descripcionNivel = it.descripcionNivel
                    precioValorizado = it.precioValorizado
                    precioCatalogo = it.precioCatalogo
                    ganancia = it.ganancia
                    fotoProductoSmall = it.fotoProductoSmall
                    fotoProductoMedium = it.fotoProductoMedium
                    flagSeleccionado = it.flagSeleccionado
                    flagDigitable = it.flagDigitable
                    flagHabilitado = it.flagHabilitado
                    flagHistorico = it.flagHistorico
                    isCatalogo = it.isCatalogo
                })
            }
        }.toList()
    }

    fun transformOferta(service: ServiceDto<OfertaEntity?>?): BasicDto<Oferta?> {
        return BasicDto<Oferta?>().apply {
            code = service?.code
            data = OfertaEntity.transform(service?.data)
            message = service?.message
        }
    }

    fun transformBoolean(service: ServiceDto<Boolean>): BasicDto<Boolean> {
        return BasicDto<Boolean>().apply {
            code = service.code
            data = service.data
            message = service.message
        }
    }

}
