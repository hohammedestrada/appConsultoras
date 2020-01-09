package biz.belcorp.consultoras.feature.vinculacion

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.User


interface VinculacionView : View, LoadingView {

    fun setData(user: User)

    fun initScreenTrack(model: LoginModel)

    fun onCreditAgreementAccept()

    fun onUrlCreditAgreement(url: String)

    fun onError(errorModel: ErrorModel)

    fun onError(message: String)

}
