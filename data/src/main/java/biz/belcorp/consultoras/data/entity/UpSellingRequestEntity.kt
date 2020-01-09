package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.UpSellingConfiguracion
import biz.belcorp.consultoras.domain.entity.UpSellingRequest
import com.google.gson.annotations.SerializedName

class UpSellingRequestEntity {

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
    var configuracion: UpSellingConfiguracionEntity? = null

    companion object {

        fun transform(input: UpSellingRequest?): UpSellingRequestEntity? {
            input?.run {
                val newUpSellingRQEntity = UpSellingRequestEntity()
                newUpSellingRQEntity.campaniaId = campaniaId
                newUpSellingRQEntity.codigoProducto = codigoProducto
                newUpSellingRQEntity.precioCatalogo = precioCatalogo
                newUpSellingRQEntity.codigoZona = codigoZona
                newUpSellingRQEntity.personalizacionesDummy = personalizacionesDummy
                newUpSellingRQEntity.cuv = cuv
                newUpSellingRQEntity.fechaInicioFacturacion = fechaInicioFacturacion
                newUpSellingRQEntity.configuracion = UpSellingConfiguracionEntity.transform(configuracion)
                return newUpSellingRQEntity
            }
            return null
        }

    }
    
}

class UpSellingConfiguracionEntity{

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

        fun transform(input: UpSellingConfiguracion?): UpSellingConfiguracionEntity?{
            input?.run {
                val newSearchEntity = UpSellingConfiguracionEntity()
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
