package biz.belcorp.consultoras.feature.terms

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
import biz.belcorp.consultoras.feature.terms.di.DaggerTermsComponent
import biz.belcorp.consultoras.feature.terms.di.TermsComponent
import biz.belcorp.consultoras.feature.vinculacion.VinculacionFragment
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.CreditApplicationType
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.android.synthetic.main.toolbar_black.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class TermsActivity : BaseActivity(), HasComponent<TermsComponent>, LoadingView,
    TermsFragment.Listener, VinculacionFragment.VinculacionListener {


    private var component: TermsComponent? = null

    private var creditApplication: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
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

    override fun onBackPressed() {
        // EMPTY
    }

    /** */

    override fun init(savedInstanceState: Bundle?) {
        initBundle()
        initializeToolbar()
        initializeInjector()

        if (savedInstanceState == null) {
            when (creditApplication) {
                CreditApplicationType.ACCEPT, CreditApplicationType.NOT_APPLY ->  {
                    val fragment = TermsFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fltContainer, fragment).commit()
                }
                CreditApplicationType.NOT_ACCEPT -> {
                    val fragment = VinculacionFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fltContainer, fragment).commit()
                }
            }
        }

    }

    override fun initializeInjector() {
        component = DaggerTermsComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    private fun initializeToolbar() {
        tvw_toolbar_title.text = getString(R.string.terms_title)
    }

    private fun initBundle() {
        val bundle = intent.extras

        if (bundle != null) {
            creditApplication = bundle.getInt(TermsActivity.BUNDLE_INDICADOR_CONTRATO_CREDITO
                , CreditApplicationType.NOT_APPLY)
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

    override fun initControls() {

    }

    override fun initEvents() {
        // EMPTY
    }

    override fun showLoading() {
        if (null != viewLoading) viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        if (null != viewLoading) viewLoading.visibility = View.GONE
    }

    override fun getComponent(): TermsComponent? {
        return component
    }

    override fun onHome() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onTerms() {
        val fragment = TermsFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fltContainer, fragment).commit()
    }

    companion object {
        const val BUNDLE_INDICADOR_CONTRATO_CREDITO = "bundle_indicador_contrato_credito"
        const val ACEPTA_TERMINOS_Y_CONDICIONES= "ACEPTA_TERMINOS_Y_CONDICIONES"
    }

}
