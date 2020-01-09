package biz.belcorp.consultoras.feature.auth.registration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.auth.di.AuthComponent
import biz.belcorp.consultoras.feature.auth.di.DaggerAuthComponent
import biz.belcorp.consultoras.feature.terms.TermsActivity
import biz.belcorp.consultoras.feature.welcome.WelcomeActivity
import biz.belcorp.consultoras.navigation.Navigator
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.view_connection.*

class RegistrationActivity : BaseActivity(), HasComponent<AuthComponent>, LoadingView, RegistrationFragment.RegistrationFragmentListener {

    private var component: AuthComponent? = null

    fun goToLogin() {
        val extras = intent.extras
        this.navigator.navigateToLogin(this, extras)
    }

    fun goToHome() {
        val extras = Bundle()
        extras.putBoolean(GlobalConstant.LOGIN_STATE, true)
        this.navigator.navigateToHome(this, extras)
    }

    private fun goToTerms(loginModel: LoginModel) {
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

    private fun goToTutorial(consultantCode: String, countryISO: String) {
        this.navigator.navigateToTutorialWithResult(this, consultantCode, countryISO)
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
        setContentView(R.layout.activity_registration)
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

    override fun onBackPressed() {
        super.onBackPressed()
        goToLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Navigator.REQUEST_CODE_TERMS && resultCode == Activity.RESULT_OK) {
            goToHome()
        }
        if (requestCode == Navigator.REQUEST_CODE_TUTORIAL) {
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
        initializeToolbar()
        if (savedInstanceState == null) {
            val registrationFragment = RegistrationFragment()
            registrationFragment.arguments = intent.extras
            addFragment(R.id.fltContainer, registrationFragment)
        }
    }

    override fun initializeInjector() {
        this.component = DaggerAuthComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    private fun initializeToolbar() {
        val tvwToolbarTitle = toolbar.findViewById<TextView>(R.id.tvw_toolbar_title)
        tvwToolbarTitle.text = getString(R.string.registration_title)
        toolbar.setNavigationOnClickListener {
            val fragment = visibleFragment

            if (fragment != null)
                (fragment as RegistrationFragment).trackBackPressed()

            onBackPressed()
        }
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

    override fun onHome() {
        goToHome()
    }

    override fun onTerms(loginModel: LoginModel) {
        goToTerms(loginModel)
    }

    override fun onTutorial(consultantCode: String, countryISO: String) {
        goToTutorial(consultantCode, countryISO)
    }

    override fun onFinish() {
        setResult(Activity.RESULT_OK, Intent().apply { putExtras(Bundle().apply { putBoolean("LogueoConsultora", true) }) })
        finish()
    }

    override fun onFinishError() {
        setResult(Activity.RESULT_OK, Intent().apply { putExtras(Bundle().apply { putBoolean("LogueoConsultora", false) }) })
        finish()
    }

    companion object {

        /** */

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, RegistrationActivity::class.java)
        }
    }
}
