package biz.belcorp.consultoras.feature.dreammeter.feature.detail

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import java.math.BigDecimal

interface DetailView : View, LoadingView {

    fun getExtraDreamMeter() : DreamMeter?
    fun showDreamMeter(user: User?, consultantDream: DreamMeter.ConsultantDream, totalProgress: BigDecimal, showAnimation: Boolean)
    fun onErrorDreamMeter()
    fun onLoadConfiguracion(it: User)
    fun reloadDreamMeter(dreamMeter: DreamMeter)
    fun setCanBack(canBack: Boolean)
    fun hideEditar()
    fun showEditar()
    fun setMensajeDream(mensaje: Int)
    fun setMensajeDream(mensaje: String)
    fun getStringByIdRes(idRes: Int, vararg params: String) : String

}
