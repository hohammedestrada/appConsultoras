package biz.belcorp.consultoras.feature.orderdetail

import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.orders.di.DaggerOrderDetailComponent
import biz.belcorp.consultoras.feature.orders.di.OrderDetailComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.toolbar_black.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrderDetailActivity : BaseActivity(),
    HasComponent<OrderDetailComponent>,
    LoadingView, OrderDetailFragment.Listener {

    private var component: OrderDetailComponent? = null

    val fragment = OrderDetailFragment()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        supportFragmentManager.beginTransaction().replace(R.id.fltContainer, fragment).commit()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    /** EventBus Functions */

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

    /** Functions */

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


    override fun getComponent(): OrderDetailComponent? {
        return component
    }


    override fun init(savedInstanceState: Bundle?) {
        initializeToolbar()
        initializeInjector()
    }

    override fun initializeInjector() {
        this.component = DaggerOrderDetailComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    private fun initializeToolbar() {
        tvw_toolbar_title.text = getString(R.string.order_detail_title)
        toolbar.setNavigationOnClickListener {
            val fragment = visibleFragment
            if (fragment != null && fragment is OrderDetailFragment) {
                fragment.trackBackPressed()
            } else onBackPressed()
        }
    }

    override fun initControls() {
        // EMPTY
    }

    override fun initEvents() {
        // EMPTY
    }


    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }


    override fun onBackPressed() {
        finish()
    }

    override fun onBackFromFragment() {
        onBackPressed()
    }

}
