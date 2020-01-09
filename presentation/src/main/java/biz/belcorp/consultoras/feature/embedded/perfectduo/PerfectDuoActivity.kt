package biz.belcorp.consultoras.feature.embedded.perfectduo

import android.app.Activity
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.embedded.perfectduo.di.DaggerPerfectDuoComponent
import biz.belcorp.consultoras.feature.embedded.perfectduo.di.PerfectDuoComponent
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersFragment
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PerfectDuoActivity: BaseActivity(),PerfectDuoFragment.ListenerBanner ,HasComponent<PerfectDuoComponent> {

    private var component: PerfectDuoComponent? = null
    private var fragment: PerfectDuoFragment? = null
    private var searchitem: MenuItem? = null
    private var shopCar: MenuItem? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }


    fun showSearchOption(){
        searchitem?.isVisible = true
        shopCar?.isVisible = true

    }

    fun goToBack() {
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
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
        if (NetworkUtil.isThereInternetConnection(this))
            setStatusTopNetwork()
        else {
            viewConnection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    /** */

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

    override fun getComponent(): PerfectDuoComponent? = component

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



    override fun init(savedInstanceState: Bundle?) {
        initializeInjector()
        if(savedInstanceState == null){
            fragment = PerfectDuoFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction().add(R.id.fltContainer, fragment).commit()
        }
        initializeToolbar()
    }

    private fun initializeToolbar() {
        toolbar?.setNavigationOnClickListener {
            if (fragment != null)
                fragment?.trackBackPressed()
            else
                onBackFromFragment()
        }
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener { _ ->

            onBackPressed()
        }

        tvw_toolbar_title?.text= intent.extras?.getString(GlobalConstant.TITLE)

        toolbar.inflateMenu(R.menu.search_menu)
        searchitem = toolbar.menu.findItem(R.id.item_search)
        shopCar = toolbar.menu.findItem(R.id.item_orders)
        shopCar!!.isVisible = false
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.item_search -> {
                    if (visibleFragment is AddOrdersFragment)
                        (visibleFragment as AddOrdersFragment)
                            .trackEvent(GlobalConstant.EVENT_CAT_SEARCH,
                                GlobalConstant.EVENT_ACTION_SEARCH_SELECTION,
                                GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
                                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT)
                    navigator.navigateToSearchWithResult(this)
                }
                R.id.item_orders ->{
                    navigator.navigateToAddOrders(this)
                }

                else -> super.onOptionsItemSelected(item)
            }
            return@setOnMenuItemClickListener true
        }
    }



    override fun initializeInjector() {
        component =  DaggerPerfectDuoComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()

    }

    override fun initControls() {
    }

    override fun initEvents() {
    }

    override fun onBackFromFragment() {
        val goBack = fragment!!.onBackWebView()
        if (!goBack) {
            val upIntent = NavUtils.getParentActivityIntent(this)
            if (null != upIntent  && NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities()
            }
            setResult(Activity.RESULT_OK, upIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        if (fragment != null)
            fragment!!.trackBackPressed()
        else
            onBackFromFragment()
    }

    companion object {
        //const val URL = "url"
        const val RESULT_REQUEST_KEY = "opcion"
        const val RESULT_REQUEST_CODE = 1001
    }

}
