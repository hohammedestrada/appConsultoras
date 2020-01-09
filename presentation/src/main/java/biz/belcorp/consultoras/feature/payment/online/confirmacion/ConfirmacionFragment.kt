package biz.belcorp.consultoras.feature.payment.online.confirmacion

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.domain.entity.VisaConfig
import biz.belcorp.consultoras.common.model.pagoonline.ConfirmacionPagoModel
import biz.belcorp.consultoras.common.model.pagoonline.ResultadoPagoModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.payment.online.PayOnlineActivity
import biz.belcorp.consultoras.feature.payment.online.di.PaymentOnlineComponent
import biz.belcorp.consultoras.feature.payment.online.metodopago.PaymentOnlinePresenter
import biz.belcorp.consultoras.feature.payment.online.metodopago.PaymentOnlineView
import javax.inject.Inject
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.annotation.Country
import biz.belcorp.library.util.CountryUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_confirm_ammount.*
import kotlinx.android.synthetic.main.view_monto_pagar.*
import pe.com.visanet.lib.VisaNetPaymentInfo
import java.text.DecimalFormat

class ConfirmacionFragment: BaseFragment(), PaymentOnlineView, ConfirmacionView {


    @Inject
    lateinit var presenter: PaymentOnlinePresenter

    @Inject
    lateinit var confirmacionPresenter: ConfirmacionPresenter

    private var listener: VisaListener? = null
    private var visaConfig:VisaConfig? = null
    private var nextCounter:String? = null
    private lateinit var confirm: ConfirmacionPagoModel

    private var decimalFormat: DecimalFormat = CountryUtil.getDecimalFormatByISO(Country.PE, true)

    companion object {
        fun newInstance(confirmacion: ConfirmacionPagoModel):ConfirmacionFragment{
            val argumentos = Bundle().apply {
                putParcelable(GlobalConstant.CONFIRMACION_PAGO, confirmacion)
            }
            return ConfirmacionFragment().apply {
                this.arguments = argumentos
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_ammount, container, false)
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(PaymentOnlineComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        this.confirmacionPresenter.attachView(this)
        presenter.fillUser()
        init(arguments)
    }

    private fun init(arguments: Bundle?) {

        presenter.getVisaConfiguration(FirebaseRemoteConfig.getInstance().getString(BuildConfig.REMOTE_CONFIG_SECRET))

        arguments?.let{
            it.getParcelable<ConfirmacionPagoModel>(GlobalConstant.CONFIRMACION_PAGO)?.let{confirm ->
                this.confirm = confirm
                label_porcentaje.text = confirm.labelGasto.plus(":")
                tvw_mnt_bruto.text = confirm.simboloMoneda.plus(" ".plus(decimalFormat.format(confirm.montoBruto)))
                porcentaje.text = confirm.simboloMoneda.plus(" ".plus(decimalFormat.format(confirm.porcentajeBruto)))
                total_pagar.text = confirm.simboloMoneda.plus(" ".plus(decimalFormat.format(confirm.totalPagar?.toDouble())))
                initGlide(confirm.url)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is VisaListener){
            this.listener=context
        }
    }

    private fun initGlide(url: String?) {
        url.let {
            Glide.with(this).asBitmap().load(it)
                .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                        img_ico_tarjeta.setImageResource(R.drawable.ic_bancainternet)
                        return true
                    }
                })
                .into(img_ico_tarjeta)
        }
    }

    override fun getVisaConfig(config: VisaConfig){
        listener?.setConfiguration(config!!)
        this.visaConfig = config
        unblockPago()

        btn_paid_continue.setOnClickListener {
            blockPago()

            Tracker.trackEvent(
                GlobalConstant.SCREEN_PAGO_EN_LINEA,
                GlobalConstant.EVENT_NAME_PAGO_LINEA,
                GlobalConstant.EVENT_ACTION_VISA_PASO_DOS,
                GlobalConstant.EVENT_LABEL_PAGAR_VISA,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                presenter.user)

            if(nextCounter == null){
                val nextCounterUri = String.format(config.nextCounterUrl+"/%s/", config.merchantId).replace("//", "/")
                var authorization:String = String.format("%s:%s", config.accessKeyId, config.secretAccessKey)
                presenter.getVisaNextCounter(nextCounterUri, Base64.encodeToString(authorization.toByteArray(), Base64.NO_WRAP))
            } else{
                getVisaNextCounter(nextCounter!!)
            }
        }
    }

    fun cargarPasarelaVisa(nextCounter: String){
        listener!!.getPasarelaVisa(nextCounter, visaConfig!!, confirm)
    }

    override fun getVisaNextCounter(nextCounter: String?){
        this.nextCounter = nextCounter
        when(nextCounter){
            null -> unblockPago()
            else -> cargarPasarelaVisa(nextCounter)
        }
    }

    override fun onResultPayment(resultCode: Int, result: VisaNetPaymentInfo?, config: VisaConfig) {
        confirmacionPresenter.setResultPaymentVisa(resultCode, result, confirm, config)
    }

    override fun blockPago(){
        btn_paid_continue.isEnabled = false
    }

    override fun unblockPago(){
        btn_paid_continue.isEnabled = true
    }

    override fun setResultPayment_Success(resultadoPagoModel: ResultadoPagoModel){
        (activity as PayOnlineActivity).goToFragmentResultadoSuccess(resultadoPagoModel)
    }

    override fun setResultPayment_Rejected(resultadoPagoRechazadoModel: ResultadoPagoModel.ResultadoPagoRechazadoModel){
        (activity as PayOnlineActivity).goToFragmentResultadoRejected(resultadoPagoRechazadoModel)
    }

    override fun onCancelPayment(){
        Toast.makeText(context, "On Cancel Pago Visa", Toast.LENGTH_LONG).show()
    }


    interface VisaListener {
        fun setConfiguration(config: VisaConfig)
        fun getPasarelaVisa(nextCounter: String, config: VisaConfig, confirm: ConfirmacionPagoModel)
    }

}
