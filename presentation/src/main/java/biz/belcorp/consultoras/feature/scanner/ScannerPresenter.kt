package biz.belcorp.consultoras.feature.scanner

import android.webkit.URLUtil
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.di.PerActivity
import javax.inject.Inject

@PerActivity
class ScannerPresenter @Inject internal constructor() : Presenter<ScannerView> {

    var scannerView: ScannerView? = null

    override fun attachView(view: ScannerView) {
        this.scannerView = view
    }

    override fun resume() {
        // Empty
    }

    override fun pause() {
        // Empty
    }

    override fun destroy() {
    }

    fun evaluateResult(value: String?) {
        scannerView?.showLoading()
        val e = URLUtil.isValidUrl(value)
        if (e) {
            value?.let {
                scannerView?.isValidUrl(value)
            } ?: scannerView?.isNotValidUrl()
        } else {
            scannerView?.isNotValidUrl()
        }
    }
}
