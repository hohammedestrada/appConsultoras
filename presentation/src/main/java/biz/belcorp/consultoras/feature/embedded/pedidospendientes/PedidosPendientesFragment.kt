package biz.belcorp.consultoras.feature.embedded.pedidospendientes

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import javax.inject.Inject

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.dialog.PendingOrdersDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.feature.embedded.pedidospendientes.di.PedidosPendientesComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.library.analytics.annotation.AnalyticScreen
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DeviceUtil
import kotlinx.android.synthetic.main.fragment_offers.*

@AnalyticScreen(name = "OffersScreen")
class PedidosPendientesFragment : BaseFragment(), PedidosPendientesView {

    companion object {

        private const val URL_LOGIN = "/Login"
        private const val URL_ORDER_VALIDATE = "/Mobile/Pedido/Validado"
        private const val URL_ORDER = "/Pedido"

        private const val EXT_PDF = ".pdf"

        fun newInstance(): PedidosPendientesFragment {
            return PedidosPendientesFragment()
        }
    }

    @Inject
    lateinit var presenter: PedidosPendientesPresenter

    internal var listener: Listener? = null

    private var isWebBackLocked = false
    private var isFirstTime = true
    private var hasGoneBack = false

    private var hasError = false
    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    internal var deviceId = ""
    private var loginModel: LoginModel? = null

    private val retryListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            listener?.setResultOk()
        }

        override fun cancelar() {
            // Empty
        }
    }

    override fun context() = activity?.applicationContext

    override fun onInjectView(): Boolean {
        getComponent(PedidosPendientesComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)

        initHandler()

        if (savedInstanceState == null) {
            deviceId = DeviceUtil.getId(activity)

            presenter.load(deviceId, PageUrlType.PEDIDOS_PENDIENTES)
            presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
            setTitle(getString(R.string.pending_orders_title))
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_offers, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (refreshDataHandler == null) initHandler()
        refreshDataHandler?.removeCallbacks(refreshDataRunnable)
        refreshDataHandler?.postDelayed(refreshDataRunnable, 200)
    }

    override fun setTitle(title: String?) {
        title?.let { (activity as PedidosPendientesActivity).setTitle(it) }
    }

    override fun initScreenTrack(model: LoginModel) {
        this.loginModel = model
        Tracker.trackScreen(GlobalConstant.SCREEN_OFFERS, model)
    }

    override fun trackBack(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_OFFERS, model)
        listener?.onBackFromFragment()
    }

    override fun showSearchOption() {
        (activity as PedidosPendientesActivity).showSearchOption()
    }

    override fun showUrl(url: String) {

        BelcorpLogger.d("TAG URL ", url)

        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
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
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    loadPdf(view, request.url.toString())
                }
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                loadPdf(view, url)
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

                    webView.visibility = View.GONE
                    rltContentError?.visibility = View.VISIBLE
                }
            }

            fun loadPdf(view: WebView, url: String): Boolean {

                if (url.contains(EXT_PDF)) {
                    val path = Uri.parse(url)
                    val pdfIntent = Intent(Intent.ACTION_VIEW, path)

                    try {
                        startActivity(pdfIntent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(activity, getString(R.string.pdf_app_not_found_message), Toast.LENGTH_SHORT).show()
                    } catch (otherException: Exception) {
                        Toast.makeText(activity, getString(R.string.pdf_error_deafult_message), Toast.LENGTH_SHORT).show()
                    }

                }

                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                hideLoading()

                if (isFirstTime || !url.contains(URL_LOGIN)) {

                    isFirstTime = false
                    isWebBackLocked = url.endsWith(URL_ORDER_VALIDATE)

                    if (hasError && isVisible) {
                        hasError = false
                        showNetworkError()
                        webView.visibility = View.GONE
                        rltContentError.visibility = View.VISIBLE
                    } else if (hasGoneBack && isVisible) {
                        hasGoneBack = false
                        webView.reload()
                    }

                    if (isVisible && url.contains(URL_ORDER)) {
                        context?.let {
                            PendingOrdersDialog.Builder(it)
                                .setIcon(R.drawable.ic_check_selector_green)
                                .setTitle(getString(R.string.message_not_pending_orders))
                                .setListener(object : PendingOrdersDialog.PendingOrdersDialogListener {
                                    override fun onBackPressedDialog(dialog: PendingOrdersDialog) {
                                        dialog.dismiss()
                                        listener?.setResultOk()
                                    }

                                    override fun onClickAceptar(dialog: PendingOrdersDialog) {
                                        dialog.dismiss()
                                        listener?.setResultOk()
                                    }
                                }).show()
                        }
                    }

                } else if (isVisible) {
                    if (hasError) {
                        hasError = false
                        showNetworkError()
                        webView.visibility = View.GONE
                        rltContentError.visibility = View.VISIBLE
                    } else {
                        showExpiredSesion()
                    }
                }
            }
        }

        webView.loadUrl(url)
    }

    override fun showError() {
        if (isVisible) {
            webView?.visibility = View.GONE
            rltContentError?.visibility = View.VISIBLE
        }
    }

    override fun onGetMenu(menu: Menu) {
        listener?.onGetMenu(menu)
    }

    private fun initHandler() {
        refreshDataHandler = Handler()
        refreshDataRunnable = Runnable {
            presenter.initScreenTrack()
        }
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
            BelcorpLogger.w("showExpiredSesion", e)
        }
    }

    fun trackBackPressed() {
        presenter.trackBack()
    }

    fun onBackWebView(): Boolean {
        return if (!isWebBackLocked && webView.canGoBack()) {
            hasGoneBack = true
            webView.goBack()
            true
        } else false
    }

    fun trackEvent(category: String, action: String, label: String, eventName: String) {
        Tracker.trackEvent(GlobalConstant.SCREEN_OFFERS,
            category, action, label, eventName, loginModel)
    }

    internal interface Listener {
        fun onBackFromFragment()
        fun setResultOk()
        fun onGetMenu(menu: Menu)
    }
}
