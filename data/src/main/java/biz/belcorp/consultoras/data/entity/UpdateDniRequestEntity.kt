package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.UpdateDniRequest
import com.google.gson.annotations.SerializedName

class UpdateDniRequestEntity(
    @SerializedName("CampaniaID") var campaniaId: Int? = null,
    @SerializedName("PedidoID") var idPedido: Int?= null,
    @SerializedName("RecogerDNI") var document: String? = "",
    @SerializedName("RecogerNombre") var nameCollect: String? = "",
    @SerializedName("IPUsuario") var ipUsuario: String? = ""
) {

    companion object {
        fun transform(input: UpdateDniRequest?): UpdateDniRequestEntity? {
            input?.run {
                return UpdateDniRequestEntity(campaniaId, idPedido, document, nameCollect, ipUsuario)
            }
            return null
        }
    }
}
