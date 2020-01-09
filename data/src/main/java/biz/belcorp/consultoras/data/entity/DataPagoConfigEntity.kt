package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataPagoConfigEntity : Serializable {

    @SerializedName("EstadoCuenta")
    var estadoCuenta: EstadoCuentaEntity? = null
    @SerializedName("ListaTipoPago")
    var listaTipoPago: List<TipoPagoEntity>? = null
    @SerializedName("ListaMedioPago")
    var listaMedioPago: List<MedioPagoEntity>? = null
    @SerializedName("ListaMetodoPago")
    var listaMetodoPago: List<MetodoPagoEntity>? = null
    @SerializedName("ListaBanco")
    var listaBanco: List<BancoEntity>? = null

    class EstadoCuentaEntity {

        @SerializedName("Deuda")
        var deuda: Double? = null
        @SerializedName("DeudaFormatted")
        var deudaFormateada: String? = null

    }

    class TipoPagoEntity {

        @SerializedName("PagoEnLineaTipoPagoId")
        var tipoId: Int? = null
        @SerializedName("Descripcion")
        var descripcion: String? = null
        @SerializedName("Codigo")
        var codigo: String? = null
        @SerializedName("Estado")
        var estado: Boolean? = null

    }

    class MedioPagoEntity {

        @SerializedName("PagoEnLineaMedioPagoId")
        var medioId: Int? = null
        @SerializedName("Descripcion")
        var descripcion: String? = null
        @SerializedName("Codigo")
        var codigo: String? = null
        @SerializedName("RutaIcono")
        var rutaIcono: String? = null
        @SerializedName("Orden")
        var orden: Int? = null
        @SerializedName("Estado")
        var estado: Boolean? = null

    }

    class MetodoPagoEntity {

        @SerializedName("PagoEnLineaMedioPagoDetalleId")
        var medioPagoDetalleId: Int? = null
        @SerializedName("PagoEnLineaMedioPagoId")
        var medioPagoId: Int? = null
        @SerializedName("Descripcion")
        var descripcion: String? = null
        @SerializedName("Orden")
        var orden: Int? = null
        @SerializedName("TipoVisualizacionTyC")
        var tipoVisualizacion: String? = null
        @SerializedName("TerminosCondiciones")
        var termCondicion: String? = null
        @SerializedName("TipoPasarelaCodigoPlataforma")
        var tipoPasarela: String? = null
        @SerializedName("ExpresionRegularTarjeta")
        var regExpreTarjeta: String? = null
        @SerializedName("TipoTarjeta")
        var tipoTarjeta: String? = null
        @SerializedName("PorcentajeGastosAdministrativos")
        var porcentajeGastosAdministrativos: Double? = null
        @SerializedName("PagoEnLineaGastosLabel")
        var pagoEnLineaGastosLabel: String? = null
        @SerializedName("MontoMinimoPago")
        var montoMinimoPago: Double? = null
        @SerializedName("Estado")
        var estado: Boolean? = null

    }

    class BancoEntity {

        @SerializedName("Id")
        var id: Int? = null
        @SerializedName("Banco")
        var banco: String? = null
        @SerializedName("URlPaginaWeb")
        var urlWeb: String? = null
        @SerializedName("URLIcono")
        var urlIcono: String? = null
        @SerializedName("URIExternalApp")
        var packageApp: String? = null
        @SerializedName("Estado")
        var estado: Boolean? = null

    }
}
