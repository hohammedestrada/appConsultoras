package biz.belcorp.consultoras.feature.ficha.common

import android.support.annotation.StringRes
import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import biz.belcorp.consultoras.common.view.LoadingDialogView
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.mobile.components.design.counter.Counter

interface BaseFichaView : View, LoadingView, LoadingDialogView {

    var imagesMaxFicha: Long?

    fun initScreenTrack(user: User)
    fun setUser(user: User?)
    fun load(oferta: Oferta) // para ficha normal y camino brillante
    fun load(component: Componente?) // para ficha componente
    fun load(promotion: PromotionOfferModel?) // para ficha normal
    fun share(url: String)
    fun addComplete(quantity: Int, productCUV: ProductCUV, showImage: Boolean, message: String?, codeAlert: String?)
    fun updateProlMessages(mensajes: Collection<MensajeProl?>?)
    fun showError()
    fun showError(message: String?)
    fun showError(canBack: Boolean)
    fun showError(@StringRes stringId: Int)
    fun setCarouselOffers(offers: List<Oferta>, type: Int, promotion: PromotionResponse? = null)
    fun onGetMenu(menu:Menu)
    fun setTextTitles(resIdPrice: Int?, strikethroughPrice: Boolean, resIdPriceClient: Int?, strikethroughPriceClient: Boolean ,resIdGain: Int?, strikethroughGain: Boolean)
    fun setupSello(selloConfig: FestivalSello?)
    fun setDataAward(listaFestivalProgressResponse: List<FestivalProgressResponse?>?, oferta: Oferta?, festivalResponse: FestivalResponse?)

    fun onFormattedOrderReceived(order: OrderModel?, clientModelList: List<ClienteModel?>?,
                                 callFrom: Int)

    fun validateFestCondition(oferta: Oferta, quantity: Int, counterView: Counter, identifier: String,
                              codigo: String?, palanca: String?, valor: String?,
                              editable: Boolean = false, id: Int = 0, clientID: Int = 0, reemplazarFestival: Boolean? = false, message: String?)

    fun onProductNotAdded(message: String?)
    fun onError(errorModel: ErrorModel)
    fun close()
}
