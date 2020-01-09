package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.demostrador

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.GroupFilter
import biz.belcorp.consultoras.domain.entity.Ordenamiento
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.DemostradorCaminoBrillante
import biz.belcorp.mobile.components.design.counter.Counter

interface DemostradorView : View, LoadingView {

    fun showDemostradores(demostradores: List<DemostradorCaminoBrillante>)
    fun onDemostradorAdded(item: DemostradorCaminoBrillante, counter: Counter,position: Int, quantity: Int)
    fun showMessage(message: String)
    fun showMessage(message: Int)
    fun showError(message: String)
    fun showError(message: Int)
    fun hideError()
    fun onDemostradorLoaded()
    fun onFiltrosLoaded(filtros: List<GroupFilter?>)
    fun onOrderLoaded(orders : List<Ordenamiento?>)
    fun setUser(user: User)
    fun loadDemostradores()
    fun onSelectTab()
    fun hideFilters()
    fun hideOrders()
    fun setCanBack(canBack: Boolean)

}
