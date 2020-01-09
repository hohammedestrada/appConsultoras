package biz.belcorp.consultoras.feature.payment.online.confirmacion

import android.app.Activity
import android.util.Log
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.pagoonline.ConfirmacionPagoModel
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineVisaMapper
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.PagoOnlineUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import pe.com.visanet.lib.VisaNetPaymentInfo
import javax.inject.Inject
import com.google.gson.Gson



class ConfirmacionPresenter

    @Inject
    internal constructor(
        private val payment: PagoOnlineUseCase,
        private val userUseCase: UserUseCase,
        private val pagoOnlineVisaMapper: PagoOnlineVisaMapper
    ):Presenter<ConfirmacionView> {

    private var confirmacionView: ConfirmacionView? = null

    override fun attachView(view: ConfirmacionView) {
        confirmacionView = view
    }


    override fun resume() {

    }

    override fun pause() {

    }

    override fun destroy() {

    }

    fun setResultPaymentVisa(resultCode: Int, visaNetPaymentInfo: VisaNetPaymentInfo?, confirm: ConfirmacionPagoModel, config: VisaConfig){
        //val gson = Gson()
        //val jsonInString = gson.toJson(visaNetPaymentInfo)
        //Log.d("setResultPaymentVisa", jsonInString)

        if(visaNetPaymentInfo != null) {
            when(resultCode){
                Activity.RESULT_OK, Activity.RESULT_CANCELED -> {
                    userUseCase[object : BaseObserver<User?>(){
                        override fun onNext(t: User?) {
                            t!!.let {
                                saveResultPaymentVisa(pagoOnlineVisaMapper.transform(visaNetPaymentInfo, confirm, config, t))
                            }
                        }
                    }]
                }
            }
        }
        confirmacionView!!.unblockPago()
    }

    private fun saveResultPaymentVisa(visaPayment : VisaPayment){

        payment.savePayment(visaPayment,0 ,object : BaseObserver<BasicDto<ResultadoPagoEnLinea>>(){
            private var success = false
            override fun onStart() {
                confirmacionView!!.showLoading()
            }
            override fun onNext(t: BasicDto<ResultadoPagoEnLinea>) {
                when(visaPayment.paymentStatus){
                    "0" -> {
                        confirmacionView!!.setResultPayment_Success(pagoOnlineVisaMapper.transform(visaPayment,t.data!!))
                    }
                    else -> {
                        confirmacionView!!.setResultPayment_Rejected(pagoOnlineVisaMapper.transform(visaPayment))
                    }
                }
                success = true
                confirmacionView!!.hideLoading()
            }
        })
    }

}
