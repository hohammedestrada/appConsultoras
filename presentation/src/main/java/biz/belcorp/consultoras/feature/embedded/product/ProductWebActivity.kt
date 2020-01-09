package biz.belcorp.consultoras.feature.embedded.product

import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.feature.embedded.product.di.DaggerProductWebComponent
import biz.belcorp.consultoras.feature.embedded.product.di.ProductWebComponent
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.toolbar_black.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ProductWebActivity : BaseActivity(), HasComponent<ProductWebComponent>,
    ProductWebFragment.Listener, LoadingView {

    private var component: ProductWebComponent? = null
    private var fragment: ProductWebFragment? = null
    private var from: Int = 0 // Indicador para la lupa (default: 0)
    private var searchItem: MenuItem? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    /** EventBus */

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

    override fun onGetMenu(menu: Menu) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.item_search -> {
                    navigator.navigateToSearch(this)
                }
                R.id.item_orders -> {
                    navigator.navigateToOrdersWithResult(this, menu)
                }
                else -> super.onOptionsItemSelected(item)
            }
            return@setOnMenuItemClickListener true
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

    fun showSearchOption(){
        if(from == AddOrdersActivity.FROM_ADDODERS_ACTIVITY){ // Solo muestra el buscador si viene desde el carrusel de promociones
            searchItem?.isVisible = true
        }
    }

    override fun getComponent(): ProductWebComponent? {
        return component
    }

    override fun init(savedInstanceState: Bundle?) {
        from = intent.extras?.getInt(ProductWebActivity.EXTRA_FROM) ?: 0
        if (savedInstanceState == null) {
            fragment = ProductWebFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction().replace(R.id.fltContainer, fragment).commit()
        }
        initializeToolbar()
        initializeInjector()

        toolbar?.inflateMenu(R.menu.search_menu)
        searchItem = toolbar?.menu?.findItem(R.id.item_search)

    }

    override fun initializeInjector() {
        this.component = DaggerProductWebComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    private fun initializeToolbar() {
        tvw_toolbar_title.text = getString(R.string.product_detail_title)
        toolbar?.setNavigationOnClickListener {
            if (fragment != null)
                fragment?.trackBackPressed()
            else
                onBackFromFragment()
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
        if (fragment != null)
            fragment?.trackBackPressed()
        else
            onBackFromFragment()
    }

    override fun onBackFromFragment() {
        val goBack = fragment?.onBackWebView() ?: false
        if (!goBack) {
            val upIntent = NavUtils.getParentActivityIntent(this)
            if (null != upIntent && NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities()
            }
            setResult(Activity.RESULT_OK, upIntent)
            finish()
        }
    }

    companion object {
        const val EXTRA_CUV = "extra_product_cuv"
        const val EXTRA_PALANCA_ID = "extra_product_palanca_id"
        const val EXTRA_ORIGIN = "extra_product_origen"
        const val EXTRA_FROM = "extra_activity_from"
        const val RESULT = 1020
    }
}
