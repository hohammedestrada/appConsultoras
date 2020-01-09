package biz.belcorp.consultoras.feature.payment.online.constancia

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.pagoonline.ResultadoPagoModel
import biz.belcorp.consultoras.feature.payment.online.di.PaymentOnlineComponent
import biz.belcorp.consultoras.feature.payment.online.resultado.ResultadoPresenter
import biz.belcorp.consultoras.feature.payment.online.resultado.ResultadoView
import biz.belcorp.library.annotation.Country
import biz.belcorp.library.util.CountryUtil
import kotlinx.android.synthetic.main.view_resumen_pago.*
import java.text.DecimalFormat
import javax.inject.Inject

class ConstanciaFragment : BaseFragment(), ResultadoView {

    @Inject
    lateinit var presenter: ResultadoPresenter
    private var resultadoPago: ResultadoPagoModel? = null
    private var decimalFormat: DecimalFormat = CountryUtil.getDecimalFormatByISO(Country.PE, true)

    companion object {
        fun newInstance(resultadoPago: ResultadoPagoModel): ConstanciaFragment {
            return ConstanciaFragment().apply {
                this.resultadoPago = resultadoPago
            }
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
        if (resultadoPago != null) {
            init(resultadoPago!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_constancia, container, false)
    }

    fun init(entity: ResultadoPagoModel) {
        tvw_operacion.text = String.format(getString(R.string.numero_operacion_recibo), entity.numeroOperacion)
        tvw_num_tarjeta.text = entity.numeroTarjeta
        tvw_fecha_pago.text = entity.fechaPago
        tvw_monto.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.monto?.toDouble())))
        tvw_total_pagado.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.totalPagado?.toDouble())))
        tvw_gastos_admin.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.gastosAdmin?.toDouble())))
        tvw_saldo_pendiente.text = entity.simboloMoneda.plus(" ".plus(decimalFormat.format(entity.saldoPendiente?.toDouble())))
        tvw_vencimiento.text = String.format(getString(R.string.estado_cuenta_vencimiento), entity.fechaVencimiento)
    }

}
