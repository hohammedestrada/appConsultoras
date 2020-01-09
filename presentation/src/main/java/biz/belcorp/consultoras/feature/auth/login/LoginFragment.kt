package biz.belcorp.consultoras.feature.auth.login

import android.Manifest
import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AlertDialog
import android.text.Html
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.model.auth.CredentialsModel
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.country.CountryModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.dto.AuthErrorDto
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.Verificacion
import biz.belcorp.consultoras.feature.auth.di.AuthComponent
import biz.belcorp.consultoras.feature.splash.SplashPresenter
import biz.belcorp.consultoras.feature.splash.SplashService
import biz.belcorp.consultoras.util.*
import biz.belcorp.consultoras.util.anotation.AuthType
import biz.belcorp.consultoras.util.anotation.BusinessErrorCode
import biz.belcorp.consultoras.util.anotation.CreditApplicationType
import biz.belcorp.consultoras.util.anotation.ErrorCode
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.analytics.annotation.AnalyticEvent
import biz.belcorp.library.analytics.annotation.AnalyticScreen
import biz.belcorp.library.annotation.Country
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import com.facebook.FacebookException
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_splash.*
import permissions.dispatcher.*
import javax.inject.Inject


@RuntimePermissions
@AnalyticScreen(name = GlobalConstant.SCREEN_LOGIN)
class LoginFragment : BaseFragment(), LoginView, SplashService.Listener {

    @Inject
    lateinit var presenter: LoginPresenter

    @Inject
    lateinit var splashPresenter: SplashPresenter

    private var fbUtil: FacebookUtil? = null
    private var facebookProfile: FacebookProfileModel? = null
    private var country: CountryModel? = null
    private var kinesisManager: KinesisManager? = null

    private var listener: Listener? = null

    private var isFbLogin = false


    /** Atributos y métodos del evento del SplashService */

    private var splashService : SplashService? = null
    private var mBound = false
    private var progressDialog : ProgressDialog? = null

    private var mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            // EMPTY
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder =  service as SplashService.LocalBinder
            splashService = binder.service
            mBound = true

            splashService?.presenter = splashPresenter
            splashService?.listener = this@LoginFragment
            splashService?.activity = activity as BaseActivity

            //iniciar el login
            progressDialog = ProgressDialog(activity)
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            //Without this user can hide loader by tapping outside screen
            progressDialog?.setCancelable(false)
            //Setting Title
            progressDialog?.setMessage("Cargando...")
            progressDialog?.show()

            if (!isAdded) return

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val wrapDrawable = DrawableCompat.wrap(pgb_download.indeterminateDrawable)
                DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context!!, R.color.white))
                pgb_download.indeterminateDrawable = DrawableCompat.unwrap(wrapDrawable)
            }
            splashService?.checkVersion()
        }
    }


    /** FacebookUtil */

    private val facebookListener: FacebookUtil.OnFacebookListener = object : FacebookUtil.OnFacebookListener {
        override fun onLoginSuccess(model: FacebookProfileModel) {
            facebookProfile = model

            val credentials = CredentialsModel()
            credentials.username = model.id
            credentials.password = ""
            credentials.pais = ""
            credentials.tipoAutenticacion = AuthType.FACEBOOK

            presenter.loginWithFacebook(credentials, facebookProfile)
        }

        override fun onLoginFailure(exception: FacebookException) {
            hideLoading()
            showErrorFacebook()
            isFbLogin = false
        }

        override fun onLoginCancel() {
            hideLoading()
            isFbLogin = false
        }

        override fun onRenderData(model: FacebookProfileModel) {
            hideLoading()
            facebookProfile = model
        }

        override fun onLoginPermissionDeclined() {
            hideLoading()
            showErrorFacebookPermissionDeclined()
        }

        private fun showErrorFacebook() {
            try {
                fragmentManager?.let{
                    MessageDialog()
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setStringTitle(R.string.login_error_facebook_title)
                        .setStringMessage(R.string.login_error_facebook_message)
                        .setStringAceptar(R.string.button_aceptar)
                        .showIcon(true)
                        .showClose(false)
                        .show(it, "modalError")
                }

            } catch (e: IllegalStateException) {
                BelcorpLogger.w("modalError", e)
            }

        }

        private fun showErrorFacebookPermissionDeclined() {
            try {
                fragmentManager?.let{
                    MessageDialog()
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setStringTitle(R.string.login_error_facebook_permission_declined_title)
                        .setStringMessage(R.string.login_error_facebook_permission_declined_message)
                        .setStringAceptar(R.string.button_aceptar)
                        .setStringCancelar(R.string.button_cancelar)
                        .setListener(permissionListener)
                        .showIcon(true)
                        .showClose(false)
                        .showCancel(true)
                        .show(it, "modalError")
                }

            } catch (e: IllegalStateException) {
                BelcorpLogger.w("modalError", e)
            }

        }
    }

    private val permissionListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            if (NetworkUtil.isThereInternetConnection(context!!)) {
                showLoading()
                fbUtil!!.retryPermission(activity)
            } else {
                showNetworkError()
            }
        }

        override fun cancelar() {
            fbUtil!!.logOutFacebook()
            isFbLogin = false
        }
    }

    /** BaseFragment functions */

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(AuthComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)

        if (savedInstanceState == null) {
            init()
        }
    }

    /** */

    override fun onError() {
        // EMPTY
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (fbUtil == null)
            fbUtil = FacebookUtil(facebookListener)

        if (fbUtil!!.callBackManager != null) {
            fbUtil!!.callBackManager.onActivityResult(requestCode, resultCode, data)
            isFbLogin = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isFbLogin) initScreenTrack()
    }

    /** private funtions */

    private fun initScreenTrack() {

        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserNullProperties(null)

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    private fun init() {

        if (CountryUtil.getCode(context!!) == Country.CR) {
            tvw_title.visibility = View.GONE
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                tvw_title.text = Html.fromHtml(getString(R.string.login_welcome), Html.FROM_HTML_MODE_LEGACY)
            else
                tvw_title.text = Html.fromHtml(getString(R.string.login_welcome))
        }

        btn_facebook.setOnClickListener { onFacebook() }

        btn_login.setOnClickListener { onLogin() }

        tvw_register.setOnClickListener { onRegister() }

        fbUtil = FacebookUtil(facebookListener)
        presenter.data()

    }

    private fun openRegisterLink(url: String) {
        if (null != listener) {
            listener!!.openRegisterLink(url)
        }
    }

    /** LoginView */

    override fun renderData(country: CountryModel?) {
        this.country = country
    }

    override fun success(login: Login, model: LoginModel, verificacion: Verificacion) {
        presenter.getUsabilityConfig(login)

        val bundle = Bundle()
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_LOGIN_SUCCESS)
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_LOGIN_FB)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        model.tipoIngreso = login.tipoIngreso
        val properties = AnalyticsUtil.getUserProperties(model)
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_FB_NAME, bundle, properties)

        if (null != listener) {
            if (model.isAceptaTerminosCondiciones && (model.indicadorContratoCredito
                    == CreditApplicationType.ACCEPT || model.indicadorContratoCredito
                    == CreditApplicationType.NOT_APPLY)) {
                val sessionManager = SessionManager.getInstance(context()!!)
                if (sessionManager.isTutorial(model.consultantCode)!!) {
                    hideLoading()
                    listener!!.onTutorial(model.consultantCode, model.countryISO)
                } else {
                    hideLoading()
                    listener!!.onHome()
                }
            } else {
                hideLoading()
                listener!!.onTerms(model)
            }
        }
    }

    override fun setLogAccess(kinesisModel: KinesisModel, login: Login) {
        try {
            if (kinesisManager == null) {
                kinesisManager = KinesisManager.create(activity!!, GlobalConstant.SCREEN_LOG_LOGIN, kinesisModel)
            }
            kinesisManager!!.save(login)
        } catch (ignored: Exception) {
            // EMPTY
        }

    }

    override fun failed(error: ErrorModel) {
        when (error.code) {
            ErrorCode.NETWORK -> showNetworkError()
            ErrorCode.BAD_REQUEST, ErrorCode.SERVICE -> {
                val response = RestApi.readError(error.params, AuthErrorDto::class.java)

                if (response != null && BusinessErrorCode.AUTENTICACION_USUARIO_NOEXISTE == response.error && null != listener) {
                    listener!!.openRegistration(facebookProfile)
                    return
                }

                if (response == null || StringUtil.isNullOrEmpty(response.description))
                    showErrorDefault()
                else
                    showErrorMessage(response.description)
            }
            else -> showErrorDefault()
        }
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    /** */

    private fun onFacebook() {

        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BUTTON)
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LOGIN_FB)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_NAME, bundle)

        if (NetworkUtil.isThereInternetConnection(context!!) && fbUtil != null) {
            showLoading()
            fbUtil!!.login(activity)
        } else {
            showNetworkError()
        }
    }

    internal fun onLogin() {

        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BUTTON)
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_NAME, bundle)

        if (null != listener) {
            listener!!.openLoginForm()
        }
    }

    @AnalyticEvent(action = "OnRegisterClick", category = "Click")
    internal fun onRegister() {
        if (null == country)
            openRegisterLink("http://www.uneteabelcorp.com/")
        else if (Patterns.WEB_URL.matcher(country!!.urlJoinBelcorp).matches())
            openRegisterLink(country!!.urlJoinBelcorp)
    }

    override fun onStart() {
        super.onStart()
        arguments?.getBoolean("AppMV")?.takeIf {it}?.apply {
            checkVersionWithPermissionCheck()
        }
    }

    override fun onStop() {
        // Unbind from the service
        if (mBound) {
            activity?.unbindService(mConnection)
            mBound = false
        }
        super.onStop()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun checkVersion() {
        val intent = Intent(activity, SplashService::class.java)
        activity?.startService(intent)
        activity?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onSplashInitScreenTrack(model: User?) {
        // EMPTY
    }

    override fun onSplashShowMessageSubida() {
        progressDialog?.setMessage(getString(R.string.splash_subida))
    }

    override fun onSplashShowMessageBackup() {
        progressDialog?.setMessage(getString(R.string.splash_backup))
    }

    override fun onSplashShowMessageRestore() {
        progressDialog?.setMessage(getString(R.string.splash_restore))
    }

    override fun onSplashFinished() {
        progressDialog?.dismiss()
    }

    override fun onSplashShowMessageData() {
        progressDialog?.setMessage(getString(R.string.splash_download))
    }

    override fun onSplashGoToHome(loginModel: LoginModel?) {
        // EMPTY
    }

    override fun onSplashGoToTerms(loginModel: LoginModel) {
        // EMPTY
    }

    /** Dispatcher permission */

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    internal fun showRationaleForStorage(request: PermissionRequest) {
        AlertDialog.Builder(context!!)
            .setMessage(R.string.permission_write_rationale)
            .setPositiveButton(R.string.button_aceptar) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.button_cancelar) { _, _ -> request.cancel() }
            .setCancelable(false)
            .show()
    }


    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    internal fun showDeniedForStorage() {
        this@LoginFragment.activity!!.finish()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    internal fun showNeverAskForStorage() {
        Toast.makeText(context, R.string.permission_write_neverask, Toast.LENGTH_SHORT).show()
        AlertDialog.Builder(context!!)
            .setMessage(R.string.permission_write_denied)
            .setPositiveButton(R.string.button_go_to_settings) { _, _ ->
                CommunicationUtils.goToSettings(context!!)
                this@LoginFragment.activity!!.finish()
            }
            .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                dialog.dismiss()
                this@LoginFragment.activity!!.finish()
            }
            .show()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }


}
