package biz.belcorp.consultoras.feature.search.single

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.client.di.DaggerClientComponent
import biz.belcorp.consultoras.feature.home.addorders.client.ClientOrderFilterFragment
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_search_product.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchProductActivity: BaseActivity(),
    HasComponent<ClientComponent>,
    LoadingView,
    SearchProductFragment.Listener,
    ClientOrderFilterFragment.OnFilterCompleteListener {

    companion object {

        const val EXTRA_TITLE = "activityTitle"
        const val EXTRA_MENSAJE_PROL = "mensajeprol"
        const val EXTRA_COUNTRYISO = "countryISO"
        const val EXTRA_MONEYSYMBOL = "moneySymbol"
        const val EXTRA_CLIENTEMODEL = "clienteModel"
        const val EXTRA_ORDER_MODEL = "orderModel"
        const val EXTRA_LIST_TAG_ORDER = "listTagOrder"
        const val EXTRA_VOICE = "voice"
        const val EXTRA_CUV = "cuv"
        const val MESSAGE_ADDING = "message_adding"
        const val EXTRA_CUV_QT = "cuv_quantity"
        const val EXTRA_CUV_BRAND_ID = "cuv_brand_id"
        const val EXTRA_BUNDLE = "extra_bundle"
        const val SEARCH_RESULT = 100
        const val RECORD_REQUEST_CODE = 200
        const val MESSAGE_RESULT = 300
        const val EXTRA_MESSAGE_ADDING = "EXTRA_MESSAGE_ADDING"
        const val EXTRA_TYPE_ALERT = "EXTRA_TYPE_ALERT"
        const val FROM_ONPRODUCT_ADDED = "FROM_ONPRODUCT_ADDED"
        const val MESSAGE_CODE_TOOLTIP = 666
    }

    private var component: ClientComponent? = null
    private var fragment: SearchProductFragment? = null
    private var clientFragment: ClientOrderFilterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_search_product)

        initializeInjector()
        init(savedInstanceState)
    }

    fun goToBack() {
        finish()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == SearchProductActivity.RECORD_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fragment?.showVoiceRecognition()
            }
        }
    }

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
            fragment = SearchProductFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction().add(R.id.fltContainer, fragment).commit()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            val fragment = visibleFragment
            if (fragment != null && fragment is SearchProductFragment) {
                fragment.trackBackPressed()
            }
            onBackPressed()
        }
    }

    override fun initializeInjector() {
        this.component = DaggerClientComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun onFilter() {
        if (clientFragment == null) {
            clientFragment = ClientOrderFilterFragment()
        }

        if (visibleFragment !is ClientOrderFilterFragment)
            addFragment(R.id.fltContainer, clientFragment, false)
    }

    /** */

    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    /** */

    override fun getComponent(): ClientComponent? {
        return component
    }

    /** */

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        if (fragmentManager.backStackEntryCount > 0)
            fragmentManager.popBackStack()
        else{

            if(fragment?.addedRelated == true){
                fragment?.sendBack()
            }else{
                val returnIntent = Intent()
                val bundle = Bundle()
                val clientModel = fragment?.getClientModel()
                if(clientModel != null) {
                    bundle.putParcelable(EXTRA_CLIENTEMODEL, clientModel as Parcelable)
                }
                returnIntent.putExtra(EXTRA_BUNDLE, bundle)
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        }
    }

    override fun initControls() {
        // EMPTY
    }

    override fun initEvents() {
        // EMPTY
    }

    override fun onComplete(clienteModel: ClienteModel) {
        fragment?.onComplete(clienteModel)
        onBackPressed()
    }

}
