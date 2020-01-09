package biz.belcorp.consultoras.feature.payment.online

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.model.pagoonline.ConfirmacionPagoModel
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.common.model.pagoonline.ResultadoPagoModel
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.VisaConfig
import biz.belcorp.consultoras.feature.home.accountstate.AccountStateActivity
import biz.belcorp.consultoras.feature.payment.online.confirmacion.ConfirmacionFragment
import biz.belcorp.consultoras.feature.payment.online.confirmacion.ConfirmacionView
import biz.belcorp.consultoras.feature.payment.online.constancia.ConstanciaFragment
import biz.belcorp.consultoras.feature.payment.online.di.DaggerPaymentOnlineComponent
import biz.belcorp.consultoras.feature.payment.online.di.PaymentOnlineComponent
import biz.belcorp.consultoras.feature.payment.online.metodopago.MethodPaymentFragment
import biz.belcorp.consultoras.feature.payment.online.error.ErrorFragment
import biz.belcorp.consultoras.feature.payment.online.resultado.ResultadoFragment
import biz.belcorp.consultoras.feature.payment.online.tipopago.TipoPagoFragment
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pe.com.visanet.lib.VisaNetConfigurationContext
import pe.com.visanet.lib.VisaNetPaymentActivity
import pe.com.visanet.lib.VisaNetPaymentInfo

class PayOnlineActivity : BaseActivity(), HasComponent<PaymentOnlineComponent>,
    LoadingView, MethodPaymentFragment.MethodListener, TipoPagoFragment.TypeListener,
    ConfirmacionFragment.VisaListener,
    ResultadoFragment.ResultadoFragmentListener{

    private val REQUEST_CODE_PAYMENT = 1

    private var fragment: MethodPaymentFragment? = null  //TODO no debemos tener referencia
    private var confirmacionFragment: ConfirmacionFragment? = null
    private var component: PaymentOnlineComponent? = null
    private var configuracion: PagoOnlineConfigModel? = null
    private var visaConfig: VisaConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_base)
        initializeInjector()
        init(savedInstanceState)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if (NetworkUtil.isThereInternetConnection(this))
            setStatusTopNetwork()
        else {
            viewConnection.visibility = android.view.View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        initializeToolbar()
        if (savedInstanceState == null) goToFragmentMetodoPago()
    }

    override fun initializeInjector() {
        component = DaggerPaymentOnlineComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun initEvents() {}

    override fun getComponent(): PaymentOnlineComponent? {
        return component
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkEvent(event: NetworkEvent) {
        when (event.event) {
            NetworkEventType.CONNECTION_AVAILABLE -> {
                SyncUtil.triggerRefresh()
                setStatusTopNetwork()
            }
            NetworkEventType.CONNECTION_NOT_AVAILABLE -> {
                viewConnection.visibility = android.view.View.VISIBLE
                tvw_connection_message.setText(R.string.connection_offline)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                viewConnection.visibility = android.view.View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> viewConnection.visibility = android.view.View.GONE
            else -> viewConnection.visibility = android.view.View.GONE
        }
    }

    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.DATAMI_AVAILABLE -> {
                viewConnection.visibility = android.view.View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> viewConnection.visibility = android.view.View.GONE
            else -> viewConnection.visibility = android.view.View.GONE
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager


        when {
            fragmentManager.backStackEntryCount > 0 -> fragmentManager.popBackStack()
            else -> {
                val returnIntent = Intent()
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        }
    }

    override fun showLoading() {
        viewLoading.visibility = android.view.View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = android.view.View.GONE
    }

    private fun initializeToolbar() {
        tvw_toolbar_title.setText(R.string.pago_en_linea)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }
    }

    override fun setCardSelected(idCardSelected: Int, rutaIcono: String) {
        configuracion!!.let {
            it.listaMetodoPago!!.forEach { it1 ->
                if (it1.medioPagoDetalleId == idCardSelected) {
                    goToFragmentTipoPago(it.estadoCuenta!!, it1, it.listaTipoPago!!, rutaIcono)
                }
            }
        }
    }

    override fun setConfiguration(config: PagoOnlineConfigModel) {
        this.configuracion = config
    }

    override fun dispatchTouchEvent(event: MotionEvent) : Boolean {
        var view:View? = currentFocus
        var ret:Boolean = super.dispatchTouchEvent(event)
        view?.let{viewValidate ->
            if (viewValidate is EditText) {
                var w:View = viewValidate
                var scrcoords = IntArray(2)
                w.getLocationOnScreen(scrcoords)
                var x : Float = event.rawX + w.left - scrcoords[0]
                var y : Float = event.rawY + w.top - scrcoords[1]
                if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right || y < w.top || y > w.bottom) ) {
                    var imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    window?.currentFocus?.let{
                        imm.hideSoftInputFromWindow(it.windowToken, 0)
                    }
                }
            }
        }


        return ret
    }

    /* Util */
    fun hideKeyboard() {
        val inputMethodManager = context()!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this!!.currentFocus
        if (view == null) view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /* Flow Fragments */
    override fun goToPagoVisaConfirmacion(confirmacion: ConfirmacionPagoModel) {
        hideKeyboard()
        confirmacionFragment = ConfirmacionFragment.newInstance(confirmacion)
        replaceFragment(R.id.fltContainer, confirmacionFragment!!, false)
    }

    fun goToFragmentMetodoPago() {
        hideKeyboard()
        fragment = MethodPaymentFragment()
        replaceFragment(R.id.fltContainer, fragment!!, true)
    }

    fun goToFragmentTipoPago(estadoCuenta: PagoOnlineConfigModel.EstadoCuenta, metodoPago: PagoOnlineConfigModel.MetodoPago, tipoPago: List<PagoOnlineConfigModel.TipoPago>, rutaIcono: String) {
        hideKeyboard()
        val fragmentTipoPago = TipoPagoFragment.newInstance(estadoCuenta, metodoPago, tipoPago, rutaIcono)
        replaceFragment(R.id.fltContainer, fragmentTipoPago, false)
    }

    fun goToFragmentResultadoRejected(resultadoPagoRechazadoModel: ResultadoPagoModel.ResultadoPagoRechazadoModel) {
        hideKeyboard()
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replaceFragment(R.id.fltContainer, ResultadoFragment.newInstance(resultadoPagoRechazadoModel), true)
    }

    fun goToFragmentResultadoSuccess(resultadoPagoModel: ResultadoPagoModel) {
        hideKeyboard()
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replaceFragment(R.id.fltContainer, ResultadoFragment.newInstance(resultadoPagoModel), true)
    }

    fun goToFragmentError() {
        hideKeyboard()
        replaceFragment(R.id.fltContainer, ErrorFragment.newInstance(), false)
    }

    /* Implementacion de Visa Listener */
    override fun setConfiguration(config: VisaConfig) {
        this.visaConfig = config
    }

    override fun getPasarelaVisa(nextCounter: String, config: VisaConfig, confirm: ConfirmacionPagoModel) {
        config.let {
            val ctx = VisaNetConfigurationContext()
            ctx.let {
                it.endPointURL = config!!.endPointUrl
                //it.endPointURL = "https://devapi.vnforapps.com/api.tokenization/api/v2"

                it.data = HashMap<String, String>()
                it.accessKeyId = config!!.accessKeyId
                it.secretAccess = config!!.secretAccessKey
                it.merchantId = config!!.merchantId
                it.currency = VisaNetConfigurationContext.VisaNetCurrency.PEN //TODO MONEDA
                it.amount = confirm.totalPagar!!.toDouble()
                it.transactionId = nextCounter
                it.externalTransactionId = config!!.sessionToken
                it.userTokenId = when (config!!.tokenTarjetaGuardada) {
                    "" -> null
                    else -> config!!.tokenTarjetaGuardada
                }
            }
            var intent = Intent(context(), VisaNetPaymentActivity::class.java)
            intent.putExtra(VisaNetConfigurationContext.VISANET_CONTEXT, ctx)
            startActivityForResult(intent, REQUEST_CODE_PAYMENT)
        }
    }

    override fun onHome() {
        this.navigator.navigateToHome(this, null)
        this.finish()
    }

    override fun onTryAgain() {
        goToFragmentMetodoPago()
    }

    override fun goToAccountStatus() {
        val intent = Intent(this, AccountStateActivity::class.java)
        intent.putExtra("RELOAD_DEUDA",true)
        startActivity(intent)
        this.finish()
    }

    override fun goToConstancia(entity: ResultadoPagoModel) {
        hideKeyboard()
        tvw_toolbar_title.text = "CONSTANCIA DE PAGO"
        val constanciaFragment = ConstanciaFragment.newInstance(entity)
        replaceFragment(R.id.fltContainer, constanciaFragment, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYMENT) {
            var visaNetPaymentInfo = when (data) {
                null -> null
                else -> (data.getSerializableExtra(VisaNetConfigurationContext.VISANET_CONTEXT) as VisaNetPaymentInfo)
            }
            (confirmacionFragment as ConfirmacionView?)!!.onResultPayment(resultCode, visaNetPaymentInfo, visaConfig!!)
        }
    }

}
