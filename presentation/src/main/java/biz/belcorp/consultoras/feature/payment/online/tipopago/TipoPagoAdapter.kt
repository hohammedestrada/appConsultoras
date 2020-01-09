package biz.belcorp.consultoras.feature.payment.online.tipopago

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.pagoonline.ConfirmacionPagoModel
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.common.model.pagoonline.TipoPagoModel
import biz.belcorp.consultoras.util.AdapterPagoHelper
import biz.belcorp.consultoras.util.GlobalConstant
import java.text.DecimalFormat
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.MoneyValueFilter
import android.view.inputmethod.InputMethodManager
import android.widget.*
import biz.belcorp.consultoras.domain.util.convertToDouble

import biz.belcorp.consultoras.domain.util.toDateAndFormat
import biz.belcorp.consultoras.domain.util.toStringOpcionalDecimal

class TipoPagoAdapter(var pago: TipoPagoModel, var listener: TipoPagoListener, var context: Context, var iso: String?)
    : RecyclerView.Adapter<TipoPagoAdapter.TipoPagoViewHolder>() {

    private val TIPO_PAGO_TOTAL = GlobalConstant.CODIGO_TIPO_PAGO_TOTAL.toInt()
    private val TIPO_PAGO_PARCIAL = GlobalConstant.CODIGO_TIPO_PAGO_PARCIAL.toInt()

    private val decimalFormat: DecimalFormat = CountryUtil.getDecimalFormatByISO(iso, true)

    private var expandedPosition = -1
    private var listaTipoPago: List<PagoOnlineConfigModel.TipoPago> = pago.tipoPago
    private var porcentajeCalculado: Double? = null
    private var montoAux: Double = 0.0
    private var montoAuxStr: String = ""

    override fun getItemViewType(position: Int): Int {
        return when (listaTipoPago[position].codigo) {
            GlobalConstant.CODIGO_TIPO_PAGO_TOTAL -> TIPO_PAGO_TOTAL
            else -> TIPO_PAGO_PARCIAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoPagoAdapter.TipoPagoViewHolder {
        return when (viewType) {
            TIPO_PAGO_TOTAL -> PagoTotalViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_payment_total, parent, false))
            else -> PagoParcialViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_payment_parcial, parent, false))
        }
    }

    override fun onBindViewHolder(holder: TipoPagoAdapter.TipoPagoViewHolder, position: Int) {
        holder.bin()
        when (position) {
            expandedPosition -> holder.show()
            else -> holder.hide()
        }
    }

    override fun getItemCount(): Int {
        return listaTipoPago.size
    }

    /* View Holders */
    abstract inner class TipoPagoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bin()
        abstract fun show()
        abstract fun hide()

        fun toogle(countainer: ViewGroup) {
            expandedPosition = when (countainer.visibility) {
                View.VISIBLE -> -1
                else -> adapterPosition
            }
            notifyDataSetChanged()
        }
    }

    inner class PagoTotalViewHolder(itemView: View) : TipoPagoViewHolder(itemView) {

        private val tvw_pago_bruto: TextView = itemView.findViewById(R.id.tvw_pago_bruto)
        private val tvw_fecha_vencimiento: TextView = itemView.findViewById(R.id.tvw_fecha_vencimiento)
        private val tvw_mnt_bruto_total: TextView = itemView.findViewById(R.id.tvw_mnt_bruto_total)
        private val porcentaje_total: TextView = itemView.findViewById(R.id.porcentaje_total)
        private val total_pagar: TextView = itemView.findViewById(R.id.total_pagar)
        private val label_porcentaje_total: TextView = itemView.findViewById(R.id.label_porcentaje_total)
        private val tvwTerminos_total: TextView = itemView.findViewById(R.id.tvwTerminos_total)
        private val lnl_pago_total: LinearLayout = itemView.findViewById(R.id.lnl_pago_total)
        private val lnr_continue: LinearLayout = itemView.findViewById(R.id.lnr_continue)
        private val btn_paid_continue_total: Button = itemView.findViewById(R.id.btn_paid_continue_total)
        private val img_arrow_total: ImageView = itemView.findViewById(R.id.img_arrow_total)
        private val twv_error_total: TextView = itemView.findViewById(R.id.twv_error_total)

        @SuppressLint("SetTextI18n")
        override fun bin() = with(itemView) {
            val percent = pago.tarjeta!!.porcentajeGastosAdministrativos.toStringOpcionalDecimal() + "%"

            val percentCalculated = AdapterPagoHelper.calculatePercentajeAmmount(pago.estadoCuenta!!.deuda, pago.tarjeta!!.porcentajeGastosAdministrativos,iso!!)

            tvw_pago_bruto.text = pago.simboloMoneda.plus(" ".plus(pago.estadoCuenta!!.deudaFormateada))
            tvw_fecha_vencimiento.text = GlobalConstant.LABEL_VENCE.plus(pago.vencimientoDeuda.toDateAndFormat("dd/mm/yyyy", "dd/mm"))
            tvw_mnt_bruto_total.text = pago.simboloMoneda.plus(" ".plus(pago.estadoCuenta!!.deudaFormateada))
            porcentaje_total.text = pago.simboloMoneda.plus(" ".plus(decimalFormat.format(percentCalculated)))
            total_pagar.text = pago.simboloMoneda.plus(" ".plus(decimalFormat.format(pago.estadoCuenta!!.deuda!! + percentCalculated!!)))
            if (!pago.tarjeta!!.pagoEnLineaGastosLabel.isNullOrBlank()) {
                label_porcentaje_total.text = "".plus(pago.tarjeta!!.pagoEnLineaGastosLabel).plus(" (").plus(percent).plus("): ")
            }

            tvwTerminos_total.setOnClickListener {
                if (pago.tarjeta!!.tipoVisualizacion.equals(GlobalConstant.POPUP)) {
                    listener.openDialogTerm(pago.tarjeta!!.termCondicion!!, GlobalConstant.PAGO_TOTAL)
                } else {
                    AdapterPagoHelper.openPdfTerms(pago.tarjeta!!.termCondicion!!, it.context)
                }
            }

            if(pago.estadoCuenta!!.deuda==0.0)
                btn_paid_continue_total.isEnabled=false

            if(pago.estadoCuenta!!.deuda != null && pago.tarjeta!!.montoMinimoPago != null){
                if(pago.tarjeta!!.montoMinimoPago!! > pago.estadoCuenta!!.deuda!!){
                    btn_paid_continue_total.isEnabled = false
                    twv_error_total.text =  itemView.context.getText(R.string.monto_minimo).toString().plus(" " + pago.simboloMoneda).plus(" ".plus(pago.tarjeta!!.montoMinimoPago.toString()))
                    twv_error_total.visibility = View.VISIBLE
                }
            }

            btn_paid_continue_total.setOnClickListener {
                if (this.isEnabled) {
                    listener.goToDetailPay(ConfirmacionPagoModel().apply {
                        if (!pago.tarjeta!!.pagoEnLineaGastosLabel.isNullOrBlank()) {
                            this.labelGasto = pago.tarjeta!!.pagoEnLineaGastosLabel!!.plus(" (").plus(percent).plus(")")
                        }
                        this.tipoPago = GlobalConstant.PAGO_TOTAL
                        this.montoBruto = pago.estadoCuenta!!.deuda
                        this.porcentaje = percent
                        this.porcentajeBruto = percentCalculated
                        this.totalPagar = (pago.estadoCuenta!!.deuda!! + percentCalculated).toString()
                        this.url = pago.urlIcono
                        this.simboloMoneda = pago.simboloMoneda
                    })
                }
            }

            lnl_pago_total.setOnClickListener { toogle(lnr_continue) }
        }

        override fun show() {
            lnr_continue.visibility = View.VISIBLE
            img_arrow_total.rotation = 270f
        }

        override fun hide() {
            lnr_continue.visibility = View.GONE
            img_arrow_total.rotation = 90f
        }

    }

    inner class PagoParcialViewHolder(itemView: View) : TipoPagoViewHolder(itemView) {

        private val moneda_parcial: TextView = itemView.findViewById(R.id.moneda_parcial)
        private val edtmonto_parcial: EditText = itemView.findViewById(R.id.edtmonto_parcial)
        private val label_porcentaje: TextView = itemView.findViewById(R.id.label_porcentaje)
        private val tvwTerminos_parcial: TextView = itemView.findViewById(R.id.tvwTerminos_parcial)
        private val lnl_pago_parcial: LinearLayout = itemView.findViewById(R.id.lnl_pago_parcial)
        private val btn_paid_continue: Button = itemView.findViewById(R.id.btn_paid_continue)
        private val lnr_continue_parcial: LinearLayout = itemView.findViewById(R.id.lnr_continue_parcial)
        private val tvw_mnt_bruto_parcial: TextView = itemView.findViewById(R.id.tvw_mnt_bruto_parcial)
        private val porcentaje_parcial: TextView = itemView.findViewById(R.id.porcentaje_parcial)
        private val total_pagar_parcial: TextView = itemView.findViewById(R.id.total_pagar_parcial)
        private val twv_error_parcial: TextView = itemView.findViewById(R.id.twv_error_parcial)
        private val img_arrow_parcial: ImageView = itemView.findViewById(R.id.img_arrow_parcial)

        override fun bin() = with(itemView) {
            var percent = pago.tarjeta!!.porcentajeGastosAdministrativos.toStringOpcionalDecimal() + "%"

            moneda_parcial.text = pago.simboloMoneda
            label_porcentaje.text = pago.tarjeta!!.pagoEnLineaGastosLabel!!.plus(" (").plus(percent).plus("): ")

            tvwTerminos_parcial.setOnClickListener {
                if (pago.tarjeta!!.tipoVisualizacion.equals(GlobalConstant.POPUP)) {
                    listener.openDialogTerm(pago.tarjeta!!.termCondicion!!, GlobalConstant.PAGO_PARCIAL)
                } else {
                    AdapterPagoHelper.openPdfTerms(pago.tarjeta!!.termCondicion!!, it.context)
                }
            }

            if(pago.estadoCuenta!!.deuda==0.0)
                btn_paid_continue.isEnabled=false

            edtmonto_parcial.filters = arrayOf<InputFilter>(MoneyValueFilter())
            cleanLabels()
            edtmonto_parcial.onChange {

                    montoAux = it.convertToDouble(it)
                    calculateMount(it, percent)
                    montoAuxStr = it


            }

            if (montoAux > 0) edtmonto_parcial.setText(montoAuxStr)

            edtmonto_parcial.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    if (!edtmonto_parcial.text.toString().isNullOrBlank() && edtmonto_parcial.text.toString() != "." )
                        montoAux = edtmonto_parcial.text.toString().toDouble()
                    else if(expandedPosition != adapterPosition){
                        expandedPosition = adapterPosition
                        notifyDataSetChanged()
                    }
                }
            }
            btn_paid_continue.setOnClickListener {
                if (this.isEnabled) {
                    listener.goToDetailPay(ConfirmacionPagoModel().apply {
                        this.tipoPago = GlobalConstant.PAGO_PARCIAL
                        this.labelGasto = pago.tarjeta!!.pagoEnLineaGastosLabel!!.plus(" (").plus(percent).plus(")")
                        this.montoBruto = edtmonto_parcial.text.toString().toDouble()
                        this.porcentaje = percent
                        this.porcentajeBruto = porcentajeCalculado
                        this.totalPagar = pago.tipoPago[adapterPosition].previousMount.toString()//(edtmonto_parcial.text.toString().toDouble() + percentCalculated).toString()
                        this.url = pago.urlIcono
                        this.simboloMoneda = pago.simboloMoneda
                    })
                }
            }

            lnl_pago_parcial.setOnClickListener { toogle(lnr_continue_parcial) }
        }

        private fun cleanLabels() = with(itemView) {
            tvw_mnt_bruto_parcial.text = pago.simboloMoneda.plus(" 0.00")
            porcentaje_parcial.text = pago.simboloMoneda.plus(" 0.00")
            total_pagar_parcial.text = pago.simboloMoneda.plus(" 0.00")
        }

        fun EditText.onChange(cb: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    cb(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        fun calculateMount(it: String, percent: String) = with(itemView) {
            if (!it.isNullOrBlank()) {
                if (!it.endsWith(".") || !it.endsWith(",")) {
                    it.let { it1 ->
                        porcentajeCalculado = AdapterPagoHelper.calculatePercentajeAmmount(it1.convertToDouble(it1), pago.tarjeta!!.porcentajeGastosAdministrativos, iso!!)!!

                        if (pago.tarjeta!!.montoMinimoPago != null) {
                            if (it1.convertToDouble(it1) < pago.tarjeta!!.montoMinimoPago!!.toDouble()) {
                                var minimo: String = itemView.context.getText(R.string.monto_minimo).toString().plus(" " + pago.simboloMoneda)
                                twv_error_parcial.text = minimo.plus(" ".plus(pago.tarjeta!!.montoMinimoPago.toString()))
                                twv_error_parcial.visibility = View.VISIBLE
                                btn_paid_continue.isEnabled = false
                                cleanLabels()
                            } else {
                                if (it1.convertToDouble(it1) > (pago.estadoCuenta!!.deuda!! /*+ porcentajeCalculado!! */)) {
                                    twv_error_parcial.text = itemView.context.getText(R.string.monto_exceder)
                                    twv_error_parcial.visibility = View.VISIBLE
                                    btn_paid_continue.isEnabled = false
                                    cleanLabels()
                                } else {
                                    twv_error_parcial.visibility = View.GONE
                                    btn_paid_continue.isEnabled = true
                                    tvw_mnt_bruto_parcial.text = pago.simboloMoneda.plus(" ".plus(decimalFormat.format(it1.convertToDouble(it1))))
                                    porcentaje_parcial.text = pago.simboloMoneda.plus(" ".plus(decimalFormat.format(porcentajeCalculado!!)))
                                    pago.tipoPago[adapterPosition].previousMount = it1.convertToDouble(it1) + porcentajeCalculado!!
                                    total_pagar_parcial.text = pago.simboloMoneda.plus(" ".plus((
                                        decimalFormat.format(pago.tipoPago[adapterPosition].previousMount)).toString()))

                                }
                            }
                        } else { // no tiene monto minimo
                            twv_error_parcial.visibility = View.GONE
                            btn_paid_continue.isEnabled = true
                            label_porcentaje.text = pago.tarjeta!!.pagoEnLineaGastosLabel!!.plus(" (").plus(percent).plus("): ")
                            tvw_mnt_bruto_parcial.text = pago.simboloMoneda.plus(" ".plus(decimalFormat.format(it1.toDouble()))) //pago.simboloMoneda.plus(" ".plus(pago.estadoCuenta!!.deudaFormateada))
                            porcentaje_parcial.text = pago.simboloMoneda.plus(" ".plus(decimalFormat.format(porcentajeCalculado!!)))
                            total_pagar_parcial.text = pago.simboloMoneda.plus(" ".plus(decimalFormat.format((it1.toDouble() + porcentajeCalculado!!)).toString()))
                            pago.tipoPago[adapterPosition].previousMount = it1.toDouble() + porcentajeCalculado!!//0.0
                        }
                    }
                }
            } else {
                twv_error_parcial.visibility = View.GONE
                btn_paid_continue.isEnabled = false
                cleanLabels()
            }
        }

        override fun show() {
            lnr_continue_parcial.visibility = View.VISIBLE
            if (edtmonto_parcial.text.isNullOrEmpty()) {
                Handler().postDelayed({
                    edtmonto_parcial.requestFocus()
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(edtmonto_parcial, InputMethodManager.SHOW_IMPLICIT)
                }, 450)
            }
            if (montoAux > 0.0) {
                calculateMount(montoAux.toString(), pago.tarjeta!!.porcentajeGastosAdministrativos.toString() + "%")
            }
            img_arrow_parcial.rotation = 270f
        }

        override fun hide() {
            lnr_continue_parcial.visibility = View.GONE
            img_arrow_parcial.rotation = 90f
        }

    }

    /* Listener */
    interface TipoPagoListener {
        fun openDialogTerm(term: String, tipoPago: String)
        fun goToDetailPay(confirmacion: ConfirmacionPagoModel)
    }
}
