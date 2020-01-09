package biz.belcorp.consultoras.feature.auth.login

import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel

interface Listener {

    fun openRegisterLink(url: String)
    fun openRecovery()
    fun openLogin()
    fun openLoginForm()
    fun openRegistration(facebookProfile: FacebookProfileModel?)
    fun onHome()
    fun onTerms(model: LoginModel)
    fun onTutorial(consultantName: String, countryISO: String)
    fun onFinish()
    fun onFinishError()

}
