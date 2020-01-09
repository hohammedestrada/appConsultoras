package biz.belcorp.consultoras.feature.payment.online.confirmacion

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.pagoonline.ResultadoPagoModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.VisaConfig
import pe.com.visanet.lib.VisaNetPaymentInfo

interface ConfirmacionView : View, LoadingView {

    fun onResultPayment(resultCode: Int, result : VisaNetPaymentInfo?, config: VisaConfig)
    fun blockPago()
    fun unblockPago()
    fun setResultPayment_Success(resultadoPagoModel: ResultadoPagoModel)
    fun setResultPayment_Rejected(resultadoPagoRechazadoModel: ResultadoPagoModel.ResultadoPagoRechazadoModel)
    fun onCancelPayment()

}
