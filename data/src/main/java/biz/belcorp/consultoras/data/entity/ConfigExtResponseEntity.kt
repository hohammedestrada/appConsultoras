package biz.belcorp.consultoras.data.entity


import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Clase de configuracion
 */

class ConfigExtResponseEntity {

    @SerializedName("ubicacion")
    var listaUbicacion: List<UbicacionOrigenMarcacionEntity?>? = null

    @SerializedName("seccion")
    var listaSeccion: List<UbicacionOrigenMarcacionEntity?>? = null

    @SerializedName("lista")
    var listaListas: List<String?>? = null

    @SerializedName("palanca")
    var listaPalancas: List<UbicacionOrigenMarcacionEntity?>? = null

    @SerializedName("subseccion")
    var listaSubseccion: List<UbicacionOrigenMarcacionEntity?>? = null


    init {
        this.listaUbicacion = ArrayList()
        this.listaSeccion = ArrayList()
        this.listaListas = ArrayList()
        this.listaPalancas = ArrayList()
        this.listaSubseccion = ArrayList()
    }

}
