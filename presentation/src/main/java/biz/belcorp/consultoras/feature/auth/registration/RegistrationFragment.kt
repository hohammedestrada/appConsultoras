package biz.belcorp.consultoras.feature.auth.registration

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.adapter.CountryAdapter
import biz.belcorp.consultoras.common.dialog.ListDialog
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.dialog.Tooltip
import biz.belcorp.consultoras.common.model.auth.AssociateModel
import biz.belcorp.consultoras.common.model.auth.CredentialsModel
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.country.CountryModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.Verificacion
import biz.belcorp.consultoras.feature.auth.di.AuthComponent
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.KinesisManager
import biz.belcorp.consultoras.util.anotation.CreditApplicationType
import biz.belcorp.consultoras.util.anotation.ErrorCode
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.analytics.annotation.AnalyticEvent
import biz.belcorp.library.analytics.annotation.AnalyticScreen
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.KeyboardUtil
import biz.belcorp.library.util.StringUtil
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject

/** */

@AnalyticScreen(name = "RegistrationScreen")
class RegistrationFragment : BaseFragment(), RegistrationView {

    @Inject
    internal lateinit var presenter: RegistrationPresenter

    internal var facebookProfile: FacebookProfileModel? = null

    internal lateinit var countrySIM: String
    internal var countries: List<CountryModel>? = null
    internal lateinit var countryAdapter: CountryAdapter
    internal var countrySelected: CountryModel? = null
    internal lateinit var login: LoginModel

    internal var visiblePassword = false

    private var kinesisManager: KinesisManager? = null
    private var listener: RegistrationFragmentListener? = null

    /** */

    internal val listDialogListener: ListDialog.ListDialogListener = ListDialog.ListDialogListener { position -> setSelectedCountry(countryAdapter.getItem(position) as CountryModel) }

    /** */

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

    internal interface RegistrationFragmentListener {
        fun onHome()
        fun onTerms(loginModel: LoginModel)
        fun onTutorial(consultantCode: String, countryISO: String)
        fun onFinish()
        fun onFinishError()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RegistrationFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        initScreenTrack()
    }

    /** */

    private fun init() {
        val bundle = arguments
        if (bundle != null)
            facebookProfile = bundle.getParcelable("facebook")

        if (null == facebookProfile)
            activity?.onBackPressed()


        rlt_country.setOnClickListener {
            onCountrySelected()
        }

        ivw_help_user.setOnClickListener {
            onHelpUser(ivw_help_user)
        }

        ivw_help_pwd.setOnClickListener {
            onHelpPassword(ivw_help_pwd)
        }

        ivw_show.setOnClickListener {
            onShowPassword()
        }

        btn_login.setOnClickListener {
            onLogin()
        }

        initData()
        presenter.data()
    }

    private fun initData() {
        Glide.with(this).load(facebookProfile?.image).apply(RequestOptions.noTransformation()
                .placeholder(R.drawable.ic_contact_default)
                .error(R.drawable.ic_contact_default)
                .priority(Priority.HIGH))
                .into(ivw_registration_image)
        tvw_registration_name.text = facebookProfile?.name
    }

    private fun initScreenTrack() {
        val bundle = Bundle()
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_REGISTER_FACEBOOK)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserNullProperties(null)

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    fun trackBackPressed() {
        val analytics = Bundle()
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_REGISTER_FACEBOOK)
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE)
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserNullProperties(null)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties)
    }

    /** */

    private fun setSelectedCountry(model: CountryModel) {
        countrySelected = model
        tvw_country.text = countrySelected?.name
        tvw_country.setAllCaps(true)
        ivw_flag.setImageDrawable(ContextCompat.getDrawable(context()!!, CountryUtil.getFlag(countrySelected?.iso)))
    }

    /** */

    override fun renderData(countrySIM: String, countries: List<CountryModel>?) {
        this.countrySIM = countrySIM
        this.countries = countries

        if (null != countries && !countries.isEmpty()) {
            this.countryAdapter = CountryAdapter(context(), countries)

            for (model in countries) {
                if (model.iso == countrySIM) {
                    setSelectedCountry(model)
                    break
                }
            }
        } else {
            this.countryAdapter = CountryAdapter(context(), emptyList())
        }
    }

    override fun success(login: Login, loginModel: LoginModel, verificacion: Verificacion) {
        presenter.getUsabilityConfig(login)

        this.login = loginModel

        this.login.tipoIngreso = login.tipoIngreso

        if (null != listener) {
            if ((verificacion.opcionVerificacionSMS == CreditApplicationType.NOT_APPLY || verificacion.opcionVerificacionSMS == CreditApplicationType.ACCEPT)
                    && (verificacion.opcionVerificacionCorreo == CreditApplicationType.NOT_APPLY || verificacion.opcionVerificacionCorreo == CreditApplicationType.ACCEPT)
                    && (verificacion.opcionCambioClave == CreditApplicationType.NOT_APPLY || verificacion.opcionCambioClave == CreditApplicationType.ACCEPT)
                    && (loginModel.indicadorContratoCredito == CreditApplicationType.NOT_APPLY || loginModel.indicadorContratoCredito == CreditApplicationType.ACCEPT)
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
        }
    }

    override fun setLogAccess(kinesisModel: KinesisModel, login: Login) {
        try {
            if (kinesisManager == null) {
                kinesisManager = KinesisManager.create(activity!!, GlobalConstant.SCREEN_LOG_LOGIN, kinesisModel)
            }
            kinesisManager?.save(login)
        } catch (ignored: Exception) {
        }

    }

    override fun failed(error: ErrorModel) {
        when (error.code) {
            ErrorCode.NETWORK -> showNetworkError()
            ErrorCode.BAD_REQUEST, ErrorCode.SERVICE -> {
                val response = RestApi.readError(error.params)
                if (null == response || StringUtil.isNullOrEmpty(response.message))
                    showErrorDefault()
                else
                    showErrorMessage(response.message)
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
                            listener?.onFinishError()
                        }

                    })
                }
                msg.show(childFragmentManager, BaseFragment.MODAL_TAG)
            } catch (e: IllegalStateException) {
                BelcorpLogger.w(BaseFragment.MODAL_TAG, e)
            }

        }
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    /** */

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

    internal fun onHelpUser(view: View) {
        if (countrySelected != null)
            showTooltip(view, countrySelected!!.textHelpUser, Tooltip.GRAVITY_LEFT)
        else {
            val message = getString(R.string.login_validation_pais)
            showTooltip(tvw_country, message, Tooltip.GRAVITY_RIGHT, true)
        }
    }

    internal fun onHelpPassword(view: View) {
        if (countrySelected != null)
            showTooltip(view, countrySelected!!.textHelpPassword, Tooltip.GRAVITY_LEFT)
        else {
            val message = getString(R.string.login_validation_pais)
            showTooltip(tvw_country, message, Tooltip.GRAVITY_RIGHT, true)
        }
    }

    internal fun onShowPassword() {
        visiblePassword = !visiblePassword

        if (visiblePassword) {
            tie_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            tie_password.inputType = 129
        }
    }

    internal fun onLogin() {

        val credentials = CredentialsModel()

        credentials.pais = if (null == countrySelected) null else countrySelected?.iso
        credentials.username = tie_username.text.toString()
        credentials.password = tie_password.text.toString()
        credentials.tipoAutenticacion = 1

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

        val associate = AssociateModel()

        associate.codigoUsuario = credentials.username
        associate.claveSecreta = credentials.password
        associate.idAplicacion = facebookProfile?.id
        associate.login = facebookProfile?.id
        associate.nombres = facebookProfile?.name
        associate.apellidos = facebookProfile?.lastName
        associate.fechaNacimiento = facebookProfile?.birthday
        associate.correo = facebookProfile?.email
        associate.genero = facebookProfile?.gender
        associate.ubicacion = facebookProfile?.location
        associate.linkPerfil = facebookProfile?.linkProfile
        associate.fotoPerfil = facebookProfile?.image
        associate.paisISO = credentials.pais
        associate.proveedor = "Facebook"

        presenter.associate(associate, credentials, facebookProfile)

        KeyboardUtil.dismissKeyboard(context!!, tie_username)
    }
}
