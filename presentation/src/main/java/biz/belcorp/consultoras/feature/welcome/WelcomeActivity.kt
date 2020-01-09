package biz.belcorp.consultoras.feature.welcome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.view.View
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sms.SMSEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.Verificacion
import biz.belcorp.consultoras.feature.home.profile.password.ChangePasswordFragment
import biz.belcorp.consultoras.feature.sms.SMSActivity
import biz.belcorp.consultoras.feature.sms.SMSFragment
import biz.belcorp.consultoras.feature.terms.TermsActivity
import biz.belcorp.consultoras.feature.terms.TermsFragment
import biz.belcorp.consultoras.feature.vinculacion.VinculacionFragment
import biz.belcorp.consultoras.feature.welcome.di.DaggerWelcomeComponent
import biz.belcorp.consultoras.feature.welcome.di.WelcomeComponent
import biz.belcorp.consultoras.navigation.Navigator
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.CreditApplicationType
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.consultoras.util.anotation.UserType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.toolbar_black.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WelcomeActivity : BaseActivity(), HasComponent<WelcomeComponent>, LoadingView
    , WelcomeFragment.WelcomeFragmentListener, SMSFragment.SMSFragmentListener
    , ChangePasswordFragment.ChangePasswordListener, VinculacionFragment.VinculacionListener
    , TermsFragment.Listener{

    private var component: WelcomeComponent? = null
    private var smsFragment: SMSFragment? = null
    private var indicadorContratoCredito: Int? = null
    private var aceptaTerminosYCondiciones: Boolean? = null
    private var verifcacion: Verificacion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        init(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (NetworkUtil.isThereInternetConnection(this))
            setStatusTopNetwork()
        else {
            view_connection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSyncEvent(event: SMSEvent) {
        smsFragment?.autocompleteCode(event.body)
    }



    override fun onBackPressed() {
         super.onBackPressed()
         finishAffinity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.findFragmentById(R.id.fltContainer)
        fragment?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Navigator.REQUEST_CODE_TUTORIAL && resultCode == Activity.RESULT_OK) {
            navigator.navigateToHome(this@WelcomeActivity, null)
            finish()
        }
    }


    /** */

    override fun init(savedInstanceState: Bundle?) {
        initializeInjector()
        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.empty)
        toolbar.setNavigationOnClickListener {  }

        lltContent.removeAllViews()
        indicadorContratoCredito = intent.extras?.getInt(TermsActivity.BUNDLE_INDICADOR_CONTRATO_CREDITO)
        aceptaTerminosYCondiciones = intent.extras?.getBoolean(TermsActivity.ACEPTA_TERMINOS_Y_CONDICIONES)
        val welcomeFragment = WelcomeFragment()
        welcomeFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().replace(R.id.lltContent, welcomeFragment).commit()
        rlt_leyenda.visibility = View.GONE
    }

    override fun initializeInjector() {
        component = DaggerWelcomeComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initializeToolbar() {
        tvw_toolbar_title.text = getString(R.string.welcome_title_1)
    }

    override fun onSms(model: LoginModel, verificacion: Verificacion) {
        tvw_toolbar_title.text = getString(R.string.welcome_title_2)
        rlt_leyenda.visibility = View.VISIBLE
        ivw_welcome_cell.visibility = View.VISIBLE
        smsFragment = SMSFragment()
        var arguments = Bundle()
        intent?.let{intentValidate ->
            intentValidate?.extras?.let{extrasValidate ->
                arguments.putString(SMSActivity.EXTRA_NEW_PHONE_NUMBER, extrasValidate.getString(WelcomeActivity.MOBILE))
                arguments.putString(SMSActivity.EXTRA_PHONE_NUMBER, extrasValidate.getString(WelcomeActivity.MOBILE))
            }
        }

        arguments.putString(SMSActivity.EXTRA_COUNTRY_ISO, model.countryISO)
        arguments.putString(SMSActivity.EXTRA_CAMPAING, model.campaing)
        arguments.putBoolean(SMSActivity.FROM_WELCOME, true)
        verificacion.origenID?.let { arguments.putInt(SMSActivity.EXTRA_ORIGEN_ID, it) }
        arguments.putString(SMSActivity.EXTRA_ORIGEN_DESCRIPCION, verificacion.origenDescripcion)
        verificacion.idEstadoActividad?.let { arguments.putInt(SMSActivity.EXTRA_ID_ESTADO_ACTIVIDAD, it) }
        smsFragment?.arguments = arguments

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        animateToRight(fragmentTransaction)
        fragmentTransaction.replace(R.id.lltContent, smsFragment).commit()
    }

    override fun onPhoneConfirm(type: Int) {
        rlt_leyenda.visibility = View.VISIBLE
        when(verifcacion?.opcionCambioClave){
            CreditApplicationType.NOT_APPLY -> {
                onPasswordSaved()
            }
            CreditApplicationType.NOT_ACCEPT -> {
                tvw_toolbar_title.text = getString(R.string.welcome_title_3)
                select2from1()
                val changePasswordFragment = ChangePasswordFragment()
                val args = Bundle()
                args.putBoolean(ChangePasswordFragment.ENABLE_OLD_PSW, false)
                changePasswordFragment.arguments = args
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                animateToRight(fragmentTransaction)
                fragmentTransaction.replace(R.id.lltContent, changePasswordFragment).commit()
            }
            CreditApplicationType.ACCEPT -> {
                onPasswordSaved()
            }
        }


    }

    override fun setVerificacion(verificacion: Verificacion) {
        this.verifcacion = verificacion
    }

    override fun onPhoneNewChange() {

    }

    override fun onPasswordSaved() {
        rlt_leyenda.visibility = View.VISIBLE
        tvw_toolbar_title.text = getString(R.string.welcome_title_4)
        when(indicadorContratoCredito){
            CreditApplicationType.NOT_APPLY -> {
                select4from2()
                onTerms()
            }
            CreditApplicationType.NOT_ACCEPT -> {
                select3from2()
                var vinculacionFragment = VinculacionFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                animateToRight(fragmentTransaction)
                fragmentTransaction.replace(R.id.lltContent, vinculacionFragment).commit()
            }
            CreditApplicationType.ACCEPT -> {
                ivw_welcome_validation.setImageResource(R.drawable.ic_welcome_check)
                onTerms()
            }
        }

    }

    override fun onTerms() {
        rlt_leyenda.visibility = View.VISIBLE
        when(aceptaTerminosYCondiciones){
            false -> {
                select4from3()
                var termsFragment = TermsFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                animateToRight(fragmentTransaction)
                fragmentTransaction.replace(R.id.lltContent, termsFragment).commit()
            }
            true-> {
                onHome()
            }
        }
    }

    fun animateToRight(fragmentTransaction: FragmentTransaction) {
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onHome() {
        intent?.let{intentValidate ->
            intentValidate.extras?.let{extrasValidate ->
                extrasValidate.getInt(WelcomeActivity.USER_TYPE, UserType.CONSULTORA)?.let{
                    when(it){
                        UserType.CONSULTORA -> {
                            rltMain.visibility = View.GONE
                            tvw_toolbar_title.text = StringUtil.Empty
                            toolbar.visibility = View.GONE

                            var title = extrasValidate.getString(FIRST_NAME)?:StringUtil.Empty

                            FullScreenDialog.Builder(this)
                                .withTitle("¡BIENVENID@ " + title.toUpperCase() + "!")
                                .withMessage("Ya estás list@ para empezar a vivir tu experiencia " +
                                    "como consultora en tu APP "+getString(R.string.app_name_formatted))
                                .withIcon(R.drawable.ic_confeti_white)
                                .withIconAnimation()
                                .withAnimation(this@WelcomeActivity.resources, FullScreenDialog.SIMPLE_ANIMATION
                                    , ContextCompat.getColor(this, R.color.primary)
                                    , ContextCompat.getColor(this, R.color.primary_dark))
                                .withButtonMessage(resources.getString(R.string.button_aceptar))
                                .setOnItemClick(object : FullScreenDialog.FullScreenDialogListener {
                                    override fun onDismiss() {
                                        goToHomeOrTutorial()
                                    }

                                    override fun onClickAction(dialog: FullScreenDialog) {
                                        //Empty
                                    }

                                    override fun onClickAceptar(dialog: FullScreenDialog) {
                                        goToHomeOrTutorial()
                                    }
                                })
                                .show()
                        }
                        UserType.POSTULANTE -> {
                            if(extrasValidate.getBoolean("AppMV", false)) {
                                setResult(Activity.RESULT_OK, Intent().apply { putExtras(Bundle().apply { putBoolean("LogueoConsultora", true) }) })
                            } else {
                                navigator.navigateToHome(this@WelcomeActivity, null)
                            }
                            finish()
                        }
                    }
                }


            }
        }
    }

    private fun goToHomeOrTutorial() {
        val sessionManager = SessionManager.getInstance(this@WelcomeActivity)
        intent?.extras?.let{extras ->
            if(extras.getBoolean("AppMV", false)) {
                setResult(Activity.RESULT_OK, Intent().apply { putExtras(Bundle().apply { putBoolean("LogueoConsultora", true) }) })
                finish()
            } else{
                sessionManager.isTutorial(extras.getString(CONSULTANT_CODE))?.let {
                    if (it) {
                        navigator.navigateToTutorialWithResult(this@WelcomeActivity,
                            extras.getString(CONSULTANT_CODE)
                            , extras.getString(COUNTRY_ISO))
                    } else {
                        navigator.navigateToHome(this@WelcomeActivity, null)
                    }
                }
            }
        }

    }


    override fun configIcons(verificacion: Verificacion) {
        when(verificacion.opcionVerificacionSMS){
            CreditApplicationType.NOT_APPLY -> {
                ivw_welcome_cell.visibility = View.GONE
            }
            CreditApplicationType.NOT_ACCEPT -> {
                ivw_welcome_cell.setImageResource(R.drawable.ic_welcome_cell)
            }
            CreditApplicationType.ACCEPT -> {
                ivw_welcome_cell.visibility = View.GONE
            }
        }
        when(verificacion.opcionCambioClave){
            CreditApplicationType.NOT_APPLY -> {
                ivw_welcome_lock.visibility = View.GONE
            }
            CreditApplicationType.NOT_ACCEPT -> {
                ivw_welcome_lock.setImageResource(R.drawable.ic_welcome_lock_off)
            }
            CreditApplicationType.ACCEPT -> {
                ivw_welcome_lock.visibility = View.GONE
            }
        }
        when(indicadorContratoCredito){
            CreditApplicationType.NOT_APPLY -> {
                ivw_welcome_validation.visibility = View.GONE
            }
            CreditApplicationType.NOT_ACCEPT -> {
                ivw_welcome_validation.setImageResource(R.drawable.ic_welcome_validation_off)
            }
            CreditApplicationType.ACCEPT -> {
                ivw_welcome_validation.visibility = View.GONE
            }
        }
        when(aceptaTerminosYCondiciones){
            false -> {
                ivw_welcome_terms.setImageResource(R.drawable.ic_welcome_terms_off)
            }
            true-> {
                //ivw_welcome_cell.visibility = View.GONE
                ivw_welcome_terms.visibility = View.GONE
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkEvent(event: NetworkEvent) {
        when (event.event) {
            NetworkEventType.CONNECTION_AVAILABLE -> {
                SyncUtil.triggerRefresh()
                setStatusTopNetwork()
            }
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

    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.DATAMI_AVAILABLE -> {
                view_connection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> view_connection.visibility = View.GONE
            else -> view_connection.visibility = View.GONE
        }
    }

    override fun initControls() {

    }

    override fun initEvents() {
        // EMPTY
    }

    override fun showLoading() {
        if (null != view_loading) view_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        if (null != view_loading) view_loading.visibility = View.GONE
    }

    override fun getComponent(): WelcomeComponent? {
        return component
    }

    /** */
    fun select2from1(){
        ivw_welcome_cell.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_lock.setImageResource(R.drawable.ic_welcome_lock)
    }

    fun select3from2(){
        ivw_welcome_cell.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_lock.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_validation.setImageResource(R.drawable.ic_welcome_validation)
    }

    fun select4from2(){
        ivw_welcome_cell.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_lock.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_terms.setImageResource(R.drawable.ic_welcome_terms)
    }

    fun select4from3(){
        ivw_welcome_cell.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_lock.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_validation.setImageResource(R.drawable.ic_welcome_check)
        ivw_welcome_terms.setImageResource(R.drawable.ic_welcome_terms)
    }

    companion object {
        const val COUNTRY_ISO = "COUNTRY_ISO"
        const val USER_TYPE = "USER_TYPE"
        const val CONSULTANT_CODE = "CONSULTANT_CODE"
        const val FIRST_NAME = "FIRST_NAME"
        const val MOBILE = "MOBILE"
    }

}
