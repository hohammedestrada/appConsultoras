package biz.belcorp.consultoras.feature.home.addorders

import android.support.annotation.StringRes
import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.error.BooleanDtoModel
import biz.belcorp.consultoras.common.model.orders.*
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*

interface AddOrdersView : View, LoadingView {

    fun setData(user: User)

    fun setPedidosPendientes(data: ArrayList<UserConfigData?>?)

    fun showConfig(pedidoConfig: PedidoConfigModel?)

    fun onError(errorModel: ErrorModel)

    fun showLoadingDialog()

    fun hideLoadingDialog()

    fun onError(message: String)

    fun initTrack(model: LoginModel, tipoTrack: Int)

    fun setOfferTitle(title: String)

    fun trackBackPressed(model: LoginModel)

    fun onOrderReserved(data: ReserveResponseModel?, message: String, flagOfertaFinal : Boolean, configuracionPremio: ConfiguracionPremio?)

    fun onOrderError(errorModel: BooleanDtoModel)

    fun onOrderError(message: String, orderReserved: OrderModel?)

    fun onFormattedOrderReceived(order: OrderModel?, clientModelList: List<ClienteModel?>?,
                                 callFrom: Int)

    fun onOffersReceived(offers: List<EstrategiaCarrusel>)

    fun setFinalOfferList(data: OfertaFinalResponseModel?, reserve: ReserveResponseModel?)

    fun getIsAnimatedShowed(isShowed: Boolean)

    fun getIsShowedToolTip(isShowed: Boolean)

    fun showOffers(nativo: Boolean)

    fun showDetailOptionReceiver(dni: String, nameReceiver: String)

    fun showOptionReceiver()

    fun updateDniSuccessful()

    fun disabledReceiverOption()

    fun onProductAdded(quantity: Int, productCUV: ProductCUV, message: String?, codeAlert: Int)

    fun onMensajeProl(mensajes: Collection<MensajeProl?>?)

    fun onMensajeProl(image:String, message: String,productCUV: ProductCUV)

    fun setImageEnabled(imageDialog: Boolean)

    fun onProductNotAdded(message: String?)

    fun onProductFestNotEliminated(message: String?)

    fun showError(b: Boolean)

    fun showError(@StringRes stringId: Int)

    fun onUndoReserve()

    fun reserveOrder()

    fun setMontoIncentivo(monto: Double)
    
    fun onEmailUpdated(newEmail: String?)

    fun showPedidosPendientesBloqueante(pedidos : Int?)

    fun showPedidosPendientesNoBloqueante(pedidos : Int?)

    fun showPedidosPendientesOnStart(pedidos : Int?)

    fun showButtonPendingOrder(pedidos : Int?)

    fun hideButtonPendingOrder()

    fun showAlertPendingOrders(pedidos : Int?)

    fun setNameListAnalytics(nameList: String?)

    fun setConfigFest(configFest: FestivalConfiguracion?)

    fun showOptionMultiOrderEnabled(isOn : Boolean)

    fun showOptionMultiOrderDisabled()

    fun showTooltipOn()

    fun showTooltipOff()

    fun setOffersRecomended(offers: List<Oferta>)

    fun mostrarExperiencia(mostrarExperiencia: Boolean, estadoPremio: Int)

    fun showPopUpDuoRecordatorio()

    fun loadPromotion(promotion: PromotionOfferModel?)

}
