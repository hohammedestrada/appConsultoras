package biz.belcorp.consultoras.feature.home.fest

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.premio.FichaPremioActivity
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.fest.di.DaggerFestComponent
import biz.belcorp.consultoras.feature.home.fest.di.FestComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.FestPremioStateType
import biz.belcorp.consultoras.util.anotation.FromOpenActivityType
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.mobile.components.dialogs.list.ListDialog
import biz.belcorp.mobile.components.dialogs.list.adapters.ListDialogAdapter
import biz.belcorp.mobile.components.dialogs.list.model.ListDialogModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FestActivity : BaseActivity(), HasComponent<FestComponent>, FestFragment.Listener, FestErrorFragment.Listener {

    private lateinit var cartBadge: TextView
    private lateinit var titleView: TextView

    private var component: FestComponent? = null
    private var fragment: FestFragment? = null
    private var ordersCount: Int? = null
    private var countReceiver: BroadcastReceiver? = null
    private var actionFestReceiver: BroadcastReceiver? = null
    private var fromOpenActivity: String = FromOpenActivityType.ACTIVITY // default
    private val listDialogOrder = ListDialog()
    private val tagOrderListFragment = "list_order_fragment"
    private var festPremioPendingUpdate: Boolean = false

    /**
     * Lifecycle override functions
     */

    override fun onAttachedToWindow() {

        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_base)

        initializeInjector()
        init(savedInstanceState)

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

        if (NetworkUtil.isThereInternetConnection(this)) setStatusTopNetwork()
        else {

            viewConnection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))

        }

    }

    /**
     * Custom override functions
     */

    override fun init(savedInstanceState: Bundle?) {

        ordersCount = SessionManager.getInstance(this).getOrdersCount()

        if (savedInstanceState == null) {

            intent.getStringExtra(GlobalConstant.FROM_OPEN_ACTIVITY)?.let {
                fromOpenActivity = it
            }

            val festIntent = intent

            fragment = FestFragment()
            fragment?.arguments = festIntent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.fltContainer, fragment, FestFragment.TAG)
                .commit()

        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        titleView = toolbar.findViewById(R.id.tvw_toolbar_title)

        toolbar.inflateMenu(R.menu.arma_pack_menu)

        val actionView = MenuItemCompat.getActionView(toolbar.menu.findItem(R.id.item_cart))
        cartBadge = actionView.findViewById(R.id.tvi_cart)

        (actionView.findViewById(R.id.imgCart) as ImageView).setOnClickListener { navigator.navigateToAddOrdersFromAtpWithResult(this) }

        updateBadge()

        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.item_search) {
                navigator.navigateToSearchWithResult(this)
            }
            true
        }

        registerCountBroadcast()
        registerFestLandingBroadcast()
    }

    override fun setScreenTitle(title: String) {
        titleView.text = title
    }

    override fun initializeInjector() {

        this.component = DaggerFestComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()

    }

    override fun initControls() { /* Not necessary */ }

    override fun initEvents() { /* Not necessary */ }

    override fun showLoading() { viewLoading.visibility = View.VISIBLE }

    override fun hideLoading() { viewLoading.visibility = View.GONE }

    override fun getComponent(): FestComponent? = component

    override fun showErrorScreen(type: Int) {

        val currentFragment = supportFragmentManager.findFragmentByTag(FestFragment.TAG)

        val args = Bundle()
        args.putInt(FestErrorFragment.FRAGMENT_ERROR_TYPE_KEY, type)

        val errorFragment = FestErrorFragment.newInstance()
        errorFragment.arguments = args
        supportFragmentManager.beginTransaction()
            .hide(currentFragment)
            .add(R.id.fltContainer, errorFragment, FestErrorFragment.TAG)
            .commit()

    }

    override fun updateOffersCount(count: Int) {
        ordersCount = if (ordersCount == null)
            count
        else
            ordersCount!! + count
        SessionManager.getInstance(this).saveOffersCount(ordersCount!!)
        updateBadge()

        sendCountBroadcast()
    }

    override fun goToOffers() {
        val homeIntent = HomeActivity.getCallingIntent(this)
        homeIntent.putExtra(HomeActivity.MENU_OPTION, MenuCodeTop.OFFERS)
        startActivity(homeIntent)
        finish()
    }

    override fun goToFichaProduct(extras: Bundle) {
        navigator.navigateToFichaProductWithResult(this, extras)
    }

    override fun refreshFromError() {

        val festFragment = supportFragmentManager.findFragmentByTag(FestFragment.TAG) as FestFragment
        val festErrorFragment = supportFragmentManager.findFragmentByTag(FestErrorFragment.TAG) as FestErrorFragment

        festFragment.refreshTodo()

        supportFragmentManager.beginTransaction()
            .remove(festErrorFragment)
            .show(festFragment)
            .commit()

    }

    override fun onBackPressed() {
        when(fromOpenActivity) {
            FromOpenActivityType.ACTIVITY -> {
                val returnIntent = Intent()
                if (fragment?.itemsAdded == true) setResult(Activity.RESULT_OK, returnIntent)
                else setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
            FromOpenActivityType.NOTIFICATION -> {
                val fragmentFest = supportFragmentManager.findFragmentByTag(FestFragment.TAG)
                (fragmentFest as FestFragment).refreshSchedule()
            }
        }
    }

    override fun showOrder() {
        listDialogOrder.show(supportFragmentManager, tagOrderListFragment)
    }

    override fun setOrder(itemListener: ListDialog.ListItemDialogListener, arrayDialog: ArrayList<ListDialogModel>) {
        listDialogOrder.setAdapter(ListDialogAdapter(this@FestActivity, arrayDialog))
        listDialogOrder.setListener(itemListener)
    }

    override fun setSelectedItemOrder(index: Int) {
        listDialogOrder.setSelected(index)
    }

    override fun resetFestPendingUpdate() {
        festPremioPendingUpdate = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AddOrdersActivity.RESULT -> {
                checkedFestPendingUpdate()
            }
            BaseFichaActivity.RESULT -> {
                checkedFestPendingUpdate()
            }
            FichaPremioActivity.REQUEST_CODE_FICHA_PREMIO->{
                checkedFestPendingUpdate()
                fragment?.onActivityResult(requestCode, resultCode, data)
            }

        }
    }

    /**
     * Events functions
     */

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

            if (it) viewLoadingSync.visibility = View.VISIBLE
            else viewLoadingSync.visibility = View.GONE

        }

    }

    /**
     * Helper functions
     */

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

    private fun registerFestLandingBroadcast() {
        actionFestReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getIntExtra(BROADCAST_STATE_FEST_EXTRAS, -1)) {
                    FestPremioStateType.INSERT_UPDATE -> festPremioPendingUpdate = true
                    FestPremioStateType.DELETE -> festPremioPendingUpdate = true
                    else -> throw IllegalArgumentException()
                }
            }
        }

        registerReceiver(actionFestReceiver, IntentFilter(BROADCAST_FEST_ACTION))
    }

    private fun registerCountBroadcast() {
        countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateOffersCount(SessionManager.getInstance(this@FestActivity).getOrdersCount())
            }
        }

        registerReceiver(countReceiver, IntentFilter(BROADCAST_COUNT_ACTION))
    }

    private fun updateOffersCount(count: Int?) {
        ordersCount = count
        updateBadge()
    }

    private fun sendCountBroadcast() {
        val countIntent = Intent(BROADCAST_COUNT_ACTION)
        sendBroadcast(countIntent)
    }

    private fun updateBadge() { cartBadge.text = formatCount(ordersCount) ?: "0" }

    private fun formatCount(count: Int?) : String? {
        count?.let {
            return if (it <= 99)
                count.toString()
            else
                "99+"
        }
        return null
    }

    fun goToFichaPremio(extras: Bundle){
        navigator.navigateToFichaPremio(this, extras)
    }

    private fun checkedFestPendingUpdate() {
        if (festPremioPendingUpdate) {
            refreshDataPremio()
        }
    }

    private fun refreshDataPremio() {
        if (supportFragmentManager.findFragmentById(R.id.fltContainer).tag == FestFragment.TAG) {
            (supportFragmentManager.findFragmentById(R.id.fltContainer) as FestFragment).refreshFestPremio()
        }
    }

    /**
     * Static constants/functions
     */

    companion object {

        const val BROADCAST_COUNT_ACTION = "BROADCAST_COUNT_ACTION"
        const val BROADCAST_FEST_ACTION = "BROADCAST_FEST_ACTION"
        const val BROADCAST_STATE_FEST_EXTRAS = "BROADCAST_STATE_FEST_EXTRAS"
        const val RESULT = 9001

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, FestActivity::class.java)
        }

    }

}
