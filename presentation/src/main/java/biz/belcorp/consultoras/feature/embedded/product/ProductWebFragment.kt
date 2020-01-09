package biz.belcorp.consultoras.feature.embedded.product

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.feature.embedded.product.di.ProductWebComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.OrderOriginCode
import biz.belcorp.library.log.BelcorpLogger
import kotlinx.android.synthetic.main.fragment_webview.*
import biz.belcorp.library.util.DeviceUtil
import javax.inject.Inject
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.library.util.StringUtil


/** */

class ProductWebFragment : BaseFragment(), ProductWebView {

    @Inject
    lateinit var presenter: ProductWebPresenter

    internal var listener: Listener? = null

    private var isSessionExpired = false
    private var isWebBackLocked = false
    private var isFirstTime = true
    private var hasGoneBack = false
    private var hasError = false

    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    lateinit var loginModel: LoginModel

    private var cuv: String = ""
    private var palanca: String = ""
    private var origin: String = OrderOriginCode.DEFAULT
    /** */

    private val retryListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            activity?.finish()
        }

        override fun cancelar() {
            // EMPTY
        }
    }

    /** */
    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(ProductWebComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        val deviceId = DeviceUtil.getId(context()!!)

        val extras = arguments

        extras?.let {
            cuv = extras.getString(ProductWebActivity.EXTRA_CUV)?:StringUtil.Empty
            palanca = extras.getString(ProductWebActivity.EXTRA_PALANCA_ID)?:StringUtil.Empty
            origin = extras.getString(ProductWebActivity.EXTRA_ORIGIN, OrderOriginCode.DEFAULT)
        }
        presenter.loadURL(deviceId, cuv, palanca, origin)
        presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
    }

    /** */
    override fun onGetMenu(menu: Menu) {
        listener?.onGetMenu(menu)
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

    /** */



    override fun initScreenTrack(model: LoginModel) {
        loginModel = model
        Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS_PRODUCT, model)
    }

    override fun showSearchOption() {
        (activity as ProductWebActivity).showSearchOption()
    }

    override fun trackBack(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_ORDERS_PRODUCT, model)

        listener?.onBackFromFragment()

    }

    override fun showPostulantDialog() {
        try {
            val messageDialog = MessageDialog()
            messageDialog.isCancelable = false
            messageDialog
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.error_postulant_title)
                .setStringMessage(R.string.error_postulant_message)
                .setStringAceptar(R.string.button_continuar)
                .showCancel(false)
                .showIcon(true)
                .showClose(false)
                .show(childFragmentManager, "modalAceptar")
        } catch (e: Exception) {
            BelcorpLogger.w("showPostulant", e)
        }

    }

    override fun showUrl(url: String) {

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
                hideLoading()

                if (isFirstTime || !url.contains("/Login")) {

                    isFirstTime = false
                    isWebBackLocked = url.endsWith("/Pedido/Validado")

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

    /** */

    fun trackBackPressed() {
        presenter?.trackBack()
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
        refreshDataRunnable = Runnable { presenter?.initScreenTrack() }
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

    fun trackEvent(category: String, action: String, label: String, eventName: String) {
        Tracker.trackEvent(GlobalConstant.SCREEN_ORDERS_PRODUCT,
            category, action, label, eventName, loginModel)
    }

    /** */

    internal interface Listener {
        fun onBackFromFragment()
        fun onGetMenu(menu: Menu)
    }

    /** */

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }


    companion object {
        fun newInstance(): ProductWebFragment {
            return ProductWebFragment()
        }
    }
}

