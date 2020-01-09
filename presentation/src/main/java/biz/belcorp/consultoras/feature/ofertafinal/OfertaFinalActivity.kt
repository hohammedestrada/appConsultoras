package biz.belcorp.consultoras.feature.ofertafinal

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasLandingFragment
import biz.belcorp.consultoras.feature.ofertafinal.di.DaggerOfertaFinalComponent
import biz.belcorp.consultoras.feature.ofertafinal.di.OfertaFinalComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.PremioEstadoType
import kotlinx.android.synthetic.main.activity_base.*

class OfertaFinalActivity : BaseActivity(), HasComponent<OfertaFinalComponent>, OfertaFinalLandingFragment.Listener {

    private var component: OfertaFinalComponent? = null
    private lateinit var titleView: TextView
    private var fragment: OfertaFinalLandingFragment? = null
    private lateinit var searchItem: MenuItem
    private var flagAddedLanding = false
    private var premioEstado = PremioEstadoType.BLOCKED
    private var progressBroadcast: BroadcastReceiver? = null
    private var getProgressOnResume: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        initializeInjector()
        init(savedInstanceState)
    }

    override fun getComponent(): OfertaFinalComponent? = component

    override fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragment = OfertaFinalLandingFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fltContainer, fragment, OfertaFinalLandingFragment.TAG)
                .commit()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        titleView = toolbar.findViewById(R.id.tvw_toolbar_title)

        toolbar.inflateMenu(R.menu.oferta_final_menu)
        searchItem = toolbar.menu.findItem(R.id.item_search)

        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.item_search)
                navigator.navigateToSearchWithResult(this)
            true
        }

        registerBroadcast()

    }

    override fun onResume() {

        super.onResume()

        if (getProgressOnResume) {

            fragment?.updateProgress()
            getProgressOnResume = false

        }


    }

    override fun onDestroy() {

        super.onDestroy()
        unregisterReceiver(progressBroadcast)

    }

    private fun registerBroadcast() {

        progressBroadcast = object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                getProgressOnResume = true
            }

        }

        registerReceiver(progressBroadcast, IntentFilter(BROADCAST_PROGRESS_ACTION))

    }

    override fun initControls() {
        /* Not necessary */
    }

    override fun initEvents() {
        /* Not necessary */
    }

    override fun initializeInjector() {
        this.component = DaggerOfertaFinalComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }


    override fun showLoading() {
        viewLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        viewLoading.visibility = View.GONE
    }

    override fun showSearchItem() {
        searchItem.isVisible = true
    }


    override fun goToFicha(extras: Bundle) {
        this.navigator.navigateToFicha(this, extras)
    }


    override fun goToFichaPremio(extras: Bundle) {
        this.navigator.navigateToFichaOfertaFinal(this, extras)
    }

    override fun goToFest(extras: Bundle) {
        this.navigator.navigateToFest(this, extras)
    }

    override fun setScreenTitle(title: String) {
        titleView.text = title
    }

    override fun updateAdded() {
        flagAddedLanding = true

    }

    override fun updatePremioEstado(premioEstado: Int) {
        this.premioEstado = premioEstado
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(EXTRA_FLAG_ADDED_LANDING, flagAddedLanding)
        intent.putExtra(EXTRA_PREMIO_ESTADO, premioEstado)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun showErrorScreen(type: Int) {
        val currentFragment = supportFragmentManager.findFragmentByTag(OfertaFinalLandingFragment.TAG)
        val args = Bundle()
        args.putInt(OfertaFinalErrorFragment.FRAGMENT_ERROR_TYPE_KEY, type)
        val errorFragment = OfertaFinalErrorFragment.newInstance()
        errorFragment.arguments = args
        supportFragmentManager.beginTransaction()
            .hide(currentFragment)
            .add(R.id.fltContainer, errorFragment, OfertaFinalErrorFragment.TAG)
            .commit()
    }

    companion object {

        val REQUEST_CODE_LANDING_OFERTAFINAL = 103
        val EXTRA_FLAG_ADDED_LANDING = "EXTRA_FLAG_ADDED_LANDING"
        val EXTRA_PREMIO_ESTADO = "EXTRA_PREMIO_ESTADO"

        const val BROADCAST_PROGRESS_ACTION = "BROADCAST_PROGRESS_ACTION"

    }
}
