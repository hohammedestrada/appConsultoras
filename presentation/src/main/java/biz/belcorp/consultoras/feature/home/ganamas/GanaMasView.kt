package biz.belcorp.consultoras.feature.home.ganamas

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel

interface GanaMasView: View, LoadingView {

    // Analytics
    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)

    fun setOffers(type: String, offers: List<Oferta?>)
    fun setOffersBySearch(products: List<ProductCUV?>)
    fun setConfig(config: List<ConfiguracionPorPalanca?>, flagATP: Boolean?)
    fun setUser(user: User?)
    fun setCategories(categories: List<Categoria?>, isCountryGanaMas: Boolean)
    fun setFilters(filters: List<GroupFilter?>)
    fun setOrdenamientos(orders: List<Ordenamiento?>)
    fun onOffersByLeverResponse(offers: List<Oferta?>, typeLever: String)
    fun onOfferAdded(quantity: Int, productCUV: ProductCUV, message: String?, code: Int, isFromKitNueva: Boolean = false)
    fun onOfferNotAdded(message: String?)
    fun setFilterOrderLabel(isVisible:Boolean, layoutView: Int)
    fun restartFragment()
    fun removeOffer(type: String)
    fun showErrorScreenMessage(type: Int)
    fun hideFilters()
    fun setImageEnabled(imageDialog: Boolean)
    fun setFlatHideViewForABTesting(hideView: Boolean)
    fun filterOffers(filters: ArrayList<CategoryFilterModel>?)
    fun showDialogDeleteArmaTuPack(typeLever: String, titleLever: String)
    fun setExpandedSearchviewForABTesting(expandedSearchview: Boolean)
    fun setFlagOrderConfigurableLever(jsonConfig: String)
    fun setFestivalConfiguracion(conf:FestivalConfiguracion)
    fun setSubCampaniaConfiguracion(conf: SubCampaniaConfiguracion)
    fun setDefaultBanner(imageBackground:Boolean)
    fun setBannerSubcampaniaPalanca(type: String)
}

