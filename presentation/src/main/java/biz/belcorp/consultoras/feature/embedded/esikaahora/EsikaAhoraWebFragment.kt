package biz.belcorp.consultoras.feature.embedded.esikaahora

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.feature.catalog.CatalogContainerActivity
import biz.belcorp.consultoras.feature.embedded.esikaahora.di.EsikaAhoraWebComponent
import biz.belcorp.consultoras.util.anotation.MenuCode
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DeviceUtil
import kotlinx.android.synthetic.main.fragment_webview.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EsikaAhoraWebFragment: BaseFragment(), EsikaAhoraWebView {

    @Inject
    lateinit var presenter: EsikaAhoraWebPresenter
    internal var listener: Listener? = null

    private var isSessionExpired = false
    private var isWebBackLocked = false
    private var hasGoneBack = false
    private var hasError = false

    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    internal var startNs: Long = 0

    override fun onInjectView(): Boolean {
        getComponent(EsikaAhoraWebComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.getPageTitle(MenuCode.MEN_ESIKA_AHORA)
        val deviceId = DeviceUtil.getId(context())
        presenter.load(deviceId)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onResume() {
        super.onResume()

        if (refreshDataHandler == null) initHandler()

        refreshDataHandler?.removeCallbacks(refreshDataRunnable)
        refreshDataHandler?.postDelayed(refreshDataRunnable, 200)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    override fun showUrl(url: String) {
        BelcorpLogger.d("TAG URL ", url)

        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.isVerticalScrollBarEnabled = true
        webView.isFocusable = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {


            @Deprecated("", ReplaceWith("false"))
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                showLoading()
                return false
            }


            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                showLoading()
                return false
            }


            @Deprecated("")
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                BelcorpLogger.d("WebActivity", "Error loading page $description")
                hasError = true
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onReceivedHttpError(view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse) {
                super.onReceivedHttpError(view, request, errorResponse)

                if (request.isForMainFrame) {
                    hasError = true
                    if (isVisible) {
                        webView.visibility = View.GONE
                        rltContentError.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                BelcorpLogger.d("Time: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs))

                isWebBackLocked =  url.contains(URL_ESIKA_MAIN)
                if (hasError && isVisible) {
                    hasError = false
                    showNetworkError()
                    webView.visibility = View.GONE
                    rltContentError.visibility = View.VISIBLE
                } else if (hasGoneBack && isVisible) {
                    hasGoneBack = false
                    webView.reload()
                }

                hideLoading()
            }


            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (isVisible && url?.contains(URL_ESIKA_COMPARTIR_CATALOGO) == true) {
                    val intent = Intent(activity, CatalogContainerActivity::class.java)
                    startActivity(intent)
                    view?.goBack()
                }
            }
        }
        webView.loadUrl(url)
    }

    override fun showLoading() {
        if (isVisible) {
            webView.visibility = View.GONE
        }
        super.showLoading()
    }

    override fun hideLoading() {
        if (isVisible)
            webView.visibility = View.VISIBLE
        super.hideLoading()
    }

    override fun showError() {
        if (isVisible) {
            webView.visibility = View.GONE
            rltContentError.visibility = View.VISIBLE
        }
    }

    override fun setMenuTitle(title: String?) {

        listener?.setMenuTitle(title)

    }

    fun onBackWebView(): Boolean {
        return if (!isSessionExpired && !isWebBackLocked && webView.canGoBack()) {
            hasGoneBack = true
            webView.goBack()
            true
        } else
            false
    }

    private fun initHandler() {
        refreshDataHandler = Handler()
    }

    internal interface Listener {
        fun onBackFromFragment()
        fun setMenuTitle(title: String?)
    }

    companion object {
        val ARGS_NIVEL = "ARGS_NIVEL"
        private const val URL_ESIKA_MAIN = "EsikaAhora"
        private const val URL_ESIKA_COMPARTIR_CATALOGO = "/MisCatalogosRevistas"

        fun newInstance(nivel: String): EsikaAhoraWebFragment {
            return EsikaAhoraWebFragment().apply {
                this.arguments = Bundle().apply {
                    putString(ARGS_NIVEL, nivel)
                }
            }
        }
    }

}
