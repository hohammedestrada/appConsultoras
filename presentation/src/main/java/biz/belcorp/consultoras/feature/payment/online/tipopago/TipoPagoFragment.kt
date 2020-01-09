package biz.belcorp.consultoras.feature.payment.online.tipopago

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.R.id.rvw_type_pay
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.pagoonline.ConfirmacionPagoModel
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.common.model.pagoonline.TipoPagoModel
import biz.belcorp.consultoras.feature.payment.online.di.PaymentOnlineComponent
import biz.belcorp.consultoras.util.GlobalConstant
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import javax.inject.Inject
import biz.belcorp.consultoras.common.dialog.MessageFullDialog
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.library.util.CountryUtil
import kotlinx.android.synthetic.main.fragment_payment_type.*
import kotlinx.android.synthetic.main.gifbarview.*

class TipoPagoFragment : BaseFragment(), TipoPagoView, TipoPagoAdapter.TipoPagoListener {

    @Inject
    lateinit var presenter: TipoPagoPresenter
    private var adapter: TipoPagoAdapter? = null
    private var tipoListener: TypeListener? = null
    companion object {
        fun newInstance(estadoCuenta: PagoOnlineConfigModel.EstadoCuenta, metodoPago: PagoOnlineConfigModel.MetodoPago,
                        tipoPago: List<PagoOnlineConfigModel.TipoPago>, rutaIcono: String): TipoPagoFragment {
            val argumentos = Bundle().apply {
                putString(GlobalConstant.URL_ICONO, rutaIcono)
                putParcelable(GlobalConstant.CARD_SELECTED, metodoPago)
                putParcelable(GlobalConstant.ESTADO_CUENTA, estadoCuenta)
                putParcelableArrayList(GlobalConstant.TIPO_PAGO, ArrayList(tipoPago))
            }
            return TipoPagoFragment().apply {
                this.arguments = argumentos
            }
        }
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

        presenter.createTipoPago(arguments!!.getParcelable<PagoOnlineConfigModel.EstadoCuenta>(GlobalConstant.ESTADO_CUENTA),
            arguments!!.getParcelable<PagoOnlineConfigModel.MetodoPago>(GlobalConstant.CARD_SELECTED),
            arguments!!.getParcelableArrayList<PagoOnlineConfigModel.TipoPago>(GlobalConstant.TIPO_PAGO),
            arguments!!.getString(GlobalConstant.URL_ICONO))

    }

    fun setIcon(url: String) {
        url.let {
            Glide.with(this).asBitmap().load(it)
                .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                        imgTarjeta.setImageResource(R.drawable.ic_bancainternet)
                        return true
                    }
                })
                .into(imgTarjeta)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is TypeListener) {
            this.tipoListener = context
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_type, container, false)
    }

    override fun getTipo(tipoPago: TipoPagoModel) {
        setIcon(tipoPago.urlIcono!!)
        adapter = TipoPagoAdapter(tipoPago, this, context!!,/*formate*/presenter.userTrack.countryISO)
        rvw_type_pay.adapter = adapter

    }

    override fun openDialogTerm(term: String, tipoPago: String) {

        Tracker.trackEvent(
            GlobalConstant.SCREEN_PAGO_EN_LINEA,
            GlobalConstant.EVENT_NAME_PAGO_LINEA,
            GlobalConstant.EVENT_ACTION_VISA_PASO_UNO,
            tipoPago.plus(GlobalConstant.EVENT_TERMINOS),
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
            presenter.userTrack)


        var _term = "<p style='text-align: right;' >$term</p>";

        MessageFullDialog.Builder(context!!)
            .withHtmlMessage(_term)
            .withTitle(resources.getString(R.string.terminos_y_condiciones))
            .show()
    }

    override fun goToDetailPay(confirmacion: ConfirmacionPagoModel) {

        Tracker.trackEvent(
            GlobalConstant.SCREEN_PAGO_EN_LINEA,
            GlobalConstant.EVENT_NAME_PAGO_LINEA,
            GlobalConstant.EVENT_ACTION_VISA_PASO_UNO,
            confirmacion.tipoPago.plus(GlobalConstant.EVENT_CONTINUAR),
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
            presenter.userTrack)
        tipoListener!!.goToPagoVisaConfirmacion(confirmacion)
    }

    interface TypeListener {
        fun goToPagoVisaConfirmacion(confirmacion: ConfirmacionPagoModel)
    }
}
