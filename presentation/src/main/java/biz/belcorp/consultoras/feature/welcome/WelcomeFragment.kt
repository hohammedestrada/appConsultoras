package biz.belcorp.consultoras.feature.welcome

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.SMSResquest
import biz.belcorp.consultoras.domain.entity.Verificacion
import biz.belcorp.consultoras.feature.welcome.di.WelcomeComponent
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.CommunicationUtils
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ViewUtils
import biz.belcorp.consultoras.util.anotation.CreditApplicationType
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.KeyboardUtil
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.fragment_welcome.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author andres.escobar on 3/08/2017.
 */
/** */

class WelcomeFragment : BaseFragment(), WelcomeView {

    @Inject
    lateinit var presenter: WelcomePresenter

    private var listener: WelcomeFragmentListener? = null

    private lateinit var loginModel: LoginModel

    private lateinit var verificacion: Verificacion

    private lateinit var word: String

    private var timer : CountDownTimer? = null

    private lateinit var mobile: String

    override fun context(): Context? {
        return null
    }

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is WelcomeFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onDestroyView() {
        timer?.cancel()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        presenter?.initScreenTrack()
    }

    /** */

    override fun onInjectView(): Boolean {
        getComponent(WelcomeComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    private fun init() {
        presenter.initScreenTrack()
        arguments?.getString(WelcomeActivity.MOBILE)?.let { mobile = it}
        arguments?.getString(WelcomeActivity.COUNTRY_ISO)?.let { presenter.verificacion(it)}

        lltMain.visibility = View.GONE
        btn_send_sms.setOnClickListener {
            if (NetworkUtil.isThereInternetConnection(context)){

                sendSMSEnabled()
            }else{
                Toast.makeText(context, resources.getString(R.string.connection_offline), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onVerificacionResponse(verificacion: Verificacion) {
        this.verificacion = verificacion
        listener?.setVerificacion(verificacion)
        listener?.configIcons(verificacion)

        when(verificacion.opcionVerificacionSMS){
            CreditApplicationType.NOT_APPLY -> {
                listener?.onPhoneConfirm(1)
            }
            CreditApplicationType.NOT_ACCEPT -> {
                listener?.initializeToolbar()
                when(verificacion.mostrarOpcion){
                    Verificacion.MUESTRA_SMS_EMAIL ->{
                        /**no debería hacer nada, pero como la verificacion por correo
                         * no está implementada, siempre se oculta dicho boton
                         */
                        lltMain.visibility = View.VISIBLE
                        btn_send_email.visibility = View.GONE
                    }
                    Verificacion.NO_MUESTRA_SMS ->{
                        /**aquí se debería ocultar la verificacion por sms, pero como
                         * la verificacion por correo no está implementada, siempre se
                         *  oculta dicho boton y se redirecciona al cambio de contraseña
                         */
                        listener?.onPhoneConfirm(1)
                        return
                    }
                    Verificacion.NO_MUESTRA_EMAIL ->{
                        lltMain.visibility = View.VISIBLE
                        btn_send_email.visibility = View.GONE
                    }

                }

                verificacion.intentosRestanteSms?.let {
                    if(it == 0){
                        startTimer()
                        btn_send_email.visibility = View.GONE
                        btn_send_sms.visibility = View.GONE
                        btn_send_sms_off.visibility = View.VISIBLE
                    } else {
                        lltAlerta.visibility = View.GONE
                    }
                }


                tvwCelular.text = verificacion.celularEnmascarado
                tvwCelularOff.text = verificacion.celularEnmascarado
                tvwEmail.text = verificacion.correoEnmascarado
                tvwWelcomeTitle.text = """¡Hola ${verificacion.primerNombre}!"""
                tvwWelcomeMessage.text = verificacion.mensajeSaludo
                val res = resources

                if(verificacion.telefono1.isNullOrEmpty() && verificacion.telefono2.isNullOrEmpty()){
                    tvwCallCenter.visibility = View.GONE
                } else if(!verificacion.telefono1.isNullOrEmpty() && verificacion.telefono2.isNullOrEmpty()){
                    if(!verificacion.celularEnmascarado.isNullOrEmpty()){
                        val html = String.format(res.getString(R.string.welcome_call_center_one_1), verificacion.telefono1)
                        tvwCallCenter.text = setText(tvwCallCenter, html)
                    } else {
                        val html = String.format(res.getString(R.string.welcome_call_center_one_2), verificacion.telefono1)
                        tvwCallCenter.text = setText(tvwCallCenter, html)
                    }
                } else if(verificacion.telefono1.isNullOrEmpty() && !verificacion.telefono2.isNullOrEmpty()){
                    if(!verificacion.celularEnmascarado.isNullOrEmpty()){
                        val html = String.format(res.getString(R.string.welcome_call_center_one_1), verificacion.telefono2)
                        tvwCallCenter.text = setText(tvwCallCenter, html)
                    } else {
                        val html = String.format(res.getString(R.string.welcome_call_center_one_2), verificacion.telefono2)
                        tvwCallCenter.text = setText(tvwCallCenter, html)
                    }
                } else {
                    if(!verificacion.celularEnmascarado.isNullOrEmpty()){
                        val html = String.format(res.getString(R.string.welcome_call_center_1), verificacion.telefono1, verificacion.telefono2)
                        tvwCallCenter.text = setText(tvwCallCenter, html)
                    } else {
                        val html = String.format(res.getString(R.string.welcome_call_center_2), verificacion.telefono1, verificacion.telefono2)
                        tvwCallCenter.text = setText(tvwCallCenter, html)
                    }
                }

                tvwCallCenter.setOnTouchListener { _, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        var mOffset = tvwCallCenter.getOffsetForPosition(motionEvent.x, motionEvent.y)
                        word = ViewUtils.findWordForRightHanded(tvwCallCenter.text.toString(), mOffset).replace("[\\s\\-()]".toRegex(), "")
                        word = validateNumber(word)
                        if(word.isNotBlank())
                            call(word)

                    }
                    false
                }
            }
            CreditApplicationType.ACCEPT -> {
                listener?.onPhoneConfirm(1)
            }
        }


    }

    private fun validateNumber(word: String): String {
        var number = word.replace("-","").replace("(","").replace(")","")

        return try{
            number.toBigInteger()
            word
        }
        catch (nfe: NumberFormatException) {
            ""
        }
    }

    private fun setText(textView: TextView, html: String?): CharSequence?{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    private fun call(number: String){
        CommunicationUtils.llamar(activity,number)
    }

    private fun startTimer(){
        timer?.cancel()

        val times = verificacion.horaRestanteSms?.toLong()?.times(1000)?.let { it } ?: 0
        timer = object : CountDownTimer(times, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val h = TimeUnit.MILLISECONDS.toHours( millisUntilFinished)
                val min = TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished) - TimeUnit.HOURS.toMinutes(h)
                val seg =  TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                val hours = if(h<10) "0$h" else h
                val minutes = if(min<10) "0$min" else min
                val seconds = if(seg<10) "0$seg" else seg
                val time = "$hours:$minutes:$seconds"
                tvwTiempoRestante.text = time
            }
            override fun onFinish() {
                btn_send_sms.visibility = View.VISIBLE
                btn_send_sms_off.visibility = View.GONE
                lltAlerta.visibility = View.GONE
            }
        }.start()
    }


    private fun sendSMSEnabled(){
        presenter.sendSMS(SMSResquest().apply {
            campaniaID = loginModel.campaing.toInt()
            celularActual = mobile
            celularNuevo = mobile
            origenID = verificacion.origenID
            origenDescripcion = verificacion.origenDescripcion
        })
    }

    override fun onError() {
        Toast.makeText(context, "Ocurrio un error al enviar correo. " +
            "Inténtelo nuevamente.", Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun onSMSResponse(send: BasicDto<Boolean>?) {
        send?.let {
            if(it.code == "0000") listener?.onSms(loginModel, verificacion)
            else Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSMSError(exception: Throwable) {
        BelcorpLogger.w("SMSUpdate", exception)
        Toast.makeText(context, "Hubo un error al llamar al servidor. Inténtelo nuevamente.", Toast.LENGTH_SHORT).show()
    }

    /** */

    override fun initScreenTrack(loginModel: LoginModel) {
        this.loginModel = loginModel
        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TEMS)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties)
    }

    private fun hideKeyboard() {
        var view = activity!!.currentFocus
        if (view == null) view = View(activity)
        KeyboardUtil.dismissKeyboard(activity!!, view)
    }


    /** */

    internal interface WelcomeFragmentListener {
        fun onHome()
        fun onSms(model: LoginModel, verificacion: Verificacion)
        fun onPhoneConfirm(opcion: Int)
        fun onPasswordSaved()
        fun setVerificacion(verificacion: Verificacion)
        fun initializeToolbar()
        fun configIcons(verificacion: Verificacion)
    }

    companion object {

        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }

    /** */

}
