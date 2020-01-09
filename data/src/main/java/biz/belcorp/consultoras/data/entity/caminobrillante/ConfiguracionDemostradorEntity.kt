package biz.belcorp.consultoras.data.entity.caminobrillante

import biz.belcorp.consultoras.data.entity.GroupFilterEntity
import com.google.gson.annotations.SerializedName

class ConfiguracionDemostradorEntity {

    @SerializedName("Ordenamientos")
    var ordenamientos: List<GroupFilterEntity>? = null

    @SerializedName("Filtros")
    var filtros: List<GroupFilterEntity>? = null

}
