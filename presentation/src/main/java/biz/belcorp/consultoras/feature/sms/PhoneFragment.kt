package biz.belcorp.consultoras.feature.sms

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import javax.inject.Inject

import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.client.ClienteValidator
import biz.belcorp.consultoras.feature.sms.di.SMSComponent
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.CountryUtil
import kotlinx.android.synthetic.main.fragment_phone.*
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.SMSResquest
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.NetworkUtil


/**
 * @author Leonardo Castañeda on 24/08/2017.
 */

class PhoneFragment : BaseFragment(), SMSView {

    @Inject
    lateinit var presenter: SMSPresenter
    private var listener: PhoneFragment.PhoneFragmentListener? = null
    private var valid = false

    private var currentPhone = ""
    private var countryISO = ""
    private var campaing = ""

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onInjectView(): Boolean {
        getComponent(SMSComponent::class.java).inject(this)
        return true
    }


    // Overrides Fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PhoneFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        gestionarAtributos()
        return inflater.inflate(R.layout.fragment_phone, container, false)

    }


    // Override BaseFragment
    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    // Override SMSLayout
    override fun initScreenTrack(model: LoginModel) {
        val bundle = Bundle()
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PRODUCT)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(model)
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    override fun trackBackPressed(model: LoginModel) {
        val analytics = Bundle()
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PRODUCT)
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK)
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE)
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(model)
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties)
    }

    override fun onSMSResponse(send: BasicDto<Boolean>?) {
        send?.let { it ->
            if (it.code == "0000") {
                val numero = arguments?.getBoolean(SMSActivity.EXTRA_SMS_DIRECTO)?.let { esDirecto ->
                    if (esDirecto)
                        currentPhone
                    else
                        edtNewCell.text.toString()

                } ?: kotlin.run { "" }

                listener?.onConfirm(numero)
            } else Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSMSError(exception: Throwable) {
        BelcorpLogger.w("SMSUpdate", exception)
    }

    override fun onShowTerms(url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(activity, R.string.terms_activity_not_found, Toast.LENGTH_SHORT).show()
            BelcorpLogger.w("openPdfTerms", ex)
        }
    }

    override fun onConfirmSMSCodeResponse(send: BasicDto<Boolean>?) {}
    override fun onConfirmSMSCodeError(exception: Throwable) {}
    override fun onConfirmPhoneChangeResponse(send: BasicDto<Boolean>?) {}
    override fun onConfirmPhoneChangeError(exception: Throwable) {}

    // VinculacionListener
    internal interface PhoneFragmentListener {
        fun onConfirm(newNumber: String)
        fun onCancel()
    }

    // Private Metodos
    private fun init() {
        arguments?.getBoolean(SMSActivity.EXTRA_SMS_DIRECTO)?.let {
            if (it) { //si se va de frente a mandar el sms
                gestionarConexionSms()
                scrollMain.visibility = View.GONE
                lnrEnviandoSms.visibility = View.VISIBLE
            } else {
                scrollMain.visibility = View.VISIBLE
                lnrEnviandoSms.visibility = View.GONE
                hasMobileNumber(currentPhone)

                btnConfirm.setOnClickListener {
                    if (valid) {
                        if (chkTerms.isChecked) {
                            val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(edtNewCell.windowToken, 0)

                            gestionarConexionSms(true)

                        } else {
                            Toast.makeText(context, resources.getString(R.string.no_terms_accepted), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, resources.getString(R.string.no_valid_number), Toast.LENGTH_SHORT).show()
                    }

                }

                lnlCancel.setOnClickListener {
                    listener?.onCancel()
                }

                edtNewCell.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(cs: CharSequence, start: Int, before: Int, count: Int) {
                        if (!cs.isEmpty()) {
                            showAlerts(cs.toString())
                        } else {
                            txtNumberAlert.visibility = View.INVISIBLE
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun afterTextChanged(s: Editable) {}
                })

                txt_terminos.setOnClickListener {
                    presenter.getUrlTyC()
                }
            }
        }
    }

    private fun hasMobileNumber(phone: String?) {

        if (phone != null && !phone.isEmpty()) {
            txtCurrent.visibility = View.VISIBLE
            txtHeader.visibility = View.GONE
            txtCurrentPhone.visibility = View.VISIBLE
            txtCurrentPhone.text = phone
        } else {
            txtCurrent.visibility = View.GONE
            txtHeader.visibility = View.VISIBLE
            txtCurrentPhone.visibility = View.GONE
        }

    }

    fun gestionarConexionSms(solomostrarToast: Boolean = true) {

        if (NetworkUtil.isThereInternetConnection(context)) {
            arguments?.getBoolean(SMSActivity.EXTRA_SMS_DIRECTO)?.let {
                if (it) {
                    presenter.sendSMS(SMSResquest().apply { campaniaID = campaing.toInt(); celularActual = currentPhone; celularNuevo = currentPhone }, false)
                } else
                    presenter.sendSMS(SMSResquest().apply { campaniaID = campaing.toInt(); celularActual = currentPhone; celularNuevo = edtNewCell.text.toString() })
            }

        } else {

            if (solomostrarToast)
                Toast.makeText(context, resources.getString(R.string.connection_offline), Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(context, resources.getString(R.string.connection_offline), Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }

        }


    }

    private fun gestionarAtributos() {
        currentPhone = arguments?.getString(SMSActivity.EXTRA_PHONE_NUMBER) ?: ""
        countryISO = arguments?.getString(SMSActivity.EXTRA_COUNTRY_ISO)!!
        campaing = arguments?.getString(SMSActivity.EXTRA_CAMPAING) ?: ""
    }

    private fun showAlerts(number: String) {

        // Validacion del primer numero
        if (ClienteValidator.validateStartNumber(number, countryISO)) {
            txtNumberAlert.visibility = View.INVISIBLE
        } else {
            txtNumberAlert.text = resources.getString(R.string.start_number_validation).replace("$", CountryUtil.getMobileStartNumberMap()[countryISO].toString())
            txtNumberAlert.visibility = View.VISIBLE
            valid = false
            return
        }

        // Validacion de la longitud del numero
        if (ClienteValidator.validateMobileLength(number, countryISO)) {
            txtNumberAlert.visibility = View.INVISIBLE
        } else {
            txtNumberAlert.text = resources.getString(R.string.length_validation).replace("$", CountryUtil.getMobileLengthMap()[countryISO].toString())
            txtNumberAlert.visibility = View.VISIBLE
            valid = false
            return
        }

        valid = true

    }

    // Companion
    companion object {
        fun newInstance(): PhoneFragment {
            return PhoneFragment()
        }
    }

}
