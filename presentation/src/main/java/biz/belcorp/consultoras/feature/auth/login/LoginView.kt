package biz.belcorp.consultoras.feature.auth.login

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.country.CountryModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.Verificacion

interface LoginView : View, LoadingView {

    fun renderData(country: CountryModel?)

    fun success(login: Login, model: LoginModel, verificacion: Verificacion)

    fun failed(error: ErrorModel)

    fun setLogAccess(kinesisModel: KinesisModel, login: Login)

}
