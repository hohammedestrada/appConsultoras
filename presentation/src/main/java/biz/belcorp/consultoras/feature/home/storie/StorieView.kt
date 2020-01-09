package biz.belcorp.consultoras.feature.home.storie

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView

interface StorieView: View, LoadingView {

    fun onStatusStorieSaved(){}

}
