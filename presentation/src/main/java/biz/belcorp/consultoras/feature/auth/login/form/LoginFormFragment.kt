package biz.belcorp.consultoras.feature.auth.login.form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.adapter.CountryAdapter
import biz.belcorp.consultoras.common.dialog.ListDialog
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.dialog.Tooltip
import biz.belcorp.consultoras.common.model.auth.CredentialsModel
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.country.CountryModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.dto.AuthErrorDto
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.Verificacion
import biz.belcorp.consultoras.feature.auth.di.AuthComponent
import biz.belcorp.consultoras.feature.auth.login.Listener
import biz.belcorp.consultoras.navigation.Navigator
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.KinesisManager
import biz.belcorp.consultoras.util.anotation.AuthType
import biz.belcorp.consultoras.util.anotation.CreditApplicationType
import biz.belcorp.consultoras.util.anotation.ErrorCode
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.notification.TipoIngreso
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.KeyboardUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_login_form.*
import javax.inject.Inject

/** Class Formulario Login */
class LoginFormFragment : BaseFragment(), LoginFormView {

    @Inject
    lateinit var presenter: LoginFormPresenter

    private var countries: List<CountryModel>? = null
    private var countryAdapter: CountryAdapter? = null
    private var countrySelected: CountryModel? = null
    private var kinesisManager: KinesisManager? = null

    private var visiblePassword = false
    private var fromTermsTutorial: Boolean = false

    private var listener: Listener? = null


    private val listDialogListener: ListDialog.ListDialogListener = ListDialog
        .ListDialogListener { position -> setSelectedCountry(countryAdapter!!.getItem(position) as CountryModel) }

    /** */

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(AuthComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter?.attachView(this)

        if (savedInstanceState == null) {
            init()
        }
    }

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_login_form, container, false)

        ButterKnife.bind(this, v)

        v.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            v.getWindowVisibleDisplayFrame(r)
            val screenHeight = v.rootView.height
            val keypadHeight = screenHeight - r.bottom
        }

        return v
    }

    override fun onResume() {
        super.onResume()
        if (!fromTermsTutorial)
            initTrackScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        fromTermsTutorial = requestCode == Navigator.REQUEST_CODE_TERMS
            && resultCode == Activity.RESULT_OK
            || requestCode == Navigator.REQUEST_CODE_TUTORIAL
            && resultCode == Activity.RESULT_OK
    }

    /** */

    private fun initTrackScreen() {

        val bundle = Bundle()
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserNullProperties(null)

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties) //REPORTAR ESTO!!
    }

    private fun init() {
        presenter.data()
        tie_password.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                btn_login.performClick()
                true
            } else false
        }


    }

    /** */

    private fun setSelectedCountry(model: CountryModel) {
        countrySelected = model
        tvw_country.text = countrySelected!!.name
        tvw_country.setAllCaps(true)
        ivw_flag.setImageDrawable(ContextCompat.getDrawable(context!!, CountryUtil.getFlag(countrySelected!!.iso)))
    }

    private fun openRegisterLink(url: String) {
        if (null != listener) {
            listener!!.openRegisterLink(url)
        }
    }

    /** */

    override fun renderData(countrySIM: String, countries: List<CountryModel>?) {
        var sim = countrySIM
        arguments?.getString("Pais")?.let {
            sim = it
            rlt_country.isEnabled = false
        }

        this.countries = countries

        if (!isVisible) return

        if (null != countries && !countries.isEmpty()) {
            this.countryAdapter = CountryAdapter(context, countries)

            for (model in countries) {
                if (model.iso == sim) {
                    setSelectedCountry(model)
                    break
                }
            }

        } else {
            this.countryAdapter = CountryAdapter(context, emptyList())
        }
    }

    override fun successOnline(login: Login, model: LoginModel) {
        presenter?.getUsabilityConfig(login)

        val bundle = Bundle()
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_LOGIN_SUCCESS)
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        model.tipoIngreso = login.tipoIngreso
        val properties = AnalyticsUtil.getUserProperties(model)
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_FORM_NAME, bundle, properties)

        presenter?.verificacion(model)
    }

    override fun setLogAccess(kinesisModel: KinesisModel, login: Login) {
        try {
            if (kinesisManager == null) {
                kinesisManager = KinesisManager.create(activity!!, GlobalConstant.SCREEN_LOG_LOGIN, kinesisModel)
            }
            kinesisManager!!.save(login)
        } catch (ignored: Exception) {
            BelcorpLogger.d("LogAccess", ignored)
        }

    }

    override fun successOffline() {

        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_LOGIN_SUCCESS)
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_FORM_NAME, bundle)

        listener?.onHome()

    }

    override fun failedOnline(error: ErrorModel) {
        when (error.code) {
            ErrorCode.NETWORK -> showNetworkError()
            ErrorCode.BAD_REQUEST, ErrorCode.SERVICE -> {
                if(error.params != null){
                    val response = RestApi.readError(error.params, AuthErrorDto::class.java)
                    if (response == null || StringUtil.isNullOrEmpty(response.description))
                        showErrorDefault()
                    else {
                        showErrorMessage(response.description)
                    }
                } else {
                    showErrorDefault()
                }
            }
            else -> showErrorDefault()
        }
    }

    override fun showErrorMessage(message: String?) {
        if (isVisible) {
            try {
                val msg = MessageDialog()
                    .setIcon(R.drawable.ic_alerta, 0)
                    .setStringTitle(R.string.incentives_error)
                    .setResMessage(message!!)
                    .setStringAceptar(R.string.button_aceptar)
                    .showIcon(true)
                    .showClose(false)
                if(arguments?.getBoolean("AppMV", false) == true){
                    msg.setListener(object: MessageDialog.MessageDialogListener{
                        override fun cancelar() {
                            msg.dismiss()
                        }

                        override fun aceptar() {
                            //listener?.onFinishError()
                        }

                    })
                }
                msg.show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: IllegalStateException) {
                BelcorpLogger.w(BaseFragment.MODAL_TAG, e)
            }

        }
    }

    override fun failedOffline() {
        showErrorMessage(getString(R.string.login_error_offline))
    }

    override fun onVerificacionResponse(verificacion: Verificacion, loginModel: LoginModel) {
        if (null != listener) {
            if ((verificacion.opcionVerificacionSMS == CreditApplicationType.NOT_APPLY
                    || verificacion.opcionVerificacionSMS == CreditApplicationType.ACCEPT)
                    && (verificacion.opcionVerificacionCorreo == CreditApplicationType.NOT_APPLY
                    || verificacion.opcionVerificacionCorreo == CreditApplicationType.ACCEPT)
                    && (verificacion.opcionCambioClave == CreditApplicationType.NOT_APPLY
                    || verificacion.opcionCambioClave == CreditApplicationType.ACCEPT)
                    && (loginModel.indicadorContratoCredito == CreditApplicationType.NOT_APPLY
                    || loginModel.indicadorContratoCredito == CreditApplicationType.ACCEPT)
                    && loginModel.isAceptaTerminosCondiciones) {
                if(arguments?.getBoolean("AppMV", false) == true){
                    listener?.onFinish()
                } else {
                    val sessionManager = SessionManager.getInstance(context()!!)
                    if (sessionManager.isTutorial(loginModel.consultantCode)!!) {
                        listener?.onTutorial(loginModel.consultantCode, loginModel.countryISO)
                    } else {
                        listener?.onHome()
                    }
                }

            } else {
                listener?.onTerms(loginModel)
            }
        }else{
            hideLoading()
        }
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    /** */

    @OnClick(R.id.rlt_country)
    internal fun onCountrySelected() {
        try {
            ListDialog()
                    .setTitle(R.string.login_select_country)
                    .setCountries(countries)
                    .setAdapter(countryAdapter)
                    .setListViewListener(listDialogListener)
                    .show(fragmentManager, "modalCountries")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalCountries", e)
        }

    }

    @OnClick(R.id.ivw_help_user)
    internal fun onHelpUser(view: View) {
        if (countrySelected != null)
            showTooltip(view, countrySelected!!.textHelpUser, Tooltip.GRAVITY_LEFT)
        else {
            val message = getString(R.string.login_validation_pais)
            showTooltip(tvw_country, message, Tooltip.GRAVITY_RIGHT, true)
        }
    }

    @OnClick(R.id.ivw_help_pwd)
    internal fun onHelpPassword(view: View) {
        if (countrySelected != null)
            showTooltip(view, countrySelected!!.textHelpPassword, Tooltip.GRAVITY_LEFT)
        else {
            val message = getString(R.string.login_validation_pais)
            showTooltip(tvw_country, message, Tooltip.GRAVITY_RIGHT, true)
        }
    }

    @OnClick(R.id.ivw_show)
    internal fun onShowPassword() {
        visiblePassword = !visiblePassword

        if (visiblePassword) {
            tie_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            tie_password.inputType = 129
        }

        tie_password.setSelection(tie_password.text.toString().length)
    }

    @OnClick(R.id.btn_login)
    internal fun onLogin() {

        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN)
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BUTTON)
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LOGIN_NORMAL)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_NAME, bundle)

        val credentials = CredentialsModel()

        credentials.pais = if (null == countrySelected) null else countrySelected!!.iso
        credentials.username = tie_username.text.toString()
        credentials.password = tie_password.text.toString()
        credentials.tipoAutenticacion = AuthType.FORM

        if (!credentials.isValid) {
            if (null == countrySelected)
                showTooltip(tvw_country, getString(R.string.login_validation_pais), Tooltip.GRAVITY_RIGHT, true)
            else if (StringUtil.isNullOrEmpty(credentials.username) && StringUtil.isNullOrEmpty(credentials.password))
                showTooltip(tie_username, getString(R.string.login_validation_invalid), Tooltip.GRAVITY_RIGHT, true)
            else if (StringUtil.isNullOrEmpty(credentials.username))
                showTooltip(tie_username, getString(R.string.login_validation_username), Tooltip.GRAVITY_RIGHT, true)
            else
                showTooltip(tie_password, getString(R.string.login_validation_password), Tooltip.GRAVITY_RIGHT, true)

            return
        }

        KeyboardUtil.dismissKeyboard(context!!, tie_username)

        presenter?.loginWithForm(credentials, NetworkUtil.isThereInternetConnection(context!!))
    }

    @OnClick(R.id.tvw_forgot)
    internal fun onForgot() {
        if (null != listener)
            listener!!.openRecovery()
    }

}
