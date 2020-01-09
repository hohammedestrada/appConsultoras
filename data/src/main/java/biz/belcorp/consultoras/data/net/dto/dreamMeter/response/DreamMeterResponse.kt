package biz.belcorp.consultoras.data.net.dto.dreamMeter.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class DreamMeterResponse(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("dreamExample")
    var dreamExample: String? = null,
    @SerializedName("amountExample")
    var amountExample: String? = null,
    @SerializedName("firstCampaignId")
    var firstCampaignId: Int? = null,
    @SerializedName("lastCampaignId")
    var lastCampaignId: Int? = null,
    @SerializedName("startProgramCampaignId")
    var startProgramCampaignId: Int? = null,
    @SerializedName("endProgramCampaignId")
    var endProgramCampaignId: Int? = null,
    @SerializedName("status")
    var status: Boolean? = null,
    @SerializedName("consultantDream")
    var consultantDream: ConsultantDreamResponse? = null
) {
    data class ConsultantDreamResponse(
        @SerializedName("programDreamId")
        var programDreamId: Int? = null,
        @SerializedName("consultantId")
        var consultantId: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("dreamAmount")
        var dreamAmount: BigDecimal? = null,
        @SerializedName("saleAmount")
        var saleAmount: BigDecimal? = null,
        @SerializedName("dreamStatus")
        var dreamStatus: Boolean? = null,
        @SerializedName("details")
        var details: List<ConsultingDreamDetailResponse>? = null
    ) {
        data class ConsultingDreamDetailResponse(
            @SerializedName("campaignId")
            var campaignId: Int? = null,
            @SerializedName("sale")
            var sale: BigDecimal? = null,
            @SerializedName("realSale")
            var realSale: BigDecimal? = null,
            @SerializedName("gain")
            var gain: BigDecimal? = null,
            @SerializedName("realGain")
            var realGain: BigDecimal? = null,
            @SerializedName("status")
            var status: Int? = null
        )
    }
}

