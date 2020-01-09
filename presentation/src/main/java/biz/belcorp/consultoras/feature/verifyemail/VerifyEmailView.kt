package biz.belcorp.consultoras.feature.verifyemail

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

/**
 * @author andres.escobar on 4/08/2017.
 */
interface VerifyEmailView : View, LoadingView {

    fun initScreenTrack(transform: LoginModel)
    fun onEmailUpdated()
    fun onError()
    fun onError(it: String)
}
