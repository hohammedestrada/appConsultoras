package biz.belcorp.consultoras.feature.product

import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlDataMapper
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.consultoras.feature.home.addorders.client.ClientOrderFilterFragment
import biz.belcorp.consultoras.feature.product.di.DaggerProductComponent
import biz.belcorp.consultoras.feature.product.di.ProductComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ProductActivity : BaseActivity(),
    LoadingView,
    HasComponent<ProductComponent>,
    ProductFragment.OnClientFilterClick,
    ClientOrderFilterFragment.OnFilterCompleteListener {

    private var component: ProductComponent? = null
    private var fragment: ProductFragment? = null
    private var clientFragment: ClientOrderFilterFragment? = null

    companion object {
        const val PRODUCT_RESULT = 201
        const val EXTRA_BUNDLE = "extra_bundle"
        const val EXTRA_MENSAJE_PROL = "mensajeprol"
        const val EXTRA_ID_PEDIDO = "orden"
        const val EXTRA_PRODUCTO = "producto"
        const val EXTRA_MONEY_SYMBOL = "symbol"
        const val EXTRA_COUNTRY_ISO = "iso"
        const val EXTRA_MESSAGE_ADDING = "EXTRA_MESSAGE_ADDING"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_product)

        initializeInjector()
        init(savedInstanceState)
    }


    // Overrides BaseActivity
    override fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragment = ProductFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction().add(R.id.fltContainer, fragment).commit()
        }

        tvw_toolbar_title.setText(R.string.product_title)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }

    }

    override fun initializeInjector() {
        this.component = DaggerProductComponent.builder()
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
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        when {
            fragmentManager.backStackEntryCount > 0 -> fragmentManager.popBackStack()
            else -> {
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
            BelcorpLogger.d("ProductActiviy onSyncEvent $it", "")
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

    override fun getComponent(): ProductComponent? {
        return component
    }

    // ProductFragment.OnClientFilterClick
    override fun onFilter() {
        if (clientFragment == null) {
            clientFragment = ClientOrderFilterFragment()
        }

        if (visibleFragment !is ClientOrderFilterFragment)
            addFragment(R.id.fltContainer, clientFragment, false)
    }

    // ClientOrderFilterFragment.OnFilterCompleteListener
    override fun onComplete(clienteModel: ClienteModel) {
        fragment?.onComplete(clienteModel)
        onBackPressed()
    }

}
