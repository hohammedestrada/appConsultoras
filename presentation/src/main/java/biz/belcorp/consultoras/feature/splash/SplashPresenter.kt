package biz.belcorp.consultoras.feature.splash

import android.content.Context
import android.util.Log
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.AuthModelDataMapper
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.exception.ErrorFactory
import biz.belcorp.consultoras.util.Constant
import biz.belcorp.library.util.VersionUtil
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@PerActivity
class SplashPresenter @Inject
internal constructor(private val menuUseCase: MenuUseCase,
                     private val origenMarcacionUseCase:OrigenMarcacionUseCase,
                     private val userUseCase: UserUseCase,
                     private val authUseCase: AuthUseCase,
                     private val configUseCase: ConfigUseCase,
                     private val configExtUseCase: ConfigExtUseCase,
                     private val sessionUseCase: SessionUseCase,
                     private val backupUseCase: BackupUseCase,
                     private val syncUseCase: SyncUseCase,
                     private val authModelDataMapper: AuthModelDataMapper,
                     private val userModelDataMapper: UserModelDataMapper,
                     private val loginModelDataMapper: LoginModelDataMapper,
                     private val accountUseCase: AccountUseCase,
                     private val festivalUseCase: FestivalUseCase) : Presenter<SplashView> {

    private var splashView: SplashView? = null

    private lateinit var mContext: Context

    override fun attachView(view: SplashView) {
        splashView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.origenMarcacionUseCase.dispose()
        this.menuUseCase.dispose()
        this.userUseCase.dispose()
        this.authUseCase.dispose()
        this.configUseCase.dispose()
        this.configExtUseCase.dispose()
        this.sessionUseCase.dispose()
        this.backupUseCase.dispose()
        this.syncUseCase.dispose()
        this.accountUseCase.dispose()
        this.splashView = null
    }

    /**
     * CUS 1:   Se valida si existe algun usuario authentificado.
     * Esto es importante para determinar si el flujo se cancelara o dejara continuar
     */
    fun data() {
        userUseCase.cleanData(CleanDataObserver())
    }

    /**
     * CUS 2:   Se procede a guardar el pais que ha sido detectado por el SIM, en caso se
     * devolviera nulo o error debe dejar continuar el siguiente CUS.
     *
     * @param country Pais obtenido del SIM
     */
    internal fun saveCountry(country: String) {
        this.sessionUseCase.saveCountrySIM(country, SaveCountrySIMObserver())
    }

    /**
     * CUS 3:   Se procede a obtener la configuración de la aplicación.
     * En caso el servicio fallara y no esta autentificado se bloqueara el ingreso
     * de lo contrario dejara continuar el flujo normalmente.
     */
    internal fun saveAppName() {
        this.configUseCase.saveAppName(BuildConfig.APP_NAME, SaveAppNameObserver())
    }

    /**
     * CUS 5:   Se valida si existe alguna versión superior con la de aplicación.
     * Esto es importante para determinar si el flujo se cancelara o dejara continuar
     */
    internal fun checkVersionWS(countrySIM: String?) {
        this.configUseCase[VersionWSObserver(countrySIM)]
    }

    fun getConfiguracionfestival(){
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){
                try {
                    festivalUseCase.getConfiguracion(1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    internal fun checkVersionLocal() {
        this.sessionUseCase.getVersion(GetVersionLocalObserver())
    }

    /**
     * CUS 8:   Sincroniza los registros no sincronizados.
     * Esto es importante para determinar si el flujo se cancelara o dejara continuar
     */
    internal fun subida() {
        this.syncUseCase.syncClientsData(SubidaObserver())
    }

    /**
     * CUS 9:   Realiza un backup de todas las tablas de la base de datos.
     * Esto es importante para determinar si el flujo se cancelara o dejara continuar
     */
    internal fun backup() {
        this.backupUseCase.backup(BackupObserver())
    }

    /**
     * CUS 10:   Borra y restaura las tablas de la base de datos.
     * Esto es importante para determinar si el flujo se cancelara o dejara continuar
     */
    private fun reset() {
        this.backupUseCase.reset(ResetObserver())
    }

    /**
     * CUS 11:   Restaura los datos del backup en la base de datos.
     * Esto es importante para determinar si el flujo se cancelara o dejara continuar
     */
    internal fun restore() {
        this.backupUseCase.restore(RestoreObserver())
    }

    /**
     * CUS 13:   Se procede a obtener el valor del menú por única vez, en caso se
     * devolviera nulo o error debe dejar continuar el siguiente CUS.
     */
    internal fun getCallMenu(countrySIM: String, campaign: String, revistaDigital: Int) {
        this.menuUseCase.getAndSave(countrySIM, campaign, revistaDigital, GetSaveMenuObserver(countrySIM))
    }

    internal fun verificacion() {
        accountUseCase.getUpdatedData(RefreshDataObserver())
    }

    internal fun getUser() {
        userUseCase[UserObserver()]
    }

    internal fun getCurrentUser() {
        userUseCase[GetUserObserver()]
    }

    internal fun initScreenTrack() {
        authUseCase.data(AuthObserver())
    }

    /** Observers */

    private inner class CleanDataObserver : BaseObserver<Boolean>() {
        override fun onNext(t: Boolean) {
            authUseCase.data(DataObserver())
        }

        override fun onError(exception: Throwable) {
            authUseCase.data(DataObserver())
        }
    }

    private inner class GetSaveMenuObserver internal constructor(private val countrySIM: String)
        : BaseObserver<Boolean?>() {

        override fun onNext(t: Boolean?) {
            if (t!!)
                sessionUseCase.saveCallMenu(SaveCallMenuObserver(countrySIM))

        }

        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }
    }

    private inner class SaveCallMenuObserver(val countrySIM: String) : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            sessionUseCase.saveCountrySIM(countrySIM, SaveCountrySIMObserver())
        }

        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }
    }

    private inner class SaveCountrySIMObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            splashView?.savedCountrySIM(t)
        }

        override fun onError(exception: Throwable) {
            splashView?.savedCountrySIM(false)
        }

    }

    private inner class DataObserver : BaseObserver<Auth?>() {
        override fun onNext(t: Auth?) {
            splashView?.loadData(authModelDataMapper.transform(t)!!)
        }

        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }
    }

    private inner class DataV2Observer : BaseObserver<Auth?>() {
        override fun onNext(t: Auth?) {
            if (null != t && t.isLogged)
                splashView?.initSubida()
            else
                splashView?.initBackup()

        }

        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }
    }

    private inner class SaveVersionObserver : BaseObserver<Boolean>() {
        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }

        override fun onComplete() {
            splashView?.initData()
        }
    }

    private inner class VersionWSObserver(val countrySIM: String?) : BaseObserver<ConfigReponse?>() {

        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))

        }

        override fun onNext(t: ConfigReponse?) {

            t?.let {
                var oApp: App? = null

                it.apps?.forEach { apps ->
                    if (apps?.pais == countrySIM
                        && apps?.aplicacion?.toLowerCase()?.contains(BuildConfig.FLAVOR.toLowerCase()) == true) {
                        oApp = apps
                        return@forEach
                    }
                }

                if (oApp == null) oApp = getCountryEmpty(it.apps)

                oApp?.let { app ->
                    when {
                        VersionUtil.checkForUpdate(BuildConfig.VERSION_APP, app.minimaVersion) -> splashView?.updateVersion(true, app.url!!, app.tipoDescarga)
                        VersionUtil.checkForUpdate(BuildConfig.VERSION_APP, app.version) -> splashView?.updateVersion(app.isRequiereActualizacion, app.url!!, app.tipoDescarga)
                        else -> sessionUseCase.getVersion(GetVersionLocalObserver())
                    }
                } ?: sessionUseCase.getVersion(GetVersionLocalObserver())

            }
        }
    }

    private inner class GetVersionLocalObserver : BaseObserver<String>() {

        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }

        override fun onNext(t: String) {

            if (t.isEmpty() || VersionUtil.checkForUpdate(t, BuildConfig.VERSION_APP)) {
                authUseCase.data(DataV2Observer())
                //splashView.onSplashShowMessageSubida();
                //splashView.onSplashShowMessageBackup();
                //authUseCase.logoutWithData(new Logout());
            } else {
                splashView?.initData()
            }

        }
    }

    private inner class AnalyticsListObserver: BaseObserver<ConfigExtReponse?>() {

        override fun onError(exception: Throwable) {
            requestUpdateTokenForAnalytics()
        }

        override fun onNext(t: ConfigExtReponse?) {}

    }

    private inner class BackupObserver : BaseObserver<Boolean?>() {
        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }

        override fun onComplete() {
            reset()
        }
    }

    private inner class ResetObserver : BaseObserver<Boolean?>() {
        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }

        override fun onComplete() {
            splashView?.initRestore()
        }
    }

    private inner class SubidaObserver : BaseObserver<Boolean>() {
        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }

        override fun onComplete() {
            splashView?.initBackup()
        }
    }

    private inner class RestoreObserver : BaseObserver<Boolean?>() {
        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }

        override fun onComplete() {
            sessionUseCase.saveVersion(SaveVersionObserver(), BuildConfig.VERSION_APP)
        }
    }

    private inner class SaveAppNameObserver : BaseObserver<Boolean>() {
        override fun onNext(t: Boolean) {
            splashView?.saved(t)
        }

        override fun onError(exception: Throwable) {
            splashView?.showError(ErrorFactory.create(exception))
        }
    }

    private inner class UserObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            val userModel = userModelDataMapper.transform(t)
            splashView?.showMenu(t?.campaing!!, userModel!!.revistaDigitalSuscripcion)
        }
    }

    private inner class GetUserObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            val userModel = userModelDataMapper.transform(t)
            splashView?.showHome(userModel)
        }

    }

    private inner class AuthObserver : BaseObserver<Auth?>() {
        override fun onNext(t: Auth?) {
            if (null != t && t.isLogged) {
                userUseCase[UserPropertyObserver()]
            } else
                splashView?.initScreenTrack(null)

        }

        override fun onError(exception: Throwable) {
            splashView?.initScreenTrack(null)
        }
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let { splashView?.initScreenTrack(it) }
        }
    }

    private inner class RefreshDataObserver : BaseObserver<Login?>() {

        override fun onNext(t: Login?) {
            accountUseCase.verificacion(VerificacionObserver(loginModelDataMapper.transform(t)))
        }

        override fun onError(exception: Throwable) {
            accountUseCase.getLoginData(GetLoginDataObserver())
        }

    }

    private inner class GetLoginDataObserver : BaseObserver<Login?>() {

        override fun onNext(t: Login?) {
            accountUseCase.verificacion(VerificacionObserver(loginModelDataMapper.transform(t)))
        }

    }

    private inner class VerificacionObserver(val loginModel: LoginModel?)
        : BaseObserver<Verificacion?>() {

        override fun onNext(t: Verificacion?) {
            t?.let { verificacion ->
                loginModel?.let { loginModel ->
                    splashView?.onVerificacionResponse(verificacion, loginModel, true)
                }
            }
        }

        override fun onError(exception: Throwable) {
            accountUseCase.verificacionOffline(VerificacionOfflineObserver(loginModel))
        }
    }

    private inner class VerificacionOfflineObserver(val loginModel: LoginModel?)
        : BaseObserver<Verificacion?>() {

        override fun onNext(t: Verificacion?) {
            t?.let { verificacion ->
                loginModel?.let { loginModel ->
                    splashView?.onVerificacionResponse(verificacion, loginModel, false)
                }
            }
        }

    }

    fun getListaMarcaciones(context: Context){

        mContext = context
        val session = SessionManager.getInstance(context)
        val token = session.getTokenAnalytics()

        token?.let {
            getConfiguracionListas(token)
        } ?: run {
            requestUpdateTokenForAnalytics()
        }

    }

    private fun getConfiguracionListas(token:String) {

        GlobalScope.launch(Dispatchers.IO) {

            try {

                configExtUseCase.getWithCoroutines(token)

            } catch (e: Exception) {

                Log.e("getConfiguracionListas", e.message)

                requestUpdateTokenForAnalytics()

            }

        }

    }

    private fun requestUpdateTokenForAnalytics() {

        val remoteConfig = FirebaseRemoteConfig.getInstance()

        remoteConfig.fetch().addOnCompleteListener{task->

            if (task.isSuccessful) {
                remoteConfig.activateFetched()
            }

            GlobalScope.launch(Dispatchers.IO) {

                try {

                    val session = SessionManager.getInstance(mContext)

                    val clientId = remoteConfig.getString(BuildConfig.REMOTE_CONFIG_CLIENT_ID)
                    val clientSecret = remoteConfig.getString(BuildConfig.REMOTE_CONFIG_CLIENT_SECRET)

                    val result = authUseCase.getAnalyticsToken(CLIENT_CRED, clientId, clientSecret)

                    result?.accessToken?.let { token ->

                        session.saveTokenAnalytics(token)
                        getConfiguracionListas(token)

                    }

                } catch (e: Exception) {}

            }

        }

    }

    companion object {

        const val CLIENT_CRED = "client_credentials"

        private fun getCountryEmpty(apps: List<App?>?): App? {
            apps?.forEach { oApp ->
                if (oApp?.pais == Constant.BRAND_COUNTRY_DEFAULT
                    && oApp.aplicacion!!.toLowerCase().contains(BuildConfig.FLAVOR.toLowerCase())) {
                    return oApp
                }
            }
            return null
        }
    }
}
