package biz.belcorp.consultoras.feature.home.subcampaing

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel

interface SubCampaingView : View, LoadingView {

    fun setOrdenamientos(orders: List<Ordenamiento?>)
    fun showErrorScreenMessage(type: Int)
    fun setUser(user: User?)
    fun onProductAdded(productCUV: ProductCUV, message: String?)
    fun onAddProductError(text: String?, exception: Throwable?)
    fun setFilters(filters: List<GroupFilter?>)
    fun hideFilters()
    fun setOrders(orders: List<Ordenamiento?>)
    fun setFilterOrderLabel(isVisible: Boolean, layoutView: Int)
    fun onOffersByLeverResponse(offers: List<Oferta?>, typeLever: String)
    fun filterOffers(filters: ArrayList<CategoryFilterModel>?)
    fun setConfig(config: List<ConfiguracionPorPalanca?>, flagATP: Boolean?)
    fun setTitleSubcampaign(title: String)

    // Redirige al Home y luego a Gana+ cuando se inicia desde una Notificacion
    fun goToOffers()

}
