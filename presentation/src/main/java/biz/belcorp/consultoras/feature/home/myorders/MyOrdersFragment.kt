package biz.belcorp.consultoras.feature.home.myorders

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.menu.MenuModel
import biz.belcorp.consultoras.common.model.order.MyOrderModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.client.order.history.ClientOrderHistoryWebActivity
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity
import biz.belcorp.consultoras.feature.embedded.pedidospendientes.PedidosPendientesActivity
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.survey.SurveyBottomDialogFragment
import biz.belcorp.consultoras.feature.home.tracking.TrackingActivity
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.ToastUtil
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.consultoras.util.anotation.OrderType
import biz.belcorp.consultoras.util.anotation.SurveyValidationType
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.fragment_my_orders.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

/** Clase
 * Mis Pedidos */

class MyOrdersFragment : BaseFragment(), MyOrdersView,
    IngresadosListAdapter.OnListener,
    FacturadosListAdapter.OnListener {

    @Inject
    lateinit var presenter: MyOrdersPresenter

    private var loginModel: LoginModel? = null
    private var pdfActive = false

    private lateinit var adapterIngresados: IngresadosListAdapter
    private lateinit var adapterFacturados: FacturadosListAdapter

    private var numeroPedido: String? = null

    private var listener: Listener? = null

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(ClientComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.data()
        presenter.evaluatePendingOrders()
        init()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> {
                presenter.data()
                presenter.evaluatePendingOrders()
            }
            REQUEST_CODE_PENDING -> {
                presenter.evaluatePendingOrders()
            }
        }
    }

    /** Interface */

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    override fun onError(throwable: Throwable) {
        processError(throwable)
    }

    override fun initScreenTrack(loginModel: LoginModel) {
        this.loginModel = loginModel
        Tracker.trackScreen(GlobalConstant.SCREEN_MY_ORDERS, loginModel)
    }

    override fun trackBackPressed(loginModel: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_MY_ORDERS, loginModel)
    }

    override fun showOrders(list: List<MyOrderModel>) {

        val ingresados = ArrayList<MyOrderModel>()
        val facturados = ArrayList<MyOrderModel>()

        for (myOrderModel in list) {
            if (myOrderModel.estadoPedidoDesc == OrderType.INGRESADO) {
                ingresados.add(myOrderModel)
            } else if (myOrderModel.estadoPedidoDesc == OrderType.FACTURADO) {
                facturados.add(myOrderModel)
            }
        }

        showIngresados(ingresados)
        showFacturados(facturados)
    }

    override fun showMenuOrder(menu: MenuModel?) {
        if (menu != null)
            if (menu.isVisible!! && menu.codigo == MenuCodeTop.ORDERS) {
                val intent = Intent(context, OrderWebActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, REQUEST_CODE)
            } else if (menu.isVisible!! && menu.codigo == MenuCodeTop.ORDERS_NATIVE) {
                val intent = Intent(context, AddOrdersActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, REQUEST_CODE)
            }
    }

    override fun openPDF(base64: String) {

        val file = File(context?.getExternalFilesDir("PDFS"), " $numeroPedido.pdf")

        try {
            val decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64.toByteArray())
            val os = FileOutputStream(file)
            os.write(decoded)
            os.flush()
            os.close()

            showPDF(file)
            hideLoading()

        } catch (e: Exception) {
            e.printStackTrace()
            hideLoading()
            showPDFError()
        }

    }

    override fun showPDFError() {
        context?.let {
            ToastUtil.show(it, resources.getString(R.string.my_orders_not_available), Toast.LENGTH_LONG)
        }
    }

    override fun activePDF() {
        this.pdfActive = true
    }

    override fun showPedidosPendientesButton(pedidosPendientes: Int, isEnabled: Boolean) {
        hideLoading()
        pendingOrdersButton.text = String.format(getString(R.string.pending_orders_count), pedidosPendientes.toString())
        pendingOrdersButton.visibility = if(isEnabled) View.VISIBLE else View.INVISIBLE
        pendingOrdersButton.isEnabled = isEnabled
    }

    /** private functions */

    fun init() {

        llt_ingresar_pedido.setOnClickListener {
            ingresarPedido()
        }

        pendingOrdersButton.setOnClickListener {
            startActivityForResult(Intent(activity, PedidosPendientesActivity::class.java), REQUEST_CODE_PENDING)
        }
    }

    fun ingresarPedido() {
        if (!NetworkUtil.isThereInternetConnection(context()!!)) {
            showNetworkError()
            return
        }
        listener?.goToAddOrders()
    }

    fun trackBackPressed() {
        presenter.trackBackPressed()
    }


    fun showIngresados(ingresados: List<MyOrderModel>?) {
        if (ingresados != null && !ingresados.isEmpty()) {
            adapterIngresados = IngresadosListAdapter(context(), ingresados, this)
            rvw_ingresados.adapter = adapterIngresados
            rvw_ingresados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvw_ingresados.setHasFixedSize(true)
            rvw_ingresados.isNestedScrollingEnabled = false
            rvw_ingresados.visibility = View.VISIBLE
            llt_header_ingresados.visibility = View.VISIBLE
            llt_content_ingresados.visibility = View.VISIBLE
            tvw_ingresados.visibility = View.VISIBLE
            llt_ingresados_empty.visibility = View.GONE
            tvw_ingresados.text = String.format(getString(R.string.my_orders_ingresados), ingresados.size)
        } else {
            tvw_ingresados.visibility = View.VISIBLE
            tvw_ingresados.text = String.format(getString(R.string.my_orders_ingresados), 0)
            llt_header_ingresados.visibility = View.VISIBLE
            llt_ingresados_empty.visibility = View.VISIBLE
            llt_content_ingresados.visibility = View.GONE
            rvw_ingresados.visibility = View.GONE
        }
    }

    fun showFacturados(facturados: List<MyOrderModel>?) {
        if (facturados != null && !facturados.isEmpty()) {
            adapterFacturados = FacturadosListAdapter(facturados, pdfActive, this)
            rvw_facturados.adapter = adapterFacturados
            rvw_facturados.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvw_facturados.setHasFixedSize(true)
            rvw_facturados.isNestedScrollingEnabled = false
            llt_header_facturados.visibility = View.VISIBLE
            tvw_facturados.text = String.format(getString(R.string.my_orders_facturados), facturados.size)
        } else {
            llt_header_facturados.visibility = View.GONE
        }
    }

    fun goToSeguimiento(myOrderModel: MyOrderModel) {

        /*
        trackEvent(
            GlobalConstant.EVENT_CAT_MY_ORDERS,
            GlobalConstant.EVENT_ACTION_MY_ORDERS_TRACKING,
            String.valueOf(myOrderModel.getCampaniaID()),
            GlobalConstant.EVENT_NAME_MY_ORDERS);
        */

        val intent = Intent(activity, TrackingActivity::class.java)
        intent.putExtra(GlobalConstant.TRACKING_TOP, adapterFacturados.itemCount)
        intent.putExtra(GlobalConstant.CAMPAIGN_KEY, myOrderModel.campaniaID)
        intent.putExtra(GlobalConstant.DATE_KEY, myOrderModel.fechaRegistro)
        startActivity(intent)
    }

    fun goToPaqDoc(myOrderModel: MyOrderModel) {
        numeroPedido = myOrderModel.numeroPedido.toString()

        if (pdfActive) {
            val file = File(context?.getExternalFilesDir("PDFS"), " $numeroPedido.pdf")
            if (file.exists()) {
                showPDF(file)
            } else {
                presenter.paqueteDocumentario(numeroPedido)
            }
        } else if (!myOrderModel.rutaPaqueteDocumentario.isEmpty()) {
            try {
                trackEvent(
                    GlobalConstant.EVENT_CAT_MY_ORDERS,
                    GlobalConstant.EVENT_ACTION_MY_ORDERS_PAQDOC, myOrderModel.numeroPedido.toString(),
                    GlobalConstant.EVENT_NAME_PAQUETE_DOC)

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(myOrderModel.rutaPaqueteDocumentario))
                startActivity(browserIntent)

            } catch (ex: ActivityNotFoundException) {
                ToastUtil.show(context!!, R.string.my_orders_not_available_app, Toast.LENGTH_SHORT)
            }
        } else {
            ToastUtil.show(context!!, R.string.my_orders_not_available, Toast.LENGTH_SHORT)
        }
    }

    private fun showPDF(file: File) {
        context?.let {
            val uri = FileProvider.getUriForFile(it, GlobalConstant.PROVIDER_FILE, file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.setDataAndType(uri, "application/pdf")
            startActivity(intent)
        }
    }

    fun goToDetalle(myOrderModel: MyOrderModel) {

        /*
        trackEvent(
            GlobalConstant.EVENT_CAT_MY_ORDERS,
            GlobalConstant.EVENT_ACTION_MY_ORDERS_DETAIL,
            String.valueOf(myOrderModel.getCampaniaID()),
            GlobalConstant.EVENT_NAME_MY_ORDERS);
        */

        val intent = Intent(activity, ClientOrderHistoryWebActivity::class.java)
        intent.putExtra(GlobalConstant.CLIENTE_ID, 0)
        intent.putExtra(GlobalConstant.CAMPAIGN_KEY, myOrderModel.campaniaID)
        startActivity(intent)
    }

    fun trackEvent(category: String, action: String, label: String, eventName: String) {
        val analytics = Bundle()
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_MY_ORDERS)
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, category)
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, action)
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, label)
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        val properties = AnalyticsUtil.getUserProperties(loginModel)
        BelcorpAnalytics.trackEvent(eventName, analytics, properties)
    }

    /** FacturadosListAdapter.OnListener */

    override fun onSeguimientoClick(myOrderModel: MyOrderModel) {
        goToSeguimiento(myOrderModel)
    }

    override fun onPaqDocClick(myOrderModel: MyOrderModel) {
        goToPaqDoc(myOrderModel)
    }

    override fun onDetalleClick(myOrderModel: MyOrderModel) {
        goToDetalle(myOrderModel)
    }

    override fun onSurveyOptionClick(myOrderModel: MyOrderModel?) {
        showLoading()
        val survey = SurveyBottomDialogFragment.newInstance(
            SurveyValidationType.VALIDATION_SURVEY_MY_ORDERS,
            myOrderModel?.campaniaID.toString(), ClientComponent::class.java)
        survey.surveyDialogCallbacks = object : SurveyBottomDialogFragment.SurveyDialogCallbacks {
            override fun onDismiss() {
                presenter.data()
            }

            override fun onLoadSurvey() {
                hideLoading()
                Tracker.Survey.clickEncuesta("C${myOrderModel?.campaniaID.toString().substring(4)}", loginModel)
            }
        }
        survey.show(fragmentManager, SurveyBottomDialogFragment.TAG)
    }

    interface Listener {
        fun goToAddOrders()
    }

    /** Class  */

    companion object {

        const val REQUEST_CODE = 999
        const val REQUEST_CODE_PENDING = 1000

        fun newInstance(): MyOrdersFragment {
            return MyOrdersFragment()
        }
    }
}
