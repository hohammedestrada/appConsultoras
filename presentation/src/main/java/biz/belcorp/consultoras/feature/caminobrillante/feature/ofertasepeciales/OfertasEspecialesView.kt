package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.User

interface OfertasEspecialesView: View, LoadingView {
    fun setUser(user: User)
    fun getCurrentPosition() : Int
    fun reloadOffers()
    fun getSizeCount(): Int
}
