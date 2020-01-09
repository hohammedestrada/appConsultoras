package biz.belcorp.consultoras.feature.sms

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.component.SMSLayout
import biz.belcorp.consultoras.common.component.SMSLayout.SMSListener
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.SMSResquest
import biz.belcorp.consultoras.feature.sms.di.SMSComponent
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.fragment_sms.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Leonardo Castañeda on 24/08/2017.
 */

class SMSFragment : BaseFragment(), SMSView, SMSListener {

    @Inject
    lateinit var presenter: SMSPresenter
    private var listener: SMSFragment.SMSFragmentListener? = null

    private var newPhone = ""
    private var campaing = ""
    private var countryISO = ""
    private var oldPhone = ""

    private var timer : CountDownTimer? = null

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        presenter.destroy()
    }

    override fun onInjectView(): Boolean {
        getComponent(SMSComponent::class.java).inject(this)
        return true
    }


    // Overrides Fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SMSFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sms, container, false)
    }

    override fun onDestroyView() {
        timer?.cancel()
        super.onDestroyView()
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

    // Override SMSView
    override fun initScreenTrack(model : LoginModel) {
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
        send?.let {
            if(it.code == "0000"){
                //listener?.onPhoneConfirm()
                startTimer()
            }
            else if(it.code == "1302") {
                txtMaxRetriesReached.visibility = View.VISIBLE
                startTimer()
                txtReenviar.isEnabled = false

            }
            else Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConfirmSMSCodeResponse(send: BasicDto<Boolean>?) {
        send?.let {
            if(it.code == "0000"){
                enableSMSRetry(false)
                smsLayout.showStatus(SMSLayout.STATUS_SUCCESS)
                txtTimer.text = ""
            }
            else{
                enableSMSRetry(true)
                smsLayout.showStatus(SMSLayout.STATUS_FAILURE)
            }
        }
    }

    override fun onConfirmPhoneChangeResponse(send: BasicDto<Boolean>?) {
        send?.let {
            if(it.code == "0000"){
                arguments?.getBoolean(SMSActivity.FROM_WELCOME)?.let {bool->
                    if(bool){
                        presenter.refreshData()
                        listener?.onPhoneConfirm(2)
                    } else {
                        showFullScreenDIalog()
                    }
                } ?: showFullScreenDIalog()



            }
            else{
                enableSMSRetry(true)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                smsLayout.clearText()
                smsLayout.showStatus(SMSLayout.STATUS_NEUTRAL)
            }
        }
    }

    fun showFullScreenDIalog() {
        FullScreenDialog.Builder(context!!)
            .withTitle(resources.getString(R.string.sms_full_screen_dialog_title))
            .withMessage(resources.getString(R.string.sms_full_screen_dialog_body))
            .withAction(resources.getString(R.string.sms_full_screen_dialog_action))
            .withButtonMessage(resources.getString(R.string.sms_full_screen_dialog_button))
            .withIcon(R.drawable.ic_hands)
            .setOnItemClick(object : FullScreenDialog.FullScreenDialogListener {
                override fun onDismiss() {
                }

                override fun onClickAceptar(dialog: FullScreenDialog) {
                    presenter.refreshData()
                    listener?.onPhoneConfirm(2)
                    dialog.dismiss()
                }

                override fun onClickAction(dialog: FullScreenDialog) {
                    listener?.onPhoneConfirm(1)
                    dialog.dismiss()
                }
            })
            .show()
    }

    override fun onShowTerms(url: String) {}
    override fun onSMSError(exception: Throwable) {
        Toast.makeText(context, resources.getString(R.string.sms_service_error), Toast.LENGTH_SHORT).show()
    }
    override fun onConfirmSMSCodeError(exception: Throwable) {
        Toast.makeText(context, resources.getString(R.string.sms_service_error), Toast.LENGTH_SHORT).show()
    }
    override fun onConfirmPhoneChangeError(exception: Throwable) {
        Toast.makeText(context, resources.getString(R.string.sms_service_error), Toast.LENGTH_SHORT).show()
    }

    // Override SMSListener
    override fun onCodeComplete() {
        if (NetworkUtil.isThereInternetConnection(context)){
            presenter.confirmSMSCode(SMSResquest().apply { campaniaID = campaing.toInt()
                addOrigenParameters(this)
                txtMaxRetriesReached.visibility = View.GONE
                codigoSMS = smsLayout.getCode()
                soloValidar  = true})
        }else{
            Toast.makeText(context, resources.getString(R.string.connection_offline), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInvalidText() {
        Toast.makeText(context, resources.getString(R.string.sms_service_error), Toast.LENGTH_SHORT).show()
    }


    // VinculacionListener
    internal interface SMSFragmentListener {
        fun onPhoneConfirm(type: Int)
        fun onPhoneNewChange()
    }


    // Private Methods
    private fun init(){

        smsLayout.listener = this
        smsLayout.firstFocus()

        newPhone = arguments?.getString(SMSActivity.EXTRA_NEW_PHONE_NUMBER) ?: ""
        countryISO = arguments?.getString(SMSActivity.EXTRA_COUNTRY_ISO)!!
        oldPhone = arguments?.getString(SMSActivity.EXTRA_PHONE_NUMBER) ?: ""
        campaing = arguments?.getString(SMSActivity.EXTRA_CAMPAING) ?: ""

        txtMaxRetriesReached.visibility = View.GONE

        txtNumber.text = resources.getString(R.string.sms_send_message).replace("$", newPhone)

        arguments?.getBoolean(SMSActivity.FROM_WELCOME, false)?.let {
            if(it){
                lltFooter.visibility = View.GONE
            }
        }

        btnConfirm.setOnClickListener {
            if (NetworkUtil.isThereInternetConnection(context)){
                presenter.confirmPhoneChange(SMSResquest().apply { campaniaID = campaing.toInt()
                    addOrigenParameters(this)
                    codigoSMS = smsLayout.getCode()
                    soloValidar  = false})
            }else{
                Toast.makeText(context, resources.getString(R.string.connection_offline), Toast.LENGTH_SHORT).show()
            }
        }

        txtReenviar.setOnClickListener{
            if (NetworkUtil.isThereInternetConnection(context)){
                timer?.cancel()
                txtTimerBefore.visibility = View.GONE
                txtTimer.text = resources.getText(R.string.sms_sending_code)
                presenter.sendSMS(SMSResquest().apply { campaniaID = campaing.toInt()
                    addOrigenParameters(this)
                    celularActual = oldPhone
                    celularNuevo = newPhone} )
            }else{
                Toast.makeText(context, resources.getString(R.string.connection_offline), Toast.LENGTH_SHORT).show()
            }
        }

        txtChangeNumber.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage(resources.getString(R.string.sms_dialog_body))
                .setPositiveButton("Aceptar"){_, _ ->
                    listener?.onPhoneNewChange()
                }
                .setNegativeButton("Cancelar"){_,_ -> }
                .create().show()
        }

        startTimer()
    }

    private fun addOrigenParameters(smsResquest: SMSResquest) {
        arguments?.getBoolean(SMSActivity.FROM_WELCOME, false)?.let {
            if(it){
                smsResquest.origenDescripcion = arguments?.getString(SMSActivity.EXTRA_ORIGEN_DESCRIPCION)
                smsResquest.origenID = arguments?.getInt(SMSActivity.EXTRA_ORIGEN_ID)
                smsResquest.estadoActividadID = arguments?.getInt(SMSActivity.EXTRA_ID_ESTADO_ACTIVIDAD)
            } else {
                smsResquest.origenDescripcion = ""
                smsResquest.origenID = 3
                smsResquest.estadoActividadID = 0
            }
        }


    }

    private fun intOrString(str: String): Boolean {
        val v = str.toIntOrNull()
        return when(v) {
            null -> false
            else -> true
        }
    }

    fun autocompleteCode(body: String?) {
        body?.let {b ->
            smsLayout.setCode(b.split(" ").first { it.length == 6 && intOrString(it) })
        }
    }

    private fun startTimer(){
        txtTimerBefore.visibility = View.VISIBLE
        timer?.cancel()
        smsLayout.setAvailability(true)
        timer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                smsLayout.isClickable = true
                val min = TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)
                val seg =  TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                val minutes = if(min<10) "0$min" else min
                val seconds = if(seg<10) "0$seg" else seg
                val time = "$minutes:$seconds"
                txtTimer.text = time
            }
            override fun onFinish() {
                enableSMSRetry(true)
                smsLayout.showStatus(SMSLayout.STATUS_NEUTRAL)
                smsLayout.clearText()
                txtTimerBefore.visibility = View.GONE
                txtTimer.text = resources.getString(R.string.sms_time_out)
                smsLayout.setAvailability(false)
            }
        }.start()
    }

    private fun enableSMSRetry(enable: Boolean){
        if(enable){
            btnConfirm.isEnabled = false
            btnConfirm.setBackgroundResource(R.drawable.btn_disabled)
            txtReenviar.isEnabled = true
            txtReenviar.setTextColor(resources.getColor(R.color.black))
        }else{
            btnConfirm.isEnabled = true
            btnConfirm.setBackgroundResource(R.drawable.btn_selector_primary)
            txtReenviar.isEnabled = false
            txtReenviar.setTextColor(resources.getColor(R.color.tab_disabled))
        }
    }

    // Companion
    companion object {
        fun newInstance(): SMSFragment {
            return SMSFragment()
        }
    }

}
