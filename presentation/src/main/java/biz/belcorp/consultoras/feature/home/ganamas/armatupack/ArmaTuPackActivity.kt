package biz.belcorp.consultoras.feature.home.ganamas.armatupack

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
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.di.ArmaTuPackComponent
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.di.DaggerArmaTuPackComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.ArmaTuPackStateType
import biz.belcorp.consultoras.util.anotation.FromOpenActivityType
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ArmaTuPackActivity : BaseActivity(), HasComponent<ArmaTuPackComponent>,
    ArmaTuPackFragment.Listener, ArmaTuPackErrorFragment.Listener {

    private lateinit var cartBadge: TextView
    private lateinit var titleView: TextView

    private var component: ArmaTuPackComponent? = null
    private var fragment: ArmaTuPackFragment? = null
    private var ordersCount: Int? = null
    private var countReceiver: BroadcastReceiver? = null
    private var actionAtpReceiver: BroadcastReceiver? = null
    private var thisAtpRemoved: Boolean = false
    private var fromOpenActivity: String = FromOpenActivityType.ACTIVITY // default

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

            val atpIntent = intent
            atpIntent.putExtra(ATP_TYPE_KEY, OfferTypes.ATP)

            fragment = ArmaTuPackFragment()
            fragment?.arguments = atpIntent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.fltContainer, fragment, ArmaTuPackFragment.TAG)
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

        registerBannerAtpBroadcast()
        registerCountBroadcast()

    }

    override fun setScreenTitle(title: String) {
        titleView.text = title
    }

    override fun initializeInjector() {

        this.component = DaggerArmaTuPackComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()

    }

    override fun initControls() { /* Not necessary */ }

    override fun initEvents() { /* Not necessary */ }

    override fun showLoading() { viewLoading.visibility = View.VISIBLE }

    override fun hideLoading() { viewLoading.visibility = View.GONE }

    override fun getComponent(): ArmaTuPackComponent? = component

    override fun showErrorScreen(type: Int) {

        val currentFragment = supportFragmentManager.findFragmentByTag(ArmaTuPackFragment.TAG)

        val args = Bundle()
        args.putInt(ArmaTuPackErrorFragment.FRAGMENT_ERROR_TYPE_KEY, type)

        val errorFragment = ArmaTuPackErrorFragment.newInstance()
        errorFragment.arguments = args
        supportFragmentManager.beginTransaction()
            .hide(currentFragment)
            .add(R.id.fltContainer, errorFragment, ArmaTuPackErrorFragment.TAG)
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

    override fun resetAtpRemoved() {
        thisAtpRemoved = false
    }

    override fun goToOffers() {
        val homeIntent = HomeActivity.getCallingIntent(this)
        homeIntent.putExtra(HomeActivity.MENU_OPTION, MenuCodeTop.OFFERS)
        startActivity(homeIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AddOrdersActivity.RESULT -> {
                val resultOk = resultCode == Activity.RESULT_OK
                if (thisAtpRemoved) {
                    refreshDataAtp()
                }
            }
        }
    }

    override fun refresh() {

        val atpFragment = supportFragmentManager.findFragmentByTag(ArmaTuPackFragment.TAG) as ArmaTuPackFragment
        val atpErrorFragment = supportFragmentManager.findFragmentByTag(ArmaTuPackErrorFragment.TAG) as ArmaTuPackErrorFragment

        atpFragment.refreshTodo()

        supportFragmentManager.beginTransaction()
            .remove(atpErrorFragment)
            .show(atpFragment)
            .commit()

    }

    override fun onBackPressed() {
        when(fromOpenActivity) {
            FromOpenActivityType.ACTIVITY -> super.onBackPressed()
            FromOpenActivityType.NOTIFICATION -> {
                val fragmentAtp = supportFragmentManager.findFragmentByTag(ArmaTuPackFragment.TAG)
                (fragmentAtp as ArmaTuPackFragment).refreshSchedule()
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

    private fun refreshDataAtp() {
        if (supportFragmentManager.findFragmentById(R.id.fltContainer).tag == ArmaTuPackFragment.TAG) {
            (supportFragmentManager.findFragmentById(R.id.fltContainer) as ArmaTuPackFragment).refreshAtp()
        }
    }

    private fun registerBannerAtpBroadcast() {
        actionAtpReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getIntExtra(BROADCAST_STATE_ATP_EXTRAS, -1)) {
                    ArmaTuPackStateType.INSERT_UPDATE -> thisAtpRemoved = false
                    ArmaTuPackStateType.DELETE -> thisAtpRemoved = true
                    else -> throw IllegalArgumentException();
                }
            }
        }

        registerReceiver(actionAtpReceiver, IntentFilter(BROADCAST_ATP_ACTION))
    }

    private fun registerCountBroadcast() {
        countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateOffersCount(SessionManager.getInstance(this@ArmaTuPackActivity).getOrdersCount())
            }
        }

        registerReceiver(countReceiver, IntentFilter(BROADCAST_COUNT_ACTION))
    }

    private fun updateOffersCount(count: Int?) {
        ordersCount = count
        updateBadge()
    }

    private fun sendCountBroadcast() {
        val countIntent = Intent(BaseFichaActivity.BROADCAST_COUNT_ACTION)
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

    /**
     * Static constants/functions
     */

    companion object {

        const val ATP_TYPE_KEY = "ATP_TYPE_KEY"
        const val ATP_WITH_RESULT_EXTRA = "ATP_WITH_RESULT_EXTRA"

        const val BROADCAST_COUNT_ACTION = "BROADCAST_COUNT_ACTION"
        const val BROADCAST_ATP_ACTION = "BROADCAST_ATP_ACTION"
        const val BROADCAST_STATE_ATP_EXTRAS = "BROADCAST_STATE_ATP_EXTRAS"

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ArmaTuPackActivity::class.java)
        }

    }

}
