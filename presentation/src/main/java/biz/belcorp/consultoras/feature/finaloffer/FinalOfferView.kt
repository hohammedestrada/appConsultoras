package biz.belcorp.consultoras.feature.finaloffer

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*

interface FinalOfferView: View, LoadingView {

    fun initScreenTrack(model: LoginModel)

    fun onError(errorModel: ErrorModel)

    fun onError(message: String)

    fun setData(user: User)

    fun updateOrder(t: OrderModel?)

    fun onReserveSuccess(data: ReservaResponse?)

    fun onReserveError(message: String, data: ReservaResponse?)

    fun onFinalOfferAddResponse(item: OfertaFinalModel, position: Int)

    fun onAutoSavedGift()

    fun onEmailUpdated(newEmail: String?)

    fun showLoadingDialog()

    fun hideLoadingDialog()

    fun initializeListOfferFinal(nameList: String?)
}
