package biz.belcorp.consultoras.data.entity.campaignInformation

import com.google.gson.annotations.SerializedName

class InfoCampaignEntity : InfoCampaignDetailEntity(){

    @SerializedName("CampaniaAnterior")
    var CampaniaAnterior : ArrayList<InfoCampaignDetailEntity>? = null

    @SerializedName("CampaniaSiguiente")
    var CampaniaSiguiente : ArrayList<InfoCampaignDetailEntity>? = null

}
