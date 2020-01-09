package biz.belcorp.consultoras.feature.payment.online

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.VisaConfig

interface VisaView : View, LoadingView {
    fun getInitialVisaConfig(config: VisaConfig?)
}
