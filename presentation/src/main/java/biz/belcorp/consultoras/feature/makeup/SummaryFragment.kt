package biz.belcorp.consultoras.feature.makeup

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.entity.ProductoMasivo
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.makeup.di.SummaryComponent
import biz.belcorp.consultoras.util.CommunicationUtils
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import kotlinx.android.synthetic.main.fragment_summary.*
import permissions.dispatcher.*
import java.lang.Exception
import java.text.DecimalFormat
import javax.inject.Inject


@RuntimePermissions
class SummaryFragment : BaseFragment(), SummaryView {

    @Inject
    lateinit var presenter: SummaryPresenter
    private var tabAdapter: SummaryTabAdapter? = null
    private var user: User? = null
    private var decimalFormat: DecimalFormat? = null
    private var moneySymbol: String? = null
    private lateinit var summaryListener: Listener
    private lateinit var menuOrder: Menu

    var productosCP = mutableListOf<ProductoMasivo>()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            summaryListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(SummaryComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_SUMMARY, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_SUMMARY, model)
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    // Private  Functions
    @SuppressLint("SetTextI18n")
    private fun init() {
        btnContinuar.setOnClickListener {
            menuOrder.let { summaryListener.goToOrder(it) }
        }

        btnRegresar.setOnClickListener {
            summaryListener.onRegresar()
        }

        tvwSubtitle.visibility = View.GONE
        lltRejected.visibility = View.GONE

        getMQVirtualPermissionWithPermissionCheck()
    }

    val uri = Uri.parse(BuildConfig.APP_MQVIRTUAL_CONTENT_PROVIDER)!!

    /** The name of the name column.  */
    val CUV = "CUV"
    val DESCRIPCION = "Descripcion"
    val CANTIDAD = "Cantidad"
    val MARCA_ID = "MarcaID"
    val MARCA_DESCRIPCION = "MarcaDescripcion"
    val CLIENTE_ID = "ClienteID"
    val CLIENTE_DESCRIPCION = "ClienteDescripcion"
    val PRECIO_CATALOGO = "PrecioCatalogo"
    val TONO = "Tono"
    val URL_IMAGEN = "URLImagen"
    val ID_ORDER = "IdOrder"

    var i = 0
    private val mLoaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return context?.let {
                when (id) {
                    LOADER ->  {
                        return CursorLoader(it,uri,
                            null, null, null, null)
                    }
                    else -> throw IllegalArgumentException()
                }
            }!!
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            when (loader.id) {
                LOADER -> {
                    data?.let {
                        var productoCP: ProductoMasivo? = null
                        while (it.moveToPosition(i)) {
                            productoCP = ProductoMasivo().apply {
                                this.cuv = it.getString(it.getColumnIndexOrThrow(CUV))
                                this.descripcion = it.getString(it.getColumnIndexOrThrow(DESCRIPCION))
                                this.cantidad = it.getInt(it.getColumnIndexOrThrow(CANTIDAD))
                                this.marcaId = it.getInt(it.getColumnIndexOrThrow(MARCA_ID))
                                this.marcaDescripcion = it.getString(it.getColumnIndexOrThrow(MARCA_DESCRIPCION))
                                this.clienteId = it.getInt(it.getColumnIndexOrThrow(CLIENTE_ID))
                                this.clienteDescripcion = it.getString(it.getColumnIndexOrThrow(CLIENTE_DESCRIPCION))
                                this.precioCatalogo = it.getDouble(it.getColumnIndexOrThrow(PRECIO_CATALOGO))
                                this.tono = it.getString(it.getColumnIndexOrThrow(TONO))
                                this.urlImagen = it.getString(it.getColumnIndexOrThrow(URL_IMAGEN))
                                this.idOrder = it.getLong(it.getColumnIndexOrThrow(ID_ORDER))
                            }
                            productosCP.add(productoCP)
                            i++
                        }

                        if(productosCP.isEmpty()){
                            showFullScreenErrorWithListener("Error", "No se ha enviado ningun producto.",  object : FullScreenDialog.FullScreenDialogListener {
                                override fun onBackPressed(dialog: FullScreenDialog) {
                                    dialog.dismiss()
                                    activity?.finish()
                                }

                                override fun onDismiss() {
                                    //EMPTY
                                }
                                override fun onClickAceptar(dialog: FullScreenDialog) {
                                    dialog.dismiss()
                                    activity?.finish()
                                }
                                override fun onClickAction(dialog: FullScreenDialog) {
                                    // EMPTY
                                }
                            })
                        }
                        presenter.getUser()
                    }

                }
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            when (loader.id) {
                //LOADER -> mCheeseAdapter.setCheeses(null)
            }
        }

    }

    override fun onErrorMessage(exception: Throwable) {
        showFullScreenErrorWithListener("Error", exception.localizedMessage,
            object : FullScreenDialog.FullScreenDialogListener {
            override fun onBackPressed(dialog: FullScreenDialog) {
                dialog.dismiss()
                activity?.finish()
            }

            override fun onDismiss() {
                // EMPTY
            }
            override fun onClickAceptar(dialog: FullScreenDialog) {
                dialog.dismiss()
                activity?.finish()
            }
            override fun onClickAction(dialog: FullScreenDialog) {
                // EMPTY
            }
        })
    }

    var agregadosSize = 0
    var rechazadosSize = 0

    override fun setUser(user: User?) {
        this.user = user
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(user?.countryISO!!, true)
        this.moneySymbol =  user?.countryMoneySymbol

        presenter.insercionMasivaPedido(productosCP.toList(), DeviceUtil.getId(context))
    }

    override fun onGetMenu(menu: Menu) {
        this.menuOrder = menu
    }

    override fun postInit(productosAgregados: List<ProductoMasivo>,
                          productosRechazados: List<ProductoMasivo>){
        agregadosSize = productosAgregados.size
        rechazadosSize = productosRechazados.size

        tabAdapter = fragmentManager?.let { SummaryTabAdapter(it) }
        tabAdapter?.dataAccepted = productosAgregados
        tabAdapter?.dataRejected = productosRechazados
        tabAdapter?.setData(user?.countryMoneySymbol ?: "", decimalFormat)
        viewPagerSummary.adapter = tabAdapter
        tabSummary.setupWithViewPager(viewPagerSummary)


        val textSubtitle = String.format(resources.getString(R.string.summary_subtitle)
            , user?.primerNombre)
        val textAgregados = resources.getQuantityString(R.plurals.summary_added_messages, agregadosSize
            , user?.primerNombre, agregadosSize)
        val textRechazados = resources.getQuantityString(R.plurals.summary_rejected_messages, rechazadosSize
            , user?.primerNombre, rechazadosSize)

        if(!productosAgregados.isEmpty() && !productosRechazados.isEmpty()){
            tabSummary.visibility = View.VISIBLE
            tvwSubtitle.visibility = View.VISIBLE
            lltRejected.visibility = View.GONE
            tvwSubtitle.text = textSubtitle
        } else if(productosAgregados.isEmpty()){
            tabSummary.visibility = View.GONE
            tvwSubtitle.visibility = View.GONE
            lltRejected.visibility = View.VISIBLE
            viewPagerSummary.arrowScroll(View.FOCUS_RIGHT)
            viewPagerSummary.setPagingEnabled(false)
            tvwRejected.text = textRechazados
        } else if(productosRechazados.isEmpty()){
            tabSummary.visibility = View.GONE
            tvwSubtitle.visibility = View.VISIBLE
            lltRejected.visibility = View.GONE
            viewPagerSummary.setPagingEnabled(false)
            tvwSubtitle.text = textAgregados
        }

        loadingView?.hideLoading()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    /**  Permission read provider MQVirtual  */

    @NeedsPermission(GlobalConstant.PERMISSION_MQVIRTUAL)
    fun getMQVirtualPermission() {
        try {
            loadingView?.showLoading()
            activity?.supportLoaderManager?.initLoader(LOADER, null, mLoaderCallbacks)
        } catch (e: Exception){
            loadingView?.hideLoading()
            BelcorpLogger.d(e)
        }
    }

    @OnShowRationale(GlobalConstant.PERMISSION_MQVIRTUAL)
    fun showRationaleForMQVirtualPermission(request: PermissionRequest) {
        AlertDialog.Builder(context!!)
            .setMessage(R.string.permission_provider_mqvirtual_rationale)
            .setPositiveButton(R.string.button_aceptar) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.button_cancelar) { _, _ -> request.cancel() }
            .show()
    }

    @OnPermissionDenied(GlobalConstant.PERMISSION_MQVIRTUAL)
    fun showDeniedForMQVirtualPermission() {
        this@SummaryFragment.activity?.finish()
    }

    @OnNeverAskAgain(GlobalConstant.PERMISSION_MQVIRTUAL)
    fun showNeverAskForMQVirtualPermission() {
        Toast.makeText(context, R.string.permission_provider_mqvirtual_neverask,
            Toast.LENGTH_SHORT).show()
        AlertDialog.Builder(context!!)
            .setMessage(R.string.permission_provider_mqvirtual_denied)
            .setPositiveButton(R.string.button_go_to_settings) { _, _ ->
                CommunicationUtils.goToSettings(context!!)
                this@SummaryFragment.activity?.finish()
            }
            .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                dialog.dismiss()
                this@SummaryFragment.activity?.finish() }
            .show()

    }

    /** Summary  */

    companion object {
        const val LOADER = 1
    }

    /** Interfaces */

    interface Listener {
        fun onRegresar()
        fun goToOrder(menu: Menu)
    }

}
