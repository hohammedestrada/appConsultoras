package biz.belcorp.consultoras.feature.changeemail

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.changeemail.di.ChangeEmailComponent
import biz.belcorp.consultoras.feature.changeemail.di.DaggerChangeEmailComponent
import biz.belcorp.consultoras.feature.home.profile.MyProfileActivity
import biz.belcorp.consultoras.feature.verifyemail.VerifyEmailFragment
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_change_email.*
import kotlinx.android.synthetic.main.toolbar_black.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ChangeEmailActivity : BaseActivity(), HasComponent<ChangeEmailComponent>, LoadingView
    , ChangeEmailFragment.ChangeEmailFragmentListener, VerifyEmailFragment.VerifyEmailFragmentListener {

    private var component: ChangeEmailComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)
        init(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (NetworkUtil.isThereInternetConnection(this))
            setStatusTopNetwork()
        else {
            view_connection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /** */

    override fun init(savedInstanceState: Bundle?) {
        initializeToolbar()
        initializeInjector()
        toolbar.title = ""
        lltContent.removeAllViews()


        if(intent.extras?.getString(SAME_EMAIL).isNullOrEmpty()){
            goToChangeEmail()
        }
        else{
            intent.extras?.getString(SAME_EMAIL)?.let {
                goToVerifyEmail(it)
            }
        }

    }

    override fun initializeInjector() {
        component = DaggerChangeEmailComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    private fun initializeToolbar() {
        tvw_toolbar_title.text = getString(R.string.change_email_title)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkEvent(event: NetworkEvent) {
        when (event.event) {
            NetworkEventType.CONNECTION_AVAILABLE -> {
                SyncUtil.triggerRefresh()
                setStatusTopNetwork()
            }
            NetworkEventType.CONNECTION_NOT_AVAILABLE -> {
                view_connection.visibility = View.VISIBLE
                tvw_connection_message.setText(R.string.connection_offline)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                view_connection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> view_connection.visibility = View.GONE
            else -> view_connection.visibility = View.GONE
        }
    }

    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.DATAMI_AVAILABLE -> {
                view_connection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> view_connection.visibility = View.GONE
            else -> view_connection.visibility = View.GONE
        }
    }

    override fun initControls() {
        // EMPTY
    }

    override fun initEvents() {
        // EMPTY
    }

    override fun showLoading() {
        if (null != view_loading) view_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        if (null != view_loading) view_loading.visibility = View.GONE
    }

    override fun getComponent(): ChangeEmailComponent? {
        return component
    }

    override fun onHome() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun goToVerifyEmail(newEmail: String) {
        tvw_toolbar_title.text = getString(R.string.verify_email_title)
        val verifyEmailFragment = VerifyEmailFragment()
        var bundle = Bundle()
        bundle.putString(NEW_EMAIL, newEmail)
        verifyEmailFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.lltContent, verifyEmailFragment).commit()
        EventBus.getDefault().post(MyProfileActivity.MyProfileActivityEvent())
    }

    override fun goToChangeEmail() {
        tvw_toolbar_title.text = getString(R.string.change_email_title)
        val changeEmailFragment = ChangeEmailFragment()
        changeEmailFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().replace(R.id.lltContent, changeEmailFragment).commit()
    }

    companion object {
        const val OLD_EMAIL = "old email"
        const val NEW_EMAIL = "new email"
        const val SAME_EMAIL = "same email"
    }


}
