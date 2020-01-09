package biz.belcorp.consultoras.feature.sms

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.BasicDto

/**
 * @author Leonardo Castañeda on 24/08/2017.
 */

interface SMSView : View, LoadingView {

    // Analytcs
    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)

    // View
    fun onSMSResponse(send: BasicDto<Boolean>?)
    fun onSMSError(exception: Throwable)

    fun onConfirmSMSCodeResponse(send: BasicDto<Boolean>?)
    fun onConfirmSMSCodeError(exception: Throwable)

    fun onConfirmPhoneChangeResponse(send: BasicDto<Boolean>?)
    fun onConfirmPhoneChangeError(exception: Throwable)

    fun onShowTerms(url: String)

}
