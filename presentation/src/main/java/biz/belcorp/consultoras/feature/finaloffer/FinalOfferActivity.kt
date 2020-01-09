package biz.belcorp.consultoras.feature.finaloffer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.Window
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.finaloffer.di.DaggerFinalOfferComponent
import biz.belcorp.consultoras.feature.finaloffer.di.FinalOfferComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_final_offer.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FinalOfferActivity : BaseActivity(), HasComponent<FinalOfferComponent>,
    FinalOfferFragment.Listener, LoadingView {

    private var component: FinalOfferComponent? = null

    private var fragment: FinalOfferFragment? = null

    /** */

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_final_offer)

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

    override fun onBackPressed() {
        // EMPTY
    }

    /** */

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

    override fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragment = FinalOfferFragment()
            fragment?.arguments = intent.extras

            supportFragmentManager.beginTransaction().add(R.id.fltContainer, fragment).commit()
        }

        tvw_toolbar_title.setText(R.string.orders_title)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }
    }

    override fun initializeInjector() {
        this.component = DaggerFinalOfferComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()    }

    override fun initControls() {
        // EMPTY
    }

    override fun initEvents() {
        // EMPTY
    }

    override fun getComponent(): FinalOfferComponent? {
        return component
    }

    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    /** */

    @SuppressLint("SetTextI18n")
    override fun setCampaign(campaign: String) {
        var campaignString = campaign

        if (!TextUtils.isEmpty(campaign) && campaign.length == 6) {
            campaignString = "C${campaign.substring(4)}"
        }

        tvw_toolbar_title.text = "${getString(R.string.orders_title)} $campaignString"
    }

    /** */

    companion object {
        const val FINAL_OFFER_REQUEST_CODE = 1009
        const val EXTRA_ORDER_RESERVE_RESULT = "order_reserve_result"
        const val EXTRA_FINAL_OFFER_HEADER = "final_offer_header"
        const val EXTRA_FINAL_OFFER_LIST = "final_offer_list"
        const val EXTRA_ORDER_IMPORT_TOTAL = "order_import_total"
        const val EXTRA_MONTO_REGALO = "monto_regalo"
        const val EXTRA_REGALO_ELEGIDO = "regalo_elegido"
        const val EXTRA_REGALO_TIENEREGALO= "tiene_regalo"
    }

}
