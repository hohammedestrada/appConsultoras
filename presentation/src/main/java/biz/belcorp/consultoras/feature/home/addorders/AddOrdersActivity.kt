package biz.belcorp.consultoras.feature.home.addorders

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlDataMapper
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.client.di.DaggerClientComponent
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.addorders.client.ClientOrderFilterFragment
import biz.belcorp.consultoras.feature.home.addorders.updatemail.UpdateEmailOrderView
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.FestivityAnimationUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.MensajeProlUtil
import biz.belcorp.consultoras.util.anotation.HolydayType
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_add_orders.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AddOrdersActivity : BaseActivity(),
    HasComponent<ClientComponent>,
    LoadingView,
    ClientOrderFilterFragment.OnFilterCompleteListener,
    AddOrdersFragment.Listener {

    private var component: ClientComponent? = null

    var fragment: AddOrdersFragment? = null
    private var clientFragment: ClientOrderFilterFragment? = null
    private val handler = Handler()
    private var searchItem: MenuItem? = null

    private var ordersCount: Int? = null

    fun showSearchOption() {
        searchItem?.isVisible = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_orders)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkEvent(event: NetworkEvent) {
        when (event.event) {
            NetworkEventType.CONNECTION_AVAILABLE -> {
                SyncUtil.triggerRefresh()
                setStatusTopNetwork()
            }
            NetworkEventType.CONNECTION_NOT_AVAILABLE -> {
                multiOrderView.visibility = View.GONE
                connectionView.visibility = View.VISIBLE
                viewConnection.visibility = View.VISIBLE
                tvw_connection_message.setText(R.string.connection_offline)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                multiOrderView.visibility = View.GONE
                connectionView.visibility = View.VISIBLE
                viewConnection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> viewConnection.visibility = View.GONE
            NetworkEventType.MULTI_ORDER_AVAILABLE -> {
                multiOrderView.visibility = View.VISIBLE
                connectionView.visibility = View.GONE
                viewConnection.visibility = View.VISIBLE
            }
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

    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.WIFI -> viewConnection.visibility = View.GONE
            NetworkEventType.MULTI_ORDER_AVAILABLE -> {
                multiOrderView.visibility = View.VISIBLE
                connectionView.visibility = View.GONE
                viewConnection.visibility = View.VISIBLE
            }
            else -> viewConnection.visibility = View.GONE
        }
    }

    private fun removeOrderReserved(saved: Boolean) {

        fltHoliday.animate()
            .alpha(0.0f)
            .setDuration(800)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    // EMPTY
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    // EMPTY
                }

                override fun onAnimationEnd(animation: Animator?) {

                    handler.removeCallbacksAndMessages(null)
                    fltHoliday.removeAllViews()
                    fltContainer.visibility = View.VISIBLE
                    fltHoliday.visibility = View.GONE

                    if (!saved) {
                        fragment?.callTrackReservar()
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                    // EMPTY
                }
            })
    }

    override fun onOrderReserved(saved: Boolean, updateEmail: Boolean, pendingEmail: String?) {

        val reserved = Reserved(this)

        if (updateEmail) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            reserved.updateEmail(pendingEmail)
            reserved.listener = object : Reserved.ReservedListener {
                override fun onCloseAction() {
                    removeOrderReserved(saved)
                }

            }
        }

        fltHoliday.visibility = View.VISIBLE
        fltHoliday.alpha = 0f

        fltHoliday.animate()
            .alpha(1.0f)
            .setDuration(800)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    fltContainer.visibility = View.GONE
                    fltHoliday.visibility = View.VISIBLE

                    reserved.initLayoutParams()
                    fltHoliday.addView(reserved)
                    reserved.setUpdateOrderViewListener(object : UpdateEmailOrderView.UpdateEmailOrderListener {
                        override fun onClickUpdateEmail(email: String) {
                            showLoading()
                            fragment?.emailUpdate(email, object : AddOrdersFragment.AdditionalOrderListener {
                                override fun onClose() {
                                    handler.post {
                                        removeOrderReserved(saved)
                                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                                    }
                                }
                            })

                        }
                    })

                    if (saved) {
                        reserved.postInit(HolydayType.BELCORP_ORDER_GUARDAR)
                    } else {
                        reserved.postInit(HolydayType.BELCORP_ORDER_RESERVAR)
                    }
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    // EMPTY
                }

                override fun onAnimationEnd(animation: Animator?) {

                    reserved.start()
                    FestivityAnimationUtil.getCommonConfetti(
                        ContextCompat.getColor(this@AddOrdersActivity, R.color.dorado),
                        ContextCompat.getColor(this@AddOrdersActivity, R.color.primary),
                        resources, fltHoliday)

                    if (!updateEmail) {
                        handler.postDelayed({
                            removeOrderReserved(saved)
                        }, 6000)
                    }

                }

                override fun onAnimationCancel(animation: Animator?) {
                    // EMPTY
                }
            })
    }

    override fun init(savedInstanceState: Bundle?) {
        ordersCount = SessionManager.getInstance(this).getOrdersCount()
        if (savedInstanceState == null) {
            fragment = AddOrdersFragment()
            fragment?.arguments = intent.extras //No se usa para nada en este momento

            supportFragmentManager.beginTransaction().add(R.id.fltContainer, fragment).commit()
        }

        tvw_toolbar_title.setText(R.string.orders_title)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }

        toolbar.inflateMenu(R.menu.search_menu)
        toolbar.menu.findItem(R.id.item_orders).isVisible = false
        searchItem = toolbar.menu.findItem(R.id.item_search)
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
                else -> super.onOptionsItemSelected(item)
            }
            return@setOnMenuItemClickListener true
        }
        tvwMultiOrdermessage.text = FirebaseRemoteConfig.getInstance().getString(BuildConfig.REMOTE_CONFIG_MESSAGE_MULTI_ORDER)
    }

    override fun initializeInjector() {
        this.component = DaggerClientComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun navigateToOfertaFinal() {
        navigator.navigateToOfertaFinalLandig(this, null)
    }
    override fun initControls() {
        // EMPTY
    }

    override fun initEvents() {
        // EMPTY
    }

    override fun updateOffersCount(count: Int) {
        ordersCount = if (ordersCount == null)
            count
        else
            ordersCount!! + count

        SessionManager.getInstance(this).saveOffersCount(ordersCount!!)
    }

    override fun onFilter() {
        if (clientFragment == null) {
            clientFragment = ClientOrderFilterFragment()
        }

        if (visibleFragment !is ClientOrderFilterFragment)
            addFragment(R.id.fltContainer, clientFragment, false)
    }

    override fun onComplete(clienteModel: ClienteModel) {
        fragment?.onComplete(clienteModel)
        onBackPressed()
    }

    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    override fun getComponent(): ClientComponent? {
        return component
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        val visible = visibleFragment
        when (visible) {
            is AddOrdersFragment -> visible.trackBackPressed()
            is ClientOrderFilterFragment -> visible.trackBackPressed()
        }

        when {
            fragmentManager.backStackEntryCount > 0 -> fragmentManager.popBackStack()
            else -> {
                if (fragment?.changes == true) {
                    val returnIntent = Intent()
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                } else {
                    super.onBackPressed()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setCampaign(campaign: String) {
        var campaignString = campaign

        if (!TextUtils.isEmpty(campaign) && campaign.length == 6) {
            campaignString = "C${campaign.substring(4)}"
        }

        tvw_toolbar_title.text = "${getString(R.string.orders_title)} $campaignString"
    }

    @SuppressLint("SetTextI18n")
    override fun setActivityTitle(title: String) {
        tvw_toolbar_title.text = title
    }

    override fun goToOffers() {
        val homeIntent = HomeActivity.getCallingIntent(this)
        homeIntent.putExtra(HomeActivity.EXTRA_KEY_LANDING, true)
        startActivity(homeIntent)
    }

    fun mensajeProl(mensajes: Collection<MensajeProl?>?) {

        mensajes?.let {
            if (it.size > 0) {
                MensajeProlUtil.showMensajeProl(this@AddOrdersActivity, MensajeProlDataMapper().transformToDomainModel(it))

            }
        }
    }

    companion object {
        const val FROM_ADDODERS_ACTIVITY = 1
        const val RESULT = 1050
        const val RESULT_GO_TO_OFFERS = "RESULT_GO_TO_OFFERS"
        const val BRANDING_CONFIG = "BrandConfig"
    }

}
