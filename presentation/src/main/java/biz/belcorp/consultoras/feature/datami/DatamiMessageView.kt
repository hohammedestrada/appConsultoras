package biz.belcorp.consultoras.feature.datami

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel

interface DatamiMessageView : View {

    fun initScreenTrack(model: LoginModel)
    fun closeMessageResult()

}
