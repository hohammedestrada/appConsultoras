package biz.belcorp.consultoras.feature.contest.rewards

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView

interface RewardsView : View, LoadingView {
    fun trackScreen(model: LoginModel)
    fun trackBack(model: LoginModel)
}
