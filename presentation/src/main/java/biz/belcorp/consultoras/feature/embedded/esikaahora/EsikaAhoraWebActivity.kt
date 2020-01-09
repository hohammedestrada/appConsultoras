package biz.belcorp.consultoras.feature.embedded.esikaahora

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.embedded.esikaahora.di.EsikaAhoraWebComponent
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.feature.embedded.esikaahora.di.DaggerEsikaAhoraWebComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.activity_esika_ahora_web.*
import kotlinx.android.synthetic.main.toolbar_black.*
import kotlinx.android.synthetic.main.view_connection.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class EsikaAhoraWebActivity: BaseActivity(), HasComponent<EsikaAhoraWebComponent>,
    EsikaAhoraWebFragment.Listener, LoadingView {

    companion object {
        val EXTRA_VALUE = "EXTRA_VALUE"

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, EsikaAhoraWebActivity::class.java)
        }
    }

    private var component: EsikaAhoraWebComponent? = null
    private var fragment: EsikaAhoraWebFragment? = null

    override fun initializeInjector() {
        component = DaggerEsikaAhoraWebComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun getComponent(): EsikaAhoraWebComponent? {
        return component
    }

    fun getCallingIntent(context: Context): Intent {
        return Intent(context, EsikaAhoraWebActivity::class.java)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_esika_ahora_web)

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
            view_connection?.visibility = View.VISIBLE
            tvw_connection_message?.setText(R.string.connection_offline)
            ivw_connection?.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onBackPressed() {
        onBackFromFragment()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkEvent(event: NetworkEvent) {
        when (event.event) {
            NetworkEventType.CONNECTION_AVAILABLE -> {
                SyncUtil.triggerRefresh()
                setStatusTopNetwork()
            }
            NetworkEventType.CONNECTION_NOT_AVAILABLE -> {
                view_connection?.visibility = View.VISIBLE
                tvw_connection_message?.setText(R.string.connection_offline)
                ivw_connection?.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                view_connection?.visibility = View.VISIBLE
                tvw_connection_message?.text = getString(R.string.connection_datami_available)
                ivw_connection?.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> view_connection?.visibility = View.GONE
            else -> view_connection?.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSyncEvent(event: SyncEvent) {
        if (event.isSync) {
            view_loading_sync?.visibility = View.VISIBLE
        } else {
            view_loading_sync?.visibility = View.GONE
        }
    }

    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.DATAMI_AVAILABLE -> {
                view_connection?.visibility = View.VISIBLE
                tvw_connection_message?.text = getString(R.string.connection_datami_available)
                ivw_connection?.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> view_connection.visibility = View.GONE
            else -> view_connection?.visibility = View.GONE
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        initializeInjector()
        initializeToolbar()
        setMenuTitle(getString(R.string.esika_ahora_title))
        if (savedInstanceState == null) {
            fragment = EsikaAhoraWebFragment.newInstance(getExtraNivel())
            addFragment(R.id.fltContainer, fragment)
        }
    }

    private fun getExtraNivel() : String {
        return intent.extras?.getString(EXTRA_VALUE) ?: StringUtil.Empty
    }


    private fun initializeToolbar() {
        toolbar.setNavigationOnClickListener {
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
        view_loading?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        view_loading?.visibility = View.GONE
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

    override fun setMenuTitle(title: String?) {
        title?.let {
            tvw_toolbar_title?.text = title
        } ?: kotlin.run {
            tvw_toolbar_title?.text = getString(R.string.esika_ahora_title)
        }
    }
}
