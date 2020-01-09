package biz.belcorp.consultoras.feature.search.results

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*

interface SearchResultsView: View, LoadingView {

    fun setUser(user: User?)

    fun onSearchResult(result: SearchResponse?)
    fun onSearchError(text: String?, exception: Throwable?)
    fun onAddProductError(text: String?, exception: Throwable?)
    fun onOrderByParametersResult(parameters: Collection<SearchOrderByResponse?>)
    fun setFilters(filters: List<GroupFilter?>)

    fun onProductAdded(productCUV: ProductCUV, message: String?, resultCode: String?)
    fun setImageEnabled(imageDialog: Boolean)

    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)
    fun hideFilters()
}
