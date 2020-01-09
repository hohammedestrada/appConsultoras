package biz.belcorp.consultoras.feature.dreammeter

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.feature.dreammeter.di.DaggerDreamMeterComponent
import biz.belcorp.consultoras.feature.dreammeter.di.DreamMeterComponent
import biz.belcorp.consultoras.feature.dreammeter.feature.detail.DetailFragment
import biz.belcorp.consultoras.feature.dreammeter.feature.save.SaveFragment
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.FestivityAnimationUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DreamMeterActivity : BaseActivity(), HasComponent<DreamMeterComponent>, LoadingView, SaveFragment.Listener, DetailFragment.Listener {

    private var component: DreamMeterComponent? = null

    private var saveFragment: SaveFragment? = null
    private var detailFragment: DetailFragment? = null

    private var canBack = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_base)
        initializeInjector()
        init(savedInstanceState)
    }

    override fun getComponent(): DreamMeterComponent? {
        return component
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
            viewConnection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    private fun initializeToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = getString(R.string.dream_meter)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        initializeToolbar()
        if (savedInstanceState == null) {
            goToSave(null, true)
        }
    }

    override fun initializeInjector() {
        component = DaggerDreamMeterComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {}

    override fun initEvents() {}

    override fun goToDetail(dreamMeter: DreamMeter, isReplace: Boolean) {
        hideKeyboard()

        if (isReplace) {
            detailFragment = DetailFragment.newInstance(dreamMeter)
            detailFragment?.let {
                replaceFragment(R.id.fltContainer, it, true)
            }
        } else {
            super.onBackPressed()
            detailFragment?.reloadDreamMeter(dreamMeter)
        }
    }

    override fun goToSave(dreamMeter: DreamMeter?, isReplace: Boolean) {
        hideKeyboard()
        saveFragment = SaveFragment.newInstance(dreamMeter)
        saveFragment?.let {
            if (isReplace) {
                replaceFragment(R.id.fltContainer, it, true)
            } else {
                addFragment(R.id.fltContainer, it, false)
            }
        }

    }

    override fun showConfeti() {
        FestivityAnimationUtil.getCommonConfetti(
            ContextCompat.getColor(this, R.color.dorado),
            ContextCompat.getColor(this, R.color.primary),
            resources, rlt_base)
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view: View? = currentFocus
        val ret: Boolean = super.dispatchTouchEvent(event)
        view?.let{viewValidate ->
            if (view is EditText) {
                val w: View = viewValidate
                val scrcoords = IntArray(2)
                w.getLocationOnScreen(scrcoords)
                val x: Float = event.rawX + w.left - scrcoords[0]
                val y: Float = event.rawY + w.top - scrcoords[1]
                if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right || y < w.top || y > w.bottom)) {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    window?.currentFocus?.let{
                        imm.hideSoftInputFromWindow(it.windowToken, 0)
                    }

                }
            }
        }

        return ret
    }

    override fun onBackPressed() {
        if (canBack) {
            super.onBackPressed()
        }
    }

    override fun setCanBack(isCanBack: Boolean) {
        canBack = isCanBack
    }

    fun hideKeyboard() {
        val inputMethodManager = context().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
