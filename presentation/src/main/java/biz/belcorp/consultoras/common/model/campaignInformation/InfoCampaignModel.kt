package biz.belcorp.consultoras.common.model.campaignInformation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class InfoCampaignModel() : InfoCampaignDetailModel(), Parcelable{

    var CampaniaAnterior : ArrayList<InfoCampaignDetailModel>? = null

    var CampaniaSiguiente : ArrayList<InfoCampaignDetailModel>? = null

}
