package biz.belcorp.consultoras.feature.payment.online.metodopago

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.VisaConfig

interface PaymentOnlineView: View, LoadingView {
    fun getInitialConfig(config: PagoOnlineConfigModel) {  }
    fun getVisaConfig(config: VisaConfig){  }
    fun getVisaNextCounter(nextCounter: String?){  }
}
