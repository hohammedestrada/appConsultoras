package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.regex.Pattern
import javax.inject.Inject

/**
 *
 */
class OrderEntityDataMapper @Inject
constructor(private val productoDetalleEntityDataMapper: ProductoDetalleEntityDataMapper,
            val productCuvEntityDataMapper: ProductCuvEntityDataMapper)// EMPTY
{

    fun transform(input: OrderEntity?): Order? {
        return input?.let {
            Order().apply {
                aceptacionConsultoraDA = it.aceptacionConsultoraDA
                campania = it.campania
                cantidad = it.cantidad
                cuv = it.cuv
                origenPedidoWeb = it.origenPedidoWeb
            }
        }
    }

    fun transform(input: Order?): OrderEntity? {
        return input?.let {
            OrderEntity().apply {
                aceptacionConsultoraDA = it.aceptacionConsultoraDA
                campania = it.campania
                cantidad = it.cantidad
                cuv = it.cuv
                origenPedidoWeb = it.origenPedidoWeb
            }
        }
    }

    fun transformSearchCUV(input: ServiceDto<ProductCuvEntity>?): BasicDto<ProductCUV>? {
        return input?.let {
            BasicDto<ProductCUV>().apply {
                code = it.code
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<ProductCuvEntity>() {}.type
                try {
                    val epe: ProductCuvEntity = Gson().fromJson(json, type)
                    data = productCuvEntityDataMapper.transform(epe)
                } catch (e: Exception) {
                    BelcorpLogger.d("Reserva pedido: Data de respuesta de pedido nula")
                }
                message = it.message
            }
        }
    }

    fun transformSuggestedReplacement(input: List<ProductCuvEntity?>?): List<ProductCUV?>? {
        val output = java.util.ArrayList<ProductCUV>()

        if (null == input) {
            return emptyList()
        }

        input.forEach {
            val model = productCuvEntityDataMapper.transform(it)
            model?.let { it1 -> output.add(it1) }

        }
        return output
    }

    fun transformRelatedOffers(input: RelatedOfferResponseEntity?): RelatedOfferResponse? {
        return RelatedOfferResponse().apply {
            total = input?.total
            productos = transformSuggestedReplacement(input?.productos)
        }
    }


    fun transform(input: ObservacionPedidoEntity?): ObservacionPedido? {
        return input?.let {
            ObservacionPedido().apply {
                cuv = it.cuv
                descripcion = it.descripcion
                caso = it.caso
            }
        }
    }

    fun transform(input: MyOrderEntity?): MyOrder? {
        return input?.let {
            MyOrder().apply {
                id = it.id
                estadoPedidoDesc = it.estadoPedidoDesc
                campaniaID = it.campaniaID
                fechaRegistro = it.fechaRegistro
                importeTotal = it.importeTotal
                rutaPaqueteDocumentario = it.rutaPaqueteDocumentario
                numeroPedido = it.numeroPedido
                estadoEncuesta = it.estadoEncuesta
            }
        }
    }

    fun transform(input: MyOrder?): MyOrderEntity? {
        return input?.let {
            MyOrderEntity().apply {
                id = it.id
                estadoPedidoDesc = it.estadoPedidoDesc
                campaniaID = it.campaniaID
                fechaRegistro = it.fechaRegistro
                importeTotal = it.importeTotal
                rutaPaqueteDocumentario = it.rutaPaqueteDocumentario
                numeroPedido = it.numeroPedido
                estadoEncuesta = it.estadoEncuesta
            }
        }
    }

    fun transform(input: GananciaListItemEntity?): GananciaListItem? {
        return input?.let {
            GananciaListItem().apply {
                descripcion = it.descripcion
                montoGanancia = it.montoGanancia
            }
        }
    }

    fun transform(input: OrderListItemEntity?): OrderListItem? {
        return input?.let {
            OrderListItem().apply {
                id = it.id
                cuv = it.cuv
                descripcionProd = it.descripcionProd
                descripcionCortaProd = it.descripcionCortaProd
                cantidad = it.cantidad
                precioUnidad = it.precioUnidad
                importeTotal = it.importeTotal
                clienteID = it.clienteID
                clienteLocalID = it.clienteLocalID
                nombreCliente = it.nombreCliente
                isEsKitNueva = it.isEsKitNueva
                tipoEstrategiaID = it.tipoEstrategiaID
                tipoOfertaSisID = it.tipoOfertaSisID

                it.observacionPROL?.let { text ->
                    if (text.contains("|")) {
                        observacionPROLType = 1
                        var obs = text
                        if (text.endsWith("|") && text.length > 1) obs = text.substring(0, text.length - 1)
                        val obsList = obs.split(Pattern.quote("|").toRegex())
                            .dropLastWhile { it1 -> it1.isEmpty() }.toTypedArray()

                        obsList.let { list ->
                            observacionPROLList = list.toCollection(ArrayList())
                        }
                    } else {
                        observacionPROL = text
                    }
                }

                it.observacionPromociones?.forEach {
                    observacionPROLType = 1
                    it?.let {
                        observacionPromociones?.add(it)
                    }
                }

                etiquetaProducto = it.etiquetaProducto
                indicadorOfertaCUV = it.indicadorOfertaCUV
                isEsBackOrder = it.isEsBackOrder
                isAceptoBackOrder = it.isAceptoBackOrder
                isFlagNueva = it.isFlagNueva
                isEnRangoProgNuevas = it.isEnRangoProgNuevas
                isEsDuoPerfecto = it.isEsDuoPerfecto
                isEsPremioElectivo = it.isEsPremioElectivo
                setID = it.getSetID()
                tipoOferta = it.tipoOferta

                isKitCaminoBrillante = it.isKitCaminoBrillante
                isArmaTuPack = it.isArmaTuPack
                isFestival = it.isFestival
                flagFestival = it.flagFestival

                isPromocion = it.isPromocion
                componentes = transformToPedidoComponents(it.componentes)
                isDeleteKit = it.isDeleteKit

            }
        }
    }

    private fun transformToPedidoComponents(input: List<PedidoComponentesEntity?>?): List<PedidoComponentes>? {
        return ArrayList<PedidoComponentes>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }

    fun transform(input: PedidoComponentesEntity?): PedidoComponentes? {
        return input?.let {
            PedidoComponentes().apply {
                setDetalleId = it.setDetalleId
                setId = it.setId
                cuv = it.cuv
                nombreProducto = it.nombreProducto
                cantidad = it.cantidad
                factorRepetecion = it.factorRepetecion
                precioUnidad = it.precioUnidad
            }
        }
    }


    fun transform(input: EscalaDescuentoEntity?): EscalaDescuento? {
        return input?.let {
            EscalaDescuento().apply {
                montoDesde = it.montoDesde
                montoHasta = it.montoHasta
                porDescuento = it.porDescuento
                tipoParametriaOfertaFinal = it.tipoParametriaOfertaFinal
                precioMinimo = it.precioMinimo
                algoritmo = it.algoritmo
            }
        }
    }

    fun transform(input: MensajeMetaEntity?): MensajeMeta? {
        return input?.let {
            MensajeMeta().apply {
                tipoMensaje = it.tipoMensaje
                titulo = it.titulo
                mensaje = it.mensaje
            }
        }
    }

    fun transform(input: OrderListItem?): OrderListItemEntity? {
        return input?.let {
            OrderListItemEntity().apply {
                id = it.id
                cuv = it.cuv
                descripcionProd = it.descripcionProd
                descripcionCortaProd = it.descripcionCortaProd
                cantidad = it.cantidad
                precioUnidad = it.precioUnidad
                importeTotal = it.importeTotal
                clienteID = it.clienteID
                clienteLocalID = it.clienteLocalID
                nombreCliente = it.nombreCliente
                isEsKitNueva = it.isEsKitNueva
                tipoEstrategiaID = it.tipoEstrategiaID
                tipoOfertaSisID = it.tipoOfertaSisID
                observacionPROL = it.observacionPROL
                etiquetaProducto = it.etiquetaProducto
                indicadorOfertaCUV = it.indicadorOfertaCUV
                isEsBackOrder = it.isEsBackOrder
                isAceptoBackOrder = it.isAceptoBackOrder
                isFlagNueva = it.isFlagNueva
                isEnRangoProgNuevas = it.isEnRangoProgNuevas
                isEsDuoPerfecto = it.isEsDuoPerfecto
                isEsPremioElectivo = it.isEsPremioElectivo
                isArmaTuPack = it.isArmaTuPack
                tipoOferta = it.tipoOferta
                setSetID(it.setID)
                isDeleteKit = it.isDeleteKit

            }
        }
    }

    fun transform(input: EstrategiaCarrusel?): EstrategiaCarruselEntity? {
        return input?.let {
            EstrategiaCarruselEntity().apply {
                estrategiaID = it.estrategiaID
                codigoEstrategia = it.codigoEstrategia
                cuv = it.cuv
                descripcionCUV = it.descripcionCUV
                descripcionCortaCUV = it.descripcionCortaCUV
                marcaID = it.marcaID
                descripcionMarca = it.descripcionMarca
                precioValorizado = it.precioValorizado
                precioFinal = it.precioFinal
                ganancia = it.ganancia
                fotoProductoSmall = it.fotoProductoSmall
                fotoProductoMedium = it.fotoProductoMedium
                flagNueva = it.flagNueva
                tipoEstrategiaID = it.tipoEstrategiaID
                indicadorMontoMinimo = it.indicadorMontoMinimo
                origenPedidoWebFicha = it.origenPedidoWebFicha
                tieneStock = it.tieneStock
                flagSeleccionado = it.flagSeleccionado
                flagFestival = it.flagFestival
                flagPremioDefault = it.flagPremioDefault
                origenPedidoWeb = it.origenPedidoWeb
                productoDetalle = productoDetalleEntityDataMapper.transform(it.productoDetalle) as List<ProductoDetalleEntity>?
                tipoPersonalizacion = it.tipoPersonalizacion
            }
        }
    }

    fun transform(input: List<EstrategiaCarruselEntity?>?): List<EstrategiaCarrusel?>? {
        val output = java.util.ArrayList<EstrategiaCarrusel>()

        if (null == input) {
            return emptyList()
        }

        input.forEach {
            val model = transform(it)
            model?.let { it1 -> output.add(it1) }

        }
        return output
    }

    fun transform(input: EstrategiaCarruselEntity?): EstrategiaCarrusel? {
        return input?.let {
            EstrategiaCarrusel().apply {
                estrategiaID = it.estrategiaID
                codigoEstrategia = it.codigoEstrategia
                cuv = it.cuv
                descripcionCUV = it.descripcionCUV
                descripcionCortaCUV = it.descripcionCortaCUV
                marcaID = it.marcaID
                descripcionMarca = it.descripcionMarca
                precioValorizado = it.precioValorizado
                precioFinal = it.precioFinal
                ganancia = it.ganancia
                fotoProductoSmall = it.fotoProductoSmall
                fotoProductoMedium = it.fotoProductoMedium
                flagNueva = it.flagNueva
                tipoEstrategiaID = it.tipoEstrategiaID
                indicadorMontoMinimo = it.indicadorMontoMinimo
                origenPedidoWebFicha = it.origenPedidoWebFicha
                origenPedidoWeb = it.origenPedidoWeb
                tieneStock = it.tieneStock
                flagSeleccionado = it.flagSeleccionado
                flagFestival = it.flagFestival
                productoDetalle = productoDetalleEntityDataMapper.transform(it.productoDetalle) as List<ProductoDetalle>?
                tipoPersonalizacion = it.tipoPersonalizacion
            }
        }
    }

    fun transform(input: PedidoConfigEntity?): PedidoConfig? {
        return input?.let {
            PedidoConfig().apply {
                escalaDescuento = transformToEscalaDescuentoListItem(it.escalaDescuentoEntity)
                mensajeMeta = transformToMensajeMetaListItem(it.mensajeMetaEntity)
            }
        }
    }

    fun transform(input: PedidoGetResponseEntity?): PedidoGetResponse? {
        return input?.let {
            PedidoGetResponse().apply {
                cantidadCuv = it.cantidadCuv
                cantidadProductos = it.cantidadProductos
                descuentoProl = it.descuentoProl
                detalle = transformToOrderListItem(it.detalle)
                gananciaEstimada = it.gananciaEstimada
                importeTotal = it.importeTotal
                importeTotalDescuento = it.importeTotalDescuento
                montoAhorroCatalogo = it.montoAhorroCatalogo
                montoAhorroRevista = it.montoAhorroRevista
                montoEscala = it.montoEscala
                pedidoID = it.pedidoID
                tippingPoint = it.tippingPoint
                muestraRegalo = it.muestraRegalo ?: false
                pedidoValidado = it.pedidoValidado ?: false
                precioPorNivel = it.precioPorNivel ?: false
                isTieneArmaTuPack = it.isTieneArmaTuPack
                recogerDNI = it.recogerDNI
                recogerNombre = it.recogerNombre
                facturarPedidoFM = it.facturarPedidoFM ?: false
                activaMultiPedido = it.activaMultiPedido ?: false
            }
        }
    }

    fun transformConditions(input: List<ConditionsGetResponseEntity?>?): List<ConditionsGetResponse?>? {

        var items = ArrayList<ConditionsGetResponse>()
        input?.forEach {
            items.add(
                ConditionsGetResponse().apply {
                    this.cuvCondition = it?.cuvCondition
                    this.cuvPromotion = it?.cuvPromotion
                    this.imageURLCondition = it?.imageURLCondition
                    this.imageURLPromotion = it?.imageURLPromotion
                    this.descriptionCondition = it?.descriptionCondition
                    this.descriptionPromotion = it?.descriptionPromotion
                    this.messagePromotion = it?.messagePromotion
                    this.typeCondition = it?.typeCondition
                    this.typePromotion = it?.typePromotion
                    this.priceCondition = it?.priceCondition
                    this.pricePromotion = it?.pricePromotion
                }
            )
        }

        return items
    }

    fun transformFormatted(input: PedidoGetResponseEntity?): FormattedOrder? {
        return input?.let {
            FormattedOrder().apply {
                cantidadCuv = it.cantidadCuv
                cantidadProductos = it.cantidadProductos
                descuentoProl = it.descuentoProl
                productosDetalle = transformToOrderListItem(it.detalle)
                gananciaEstimada = it.gananciaEstimada
                importeTotal = it.importeTotal
                importeTotalDescuento = it.importeTotalDescuento
                montoAhorroCatalogo = it.montoAhorroCatalogo
                montoAhorroRevista = it.montoAhorroRevista
                montoEscala = it.montoEscala
                pedidoID = it.pedidoID
                tippingPoint = it.tippingPoint
                muestraRegalo = it.muestraRegalo ?: false
                pedidoValidado = it.pedidoValidado ?: false
                precioPorNivel = it.precioPorNivel ?: false
                recogerDNI = it.recogerDNI
                recogerNombre = it.recogerNombre
                montoPagoContadoSIC = it.montoPagoContadoSIC
                montoDeudaAnteriorSIC = it.montoDeudaAnteriorSIC
                montoDescuentoSIC = it.montoDescuentoSIC
                montoFleteSIC = it.montoFleteSIC
                precioRegalo = it.precioRegalo
                facturarPedidoFM = it.facturarPedidoFM ?: false
                activaMultiPedido = it.activaMultiPedido ?: false
                gananaciaDetalle = transformToGananciaListItem(it.detalleGanancia)
            }
        }
    }

    fun transform(input: List<MyOrderEntity?>?): Collection<MyOrder?>? {
        val output = ArrayList<MyOrder>()

        if (null == input) {
            return emptyList()
        }

        for (entity in input) {
            val model = transform(entity)
            if (null != model) {
                output.add(model)
            }
        }

        return output
    }


    private fun transformToGananciaListItem(input: List<GananciaListItemEntity?>?): List<GananciaListItem?>? {
        return ArrayList<GananciaListItem>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }

    private fun transformToOrderListItem(input: List<OrderListItemEntity?>?): List<OrderListItem?>? {
        return ArrayList<OrderListItem>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }

    private fun transformToEscalaDescuentoListItem(input: List<EscalaDescuentoEntity?>?): List<EscalaDescuento?>? {
        return ArrayList<EscalaDescuento>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }

    private fun transformToMensajeMetaListItem(input: List<MensajeMetaEntity?>?): List<MensajeMeta?>? {
        return ArrayList<MensajeMeta>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }

    fun transform(input: ProductoMasivo?): ProductoMasivoEntity? {
        return input?.let {
            ProductoMasivoEntity().apply {
                id = it.id
                cuv = it.cuv
                descripcion = it.descripcion
                cantidad = it.cantidad
                marcaId = it.marcaId
                marcaDescripcion = it.marcaDescripcion
                clienteId = it.clienteId
                clienteDescripcion = it.clienteDescripcion
                precioCatalogo = it.precioCatalogo
                tono = it.tono
                urlImagen = it.urlImagen
                codigoRespuesta = it.codigoRespuesta
                mensajeRespuesta = it.mensajeRespuesta
                idOrder = it.idOrder
            }
        }
    }

    fun transform(input: ProductoMasivoEntity?): ProductoMasivo? {
        return input?.let {
            ProductoMasivo().apply {
                id = it.id
                cuv = it.cuv
                descripcion = it.descripcion
                cantidad = it.cantidad
                marcaId = it.cantidad
                marcaDescripcion = it.marcaDescripcion
                clienteId = it.clienteId
                clienteDescripcion = it.clienteDescripcion
                precioCatalogo = it.precioCatalogo
                tono = it.tono
                urlImagen = it.urlImagen
                codigoRespuesta = it.codigoRespuesta
                mensajeRespuesta = it.mensajeRespuesta
                idOrder = it.idOrder
            }
        }
    }

    fun transformToProductoMasivoList(input: List<ProductoMasivoEntity?>?): List<ProductoMasivo>? {
        return ArrayList<ProductoMasivo>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }

    fun transformToProductoMasivoEntityList(input: List<ProductoMasivo?>?): List<ProductoMasivoEntity?>? {
        return ArrayList<ProductoMasivoEntity>().apply {
            input?.filterNotNull()?.forEach {
                transform(it)?.let { it1 -> this.add(it1) }
            }
        }
    }

    fun transform(data: MensajeProlEntity?): MensajeProl? {
        return MensajeProl().apply {
            this.code = data?.code
            this.message = data?.message
        }
    }

    fun transformBy(input: List<MensajeProlEntity?>?): Collection<MensajeProl?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
                emptyList<MensajeProl>()
            }
    }

    fun transform(input: PedidoPendienteEntity?): PedidoPendiente? {
        return input?.let {
            PedidoPendiente().apply {
                pedidoPendiente = it.pedidoPendiente
            }
        }
    }

    fun transform(input: PedidoPendiente?): PedidoPendienteEntity? {
        return input?.let {
            PedidoPendienteEntity().apply {
                pedidoPendiente = it.pedidoPendiente
            }
        }
    }

    companion object {

        /**
         * Transform para el detalle de pedido en la ficha y oferta de kit de nuevas (HV)
         */

        fun transformFormatted(input: PedidoGetResponseEntity?): FormattedOrder? {
            return input?.let {
                FormattedOrder().apply {
                    cantidadCuv = it.cantidadCuv
                    cantidadProductos = it.cantidadProductos
                    descuentoProl = it.descuentoProl
                    productosDetalle = transformOrderListItem(it.detalle)
                    gananciaEstimada = it.gananciaEstimada
                    importeTotal = it.importeTotal
                    importeTotalDescuento = it.importeTotalDescuento
                    montoAhorroCatalogo = it.montoAhorroCatalogo
                    montoAhorroRevista = it.montoAhorroRevista
                    montoEscala = it.montoEscala
                    pedidoID = it.pedidoID
                    tippingPoint = it.tippingPoint
                    muestraRegalo = it.muestraRegalo ?: false
                    pedidoValidado = it.pedidoValidado ?: false
                    precioPorNivel = it.precioPorNivel ?: false
                    recogerDNI = it.recogerDNI
                    recogerNombre = it.recogerNombre
                    montoPagoContadoSIC = it.montoPagoContadoSIC
                    montoDeudaAnteriorSIC = it.montoDeudaAnteriorSIC
                    montoDescuentoSIC = it.montoDescuentoSIC
                    montoFleteSIC = it.montoFleteSIC
                    precioRegalo = it.precioRegalo
                    facturarPedidoFM = it.facturarPedidoFM ?: false
                    activaMultiPedido = it.activaMultiPedido ?: false
                }
            }
        }

        private fun transformOrderListItem(input: List<OrderListItemEntity?>?): List<OrderListItem?>? {
            return ArrayList<OrderListItem>().apply {
                input?.filterNotNull()?.forEach {
                    transformListItems(it)?.let { it1 -> this.add(it1) }
                }
            }
        }

        private fun transformListItems(input: OrderListItemEntity?): OrderListItem? {
            return input?.let {
                OrderListItem().apply {
                    id = it.id
                    cuv = it.cuv
                    descripcionProd = it.descripcionProd
                    descripcionCortaProd = it.descripcionCortaProd
                    cantidad = it.cantidad
                    precioUnidad = it.precioUnidad
                    importeTotal = it.importeTotal
                    clienteID = it.clienteID
                    clienteLocalID = it.clienteLocalID
                    nombreCliente = it.nombreCliente
                    isEsKitNueva = it.isEsKitNueva
                    tipoEstrategiaID = it.tipoEstrategiaID
                    tipoOfertaSisID = it.tipoOfertaSisID

                    it.observacionPROL?.let { text ->
                        if (text.contains("|")) {
                            observacionPROLType = 1
                            var obs = text
                            if (text.endsWith("|") && text.length > 1) obs = text.substring(0, text.length - 1)
                            val obsList = obs.split(Pattern.quote("|").toRegex())
                                .dropLastWhile { it1 -> it1.isEmpty() }.toTypedArray()

                            obsList.let { list ->
                                observacionPROLList = list.toCollection(ArrayList())
                            }
                        } else {
                            observacionPROL = text
                        }
                    }

                    etiquetaProducto = it.etiquetaProducto
                    indicadorOfertaCUV = it.indicadorOfertaCUV
                    isEsBackOrder = it.isEsBackOrder
                    isAceptoBackOrder = it.isAceptoBackOrder
                    isFlagNueva = it.isFlagNueva
                    isEnRangoProgNuevas = it.isEnRangoProgNuevas
                    isEsDuoPerfecto = it.isEsDuoPerfecto
                    isEsPremioElectivo = it.isEsPremioElectivo
                    setID = it.getSetID()
                    tipoOferta = it.tipoOferta

                    isKitCaminoBrillante = it.isKitCaminoBrillante
                    isArmaTuPack = it.isArmaTuPack
                    isFestival = it.isFestival
                    flagFestival = it.flagFestival

                    isDeleteKit = it.isDeleteKit

                }
            }
        }

    }

}
