package biz.belcorp.consultoras.feature.embedded.offers

import android.annotation.TargetApi
import android.app.Activity
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
import android.webkit.*
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.feature.embedded.offers.di.OffersComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.DeviceUtil
import kotlinx.android.synthetic.main.fragment_offers.*
import javax.inject.Inject


class OffersFragment : BaseFragment(), OffersView {

    @Inject
    lateinit var presenter: OffersPresenter

    internal var listener: Listener? = null

    private var isWebBackLocked = false
    private var isFirstTime = true
    private var hasGoneBack = false

    private var hasError = false
    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    internal var deviceId = ""
    private var loginModel: LoginModel? = null

    private var page: String? = PageUrlType.OFFERS

    /**   */

    private val retryListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            activity?.finish()
        }

        override fun cancelar() {
            // EMPTY
        }
    }


    /**   */

    override fun onInjectView(): Boolean {
        getComponent(OffersComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)

        initBundle()
        initHandler()

        if (savedInstanceState == null) {
            deviceId = DeviceUtil.getId(activity)

            // Obtener el titulo de la pagina

            presenter.load(deviceId, page)
            presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
        }
    }

    /** */

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

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /**   */

    override fun setTitle(title: String?) {
        if (title != null) {
            (activity as OffersActivity).setTitle(title)
        }
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
        (activity as OffersActivity).showSearchOption()
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

                    if (webView != null) {
                        webView.visibility = View.GONE
                    }

                    if (rltContentError != null) {
                        rltContentError.visibility = View.VISIBLE
                    }
                }
            }

            fun loadPdf(view: WebView, url: String) {

                if (url.contains(".pdf")) {
                    val path = Uri.parse(url)
                    val pdfIntent = Intent(Intent.ACTION_VIEW, path)

                    try {
                        startActivity(pdfIntent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(activity, "Aplicación para ver archivos PDF no encontrada", Toast.LENGTH_SHORT).show()
                        BelcorpLogger.d(e)
                    } catch (otherException: Exception) {
                        Toast.makeText(activity, "Error desconocido", Toast.LENGTH_SHORT).show()
                        BelcorpLogger.d(otherException)
                    }

                }
            }

            fun loadGanaMas(url: String) {
                if (url.contains(BuildConfig.URL_REDIRECT_OFERTA)) {
                    showLoading()
                    presenter.refreshData()
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                loadGanaMas(url)
                hideLoading()

                if(url.contains("/ArmaTuPack/AgregarATPApp")){
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                    return
                }

                if (isFirstTime || !url.contains("/Login")) {

                    isFirstTime = false
                    isWebBackLocked = url.endsWith("/Mobile/Pedido/Validado")


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
            webView.visibility = View.GONE
            rltContentError.visibility = View.VISIBLE
        }
    }

    override fun onGetMenu(menu: Menu) {
        listener?.onGetMenu(menu)
    }

    /**   */

    private fun initBundle() {
        val bundle = arguments

        if (bundle != null) {
            page = bundle.getString(OffersActivity.OPTION)
            if(page == PageUrlType.REVISTA_DIGITAL_INFO){
                presenter.getPageTitle(MenuCodeTop.OFFERS)
            } else {
                bundle.getString(OffersActivity.SECTION)?.let {
                    setTitle(it)
                } ?: run {
                    presenter.getPageTitle(MenuCodeTop.OFFERS)
                }
            }


        }
    }

    private fun initHandler() {
        refreshDataHandler = Handler()
        refreshDataRunnable = Runnable { if (presenter != null) presenter?.initScreenTrack() }
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
        if (!isWebBackLocked && webView.canGoBack()) {
            hasGoneBack = true
            webView.goBack()
            return true
        } else
            return false
    }

    fun trackEvent(category: String, action: String, label: String, eventName: String) {
        Tracker.trackEvent(GlobalConstant.SCREEN_OFFERS,
                category, action, label, eventName, loginModel)
    }

    override fun onGanaMasFinish() {
        hideLoading()
        listener?.onGanaMasFinish()
    }


    /**   */

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    /**   */

    internal interface Listener {
        fun onBackFromFragment()
        fun onGetMenu(menu: Menu)
        fun onGanaMasFinish()
    }

    companion object {

        fun newInstance(): OffersFragment {
            return OffersFragment()
        }
    }
}
