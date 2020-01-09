package biz.belcorp.consultoras.feature.embedded.perfectduo

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.feature.embedded.perfectduo.di.PerfectDuoComponent
import biz.belcorp.library.log.BelcorpLogger
import kotlinx.android.synthetic.main.fragment_webview.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PerfectDuoFragment : BaseFragment(), PerfectDuoView{


    private var isSessionExpired = false
    private var isWebBackLocked = false
    private var hasGoneBack = false
    @Inject
    lateinit var presenter: PerfectDuoPresenter
    private var hasError = false
    private var isFirstTime = true
    internal var startNs: Long = 0
    internal var listener: ListenerBanner? = null

    override fun context(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PerfectDuoFragment.ListenerBanner) {
            listener = context
        }
    }

    /** */
    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(PerfectDuoComponent::class.java).inject(this)
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        //showLoading()
        presenter.generateUrlBanner()

    }

    fun trackBackPressed() {
       // presenter?.trackBack()
        listener?.onBackFromFragment()
    }

    override fun showUrl(url: String) {

        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.isVerticalScrollBarEnabled = true
        webView.isFocusable = true
        webView.clearCache(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {

            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (view_loading != null) {
                    view_loading.visibility = View.VISIBLE
                    }

                super.onPageStarted(view, url, favicon)
            }

            @Deprecated("", ReplaceWith("false"))
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
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

                if (isFirstTime || !url.contains("/Login")) {

                    isFirstTime = false
                    if (hasError && isVisible) {
                        hasError = false
                        showNetworkError()
                        webView.visibility = View.GONE
                        rltContentError.visibility = View.VISIBLE
                    } else if (hasGoneBack && isVisible) {
                        hasGoneBack = false
                        webView.reload()
                    }
                } else if (isVisible) {
                    isSessionExpired = true
                    showExpiredSesion()
                }
            }
        }
        startNs = System.nanoTime()
        webView.loadUrl(url)
    }

    fun onBackWebView(): Boolean {
        return if (!isSessionExpired && !isWebBackLocked && webView.canGoBack()) {
            hasGoneBack = true
            webView.goBack()
            true
        } else
            false
    }

    override fun showButtonToolbar() {
        (activity as PerfectDuoActivity).showSearchOption()
    }
    override fun trackBack(model: LoginModel) {
        //TODO ANALITYCS
    }

    private fun showExpiredSesion() {
        try {
            val messageDialog = MessageDialog()
            messageDialog.isCancelable = false
            messageDialog
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.error_expire_session_title)
                .setStringMessage(R.string.error_expire_session_message)
                .setStringAceptar(R.string.button_aceptar)
                .showCancel(false)
                .showIcon(true)
                .showClose(false)
                .setListener(retryListener).show(childFragmentManager, "modalAceptar")
        } catch (e: Exception) {
            BelcorpLogger.w("showExpiredSession", e)
        }

    }

    private val retryListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            activity?.finish()
        }

        override fun cancelar() {
            // EMPTY
        }
    }

    internal interface ListenerBanner {
        fun onBackFromFragment()
    }
}
