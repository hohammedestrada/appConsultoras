package biz.belcorp.consultoras.feature.scanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.scanner.di.DaggerScannerComponent
import biz.belcorp.consultoras.feature.scanner.di.ScannerComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_scanner.viewConnection
import kotlinx.android.synthetic.main.activity_scanner.viewLoading
import kotlinx.android.synthetic.main.activity_scanner.viewLoadingSync
import kotlinx.android.synthetic.main.toolbar_black.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ScannerActivity : BaseActivity(), LoadingView, HasComponent<ScannerComponent>, ScannerFragment.OnScannerInteractionListener {

    private var component: ScannerComponent? = null
    private var fragment: ScannerFragment? = null
    private var isShowScanner = true

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ScannerActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        initializeInjector()
        init(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
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

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragment = ScannerFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction().add(R.id.fltContainer, fragment).commit()
        }
        initToolbar()
    }

    override fun initializeInjector() {
        this.component = DaggerScannerComponent.builder()
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

    private fun initToolbar() {
        toolbar.title = getString(R.string.scanner_title)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    override fun onFinishView() {
        this@ScannerActivity.finish()
    }

    override fun getComponent() = component

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
            BelcorpLogger.d("Scannerctivity onSyncEvent $it", "")
            if (it) {
                viewLoadingSync.visibility = View.VISIBLE
            } else {
                viewLoadingSync.visibility = View.GONE
            }
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

    override fun setShowScanner(isShow: Boolean) {
        this.isShowScanner = isShow
    }

    override fun onBackPressed() {
        if (isShowScanner) {
            super.onBackPressed()
        } else {
            isShowScanner = true
            fragment?.showScanner()
        }
    }
}
