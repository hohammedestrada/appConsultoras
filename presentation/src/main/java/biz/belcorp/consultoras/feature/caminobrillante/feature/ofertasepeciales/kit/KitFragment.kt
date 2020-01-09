package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.KitCaminoBrillante
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.caminobrillante.di.CaminoBrillanteComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.OrderOriginCode
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import kotlinx.android.synthetic.main.fragment_kit_demostrador.*
import kotlinx.android.synthetic.main.view_error.*
import java.text.DecimalFormat
import javax.inject.Inject

class KitFragment : BaseFragment(), KitView {

    @Inject
    lateinit var presenter: KitPresenter

    private lateinit var kits: ArrayList<KitCaminoBrillante?>

    private lateinit var kitAdapter: KitAdapter
    private lateinit var kitLayoutManager: GridLayoutManager
    private var mLastClickTime: Long = 0
    private lateinit var listener: KitListener

    private lateinit var user: User

    override fun context(): Context {
        return activity?.applicationContext!!
    }

    companion object {
        var decimalFormat = DecimalFormat()
        var moneySymbol: String = ""

        fun formatWithMoneySymbol(precio: Double): String {
            return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
        }

        fun newInstance(): KitFragment {
            return KitFragment()
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CaminoBrillanteActivity) {
            listener = context
        }
    }

    private fun init() {
        initViews()
        setUpListeners()
        presenter.getUser()
    }

    private fun initViews() {
        this.kits = arrayListOf()

        kitLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        kitAdapter = KitAdapter(null)

        rvwOfertas.layoutManager = kitLayoutManager
        rvwOfertas.adapter = kitAdapter
        rvwOfertas.addOnScrollListener(GridScrollListener())
    }

    private fun setUpListeners() {
        kitAdapter.listener = object : KitAdapter.Listener {
            override fun onClickAdd(item: KitCaminoBrillante, quantity: Int) {
                if (tiempoSuficiente())
                presenter.addKit(item, quantity, DeviceUtil.getId(context))
            }

            override fun onClickItem(kit: KitCaminoBrillante, position: Int) {
                Tracker.CaminoBrillante.onSelectProduct(user, GlobalConstant.LIST_KITS, kitToProduct(kit, position))
                listener.onClickItem(kit.cuv ?: "", kit.marcaID ?: 0, kit.descripcionMarca
                    ?: "", OrderOriginCode.CAMINO_BRILLANTE_OFERTAS_OFERTAS_ESPECIALES_FICHA)
            }
        }

        btn_volver_intentar.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                presenter.loadKits()
            }
        }
    }

    override fun setUser(user: User) {
        this.user = user
        decimalFormat = CountryUtil.getDecimalFormatByISO(user.countryISO, true)
        moneySymbol = user.countryMoneySymbol?.let { it } ?: ""

        presenter.loadKits()
    }

    override fun loadKits() {
        presenter.loadKits()
    }

    override fun showKits(kits: List<KitCaminoBrillante>) {
        this.kits.clear()
        this.kits.addAll(kits)

        rvwOfertas.visibility = View.VISIBLE
        kitAdapter.setData(kits)

        if (listener.getCurrentPosition() == 0) {
            rvwOfertas.postDelayed({ onItemImpression() }, 200)
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

    override fun hideError() {
        view_error?.visibility = View.GONE
    }

    override fun onKitAdded(item: KitCaminoBrillante, quantity: Int) {
        context?.let {
            showBottomDialog(it, it.getString(R.string.kit_agregado_con_exito), item.getFotoProducto(), ContextCompat.getColor(it, R.color.product_added_success))
            LocalBroadcastManager.getInstance(it).sendBroadcast(Intent(CaminoBrillanteActivity.BROADCAST_COUNT_ACTION).putExtra(CaminoBrillanteActivity.EXTRA_COUNT_BADGE, quantity))
        }
    }

    override fun onKitLoaded() {
        listener.hideLoadingOfertas()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    interface KitListener {
        fun hideLoadingOfertas()
        fun getCurrentPosition(): Int
        fun onClickItem(key: String, marcaId: Int, marcaName: String, origenPedidoWeb: String)
        fun setCanBack(canBack: Boolean)
    }

    private fun onItemImpression() {
        val posInit = kitLayoutManager.findFirstVisibleItemPosition()
        val posEnd = kitLayoutManager.findLastVisibleItemPosition()

        if (posInit != -1 || posEnd != -1) {
            getVisibleKits(posInit, posEnd)
        }
    }

    private fun getVisibleKits(posInit: Int, posEnd: Int) {
        val list = arrayListOf<ProductCUV>()

        this.kits.isNotEmpty().apply {
            if (posInit == posEnd) {
                val item = kits[posInit]
                item?.let {
                    list.add(kitToProduct(it, posInit))
                }
            } else {
                for (pos in posInit until posEnd) {
                    val item = kits[pos]
                    item?.let {
                        list.add(kitToProduct(it, pos))
                    }
                }
            }
        }

        if (list.size > 0) {
            Tracker.CaminoBrillante.impressionItems(user, GlobalConstant.LIST_KITS, list)
        }
    }

    fun kitToProduct(it: KitCaminoBrillante, pos: Int): ProductCUV {
        return ProductCUV().apply {
            cuv = it.cuv
            description = it.descripcionCUV
            descripcionCategoria = null
            descripcionMarca = it.descripcionMarca
            precioCatalogo = it.precioCatalogo
            index = pos
        }
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
