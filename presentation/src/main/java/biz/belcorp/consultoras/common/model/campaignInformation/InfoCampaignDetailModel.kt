package biz.belcorp.consultoras.common.model.campaignInformation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class InfoCampaignDetailModel : Parcelable {

    var Campania: String? = null

    var FechaFacturacion: String? = null

    var FechaPago: String? = null

}
