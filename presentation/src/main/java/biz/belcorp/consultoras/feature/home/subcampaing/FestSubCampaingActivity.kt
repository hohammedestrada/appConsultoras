package biz.belcorp.consultoras.feature.home.subcampaing

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
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.fest.FestErrorFragment
import biz.belcorp.consultoras.feature.home.subcampaing.di.DaggerSubCampaingComponent
import biz.belcorp.consultoras.feature.home.subcampaing.di.SubCampaingComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.GlobalConstant
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
import org.jetbrains.anko.displayMetrics
import kotlin.math.roundToInt

class FestSubCampaingActivity : BaseActivity(), HasComponent<SubCampaingComponent>,
    FestSubCampaingFragment.Listener, FestErrorFragment.Listener {


    override fun initControls() {
        // Empty
    }

    override fun initEvents() {
        // Empty
    }


    override fun setSelectedItemOrder(index: Int) {
        listDialogOrder.setSelected(index)
    }

    private val listDialogOrder = ListDialog()


    override fun showOrder() {
        listDialogOrder.show(supportFragmentManager, "listorder_fragment")
    }

    override fun dismissOrder() {
        listDialogOrder.dismiss()
    }

    override fun setOrder(itemListener: ListDialog.ListItemDialogListener, arrayDialog: ArrayList<ListDialogModel>) {
        listDialogOrder.setAdapter(ListDialogAdapter(this@FestSubCampaingActivity, arrayDialog))
        listDialogOrder.setListener(itemListener)
    }


    override fun isToolbarText(s: String) {
        titleView.isAllCaps = false
        titleView.text = s
        val dpTop = (TITLE_TEXT_SIZE * displayMetrics.density).roundToInt().toFloat()
        titleView.textSize = dpTop
    }


     var titlebartext:String?=null
    private lateinit var cartBadge: TextView
    private lateinit var titleView: TextView

    private var component: SubCampaingComponent? = null
    private var fragment: FestSubCampaingFragment? = null
    private var ordersCount: Int? = null
    private var countReceiver: BroadcastReceiver? = null
    private var fromOpenActivity: String = FromOpenActivityType.ACTIVITY // default


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

    private fun registerCountBroadcast() {
        countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateOffersCount(SessionManager.getInstance(this@FestSubCampaingActivity).getOrdersCount())
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

    /**
     * Static constants/functions
     */

    override fun init(savedInstanceState: Bundle?) {
        ordersCount = SessionManager.getInstance(this).getOrdersCount()

        if (savedInstanceState == null) {

            intent.getStringExtra(GlobalConstant.FROM_OPEN_ACTIVITY)?.let {
                fromOpenActivity = it
            }

            titlebartext = intent.getStringExtra(TITLE_KEY)

            val atpIntent = intent

            fragment = FestSubCampaingFragment()
            fragment?.arguments = atpIntent.extras
            supportFragmentManager.beginTransaction()
                .add(R.id.fltContainer, fragment, FestSubCampaingFragment.TAG)
                .commit()

        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        titleView = toolbar.findViewById(R.id.tvw_toolbar_title)

        titlebartext?.let {
            isToolbarText(it)
        }

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

    }

    override fun updateSubcampaignTitle(title: String) {
        isToolbarText(title)
    }

    override fun initializeInjector() {
        this.component = DaggerSubCampaingComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun getComponent(): SubCampaingComponent? = component


    override fun setScreenTitle(title: String) {
        titleView.text = title
    }

    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    override fun showErrorScreen(type: Int) {
        val currentFragment = supportFragmentManager.findFragmentByTag(FestSubCampaingFragment.TAG)

        val args = Bundle()
        args.putInt(SubCampaingErrorFragment.FRAGMENT_ERROR_TYPE_KEY, type)

        val errorFragment = SubCampaingErrorFragment.newInstance()
        errorFragment.arguments = args
        supportFragmentManager.beginTransaction()
            .hide(currentFragment)
            .add(R.id.fltContainer, errorFragment, SubCampaingErrorFragment.TAG)
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

    override fun refreshFromError() {
        val festFragment = supportFragmentManager.findFragmentByTag(FestSubCampaingFragment.TAG) as FestSubCampaingFragment
        val festErrorFragment = supportFragmentManager.findFragmentByTag(FestErrorFragment.TAG) as FestErrorFragment

        festFragment.refreshTodo()

        supportFragmentManager.beginTransaction()
            .remove(festErrorFragment)
            .show(festFragment)
            .commit()
    }

    override fun onBackPressed() {
        when(fromOpenActivity) {
            FromOpenActivityType.ACTIVITY -> super.onBackPressed()
            FromOpenActivityType.NOTIFICATION -> {
                val fragmentFest = supportFragmentManager.findFragmentByTag(FestSubCampaingFragment.TAG)
                (fragmentFest as FestSubCampaingFragment).refreshSchedule()
            }
        }
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

            if (it) viewLoadingSync.visibility = View.VISIBLE
            else viewLoadingSync.visibility = View.GONE

        }

    }

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

    companion object {

        const val BROADCAST_COUNT_ACTION = "BROADCAST_COUNT_ACTION"
        const val TITLE_KEY = "TITLE_KEY"
        const val TITLE_TEXT_SIZE = 6

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, FestSubCampaingActivity::class.java)
        }

    }

}
