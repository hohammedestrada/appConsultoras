package biz.belcorp.consultoras.data.entity.campaignInformation

import com.google.gson.annotations.SerializedName

open class InfoCampaignDetailEntity{

    @SerializedName("Campania")
    var Campania: String? = null

    @SerializedName("FechaFacturacion")
    var FechaFacturacion: String? = null

    @SerializedName("FechaPago")
    var FechaPago: String? = null
}
