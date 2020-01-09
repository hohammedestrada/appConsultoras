package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.net.dto.dreamMeter.response.DreamMeterResponse
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DreamMeterDataMapper @Inject
internal constructor() {

    fun transformDreamMeter(response: DreamMeterResponse?): DreamMeter? {
        return DreamMeter().apply {
            id = response?.id
            name = response?.name
            dreamExample = response?.dreamExample
            amountExample = response?.amountExample
            firstCampaignId = response?.firstCampaignId
            lastCampaignId = response?.lastCampaignId
            startProgramCampaignId = response?.startProgramCampaignId
            endProgramCampaignId = response?.endProgramCampaignId
            status = response?.status
            if(response?.consultantDream?.programDreamId != null){
                consultantDream = transformConsultingDream(response.consultantDream)
            }
        }
    }

    fun transformConsultingDream(response: DreamMeterResponse.ConsultantDreamResponse?): DreamMeter.ConsultantDream {
        return DreamMeter.ConsultantDream().apply {
            programDreamId = response?.programDreamId
            consultantId = response?.consultantId
            description = response?.description
            dreamAmount = response?.dreamAmount
            saleAmount = response?.saleAmount
            status = response?.dreamStatus
            response?.details?.let {
                details = transformConsultingDreamDetail(it)
            }
        }
    }

    fun transformConsultingDreamDetail(details: List<DreamMeterResponse.ConsultantDreamResponse.ConsultingDreamDetailResponse>?): List<DreamMeter.ConsultantDream.ConsultantDreamDetail>? {
        return mutableListOf<DreamMeter.ConsultantDream.ConsultantDreamDetail>().apply {
            details?.forEach {
                add(DreamMeter.ConsultantDream.ConsultantDreamDetail().apply {
                    campaignId = it.campaignId
                    sale = it.sale
                    realSale = it.realSale
                    gain = it.gain
                    realGain = it.realGain
                    status = it.status
                })
            }
        }
    }

}
