package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.KitCaminoBrillante

interface KitView : View, LoadingView {

    fun showKits(kits: List<KitCaminoBrillante>)
    fun loadKits()
    fun onKitAdded(item: KitCaminoBrillante, quantity: Int)
    fun showMessage(message: String)
    fun showMessage(message: Int)
    fun showError(message: String)
    fun showError(message: Int)
    fun hideError()
    fun onKitLoaded()
    fun setUser(user: User)
    fun onSelectTab()
    fun setCanBack(canBack: Boolean)

}
