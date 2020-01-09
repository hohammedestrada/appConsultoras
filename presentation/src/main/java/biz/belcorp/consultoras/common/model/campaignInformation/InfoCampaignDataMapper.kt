package biz.belcorp.consultoras.common.model.campaignInformation

import biz.belcorp.consultoras.domain.entity.campaignInformation.InfoCampaignDetail

class InfoCampaignDataMapper{

    fun transform(input : InfoCampaignDetail) : InfoCampaignDetailModel{
        return InfoCampaignDetailModel().apply{
            input?.let {
                Campania = it.Campania
                FechaFacturacion = it.FechaFacturacion
                FechaPago = it.FechaPago
            }
        }
    }

    fun transform(input : ArrayList<InfoCampaignDetail>) : ArrayList<InfoCampaignDetailModel>{
        return ArrayList<InfoCampaignDetailModel>().apply {
            input?.let { input ->
                input.forEach { item->
                    add(transform(item))
                }
            }
        }
    }
}
