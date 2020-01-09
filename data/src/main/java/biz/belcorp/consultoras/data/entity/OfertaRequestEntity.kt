package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.OfertaRequest
import com.google.gson.annotations.SerializedName
import retrofit2.http.Query

data class OfertaRequestEntity(

    @SerializedName("Tipo") var tipo: String?,
    @SerializedName("CampaniaID") var campaniaId: String?,
    @SerializedName("ZonaId") var zonaId: Int?,
    @SerializedName("CodigoZona") var codigoZona: String?,
    @SerializedName("CodigoRegion") var codigoRegion: String?,
    @SerializedName("EsSuscrita") var esSuscrita: Boolean?,
    @SerializedName("EsActiva") var esActiva: Boolean?,
    @SerializedName("TieneMG") var tieneMG: Boolean?,
    @SerializedName("DiaInicio") var diaInicio: Int?,
    @SerializedName("FechaInicioFacturacion") var fechaInicioFacturacion: String?,
    @SerializedName("FLagComponentes") var flagComponentes: Boolean?,
    @SerializedName("FlagSubCampania") var flagSubCampania: Boolean?,
    @SerializedName("FlagIndividual") var flagIndividual: Boolean?,
    @SerializedName("FlagFestival") var flagFestival: Boolean?,
    @SerializedName("CodigoPrograma") var codigoPrograma: String?,
    @SerializedName("ConsecutivoNueva") var consecutivoNueva: Int?,
    @SerializedName("MontoMaximoPedido") var montoMaximoPedido: Double?,
    @SerializedName("ConsultoraNueva") var consultoraNueva: Int?,
    @SerializedName("NroCampanias") var nroCampanias: Int?,
    @SerializedName("NombreConsultora") var nombreConsultora: String?,
    @SerializedName("CodigoSeccion") var codigoSeccion: String?,
    @SerializedName("EsUltimoDiaFacturacion") var esUltimoDiaFacturacion: Boolean?,
    @SerializedName("PagoContado") var pagoContado: Boolean?,
    @SerializedName("FechaFinFacturacion") var fechaFinFacturacion: String?,
    /*INI ABT GAB-11*/
    @SerializedName("variantea") var variantea: Boolean?,
    @SerializedName("varianteb") var varianteb: Boolean?,
    @SerializedName("variantec") var variantec: Boolean?
    /*END ABT GAB-11*/
) {
    companion object {

        fun transform(input: OfertaRequest?): OfertaRequestEntity? {
            input?.run {
                return OfertaRequestEntity(
                    tipo,
                    campaniaId,
                    zonaId,
                    codigoZona,
                    codigoRegion,
                    esSuscrita,
                    esActiva,
                    tieneMG,
                    diaInicio,
                    fechaInicioFacturacion,
                    flagComponentes,
                    flagSubCampania,
                    flagIndividual,
                    flagFestival,
                    codigoPrograma,
                    consecutivoNueva,
                    montoMaximoPedido,
                    consultoraNueva,
                    nroCampanias,
                    nombreConsultora,
                    codigoSeccion,
                    esUltimoDiaFacturacion,
                    pagoContado,
                    fechaFinFacturacion,
                    variantea ,
                    varianteb ,
                    variantec
                )
            }
            return null
        }

    }

}
