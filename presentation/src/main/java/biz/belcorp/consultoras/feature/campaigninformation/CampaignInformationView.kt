package biz.belcorp.consultoras.feature.campaigninformation

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.campaignInformation.InfoCampaign

interface CampaignInformationView : View, LoadingView {

    fun onError(errorModel: ErrorModel)

    fun onInfoCampania(info : InfoCampaign?)

}
