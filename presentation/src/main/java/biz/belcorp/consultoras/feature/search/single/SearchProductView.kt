package biz.belcorp.consultoras.feature.search.single

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*

interface SearchProductView: View, LoadingView{

    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)
    fun setData(user: User)
    fun setRelatedConfig(config: List<UserConfigData?>?)
    fun productNotFound()
    fun showCUV(productCUV: ProductCUV, msg: String)
    fun showCUVWithError(t: BasicDto<ProductCUV>)
    fun showCUVWithError(t: String?)
    fun onError(errorModel: ErrorModel)
    fun errorAddingProduct(mensaje: String)
    fun onProductAdded(message: String?)
    fun onFestivalAwardReached(message: String?)
    fun onProductNotAdded(message: String?)
    fun showToolTipError(mesaje: String?)
    fun showAlternative(type:Int,mensaje: String?, productList: Collection<ProductCUV?>, total: Int?)
    fun getRelatedOffers(productCUV: ProductCUV)
    fun showErrorExcedido(message: String)
    fun setImageEnabled(imageDialog: Boolean)
    fun onMensajeProl(mensajes: Collection<MensajeProl?>?)
    fun updateOriginValue(value: String, product: ProductCUV)
    fun setNameListTracker(nameList: String?)
    fun setNameListTrackerSuggest(nameList: String?)
    fun setConfigFest(configFest: FestivalConfiguracion?)
}
