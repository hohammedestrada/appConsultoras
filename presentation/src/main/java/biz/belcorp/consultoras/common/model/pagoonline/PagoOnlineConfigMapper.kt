package biz.belcorp.consultoras.common.model.pagoonline

import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import javax.inject.Inject

class PagoOnlineConfigMapper @Inject internal constructor() {

    fun transform(input: PagoOnlineConfig): PagoOnlineConfigModel{
        return input.let {
            PagoOnlineConfigModel().apply {
                this.estadoCuenta = transform(it.estadoCuenta)
                this.listaBanco = transformListBanco(it.listaBanco) as List<PagoOnlineConfigModel.Banco>
                this.listaMedioPago = transformMedioPago(it.listaMedioPago) as List<PagoOnlineConfigModel.MedioPago>
                this.listaMetodoPago = transformMetodoPago(it.listaMetodoPago) as List<PagoOnlineConfigModel.MetodoPago>
                this.listaTipoPago = transformListaTipoPago(it.listaTipoPago) as List<PagoOnlineConfigModel.TipoPago>
            }
        }
    }

    private fun transformListaTipoPago(input: List<PagoOnlineConfig.TipoPago>?): Collection<PagoOnlineConfigModel.TipoPago?>? {
        return input?.let {
            it
                .map { it1 -> transformItemTipoPago(it1) }
                .filter { it1 -> null != it1 }
        }?: run {
            emptyList<PagoOnlineConfigModel.TipoPago>()
        }
    }

    private fun transformItemTipoPago(input: PagoOnlineConfig.TipoPago): PagoOnlineConfigModel.TipoPago {
        return PagoOnlineConfigModel.TipoPago().apply {
            this.codigo=input.codigo
            this.descripcion = input.descripcion
            this.estado = input.estado
            this.tipoId = input.tipoId
        }
    }

    private fun transformMetodoPago(input: List<PagoOnlineConfig.MetodoPago>?): Collection<PagoOnlineConfigModel.MetodoPago?>? {
        return input?.let {
            it
                .map { it1 -> transformItmMetodo(it1) }
                .filter { it1 -> null != it1 }
        }?: run {
            emptyList<PagoOnlineConfigModel.MetodoPago>()
        }
    }

    private fun transformMedioPago(input: List<PagoOnlineConfig.MedioPago>?): Collection<PagoOnlineConfigModel.MedioPago?>? {
        return input?.let {
            it.map { it1->transformMedio(it1) }
                .filter { it1 -> null != it1 }
        }?: run {
            emptyList<PagoOnlineConfigModel.MedioPago>()
        }
    }

    private fun transformListBanco(input: List<PagoOnlineConfig.Banco>?): Collection<PagoOnlineConfigModel.Banco?>? {
        return input?.let {
           it
               .map { it1 -> transformItmBanco(it1) }
               .filter { it1 -> null != it1 }
        }?: run {
            emptyList<PagoOnlineConfigModel.Banco>()
        }
    }

    private fun transformItmBanco(input: PagoOnlineConfig.Banco): PagoOnlineConfigModel.Banco {
        return PagoOnlineConfigModel.Banco().apply {
            this.banco = input.banco
            this.estado = input.estado
            this.id = input.id
            this.packageApp = input.packageApp
            this.urlIcono = input.urlIcono
            this.urlWeb = input.urlWeb
        }
    }

    private fun transform(input: PagoOnlineConfig.EstadoCuenta?): PagoOnlineConfigModel.EstadoCuenta {
        return PagoOnlineConfigModel.EstadoCuenta().apply {
            this.deuda = input!!.deuda
            this.deudaFormateada = input.deudaFormateada
        }
    }

    private fun transformMedio(input: PagoOnlineConfig.MedioPago): PagoOnlineConfigModel.MedioPago {
        return PagoOnlineConfigModel.MedioPago().apply {
            this.codigo = input.codigo
            this.descripcion = input.descripcion
            this.estado = input.estado
            this.medioId = input.medioId
            this.orden = input.orden
            this.rutaIcono = input.rutaIcono
        }
    }

    private fun transformItmMetodo(input: PagoOnlineConfig.MetodoPago): PagoOnlineConfigModel.MetodoPago {
        return PagoOnlineConfigModel.MetodoPago().apply {

            this.medioPagoDetalleId = input.medioPagoDetalleId

            this.medioPagoId = input.medioPagoId

            this.descripcion = input.descripcion

            this.orden = input.orden

            this.tipoVisualizacion = input.tipoVisualizacion

            this.termCondicion = input.termCondicion

            this.tipoPasarela = input.tipoPasarela

            this.regExpreTarjeta = input.regExpreTarjeta

            this.tipoTarjeta = input.tipoTarjeta

            input.porcentajeGastosAdministrativos.let {
                porcentajeGastosAdministrativos = if (it == null) 0.0 else it
            }

            this.pagoEnLineaGastosLabel = input.pagoEnLineaGastosLabel

            this.montoMinimoPago = input.montoMinimoPago

            this.estado = input.estado

        }
    }


    fun createTipoPago(url: String,estadoCuenta: PagoOnlineConfigModel.EstadoCuenta,tarjeta:PagoOnlineConfigModel.MetodoPago,tipoPago: ArrayList<PagoOnlineConfigModel.TipoPago>):TipoPagoModel{

        return TipoPagoModel().apply {
             this.urlIcono = url
             this.estadoCuenta = estadoCuenta
             this.tarjeta = tarjeta
             this.tipoPago = tipoPago
         }
    }
}
