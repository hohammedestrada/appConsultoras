package biz.belcorp.consultoras.feature.embedded.success

import android.annotation.TargetApi
import android.content.Context
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
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.embedded.success.di.WalkSuccessComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.log.BelcorpLogger
import kotlinx.android.synthetic.main.fragment_webview.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WalkSuccessFragment : BaseFragment(),WalkSuccessWebView {

    @Inject
    lateinit var presenter : WalkSuccessPresenter
    private var isSessionExpired = false
    private var isFirstTime = true
    private var hasGoneBack = false
    private var hasError = false
    internal var startNs: Long = 0
    internal var listener: Listener? = null


    /** */
    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(WalkSuccessComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.loadURL()
    }


    /** */

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
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
        presenter.initScreenTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroy()
    }

    /** */

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


    /** */

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_WALK_SUCCESS, model)
    }

    override fun trackBack(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_WALK_SUCCESS, model)

        listener?.onBackFromFragment()
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
            // chromium, enable hardware acceleration             
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            // older android version, disable hardware acceleration             
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {


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

                hideLoading()

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

    override fun showError() {
        if (isVisible) {
            webView.visibility = View.GONE
            rltContentError.visibility = View.VISIBLE
        }
    }

    /** */

    fun trackBackPressed() {
        presenter?.trackBack()
    }

    fun onBackWebView(): Boolean {
        return if (!isSessionExpired && webView.canGoBack()) {
            hasGoneBack = true
            webView.goBack()
            true
        } else
            false
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

    /** */

    interface Listener {
        fun onBackFromFragment()
    }

}
