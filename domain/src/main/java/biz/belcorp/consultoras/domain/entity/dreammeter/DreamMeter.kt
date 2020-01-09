package biz.belcorp.consultoras.domain.entity.dreammeter

import java.io.Serializable
import java.math.BigDecimal

class DreamMeter : Serializable {

    var id: Int? = null
    var name: String? = null
    var dreamExample: String? = null
    var amountExample: String? = null
    var firstCampaignId: Int? = null
    var lastCampaignId: Int? = null
    var startProgramCampaignId: Int? = null
    var endProgramCampaignId: Int? = null
    var status: Boolean? = null
    var consultantDream: ConsultantDream? = null

    class ConsultantDream : Serializable {
        var programDreamId: Int? = null
        var consultantId: String? = null
        var description: String? = null
        var dreamAmount: BigDecimal? = null
        var saleAmount: BigDecimal? = null
        var details: List<ConsultantDreamDetail>? = null
        var status: Boolean? = null

        class ConsultantDreamDetail : Serializable {
            var campaignId: Int? = null
            var sale: BigDecimal? = null
            var realSale: BigDecimal? = null
            var gain: BigDecimal? = null
            var realGain: BigDecimal? = null
            var status: Int? = null
        }

    }

}
