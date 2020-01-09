package biz.belcorp.consultoras.feature.home

import android.Manifest
import android.Manifest.permission.PACKAGE_USAGE_STATS
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.Settings
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.component.stories.bubble.StorieComponent
import biz.belcorp.consultoras.common.material.tap.MaterialTapTargetPrompt
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.banner.BannerModel
import biz.belcorp.consultoras.common.model.brand.BrandConfigModel
import biz.belcorp.consultoras.common.model.dreammeter.DreamMederModel
import biz.belcorp.consultoras.common.model.home.CardEntity
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel
import biz.belcorp.consultoras.common.model.menu.MenuModel
import biz.belcorp.consultoras.common.model.notification.NotificacionClienteModel
import biz.belcorp.consultoras.common.model.notification.NotificacionRecordatorioModel
import biz.belcorp.consultoras.common.model.stories.StorieModel
import biz.belcorp.consultoras.common.model.winonclick.WinOnClickModel
import biz.belcorp.consultoras.common.recordatory.BirthdayJobService
import biz.belcorp.consultoras.common.recordatory.RecordatoryJobService
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.Device
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.LoginDetail
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.client.registration.ClientRegistrationActivity
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import biz.belcorp.consultoras.feature.home.marquee.MarqueeAdapter
import biz.belcorp.consultoras.feature.home.marquee.MarqueeItem
import biz.belcorp.consultoras.feature.home.survey.SurveyBottomDialogFragment
import biz.belcorp.consultoras.feature.home.updatemail.UpdateMailDialog
import biz.belcorp.consultoras.util.*
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.library.annotation.DatetimeFormat
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.mobile.analytics.core.Analytics
import biz.belcorp.library.mobile.analytics.kinesis.KinesisSettings
import biz.belcorp.library.util.DateUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.Trigger
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

import android.Manifest.permission.PACKAGE_USAGE_STATS
import biz.belcorp.consultoras.common.dialog.VideoDialog
import biz.belcorp.consultoras.common.dialog.*
import biz.belcorp.consultoras.domain.entity.CatalogoWrapper
import biz.belcorp.consultoras.common.dialog.*
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_home.*

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/** */

@RuntimePermissions
class HomeFragment : BaseHomeFragment(), HomeView, StorieComponent.OnClickBubbleStorie, SafeLet {
    @Inject
    internal lateinit var presenter: HomePresenter

    internal var counterExecution = 0

    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    private var animFadeInHome: Animation? = null

    private var listener: HomeFragmentListener? = null
    private var moneySymbol: String? = null
    private var decimalFormatISO: DecimalFormat? = null

    private var loginModel: LoginModel? = null
    private var kinesisManager: KinesisManager? = null

    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    private var trackState = true
    private var option: String? = ""

    private var offersPage: String? = null
    private var revistaDigitalSuscripcion: Int = 0
    private var counterStorie = 0
    private var isPromptOpen: Boolean = false

    private var imageHelper: ImagesHelper? = null
    private var menuModel: MenuModel? = null


    private var firstImgLoaded = false
    private var secondImgLoaded = false

    private var titleSubcampaign = StringUtil.Empty

    private val requestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            ivwShowRoom?.visibility = View.GONE
            return false
        }

        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
            return false
        }
    }


    private val marqueeOnClickListener = object : MarqueeAdapter.OnClickListener {
        override fun onItemClick(position: Int, item: MarqueeItem?) {
            Tracker.Home.trackClickBannerLanzamiento(item, position)
            presenter?.showBannerAction(position)

        }
    }

    /** */

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(HomeComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)

        arguments?.let { bundle ->
            trackState = bundle.getBoolean(GlobalConstant.LOGIN_STATE, false)
            option = bundle.getString(HomeActivity.MENU_OPTION)
            bundle.remove(GlobalConstant.LOGIN_STATE)
            bundle.remove(HomeActivity.MENU_OPTION)

            gestionarRedireccionamiento()
            onSaveInstanceState(bundle)
        }

        presenter.clearIsShowMessagesGiftBar()
        presenter.dataOffline()
        presenter.getResumenContenidoCoroutine(true)
        presenter.loadCaminoBrillante()
        presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
        initHandler()
        setHasOptionsMenu(true)
    }


    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is HomeFragmentListener) {
            listener = context
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val token = FirebaseInstanceId.getInstance().token
        BelcorpLogger.d("TOKEN FIREBASE: $token")

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        init()
        UXCam.tagScreenName(UXCamUtils.HomeFragmentName)
    }

    override fun onResume() {
        super.onResume()
        if (refreshDataHandler == null) {
            initHandler()
        }
        if (NetworkUtil.isThereInternetConnection(context)) {
            presenter.checkSchedule()
        }

        refreshDataHandler?.removeCallbacks(refreshDataRunnable)
        refreshDataHandler?.postDelayed(refreshDataRunnable, 200)

        if (FirebaseRemoteConfig.getInstance().getBoolean(BuildConfig.REMOTE_CONFIG_SHOW_RATE_PLAY_STORE)) {
            presenter.evaluateShowRateDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshDataHandler?.removeCallbacks(refreshDataRunnable)
        presenter.destroy()
    }

    /** */

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_HOME, model)
    }

    private fun init() {
        storieBubble.listener = this
        animFadeInHome = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        context?.let { imageHelper = ImagesHelper(it) }
        hciWinclick.showWinClick()
        hciMakeup.showBeautyAdviser()

        hciDreams.setOnClickListener(onDreamClick)
        editarPerfil.setOnClickListener(onEditarPerfilClick)
        hciIncentives.setOnClickListener(onIncentivesClick)
        hciOrders.setOnClickListener(onOrdersClick)
        hciClients.setOnClickListener(onClientsClick)
        hciDebts.setOnClickListener(onDebtsClick)
        hciCatalog.setOnClickListener(onCatalogClick)
        hciMakeup.setOnClickListener(onMakeupClick)
        hciWinclick.setOnClickListener(onWinClickClick)
        ivwShowRoom.setOnClickListener(onBannerGanaMasClick)
        ivwHomeImage.setOnClickListener(profile)
        btnPayOnline.setOnClickListener(onPayClick)
        btnPayOnlineMultiOrder.setOnClickListener(onPayClick)
        btn_camino_brillante.setOnClickListener(onCaminoBrillanteClick)
        hciChat.setOnClickListener(onChatClick)
        hciSubcampaign.setOnClickListener(onSubcampaignClick)

        context?.let {
            presenter.refreshData()
            presenter.getMoverBarraNavegacion()
        }

    }

    private fun initHandler() {
        refreshDataHandler = Handler()
        refreshDataRunnable = Runnable {
            remoteConfig()
            presenter.getFestConfiguration()
            presenter.getTitleSubcampaign()

            presenter.initScreenTrack()

            if (trackState) {
                trackState = false
                return@Runnable
            }
            presenter.getFestConfiguration()
            context?.let { presenter.getCountMaxRecentSearch(it) }
        }
    }

    fun checkSearchPrompt() {
        presenter.checkSearchPrompt()
    }

    /** */
    private val onEditarPerfilClick = View.OnClickListener {
        Tracker.Notificaciones.iconMiPerfil()
        listener?.apply {
            if (!isBackLocked) {
                goToProfile()
            }
        }
    }

    private val onIncentivesClick = View.OnClickListener {
        Tracker.trackEvent(GlobalConstant.SCREEN_HOME, BuildConfig.FLAVOR + "|Home", "Ver sección", hciIncentives.fullDescription, loginModel)

        listener?.apply {
            if (!isBackLocked) {
                updateCurrentMenuTopItem(MenuCodeTop.INCENTIVES)
                updateCurrentMenuItem("MEN_INCENTIVOS")
                goToIncentives(1, -1)
            }
        }
    }

    private val onOrdersClick = View.OnClickListener {
        if (!NetworkUtil.isThereInternetConnection(context())) {
            showNetworkError()
            return@OnClickListener
        }

        menuModel?.let {
            listener?.apply {
                if (!isBackLocked) {
                    if (it.isVisible && it.codigo == MenuCodeTop.ORDERS) {
                        goToNewOrder(1, -1)
                    } else if (it.isVisible && it.codigo == MenuCodeTop.ORDERS_NATIVE) {
                        goToOrdersNative(-1)
                    }
                }
            }
        }
    }

    private val onClientsClick = View.OnClickListener {
        Tracker.trackEvent(GlobalConstant.SCREEN_HOME, BuildConfig.FLAVOR + "|Home", "Ver sección", hciClients.fullDescription, loginModel)

        listener?.apply {
            if (!isBackLocked) {
                goToOffers(0, OfferTypes.PALANCA_DEFAULT)
            }
        }

    }

    private val onDebtsClick = View.OnClickListener {
        listener?.apply {
            if (!isBackLocked) {
                updateCurrentMenuTopItem(MenuCodeTop.CLIENTS)
                updateCurrentMenuItem("MEN_MISCLIENTES")
                goToMyClients(1, -2)
            }
        }
    }

    private val onCatalogClick = View.OnClickListener {
        Tracker.trackEvent(GlobalConstant.SCREEN_HOME, BuildConfig.FLAVOR + "|Home", "Ver sección", hciCatalog.fullDescription, loginModel)

        if (!NetworkUtil.isThereInternetConnection(context())) {
            showNetworkError()
            return@OnClickListener
        }
        listener?.goToCatalog()
    }

    private val onMakeupClick = View.OnClickListener {
        Tracker.trackEvent(GlobalConstant.SCREEN_HOME, BuildConfig.FLAVOR + "|Home", "Ver sección", hciMakeup.fullDescription, loginModel)

        if (OtherAppUtil.isMaquilladorInstalled(activity as Context)) {
            val launchIntent = activity!!.packageManager.getLaunchIntentForPackage(OtherAppUtil.MQVIRTUAL_PACKAGE_NAME)
            if (launchIntent != null) startActivity(launchIntent)
        } else {
            OtherAppUtil.openMaquilladorPlayStore(activity as Context)
        }
    }

    private val onWinClickClick = View.OnClickListener {
        Tracker.trackEvent(GlobalConstant.SCREEN_HOME, BuildConfig.FLAVOR + "|Home", "Ver sección", hciWinclick.fullDescription, loginModel)

        if (!NetworkUtil.isThereInternetConnection(context)) {
            showNetworkError()
            return@OnClickListener
        }

        listener?.goToWinonclick()
    }

    private val onDreamClick = View.OnClickListener {
        if (!NetworkUtil.isThereInternetConnection(context)) {
            showNetworkError()
            return@OnClickListener
        }
        listener?.goToDreamonclick()
    }

    private val onBannerGanaMasClick = View.OnClickListener {
        listener?.apply {
            if (isBackLocked) {
                goToOffersSubscription(offersPage)

                var label = when (revistaDigitalSuscripcion) {
                    MagazineSuscriptionType.WITH_GND_SA -> GlobalConstant.EVENT_BANNER_GANA_MAS_SA
                    MagazineSuscriptionType.WITH_GND_SNA -> GlobalConstant.EVENT_BANNER_GANA_MAS_SNA
                    MagazineSuscriptionType.WITH_GND_NSA -> GlobalConstant.EVENT_BANNER_GANA_MAS_NSA
                    MagazineSuscriptionType.WITH_GND_NSNA -> GlobalConstant.EVENT_BANNER_GANA_MAS_NSNA
                    else -> ""
                }

                presenter.trackEvent(GlobalConstant.SCREEN_HOME,
                    GlobalConstant.EVENT_CAT_HOME_BANNER,
                    GlobalConstant.EVENT_ACTION_HOME_BANNER,
                    label,
                    GlobalConstant.EVENT_SHOW_SECTION, false)

            }
        }
    }

    private val profile = View.OnClickListener {
        listener?.apply {
            if (!isBackLocked) {
                goToProfile()
            }
        }
    }

    private val onPayClick = View.OnClickListener {
        listener?.apply {
            if (!isBackLocked) {
                Tracker.trackEvent(
                    GlobalConstant.SCREEN_HOME,
                    GlobalConstant.SCREEN_HOME,
                    GlobalConstant.EVENT_ACTION_CLICK_BOTON,
                    getString(R.string.paga_en_linea),
                    GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                    loginModel
                )
                presenter.gotToMethodPay()
            }
        }
    }

    private val onCaminoBrillanteClick = View.OnClickListener {
        val intent = Intent(context, CaminoBrillanteActivity::class.java)
        activity?.startActivityForResult(intent, HomeActivity.REQUEST_CODE_OFFERS)
        Tracker.CaminoBrillante.clicVerBeneficios(loginModel)

    }

    private val onChatClick = View.OnClickListener {
        Tracker.trackEvent(GlobalConstant.SCREEN_HOME, BuildConfig.FLAVOR + "|Home", "Ver sección", hciChat.fullDescription, loginModel)

        listener?.btnGoToChatBot()
    }

    private val onSubcampaignClick = View.OnClickListener {
        listener?.goSubcampaign(titleSubcampaign)
    }

    /** */

    override fun notifyRefreshDataError(exception: Throwable) {
        showNetworkError()
    }

    override fun setDecimalFormatISO(decimalFormatISO: DecimalFormat) {
        this.decimalFormatISO = decimalFormatISO
    }

    override fun setCountryMoneySymbol(countryMoneySymbol: String) {
        this.moneySymbol = countryMoneySymbol
    }

    override fun setUserType(userType: Int) {

        listener?.apply {
            setUserType(userType)
            arguments?.let { bundle ->
                bundle.getString(AdRedirectCode.ACTUALIZACION_DATO)?.let {
                    if (it.isNotEmpty()) {
                        goToProfile()
                        bundle.remove(AdRedirectCode.ACTUALIZACION_DATO) //lo elimino desde aqui ya que no lo controlo desde actualizar dato
                    }
                }
            }
        }
    }

    override fun setCountryISO(countryISO: String) {
        listener?.setCountryISO(countryISO)
    }

    override fun showCupon() {
        listener?.showCupon()
    }

    override fun setRevistaDigitalSuscripcion(userType: Int, tieneGND: Boolean,
                                              revistaDigitalSuscripcion: Int,
                                              urlBannerGanaMas: String, ganaMasNativo: Boolean, ganaMasSuscrita: Boolean) {
        this.revistaDigitalSuscripcion = revistaDigitalSuscripcion


        listener?.setRevistaDigitalSuscripcion(tieneGND, revistaDigitalSuscripcion, ganaMasNativo, ganaMasSuscrita)


        if (userType == UserType.CONSULTORA) {
            when (revistaDigitalSuscripcion) {
                MagazineSuscriptionType.WITH_GND_SA, MagazineSuscriptionType.WITH_GND_SNA -> {
                    offersPage = PageUrlType.OFFERS
                    ivwShowRoom.visibility = View.VISIBLE
                    showBannerGanaMas(urlBannerGanaMas)
                }
                MagazineSuscriptionType.WITH_GND_NSA, MagazineSuscriptionType.WITH_GND_NSNA -> {
                    offersPage = PageUrlType.REVISTA_DIGITAL_INFO
                    ivwShowRoom.visibility = View.VISIBLE
                    showBannerGanaMas(urlBannerGanaMas)
                }
                MagazineSuscriptionType.WITHOUT_GND -> {
                    offersPage = PageUrlType.OFFERS
                    ivwShowRoom.visibility = View.GONE
                }
                else -> {
                    offersPage = PageUrlType.OFFERS
                    ivwShowRoom.visibility = View.GONE
                }
            }
        } else {
            ivwShowRoom.visibility = View.GONE
        }

        arguments?.let { bundle ->
            val donde = StorieUtils.getRedirectionGanaMas(bundle)
            redirigir(MenuCode.OFFERS, donde, bundle)
            bundle.remove(donde)
        }

    }

    private fun showBannerGanaMas(urlBannerGanaMas: String?) {
        if (urlBannerGanaMas != null && !urlBannerGanaMas.isEmpty()) {
            Glide.with(this).asDrawable().load(urlBannerGanaMas)
                .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                .listener(requestListener)
                .into(ivwShowRoom)
        } else {
            ivwShowRoom.visibility = View.GONE
        }
    }

    override fun setCampaign(campaign: String, endDay: String, endHour: String, days: Int,
                             pagoEnLinea: Boolean, fechaVencimientoPago: String,
                             loginDetail: LoginDetail, isMultiOrder: Boolean, lineaConsultora: String?) {

        var date: String? = ""
        val actualDate = Calendar.getInstance()
        val deudaCalender = Calendar.getInstance()
        val campaignLabel: String
        val deudaLabel: String
        val campaignInfo: String
        var isFechaVencimientoValida = false

        try {
            loginDetail.detailDescription?.let { detailDescription ->
                isFechaVencimientoValida = !detailDescription.contains("01/01/0001")
                date = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirDDMMAAAAtoDate(detailDescription), "dd MMM"))
                deudaCalender.time = DateUtil.convertirDDMMAAAAtoDate(detailDescription)
            } ?: run {
                isFechaVencimientoValida = false
            }

        } catch (e: ParseException) {
            BelcorpLogger.w(TAG, "setCampaign", e.message)
        }

        val dayState = days == 0
        val diff = deudaCalender.timeInMillis - actualDate.timeInMillis

        val deuda = loginDetail.value ?: 0.0
        if (pagoEnLinea && isFechaVencimientoValida && lineaConsultora.isNullOrEmpty()) {
            val vence = (if (diff > 0) getString(R.string.home_deuda_vence) else getString(R.string.home_deuda_vencio)) + " el "

            date?.apply {
                if (isEmpty()) {
                    date = "--/--"
                }
            } ?: run {
                date = "--/--"
            }

            tvwVence.text = "${getString(R.string.home_tu_deuda)} $vence $date"
            multiOrderDataView.visibility = View.GONE
            btnPayOnlineMultiOrder.visibility = View.GONE
            if (deuda > 0) {
                linearPago.visibility = View.VISIBLE
                tvwMntFinal.text = moneySymbol + " " + decimalFormatISO?.format(loginDetail.value)
                campaignLabel = getString(if (dayState) R.string.home_past_campaign_label_new else R.string.home_campaign_label_new).replace("\n", "")
                campaignInfo = String.format(campaignLabel, campaign, endHour, DateUtil.formatearMes(endDay), "")
            } else {
                linearPago.visibility = View.GONE
                campaignLabel = getString(if (dayState) R.string.home_past_campaign_label_new else R.string.home_campaign_label_new)
                deudaLabel = getString(R.string.home_deuda_now) + " " + moneySymbol + " " + decimalFormatISO?.format(loginDetail.value) +
                    if (loginDetail.value ?: 0.0 > 0) " - " + (if (diff > 0) getString(R.string.home_deuda_vence) else getString(R.string.home_deuda_vencio)) + " " + date else ""
                campaignInfo = String.format(campaignLabel, campaign, endHour, endDay, deudaLabel)
            }
        } else if (lineaConsultora != null && lineaConsultora.isNotEmpty()) {
            val vence = (if (diff > 0) getString(R.string.home_deuda_vence) else getString(R.string.home_deuda_vencio)) + " el "
            date?.apply {
                if (isEmpty()) {
                    date = "--/--"
                }
            } ?: run {
                date = "--/--"
            }

            campaignLabel = getString(if (dayState) R.string.home_past_campaign_label_new else R.string.home_campaign_label_new).replace("\n", "")
            campaignInfo = String.format(campaignLabel, campaign, endHour, DateUtil.formatearMes(endDay), "")

            lineaConsultoraView.visibility = View.VISIBLE

            try {
                tvwLineaConsultora.text = "$moneySymbol ${decimalFormatISO?.format(lineaConsultora.toDouble())}"
            } catch (ex: Exception) {
                BelcorpLogger.e(ex)
                tvwLineaConsultora.text = "--"
            }

            tvwDeudaMulti.text = "$moneySymbol ${decimalFormatISO?.format(loginDetail.value)}"
            linearPago.visibility = View.GONE
            multiOrderDataView.visibility = View.VISIBLE

            val venceLabel: String
            if (deuda > 0) {
                venceLabel = "${getString(R.string.home_tu_deuda_multi_order)}/${vence.capitalize()}$date"
                if (pagoEnLinea && isFechaVencimientoValida) {
                    btnPayOnlineMultiOrder.visibility = View.VISIBLE
                } else {
                    btnPayOnlineMultiOrder.visibility = View.GONE
                }
            } else {
                venceLabel = getString(R.string.home_tu_deuda_multi_order)
                btnPayOnlineMultiOrder.visibility = View.GONE
            }
            tvwVenceMultiOrder.text = venceLabel

        } else {
            linearPago.visibility = View.GONE
            multiOrderDataView.visibility = View.GONE
            btnPayOnlineMultiOrder.visibility = View.GONE
            campaignLabel = getString(if (dayState) R.string.home_past_campaign_label_new else R.string.home_campaign_label_new)
            deudaLabel = getString(R.string.home_deuda_now) + " " + moneySymbol + " " + decimalFormatISO?.format(loginDetail.value) +
                if (loginDetail.value ?: 0.0 > 0) " - " + (if (diff > 0) getString(R.string.home_deuda_vence) else getString(R.string.home_deuda_vencio)) + " " + date else ""
            campaignInfo = String.format(campaignLabel, campaign, endHour, endDay, deudaLabel)
        }

        tvwHomeCampaign.text = campaignInfo
    }

    override fun showIncentives(cardEntity: CardEntity, countryISO: String) {
        hciIncentives.showIncentives(cardEntity, countryISO)

        if (hciIncentives.parent != null) {
            gldMenu.removeView(hciIncentives)
        }
    }

    override fun showClients(cardEntity: CardEntity) {
        hciDebts.showClients()
    }

    override fun showOrders(cardEntity: CardEntity) {
        hciOrders.showOrders(cardEntity, moneySymbol, decimalFormatISO)
    }

    override fun showOrdersCount(count: Int?) {
        listener?.updateOffersCount(count)
    }

    override fun showOffers(cardEntity: CardEntity) {
        hciClients.showOffer(loginModel?.revistaDigitalSuscripcion ?: 0)
    }

    override fun showCatalog(cardEntity: CardEntity) {
        hciCatalog.showCatalog()
    }

    override fun showChat(cardEntity: CardEntity) {
        hciChat.showChat()
    }

    override fun setUserData(model: LoginModel) {
        this.loginModel = model
        if (loginModel?.userType == UserType.CONSULTORA) {
            getToken()
        }
        if (this.loginModel?.isMostrarBuscador == true) {
            (activity as? HomeActivity)?.showSearchOption()
        } else {
            (activity as? HomeActivity)?.hideSearchOption()
        }
    }

    private fun getToken() {
        presenter.getToken()
    }

    private fun remoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build()
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults)

        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        var cacheExpiration: Long = 3600

        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(activity as Activity) { task ->

            if (task.isSuccessful) {
                mFirebaseRemoteConfig.activateFetched()
            }

            presenter.saveUsabilityConfig(mFirebaseRemoteConfig.getString(BuildConfig.REMOTE_CONFIG_USABILITY))
            presenter.startSDK(mFirebaseRemoteConfig.getString(BuildConfig.REMOTE_CONFIG_ANALYTICS))

            context?.let {
                val sessionManager = SessionManager.getInstance(it)
                sessionManager.saveApiCacheEnabled(mFirebaseRemoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_API_CACHE_ENABLED))
                sessionManager.saveApiCacheOnlineTime(mFirebaseRemoteConfig.getLong(BuildConfig.REMOTE_CONFIG_API_CACHE_ONLINE_TIME))
                sessionManager.saveApiCacheOfflineTime(mFirebaseRemoteConfig.getLong(BuildConfig.REMOTE_CONFIG_API_CACHE_OFFLINE_TIME))
                sessionManager.saveApiCacheOfflineCaminoBrillanteTime(mFirebaseRemoteConfig.getLong(BuildConfig.REMOTE_CONFIG_API_CACHE_OFFLINE_CAMINO_BRILLANTE_TIME))
                sessionManager.saveImageDialogEnabled(mFirebaseRemoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_IMAGE_DIALOG_ENABLED))
                sessionManager.saveHideViewsGridGanaMas(mFirebaseRemoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_HIDE_VIEWS_GRID_GANA_MAS))
                sessionManager.saveExpandedSearchview(mFirebaseRemoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_EXPANDED_SEARCHVIEW))
                sessionManager.saveImagesMaxFicha(mFirebaseRemoteConfig.getLong(BuildConfig.REMOTE_CONFIG_IMAGES_MAX_FICHA))
                sessionManager.saveApiClientId(mFirebaseRemoteConfig.getString(BuildConfig.REMOTE_CONFIG_API_CLIENT_ID))
                sessionManager.saveApiClientSecret(mFirebaseRemoteConfig.getString(BuildConfig.REMOTE_CONFIG_API_CLIENT_SECRET))
                sessionManager.saveOrderConfigurableLever(mFirebaseRemoteConfig.getString(BuildConfig.REMOTE_CONFIG_ORDER_CONFIGURABLE_LEVER))
                sessionManager.saveDownloadPdfCatalog(mFirebaseRemoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_DOWNLOAD_CATALOG_PDF))
                sessionManager.saveMoverBarraNavegacion(mFirebaseRemoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_MOVER_BARRA_NAVEGACION))
                sessionManager.savePromotionGroupListEnabled(mFirebaseRemoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_PROMOTION_GROUP_LIST_ENABLE))
                sessionManager.saveABTestingBonificaciones(mFirebaseRemoteConfig.getString(BuildConfig.REMOTE_CONFIG_AB_TESTING_BONIFICACIONES))
            }

        }
    }

    override fun showSearchPrompt() {

        if (!isPromptOpen) {
            isPromptOpen = true
            MaterialTapTargetPrompt.Builder(activity)
                .setTarget(R.id.ivw_search)
                .setSecondaryText(resources.getString(R.string.home_search_prompt))
                .setSecondaryTextColour(ContextCompat.getColor(context as Context, R.color.white))
                .setAnimationInterpolator(FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setPrimaryTextColour(Color.WHITE)
                .setSecondaryTextColour(Color.WHITE)
                .setPrimaryTextSize(R.dimen.material_tap_text_primary_size)
                .setSecondaryTextSize(R.dimen.material_tap_text_secondary_size)
                .setBackgroundColour(ContextCompat.getColor(activity as Context, R.color.home_search_prompt))
                .setIconDrawableColourFilter(ContextCompat.getColor(activity as Context, R.color.home_search_prompt))
                .setIconDrawable(ContextCompat.getDrawable(context as Context, R.drawable.ic_home_search))
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        presenter.saveSearchPrompt()
                    }
                }
                .show()
        }

    }

    override fun openChatBot(token: String) {
        if (activity is HomeActivity) {
            (activity as HomeActivity).openChatBot(token)
        }
    }

    override fun showChatbot(chatBot: Boolean?) {
        if (chatBot != null) {


            if (chatBot)
                hciChat.visibility = View.VISIBLE
            else
                hciChat.visibility = View.GONE

            if (hciChat.parent != null && !chatBot) {
                gldMenu.removeView(hciChat)
            } else if (hciChat.parent == null) {
                gldMenu.addView(hciChat)
            }
        }
    }

    override fun reloadMenu() {
        listener?.reloadMenu()
        presenterHome.getFestConfiguration()
    }

    override fun checkToken(device: Device?) {
        var device = device
        val token = FirebaseInstanceId.getInstance().token

        if (device == null || null != device.tokenFCM && device.tokenFCM != token
            || null != device.usuario && device.usuario != loginModel?.userCode) {

            val uuid = DeviceUtil.getId(activity as Context)
            device = Device()
            device.appId = Constant.BRAND_APP_ID
            device.pais = loginModel?.countryISO
            device.rolId = GlobalConstant.APP_ROL_ID
            device.usuario = loginModel?.userCode
            device.uuid = uuid
            device.imei = uuid
            device.so = GlobalConstant.SO
            device.modelo = DeviceUtil.getModel()
            device.version = DeviceUtil.getVersionCode()
            device.tokenFCM = token
            device.topicFCM = ""
            presenter.saveToken(device)

        }
    }

    override fun showEmptyCards(countryISO: String?) {
        hciDebts.showEmpty(CardType.CLIENTS, countryISO)
        hciOrders.showEmpty(CardType.GANANCIAS, countryISO)
        hciClients.showEmpty(CardType.OFFERS, countryISO)
        hciIncentives.showEmpty(CardType.INCENTIVES, countryISO)
        hciCatalog.showCatalog()
        if (hciIncentives.parent != null) {
            gldMenu.removeView(hciIncentives)
        }
    }

    override fun setPhoto(urlImage: String?) {

        urlImage?.let {
            if (it.isNotEmpty()) {
                Glide.with(this).load(it).apply(RequestOptions.noTransformation()
                    .placeholder(R.drawable.ic_contact_default)
                    .error(R.drawable.ic_contact_default)
                    .priority(Priority.HIGH)).into(ivwHomeImage)
            }
        } ?: run {
            ivwHomeImage.setImageDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.ic_contact_default))
        }

    }

    override fun showAnniversary() {

        listener?.showAnniversary()

    }

    override fun showBirthday() {

        listener?.showBirthday()
    }

    override fun showChristmas() {

        listener?.showChristmas()

    }

    override fun showNewYear() {

        listener?.showNewYear()

    }

    override fun showConsultantDay() {
        listener?.showConsultantDay()

    }

    override fun showPasoSextoPedido() {

        listener?.showPasoSextoPedido()

    }

    override fun showBelcorpFifty() {

        listener?.showBelcorpFifty()

    }

    override fun showPostulant() {

        listener?.showPostulant()
    }

    override fun setLogAccess(kinesisModel: KinesisModel, login: Login) {
        try {
            if (kinesisManager == null) {
                kinesisManager = KinesisManager.create(activity as Context, GlobalConstant.SCREEN_LOG_HOME, kinesisModel)
            }
            kinesisManager?.save(login)
        } catch (ignored: Exception) {
            BelcorpLogger.d(ignored)
        }

    }

    override fun getNotificationesCliente() {
        presenter.getNotificationesCliente()
    }


    override fun generateNotificationesCliente(transform: List<NotificacionClienteModel>) {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        for (model in transform) {
            var date: Date? = null
            if (null != model && null != model.clienteModel
                && null != model.clienteModel.fechaNacimiento
                && !model.clienteModel.fechaNacimiento.isEmpty()) {

                val fechaNacimiento = model.clienteModel.fechaNacimiento
                try {
                    date = simpleDateFormat.parse(fechaNacimiento)
                } catch (e: ParseException) {
                    BelcorpLogger.w(TAG, "ParseException", e)
                }

                if (date != null) {
                    val now = Calendar.getInstance()
                    val calendarAlarm = Calendar.getInstance()
                    calendarAlarm.time = date
                    calendarAlarm.set(Calendar.YEAR, DateUtil.getCurrentYear())
                    calendarAlarm.set(Calendar.HOUR_OF_DAY, 8)
                    calendarAlarm.set(Calendar.MINUTE, 0)

                    val diff = calendarAlarm.timeInMillis - now.timeInMillis
                    val startSeconds = if ((diff / 1000).toInt() < 0) 0 else (diff / 1000).toInt()
                    val endSeconds = startSeconds + 10
                    val dayDiff = calculateDaysLeft(calendarAlarm)

                    if (dayDiff >= 0) {
                        var alias: String? = model.clienteModel.alias
                        if (alias == null || alias.isEmpty()) {
                            alias = model.clienteModel.nombres + if (TextUtils.isEmpty(model.clienteModel.apellidos)) "" else " " + model.clienteModel.apellidos
                        }

                        val bundle = Bundle()
                        bundle.putString(GlobalConstant.CLIENT_MESSAGE_RECORDATORY, String.format(getString(R.string.debt_birthday_alarm), alias))
                        bundle.putInt(GlobalConstant.CLIENTE_LOCAL_ID, model.clienteModel.id)

                        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(activity))
                        val builder = dispatcher.newJobBuilder()
                        builder.setService(BirthdayJobService::class.java)
                        builder.tag = getString(R.string.reminder_birthday_key) + model.clienteModel.clienteID
                        builder.isRecurring = false
                        builder.setReplaceCurrent(true)
                        builder.lifetime = Lifetime.FOREVER
                        builder.trigger = Trigger.executionWindow(startSeconds, endSeconds)
                        builder.extras = bundle
                        dispatcher.schedule(builder.build())
                    }
                }
            }
        }
    }

    override fun generateNotificationesRecordatorio(notificaciones: List<NotificacionRecordatorioModel>) {
        for (model in notificaciones) {
            var time = ""
            var date: Date? = null

            if (null != model && null != model.recordatorioModel
                && null != model.recordatorioModel.fecha && !model.recordatorioModel.fecha.isEmpty()) {
                try {
                    date = DateUtil.convertEngFechaToDate(model.recordatorioModel.fecha, DatetimeFormat.ISO_8601)
                    time = DateUtil.convertFechaToString(date, "hh:mm a")
                        .replace("a. m.", "AM")
                        .replace("p. m.", "PM")
                } catch (e: ParseException) {
                    BelcorpLogger.w(TAG, "ParseException", e)
                }

                val now = Calendar.getInstance()
                val calendarAlarm = Calendar.getInstance()
                calendarAlarm.time = date

                val diff = calendarAlarm.timeInMillis - now.timeInMillis
                val startSeconds = if ((diff / 1000).toInt() < 0) 0 else (diff / 1000).toInt()
                val endSeconds = startSeconds + 10
                val dayDiff = calculateDaysLeft(calendarAlarm)

                if (dayDiff >= 0) {
                    val parts = model.clienteModel.nombres.split(Pattern.quote(" ").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    val bundle = Bundle()
                    bundle.putString(GlobalConstant.CLIENT_MESSAGE_RECORDATORY, String.format(getString(R.string.debt_recordatory_alarm), parts[0], time))
                    bundle.putInt(GlobalConstant.CLIENTE_LOCAL_ID, model.clienteModel.id)
                    bundle.putInt(GlobalConstant.CLIENTE_ID, model.clienteModel.clienteID)
                    bundle.putString(GlobalConstant.CLIENT_NAME, model.clienteModel.nombres)
                    bundle.putString(GlobalConstant.CLIENT_TOTAL_DEBT, model.clienteModel.totalDeuda.toString())

                    val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(activity))
                    val builder = dispatcher.newJobBuilder()
                    builder.setService(RecordatoryJobService::class.java)
                    builder.tag = getString(R.string.reminder_key) + model.clienteModel.clienteID
                    builder.isRecurring = false
                    builder.setReplaceCurrent(true)
                    builder.lifetime = Lifetime.FOREVER
                    builder.trigger = Trigger.executionWindow(startSeconds, endSeconds)
                    builder.extras = bundle
                    dispatcher.schedule(builder.build())
                }
            }
        }
    }

    override fun showDatamiMessage() {
        listener?.showDatamiMessage()

    }

    override fun enableSDK(enable: Boolean, kinesisModel: KinesisModel) {

        if (enable) {

            Analytics.checkOptIn(true)

            kinesisModel.streams?.let { streams ->


                val kinesisSettings = KinesisSettings()
                    .setKinesisID(kinesisModel.id ?: "")
                    .setDeviceStream(streams[KinesisCode.STREAM_DEVICE] ?: "")
                    .setHistoryStream(streams[KinesisCode.STREAM_HISTORY] ?: "")
                    .setApplicationStream(streams[KinesisCode.STREAM_APPLICATION] ?: "")
                    .setLocationStream(streams[KinesisCode.STREAM_LOCATION] ?: "")
                    .setUsageStream(streams[KinesisCode.STREAM_USAGE] ?: "")
                    .setExtraStream(streams[KinesisCode.STREAM_EXTRAS] ?: "")
                    .setRegion(kinesisModel.region ?: 0)
                    .build()

                Analytics.Builder()
                    .selectInfo(Analytics.ANALYTICS_DEVICE,
                        Analytics.ANALYTICS_USAGE,
                        Analytics.ANALYTICS_APPLICATIONS,
                        Analytics.ANALYTICS_HISTORY,
                        Analytics.ANALYTICS_LOCATION)
                    .setKinesisSettings(kinesisSettings)
                    .setPeriodicity(240)
                    .setClearData(7)
                    .showTerms(false)
                    .build()
                    .initialize()

                context?.let { context ->

                    val sessionManager = SessionManager.getInstance(context)
                    if (!sessionManager.checkUsagePermission()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!hasUsageStatsPermission()) {
                                sessionManager.getCountViewHome()?.minus(1)?.let {
                                    sessionManager.saveCountViewHome(it)
                                }
                                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                                sessionManager.updateUsagePermission()
                            }
                        } else {
                            sessionManager.updateUsagePermission()
                        }
                    }
                }


            }

        } else {
            Analytics.cancelScheduler()
        }

    }

    private fun hasUsageStatsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return false

        val appOps = context?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context?.packageName)

        return if (mode == AppOpsManager.MODE_DEFAULT)
            context?.checkCallingOrSelfPermission(PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        else
            mode == AppOpsManager.MODE_ALLOWED

    }

    override fun showNewNotification(quantity: Int) {
        (activity as HomeActivity)?.showNotificactionIcon(quantity)
    }

    override fun showIntrigueDialog(image: String) {
        context?.let {
            Glide.with(it)
                .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(image).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        IntrigueDialog.Builder(it).withImage(image).show()
                        return false
                    }
                }).submit()
        }

    }

    override fun showCaminoBrillante(resDrwMedalla: Int) {
        ivwCaminoBrillante.setImageDrawable(ContextCompat.getDrawable(context!!, resDrwMedalla))
        lnrCaminoBrillante.visibility = View.VISIBLE
    }

    override fun showHome() {
        svwHome.visibility = View.INVISIBLE
        svwHome.startAnimation(animFadeInHome)
    }

    override fun showRenewDialog(image: String?, imageLogo: String?, message: String?) {
        context?.let {
            Glide.with(it)
                .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(image).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        if (secondImgLoaded) {
                            safeLet(image, imageLogo, message) { image, imageLogo, message ->
                                RenewDialog.Builder(it).withImage(image).withImageLogo(imageLogo).message(message).show()
                            }
                        }
                        firstImgLoaded = true
                        return false
                    }
                }).submit()

            Glide.with(it)
                .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(image).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if (firstImgLoaded) {
                            safeLet(image, imageLogo, message) { image, imageLogo, message ->
                                RenewDialog.Builder(it).withImage(image).withImageLogo(imageLogo).message(message).show()
                            }
                        }
                        secondImgLoaded = true
                        return false
                    }

                }).submit()
        }

    }

    override fun consultantApproved() {
        val messageDialog = MessageAnimDialog()

        try {
            messageDialog.isCancelable = false
            messageDialog
                .setIcon(R.drawable.ic_confeti_white, 0)
                .setStringTitle(R.string.consultant_approved_title_two)
                .setStringMessage(R.string.consultant_approved_description_two)
                .setStringAceptar(R.string.consultant_approved_option)
                .showCancel(false)
                .showIcon(true)
                .showClose(false)
                .setAnimated(true)
                .show(childFragmentManager, "modalAceptar")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalAceptar", e)
        }

        updateNewConsultant()
    }

    override fun consultantApprovedClose() {
        val messageDialog = MessageAnimDialog()

        try {
            messageDialog.isCancelable = false
            messageDialog
                .setIcon(R.drawable.ic_confeti_white, 0)
                .setStringTitle(R.string.consultant_approved_title)
                .setStringMessage(R.string.consultant_approved_description)
                .setStringAceptar(R.string.consultant_approved_option)
                .showCancel(false)
                .showIcon(true)
                .showClose(false)
                .setAnimated(true).setListener(object : MessageAnimDialog.MessageDialogListener {
                    override fun aceptar() {
                        showLoading()

                        presenter.logout()

                    }

                    override fun cancelar() {
                        //EMPTY
                    }
                })
                .show(childFragmentManager, "modalAceptar")
        } catch (e: IllegalStateException) {
            BelcorpLogger.w("modalAceptar", e)
        }

    }

    override fun showBannerLanzamiento(marquees: List<MarqueeItem>) {
        if (marquees.isNotEmpty()) {

            context?.let {
                val marqueeAdapter = MarqueeAdapter(marquees, it)
                mvwLaunchBanner.setAdapter(marqueeAdapter)
                mvwLaunchBanner.setOnMarqueeItemClickListener(marqueeOnClickListener)
                mvwLaunchBanner.visibility = View.VISIBLE

            }
        }
    }

    override fun goToLaunchProduct(action: String?) {
        listener?.goToProductLaunch(action ?: StringUtil.Empty)
    }

    override fun redirectToAction(action: String?) {
        listener?.redirectToOptionMenu(action ?: StringUtil.Empty, menuModel?.codigo.toString())
    }

    override fun onProductAdded(message: String?) {

        context?.let {
            val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
            showBottomDialog(it, message ?: "", null, colorText)
        }
    }

    override fun errorAddingProduct(message: String?) {
        context?.let {
            val colorText = ContextCompat.getColor(it, R.color.neon_red)
            showBottomDialog(it, message
                ?: getString(R.string.error_agregar_producto), null, colorText)
        }
    }


    override fun setBrandingConfig(brand: BrandConfigModel) {

        listener?.setBrandConfigModel(brand)
        TransitionManager.beginDelayedTransition(ctlMainLayout!!)

        if (brand.colorHome != null) {
            ctlMainLayout.setBackgroundColor(Color.parseColor(brand.colorHome))
        }

        if (brand.imageHomeUrl != null) {
            Glide.with(this).asDrawable().load(brand.imageHomeUrl).apply(RequestOptions.noTransformation().priority(Priority.HIGH)).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    ivwFooter.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {

                    ivwFooter.setImageDrawable(resource)
                    ivwFooter.visibility = View.VISIBLE
                    return false
                }
            }).submit()
        }
    }

    override fun clearBrandConfig() {
        listener?.clearBrandConfig()
        ctlMainLayout.setBackgroundColor(Color.WHITE)
        ivwFooter.visibility = View.GONE
    }

    private fun calculateDaysLeft(reminderCalender: Calendar): Int {
        val actualDate = Calendar.getInstance()

        val diff = reminderCalender.timeInMillis - actualDate.timeInMillis
        val days = diff / (24 * 60 * 60 * 1000)

        return days.toInt()
    }

    override fun onError(e: Throwable) {
        processError(e)
    }

    override fun setMenuModel(menu: MenuModel) {
        menuModel = menu
        arguments?.let { bundle ->
            bundle.getString(AdRedirectCode.PASE_PEDIDO)?.let {
                if (it.isNotEmpty()) {
                    listener?.redirigirDesdeStorie(menuModel?.codigo
                        ?: StringUtil.Empty, AdRedirectCode.PASE_PEDIDO, bundle)
                }
            }
        }

        listener?.setMenuModel(menu)
        SurveyBottomDialogFragment.newInstance(SurveyValidationType.VALIDATION_SURVEY_HOME, null, HomeComponent::class.java).show(fragmentManager, SurveyBottomDialogFragment.TAG)
    }

    override fun showUpdateMailDialog() {
        context?.let {
            val updateMailDialog = UpdateMailDialog(it, object : UpdateMailDialog.UpdateMailListener {
                override fun goToUpdateMail(dialog: UpdateMailDialog) {
                    Tracker.trackEvent(
                        GlobalConstant.EVENT_CAT_HOME_OPTION,
                        GlobalConstant.EVENT_NAME_ACTUALIZACION_CORREO,
                        GlobalConstant.EVENT_ACTION_ACTUALIZACION_CORREO,
                        resources.getString(R.string.actualizar),
                        GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                        presenter.userTrack)

                    listener?.goToProfile()
                }

                override fun onCancelUpdate() {
                    Tracker.trackEvent(
                        GlobalConstant.EVENT_CAT_HOME_OPTION,
                        GlobalConstant.EVENT_NAME_ACTUALIZACION_CORREO,
                        GlobalConstant.EVENT_ACTION_ACTUALIZACION_CORREO,
                        resources.getString(R.string.ahoraNo),
                        GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                        presenter.userTrack)
                }

                override fun onCloseDialog() {
                    Tracker.trackEvent(
                        GlobalConstant.EVENT_CAT_HOME_OPTION,
                        GlobalConstant.EVENT_NAME_ACTUALIZACION_CORREO,
                        GlobalConstant.EVENT_ACTION_ACTUALIZACION_CORREO,
                        resources.getString(R.string.cerrarPopUp),
                        GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                        presenter.userTrack)
                }
            })
            updateMailDialog.show()
        }
    }

    override fun goToReferedOption() {

        option?.let {
            if (!StringUtil.isNullOrEmpty(it)) {
                listener?.goToReferedOption(it)
                option = StringUtil.Empty
            }
        }


    }

    override fun checkForGanaMasRequest() {
        listener?.checkForGanaMasRequest()
    }

    override fun setWinOnClickVideo(winOnClickModel: WinOnClickModel?) {
        if (winOnClickModel == null) {
            if (hciWinclick.parent != null) {
                gldMenu.removeView(hciWinclick)
            }
        } else {
            listener?.setWinOnClickModel(winOnClickModel)
            arguments?.let { bundle ->
                bundle.getString(MenuCode.CONFERENCIA_DIGITAL)?.let {
                    if (it.isNotEmpty()) {
                        listener?.apply {
                            if (!isBackLocked) {
                                redirigirDesdeStorie(MenuCode.CONFERENCIA_DIGITAL, AdRedirectCode.CONFERENCIA_DIGITAL, bundle)
                            }
                        }
                        bundle.remove(MenuCode.CONFERENCIA_DIGITAL)
                    }
                }

            }

        }
    }

    override fun showNavidadVideo(urlDetalleResumen: String?, idContenido: String) {
        GlobalScope.launch(Dispatchers.Main) {
            urlDetalleResumen?.let { url ->
                idContenido.let { id ->
                    context?.let { contexto ->
                        VideoDialog.Builder(contexto)
                            .withurlVideo(url)
                            .withListener(
                                object : VideoDialog.VideoListener {
                                    override fun onStaring() {
                                        hideLoading()
                                    }

                                    override fun onVideoEnded() {
                                        presenter.guardarVideoVisto(idContenido)
                                        presenter.gestionarDialogSiguienteCampania()

                                    }
                                }
                            )
                            .show()
                    }
                }
            }
        }

    }


    override fun setDreamMeterOn(dreamMederModel: DreamMederModel?) {
        if (dreamMederModel == null) {
            if (hciDreams.parent != null) {
                gldMenu.removeView(hciDreams)
            }
        } else {
            hciDreams.showDreamMeter()
            listener?.setDreamMeterOnModel(dreamMederModel)
            arguments?.let {

            }
        }
    }


    override fun showCatalogsPopUp(catalogos: List<CatalogoWrapper>, campaingNumber: String?, urlCatalogo: String) {
        (activity as Context).let { contexto ->
            ShareNextCampaing.Builder(contexto)
                .withUrlToShare(urlCatalogo)
                .withCampaing(campaingNumber)
                .withCatalogos(catalogos)
                .show()
        }
        presenter.saveStatusShareDialog(true)
    }


    /**  */

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun verifyStoragePermission() {
        // EMPTY
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    internal fun showRationaleForStorage(request: PermissionRequest) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.permission_write_rationale))
                .setPositiveButton(getString(R.string.button_aceptar)) { dialog, button -> request.proceed() }
                .setNegativeButton(getString(R.string.button_cancelar)) { dialog, button -> request.cancel() }
                .show()
        }
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    internal fun showDeniedForStorage() {
        Toast.makeText(context, getString(R.string.permission_write_denied), Toast.LENGTH_SHORT).show()
        this@HomeFragment.activity?.finish()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    internal fun showNeverAskForStorage() {
        context?.let {
            Toast.makeText(it, getString(R.string.permission_write_neverask), Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.permission_write_denied))
                .setPositiveButton(getString(R.string.button_go_to_settings)) { dialog, button -> CommunicationUtils.goToSettings(context!!) }
                .setNegativeButton(getString(R.string.button_cancelar
                )) { dialog, button -> dialog.dismiss() }
                .show()
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    fun updateBirthday() {
        presenter.updateBirthdayByYear(DateUtil.getCurrentYear())
    }

    fun updateAnniversary() {
        presenter.updateAnniversaryByYear(DateUtil.getCurrentYear())
    }

    fun updateChristmas() {
        presenter.updateChristmasByYear(DateUtil.getCurrentYear())
    }

    fun updateNewYear() {
        presenter.updateNewYearByYear(DateUtil.getCurrentYear())
    }

    fun updateConsultantDay() {
        presenter.updateConsultantDayByYear(DateUtil.getCurrentYear())
    }

    fun updatePostulant() {
        presenter.updatePostulant()
    }

    fun updateNewConsultant() {
        presenter.updateNewConsultant()
    }

    fun updatePasoSextoPedido() {
        presenter.updatePasoSextoPedido()
    }

    fun updateBelcorpFifty() {
        presenter.updateBelcorpFifty()
    }

    fun checkBirthday() {
        presenter.checkBirthdayByYear(DateUtil.getCurrentYear())
    }

    fun checkAnniversary() {
        presenter.checkAnniversaryByYear(DateUtil.getCurrentYear())
    }

    fun checkChristmas() {
        presenter.checkChristmasByYear(DateUtil.getCurrentYear())
    }

    fun checkPostulant() {
        presenter.checkPostulant()
    }

    fun checkNewConsultant() {
        presenter.checkNewConsultant()
    }

    fun checkNewYear() {
        presenter.checkNewYearByYear(DateUtil.getCurrentYear())
    }

    fun checkPasoSextoPedido() {
        presenter.checkPasoSextoPedido()
    }

    fun checkBelcorpFifty() {
        presenter.checkBelcorpFifty()
    }

    fun checkConsultantDay() {
        presenter.checkConsultantDayByYear(DateUtil.getCurrentYear())
    }

    fun setHolidayText(consultantName: String, holiday: Int, belcorpExperience: Boolean, christmasExperience: Boolean) {

        var name: String = ""

        (activity as Context).let {
            if (belcorpExperience) {
                name = String.format(getString(R.string.home_belcorp_fifty_message), consultantName)
                tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.home_belcorp_fifty_user))
            } else {
                tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                when (holiday) {
                    HolydayType.BIRTHDAY -> {
                        name = String.format(getString(R.string.home_birthday_message), consultantName)
                        tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }
                    HolydayType.ANNIVERSARY -> {
                        name = String.format(getString(R.string.home_anniversary_message), consultantName)
                        tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }
                    HolydayType.CHRISTMAS -> {
                        name = String.format(getString(R.string.home_christmas_message), consultantName)
                        tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }
                    HolydayType.NEW_YEAR -> {
                        name = String.format(getString(R.string.home_new_year_message), consultantName)
                        tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }
                    HolydayType.CONSULTANT_DAY -> {
                        name = String.format(getString(R.string.home_consultant_day_message), consultantName)
                        tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }
                    HolydayType.NONE -> {
                        name = String.format(getString(R.string.home_welcome_message), consultantName)
                        tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }
                    else -> {
                        name = String.format(getString(R.string.home_welcome_message), consultantName)
                        tvwHomeName.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }
                }
            }

            if (christmasExperience) {
                tvwHomeCampaign.setTextColor(ContextCompat.getColor(it, R.color.home_campaign_color))
            }
        }


        tvwHomeName.text = name
    }

    override fun onClickBubble() {
        storieBubble?.animateAndEnable(false)
        presenter.getListStories()
    }

    override fun onListStoriesObtained(history: StorieModel) {
        counterExecution = 0
        if (history.contenidoDetalle?.isNotEmpty() == true) {
            val tamanno = history.contenidoDetalle?.size ?: 0
            if (counterStorie <= tamanno - 1) {
                for (i in 0 until tamanno) {
                    history.contenidoDetalle?.let{ contenidoDetalle ->
                        contenidoDetalle[i].urlDetalleResumen?.let{ url->
                            Glide.with(this)
                                .asBitmap()
                                .load(imageHelper?.getResolutionURL(url))
                                .into(object : SimpleTarget<Bitmap>() {

                                    override fun onLoadFailed(errorDrawable: Drawable?) {
                                        gestionarCargaStorie(history, i, false)
                                    }

                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>) {
                                        gestionarCargaStorie(history, i, true)
                                    }
                                }
                                )
                        }
                    }


                }
            } else {
                (activity as? HomeActivity)?.goToStoriesRoot(history)
            }
        } else {
            storieBubble?.animateAndEnable(true)
        }
    }

    override fun showMultiOrderType() {
        listener?.showMultiOrderType()
    }

    override fun hideMultiOrderType() {
        listener?.hideMultiOrderType()
    }

    override fun showRateDialog() {
        context?.let {
            RateDialog.getInstance(it)?.apply {
                onClickRateDialog = object : RateDialog.OnClickRateDialog {
                    override fun onClickYesOption() {
                        OtherAppUtil.openRatePlayStore(it)
                        presenter.updateStateIsRate(true)
                    }

                    override fun onClickAlReadyOption() {
                        presenter.updateStateIsRate(true)
                    }

                    override fun onClickAfterOption() {
                        presenter.updateStateIsRate()
                    }
                }
                show()
            }
        }
    }

    override fun showItemMenuBlackFriday(flag: Boolean) {
        if(flag) {
            hciSubcampaign.visibility = View.VISIBLE
            hciSubcampaign.showSubcampaign(R.drawable.boton_black_friday)
        } else
            hciSubcampaign.visibility = View.GONE
    }

    override fun setTitleSubcampaign(title: String) {
        titleSubcampaign = title
    }

    private fun gestionarCargaStorie(history: StorieModel, position: Int, seDescargo: Boolean) {
        history?.contenidoDetalle?.let{
            it[position]?.iterado = true
            it[position]?.descargado = seDescargo
            if (iteroTodas(it)) {
                if (counterExecution < 1) {
                    storieBubble.animateAndEnable(true)
                    counterExecution++
                    if (activity != null)
                        (activity as HomeActivity).goToStoriesRoot(history)

                }
            }
        }
    }

    private fun iteroTodas(contenidoDetalle: List<StorieModel.ContenidoDetalleModel?>): Boolean {

        contenidoDetalle.forEach { contenido ->
            contenido?.apply {
                if (!iterado) {
                    counterStorie++
                    return false
                }
            }
        }
        return true
    }

    override fun onStoriesToThumbnail(storieModel: StorieModel) {
        storieBubble?.animateAndEnable(true)
        storieModel?.contenidoDetalle?.let {
            if (it.isNotEmpty()) {
                cntrHeaderStorie?.visibility = View.VISIBLE

                storieBubble?.changeAllSeen(it.filterNotNull().toMutableList())

                storieBubble?.getImageviewBubble()?.numberOfArches = 5

                Glide.with(this)
                    .asBitmap()
                    .load(storieModel.urlMiniatura)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>) {
                            storieBubble?.getImageviewBubble()?.setImageBitmap(resource)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            storieBubble?.getImageviewBubble()?.setImageResource(R.drawable.thumbnailstoriedefault)
                        }
                    })
            }
        }
    }

    private fun gestionarRedireccionamiento() {
        arguments?.let { bundle ->
            bundle.getString(AdRedirectCode.BONIFICACION)?.let {
                if (it.isEmpty()){
                    redirigir(MenuCode.INCENTIVES, AdRedirectCode.BONIFICACION, bundle)
                }
            }

            bundle.getString(AdRedirectCode.CLIENTES)?.let{
                if (it.isEmpty()) {
                    redirigir(MenuCode.CLIENTS, AdRedirectCode.CLIENTES, bundle)
                }
            }

            bundle.getString(AdRedirectCode.MI_ACADEMIA)?.let{
                if (it.isEmpty()){
                    redirigir(MenuCode.ACADEMIA, AdRedirectCode.MI_ACADEMIA, bundle)
                }
            }

            bundle.getString(AdRedirectCode.TVO)?.let{
                if (it.isEmpty()){
                    redirigir(MenuCode.TUVOZ, AdRedirectCode.TVO, bundle)
                }
            }

            bundle.getString(AdRedirectCode.CHAT)?.let{
                if (it.isEmpty()){
                    redirigir(MenuCode.CHATBOT, AdRedirectCode.CHAT, bundle)
                }
            }
        }
    }

    override fun showMenuOption(code: String?, status: Boolean?, title: String?) {
        activity?.let{ act ->
            (act as HomeActivity).showMenuOption(code, status, title)
        }
    }

    override fun openWebBrowser(url: String) {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        )
    }

    private fun redirigir(menuCode: String, codigo: String, bundle: Bundle) {
        val valueBundle = bundle.getString(codigo)
        listener?.let {
            if (!it.isBackLocked) {
                it.redirigirDesdeStorie(menuCode, valueBundle, bundle)
            }
        }
    }

    override fun setMoverBarraNavegacion(flag: Boolean) {
        listener?.setFlagMoverBarraNavegacion(flag)
    }

    /** */

    internal interface HomeFragmentListener {

        val isBackLocked: Boolean
        fun showHoliday(newConsultant: Boolean, userType: Int, birthday: Boolean,
                        anniversary: Boolean, consultantName: String, consultantCode: String,
                        sixth: Boolean, imgUrl: String, isCupon: Boolean, isDatamiMessage: Boolean)

        fun setCountryISO(countryISO: String)

        fun setUserType(userType: Int)

        fun showCupon()

        fun setRevistaDigitalSuscripcion(tieneGND: Boolean, revistaDigitalSuscripcion: Int, ganaMasNativo: Boolean, ganaMasSuscrita: Boolean)

        fun updateCurrentMenuItem(menuCode: String)

        fun updateCurrentMenuTopItem(menuCode: String)

        fun reloadMenu()

        fun goToMyClients(section: Int, menuType: Int)

        fun goToNewOrder(section: Int, menuType: Int)

        fun goToOrdersNative(menuType: Int)

        fun goToIncentives(section: Int, menuType: Int)

        fun goToDebts(section: Int)

        fun goToProfile()

        fun goToTutorial(userCode: String?, countryISO: String?, menuType: Int)

        fun goToMailbox()

        fun goToTerms(menuType: Int)

        fun goToOffers(menuType: Int, palanca: String)

        fun showAnniversary()

        fun showBirthday()

        fun showChristmas()

        fun showNewYear()

        fun showConsultantDay()

        fun showPasoSextoPedido()

        fun showBelcorpFifty()

        fun showPostulant()

        fun goToOffersSubscription(page: String?)

        fun showDatamiMessage()

        fun btnGoToChatBot()

        fun goSubcampaign(title: String)

        fun goToProductLaunch(action: String)

        fun redirectToOptionMenu(action:String, menu: String)

        fun goToCatalog()

        fun goToWinonclick()

        fun goToDreamonclick()

        fun goToReferedOption(option: String)

        fun checkForGanaMasRequest()

        fun updateOffersCount(count: Int?)

        fun setMenuModel(menu: MenuModel)

        fun setWinOnClickModel(winOnClick: WinOnClickModel)

        fun setDreamMeterOnModel(dreamMederModel: DreamMederModel)

        fun setBrandConfigModel(brandConfig: BrandConfigModel)

        fun redirigirDesdeStorie(menuCodigo: String, donde: String?, bundle: Bundle)

        fun clearBrandConfig()

        fun showMultiOrderType()

        fun hideMultiOrderType()

        fun setFlagMoverBarraNavegacion(flag: Boolean)
    }

    companion object {

        private val TAG = "HomeFragment"

        private val REQUEST_CODE_REGISTER = 203
    }

}
