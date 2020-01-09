package biz.belcorp.consultoras.feature.sms

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sms.SMSEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.sms.di.DaggerSMSComponent
import biz.belcorp.consultoras.feature.sms.di.SMSComponent
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_sms.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Leonardo Castañeda on 24/08/2017.
 */

class SMSActivity : BaseActivity(), HasComponent<SMSComponent>, LoadingView, PhoneFragment.PhoneFragmentListener, SMSFragment.SMSFragmentListener {

    private var component: SMSComponent? = null
    private var smsFragment: SMSFragment? = null
    private var phoneFragment: PhoneFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)
        initializeInjector()
        init(savedInstanceState)
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onBackPressed() {

        val sms = smsFragment?.isVisible ?: false
        if(sms){
            showAlert()
        }else{
            when {
                supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
                else -> {
                    val returnIntent = Intent()
                    setResult(Activity.RESULT_CANCELED, returnIntent)
                    finish()
                }
            }
        }
    }

    // Overrides BaseActivity
    override fun init(savedInstanceState: Bundle?) {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { _ -> onBackPressed() }

        if (savedInstanceState == null) {
                phoneFragment = PhoneFragment()
                phoneFragment?.arguments = intent.extras
                supportFragmentManager.beginTransaction().add(R.id.fltContainer, phoneFragment, "PhoneFragment").commit()
        }

        changeToolBar(resources.getString(R.string.phone_title))

    }

    override fun initializeInjector() {
        component = DaggerSMSComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {}
    override fun initEvents() {}


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
            BelcorpLogger.d("SMSActivity onSyncEvent $it", "")
            if (it) {
                viewLoadingSync.visibility = View.VISIBLE
            } else {
                viewLoadingSync.visibility = View.GONE
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSyncEvent(event: SMSEvent) {
        smsFragment?.autocompleteCode(event.body)
    }

    // Override HasComponent<SMSComponent>
    override fun getComponent(): SMSComponent? { return component }


    // Overrides LoadingView
    override fun showLoading() { viewLoading.visibility = View.VISIBLE }
    override fun hideLoading() { viewLoading.visibility = View.GONE }


    // Private Functions
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
    private fun changeToolBar(title: String){ tvw_toolbar_title.text = title }

    private fun showAlert(){
        AlertDialog.Builder(this@SMSActivity)
        .setMessage(resources.getString(R.string.sms_dialog_body))
        .setPositiveButton("Aceptar"){_, _ ->
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }
        .setNegativeButton("Cancelar"){_,_ -> }
        .create().show()
    }

    // Override PhoneListener
    override fun onConfirm(newNumber: String) {
        smsFragment = SMSFragment()
        intent.putExtra(EXTRA_NEW_PHONE_NUMBER, newNumber)
        smsFragment?.arguments = intent.extras
        supportFragmentManager.beginTransaction().add(R.id.fltContainer, smsFragment, "SMSFragment").commit()
        changeToolBar(resources.getString(R.string.sms_title))
    }

    // Override SMSListener
    override fun onPhoneConfirm(type: Int) {
        supportFragmentManager.popBackStack()
        val returnIntent = Intent()
        returnIntent.putExtra(RESULT_SMS, type)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onPhoneNewChange() {
        phoneFragment = PhoneFragment()
        intent.putExtra(EXTRA_SMS_DIRECTO,false)
        phoneFragment?.arguments = intent.extras
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().add(R.id.fltContainer, phoneFragment, "PhoneFragment").commit()
        changeToolBar(resources.getString(R.string.phone_title))
    }

    override fun onCancel() {
        onBackPressed()
    }

    // Companion
    companion object {
        const val EXTRA_PHONE_NUMBER = "number"
        const val EXTRA_NEW_PHONE_NUMBER = "new_number"
        const val EXTRA_COUNTRY_ISO = "country_iso"
        const val EXTRA_CAMPAING = "campaing"
        const val EXTRA_SMS_DIRECTO = "directo"
        const val RESULT_SMS = "sms_result"
        const val FROM_WELCOME = "FROM_WELCOME"
        const val EXTRA_ORIGEN_DESCRIPCION = "EXTRA_ORIGEN_DESCRIPCION"
        const val EXTRA_ORIGEN_ID = "EXTRA_ORIGEN_ID"
        const val EXTRA_ID_ESTADO_ACTIVIDAD= "EXTRA_ID_ESTADO_ACTIVIDAD"
    }

}
