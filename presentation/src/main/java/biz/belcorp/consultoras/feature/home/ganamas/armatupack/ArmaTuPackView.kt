package biz.belcorp.consultoras.feature.home.ganamas.armatupack

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.ConfiguracionPorPalanca
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.mobile.components.design.tone.model.CategoryToneModel
import biz.belcorp.mobile.components.design.tone.model.ToneModel

interface ArmaTuPackView : View, LoadingView {

    // Analytics
    fun initScreenTrack()
    fun trackBackPressed()

    // View
    
    fun setImageEnabled(imageDialog: Boolean)

    fun onOfferAdded(quantity: Int, productCUV: ProductCUV, message: String?)
    //fun onOfferAdded(quantity: Int, productCUV: ProductCUV)
    //fun onOfferUpdated(productCUV: ProductCUV)
    fun onOfferUpdated(productCUV: ProductCUV, message: String?)
    fun onOfferNotAdded(message: String?)
    fun showArmaTuPack(oferta: Oferta, groupProduct: ArrayList<CategoryToneModel>, productHistory: ArrayList<ToneModel>)
    fun showErrorScreenMessage(type: Int)

    // TODO: Redirige al Home y luego a Gana+ cuando se inicia desde una Notificacion
    fun goToOffers()

    // TODO: Quitar ésta sección cuando se obtenga el origen pedido web para ATP con un mejor método
    fun setConfig(config: List<ConfiguracionPorPalanca?>, flagATP: Boolean?)
    fun setUser(user: User?)

}
