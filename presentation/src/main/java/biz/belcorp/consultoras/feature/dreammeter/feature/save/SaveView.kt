package biz.belcorp.consultoras.feature.dreammeter.feature.save

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import java.lang.Exception

interface SaveView : View, LoadingView {

    fun showDream(dreamMeter: DreamMeter?)
    fun goToDetail(dreamMeter: DreamMeter, isReplace: Boolean)
    fun setupListener()

    fun getExtraDreamMeter(): DreamMeter?

    fun enableButton()
    fun disableButton()

    fun onGetConfiguration(user: User)
    fun showError(e: Exception?)
    fun showDreamMeter(dreamMeter: DreamMeter?)
    fun showMessage(message: String?)
    fun setDreamMeter(dreamMeter: DreamMeter?)
    fun setCanBack(canBack: Boolean)

    fun onErrorLoad()
}
