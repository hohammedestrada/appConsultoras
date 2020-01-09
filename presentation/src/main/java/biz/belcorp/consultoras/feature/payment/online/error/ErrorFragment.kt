package biz.belcorp.consultoras.feature.payment.online.error

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.pagoonline.ResultadoPagoModel
import biz.belcorp.consultoras.feature.payment.online.di.PaymentOnlineComponent
import biz.belcorp.consultoras.feature.payment.online.resultado.ErrorPresenter
import biz.belcorp.consultoras.feature.payment.online.resultado.ResultadoView
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.fragment_resumen_pago.*
import kotlinx.android.synthetic.main.view_resumen_pago.*
import javax.inject.Inject

class ErrorFragment: BaseFragment(), ResultadoView {

    @Inject
    lateinit var presenter: ErrorPresenter

    companion object {
        fun newInstance(): ErrorFragment {
            return ErrorFragment()
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
        presenter.attachView(this)

        arguments?.let{
            it.getParcelable<ResultadoPagoModel>(GlobalConstant.RESULTADO_PAGO)?.let{
                init(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_resumen_pago, container, false)
    }

    fun init(resultadoPago: ResultadoPagoModel){
        tvw_nombre.text = String.format(getString(R.string.estado_cuenta_user_valoracion),resultadoPago.nombre)
        tvw_operacion.text = String.format(getString(R.string.numero_operacion_recibo),resultadoPago.numeroOperacion)
        tvw_num_tarjeta.text = resultadoPago.numeroTarjeta
        tvw_fecha_pago.text = resultadoPago.fechaPago
        tvw_monto.text = resultadoPago.monto
        tvw_total_pagado.text = resultadoPago.totalPagado
        tvw_gastos_admin.text = resultadoPago.gastosAdmin
        tvw_saldo_pendiente.text = resultadoPago.saldoPendiente
        tvw_vencimiento.text = String.format(getString(R.string.estado_cuenta_vencimiento),resultadoPago.fechaVencimiento)
    }
}
