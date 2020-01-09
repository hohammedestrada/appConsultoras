package biz.belcorp.consultoras.feature.search.list

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.mobile.components.offers.sello.Sello
import biz.belcorp.consultoras.domain.entity.Categoria
import biz.belcorp.consultoras.domain.entity.OfferPromotionDual
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.SearchResponse
import biz.belcorp.consultoras.domain.entity.User


interface SearchListView : View, LoadingView {

    fun setUser(user: User?)

    fun onSearchResult(result: SearchResponse?)
    fun onSearchError(text: String?, exception: Throwable?)
    fun onAddProductError(text: String?, exception: Throwable?)


    fun onProductAdded(productCUV: ProductCUV?, showImage: Boolean, message: String?, type: Int)

    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)

    fun showLoadingSearch()
    fun hideLoadingSearch()
    fun setCategoriesInSearch(categories: List<Categoria?>)

    fun loadRecentOffers(recentOffers: List<SearchRecentOffer?>?)
    fun setConfigSellos(listSello: ArrayList<Sello?>)

    fun loadPromotions(list: ArrayList<OfferPromotionDual> )

}
