package biz.belcorp.consultoras.feature.splash

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.model.auth.AuthModel
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.user.UserModel
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.Verificacion
import biz.belcorp.consultoras.util.Constant
import biz.belcorp.consultoras.util.anotation.CreditApplicationType
import biz.belcorp.consultoras.util.anotation.ErrorCode
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.NetworkUtil

class SplashService : Service(), SplashView {

    private var downloadType: Int = 0
    private var url: String? = null
    private lateinit var countrySIM: String
    var authModel: AuthModel? = null

    lateinit var splashListener: Listener
    lateinit var activity: BaseActivity

    // Binder given to fragments
    private val mBinder = LocalBinder()

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal// Return this instance of LocalService so clients can call public methods
        val service: SplashService
            get() = this@SplashService
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    lateinit var presenter: SplashPresenter
    lateinit var listener: Listener

    override fun context(): Context {
        return baseContext
    }

    override fun onVersionError(required: Boolean, url: String?) {
        // EMPTY
    }

    fun initScreenTrack() {
        presenter.initScreenTrack()
    }

    override fun initScreenTrack(model: User?) {
        listener.onSplashInitScreenTrack(model)
    }

    override fun checkVersion() {
        presenter.attachView(this)

        presenter.getListaMarcaciones(activity)
        countrySIM = CountryUtil.getCode(activity)
        if (countrySIM.isEmpty()) countrySIM = Constant.BRAND_COUNTRY_DEFAULT
        presenter.checkVersionWS(countrySIM)

    }

    override fun updateVersion(required: Boolean, url: String, downloadType: Int) {
        this.url = url
        this.downloadType = downloadType

        try {
            val messageDialog = MessageDialog()
            messageDialog.isCancelable = false
            messageDialog
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.splash_update_title)
                .setStringMessage(R.string.splash_message_version)
                .setStringAceptar(R.string.button_aceptar)
                .showCancel(!required)
                .showIcon(true)
                .showClose(false)
                .setListener(versionListener)

            if (!required)
                messageDialog.setStringCancelar(R.string.button_omitir)

            messageDialog.show(activity.supportFragmentManager, "modalAceptar")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalAceptar", e)
        }

    }

    private val versionListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {

            if (downloadType == 1) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse(url)
                startActivity(intent)
                System.exit(0)
            } else if (downloadType == 2) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.pe"))
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        override fun cancelar() {
            presenter.checkVersionLocal()
        }
    }

    override fun initSubida() {
        listener.onSplashShowMessageSubida()
        presenter.subida()
    }

    override fun initBackup() {
        listener.onSplashShowMessageBackup()
        presenter.backup()
    }

    override fun initRestore() {
        listener.onSplashShowMessageRestore()
        presenter.restore()
    }


    override fun showHome(model: UserModel) {
        presenter.verificacion()

    }

    override fun onVerificacionResponse(verificacion: Verificacion, loginModel: LoginModel, online: Boolean) {

        if ((verificacion.opcionVerificacionSMS == CreditApplicationType.NOT_APPLY || verificacion.opcionVerificacionSMS == CreditApplicationType.ACCEPT)
            && (verificacion.opcionVerificacionCorreo == CreditApplicationType.NOT_APPLY || verificacion.opcionVerificacionCorreo == CreditApplicationType.ACCEPT)
            && (verificacion.opcionCambioClave == CreditApplicationType.NOT_APPLY || verificacion.opcionCambioClave == CreditApplicationType.ACCEPT)
            && (loginModel.indicadorContratoCredito == CreditApplicationType.NOT_APPLY || loginModel.indicadorContratoCredito == CreditApplicationType.ACCEPT)
            && loginModel.isAceptaTerminosCondiciones) {

            listener.onSplashGoToHome(loginModel)
        } else {
            if(online) {
                listener.onSplashGoToTerms(loginModel)
            } else {
                showErrorNetwork()
            }
        }
    }

    override fun initData() {
        listener.onSplashShowMessageData()
        presenter.data()
    }

    override fun showMenu(campaign: String, revistaDigital: Int) {
        presenter.getCallMenu(countrySIM, campaign, revistaDigital)
    }


    private fun onSplashFinished() {
        listener.onSplashFinished()
    }

    override fun loadData(authModel: AuthModel) {
        this.authModel = authModel
        if (authModel.isLogged) {
            presenter.getUser()
        } else {
            presenter.saveCountry(CountryUtil.getCode(context()))
        }
    }

    override fun savedCountrySIM(status: Boolean) {
        if (authModel == null) {
            onSplashFinished()
        } else if (!authModel!!.isHasUser || NetworkUtil.isThereInternetConnection(context()))
            presenter.saveAppName()
        else
            onSplashFinished()
    }

    override fun saved(result: Boolean) {
        onSplashFinished()
    }

    fun getCurrentUser(){
        presenter.getCurrentUser()
    }

    override fun showRetry() {
        // Empty
    }

    override fun hideRetry() {
        // EMPTY
    }

    override fun showError(error: ErrorModel?) {
        if(error == null){
            listener.onSplashGoToHome(null)
            return
        }

        if (authModel != null && authModel!!.isLogged) {
            listener.onError()
            return
        }

        when (error.code) {
            ErrorCode.SQL -> showErrorSQL()
            ErrorCode.NETWORK -> if (authModel != null && authModel!!.isLogged)
                initData()
            else
                showErrorNetwork()
            else -> showRetry()
        }
    }

    override fun showBusinessError(businessError: BusinessErrorModel) {
        // empty
    }

    private fun showErrorNetwork() {
        try {
            MessageDialog()
                .setIcon(R.drawable.ic_network_error, 1)
                .setStringTitle(R.string.connection_title)
                .setStringMessage(R.string.connection_offline)
                .setStringAceptar(R.string.button_reintentar)
                .showCancel(true)
                .showIcon(true)
                .showClose(false)
                .setListener(retryListener)
                .show(activity.supportFragmentManager, "modalError")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalError", e)
        }

    }

    private fun showErrorRetry() {
        try {
            MessageDialog()
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.splash_retry_title)
                .setStringMessage(R.string.splash_retry_message_service)
                .setStringAceptar(R.string.button_reintentar)
                .showCancel(true)
                .showIcon(true)
                .showClose(false)
                .setListener(retryListener)
                .show(activity.supportFragmentManager, "modalReintentar")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalReintentar", e)
        }

    }

    private fun showErrorSQL() {
        try {
            MessageDialog()
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.splash_retry_title)
                .setStringMessage(R.string.splash_retry_message_sql)
                .setStringAceptar(R.string.button_reintentar)
                .showCancel(true)
                .showIcon(true)
                .showClose(false)
                .setListener(retryListener)
                .show(activity.supportFragmentManager, "modalReintentar")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalReintentar", e)
        }

    }

    private val retryListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {
            checkVersion()
        }

        override fun cancelar() {
            System.exit(0)
        }
    }


    interface Listener {
        fun onSplashInitScreenTrack(model: User?)
        fun onSplashShowMessageSubida()
        fun onSplashShowMessageBackup()
        fun onSplashShowMessageRestore()
        fun onSplashFinished()
        fun onSplashShowMessageData()
        fun onSplashGoToHome(loginModel: LoginModel?)
        fun onSplashGoToTerms(loginModel: LoginModel)
        fun onError()
    }

}
