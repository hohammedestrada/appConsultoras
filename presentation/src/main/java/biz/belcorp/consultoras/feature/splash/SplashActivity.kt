package biz.belcorp.consultoras.feature.splash

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.Window
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.fcm.HybrisManager
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.splash.di.DaggerSplashComponent
import biz.belcorp.consultoras.feature.splash.di.SplashComponent
import biz.belcorp.consultoras.feature.terms.TermsActivity
import biz.belcorp.consultoras.feature.welcome.WelcomeActivity
import biz.belcorp.consultoras.navigation.Navigator
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import com.bumptech.glide.Glide
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.sap.cec.marketing.ymkt.mobile.configuration.Boot
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SplashActivity : BaseActivity(), HasComponent<SplashComponent>, SplashFragment.SplashFragmentListener {

    private var component: SplashComponent? = null

    lateinit var splashFragment: SplashFragment

    fun goToLogin() {
        val extras = intent.extras
        this.navigator.navigateToLogin(this, extras)
        this.finish()
    }

    fun goToFacebook() {
        val extras = intent.extras
        this.navigator.navigateToLoginFacebook(this, extras)
        this.finish()
    }

    fun goToTutorial() {
        // EMPTY
    }

    fun goToHome() {
        this.navigator.navigateToHome(this, null)
        this.finish()
    }

    fun goToHome(extras: Bundle) {
        this.navigator.navigateToHome(this, extras)
        this.finish()
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
        setContentView(R.layout.activity_splash)

        init(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
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

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Navigator.REQUEST_CODE_TERMS && resultCode == RESULT_OK) {
            goToHome()
        }
    }

    override fun onBackPressed() {
        // EMPTY
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
        initAppCenter()
        if (savedInstanceState == null) {
            clearCacheGlide()
            splashFragment = SplashFragment()
            splashFragment.arguments = intent.extras
            addFragment(R.id.fltContainer, splashFragment)
        }
        //hybris
        try {
            Boot.setupHybrisMarketing(HybrisManager.getInstance().getConfiguration(applicationContext))
        } catch (e: Exception) {
            Log.e("hybris", "ERROR", e)
        }
    }

    private fun initAppCenter() {
        AppCenter.start(application, "0b9431eb-5cc5-4457-8e3f-236b7b38118e",
                Analytics::class.java, Crashes::class.java)
    }

    override fun initializeInjector() {
        this.component = DaggerSplashComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    override fun initControls() {
        // Empty
    }

    override fun initEvents() {
        // Empty
    }

    override fun getComponent(): SplashComponent? {
        return component
    }

    /** */

    override fun onLogin() {
        goToLogin()
    }

    override fun onFacebook() {
        goToFacebook()
    }

    override fun onTutorial() {
        goToTutorial()
    }

    override fun onHome() {
        goToHome()
    }

    override fun onHome(extras: Bundle) {
        goToHome(extras)
    }

    override fun onTerms(loginModel: LoginModel) {
        val extras = Bundle()
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

    companion object {

        /** */

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
        }
    }

    fun clearCacheGlide(){
        AsyncTask.execute {
                Glide.get(this).clearDiskCache()
        }
    }

}
