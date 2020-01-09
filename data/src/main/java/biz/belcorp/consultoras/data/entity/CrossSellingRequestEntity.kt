package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.CrossSellingConfiguracion
import biz.belcorp.consultoras.domain.entity.CrossSellingRequest
import com.google.gson.annotations.SerializedName


class CrossSellingRequestEntity {

    @SerializedName("CampaniaId")
    var campaniaId: Int? = null
    @SerializedName("CodigoProducto")
    var codigoProducto: List<String?> = mutableListOf()
    @SerializedName("PrecioCatalogo")
    var precioCatalogo: Double? = null
    @SerializedName("CodigoZona")
    var codigoZona: String? = ""
    @SerializedName("PersonalizacionesDummy")
    var personalizacionesDummy: String = ""
    @SerializedName("CUV")
    var cuv: String = ""
    @SerializedName("FechaInicioFacturacion")
    var fechaInicioFacturacion: String = ""
    @SerializedName("Configuracion")
    var configuracion: CrossSellingConfiguracionEntity? = null

    companion object {

        fun transform(input: CrossSellingRequest?): CrossSellingRequestEntity? {
            input?.run {
                val newCrossSellingRQEntity = CrossSellingRequestEntity()
                newCrossSellingRQEntity.campaniaId = campaniaId
                newCrossSellingRQEntity.codigoProducto = codigoProducto
                newCrossSellingRQEntity.precioCatalogo = precioCatalogo
                newCrossSellingRQEntity.codigoZona = codigoZona
                newCrossSellingRQEntity.personalizacionesDummy = personalizacionesDummy
                newCrossSellingRQEntity.cuv = cuv
                newCrossSellingRQEntity.fechaInicioFacturacion = fechaInicioFacturacion
                newCrossSellingRQEntity.configuracion = CrossSellingConfiguracionEntity.transform(configuracion)
                return newCrossSellingRQEntity
            }
            return null
        }

    }

}

class CrossSellingConfiguracionEntity{

    @SerializedName("Lider")
    var lider: Int? = null
    @SerializedName("RDEsActiva")
    var rdEsActiva: Boolean? = null
    @SerializedName("RDActivoMdo")
    var rdActivoMdo: Boolean? = null
    @SerializedName("RDTieneRDC")
    var rdTieneRDC: Boolean? = null
    @SerializedName("RDTieneRDI")
    var rdTieneRDI: Boolean? = null
    @SerializedName("RDTieneRDCR")
    var rdTieneRDCR: Boolean? = null
    @SerializedName("DiaFacturacion")
    var diaFacturacion: Int? = null

    companion object {

        fun transform(input: CrossSellingConfiguracion?): CrossSellingConfiguracionEntity?{
            input?.run {
                val newSearchEntity = CrossSellingConfiguracionEntity()
                newSearchEntity.rdEsActiva = rdEsActiva
                newSearchEntity.lider = lider
                newSearchEntity.rdActivoMdo = rdActivoMdo
                newSearchEntity.rdTieneRDC = rdTieneRDC
                newSearchEntity.rdTieneRDI = rdTieneRDI
                newSearchEntity.rdTieneRDCR = rdTieneRDCR
                newSearchEntity.diaFacturacion = diaFacturacion
                return newSearchEntity
            }
            return null
        }

    }

}
