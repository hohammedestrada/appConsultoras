package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class KitInicioRequest {

    @SerializedName("CampaniaID")
    var campaniaId: Int? = null

    @SerializedName("NroCampanias")
    var nroCampanias: Int? = null

    @SerializedName("ConsultoraNueva")
    var consultoraNueva: Int? = null

    @SerializedName("ConsecutivoNueva")
    var consecutivoNueva: Int? = null

    @SerializedName("DiaPROL")
    var isDiaPROL: Boolean? = null

    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion: String? = null

    @SerializedName("HoraInicioNoFacturable")
    var horaInicioNoFacturable: String? = null

    @SerializedName("HoraCierreNoFacturable")
    var horaCierreNoFacturable: String? = null

    @SerializedName("HoraInicio")
    var horaInicio: String? = null

    @SerializedName("HoraFin")
    var horaFin: String? = null

    @SerializedName("CodigoPrograma")
    var codigoPrograma: String? = null
}
