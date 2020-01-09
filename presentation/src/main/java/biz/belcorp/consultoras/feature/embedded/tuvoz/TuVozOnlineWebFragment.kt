package biz.belcorp.consultoras.feature.embedded.tuvoz

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.embedded.tuvoz.di.TuVozOnlineWebComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.log.BelcorpLogger
import javax.inject.Inject
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import biz.belcorp.consultoras.util.ToastUtil
import biz.belcorp.library.util.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_tu_voz_online.*
import java.io.File

class TuVozOnlineWebFragment : BaseFragment(), TuVozOnlineWebView {

    @Inject
    lateinit var presenter: TuVozOnlineWebPresenter

    internal var listener: Listener? = null

    private var isWebBackLocked = false
    private var hasGoneBack = false
    private var hasError = false

    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(TuVozOnlineWebComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.getUrlTyC()
        showLoading()
        presenter.checkEmailIsEmpty()
        emailEdit.addTextChangedListener(getTextWatcher())
        termsCheckBox.setOnCheckedChangeListener { _, p1 ->
            updateButton.isEnabled = p1 && Patterns.EMAIL_ADDRESS.matcher(emailEdit.text.toString()).matches()
        }
        updateButton.setOnClickListener {
            KeyboardUtil.dismissKeyboard(context, emailEdit)
            presenter.updateEmail(emailEdit.text.toString())
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tu_voz_online, container, false)
    }

    override fun onResume() {
        super.onResume()

        if (refreshDataHandler == null) initHandler()

        refreshDataHandler?.removeCallbacks(refreshDataRunnable)
        refreshDataHandler?.postDelayed(refreshDataRunnable, 200)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroy()
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

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_TU_VOZ_ONLINE, model)
    }

    override fun trackBack(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_TU_VOZ_ONLINE, model)
        webView.clearCache(true)
        clearCacheFolder(context!!.cacheDir!!)
        listener?.onBackFromFragment()

    }

    override fun showUrl(url: String) {

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
                if (hasError && isVisible) {
                    hasError = false
                    showNetworkError()
                    webView.visibility = View.GONE
                    rltContentError.visibility = View.VISIBLE
                } else if (hasGoneBack && isVisible) {
                    hasGoneBack = false
                    webView.reload()
                }
            }
        }
        webView.loadUrl(url)
    }

    override fun showError() {
        if (isVisible) {
            webView.visibility = View.GONE
            updateEmailView.visibility = View.GONE
            rltContentError.visibility = View.VISIBLE
        }
    }

    override fun showError(message: String) {
        context?.let { ToastUtil.show(it, message, Toast.LENGTH_SHORT) }
    }

    override fun showTVO() {
        showUrl(GlobalConstant.URL_TUVOZ_ONLINE)
    }

    override fun showEnterDni() {
        updateEmailView.visibility = View.VISIBLE
    }

    override fun setUrlTyc(url: String) {
        termsText.setOnClickListener {
            openPdfTerms(url)
        }
    }

    override fun onUpdateEmail() {
        updateEmailView.visibility = View.GONE
        presenter.getUrl()
    }

    fun trackBackPressed() {
        presenter?.trackBack()
    }

    fun onBackWebView(): Boolean {
        return if (!isWebBackLocked && webView.canGoBack()) {
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

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // Empty
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateButton.isEnabled = termsCheckBox.isChecked && Patterns.EMAIL_ADDRESS.matcher(p0).matches()
            }
        }
    }

    private fun openPdfTerms(urlTerminos: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlTerminos))
            startActivity(browserIntent)
        } catch (ex: ActivityNotFoundException) {
            context?.let { ToastUtil.show(it, R.string.terms_activity_not_found, Toast.LENGTH_SHORT) }
            BelcorpLogger.w("openPdfTerms", ex)
        }
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    internal interface Listener {
        fun onBackFromFragment()
    }

    companion object {
        fun newInstance(): TuVozOnlineWebFragment {
            return TuVozOnlineWebFragment()
        }

        fun clearCacheFolder(dir: File?/*, numDays: Int*/): Int {

            var deletedFiles = 0
            if (dir != null && dir!!.isDirectory) {
                try {
                    for (child in dir!!.listFiles()) {

                        //first delete subdirectories recursively
                        if (child.isDirectory) {
                            deletedFiles += clearCacheFolder(child/*, numDays*/)
                        }

                        //then delete the files and subdirectories in this dir
                        //only empty directories can be deleted, so subdirs have been done first
                       // if (child.lastModified() < Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
                            if (child.delete()) {
                                deletedFiles++
                            }
                        //}
                    }
                } catch (e: Exception) {
                    Log.e("limpipiando_cache", "Error limpiando cache")
                }
            }
            return deletedFiles
        }
    }
}

