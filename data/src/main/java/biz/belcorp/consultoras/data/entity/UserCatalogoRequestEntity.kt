package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class UserCatalogoRequestEntity {

    @SerializedName("campaniaActual")
    var campania: String? = null
    @SerializedName("codigoZona")
    var codigoZona: String? = null

    @SerializedName("topAnterior")
    var topLast: Int = 0
    @SerializedName("topSiguiente")
    var topNext: Int = 0
    @SerializedName("nroCampanias")
    var campaignNumber: Int? = 0
    @SerializedName("mostrarCampaniaActual")
    var isShowCurrent: Boolean = false
    @SerializedName("esBrillante")
    var isBrillante: Boolean = false


    fun getHash(): Int{
        return "$campania$codigoZona$topLast$topNext$campaignNumber$isShowCurrent$isBrillante".hashCode()
    }
}
