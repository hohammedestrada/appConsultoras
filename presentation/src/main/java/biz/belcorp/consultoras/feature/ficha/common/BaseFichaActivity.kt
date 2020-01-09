package biz.belcorp.consultoras.feature.ficha.common

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat.getActionView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingDialogView
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.feature.ficha.di.DaggerFichaComponent
import biz.belcorp.consultoras.feature.ficha.di.FichaComponent
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.HomeActivity.Companion.KIT_NUEVA_BR_KEY
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.addorders.client.ClientOrderFilterFragment
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFichaActivity : BaseActivity(),
    LoadingView, LoadingDialogView,
    BaseFichaFragment.BaseListener,
    HasComponent<FichaComponent>, ClientOrderFilterFragment.OnFilterCompleteListener {

    private var component: FichaComponent? = null
    protected var fragment: BaseFichaFragment? = null

    protected var ordersCount: Int? = null

    private lateinit var cartBadge: TextView
    private lateinit var toolbar: Toolbar

    private val listFragments = arrayListOf<BaseFichaFragment>()

    private var countReceiver: BroadcastReceiver? = null

    private var clientFragment: ClientOrderFilterFragment? = null

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(countReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_base)

        initializeInjector()
        init(savedInstanceState)
    }

    // Overrides BaseActivity
    override fun init(savedInstanceState: Bundle?) {
        ordersCount = SessionManager.getInstance(this).getOrdersCount()

        if (savedInstanceState == null) {
            fragment = generateFichaFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.fltContainer, fragment)
                .commit()

            fragment?.let {
                listFragments.add(it)
            }
        }

        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        toolbar.inflateMenu(R.menu.ficha_menu)
        toolbar.menu.findItem(R.id.item_search).isVisible = false

        val actionView = getActionView(toolbar.menu.findItem(R.id.item_cart))
        cartBadge = actionView.findViewById(R.id.tvi_cart)

        (actionView.findViewById(R.id.imgCart) as ImageView).setOnClickListener {
            fragment?.goToOffers()
        }

        updateBadge()

        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.item_share) {
                fragment?.getShareURL()
            }

            if (item.itemId == R.id.item_search) {
                navigator.navigateToSearchWithResult(this)
            }

            if (item.itemId == R.id.item_cart) {
                navigator.navigateToAddOrdersWithResult(this)
            }
            true
        }

        registerCountBroadcast()

    }

    private fun registerCountBroadcast() {

        countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                extraActionOnReceive()
                simpleOffersCountUpdate(SessionManager.getInstance(this@BaseFichaActivity).getOrdersCount()
                    ?: 0)
            }
        }

        registerReceiver(countReceiver, IntentFilter(BROADCAST_COUNT_ACTION))

    }

    fun hideToolbarIcons(vararg menuItems: FichaMenuItems) {
        menuItems.forEach { item -> toolbar.menu.findItem(item.id).isVisible = false }
    }

    fun showToolbarIcons(vararg menuItems: FichaMenuItems) {
        menuItems.forEach { item -> toolbar.menu.findItem(item.id).isVisible = true }
    }

    private fun updateBadge() {

        cartBadge.text = formatCount(ordersCount)

    }

    private fun formatCount(count: Int?): String {

        count?.let {

            return if (it <= 99)
                count.toString()
            else
                "99+"

        } ?: return "0"

    }

    private fun simpleOffersCountUpdate(count: Int?) {
        ordersCount = count
        updateBadge()
    }

    override fun updateOrders(count: Int, isKitNueva: Boolean?) {

        ordersCount = if (ordersCount == null)
            count
        else
            ordersCount?.plus(count)

        SessionManager.getInstance(this).saveOffersCount(ordersCount ?: 0)
        updateBadge()

        sendCountBroadcast(isKitNueva)

    }

    override fun showShare(show: Boolean) {
        toolbar.menu.findItem(R.id.item_share).isVisible = show
    }

    private fun sendCountBroadcast(isFromKitNuevas: Boolean?) {
        val countIntent = Intent(BROADCAST_COUNT_ACTION)
        isFromKitNuevas?.let { countIntent.putExtra(KIT_NUEVA_BR_KEY, it) }
        sendBroadcast(countIntent)
    }

    override fun initializeInjector() {
        this.component = DaggerFichaComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {
        // Empty
    }

    override fun initEvents() {
        // Empty
    }

    // Overrides Activity
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
            viewConnection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
        //ordersCount = SessionManager.getInstance(this).getOrdersCount()
        //updateBadge()
    }

    override fun onBackPressed() {

        val fm = supportFragmentManager
        val fragmentsFichaCount = listFragments.size

        // para Ficha Detalle
        val detalleFichaFragment = fm.findFragmentByTag(TAG_FICHA_DETALLE)
        if (detalleFichaFragment != null) {
            if (fragmentsFichaCount > 0) {
                val currentFichaFragment = listFragments[fragmentsFichaCount - 1]
                if (currentFichaFragment.isHidden) {
                    fragment = currentFichaFragment
                    fragment?.refreshShare()
                    fm.beginTransaction().hide(detalleFichaFragment).show(currentFichaFragment).commit()
                    return
                }
            }
        }

        val clientFragment = fm.findFragmentByTag(TAG_CLIENT_LIST)
        if (clientFragment != null && clientFragment.isVisible) {
            toolbar.title = EMPTY
            fm.popBackStack()

        } else {
            // para Ficha desde otra Ficha
            if (fragmentsFichaCount > 1) {
                val currentFichaFragment = listFragments[fragmentsFichaCount - 2]
                fragment = currentFichaFragment
                fm.beginTransaction().replace(R.id.fltContainer, currentFichaFragment).commit()
                listFragments.removeAt(fragmentsFichaCount - 1)
            } else {
                val returnIntent = Intent()
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        }

    }

    // Overrides LoadingView
    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    override fun showLoadingDialog(){
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoadingDialog() {
        viewLoading.visibility = View.GONE
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
                viewConnection.visibility = View.VISIBLE
                tvw_connection_message.setText(R.string.connection_offline)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                viewConnection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> viewConnection.visibility = View.GONE
            else -> viewConnection.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSyncEvent(event: SyncEvent) {
        event.isSync?.let {
            if (it) {
                viewLoadingSync.visibility = View.VISIBLE
            } else {
                viewLoadingSync.visibility = View.GONE
            }
        }
    }

    // Functions
    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.DATAMI_AVAILABLE -> {
                viewConnection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> viewConnection.visibility = View.GONE
            else -> viewConnection.visibility = View.GONE
        }
    }

    override fun changeFicha(extras: Bundle) {

        val backStateName = extras.getString(EXTRA_KEY_ITEM, EXTRA_BACK_STATE_NAME)

        if (listFragments.size > 0) {
            for (i in 0 until listFragments.size) {
                if (backStateName == listFragments[i].cuv) {
                    listFragments.removeAt(i)
                    break
                }
            }
        }

        fragment = generateFichaFragment()
        fragment?.arguments = extras

        supportFragmentManager.beginTransaction()
            .replace(R.id.fltContainer, fragment)
            .commit()

        fragment?.let {
            listFragments.add(it)
        }

        updateBadge()

    }

    override fun showFichaComponente(extras: Bundle) {

        val fm = supportFragmentManager
        val fragmentsFichaCount = listFragments.size

        val detailFichaFragment = fm.findFragmentByTag(TAG_FICHA_DETALLE)
        val currentFichaFragment = listFragments[fragmentsFichaCount - 1]
        if (detailFichaFragment != null) {
            if (detailFichaFragment.isHidden) {
                fragment = detailFichaFragment as BaseFichaFragment
                fragment?.arguments = extras
                fragment?.refreshInit()
                fm.beginTransaction().hide(currentFichaFragment).show(fragment).commit()
            }
        } else {
            fragment = generateFichaFragment()
            fragment?.arguments = extras
            fm.beginTransaction().hide(currentFichaFragment)
                .add(R.id.fltContainer, fragment, TAG_FICHA_DETALLE).commit()
        }

        updateBadge()

    }


    override fun showClients() {
        if (clientFragment == null) {
            clientFragment = ClientOrderFilterFragment()
        }

        toolbar.title = resources.getString(R.string.ficha_assign_client)

        supportFragmentManager.beginTransaction().add(R.id.fltContainer, clientFragment, TAG_CLIENT_LIST).addToBackStack(TAG_CLIENT_LIST).commit()

    }

    override fun onBackPressedFragment() {
        onBackPressed()
    }

    override fun getComponent(): FichaComponent? {
        return component
    }

    override fun goToOrders(menu: Menu) {
        navigator.navigateToOrders(this, menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            AddOrdersActivity.RESULT -> fragment?.init()
        }
    }

    override fun onComplete(clienteModel: ClienteModel) {
        fragment?.onComplete(clienteModel)
        onBackPressed()
    }

    enum class FichaMenuItems(val id: Int) {

        SHARE_ITEM(R.id.item_share),
        CART_ITEM(R.id.item_cart),
        SEARCH_ITEM(R.id.item_search)

    }

    abstract fun generateFichaFragment(): BaseFichaFragment

    open fun extraActionOnReceive() {}

    companion object {

        const val RESULT = 200

        const val EXTRA_BACK_STATE_NAME = "Ficha"

        const val EXTRA_TYPE_FICHA = "EXTRA_TYPE_FICHA"
        const val EXTRA_PROMOTION = "EXTRA_PROMOCION"
        const val EXTRA_KEY_ITEM = "KEY_ITEM"
        const val EXTRA_TYPE_OFFER = "TYPE_OFFER"
        const val EXTRA_MARCA_ID = "EXTRA_MARCA_ID"
        const val EXTRA_MARCA_NAME = "EXTRA_MARCA_NAME"

        const val EXTRA_PRODUCT_COMPONENT = "EXTRA_PRODUCT_COMPONENT"

        const val EXTRA_ACCESS_FROM = "ACCESS_FROM"                                //  Indicador desde donde se accedio a la ficha
        const val EXTRAS_ORIGENES_PEDIDO_SEARCH = "EXTRAS_ORIGENES_PEDIDO_SEARCH"  //  Todos los origenes pedido web del producto (Buscador)

        const val EXTRA_ORIGEN_PEDIDO_WEB_FROM = "ORIGEN_PEDIDO_WEB_FROM"          //  Origen pedido web desde gana+
        const val EXTRA_ORIGEN_PEDIDO_WEB = "EXTRA_ORIGEN_PEDIDO_WEB"              //  Origen pedido web desde pase pedido
        const val EXTRA_FICHA_OFFER_TYPE = "EXTRA_FICHA_OFFER_TYPE"

        const val EXTRA_ENABLE_SHARE = "ENABLE_SHARE"
        const val EXTRA_ENABLE_SEARCH = "ENABLE_SEARCH"
        const val EXTRA_MATERIAL_GANANCIA = "MATERIAL_GANANCIA"
        const val BROADCAST_COUNT_ACTION = "BROADCAST_COUNT_ACTION"

        const val EXTRA_TYPE_PERSONALIZATION = "EXTRA_TYPE_PERSONALIZATION"

        const val TAG_FICHA_DETALLE = "TAG_FICHA_DETALLE"

        const val TAG_CLIENT_LIST = "TAG_CLIENT_LIST"

        const val EXTRAS_PRODUCTO_ITEM = "EXTRAS_PRODUCTO_ITEM"  //  CONTIENE EL PRODUCTO SELECCIONADO DE SearchListFragment o SearchResultFragment

        const val ACCESS_FROM_GANAMAS = 1
        const val ACCESS_FROM_BUSCADOR_DESPLEGABLE = 2
        const val ACCESS_FROM_BUSCADOR_LANDING = 3
        const val ACCESS_FROM_PEDIDO_GANASMAS = 4
        const val ACCESS_FROM_CAROUSEL_SUGERIDOS = 6 // Ficha resumida
        const val ACCESS_FROM_PASE_PEDIDO = 5 // Ficha resumida
        const val ACCESS_FROM_CAMINO_BRILLANTE = 7
        const val ACCESS_FROM_SUBCAMPANIA = 8
        const val ACCESS_FROM_LANDING_FEST = 9
        const val ACCESS_FROM_LANDING_OFERTA_FINAL = 10
        const val ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_PREMIO = 11
        const val ACCESS_FROM_BUSCADOR_DESPLAGABLE_PROMOCION_CONDICION = 12

        const val ACCESS_FROM_PEDIDO_GANASMAS_ADDED_FICHA = 8888

        const val EXTRA_MESSAGE_ADDING = "EXTRA_MESSAGE_ADDING"

        var PRODUCT_ADDED: Boolean = false

        const val EXTRA_BUNDLE = "extra_bundle"
        const val EXTRA_MENSAJE_PROL = "mensajeprol"
        const val EXTRA_PRODUCTO = "producto"
        const val EXTRA_PRODUCTO_ORDER = "producto_order"

        const val DEFAULT_VALUE = "Default"

        const val FROM_GANAMAS = "FROM_GANAMAS"

        const val EMPTY = ""

        const val EXTRA_REMAINING_AMOUNT = "REMAINING_AMOUNT"
        const val EXTRA_CODE_ALERT = "extra_bundle_code_message"

    }

}
