package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class UpdateMultipedidoStateEntity(
    @SerializedName("PedidoID") var pedidoId: Int,
    @SerializedName("FacturaFM") var state: Int? = 0
)
