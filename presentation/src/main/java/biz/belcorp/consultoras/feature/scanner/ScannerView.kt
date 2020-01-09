package biz.belcorp.consultoras.feature.scanner

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView

interface ScannerView : View, LoadingView {

    fun isValidUrl(url: String)
    fun isNotValidUrl()
}
