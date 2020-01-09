package biz.belcorp.consultoras.feature.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.*
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.dialog.MessageDialog
import biz.belcorp.consultoras.common.fcm.FBEventBusEntity
import biz.belcorp.consultoras.common.fcm.RefreshReceiver
import biz.belcorp.consultoras.common.model.brand.BrandConfigModel
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.dreammeter.DreamMederModel
import biz.belcorp.consultoras.common.model.menu.MenuModel
import biz.belcorp.consultoras.common.model.stories.StorieModel
import biz.belcorp.consultoras.common.model.winonclick.WinOnClickModel
import biz.belcorp.consultoras.common.network.NetworkEvent
import biz.belcorp.consultoras.common.sync.SyncEvent
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.auth.startTutorial.ContenidoResumenDetalleModel
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.campaigninformation.CampaignInformationActivity
import biz.belcorp.consultoras.feature.catalog.CatalogContainerActivity
import biz.belcorp.consultoras.feature.client.edit.ClientEditActivity
import biz.belcorp.consultoras.feature.dreammeter.DreamMeterActivity
import biz.belcorp.consultoras.feature.embedded.changes.ChangesActivity
import biz.belcorp.consultoras.feature.embedded.closeout.CloseoutActivity
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity
import biz.belcorp.consultoras.feature.embedded.offers.OffersActivity
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicActivity
import biz.belcorp.consultoras.feature.embedded.success.WalkSuccessActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity.Companion.BROADCAST_COUNT_ACTION
import biz.belcorp.consultoras.feature.galery.GalleryActivity
import biz.belcorp.consultoras.feature.home.accountstate.AccountStateActivity
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.clients.ClientsListFragment
import biz.belcorp.consultoras.feature.home.di.DaggerHomeComponent
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasFragment
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackActivity
import biz.belcorp.consultoras.feature.home.incentives.IncentivesContainerFragment
import biz.belcorp.consultoras.feature.home.menu.lateral.MenuLateralListAdapter
import biz.belcorp.consultoras.feature.home.menu.top.MenuTopListAdapter
import biz.belcorp.consultoras.feature.home.myorders.MyOrdersActivity
import biz.belcorp.consultoras.feature.home.profile.MyProfileActivity
import biz.belcorp.consultoras.feature.home.storie.StorieActivity
import biz.belcorp.consultoras.feature.home.subcampaing.FestSubCampaingActivity
import biz.belcorp.consultoras.feature.home.tracking.TrackingActivity
import biz.belcorp.consultoras.feature.home.tutorial.Holiday
import biz.belcorp.consultoras.feature.home.winonclick.WinOnClickActivity
import biz.belcorp.consultoras.sync.SyncUtil
import biz.belcorp.consultoras.util.*
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.library.annotation.Country
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.mobile.analytics.core.Analytics
import biz.belcorp.library.util.KeyboardUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.mobile.components.dialogs.list.ListDialog
import biz.belcorp.mobile.components.dialogs.list.adapters.ListDialogAdapter
import biz.belcorp.mobile.components.dialogs.list.model.ListDialogModel
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.view_connection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class HomeActivity : BaseActivity(), HasComponent<HomeComponent>, LoadingView, BaseHomeFragment.BaseHomeFragmentListener, HomeFragment.HomeFragmentListener, ClientsListFragment.ClientListFragmentListener, GanaMasFragment.Listener {

    private var component: HomeComponent? = null

    internal var countryISO = ""
    private var menuActual = "MEN_INIT"

    private var tieneGND: Boolean = false
    private var belcorpExperience: Boolean = false
    private var christmasExperience = false
    private var userType: Int = 0
    private var revistaDigitalSuscripcion: Int = 0
    private var ganaMasNativo = false
    private var ganaMasSuscrita = false

    private var backlocked = false
    private var holidayAnimation = false

    private var hasCupon = false
    private var isCupon = false

    private var hasDatamiMessage = false
    private var isDatamiMessage = false
    private var goToGanaMas = false

    private var consultantName: String? = null

    private var ordersCount: Int = 0

    private var countReceiver: BroadcastReceiver? = null
    private var actionAtpReceiver: BroadcastReceiver? = null
    private val listDialogOrder = ListDialog()

    private var menuModel: MenuModel? = null
    private var winOnClickModel: WinOnClickModel? = null
    private var dreamMederModel: DreamMederModel? = null
    private var brandConfigModel: BrandConfigModel? = null
    var contenidoTutorial = ArrayList<ContenidoResumenDetalleModel>()

    private var isSearchEnabled: Boolean = false

    /** */

    private val sessionListener: MessageDialog.MessageDialogListener = object : MessageDialog.MessageDialogListener {
        override fun aceptar() {

            Analytics.clearData()
            RestApi.clearCache(applicationContext)

            val baseHomeFragment = visibleFragment
            if (baseHomeFragment is BaseHomeFragment) {
                baseHomeFragment.logout()
            }

        }

        override fun cancelar() {
            // Empty
        }
    }

    fun goToLogin() {
        this.navigator.navigateToLogin(this, null)
        this.finish()
    }

    fun goToLoginFacebook() {
        this.navigator.navigateToLoginFacebook(this, null)
        this.finish()
    }

    fun goToConfiguration() {
        ivwBack.visibility = View.VISIBLE
        drawer.closeDrawer(GravityCompat.START)
        this.navigator.navigateToConfiguration(this)
    }

    fun showMenuOption(code: String?, status: Boolean?, title: String?){
        menuLateral.showMenuOption(code, status, title)
    }

    /** */

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)

        startNavigationDrawer()
        init(savedInstanceState)

        tvwVersion.text = getString(R.string.home_version_name).format(BuildConfig.VERSION_NAME)
        tvwCerrarSesion.paintFlags = tvwCerrarSesion.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvwMultiOrdermessage.text = FirebaseRemoteConfig.getInstance().getString(BuildConfig.REMOTE_CONFIG_MESSAGE_MULTI_ORDER)

        menuLateral.setOnMenuItemSelectedListener(object : MenuLateralListAdapter.OnItemSelectedListener {
            override fun onMenuItemSelected(menuModel: MenuModel, position: Int) {
                BelcorpLogger.d(TAG, menuModel.descripcion)

                if (supportFragmentManager.findFragmentById(R.id.fltContainer).tag != menuModel.codigo) {
                    updateCurrentMenuItem(menuModel.codigo)
                    when (menuModel.codigo) {
                        MenuCode.HOME -> {
                            menuTop.selectCurrentItem(MenuCodeTop.HOME)
                            goToDashboard(0)
                        }
                        MenuCode.CATALOG -> {
                            goToCatalog(0, menuModel.descripcion)
                            trackEventAB(0, GlobalConstant.EVENT_NAME_CATALOG_LATERAL, GlobalConstant.EVENT_LABEL_CATALOG)
                        }
                        MenuCode.TRACK_ORDERS -> goToTrackOrders(0)
                        MenuCode.TUTORIAL -> goToTutorial(null, null, 0)
                        MenuCode.BUZON -> goToMailbox()
                        MenuCode.TERMINOS -> goToTerms(0)
                        MenuCode.ACADEMIA -> goToAcademia(0)
                        MenuCode.TUVOZ -> {
                            Tracker.TuVozOnline.OnClickMenuLateral((visibleFragment as? BaseHomeFragment)?.presenterHome?.userTrack)
                            goToTuVozOnline(0)
                        }
                        MenuCode.CAMINO -> goToCaminoAlExito(0)
                        MenuCode.MQVIRTUAL -> goToMQVirtual(0)
                        MenuCode.ESCANER_QR -> {
                            trackEvent((visibleFragment as BaseHomeFragment)?.presenterHome?.userTrack)
                            goToScannerQR()
                        }
                        MenuCode.INFO_CAMPANIA -> {
                            Tracker.InformacionCampanias.onClickMenuLateral((visibleFragment as? BaseHomeFragment)?.presenterHome?.userTrack)
                            goToInfoCampania()
                        }
                        MenuCode.NAVI_FEST -> goToNaviFestContainer()
                        MenuCode.MEN_LAT_SEC_DESCARGA -> {
                            Tracker.Gallery.trackMenuLateral((visibleFragment as BaseHomeFragment)?.presenterHome?.userTrack)
                            goToDownloadableSection()
                        }
                        MenuCode.MEN_ESIKA_AHORA -> goToEsikaAhora()
                        MenuCode.ASESOR_REGALO -> {
                            (visibleFragment as? BaseHomeFragment)?.presenterHome?.gestionarAsesorRegalo()
                        }
                    }
                } else {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

            override fun shouldMove(): Boolean {
                return !isBackLocked
            }
        })

        menuLateral.setOnSubMenuItemSelectedListener { menuModel, menuPosition, subMenuModel, subMenuPosition ->
            BelcorpLogger.d(TAG, subMenuModel.descripcion)

            if (supportFragmentManager.findFragmentById(R.id.fltContainer).tag != subMenuModel.codigo) {
                updateCurrentMenuItem(subMenuModel.codigo)
                when (subMenuModel.codigo) {
                    MenuCode.HOME -> {
                        menuTop.selectCurrentItem(MenuCodeTop.HOME)
                        goToDashboard(0)
                    }
                    MenuCode.CONFIGURATION -> goToConfiguration()
                    MenuCode.CLIENTS -> {
                        menuTop.selectCurrentItem(MenuCodeTop.CLIENTS)
                        goToMyClients(0, 0)
                    }
                    MenuCode.ORDERS -> goToNewOrder(0, 0)
                    MenuCode.ORDERS_NATIVE -> goToOrdersNative(0)
                    MenuCode.OFFERS -> goToOffers(0, OfferTypes.PALANCA_DEFAULT)
                    MenuCode.INCENTIVES -> {
                        menuTop.selectCurrentItem(MenuCodeTop.INCENTIVES)
                        goToIncentives(0, 0)
                    }
                    MenuCode.DEBTS -> {
                        menuTop.selectCurrentItem(MenuCodeTop.CLIENTS)
                        goToDebts(0)
                    }
                    MenuCode.TUTORIAL -> goToTutorial(null, null, 0)
                    MenuCode.BUZON -> goToMailbox()
                    MenuCode.TERMINOS -> goToTerms(0)
                    MenuCode.PRODUCTOS_AGOTADOS -> goToProAgot(0)
                    MenuCode.ESTADO_CUENTA -> goToAccountStatus(0)
                    MenuCode.CAMINO_BRILLANTE -> goToCaminoBrillante(0)
                    MenuCode.CATALOG -> {
                        goToCatalog(0, subMenuModel.descripcion)
                        trackEventAB(0, GlobalConstant.EVENT_NAME_CATALOG_LATERAL,
                            GlobalConstant.EVENT_LABEL_CATALOG)
                    }
                    MenuCode.TRACK_ORDERS -> goToTrackOrders(0)
                    MenuCode.CLOSEOUT -> goToCloseout(0)
                    MenuCode.CAMBIOS_DEVOLUCIONES -> goToCambiosDevoluciones(0, subMenuModel.descripcion)
                    MenuCode.PEDIDOS_FIC -> goToPedidosFic(0, subMenuModel.descripcion)
                    MenuCode.MIS_PEDIDOS -> goToMisPedidos(0)
                    MenuCode.TUVOZ -> goToTuVozOnline(0)
                    MenuCode.MQVIRTUAL -> goToMQVirtual(0)
                    MenuCode.PEDIDOPEND -> goToPedidosPendiente(0)
                    MenuCode.INFO_CAMPANIA -> {
                        Tracker.InformacionCampanias.onClickMenuLateral((visibleFragment as? BaseHomeFragment)?.presenterHome?.userTrack)
                        goToInfoCampania()
                    }
                    MenuCode.TERMOMETRO_SUENIO -> goToDreamMeter(0)
                }// EMPTY
            } else {
                drawer.closeDrawer(GravityCompat.START)
            }

        }

        menuTop.setOnMenuItemSelectedListener(object : MenuTopListAdapter.OnMenuItemSelectedListener {
            override fun onMenuItemSelected(menuModel: MenuModel, position: Int) {

                updateCurrentMenuItem(menuModel.codigo)
                currentFocus?.let { KeyboardUtil.dismissKeyboard(this@HomeActivity, it) }
                when (menuModel.codigo) {
                    MenuCodeTop.HOME -> goToDashboard(1)
                    MenuCodeTop.CONFIGURATION -> goToConfiguration()
                    MenuCodeTop.CLIENTS ->

                        goToMyClients(0, 1)
                    MenuCodeTop.ORDERS -> goToNewOrder(0, 1)
                    MenuCodeTop.ORDERS_NATIVE -> goToOrdersNative(1)
                    MenuCodeTop.INCENTIVES -> goToIncentives(0, 1)
                    MenuCodeTop.DEBTS -> goToDebts(0)
                    MenuCodeTop.OFFERS -> goToOffers(1, OfferTypes.PALANCA_DEFAULT)
                    MenuCodeTop.STOCKOUT -> goToProAgot(1)
                    MenuCodeTop.ACCOUNT_STATE -> goToAccountStatus(1)
                    MenuCodeTop.CATALOG -> {
                        goToCatalog(1, menuModel.descripcion)
                        trackEventAB(1, GlobalConstant.EVENT_NAME_CATALOG_TOP,
                            GlobalConstant.EVENT_LABEL_CATALOG)
                    }
                    MenuCodeTop.TRACK_ORDERS -> goToTrackOrders(1)
                    MenuCode.CLOSEOUT -> goToCloseout(1)
                }// EMPTY
            }

            override fun onMenuClick(menuModel: MenuModel, position: Int) {
                if (menuModel.codigo == MenuCodeTop.OFFERS && supportFragmentManager.findFragmentById(R.id.fltContainer)
                        .tag == MenuCode.OFFERS) {
                    val gmf = supportFragmentManager
                        .findFragmentById(R.id.fltContainer) as GanaMasFragment
                    if(ganaMasSuscrita)
                        isToolbarText(resources.getString(R.string.home_card_offer_sub_title_gana_mas), ICON_GANAMAS)
                    else
                        isToolbarText(resources.getString(R.string.home_card_offer_sub_title_offers_digitales), ICON_GANAMAS)
                    gmf.resetFragmentView()
                }
            }

            override fun shouldMove(): Boolean {
                return !isBackLocked
            }
        })

        if (Belcorp.checkBelcorpExperience()) {
            ivwToolbarRight.visibility = View.GONE
            toolbar.background = ContextCompat.getDrawable(this, R.drawable.bg_toolbar_gradient_belcorp_fifty)
            belcorpExperience = true
        } else if (Belcorp.checkChristmasExperience()) {
            toolbar.background = resources.getDrawable(R.drawable.bg_christmas_toolbar)
            ivwToolbarRight.visibility = View.VISIBLE
            ivwToolbarRight.background = resources.getDrawable(R.drawable.bg_christmas_green)
            frameChristmas.visibility = View.VISIBLE
            christmasExperience = true
        } else {
            lltFanPage?.visibility = View.GONE
            belcorpExperience = false
        }

        ivwSearch.setOnClickListener {
            (visibleFragment as? BaseHomeFragment)?.trackEvent(GlobalConstant.SCREEN_HOME,
                GlobalConstant.EVENT_CAT_SEARCH,
                GlobalConstant.EVENT_ACTION_SEARCH_SELECTION,
                GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, false)

            navigator.navigateToSearch(this)
        }

        flContainerForClickSearchAbTesting.setOnClickListener {
            if (visibleFragment is BaseHomeFragment)
                (visibleFragment as BaseHomeFragment)
                    .trackEvent(GlobalConstant.SCREEN_HOME,
                        GlobalConstant.EVENT_CAT_SEARCH,
                        GlobalConstant.EVENT_ACTION_SEARCH_SELECTION,
                        GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
                        GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, false)

            navigator.navigateToSearch(this)
        }

        ivwNotificacion.setOnClickListener {
            Tracker.Notificaciones.iconNotificaciones()
            (visibleFragment as? BaseHomeFragment)?.updateNotificationStatus(false)
            navigator.navigateToNotificationList(this)

        }

        rlayCart.setOnClickListener {
            menuModel?.let { menu ->
                if (menu.isVisible && menu.codigo == MenuCodeTop.ORDERS) {
                    goToNewOrder(1, -1)
                } else if (menu.isVisible && menu.codigo == MenuCodeTop.ORDERS_NATIVE) {
                    goToOrdersNative(-1)
                }
            }
        }

        rlContainerCartAbTesting.setOnClickListener {
            menuModel?.let{ menu ->
                if (menu.isVisible && menu.codigo  == MenuCodeTop.ORDERS) {
                    goToNewOrder(1, -1)
                } else if (menu.isVisible && menu.codigo == MenuCodeTop.ORDERS_NATIVE) {
                    goToOrdersNative(-1)
                }
            }

        }

        updateOffersCount(SessionManager.getInstance(this).getOrdersCount())

        registerCountBroadcast()
        registerBannerAtpBroadcast()

        checkGanaMasFlags()
    }

    private fun registerCountBroadcast() {
        countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                val isFromKit = intent.extras?.getBoolean(KIT_NUEVA_BR_KEY) ?: false
                val f: Fragment = supportFragmentManager.findFragmentById(R.id.fltContainer)
                val newCount = SessionManager.getInstance(this@HomeActivity).getOrdersCount() ?: 0

                if (f is GanaMasFragment && isFromKit) {

                    f.updateKitNueva(newCount > ordersCount)

                }

                updateOffersCount(newCount)
            }
        }

        registerReceiver(countReceiver, IntentFilter(BROADCAST_COUNT_ACTION))
    }

    private fun goToCaminoBrillante(menuType: Int) {
        drawer.closeDrawer(GravityCompat.START)

        val intent = Intent(this, CaminoBrillanteActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_OFFERS)
    }

    private fun goToDreamMeter(menuType: Int) {
        drawer.closeDrawer(GravityCompat.START)
        val intent = Intent(this, DreamMeterActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_OFFERS)
    }

    private fun registerBannerAtpBroadcast() {
        actionAtpReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                supportFragmentManager.findFragmentById(R.id.fltContainer)?.let { fr ->
                    if (fr.tag == MenuCode.OFFERS) {
                        //val gmf = supportFragmentManager.findFragmentById(R.id.fltContainer) as GanaMasFragment

                        (supportFragmentManager.findFragmentById(R.id.fltContainer) as? GanaMasFragment)?.let { gmf ->
                            intent.getIntExtra(ArmaTuPackActivity.BROADCAST_STATE_ATP_EXTRAS, -1).apply {
                                when(this){
                                    ArmaTuPackStateType.INSERT_UPDATE -> gmf.updateBannerAtp(OfferTypes.ATP, true)
                                    ArmaTuPackStateType.DELETE -> gmf.updateBannerAtp(OfferTypes.ATP, false)
                                    else -> {
                                    }
                                }
                            }


                        }

                    }

                }



            }
        }

        registerReceiver(actionAtpReceiver, IntentFilter(ArmaTuPackActivity.BROADCAST_ATP_ACTION))
    }

    private fun checkGanaMasFlags() {
        goToGanaMas = intent.getBooleanExtra(EXTRA_KEY_LANDING, false)
    }

    fun showNotificactionIcon(quantity: Int) {

        ivwNotificacion.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_notification))
        if (quantity > 0) {
            if (quantity > 99) {
                tviCampana.text = 99.toString()
            } else
                tviCampana.text = quantity.toString()

            tviCampana.visibility = View.VISIBLE
        } else {
            tviCampana.text = 0.toString()
            tviCampana.visibility = View.GONE
        }
    }

    private fun goToCaminoAlExito(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_MENU_CAMINO)

        if (!NetworkUtil.isThereInternetConnection(context())) {
            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment?.showNetworkError()
            return
        }

        val intent = Intent(this, WalkSuccessActivity::class.java)
        startActivity(intent)

        drawer.closeDrawer(GravityCompat.START)

    }

    private fun checkFanPage(iso: String) {

        val calendar = Calendar.getInstance()

        when (iso) {
            Country.DO -> lltFanPage.visibility = View.GONE
            Country.BO -> {

                //Init : 20 de Junio del 2018
                val calendarBO = Calendar.getInstance()
                calendarBO.set(Calendar.YEAR, 2018)
                calendarBO.set(Calendar.MONTH, Calendar.JUNE)
                calendarBO.set(Calendar.DAY_OF_MONTH, 20)
                calendarBO.set(Calendar.HOUR, 0)
                calendarBO.set(Calendar.MINUTE, 0)

                //Expire : 01 de Julio del 2018
                val belcorpBO = Calendar.getInstance()
                belcorpBO.set(Calendar.YEAR, 2018)
                belcorpBO.set(Calendar.MONTH, Calendar.JULY)
                belcorpBO.set(Calendar.DAY_OF_MONTH, 1)
                belcorpBO.set(Calendar.HOUR, 23)
                belcorpBO.set(Calendar.MINUTE, 59)

                if (calendarBO.get(Calendar.YEAR) == belcorpBO.get(Calendar.YEAR) && calendar.after(calendarBO) && calendar.before(belcorpBO)) {
                    lltFanPage.visibility = View.VISIBLE
                    tvwFanpageHashtag.text = String.format(getString(R.string.home_fanpage_hashtag), CountryUtil().getHashtagBelcorp50Years(countryISO))
                } else {
                    lltFanPage.visibility = View.GONE
                }
            }
            else -> {

                //Init : 18 de Junio del 2018
                val calendarDE = Calendar.getInstance()
                calendarDE.set(Calendar.YEAR, 2018)
                calendarDE.set(Calendar.MONTH, Calendar.JUNE)
                calendarDE.set(Calendar.DAY_OF_MONTH, 18)
                calendarDE.set(Calendar.HOUR, 0)
                calendarDE.set(Calendar.MINUTE, 0)

                //Expire : 01 de Julio del 2018
                val belcorpDE = Calendar.getInstance()
                belcorpDE.set(Calendar.YEAR, 2018)
                belcorpDE.set(Calendar.MONTH, Calendar.JULY)
                belcorpDE.set(Calendar.DAY_OF_MONTH, 1)
                belcorpDE.set(Calendar.HOUR, 23)
                belcorpDE.set(Calendar.MINUTE, 59)

                if (calendarDE.get(Calendar.YEAR) == belcorpDE.get(Calendar.YEAR) && calendar.after(calendarDE) && calendar.before(belcorpDE)) {
                    lltFanPage.visibility = View.VISIBLE
                    tvwFanpageHashtag.text = String.format(getString(R.string.home_fanpage_hashtag), CountryUtil().getHashtagBelcorp50Years(countryISO))
                } else {
                    lltFanPage.visibility = View.GONE
                }
            }
        }

        rltNav.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val params = menuLateral.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = rltNav.measuredHeight
        menuLateral.layoutParams = params
    }

    private fun startNavigationDrawer() {
        setSupportActionBar(toolbar)
        title = ""

        val toggle = object : ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                if (visibleFragment is BaseHomeFragment)
                    (visibleFragment as BaseHomeFragment)
                        .trackMenu(AnalyticsUtil.getScreenName(visibleFragment))

                KeyboardUtil.dismissKeyboard(this@HomeActivity, currentFocus)
                currentFocus?.clearFocus()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                KeyboardUtil.dismissKeyboard(this@HomeActivity, currentFocus)
            }
        }
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = false

        val hView = navigationView.getHeaderView(0)
        lltFanPage?.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            goToFanPage()
        }
        val rltPhoto = hView.findViewById<RelativeLayout>(R.id.rlt_photo)
        rltPhoto.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            goToProfile()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        setMenuItemSelected()
    }

    private fun setMenuItemSelected() {
        visibleFragment?.let { fragment ->
            if (fragment is ClientsListFragment) {
                menuTop.setMenuCode(MenuCodeTop.CLIENTS)
            } else if (fragment is IncentivesContainerFragment) {
                menuTop.setMenuCode(MenuCodeTop.INCENTIVES)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(countReceiver)
    }

    override fun onResume() {
        super.onResume()
        checkNotifications()
        if (NetworkUtil.isThereInternetConnection(this)) {
            setStatusTopNetwork()
        } else {
            vwConnection.visibility = View.VISIBLE
            tvw_connection_message.setText(R.string.connection_offline)
            ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
        }
    }

    private fun setStatusTopNetwork() {
        when (ConsultorasApp.getInstance().datamiType) {
            NetworkEventType.DATAMI_AVAILABLE -> {
                multiOrderView.visibility = View.GONE
                connectionView.visibility = View.VISIBLE
                vwConnection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> vwConnection.visibility = View.GONE
            NetworkEventType.MULTI_ORDER_AVAILABLE -> {
                multiOrderView.visibility = View.VISIBLE
                connectionView.visibility = View.GONE
                vwConnection.visibility = View.VISIBLE
            }
            else -> vwConnection.visibility = View.GONE
        }
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FBEventBusEntity) {
        checkNotifications()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: GanaMasFragment.RestartGanaMasEvent) {
        goToOffers(3, OfferTypes.PALANCA_DEFAULT)
    }

    private fun checkNotifications() {
        val baseHomeFragment = visibleFragment as? BaseHomeFragment
        baseHomeFragment?.checkNotifications()
    }

    override fun onBackPressed() {
        if (!isBackLocked) {
            if (supportFragmentManager.backStackEntryCount > 0) {

                var isFragmentBackPressed = false

                if (supportFragmentManager.findFragmentById(R.id.fltContainer).tag == MenuCode.OFFERS) {
                    (supportFragmentManager.findFragmentById(R.id.fltContainer) as? GanaMasFragment)?.let { gmf ->
                        isFragmentBackPressed = gmf.onBackPress()
                    }
                }

                if (isFragmentBackPressed) {
                    if(ganaMasSuscrita)
                        isToolbarText(resources.getString(R.string.home_card_offer_sub_title_gana_mas), ICON_GANAMAS)
                    else
                        isToolbarText(resources.getString(R.string.home_card_offer_sub_title_offers_digitales), ICON_GANAMAS)
                } else {
                    isToolbarText(false, ICON_DEFAULT)
                    changeToolbars()
                }


            } else {
                moveTaskToBack(true)
            }
        }
    }


    /** */

    private fun changeToolbars() {


        if (supportFragmentManager.backStackEntryCount == 1) {
            ivwBack.visibility = View.GONE
        }

        supportFragmentManager.popBackStack()
        supportFragmentManager.executePendingTransactions()

        when (supportFragmentManager.findFragmentById(R.id.fltContainer).tag) {
            MenuCode.HOME -> menuTop.selectCurrentItem(MenuCodeTop.HOME)
            MenuCode.CLIENTS, MenuCode.DEBTS, MenuCode.CLIENTS_WITH_ORDER -> menuTop.selectCurrentItem(MenuCodeTop.CLIENTS)
            MenuCode.ORDERS -> menuTop.selectCurrentItem(MenuCodeTop.ORDERS)
            MenuCode.INCENTIVES -> menuTop.selectCurrentItem(MenuCodeTop.INCENTIVES)
            MenuCode.OFFERS -> {
                menuTop.selectCurrentItem(MenuCodeTop.OFFERS)

                if (ganaMasNativo) {
                    if(ganaMasSuscrita)
                        isToolbarText(resources.getString(R.string.home_card_offer_sub_title_gana_mas), ICON_GANAMAS)
                    else
                        isToolbarText(resources.getString(R.string.home_card_offer_sub_title_offers_digitales), ICON_GANAMAS)

                    (supportFragmentManager.findFragmentById(R.id.fltContainer) as? GanaMasFragment)?.let{ gmf ->
                        gmf.setupViewPager()
                        gmf.resetScrollSection()
                    }
                }
            }
            MenuCode.ESTADO_CUENTA -> menuTop.selectCurrentItem(MenuCodeTop.ACCOUNT_STATE)
            MenuCode.PRODUCTOS_AGOTADOS -> menuTop.selectCurrentItem(MenuCodeTop.STOCKOUT)
            MenuCode.TRACK_ORDERS -> menuTop.selectCurrentItem(MenuCodeTop.TRACK_ORDERS)
            else -> {
            }
        }
    }

    fun trackCardEvent(type: Int, label: String) {
        Tracker.Home.trackHomeCard(type, label)
    }

    private fun trackEvent(menuType: Int, eventLabel: String) {
        if (visibleFragment is BaseHomeFragment) {
            val eventName = AnalyticsUtil.getMenuLabel(menuType)
            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment.trackEvent(menuType, eventLabel, eventName,
                AnalyticsUtil.getScreenName(visibleFragment))
        }
    }

    private fun trackEvent(menuType: Int, eventName: String, eventAction: String) {
        if (visibleFragment is BaseHomeFragment) {
            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment.trackEvent(menuType, GlobalConstant.NOT_AVAILABLE, eventName,
                AnalyticsUtil.getScreenName(visibleFragment), eventAction)
        }
    }

    private fun trackEvent(menuType: Int, eventLabel: String, eventName: String, eventAction: String) {
        if (visibleFragment is BaseHomeFragment) {
            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment.trackEvent(menuType, eventLabel, eventName,
                AnalyticsUtil.getScreenName(visibleFragment), eventAction)
        }
    }

    private fun trackEventAB(menuType: Int, eventName: String, eventLabel: String) {
        if (visibleFragment is BaseHomeFragment) {

            val eventCat: String
            val eventAction: String

            when (menuType) {
                0 -> {
                    eventCat = GlobalConstant.EVENT_CATEGORY_LATERAL
                    eventAction = GlobalConstant.EVENT_ACTION_MENU_AB
                }
                1 -> {
                    eventCat = GlobalConstant.EVENT_CATEGORY_TOP
                    eventAction = GlobalConstant.EVENT_ACTION_OPTION_AB
                }
                else -> {
                    eventCat = GlobalConstant.EVENT_CATEGORY_TOP
                    eventAction = GlobalConstant.EVENT_ACTION_OPTION_AB
                }
            }

            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment.trackEvent(GlobalConstant.SCREEN_HOME, eventCat, eventAction,
                eventLabel, eventName, false)
        }
    }

    private fun trackEvent(user: User?) {
        user?.let{ Tracker.EscanerQR.OnClickMenuLateral(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            200 -> goToMyClients(0, -1)
            REQUEST_CODE_PROFILE -> {
                (visibleFragment as? BaseHomeFragment)?.getUserDataOffline()
            }
            REQUEST_CODE_OFFERS -> if (resultCode == RESULT_OK) {
                data?.apply {
                    if (getBooleanExtra(AddOrdersActivity.RESULT_GO_TO_OFFERS, false)) {
                        goToOffers(3, OfferTypes.PALANCA_DEFAULT)
                    }
                }

            }
            StorieActivity.ACTIVITYSTORIE -> {
                if (resultCode == RESULT_OK) {
                    doRefreshData()
                }
            }
            REQUEST_CODE_REVISTA_DIGITAL -> {
                if (resultCode == RESULT_OK) {
                    val gmf = supportFragmentManager.findFragmentById(R.id.fltContainer) as GanaMasFragment
                    gmf.clearSaberMas()
                }
            }
            else -> {
            }
        }
    }

    /** */

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
                vwConnection.visibility = View.VISIBLE
                tvw_connection_message.setText(R.string.connection_offline)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert))
            }
            NetworkEventType.DATAMI_AVAILABLE -> {
                multiOrderView.visibility = View.GONE
                connectionView.visibility = View.VISIBLE
                vwConnection.visibility = View.VISIBLE
                tvw_connection_message.text = getString(R.string.connection_datami_available)
                ivw_connection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet))
            }
            NetworkEventType.DATAMI_NOT_AVAILABLE, NetworkEventType.WIFI -> vwConnection.visibility = View.GONE
            NetworkEventType.MULTI_ORDER_AVAILABLE -> {
                tvwMultiOrdermessage.text = FirebaseRemoteConfig.getInstance().getString(BuildConfig.REMOTE_CONFIG_MESSAGE_MULTI_ORDER)
                multiOrderView.visibility = View.VISIBLE
                connectionView.visibility = View.GONE
                vwConnection.visibility = View.VISIBLE
            }
            else -> vwConnection.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSyncEvent(event: SyncEvent) {
        if (event.isSync) {
            vwLoadingSync.visibility = View.VISIBLE
            lockBackButtons()
        } else {
            backlocked = false
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            vwLoadingSync.visibility = View.GONE
            doRefreshData()
        }
    }

    /** */

    private val onMenuClick = View.OnClickListener {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (!isBackLocked) {
            drawer.openDrawer(GravityCompat.START)
        }
    }


    private val onBackClick = View.OnClickListener {
        (visibleFragment as? BaseHomeFragment)?.trackBackPressed(AnalyticsUtil.getScreenName(visibleFragment))

        var view = currentFocus
        if (view == null) view = View(this)
        KeyboardUtil.dismissKeyboard(this, view)

        onBackPressed()
    }

    private val onCerrarSesionClick = View.OnClickListener {
        drawer.closeDrawer(GravityCompat.START)

        try {
            MessageDialog()
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.home_dg_close_session_title)
                .setStringMessage(R.string.home_dg_close_session_message)
                .setStringAceptar(R.string.home_cerrar_sesion)
                .setStringCancelar(R.string.button_cancelar)
                .showIcon(true)
                .showClose(true)
                .showCancel(true)
                .setListener(sessionListener)
                .show(supportFragmentManager, "modalSession")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalSession", e)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        initializeInjector()
        if (savedInstanceState == null) {
            startHome()
        }
    }

    private fun startHome() {
        val fragment = HomeFragment()
        fragment.arguments = intent.extras
        addFragment(R.id.fltContainer, fragment, true, MenuCode.HOME)
        intent.removeExtra(GlobalConstant.LOGIN_STATE)
        SyncUtil.createSyncAccount(context())

        //registrar el push del remote config
        FirebaseMessaging.getInstance().subscribeToTopic("PUSH_RC")

        //broadcast receiver del remote config
        val refreshReceiver = RefreshReceiver(this)
        val filterRefresh = IntentFilter(GlobalConstant.REFRESH_RC)
        registerReceiver(refreshReceiver, filterRefresh)

        isToolbarText(ICON_DEFAULT)
        fltMenu.setOnClickListener(onMenuClick)
        ivwBack.setOnClickListener(onBackClick)
        tvwCerrarSesion.setOnClickListener(onCerrarSesionClick)

    }

    override fun initializeInjector() {
        this.component = DaggerHomeComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {
        // Empty
    }

    override fun initEvents() {
        // Empty
    }

    /** */

    override fun showLoading() {
        vwLoading.visibility = View.VISIBLE
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun hideLoading() {
        vwLoading.visibility = View.GONE
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    }

    /** */

    override fun getComponent(): HomeComponent? {
        return component
    }

    /** */

    override fun goToMyClients(type: Int, menuType: Int) {

        isToolbarText(false, ICON_DEFAULT)
        if (menuType == -2)
            trackCardEvent(type, GlobalConstant.EVENT_LABEL_CARD_CLIENTS)
        else if (menuType > -1)
            trackEvent(menuType, GlobalConstant.EVENT_LABEL_MENU_CLIENTS)

        ivwBack.visibility = View.VISIBLE

        val bundle = Bundle()
        bundle.putString("menu", MenuCode.CLIENTS)

        val fragment = ClientsListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fltContainer, fragment, MenuCode.CLIENTS)
            .addToBackStack(MenuCode.CLIENTS).commit()

        drawer.closeDrawer(GravityCompat.START)
    }

    override fun goToNewOrder(type: Int, menuType: Int) {
        if (menuType == -1)
            trackCardEvent(type, GlobalConstant.EVENT_LABEL_CARD_ORDERS)
        else
            trackEvent(menuType, GlobalConstant.EVENT_LABEL_MENU_ORDERS)

        if (!NetworkUtil.isThereInternetConnection(context())) {
            val baseHomeFragment = visibleFragment as? BaseHomeFragment
            baseHomeFragment?.showNetworkError()
            return
        }
        drawer.closeDrawer(GravityCompat.START)

        val intent = Intent(this, OrderWebActivity::class.java)
        startActivity(intent)
    }

    override fun goToOrdersNative(menuType: Int) {
        if (menuType == -1)
            trackCardEvent(1, GlobalConstant.EVENT_LABEL_CARD_ORDERS)
        else
            trackEvent(menuType, GlobalConstant.EVENT_LABEL_MENU_ORDERS)

        drawer.closeDrawer(GravityCompat.START)
        if (!NetworkUtil.isThereInternetConnection(context())) {
            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment?.showNetworkError()
            return
        }
        val extras = Bundle()
        extras.putBoolean(PEDIDOS_PENDIENTES_TAG, true)

        val intent = Intent(this, AddOrdersActivity::class.java)
        intent.putExtras(extras)

        brandConfigModel?.apply {
            intent.putExtra(AddOrdersActivity.BRANDING_CONFIG, this)
        }

        startActivity(intent)
    }

    fun goToProAgot(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_PRODUCTOS_AGOTADOS,
            GlobalConstant.ACTION_PRODUCTOS_AGOTADOS)
        drawer.closeDrawer(GravityCompat.START)
        this.navigator.navigateToStockouts(this)
    }

    fun goToAccountStatus(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_ESTADO_CUENTA,
            GlobalConstant.ACTION_ESTADO_CUENTA)
        drawer.closeDrawer(GravityCompat.START)
        val intent = Intent(this, AccountStateActivity::class.java)
        startActivity(intent)
    }

    fun showSearchOption() {
        ivwSearch.visibility = View.VISIBLE
        isSearchEnabled = true
    }

    fun hideSearchOption() {
        ivwSearch.visibility = View.GONE
        isSearchEnabled = false
    }

    override fun goToIncentives(type: Int, menuType: Int) {

        isToolbarText(false, ICON_DEFAULT)
        if (menuType == -1)
            trackCardEvent(type, GlobalConstant.EVENT_LABEL_CARD_INCENTIVES)
        else
            trackEvent(menuType, GlobalConstant.EVENT_LABEL_MENU_INCENTIVES)

        ivwBack.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fltContainer, IncentivesContainerFragment(), MenuCode.INCENTIVES)
            .addToBackStack(MenuCode.INCENTIVES)
            .commit()

        drawer.closeDrawer(GravityCompat.START)
    }

    override fun goToDebts(type: Int) {
        trackCardEvent(type, GlobalConstant.EVENT_LABEL_MENU_DEBTS)

        ivwBack.visibility = View.VISIBLE

        val bundle = Bundle()
        bundle.putString("menu", MenuCode.DEBTS)

        val fragment = ClientsListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fltContainer, fragment,
            MenuCode.DEBTS).addToBackStack(MenuCode.DEBTS).commit()

        drawer.closeDrawer(GravityCompat.START)
    }

    override val isBackLocked: Boolean
        get() = backlocked

    private fun goToFanPage() {
        trackEvent(0, GlobalConstant.EVENT_LABEL_MENU_FANPAGE)

        val url = GlobalConstant.FANPAGE_URL

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$url"))
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

    }

    protected fun goToCatalog(menuType: Int, description: String) {
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_CATALOG)
        drawer.closeDrawer(GravityCompat.START)

        goToCatalog(description)
    }


    fun goToCatalog(description: String) {
        val intent = Intent(this, CatalogContainerActivity::class.java)
        intent.putExtra(GlobalConstant.REVISTA_TITLE, description)
        intent.putExtra(GlobalConstant.REVISTA_GND, tieneGND)
        intent.putExtra(GlobalConstant.REVISTA_CODE, revistaDigitalSuscripcion)
        startActivity(intent)

    }

    private fun goToTrackOrders(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_SEGUIMIENTO_PEDIDOS,
            GlobalConstant.ACTION_SEGUIMIENTO)
        drawer.closeDrawer(GravityCompat.START)

        val intent = Intent(this, TrackingActivity::class.java)
        intent.putExtra(GlobalConstant.TRACKING_TOP, 3)
        startActivity(intent)
    }

    private fun goToCloseout(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_LIQUIDACION_WEB,
            GlobalConstant.ACTION_LIQUIDACION_WEB)
        drawer.closeDrawer(GravityCompat.START)

        if (!NetworkUtil.isThereInternetConnection(context())) {
            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment.showNetworkError()
            return
        }

        val intent = Intent(this, CloseoutActivity::class.java)
        intent.putExtra(CloseoutActivity.OPTION, PageUrlType.LIQUIDACIONWEB)
        startActivity(intent)
    }

    private fun goToCambiosDevoluciones(menuType: Int, descripcion: String) {
        trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_CAMBIOS_DEVOLUCIONES,
            GlobalConstant.ACTION_CAMBIOS_DEVOLUCIONES)
        drawer.closeDrawer(GravityCompat.START)

        if (!NetworkUtil.isThereInternetConnection(context())) {
            val baseHomeFragment = visibleFragment as BaseHomeFragment
            baseHomeFragment.showNetworkError()
            return
        }

        val intent = Intent(this, ChangesActivity::class.java)
        intent.putExtra(GlobalConstant.CLIENTE_ID, 0)
        intent.putExtra(ChangesActivity.OPTION, PageUrlType.CAMBIODEVOLUCIONES)
        intent.putExtra(ChangesActivity.TITLE, descripcion)
        startActivity(intent)
    }

    private fun goToPedidosFic(menuType: Int, descripcion: String) {
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_PEDIDO_FIC)
        drawer.closeDrawer(GravityCompat.START)

        if (!NetworkUtil.isThereInternetConnection(context())) {
            val baseHomeFragment = visibleFragment as? BaseHomeFragment
            baseHomeFragment?.showNetworkError()
            return
        }

        val intent = Intent(this, OrdersFicActivity::class.java)
        intent.putExtra(ChangesActivity.TITLE, descripcion)
        startActivity(intent)
    }

    private fun goToMisPedidos(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_MIS_PEDIDOS, GlobalConstant.ACTION_MIS_PEDIDOS)
        drawer.closeDrawer(GravityCompat.START)

        val intent = Intent(this, MyOrdersActivity::class.java)
        startActivity(intent)
    }

    fun goToTuVozOnline(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_TU_VOZ_ONLINE)
        drawer.closeDrawer(GravityCompat.START)

        if (NetworkUtil.isThereInternetConnection(context())) {
            this.navigator.navigateToTuVozOnline(this)
        } else {
            try {
                val fragment = visibleFragment as? BaseFragment
                fragment?.showNetworkError()
            } catch (e: Exception) {
                BelcorpLogger.d("Error", "Problema al mostrar mensaje de error", e)
            }

        }
    }

    fun goToAcademia(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_MY_ACADEMY)
        drawer.closeDrawer(GravityCompat.START)

        if (NetworkUtil.isThereInternetConnection(context())) {
            this.navigator.navigateToAcademia(this)
        } else {
            try {
                val fragment = visibleFragment as? BaseFragment
                fragment?.showNetworkError()
            } catch (e: Exception) {
                BelcorpLogger.d("Error", "Problema al mostrar mensaje de error", e)
            }

        }
    }

    fun goToChatBot(menuType: Int) {

        if (menuType == 0) drawer.closeDrawer(GravityCompat.START)

        if (!FacebookUtil.isFacebookMessengerInstalled(this)) {
            FacebookUtil.openFacebookMessengerPlayStore(this)
            return
        }

        if (menuType == 1)
            trackEvent(menuType, GlobalConstant.EVENT_LABEL_CHATBOT, GlobalConstant.EVENT_NAME_NAVIGATION_TOP_CHATBOT,
                GlobalConstant.EVENT_ACTION_CHATBOT)
        else
            trackEvent(menuType, GlobalConstant.EVENT_LABEL_CHATBOT)

        if (NetworkUtil.isThereInternetConnection(context())) {
            (visibleFragment as? BaseHomeFragment)?.presenterHome?.goChatBot()
        } else {
            try {
                val fragment = visibleFragment as? BaseFragment
                fragment?.showNetworkError()
            } catch (e: Exception) {
                BelcorpLogger.d("Error", "Problema al mostrar mensaje de error", e)
            }

        }
    }

    fun goToMQVirtual(menuType: Int) {
        drawer.closeDrawer(GravityCompat.START)
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_MAQUILLADOR_VIRTUAL)
        if (OtherAppUtil.isMaquilladorInstalled(context())) {
            val launchIntent = packageManager.getLaunchIntentForPackage(OtherAppUtil.MQVIRTUAL_PACKAGE_NAME)
            if (launchIntent != null) startActivity(launchIntent)
        } else {
            OtherAppUtil.openMaquilladorPlayStore(context())
        }
    }

    fun goToInfoCampania() {
        drawer.closeDrawer(GravityCompat.START)
        startActivity(Intent(this, CampaignInformationActivity::class.java))
    }

    fun goToDownloadableSection(){
        drawer.closeDrawer(GravityCompat.START);
        if (NetworkUtil.isThereInternetConnection(context())) {
            startActivity(Intent(this, GalleryActivity::class.java))
        }else {
            (visibleFragment as? BaseFragment)?.showNetworkError()
        }
    }

    fun goToNaviFestContainer(){
        drawer.closeDrawer(GravityCompat.START);
        if (NetworkUtil.isThereInternetConnection(context())) {
            startActivity(Intent(this, FestActivity::class.java))
        }else {
            (visibleFragment as? BaseFragment)?.showNetworkError()
        }
    }

    fun goToPedidosPendiente(menuType: Int) {
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_PEDIDOS_PENDIENTES)
        navigator.navigateToPedidosPendiente(this)
    }

    fun goToScannerQR() {
        drawer.closeDrawer(GravityCompat.START)
        this.navigator.navigateToScannerQR(this, null)
    }

    fun goToEsikaAhora() {
        drawer.closeDrawer(GravityCompat.START)
        this.navigator.navigateToEsikaAhora(this, null)
    }

    fun openChatBot(url: String) {
        this.navigator.openIntentLink(this, url)
    }

    fun showChatBot(url: String) {
        this.navigator.openIntentLink(this, url)
    }

    fun doRefreshData() {
        val fragment = visibleFragment
        if (fragment is HomeFragment && fragment.isAdded())
            fragment.presenter.refreshData()
        else if (fragment is ClientsListFragment && fragment.isAdded())
            fragment.refreshLists()
    }

    override fun goToProfile() {
        if (userType == UserType.CONSULTORA) {
            val intent = Intent(this, MyProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, REQUEST_CODE_PROFILE)
        } else {
            ToastUtil.show(this, getString(R.string.home_option_unavailable),
                Toast.LENGTH_SHORT)
        }
    }

    override fun goToTutorial(consultantCode: String?, countryISO: String?, menuType: Int) {
        if (menuType > -1) {
            trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_TUTORIAL,
                GlobalConstant.ACTION_TUTORIAL)
        }
        drawer.closeDrawer(GravityCompat.START)
        navigator.navigateToTutorial(this, consultantCode, countryISO)
    }

    override fun goToMailbox() {
        val baseHomeFragment = visibleFragment
        if (baseHomeFragment is BaseHomeFragment) {
            trackEvent(0, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_SUGERENCIAS,
                GlobalConstant.ACTION_SUGERENCIAS)
            drawer.closeDrawer(GravityCompat.START)
            baseHomeFragment.sendFeedBack()
        }
    }

    override fun goToTerms(menuType: Int) {
        if (visibleFragment is BaseHomeFragment) {
            trackEvent(menuType, GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL_TERMINOS,
                GlobalConstant.ACTION_TERMINOS)
            drawer.closeDrawer(GravityCompat.START)
            this.navigator.navigateToLegal(this)
        }
    }

    override fun goToOffers(menuType: Int, palanca: String) {

        drawer.closeDrawer(GravityCompat.START)

        if (ganaMasNativo && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(ganaMasSuscrita)
                isToolbarText(resources.getString(R.string.home_card_offer_sub_title_gana_mas), ICON_GANAMAS)
            else
                isToolbarText(resources.getString(R.string.home_card_offer_sub_title_offers_digitales), ICON_GANAMAS)

            menuTop.selectCurrentItem(MenuCodeTop.OFFERS)
            val fragment = GanaMasFragment()

            if (palanca != GlobalConstant.PALANCADEFAULT) {
                val bundle = Bundle()
                bundle.putString(palanca, palanca)
                fragment.arguments = bundle
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fltContainer, fragment, MenuCode.OFFERS)
                .addToBackStack(MenuCode.OFFERS).commit()

            trackEvent(menuType, GlobalConstant.EVENT_LABEL_OFFERS)

        } else {

            trackEvent(menuType, GlobalConstant.EVENT_LABEL_OFFERS)

            if (!NetworkUtil.isThereInternetConnection(context())) {
                val baseHomeFragment = visibleFragment as? BaseHomeFragment
                baseHomeFragment?.showNetworkError()
                return
            }

            val intent = Intent(this, OffersActivity::class.java)
            val section = getSectionToRedirectHibrid(palanca)
            intent.putExtra(OffersActivity.OPTION, section)
            startActivity(intent)
        }
    }

    override fun goToOffersSubscription(page: String?) {
        if (!NetworkUtil.isThereInternetConnection(context())) {
            val baseHomeFragment = visibleFragment as? BaseHomeFragment
            baseHomeFragment?.showNetworkError()
            return
        }

        val intent = Intent(this, OffersActivity::class.java)
        intent.putExtra(OffersActivity.OPTION, page)
        startActivity(intent)
    }

    override fun goToFest(extras: Bundle) {
        this.navigator.navigateToFest(this, null)
    }


    override fun showAnniversary() {
        showAnniversary(consultantName)
    }

    override fun showBirthday() {
        showBirthday(consultantName)
    }

    override fun showChristmas() {

        rltHome.postDelayed({
            try {
                FullScreenDialog.Builder(this@HomeActivity)
                    .withTitle(resources.getString(R.string.home_christmas_title))
                    .withMessage(resources.getString(R.string.home_christmas_body))
                    .withIcon(R.drawable.ic_christmas_tree)
                    .withScreenDismiss(true)
                    .withAnimation(resources,
                        FullScreenDialog.CUSTOM_ANIMATION,
                        R.drawable.ic_christmas_star_gold,
                        R.drawable.ic_christmas_star_red,
                        R.drawable.ic_christmas_star_green)
                    .show()

                val fragment = visibleFragment
                if (fragment is HomeFragment) {
                    fragment.updateChristmas()
                }
            } catch (e: Exception) {
                BelcorpLogger.w("showFullScreenError", e)
            }
        }, 300)
    }

    override fun showNewYear() {

        rltHome.postDelayed({
            try {
                FullScreenDialog.Builder(this@HomeActivity)
                    .withTitle(resources.getString(R.string.home_new_year_title))
                    .withMessage(resources.getString(R.string.home_new_year_body))
                    .withIcon(R.drawable.ic_christmas_champagne)
                    .withScreenDismiss(true)
                    .withAnimation(resources,
                        FullScreenDialog.SIMPLE_ANIMATION,
                        ContextCompat.getColor(this, R.color.dorado),
                        ContextCompat.getColor(this, R.color.dorado_transparente))
                    .show()

                val fragment = visibleFragment
                if (fragment is HomeFragment) {
                    fragment.updateNewYear()
                }
            } catch (e: Exception) {
                BelcorpLogger.w("showFullScreenError", e)
            }
        }, 300)

    }

    override fun showConsultantDay() {
        showConsultantDay(consultantName)
    }

    override fun showPasoSextoPedido() {
        startHolidayAnimation(consultantName, HolydayType.SIXTH)
    }

    override fun showBelcorpFifty() {
        startHolidayAnimation(consultantName, HolydayType.BELCORP_FIFTY)
    }

    override fun showPostulant() {
        showPostulant(consultantName)
    }

    override fun showHoliday(newConsultora: Boolean, userType: Int, birthday: Boolean,
                             anniversary: Boolean, consultantName: String, consultantCode: String,
                             sixth: Boolean, imgUrl: String, isCupon: Boolean, isDatamiMessage: Boolean) {
        this.consultantName = consultantName
        this.isCupon = isCupon
        this.isDatamiMessage = isDatamiMessage

        checkCalendar(newConsultora, userType, birthday, anniversary, consultantName, sixth)

        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(this).load(imgUrl).apply(RequestOptions.noTransformation()
                .placeholder(R.drawable.ic_contact_default)
                .error(R.drawable.ic_contact_default)
                .priority(Priority.HIGH)).into(ivwImageMain)
        } else {
            ivwImageMain.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_contact_default))
        }

        if (userType == UserType.CONSULTORA && !isCupon && !isDatamiMessage
            && NetworkUtil.isThereInternetConnection(this)) {
            SyncUtil.triggerRefresh()
        }
    }

    private fun checkCalendar(newConsultora: Boolean, userType: Int, birthday: Boolean,
                              anniversary: Boolean, consultantName: String, sixth: Boolean) {
        val today = Calendar.getInstance()
        val fragment = visibleFragment

        val christmasDays = ArrayList<Int>()
        for (i in 23..30) {
            christmasDays.add(i)
        }

        val newYearDays = ArrayList<Int>()
        for (i in 1..7) {
            newYearDays.add(i)
        }

        if (fragment is HomeFragment) {

            if (userType == UserType.POSTULANTE) {

                fragment.setHolidayText(consultantName, HolydayType.POSTULANT, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.POSTULANT)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkPostulant()

            } else if (newConsultora) {

                fragment.setHolidayText(consultantName, HolydayType.NONE, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.NONE)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkNewConsultant()

            } else if (birthday) {

                fragment.setHolidayText(consultantName, HolydayType.BIRTHDAY, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.BIRTHDAY)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkBirthday()

            } else if (today.get(Calendar.MONTH) == Calendar.DECEMBER && christmasDays.contains(today.get(Calendar.DAY_OF_MONTH))) {

                fragment.setHolidayText(consultantName, HolydayType.CHRISTMAS, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.CHRISTMAS)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkChristmas()

            } else if (today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DAY_OF_MONTH) == 31 || today.get(Calendar.MONTH) == Calendar.JANUARY && newYearDays.contains(today.get(Calendar.DAY_OF_MONTH))) {

                fragment.setHolidayText(consultantName, HolydayType.NEW_YEAR, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.NEW_YEAR)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkNewYear()

            } else if (today.get(Calendar.MONTH) == Calendar.JULY && today.get(Calendar.DAY_OF_MONTH) == 7) {

                fragment.setHolidayText(consultantName, HolydayType.CONSULTANT_DAY, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.CONSULTANT_DAY)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkConsultantDay()

            } else if (anniversary) {

                fragment.setHolidayText(consultantName, HolydayType.ANNIVERSARY, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.ANNIVERSARY)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkAnniversary()

            } else if (sixth) {

                fragment.setHolidayText(consultantName, HolydayType.NONE, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.NONE)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkPasoSextoPedido()

            } else if (belcorpExperience) {

                fragment.setHolidayText(consultantName, HolydayType.BELCORP_FIFTY, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.BELCORP_FIFTY)
                if (!hasCupon && !isCupon && !hasDatamiMessage && !isDatamiMessage)
                    fragment.checkBelcorpFifty()

            } else {

                fragment.setHolidayText(consultantName, HolydayType.NONE, belcorpExperience, christmasExperience)
                setNombreConsultura(consultantName, HolydayType.NONE)

            }

        } else {
            setNavHeaderData(birthday, anniversary, consultantName, sixth, christmasDays, newYearDays)
        }

    }

    private fun setNavHeaderData(birthday: Boolean, anniversary: Boolean, consultantName: String,
                                 sixth: Boolean, christmasDays: List<Int>, newYearDays: List<Int>) {
        val today = Calendar.getInstance()

        if (birthday) {
            setNombreConsultura(consultantName, HolydayType.BIRTHDAY)
        } else if (today.get(Calendar.MONTH) == Calendar.DECEMBER && christmasDays.contains(today.get(Calendar.DAY_OF_MONTH))) {
            setNombreConsultura(consultantName, HolydayType.CHRISTMAS)
        } else if (today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DAY_OF_MONTH) == 31 || today.get(Calendar.MONTH) == Calendar.JANUARY && newYearDays.contains(today.get(Calendar.DAY_OF_MONTH))) {
            setNombreConsultura(consultantName, HolydayType.NEW_YEAR)
        } else if (today.get(Calendar.MONTH) == Calendar.JULY && today.get(Calendar.DAY_OF_MONTH) == 7) {
            setNombreConsultura(consultantName, HolydayType.CONSULTANT_DAY)
        } else if (anniversary) {
            setNombreConsultura(consultantName, HolydayType.ANNIVERSARY)
        } else if (sixth) {
            setNombreConsultura(consultantName, HolydayType.NONE)
        } else if (belcorpExperience) {
            setNombreConsultura(consultantName, HolydayType.BELCORP_FIFTY)
        } else {
            setNombreConsultura(consultantName, HolydayType.NONE)
        }

    }

    private fun showPostulant(consultantName: String?) {
        startHolidayAnimation(consultantName, HolydayType.POSTULANT)
    }

    private fun showBirthday(consultantName: String?) {
        startHolidayAnimation(consultantName, HolydayType.BIRTHDAY)
    }

    private fun showConsultantDay(consultantName: String?) {
        startHolidayAnimation(consultantName, HolydayType.CONSULTANT_DAY)
    }

    private fun showAnniversary(consultantName: String?) {
        startHolidayAnimation(consultantName, HolydayType.ANNIVERSARY)
    }

    fun startHolidayAnimation(consultantName: String?, type: Int) {
        backlocked = true
        holidayAnimation = true

        fltHoliday.setOnClickListener { v ->
            fltHoliday.visibility = View.GONE

            Handler().postDelayed({
                holidayAnimation = false
                backlocked = false
            }, 500)
        }

        fltHoliday.postDelayed({

            if (HolydayType.BELCORP_FIFTY == type) {
                FestivityAnimationUtil.getCommonConfetti(
                    ContextCompat.getColor(this@HomeActivity, R.color.home_belcorp_fifty_color),
                    ContextCompat.getColor(this@HomeActivity, R.color.home_belcorp_fifty_color_second),
                    resources, rltHome)
            } else {
                FestivityAnimationUtil.getCommonConfetti(
                    ContextCompat.getColor(this@HomeActivity, R.color.dorado),
                    ContextCompat.getColor(this@HomeActivity, R.color.primary),
                    resources, rltHome)
            }

            val holiday = Holiday(context())
            fltHoliday.visibility = View.VISIBLE
            fltHoliday.alpha = 0f
            fltHoliday.animate().alpha(1.0f).setDuration(1000).start()

            holiday.initLayoutParams()
            fltHoliday.addView(holiday)

            holiday.postInit(consultantName, type, countryISO)

            Handler().postDelayed({ holiday.start() }, 800)

            val fragment = visibleFragment
            if (fragment is HomeFragment) {
                when (type) {
                    HolydayType.BIRTHDAY -> fragment.updateBirthday()
                    HolydayType.ANNIVERSARY -> fragment.updateAnniversary()
                    HolydayType.CONSULTANT_DAY -> fragment.updateConsultantDay()
                    HolydayType.SIXTH -> fragment.updatePasoSextoPedido()
                    HolydayType.POSTULANT -> fragment.updatePostulant()
                    HolydayType.BELCORP_FIFTY -> fragment.updateBelcorpFifty()
                    else -> {
                    }
                }
            }

            Handler().postDelayed({
                fltHoliday.animate().alpha(0.0f).setDuration(1000)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            holiday.visibility = View.GONE
                            fltHoliday.visibility = View.GONE

                            fltHoliday.clearAnimation()
                            fltHoliday.clearDisappearingChildren()

                        }
                    }).start()

                Handler().postDelayed({
                    holidayAnimation = false
                    backlocked = false
                }, 2000)
            }, 7000)


        }, 300)
    }

    /** */

    private fun goToDashboard(menuType: Int) {

        isToolbarText(false, ICON_DEFAULT)
        trackEvent(menuType, GlobalConstant.EVENT_LABEL_MENU_HOME)

        ivwBack.visibility = View.GONE
        llContainerForAbTesting.visibility =  View.GONE
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        drawer.closeDrawer(GravityCompat.START)
    }

    fun setNombreConsultura(consultantName: String, holiday: Int) {
        vieOrderNotification.reload()
        viePostulantNotification.reload()

        if (belcorpExperience) {
            if (!tvwNavbarTitle.isShown) {
                tvwNavbarTitle.visibility = View.VISIBLE
            }

            tvwNavbarTitle.text = String.format(getString(R.string.home_belcorp_fifty_message), consultantName)
        } else {
            tvwNavbarTitle.text = when (holiday) {
                HolydayType.BIRTHDAY -> getString(R.string.home_birthday_message).format(consultantName)
                HolydayType.ANNIVERSARY -> getString(R.string.home_anniversary_message).format(consultantName)
                HolydayType.CHRISTMAS -> getString(R.string.home_christmas_message).format(consultantName)
                HolydayType.NEW_YEAR -> getString(R.string.home_new_year_message).format(consultantName)
                HolydayType.CONSULTANT_DAY -> getString(R.string.home_consultant_day_message).format(consultantName)
                HolydayType.NONE -> getString(R.string.home_welcome_message).format(consultantName)
                else -> getString(R.string.home_welcome_message).format(consultantName)
            }
        }
    }

    private fun redireccionarDesdeStorie(menuCodigo: String, donde: String?, bund: Bundle? = null) {

        if (donde != null) {
            ivwBack.visibility = View.VISIBLE
            when (donde) {
                RedirectionStories.BONIFICACION -> {
                    menuTop.selectCurrentItem(MenuCodeTop.INCENTIVES)
                    goToIncentives(0, 1)
                }
                RedirectionStories.CLIENTES -> {
                    menuTop.selectCurrentItem(MenuCodeTop.CLIENTS)
                    goToMyClients(0, 1)
                }
                RedirectionStories.PASE_PEDIDO -> {
                    //debo saber que tipo es: nativo o hibrido
                    ivwBack.visibility = View.GONE
                    if (menuCodigo == MenuCodeTop.ORDERS_NATIVE) {
                        goToOrdersNative(1)
                    } else {
                        goToNewOrder(0, 1)
                    }
                }
                RedirectionStories.MI_ACADEMIA -> {
                    ivwBack.visibility = View.GONE
                    goToAcademia(0)
                }
                RedirectionStories.TVO -> {
                    ivwBack.visibility = View.GONE
                    goToTuVozOnline(1)
                }
                RedirectionStories.CHAT -> {
                    ivwBack.visibility = View.GONE
                    btnGoToChatBot()
                }
                RedirectionStories.CONFERENCIA_DIGITAL -> {
                    ivwBack.visibility = View.GONE
                    goToWinonclick()
                }
                RedirectionStories.FESTIVALES ->{
                    ivwBack.visibility = View.GONE
                    goToFest(Bundle())
                }
                else -> {
                }
            }

            if (donde.contains(RedirectionStories.GANA_MAS)) {
                goToOffers(0, donde)
            }

            bund?.remove(donde) // Para que no quede en un ciclo sin fin
            doRefreshData()
        }

    }

    override fun setCountryISO(countryISO: String) {
        this.countryISO = countryISO
        menuTop.setCountryISO(countryISO)
        checkFanPage(countryISO)
    }

    override fun setUserType(userType: Int) {
        this.userType = userType
    }

    override fun showCupon() {
        this.hasCupon = true
        navigator.navigateToCupon(this)
    }

    override fun setRevistaDigitalSuscripcion(tieneGND: Boolean, revistaDigitalSuscripcion: Int, ganaMasNativo: Boolean, ganaMasSuscrita: Boolean) {
        this.tieneGND = tieneGND
        this.revistaDigitalSuscripcion = revistaDigitalSuscripcion
        this.ganaMasNativo = ganaMasNativo
        this.ganaMasSuscrita = ganaMasSuscrita
    }

    override fun updateCurrentMenuItem(menuCode: String) {
        menuActual = menuCode
    }

    override fun updateCurrentMenuTopItem(@MenuCodeTop menuCode: String) {
        menuTop.selectCurrentItem(menuCode)
    }

    override fun reloadMenu() {
        menuLateral.reload()
        menuTop.reload()
    }

    override fun showDatamiMessage() {
        if (ConsultorasApp.getInstance().datamiType == NetworkEventType.DATAMI_AVAILABLE && !hasDatamiMessage && !hasCupon) {
            navigator.navigateToDatamiMessage(this, null)
            this.hasDatamiMessage = true
        }
    }

    override fun btnGoToChatBot() {
        if (!FacebookUtil.isFacebookMessengerInstalled(this)) {
            FacebookUtil.openFacebookMessengerPlayStore(this)
            return
        }
        if (NetworkUtil.isThereInternetConnection(context())) {
            trackEvent(1, GlobalConstant.EVENT_LABEL_CHATBOT, GlobalConstant.EVENT_NAME_SECCION_CHAT,
                GlobalConstant.EVENT_ACTION_CHATBOT)
            (visibleFragment as? BaseHomeFragment)?.presenterHome?.goChatBot()
        } else {
            try {
                val fragment = visibleFragment as? BaseFragment
                fragment?.showNetworkError()
            } catch (e: Exception) {
                BelcorpLogger.d("Error", "Problema al mostrar mensaje de error", e)
            }

        }
    }

    override fun goSubcampaign(title: String) {
        if (!NetworkUtil.isThereInternetConnection(context())) {
            (visibleFragment as? BaseHomeFragment)?.showNetworkError()
            return
        }
        val bundle = Bundle()
        bundle.putString(FestSubCampaingActivity.TITLE_KEY, title)
        navigator.navigateToSubcampaign(this, bundle)
    }

    override fun goToProductLaunch(action: String) {
        if (!NetworkUtil.isThereInternetConnection(context())) {
            (visibleFragment as? BaseHomeFragment)?.showNetworkError()
            return
        }
        goToOffers(3, action)
    }

    override fun redirectToOptionMenu(action: String, menu: String) {
        if (!NetworkUtil.isThereInternetConnection(context())) {
            (visibleFragment as? BaseHomeFragment)?.showNetworkError()
            return
        }
        redireccionarDesdeStorie(menu, action)
    }

    override fun goToCatalog() {
        goToCatalog("Catálogo digital")
    }

    override fun goToWinonclick() {
        winOnClickModel?.let { model ->
            val intent = Intent(this, WinOnClickActivity::class.java)
            intent.putExtra(GlobalConstant.WINONCLICK_VIDEO_URL, model.videoUrl)
            startActivity(intent)
        }

    }

    override fun goToDreamonclick(){
        dreamMederModel?.let{ _ ->
            val intent = Intent(this, DreamMeterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun goToReferedOption(option: String) {
        when (option) {
            MenuCodeTop.OFFERS -> goToOffers(3, OfferTypes.PALANCA_DEFAULT)
            MenuCodeTop.CLIENTS -> {
            }
        }
    }

    override fun checkForGanaMasRequest() {
        if (goToGanaMas) {
            goToGanaMas = false
            goToOffers(3, OfferTypes.PALANCA_DEFAULT)
        }
    }

    override fun setWinOnClickModel(winOnClick: WinOnClickModel) {
        winOnClickModel = winOnClick
    }

    override fun setDreamMeterOnModel(dreamMederOnClick : DreamMederModel) {
        dreamMederModel = dreamMederOnClick
    }


    override fun setBrandConfigModel(brandConfig: BrandConfigModel) {
        brandConfigModel = brandConfig

        Glide.with(this).asBitmap().load(brandConfig.imageHomeHeader).apply(RequestOptions.noTransformation().diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH)).listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    SessionManager.getInstance(this@HomeActivity).clearHomeBannerImage()
                    toolbar.context.setTheme(R.style.ToolbarTheme)
                    return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    toolbar.background = BitmapDrawable(resources, resource)
                    SessionManager.getInstance(this@HomeActivity).saveHomeBannerImage(resource)
                    return false
            }

        }).submit()

    }

    override fun clearBrandConfig() {
        brandConfigModel = null
        SessionManager.getInstance(this@HomeActivity).clearHomeBannerImage()
        toolbar.background = null
        toolbar.context.setTheme(R.style.ToolbarTheme)
    }

    override fun redirigirDesdeStorie(menuCodigo: String, donde: String?, bundle: Bundle) {
        redireccionarDesdeStorie(menuCodigo, donde, bundle)
    }

    /** */

    override fun onLogin() {
        hideLoading()
        goToLogin()
    }

    override fun onLoginFacebook() {
        hideLoading()
        goToLoginFacebook()
    }

    /** */

    override fun editar(clientemodel: ClienteModel) {
        val intent = Intent(this, ClientEditActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(GlobalConstant.CLIENTE_ID, clientemodel.id)
        startActivityForResult(intent, REQUEST_CODE_EDIT)
    }

    override fun refreshClientsList() {

        (visibleFragment as? ClientsListFragment)?.refreshLists()
    }

    override fun onAddDebt() {
        this.navigator.navigateToAddDebt(this)
    }

    fun unlockBackButtons() {
        Handler().postDelayed({
            if (!holidayAnimation) backlocked = false
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }, 100)
    }

    fun lockBackButtons() {
        backlocked = true
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * GanaMas listener
     */

    override fun goToFicha(extras: Bundle) {
        this.navigator.navigateToFicha(this, extras)
    }

    override fun goToArmaTuPack() {
        this.navigator.navigateToArmaTuPack(this, null)
    }

    fun isToolbarText(isText: Boolean) {
        val ivwToolbarBrand = findViewById<ImageView>(R.id.ivwToolbarBrand)
        ivwToolbarBrand.visibility = View.GONE
        fltMenu.visibility = View.GONE
        tvw_toolbar_title.visibility = View.GONE
        if (isText) {
            ivwBack.visibility = View.VISIBLE
            tvw_toolbar_title.visibility = View.VISIBLE
        } else {
            ivwToolbarBrand.visibility = View.VISIBLE
            fltMenu.visibility = View.VISIBLE
        }
    }

    fun isToolbarText(option: Int) {
        rlayCart.visibility = View.GONE
        rlayCampana.visibility = View.GONE
        ivwSearch.visibility = View.GONE
        when (option) {
            0 -> {
                rlayCampana.visibility = View.VISIBLE
            }
            1 -> {
                rlayCart.visibility = View.VISIBLE

                if (isSearchEnabled)
                    ivwSearch.visibility = View.VISIBLE
            }
            2 -> {

                if (isSearchEnabled)
                    llContainerForAbTesting.visibility = View.VISIBLE

            }
        }
    }

    fun showToolbarAbTesting(abTesting: Boolean) {

        if (abTesting) {
            llContainerForAbTesting.visibility = View.VISIBLE
            rlContainerToobar.visibility = View.GONE
        } else {
            rlContainerToobar.visibility = View.VISIBLE
            llContainerForAbTesting.visibility = View.GONE
        }

    }

    override fun updateOffersCountFromGanaMas(count: Int) {
        ordersCount += count
        tviCart.text = formatCount(ordersCount)
        SessionManager.getInstance(this).saveOffersCount(ordersCount)
        tvIndexCartAbTesting.text = formatCount(ordersCount)
    }

    override fun updateOffersCount(count: Int?) {
        count?.let {
            ordersCount = it
        } ?: run {
            ordersCount = 0
        }

        tviCart.text = formatCount(ordersCount)
        SessionManager.getInstance(this).saveOffersCount(ordersCount)
        tvIndexCartAbTesting.text = formatCount(ordersCount)
    }

    override fun setMenuModel(menu: MenuModel) {
        menuModel = menu
    }

    private fun formatCount(count: Int): String {
        return if (count <= 99)
            count.toString()
        else
            "99+"
    }

    fun setTutorialContent(contenido : ArrayList<ContenidoResumenDetalleModel>){
        this.contenidoTutorial = contenido
    }

    override fun isToolbarText(title: String, option: Int) {
        isToolbarText(true)
        isToolbarText(option)
        tvw_toolbar_title.text = title
    }

    fun isToolbarText(isText: Boolean, option: Int) {
        isToolbarText(isText)
        isToolbarText(option)
    }

    fun setToolbarForAbTesting(isText: Boolean, option: Int){
        isToolbarText(isText)
        isToolbarText(option)
        fltMenu.visibility = View.GONE
        llContainerForAbTesting.visibility = View.VISIBLE
        tvMostrarOfertas.setTypeface(null, Typeface.BOLD)
    }

    override fun showOrder() {
        listDialogOrder.show(supportFragmentManager, "listorder_fragment")
    }

    override fun dismissOrder() {
        listDialogOrder.dismiss()
    }

    override fun setSelectedItemOrder(index: Int) {
        listDialogOrder.setSelected(index)
    }

    override fun setOrder(itemListener: ListDialog.ListItemDialogListener, arrayDialog: ArrayList<ListDialogModel>) {
        listDialogOrder.setAdapter(ListDialogAdapter(this@HomeActivity, arrayDialog))
        listDialogOrder.setListener(itemListener)
    }

    fun goToStoriesRoot(history: StorieModel) {
        val intent = Intent(this, StorieActivity::class.java)
        intent.putExtra(GlobalConstant.STORIE_MODEL, history)
        startActivityForResult(intent, StorieActivity.ACTIVITYSTORIE)
    }

    override fun goToLeverPack(context: Context, typeLever: String, titleLever: String) {
        val bundle = Bundle()
        if (typeLever == OfferTypes.ATP) {
            bundle.putString(OffersActivity.OPTION, PageUrlType.ARMA_TU_PACK)
            bundle.putString(OffersActivity.SECTION, titleLever)
        } else {
            bundle.putString(OffersActivity.OPTION, PageUrlType.PERFECT_DUO)
            bundle.putString(OffersActivity.SECTION, titleLever)
        }
        navigator.navigateToOffers(context, bundle)
    }

    override fun onBackFromFragmentGanaMas() {
        backlocked = false
        onBackPressed()
    }

    override fun getFlagExpandedSearchview(flagExpandedSearchView: Boolean) {
        if (flagExpandedSearchView)
            runOnUiThread { setToolbarForAbTesting(false, ICON_GANAMAS_EXPANDED_SEARCHVIEW) }
    }

    override fun showMultiOrderType(){
        ConsultorasApp.getInstance().datamiType = NetworkEventType.MULTI_ORDER_AVAILABLE
        EventBus.getDefault().post(NetworkEvent(NetworkEventType.MULTI_ORDER_AVAILABLE))
    }

    override fun hideMultiOrderType() {
        ConsultorasApp.getInstance().datamiType = NetworkEventType.CONNECTION_AVAILABLE
        EventBus.getDefault().post(NetworkEvent(NetworkEventType.CONNECTION_AVAILABLE))
    }

    override fun setFlagMoverBarraNavegacion(flag: Boolean) {
        when (flag) {
            true -> {

                val paramsMenuTop = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                paramsMenuTop.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

                val paramsContainer = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                paramsContainer.addRule(RelativeLayout.ABOVE, R.id.menuTop)

                menuTop?.setLayoutParams(paramsMenuTop)
                rlt_main?.setLayoutParams(paramsContainer)
            }
        }

    }

    override fun goToEmbebedSuscription(page: String) {
        val intent = Intent(this, OffersActivity::class.java)
        intent.putExtra(OffersActivity.OPTION, page)
        startActivityForResult(intent, REQUEST_CODE_REVISTA_DIGITAL)
    }

    fun getSectionToRedirectHibrid(action: String): String {
        return when(action) {
            AdRedirectCode.OFFERS -> PageUrlType.OFFERS

            AdRedirectCode.OFFERS_FOR_YOU -> PageUrlType.OFERTAS_PARA_TI

            AdRedirectCode.OFFERS_ONLY_TODAY -> PageUrlType.SOLO_HOY

            AdRedirectCode.OFFERS_SALES_TOOLS -> PageUrlType.HERRAMIENTAS_DE_VENTA

            AdRedirectCode.OFFERS_INFORMATION -> PageUrlType.REVISTA_DIGITAL_INFO

            AdRedirectCode.GANA_MAS_LAN -> PageUrlType.LO_NUEVO_NUEVO

            AdRedirectCode.GANA_MAS_SR -> PageUrlType.SHOW_ROOM

            else -> PageUrlType.OFFERS
        }

    }

    companion object {

        const val PEDIDOS_PENDIENTES_TAG = "ValidacionPedidosPendientes"
        private val TAG = "HomeActivity"
        val MENU_OPTION = "MenuOption"

        private val REQUEST_CODE_EDIT = 150
        private val REQUEST_CODE_PROFILE = 170
        val REQUEST_CODE_OFFERS = 190
        val EXTRA_KEY_LANDING = "EXTRA_KEY_LANDING"
        val REQUEST_CODE_REVISTA_DIGITAL = 180

        internal var ICON_DEFAULT = 0
        internal var ICON_GANAMAS = 1
        internal var ICON_GANAMAS_EXPANDED_SEARCHVIEW = 2;

        const val KIT_NUEVA_BR_KEY = "KIT_NUEVA_BR_KEY"

        var KIT_NUEVA_CUV: String? = null

        /** */

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}

