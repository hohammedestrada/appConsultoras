package biz.belcorp.consultoras.feature.caminobrillante

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.feature.caminobrillante.di.CaminoBrillanteComponent
import biz.belcorp.consultoras.feature.caminobrillante.di.DaggerCaminoBrillanteComponent
import biz.belcorp.consultoras.feature.caminobrillante.feature.home.CaminoBrillanteFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado.LogroUnificadoFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.OfertasEspecialesFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.demostrador.DemostradorFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit.KitFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.tutorial.TutorialFragment
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.library.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CaminoBrillanteActivity : BaseActivity(),
    HasComponent<CaminoBrillanteComponent>,
    LoadingView,
    CaminoBrillanteFragment.Listener,
    OfertasEspecialesFragment.OfertasEspecialesListener,
    DemostradorFragment.DemostradorListener,
    KitFragment.KitListener,
    TutorialFragment.TutorialCaminoBrillanteListener,
    LogroUnificadoFragment.Listener {

    private var component: CaminoBrillanteComponent? = null

    //fragments
    private var fragment: CaminoBrillanteFragment? = null
    private var ofertasEspecialesFragment: OfertasEspecialesFragment? = null
    private var logroUnificadoFragment: LogroUnificadoFragment? = null
    private var tutorialFragment: TutorialFragment? = null

    //flags
    private var isNivelActualConsultora = true
    private var isLoading: Boolean = false
    private var canBack = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_base)
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
            viewConnection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        initializeToolbar()
        if (savedInstanceState == null) {
            goToFragmentCaminoBrillante()
            getExtraOption()?.let {
                when (it) {
                    PageUrlType.OFERTAS_ESPECIALES -> goToOfertasEspeciales()
                    PageUrlType.MEDALLAS -> goToDetalleLogro()
                }
            }
        }
    }

    override fun initializeInjector() {
        component = DaggerCaminoBrillanteComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    private fun getExtraOption(): String? {
        return intent.getStringExtra(OPTION)
    }

    override fun initControls() {
    }

    override fun initEvents() {}

    override fun getComponent(): CaminoBrillanteComponent? {
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

    override fun onBackPressed() {
        if (canBack) {
            val fragmentManager = supportFragmentManager

            when (visibleFragment) {
                tutorialFragment -> {
                    finish()
                }
                else -> when {
                    fragmentManager.backStackEntryCount > 0 -> {
                        fragmentManager.popBackStackImmediate()
                        if (visibleFragment is CaminoBrillanteFragment) {
                            fragment?.onFragmentVisible(false)
                        }
                    }
                    else -> {
                        when {
                            !isNivelActualConsultora -> {
                                fragment?.autoSelectedCurrentLevel()
                            }
                            else -> {
                                val returnIntent = Intent()
                                setResult(Activity.RESULT_CANCELED, returnIntent)
                                finish()
                            }
                        }
                    }
                }
            }
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
        title = getString(R.string.camino_brillante)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view: View? = currentFocus
        val ret: Boolean = super.dispatchTouchEvent(event)
        view?.let{viewValidate ->
            if (viewValidate is EditText) {
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

    fun hideKeyboard() {
        val inputMethodManager = context().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun goToFragmentCaminoBrillante() {
        hideKeyboard()
        fragment = CaminoBrillanteFragment.newInstance()
        fragment?.let { replaceFragment(R.id.fltContainer, it, true) }
    }

    override fun removeTutorial() {
        supportFragmentManager.popBackStack()
        fragment?.showConfirmDialog()
    }

    override fun goToOfertasEspeciales() {
        hideKeyboard()
        ofertasEspecialesFragment = OfertasEspecialesFragment.newInstance()
        addFragment(R.id.fltContainer, ofertasEspecialesFragment, false)
    }

    override fun goToDetalleLogro() {
        hideKeyboard()
        logroUnificadoFragment = LogroUnificadoFragment.newInstance()
        addFragment(R.id.fltContainer, logroUnificadoFragment, false)
    }

    override fun goToOrders(menu: Menu) {
        navigator.navigateToOrdersWithResult(this, menu)
    }

    override fun goToOnBoarding() {
        tutorialFragment = TutorialFragment.newInstance()
        tutorialFragment?.listenerTutorial = this
        addFragment(R.id.fltContainer, tutorialFragment, false)
    }

    override fun hideLoadingOfertas() {
        if (!isLoading && ofertasEspecialesFragment!!.getSizeCount() > 1) {
            isLoading = true
            canBack = false
        } else {
            hideLoading()
            canBack = true
        }
    }

    override fun goToOffers() {
        intent.putExtra(AddOrdersActivity.RESULT_GO_TO_OFFERS, true)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun resetIsLoading() {
        isLoading = false
    }

    override fun getCurrentPosition(): Int {
        return ofertasEspecialesFragment?.getCurrentPosition() ?: 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AddOrdersActivity.RESULT -> {
                val resultOk = resultCode == Activity.RESULT_OK
                val resultGoToOffers = data?.getBooleanExtra(AddOrdersActivity.RESULT_GO_TO_OFFERS, false)
                    ?: false
                if (resultOk and resultGoToOffers) {
                    goToOffers()
                } else {
                    ofertasEspecialesFragment?.reloadOffers()
                    fragment?.onFragmentVisible(true)
                }
            }
            OrderWebActivity.RESULT -> {
                ofertasEspecialesFragment?.reloadOffers()
                fragment?.onFragmentVisible(true)
            }
            BaseFichaActivity.RESULT -> {
                ofertasEspecialesFragment?.reloadOffers()
                fragment?.onFragmentVisible(true)
            }

        }
    }

    override fun onSelectNivel(isNivelActualConsultora: Boolean) {
        this.isNivelActualConsultora = isNivelActualConsultora
    }

    override fun onClickItem(key: String, marcaId: Int, marcaName: String, origenPedidoWeb: String) {
        val extras = Bundle()
        extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, key)
        extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, "0")
        extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaId)
        extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marcaName)
        extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, origenPedidoWeb)
        extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB, origenPedidoWeb)
        extras.putBoolean(BaseFichaActivity.EXTRA_ENABLE_SHARE, false)
        extras.putBoolean(BaseFichaActivity.EXTRA_ENABLE_SEARCH, false)
        extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_CAMINO_BRILLANTE)

        navigator.navigateToFichaCaminoBrillante(this, extras)
    }

    override fun setCanBack(canBack: Boolean) {
        this.canBack = canBack
    }

    companion object {
        const val OPTION = "OPTION"

        const val EXTRA_COUNT_BADGE = "EXTRA_COUNT_BADGE"
        const val BROADCAST_COUNT_ACTION = "BROADCAST_COUNT_ACTION"

        const val TIPO_OFERTA_KIT = 1
        const val TIPO_OFERTA_DEMOSTRADOR = 2
    }

}
