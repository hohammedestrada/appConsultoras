package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class AppEntity {

    @SerializedName("AplicacionNombre")
    var aplicacion: String? = null

    @SerializedName("PaisISO")
    var pais: String? = null

    @SerializedName("SistemaOperativo")
    var so: String? = null

    @SerializedName("Version")
    var version: String? = null

    @SerializedName("MinimaVersion")
    var minimaVersion: String? = null

    @SerializedName("FechaLanzamiento")
    var fechaLanzamiento: String? = null

    @SerializedName("FechaActualizacion")
    var fechaActualizacion: String? = null

    @SerializedName("RequiereActualizacion")
    var isRequiereActualizacion: Boolean = false

    @SerializedName("TipoDescarga")
    var tipoDescarga: Int = 0

    @SerializedName("Url")
    var url: String? = null
}
