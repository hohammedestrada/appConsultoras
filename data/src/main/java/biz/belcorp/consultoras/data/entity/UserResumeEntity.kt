package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.entity.caminobrillante.CaminoBrillanteEntity
import com.google.gson.annotations.SerializedName

class UserResumeEntity {

    @SerializedName("Incentivos")
    var incentivos: UserIncentivesResumeEntity? = null
    @SerializedName("Deudas")
    var deudas: UserDebtsResumeEntity? = null
    @SerializedName("Pedidos")
    var pedidos: UserOrdersResumeEntity? = null
    @SerializedName("Clientes")
    var clientes: UserClientsResumeEntity? = null
    @SerializedName("Ganancias")
    var ganancias: UserGananciasResumeEntity? = null
    @SerializedName("EstadoCuenta")
    var estadoCuenta: UserEstadoCuentaResumeEntity? = null
    @SerializedName("ConfiguracionPais")
    var config: List<UserConfigResumeEntity>? = null
    @SerializedName("CaminoBrillante")
    var caminobrillante: CaminoBrillanteEntity? = null

}
