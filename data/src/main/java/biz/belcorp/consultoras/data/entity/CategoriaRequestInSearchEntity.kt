package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.CategoriaRequestInSearch
import com.google.gson.annotations.SerializedName

data class CategoriaRequestInSearchEntity(
    @SerializedName("CampaniaID") var campaniaId: String?,
    @SerializedName("CodigoConsultora") var codigoConsultora: String?,
    @SerializedName("CodigoZona") var codigoZona: String?,
    @SerializedName("Perzonalizaciones") var perzonalizaciones: String?,
    @SerializedName("Lider") var lider: Int?,
    @SerializedName("RDEsSuscrita") var rdesSuscrita: Boolean?,
    @SerializedName("RDEsActiva") var rdesActiva: Boolean?,
    @SerializedName("RDActivoMdo") var rdActivoModo: Boolean?,
    @SerializedName("RDTieneRDC") var rdTieneRdc: Boolean?,
    @SerializedName("RDTieneRDI") var rdTieneRdi: Boolean?,
    @SerializedName("RDTieneRDCR") var rdTieneRdcr: Boolean?,
    @SerializedName("DiaFacturacion") var diaFacturacion: Int?) {

    companion object {

        fun transform(input: CategoriaRequestInSearch?): CategoriaRequestInSearchEntity?{
            input?.run {
                return CategoriaRequestInSearchEntity(
                    campaniaId,
                    codigoConsultora,
                    codigoZona,
                    perzonalizaciones,
                    lider,
                    rdesSuscrita,
                    rdesActiva,
                    rdActivoModo,
                    rdTieneRdc,
                    rdTieneRdi,
                    rdTieneRdcr,
                    diaFacturacion
                )
            }
            return null
        }

    }
}
