package biz.belcorp.consultoras.feature.payment.online.resultado

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.model.pagoonline.ResultadoPagoModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.payment.online.di.PaymentOnlineComponent
import biz.belcorp.consultoras.util.AdapterPagoHelper
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.annotation.Country
import biz.belcorp.library.util.CountryUtil
import kotlinx.android.synthetic.main.fragment_paymant_mensajes_rejected.*
import kotlinx.android.synthetic.main.fragment_resumen_pago.*
import kotlinx.android.synthetic.main.view_resumen_pago.*
import java.text.DecimalFormat
import javax.inject.Inject

class ResultadoFragment : BaseFragment(), ResultadoView {

    @Inject
    lateinit var presenter: ResultadoPresenter
    private var resultadoPago: ResultadoPagoModel? = null
    private var resultadoPagoRechazado: ResultadoPagoModel.ResultadoPagoRechazadoModel? = null
    private var listener: ResultadoFragmentListener? = null
    private var decimalFormat: DecimalFormat = CountryUtil.getDecimalFormatByISO(Country.PE, true)

    companion object {
        /* Success */
        fun newInstance(resultadoPago: ResultadoPagoModel): ResultadoFragment {
            return ResultadoFragment().apply {
                this.resultadoPago = resultadoPago
            }
        }

        /* Reject */
        fun newInstance(resultadoPagoRechazado: ResultadoPagoModel.ResultadoPagoRechazadoModel): ResultadoFragment {
            return ResultadoFragment().apply {
                this.resultadoPagoRechazado = resultadoPagoRechazado
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ResultadoFragmentListener) {
            this.listener = context
        }
    }

    override fun context(): Context? {
        return activity?.applicationContext
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(PaymentOnlineComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        presenter.fillUser()
        if (resultadoPago != null) {
            init(resultadoPago!!)
        }
        if (resultadoPagoRechazado != null) {
            init(resultadoPagoRechazado!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (resultadoPago != null) return inflater.inflate(R.layout.fragment_resumen_pago, container, false)
        return inflater.inflate(R.layout.fragment_paymant_mensajes_rejected, container, false)
    }

    fun init(entity: ResultadoPagoModel) {
        tvw_nombre.text = String.format(getString(R.string.estado_cuenta_user_valoracion), entity.nombre)
        tvw_operacion.text = String.format(getString(R.string.numero_operacion_recibo), entity.numeroOperacion)
        tvw_num_tarjeta.text = entity.numeroTarjeta
        tvw_fecha_pago.text = entity.fechaPago
        tvw_monto.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.monto?.toDouble())))
        tvw_total_pagado.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.totalPagado?.toDouble())))
        tvw_gastos_admin.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.gastosAdmin?.toDouble())))
        tvw_saldo_pendiente.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.saldoPendiente?.toDouble())))
        tvw_vencimiento.text = String.format(getString(R.string.estado_cuenta_vencimiento), entity.fechaVencimiento)

        tvw_terminos_condiciones_uso.setOnClickListener {
            Tracker.trackEvent(
                GlobalConstant.EVENT_SCREEN_CONSTANCIA_PAGO,
                GlobalConstant.EVENT_NAME_PAGO_LINEA,
                GlobalConstant.EVENT_ACTION_CONSTANCIA,
                GlobalConstant.EVENT_LABEL_TERMINOS,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                presenter.user)
            AdapterPagoHelper.openPdfTerms("https://s3.amazonaws.com/consultorasPRD/SomosBelcorp/FileConsultoras/PE/CONDICIONES_DE_USO_WEB_PE.pdf", context!!) //TODO Obtener desde el Servicio
        }

        lnr_estado_cuenta.setOnClickListener {

            Tracker.trackEvent(
                GlobalConstant.EVENT_SCREEN_CONSTANCIA_PAGO,
                GlobalConstant.EVENT_NAME_PAGO_LINEA,
                GlobalConstant.EVENT_ACTION_CONSTANCIA,
                GlobalConstant.EVENT_LABEL_VER_ESTADO_CUENTA,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                presenter.user)

            listener?.goToAccountStatus()
        }


        tvw_constancia_pago.setOnClickListener {
            Tracker.trackEvent(
                GlobalConstant.EVENT_SCREEN_CONSTANCIA_PAGO,
                GlobalConstant.EVENT_NAME_PAGO_LINEA,
                GlobalConstant.EVENT_ACTION_CONSTANCIA,
                GlobalConstant.EVENT_LABEL_REVISAR_CONSTANCIA,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                presenter.user)

            listener?.goToConstancia(entity)
        }
        Handler().postDelayed({ showAnimation() }, 750)
        presenter.updateMonto()
    }

    fun init(entity: ResultadoPagoModel.ResultadoPagoRechazadoModel) {
        tvw_numero_operacion.text = entity.operacion
        tvw_fecha_operacion.text = entity.fecha
        tvw_mensaje.text =entity.mensaje
        btn_ir_home.setOnClickListener {
            Tracker.trackEvent(
                GlobalConstant.SCREEN_NAME_RECHAZADO,
                GlobalConstant.EVENT_NAME_PAGO_LINEA,
                GlobalConstant.EVENT_ACTION_PAGO_RECHAZADO,
                getString(R.string.ir_home),
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                presenter.user)
            listener?.onHome()
        }
        btn_volver_intentar.setOnClickListener {
            Tracker.trackEvent(
                GlobalConstant.SCREEN_NAME_RECHAZADO,
                GlobalConstant.EVENT_NAME_PAGO_LINEA,
                GlobalConstant.EVENT_ACTION_PAGO_RECHAZADO,
                getString(R.string.volver_intentar),
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                presenter.user)
            listener?.onTryAgain()
        }
    }

    private fun showAnimation() {
        Tracker.trackScreenUser(
            GlobalConstant.EVENT_SCREEN_EXITOSO,
            presenter.user
        )
        FullScreenDialog.Builder(context!!)
            .withMessage((resources.getString(R.string.pago_exitoso)).toString(), 26F)
            .withIcon(R.drawable.ic_anima_por)
            .withScreenDismiss(true)
            .withAnimation(resources,
                FullScreenDialog.SIMPLE_ANIMATION,
                ContextCompat.getColor(context!!, R.color.dorado),
                ContextCompat.getColor(context!!, R.color.primary))
            .show()
    }

    interface ResultadoFragmentListener {
        fun onHome()
        fun onTryAgain()
        fun goToAccountStatus()
        fun goToConstancia(entity: ResultadoPagoModel)
    }

}
