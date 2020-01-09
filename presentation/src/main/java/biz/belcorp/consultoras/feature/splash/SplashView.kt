package biz.belcorp.consultoras.feature.splash

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.AuthModel
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.user.UserModel
import biz.belcorp.consultoras.common.view.ErrorView
import biz.belcorp.consultoras.common.view.RetryView
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.Verificacion

interface SplashView : View, RetryView, ErrorView {

    fun loadData(authModel: AuthModel)

    fun savedCountrySIM(status: Boolean)

    fun saved(result: Boolean)

    fun checkVersion()

    fun updateVersion(required: Boolean, url: String, tipoDescarga: Int)

    fun initSubida()

    fun initBackup()

    fun initRestore()

    fun initData()

    fun showMenu(campaign: String, revistaDigital: Int)

    fun showHome(user: UserModel)

    fun initScreenTrack(model: User?)

    fun onVerificacionResponse(verificacion: Verificacion, loginModel: LoginModel, online: Boolean)
}
