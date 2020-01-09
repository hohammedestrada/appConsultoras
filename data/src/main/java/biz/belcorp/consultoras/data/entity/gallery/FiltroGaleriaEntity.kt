package biz.belcorp.consultoras.data.entity.gallery

import com.google.gson.annotations.SerializedName

class FiltroGaleriaEntity {
    @SerializedName("Codigo")
    var Codigo : String = ""

    @SerializedName("Descripcion")
    var Descripcion : String = ""

    @SerializedName("Tipo")
    var Tipo : Int? = -1

    @SerializedName("Orden")
    var Orden : Int = -2

    @SerializedName("Activo")
    var Activo : Boolean = false

    @SerializedName("IdPadre")
    var IdPadre : Int = 0

    @SerializedName("CodigoPadre")
    var CodigoPadre : String = ""

    @SerializedName("OrdenPadre")
    var OrdenPadre : Int = 0

    @SerializedName("Otros")
    var Otros : String = ""

    @SerializedName("OtrosAdd")
    var OtrosAdd : String = ""

    @SerializedName("EsSeccion")
    var EsSeccion : Boolean = false

    @SerializedName("EsExcluyente")
    var EsExcluyente : Boolean = false

}
