package biz.belcorp.consultoras.feature.changeemail


import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

/**
 * @author andres.escobar on 4/08/2017.
 */
interface ChangeEmailView : View, LoadingView {

    fun initScreenTrack(model: LoginModel)
    fun onEmailUpdated()
    fun onError()
    fun onError(message: String)
    fun setUrlTyc(url: String)
}
