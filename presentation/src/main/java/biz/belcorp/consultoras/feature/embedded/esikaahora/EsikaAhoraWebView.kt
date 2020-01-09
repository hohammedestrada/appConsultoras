package biz.belcorp.consultoras.feature.embedded.esikaahora

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView

interface EsikaAhoraWebView: View, LoadingView {

    fun showUrl(url: String)

    fun showError()

    fun setMenuTitle(title: String?)

}
