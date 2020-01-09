package biz.belcorp.consultoras.feature.home.fest

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel

interface FestView : View, LoadingView {

    // Analytics
    fun initScreenTrack()

    fun trackBackPressed()

    // View
    fun setImageEnabled(imageDialog: Boolean)

    fun showErrorScreenMessage(type: Int)
    fun setUser(user: User?)
    fun setOffersFestival(festival: FestivalResponse)
    fun onProductAdded(productCUV: ProductCUV, message: String?, code: Int)
    fun onAddProductError(text: String?, exception: Throwable?)
    fun setFilters(filters: List<GroupFilter?>)
    fun hideFilters()
    fun setOrders(orders: List<Ordenamiento?>)
    fun setFilterOrderLabel(isVisible: Boolean, layoutView: Int)
    fun filterOffers(filters: ArrayList<CategoryFilterModel>?)
    fun setConditionsOffersByFilter(offers: List<FestivalProduct?>)
    fun updateRewards(progressList: List<FestivalProgressResponse?>)
    fun setRowForPage(value: Int)
    fun getRowForPage(): Int
    fun setConfiguracion(config: FestivalConfiguracion?)

    // Redirige al Home y luego a Gana+ cuando se inicia desde una Notificacion
    fun goToOffers()
}
