package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.structure.BaseModel
import java.io.Serializable

class ObservacionPedidoEntity : BaseModel(), Serializable {

    @SerializedName("CUV")
    var cuv: String? = null

    @SerializedName("Descripcion")
    var descripcion: String? = null

    @SerializedName("Caso")
    var caso: Int? = null

    @SerializedName("CuvObs")
    var cuvObs: String? = null

    @SerializedName("SetID")
    var conjuntoID: Int? = null

    @SerializedName("PedidoDetalleID")
    var pedidoDetalleID: Int? = null
}
