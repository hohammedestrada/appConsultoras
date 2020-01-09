package biz.belcorp.consultoras.feature.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.embedded.academia.AcademiaActivity
import biz.belcorp.consultoras.feature.announcement.AnnouncementActivity
import biz.belcorp.consultoras.feature.auth.di.AuthComponent
import biz.belcorp.consultoras.feature.auth.di.DaggerAuthComponent
import biz.belcorp.consultoras.feature.auth.login.facebook.LoginFacebookFragment
import biz.belcorp.consultoras.feature.auth.login.form.LoginFormFragment
import biz.belcorp.consultoras.feature.catalog.CatalogContainerActivity
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.accountstate.AccountStateActivity
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.embedded.offers.OffersActivity
import biz.belcorp.consultoras.feature.terms.TermsActivity
import biz.belcorp.consultoras.feature.welcome.WelcomeActivity
import biz.belcorp.consultoras.navigation.Navigator
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.consultoras.util.anotation.NotificationCode
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.library.util.NetworkUtil
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoginActivity : BaseActivity(), HasComponent<AuthComponent>,
    LoadingView, Listener {

    private var component: AuthComponent? = null

    private val consultantName: String? = null
    private val countryISO: String? = null

    fun goToLogin() {
        val fragment = LoginFragment()
        fragment.arguments = intent.extras
        replaceFragment(R.id.fltContainer, fragment, true)
    }


    fun goToLoginForm() {
        val fragment = LoginFormFragment()
        fragment.arguments = intent.extras
        replaceFragment(R.id.fltContainer, fragment, false)
    }

    override fun onFinish() {
        setResult(Activity.RESULT_OK, Intent().apply { putExtras(Bundle().apply { putBoolean("LogueoConsultora", true) }) })
        finish()
    }

    override fun onFinishError() {
        setResult(Activity.RESULT_OK, Intent().apply { putExtras(Bundle().apply { putBoolean("LogueoConsultora", false) }) })
        finish()
    }

    fun goToRegistration(facebookProfile: FacebookProfileModel) {
        this.navigator.navigateToRegistration(this, facebookProfile, intent.extras)
        finish()
    }

    fun goToHome() {
        var extras = intent.extras
        if (extras != null) {
            val notificationCode = extras.getString(GlobalConstant.NOTIFICATION_CODE)
            if (notificationCode != null) {
                val intent: Intent
                when (notificationCode) {
                    NotificationCode.ORDER -> {
                        val optionOrder = extras.getInt(GlobalConstant.NOTIFICATION_ORDER_OPTION_CODE, 0)
                        intent = when (optionOrder) {
                            1 -> Intent(this, OrderWebActivity::class.java)
                            2 -> Intent(this, AddOrdersActivity::class.java)
                            else -> Intent(this, HomeActivity::class.java)
                        }
                    }
                    NotificationCode.OFFERS -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.OFFERS)
                    }
                    NotificationCode.OFFERS_SHOWROOM -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.SHOW_ROOM)
                    }
                    NotificationCode.OFFERS_NEW_NEW -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.LO_NUEVO_NUEVO)
                    }
                    NotificationCode.OFFERS_FOR_YOU -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.OFERTAS_PARA_TI)
                    }
                    NotificationCode.OFFERS_ONLY_TODAY -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.SOLO_HOY)
                    }
                    NotificationCode.OFFERS_SALES_TOOLS -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.HERRAMIENTAS_DE_VENTA)
                    }
                    NotificationCode.OFFERS_INFORMATION -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.REVISTA_DIGITAL_INFO)
                    }
                    NotificationCode.OFFERS_THE_MOST_WINNING -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.THE_MOST_WINNING)
                    }
                    NotificationCode.OFFERS_PERFECT_DUO -> {
                        intent = Intent(this, OffersActivity::class.java)
                        intent.putExtra(OffersActivity.OPTION, PageUrlType.PERFECT_DUO)
                    }
                    NotificationCode.CATALOG -> intent = Intent(this, CatalogContainerActivity::class.java)
                    NotificationCode.ACCOUNT -> intent = Intent(this, AccountStateActivity::class.java)
                    NotificationCode.ANNOUNCEMENT -> {
                        intent = Intent(this, AnnouncementActivity::class.java)
                        intent.putExtra(AnnouncementActivity.OPTION, notificationCode)
                    }
                    NotificationCode.MY_ACADEMY -> intent = Intent(this, AcademiaActivity::class.java)
                    else -> intent = Intent(this, HomeActivity::class.java)
                }
                intent.putExtras(extras)
                startActivity(intent)
                finish()
            } else {
                extras = Bundle()
                extras.putBoolean(GlobalConstant.LOGIN_STATE, true)
                this.navigator.navigateToHome(this, extras)
            }
        } else {
            extras = Bundle()
            extras.putBoolean(GlobalConstant.LOGIN_STATE, true)
            this.navigator.navigateToHome(this, extras)
        }
    }

    private fun goToTutorial(consultantCode: String?, countryISO: String?) {
        this.navigator.navigateToTutorialWithResult(this, consultantCode, countryISO)
    }

    private fun goToTerms(indicadorContratoCredito: Int) {
        val extras = Bundle()
        extras.putInt(TermsActivity.BUNDLE_INDICADOR_CONTRATO_CREDITO, indicadorContratoCredito)
        this.navigator.navigateToTermsWithResult(this, extras)
    }

    private fun goToWelcome(loginModel: LoginModel) {
        val extras = Bundle()
        extras.putAll(intent.extras)
        extras.putBoolean(TermsActivity.ACEPTA_TERMINOS_Y_CONDICIONES, loginModel.isAceptaTerminosCondiciones)
        extras.putInt(TermsActivity.BUNDLE_INDICADOR_CONTRATO_CREDITO, loginModel.indicadorContratoCredito)
        extras.putString(WelcomeActivity.COUNTRY_ISO, loginModel.countryISO)
        extras.putInt(WelcomeActivity.USER_TYPE, loginModel.userType)
        extras.putString(WelcomeActivity.CONSULTANT_CODE, loginModel.consultantCode)
        extras.putString(WelcomeActivity.FIRST_NAME, loginModel.primerNombre)
        extras.putString(WelcomeActivity.MOBILE, loginModel.mobile)
        navigator.navigateToWelcome(this, extras)
        finish()
    }

    fun goToRecovery() {
        this.navigator.navigateToRecovery(this)
    }

    private fun goToRegisterLink(url: String) {
        this.navigator.openIntentLink(this, url)
    }

    /** */

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        init(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        if (NetworkUtil.isThereInternetConnection(this)) {
            when (ConsultorasApp.getInstance().datamiType) {
                NetworkEventType.DATAMI_AVAILABLE -> {
                    view_connection.visibility = View.VISIBLE
                    tvw_connection_message.text = getString(R.string.connection_datami_available)
                    ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
                }
                NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> view_connection.visibility = View.GONE
                else -> view_connection.visibility = View.GONE
            }
        } else {
            view_connection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    override fun onBackPressed() {
        intent?.extras?.let{
            if(it.getBoolean("AppMV", false)){
                finish()
            }else{
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    moveTaskToBack(true)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        Glide.get(this).clearMemory()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.findFragmentById(R.id.fltContainer)
        fragment?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Navigator.REQUEST_CODE_TERMS && resultCode == Activity.RESULT_OK) {
            goToTutorial(consultantName, countryISO)
        }

        if (requestCode == Navigator.REQUEST_CODE_TUTORIAL && resultCode == Activity.RESULT_OK) {
            goToHome()
        }
    }

    /** */

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkEvent(event: NetworkEvent) {
        when (event.event) {
            NetworkEventType.CONNECTION_NOT_AVAILABLE -> {
                view_connection.visibility = View.VISIBLE
                tvw_connection_message.setText(R.string.connection_offline)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                view_connection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> view_connection.visibility = View.GONE
            else -> view_connection.visibility = View.GONE
        }
    }

    /** */

    override fun init(savedInstanceState: Bundle?) {
        initializeInjector()

        val fromLoginForm = intent.getBooleanExtra(Navigator.NAVIGATE_FROM_LOGIN_FORM, false)
        val fromLoginFacebook = intent.getBooleanExtra(Navigator.NAVIGATE_FROM_LOGIN_FACEBOOK, false)

        if (savedInstanceState == null) {
            val login: Fragment

            if (fromLoginForm) {
                login = LoginFormFragment()
            } else if (fromLoginFacebook) {
                login = LoginFacebookFragment()
            } else {
                login = LoginFragment()
            }
            login.arguments = intent.extras
            addFragment(R.id.fltContainer, login)
        }
    }

    override fun initializeInjector() {
        this.component = DaggerAuthComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    override fun initControls() {
        // EMPTY
    }

    override fun initEvents() {
        // EMPTY
    }

    /** */

    override fun showLoading() {
        view_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        view_loading.visibility = View.GONE
    }


    /** */

    override fun getComponent(): AuthComponent? {
        return component
    }

    /** */

    override fun openRegisterLink(url: String) {
        goToRegisterLink(url)
    }

    override fun openRegistration(facebookProfile: FacebookProfileModel?) {
        facebookProfile?.let { goToRegistration(it) }
    }

    override fun openLoginForm() {
        goToLoginForm()
    }

    override fun openLogin() {
        goToLogin()
    }

    override fun openRecovery() {
        goToRecovery()
    }

    override fun onHome() {
        goToHome()
    }

    override fun onTerms(model: LoginModel) {
        goToWelcome(model)
    }


    override fun onTutorial(consultantCode: String, countryISO: String) {
        goToTutorial(consultantCode, countryISO)
    }

    companion object {

        /** */

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
