package biz.belcorp.consultoras.feature.payment.online.metodopago

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.payment.online.di.PaymentOnlineComponent
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.fragment_payment_method.*
import javax.inject.Inject

class MethodPaymentFragment: BaseFragment(), PaymentOnlineView, OptionsPayListAdapter.OnCardItemSelected {


    @Inject
    lateinit var presenter: PaymentOnlinePresenter
    private var listener: MethodListener? = null
    private var adapterMethod: OptionsPayListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
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
        presenter.fillUser()
    }

   override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is MethodListener){
            this.listener=context
        }
   }

   override fun getInitialConfig(config: PagoOnlineConfigModel) {
        listener?.setConfiguration(config!!)

        val opc = presenter.formatBancs(config!!.listaMetodoPago!!,config.listaMedioPago!!,config.listaBanco)
        adapterMethod = OptionsPayListAdapter(opc.listaOpcionPago!!, opc.listaBancoOnline, this)
        if(rvwmethod!=null) {
            rvwmethod.setHasFixedSize(true)
            rvwmethod.isNestedScrollingEnabled = false
            rvwmethod.adapter = adapterMethod

        }
   }

   override fun cardSelected(rutaIcono: String, medioPagoDetalleId: Int?, description: String?) {
        //presenter.setCard(rutaIcono, medioPagoDetalleId,description)
       var name =  description!!.replace("(", "").replace(")", "").trim()
       Tracker.trackEvent(
           GlobalConstant.SCREEN_PAGO_EN_LINEA,
           GlobalConstant.EVENT_NAME_PAGO_LINEA,
           GlobalConstant.EVENT_ACTION_METODO_PAGO,
           name,
           GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
           presenter.user)

       if(medioPagoDetalleId!=null && !rutaIcono.isNullOrBlank())
           listener?.setCardSelected(medioPagoDetalleId,rutaIcono)
   }

    override fun bankSelectedTrack(nombreBanco: String) {

        Tracker.trackEvent(
            GlobalConstant.SCREEN_PAGO_EN_LINEA,
            GlobalConstant.EVENT_NAME_PAGO_LINEA,
            GlobalConstant.EVENT_ACTION_BANCA_INTERNET,
            nombreBanco,
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
            presenter.user)
    }

   interface MethodListener {
        fun setConfiguration(config: PagoOnlineConfigModel)
        fun setCardSelected(idCardSelected: Int, rutaIcono: String)
   }

}

