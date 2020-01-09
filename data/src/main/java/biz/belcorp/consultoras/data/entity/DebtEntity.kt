package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.math.BigDecimal

/**
 *
 */
class DebtEntity {

    @SerializedName("Monto")
    @Expose
    var monto: BigDecimal? = null
    @SerializedName("Descripcion")
    @Expose
    var descripcion: String? = null
    @SerializedName("Nota")
    @Expose
    var nota: String? = null
    @SerializedName("Fecha")
    @Expose
    var fecha: String? = null
    @SerializedName("TipoMovimiento")
    @Expose
    var tipoMovimiento: String? = null
    @SerializedName("CodigoCliente")
    @Expose
    var clienteID: Int? = null
    @Expose(serialize = false, deserialize = false)
    var clienteLocalID: Int? = null
    @SerializedName("CodigoCampania")
    @Expose
    var codigoCampania: String? = null
}
