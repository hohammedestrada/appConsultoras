package biz.belcorp.consultoras.feature.scanner

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.feature.scanner.di.ScannerComponent
import biz.belcorp.consultoras.util.CommunicationUtils
import biz.belcorp.consultoras.util.ToastUtil
import biz.belcorp.mobile.components.scanner.Scanner
import biz.belcorp.mobile.components.scanner.core.ScanResult
import permissions.dispatcher.*
import javax.inject.Inject
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.fragment_scanner.*
import permissions.dispatcher.PermissionRequest
//import org.robolectric.util.Util.url
import android.content.Intent
import android.net.Uri
import biz.belcorp.consultoras.util.GlobalConstant



@RuntimePermissions
class ScannerFragment : BaseFragment(), Scanner.ResultHandler, ScannerView {

    companion object {
        private const val BLANK_PAGE = "about:blank"
        private const val MAX_LOAD = 0
    }

    @Inject
    lateinit var presenter: ScannerPresenter

    private var listener: OnScannerInteractionListener? = null
    private var countLoad = 0

    private var scanner : Scanner? = null
    private var isStarted = false
    private var toastNow : Toast? = null

    override fun onInjectView(): Boolean {
        getComponent(ScannerComponent::class.java).inject(this)
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.presenter.attachView(this)
        scanner = view.findViewById(R.id.scannerView)
        initScanner()
        initWebView()
        startScannerWithPermissionCheck()
    }

    private fun initScanner() {
        val formats = mutableListOf(
            BarcodeFormat.AZTEC,
            BarcodeFormat.MAXICODE,
            BarcodeFormat.QR_CODE
        )
        scanner?.setFormats(formats)
    }

    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (countLoad > MAX_LOAD) {
                    return true
                }
                showLoading()
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (countLoad > MAX_LOAD) {
                    return true
                }
                showLoading()
                return false
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                hideLoading()
                errorView.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (!url.equals(BLANK_PAGE)) {
                    countLoad++
                }
                hideLoading()
            }
        }
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onResume() {
        startScanner()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        isStarted = false
        scanner?.stopCamera()
    }

    override fun onDestroy() {
        scanner?.stopCamera()
        scanner = null
        super.onDestroy()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnScannerInteractionListener) {
            listener = context
        }
    }

    override fun context() = activity?.applicationContext

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun startScanner() {
        if(!isStarted && context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
            == PackageManager.PERMISSION_GRANTED) {
            scanner?.setResultHandler(this)
            scanner?.startCamera()
            isStarted = true
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_aceptar) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.button_cancelar) { _, _ -> request.cancel() }
                .setCancelable(false)
                .show()
        }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() {
        listener?.onFinishView()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onCameraNeverAskAgain() {
        context?.let {
            Toast.makeText(it, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_camera_denied)
                .setPositiveButton(R.string.button_go_to_settings) { _, _ ->
                    CommunicationUtils.goToSettings(it)
                    listener?.onFinishView()
                }
                .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                    dialog.dismiss()
                    listener?.onFinishView()
                }
                .show()
        }
    }

    override fun handleResult(rawResult: ScanResult) {
        presenter.evaluateResult(rawResult.text)
    }

    interface OnScannerInteractionListener {
        fun onFinishView()
        fun setShowScanner(isShow: Boolean)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun isValidUrl(url: String) {
        listener?.setShowScanner(false)
        toastNow?.cancel()
        val sendIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val chooser = Intent.createChooser(sendIntent, GlobalConstant.ELEGIR_NAVEGADOR)
        context?.apply{
            if (sendIntent.resolveActivity(this.packageManager) != null) {
                startActivity(chooser)
                listener?.onFinishView()
            }
        }



    }

    override fun isNotValidUrl() {
        countLoad = 0
        context?.let {
            toastNow = ToastUtil.show(it, R.string.scanner_message_url_invalid, Toast.LENGTH_SHORT)
        }
        hideLoading()
        scanner?.resumeCameraPreview(this@ScannerFragment)
    }

    fun showScanner() {
        countLoad = 0
        hideLoading()
        errorView.visibility = View.GONE
        if (Build.VERSION.SDK_INT < 18) {
            webView.clearView()
        } else {
            webView.loadUrl(BLANK_PAGE)
        }
        webView.visibility = View.GONE
        scanner?.resumeCameraPreview(this@ScannerFragment)
    }
}
