package biz.belcorp.consultoras.domain.entity

import java.io.Serializable

class PagoOnlineConfig {

    var estadoCuenta: EstadoCuenta? = null
    var listaTipoPago: List<TipoPago>? = null
    var listaMedioPago: List<MedioPago>? = null
    var listaMetodoPago: List<MetodoPago>? = null
    var listaBanco: List<Banco>? = null

    class TipoPago {

        var tipoId: Int? = null
        var descripcion: String? = null
        var codigo: String? = null
        var estado: Boolean? = null
    }

    class Banco {

        var id: Int? = null
        var banco: String? = null
        var urlWeb: String? = null
        var urlIcono: String? = null
        var packageApp: String? = null
        var estado: Boolean? = null

    }

    class EstadoCuenta : Serializable {

        var deuda: Double? = null
        var deudaFormateada: String? = null

    }

    class MedioPago {

        var medioId: Int? = null
        var descripcion: String? = null
        var codigo: String? = null
        var rutaIcono: String? = null
        var orden: Int? = null
        var estado: Boolean? = null

    }

    class MetodoPago : Serializable {

        var medioPagoDetalleId: Int? = null
        var medioPagoId: Int? = null
        var descripcion: String? = null
        var orden: Int? = null
        var tipoVisualizacion: String? = null
        var termCondicion: String? = null
        var tipoPasarela: String? = null
        var regExpreTarjeta: String? = null
        var tipoTarjeta: String? = null
        var porcentajeGastosAdministrativos: Double = 0.0
        var pagoEnLineaGastosLabel: String? = null
        var montoMinimoPago: Double? = null
        var estado: Boolean? = null

    }

}
