package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.demostrador

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.entity.caminobrillante.DemostradorCaminoBrillante
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.caminobrillante.di.CaminoBrillanteComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.OrderOriginCode
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.core.extensions.setSafeOnClickListener
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.design.filter.FilterPickerDialog
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel
import biz.belcorp.mobile.components.design.filter.model.FilterModel
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.dialogs.list.ListDialog
import biz.belcorp.mobile.components.dialogs.list.adapters.ListDialogAdapter
import biz.belcorp.mobile.components.dialogs.list.model.ListDialogModel
import kotlinx.android.synthetic.main.fragment_kit_demostrador.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_offers_filters.*
import java.text.DecimalFormat
import javax.inject.Inject

class DemostradorFragment : BaseFragment(), DemostradorView, SafeLet {

    @Inject
    lateinit var presenter: DemostradorPresenter

    private lateinit var demostradores: ArrayList<DemostradorCaminoBrillante?>

    private lateinit var demostradorAdapter: DemostradorAdapter
    private lateinit var demostradorLayoutManager: GridLayoutManager
    private var mLastClickTime: Long = 0
    private lateinit var listener: DemostradorListener

    private var filtros: ArrayList<CategoryFilterModel>? = null
    private var filtro: String? = null

    private var orders: ArrayList<ListDialogModel>? = null
    private var orden: String? = null

    val listDialogOrder = ListDialog()

    private lateinit var user: User

    companion object {
        private var decimalFormat = DecimalFormat()
        private var moneySymbol: String = ""

        fun formatWithMoneySymbol(precio: Double): String {
            return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
        }

        fun newInstance(): DemostradorFragment {
            return DemostradorFragment()
        }
    }

    override fun context(): Context {
        return activity?.applicationContext!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CaminoBrillanteActivity) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kit_demostrador, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(CaminoBrillanteComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        init()
    }

    private fun init() {
        initViews()
        setUpListener()
        presenter.getUser()
    }

    private fun initViews() {
        lltOffersFilters.visibility = View.VISIBLE

        this.demostradores = arrayListOf()

        demostradorLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        demostradorAdapter = DemostradorAdapter(null)

        rvwOfertas.layoutManager = demostradorLayoutManager
        rvwOfertas.adapter = demostradorAdapter
        rvwOfertas.addOnScrollListener(GridScrollListener())
    }

    private fun setUpListener() {
        demostradorAdapter.listener = object : DemostradorAdapter.Listener {
            override fun onClickAdd(counterView: Counter, demostrador: DemostradorCaminoBrillante, quantity: Int, position: Int) {
                if (tiempoSuficiente())
                    presenter.addDemostrador(counterView, demostrador, quantity, position, DeviceUtil.getId(context))
            }

            override fun onClickItem(demostrador: DemostradorCaminoBrillante, position: Int) {
                Tracker.CaminoBrillante.onSelectProduct(user, GlobalConstant.LIST_DEMOSTRADORES, demostradorToProduct(demostrador, position))
                listener.onClickItem(demostrador.cuv ?: StringUtil.Empty, demostrador.marcaID
                    ?: 0, demostrador.descripcionMarca
                    ?: StringUtil.Empty, OrderOriginCode.CAMINO_BRILLANTE_OFERTAS_OFERTAS_ESPECIALES_FICHA)
            }
        }

        btn_volver_intentar.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                presenter.loadDemostradores(orden, filtro, null, null)
            }
        }

        listDialogOrder.setListener(object : ListDialog.ListItemDialogListener {
            override fun clickItem(position: Int) {
                filtrarYOrdenar(position, true, filtrar = false)
                listDialogOrder.dismiss()
            }
        })

        lnlFiltro.setSafeOnClickListener {
            context?.let { context ->
                this.filtros?.let {
                    FilterPickerDialog.Builder(context)
                        .setCategories(it)
                        .setOnFilterPickerListener(object : FilterPickerDialog.FilterPickerListener {
                            override fun onButtonClicked(buttonName: String?) {
                            }

                            override fun onItemRemoved(filterName: String?) {
                            }

                            override fun onItemSelected(filterName: String?, filterGroup: String?) {
                            }

                            override fun onApply(dialog: Dialog, filters: ArrayList<CategoryFilterModel>) {
                                filtros = filters
                                filtrarYOrdenar(null, false, filtrar = true)
                                dialog.dismiss()
                            }

                            override fun onClose(dialog: Dialog) {
                                dialog.dismiss()
                            }
                        })
                        .show()
                }
            }
        }

        lltOrderContainer.setOnClickListener {
            orders?.let {
                listDialogOrder.show(fragmentManager, "listorder_fragment")
            }
        }
    }

    private fun filtrarYOrdenar(position: Int?, ordenar: Boolean, filtrar: Boolean) {
        if (ordenar) {
            orden = getOrden(position)
        }
        if (filtrar) {
            filtro = getFiltro()
        }

        scrollTopRecyclerView()

        presenter.loadDemostradores(orden, filtro, null, null)
    }

    private fun getOrden(posicion: Int?): String? {
        var marcaId: String? = null
        posicion?.let {
            marcaId = orders?.get(it)?.key
        }
        return marcaId
    }

    private fun getFiltro(): String? {
        var marcaId: String? = null
        filtros?.forEach {
            it.list.forEach {
                if (it.checked) {
                    marcaId = it.key
                }
            }
        }
        return marcaId
    }

    override fun setUser(user: User) {
        this.user = user
        decimalFormat = CountryUtil.getDecimalFormatByISO(user.countryISO, true)
        moneySymbol = user.countryMoneySymbol?.let { it } ?: StringUtil.Empty

        presenter.loadDemostradores(orden, filtro, null, null)
        presenter.loadFiltrosYOrdenamiento()
    }

    override fun loadDemostradores() {
        presenter.loadDemostradores(orden, filtro, null, null)
    }

    override fun showDemostradores(demostradores: List<DemostradorCaminoBrillante>) {
        this.demostradores.clear()
        this.demostradores.addAll(demostradores)

        setCantidadOferta(demostradores.size)

        rvwOfertas.visibility = View.VISIBLE
        demostradorAdapter.setData(demostradores)

        if (listener.getCurrentPosition() == 1) {
            rvwOfertas.postDelayed({ onItemImpression() }, 200)
        }
    }

    private fun setCantidadOferta(cantidad: Int) {
        txtOfferSize?.text = if (cantidad > 1 || cantidad == 0) "$cantidad ${resources.getString(R.string.ganamas_plural_offer)}" else "$cantidad ${resources.getString(R.string.ganamas_singular_offer)}"
        txtOfferSize?.visibility = View.VISIBLE
    }

    override fun onDemostradorAdded(item: DemostradorCaminoBrillante, counter: Counter, position: Int, quantity: Int) {
        counter.changeQuantity(1)
        context?.let {
            val message = if (quantity > 1) R.string.demostradores_agregado_con_exito else R.string.demostrador_agregado_con_exito
            showBottomDialog(it, it.getString(message), item.getFotoProducto(), ContextCompat.getColor(it, R.color.product_added_success))
            LocalBroadcastManager.getInstance(it).sendBroadcast(Intent(CaminoBrillanteActivity.BROADCAST_COUNT_ACTION).putExtra(CaminoBrillanteActivity.EXTRA_COUNT_BADGE, quantity))
        }
    }

    override fun onFiltrosLoaded(filtros: List<GroupFilter?>) {
        this.filtros = transformCategoriesFiltersList(filtros)
    }

    override fun hideFilters() {
        lnlFiltro?.visibility = View.GONE
    }

    override fun hideOrders() {
        lltOrderContainer?.visibility = View.GONE
    }

    override fun onOrderLoaded(orders: List<Ordenamiento?>) {
        activity?.let {
            val list = transformOrdersList(orders)
            this.orders = list
            val adapter = ListDialogAdapter(it, list)
            listDialogOrder.setAdapter(adapter)
        }
    }

    override fun showMessage(message: String) {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_mano_error)
                .setContent(message)
                .setNeutralText(getString(R.string.msj_entendido))
                .onNeutral(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .setNeutralBackgroundColor(R.color.magenta)
                .show()
        }
    }

    override fun showMessage(message: Int) {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_mano_error)
                .setContent(message)
                .setNeutralText(getString(R.string.msj_entendido))
                .onNeutral(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .setNeutralBackgroundColor(R.color.magenta)
                .show()
        }
    }

    override fun showError(message: String) {
        tvw_error?.text = message
        view_error?.visibility = View.VISIBLE
    }

    override fun showError(message: Int) {
        tvw_error?.setText(message)
        view_error?.visibility = View.VISIBLE
    }

    override fun hideError() {
        view_error?.visibility = View.GONE
    }

    private fun transformOrdersList(input: List<Ordenamiento?>): ArrayList<ListDialogModel> {
        val output = arrayListOf<ListDialogModel>()

        input.filterNotNull().forEach {
            output.add(ListDialogModel(it.ordenValor,
                it.ordenDescripcion,
                false,
                enable = true))
        }
        return output
    }

    private fun transformCategoriesFiltersList(input: List<GroupFilter?>): ArrayList<CategoryFilterModel> {
        val output = arrayListOf<CategoryFilterModel>()
        input.forEach {
            output.add(CategoryFilterModel(
                key = it?.nombre!!,
                name = it?.nombre!!,
                excluyente = it?.excluyente!!,
                list = transformFilterList(it?.filtros)
            ))
        }
        return output
    }

    private fun transformFilterList(input: List<Filtro?>?): ArrayList<FilterModel> {
        val output = arrayListOf<FilterModel>()
        input?.filterNotNull()?.forEach {
            safeLet(it.codigo, it.descripcion, it.valorMinimo, it.valorMaximo) { code, description, minValue, maxValue ->
                output.add(FilterModel(code, description, false, minValue, maxValue, 0, false, "0", "", ""))
                /**
                 * Leo
                 * Corregir los últimos parametros en la integración con otras ramas ya que no es dinámico
                 */
            }
        }
        return output
    }

    override fun onDemostradorLoaded() {
        listener.hideLoadingOfertas()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    interface DemostradorListener {
        fun hideLoadingOfertas()
        fun getCurrentPosition(): Int
        fun onClickItem(key: String, marcaId: Int, marcaName: String, origenPedidoWeb: String)
        fun setCanBack(canBack: Boolean)
    }

    private fun onItemImpression() {
        val posInit = demostradorLayoutManager.findFirstVisibleItemPosition()
        val posEnd = demostradorLayoutManager.findLastVisibleItemPosition()

        if (posInit == -1 || posEnd == -1) return
        getVisibleDemostradores(posInit, posEnd)
    }

    private fun getVisibleDemostradores(posInit: Int, posEnd: Int) {
        val list = arrayListOf<ProductCUV>()

        this.demostradores.isNotEmpty().apply {
            if (posInit == posEnd) {
                val item = demostradores[posInit]
                item?.let {
                    list.add(demostradorToProduct(it, posInit))
                }
            } else {
                for (pos in posInit until posEnd) {
                    val item = demostradores[pos]
                    item?.let {
                        list.add(demostradorToProduct(it, pos))
                    }
                }
            }
        }

        if (list.size > 0) {
            Tracker.CaminoBrillante.impressionItems(user, GlobalConstant.LIST_DEMOSTRADORES, list)
        }
    }

    private fun demostradorToProduct(it: DemostradorCaminoBrillante, pos: Int): ProductCUV {
        return ProductCUV().apply {
            cuv = it.cuv
            description = it.descripcionCUV
            descripcionCategoria = null
            descripcionMarca = it.descripcionMarca
            precioCatalogo = it.precioCatalogo
            index = pos
        }
    }

    private fun scrollTopRecyclerView() {
        (rvwOfertas.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
    }

    inner class GridScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    println("The RecyclerView is not scrolling")
                    onItemImpression()
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> println("Scrolling now")
                RecyclerView.SCROLL_STATE_SETTLING -> println("Scroll Settling")
            }

        }
    }

    override fun onSelectTab() {
        rvwOfertas.postDelayed({ onItemImpression() }, 200)
    }

    fun tiempoSuficiente(): Boolean {
        val ok = SystemClock.elapsedRealtime() - mLastClickTime < 1000
        mLastClickTime = SystemClock.elapsedRealtime()
        return !ok
    }

    override fun setCanBack(canBack: Boolean) {
        listener.setCanBack(canBack)
    }

}
