package biz.belcorp.consultoras.feature.home.addorders.choosegift

import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.home.addorders.choosegift.di.DaggerGiftComponent
import biz.belcorp.consultoras.feature.home.addorders.choosegift.di.GiftComponent
import biz.belcorp.consultoras.feature.payment.online.di.DaggerPaymentOnlineComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class GiftActivity : BaseActivity(), HasComponent<GiftComponent> , LoadingView{

    private var component: GiftComponent? = null
    companion object {
        const val GIFT_RESULT = 666

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_notoolbar)

        initializeInjector()
        init(savedInstanceState)
    }

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
            viewConnection.visibility = android.view.View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    override fun initializeInjector() {
        component = DaggerGiftComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun getComponent(): GiftComponent? {
        return component
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
                viewConnection.visibility = android.view.View.VISIBLE
                tvw_connection_message.setText(R.string.connection_offline)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                viewConnection.visibility = android.view.View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> viewConnection.visibility = android.view.View.GONE
            else -> viewConnection.visibility = android.view.View.GONE
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

    override fun showLoading() {
        viewLoading.visibility = android.view.View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = android.view.View.GONE
    }

    override fun init(savedInstanceState: Bundle?) {
       hideKeyboard()
        if(savedInstanceState == null){
            val giftFragment = GiftFragment()
            giftFragment.arguments = intent.extras
            replaceFragment(R.id.fltContainer, giftFragment, true)
        }
    }

    override fun initControls() {
        //vacio
    }

    override fun initEvents() {
    //vacio
    }

    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.DATAMI_AVAILABLE -> {
                viewConnection.visibility = android.view.View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> viewConnection.visibility = android.view.View.GONE
            else -> viewConnection.visibility = android.view.View.GONE
        }
    }

    fun hideKeyboard() {
        val inputMethodManager = context()?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this?.currentFocus
        if (view == null) view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



}
