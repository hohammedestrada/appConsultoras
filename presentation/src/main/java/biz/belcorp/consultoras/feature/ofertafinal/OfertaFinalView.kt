package biz.belcorp.consultoras.feature.ofertafinal

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.mobile.components.offers.model.OfferModel


interface OfertaFinalView: View, LoadingView {
    fun setOffersRecomended(offers: List<Oferta>)
    fun showOfertasRecomendadas(flag: Boolean)
    fun setUser(user: User?)
    fun onOfferAdded(quantity: Int, productCUV: ProductCUV, message: String?, code: Int)
    fun onOfferNotAdded(message: String?)
    fun showErrorScreenMessage(type: Int)
    fun setImageEnabled(imageDialog: Boolean)
    fun showSearchItem()
    fun onNotAdded(message: String?)
    fun setPremios(premios: List<PremioFinal?>?)
    fun setupProgess(premioFinalMeta: PremioFinalMeta)

}
