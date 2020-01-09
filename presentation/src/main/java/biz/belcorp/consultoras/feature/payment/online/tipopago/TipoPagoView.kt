package biz.belcorp.consultoras.feature.payment.online.tipopago

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.pagoonline.TipoPagoModel
import biz.belcorp.consultoras.common.view.LoadingView

interface TipoPagoView : View, LoadingView {
    fun getTipo(createTipoPago: TipoPagoModel)
}
