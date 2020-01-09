package biz.belcorp.consultoras.data.net.dto.dreamMeter.request

import com.google.gson.annotations.SerializedName

data class DreamMeterRequest(
    @SerializedName("countryISO")
    var countryISO : String? = null,
    @SerializedName("programDreamId")
    var programDreamId : Int? = null,
    @SerializedName("consultantId")
    var consultantId: String? = null,
    @SerializedName("description")
    var description : String? = null,
    @SerializedName("dreamAmount")
    var dreamAmount : String? = null,
    @SerializedName("campaignId")
    var campaignId : String? = null,
    @SerializedName("maximunAmountSale")
    var maximumAmountSale : Double? = null,
    @SerializedName("dreamStatus")
    var dreamStatus : Boolean? = null
)
