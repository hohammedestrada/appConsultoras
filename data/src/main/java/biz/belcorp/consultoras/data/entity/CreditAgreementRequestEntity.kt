package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class CreditAgreementRequestEntity {

    @SerializedName("Aceptado")
    var aceptado: Boolean? = null

    @SerializedName("IP")
    var ip: String? = null

    @SerializedName("SOMobile")
    var so: String? = null

    @SerializedName("IMEI")
    var imei: String? = null

    @SerializedName("DeviceID")
    var deviceID: String? = null

}
