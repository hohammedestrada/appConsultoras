package biz.belcorp.consultoras.feature.product

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.MensajeProl

interface ProductView:  View, LoadingView {

    // View
    fun loadProduct(product: ProductItem)
    fun onProductSaved(mensaje: String?)
    fun showTooltipError(message: String?)
    fun hideTooltipError()
    fun showErrorExcedido(message: String?)
    fun showBootomMessage(message: String?)

    fun onMensajeProl(mensajes: Collection<MensajeProl?>?)
    // Analytcs
    fun initScreenTrack(model: LoginModel)
    fun trackBackPressed(model: LoginModel)


}
