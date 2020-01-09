package biz.belcorp.consultoras.feature.home

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Log

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

import java.text.ParseException
import java.util.ArrayList

import javax.inject.Inject

import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.AuthModelDataMapper
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.banner.BannerModel
import biz.belcorp.consultoras.common.model.banner.BannerModelMapper
import biz.belcorp.consultoras.common.model.brand.BrandConfigModelDataMapper
import biz.belcorp.consultoras.common.model.dreammeter.DreamMederModel
import biz.belcorp.consultoras.common.model.dreammeter.DreamMeterModelDataMapper
import biz.belcorp.consultoras.common.model.home.CardEntity
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel
import biz.belcorp.consultoras.common.model.kinesis.RemoteConfigDataMapper
import biz.belcorp.consultoras.common.model.menu.MenuModel
import biz.belcorp.consultoras.common.model.menu.MenuModelDataMapper
import biz.belcorp.consultoras.common.model.notification.NotificacionClienteModelDataMapper
import biz.belcorp.consultoras.common.model.notification.NotificacionRecordatorioModelDataMapper
import biz.belcorp.consultoras.common.model.stories.StorieModel
import biz.belcorp.consultoras.common.model.stories.StorieModelMapper
import biz.belcorp.consultoras.common.model.winonclick.WinOnClickModelDataMapper
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.entity.caminobrillante.NivelConsultoraCaminoBrillante
import biz.belcorp.consultoras.domain.exception.ConsultantApprovedException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.exception.ErrorFactory
import biz.belcorp.consultoras.feature.auth.startTutorial.ContenidoResumenDetalleModel
import biz.belcorp.consultoras.feature.auth.startTutorial.startTutorialModelMapper
import biz.belcorp.consultoras.feature.home.marquee.MarqueeItem
import biz.belcorp.consultoras.util.Constant
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.mobile.analytics.core.Analytics
import biz.belcorp.library.security.AesEncryption
import biz.belcorp.library.security.JwtEncryption
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DateUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.consultoras.util.anotation.ConsultoraNivelCode.NIVEL_1
import biz.belcorp.consultoras.util.anotation.ConsultoraNivelCode.NIVEL_2
import biz.belcorp.consultoras.util.anotation.ConsultoraNivelCode.NIVEL_3
import biz.belcorp.consultoras.util.anotation.ConsultoraNivelCode.NIVEL_4
import biz.belcorp.consultoras.util.anotation.ConsultoraNivelCode.NIVEL_5
import biz.belcorp.consultoras.util.anotation.ConsultoraNivelCode.NIVEL_6
import kotlinx.coroutines.CoroutineScope

import biz.belcorp.library.util.DeviceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@PerActivity
class HomePresenter @Inject
constructor(private val sessionUseCase: SessionUseCase,
            private val menuUseCase: MenuUseCase,
            private val authUseCase: AuthUseCase,
            private val accountUseCase: AccountUseCase,
            private val userUseCase: UserUseCase,
            private val clienteUseCase: ClienteUseCase,
            private val caminobrillanteUseCase: CaminoBrillanteUseCase,
            private val authModelDataMapper: AuthModelDataMapper,
            private val loginModelDataMapper: LoginModelDataMapper,
            private val notificacionClienteModelDataMapper: NotificacionClienteModelDataMapper,
            private val notificacionRecordatorioModelDataMapper: NotificacionRecordatorioModelDataMapper,
            private val menuModelDataMapper: MenuModelDataMapper,
            private val remoteConfigDataMapper: RemoteConfigDataMapper,
            private val brandConfigModelDataMapper: BrandConfigModelDataMapper,
            private val notificationUseCase: NotificacionUseCase,
            private val storieUseCase: StorieUseCase,
            private val catalogUseCase: CatalogUseCase,
            private val festivalUseCase: FestivalUseCase,
            private val offerUseCase: OfferUseCase,
            private val orderUseCase: OrderUseCase
) : Presenter<HomeView> {
    private var homeView: HomeView? = null
    private var history: StorieModel? = null
    private var banner: BannerModel? = null
    private var showInContainer = -1
    private var loginModel: Login? = null
    var userTrack: User? = null

    override fun attachView(view: HomeView) {
        homeView = view
        getUser()
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.sessionUseCase.dispose()
        this.menuUseCase.dispose()
        this.authUseCase.dispose()
        this.accountUseCase.dispose()
        this.userUseCase.dispose()
        this.clienteUseCase.dispose()
        this.caminobrillanteUseCase.dispose()
        this.catalogUseCase.dispose()
        this.orderUseCase.dispose()
        this.homeView = null
    }

    fun getToken() {
        userUseCase.getDevice(GetToken())
    }

    fun getUser() {
        userUseCase[UserObserver()]
    }

    fun saveToken(device: Device) {
        userUseCase.saveDevice(device, TokenResult())
    }

    fun dataOffline() {
        userUseCase.getLogin(GetUser(false))
    }

    fun initScreenTrack() {
        userUseCase.getLogin(GetUserScreenTrack())
    }

    fun logout() {
        authUseCase.logoutWithData(Logout())
    }

    fun checkSchedule() {
        authUseCase.checkSchedule(StatusObserver())
    }

    fun getFestConfiguration() {
        userTrack?.campaing?.let {
            getFestConfiguration(it)
        }
    }

    fun refreshData() {
        homeView?.showLoading()
        accountUseCase.refreshData(RefreshData())
    }

    fun getMoverBarraNavegacion(){
        GlobalScope.launch{
            userUseCase.getMoverBarraNavegacion()?.let{
                homeView?.setMoverBarraNavegacion(it)
                getUser()
            }
        }
    }

    private fun getFestConfiguration(campaign: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                festivalUseCase.getConfiguracion(campaign.toInt())
                val localResult = festivalUseCase.getLocalConfiguration()
                localResult?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        homeView?.let { view ->
                            view.showMenuOption(MenuCode.NAVI_FEST, localResult.Activo, localResult.Titulo?.toUpperCase())
                        }
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    fun getCountMaxRecentSearch(context: Context) {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val session = SessionManager.getInstance(context)

        remoteConfig.fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.activateFetched()
            }

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val countMaxRecentSearch = remoteConfig.getLong(BuildConfig.REMOTE_CONFIG_COUNT_MAX_RECENT_SEARCH)
                    session.saveCountMaxRecentSearch(countMaxRecentSearch.toInt())
                } catch (e: Exception) {
                }
            }
        }
    }

    fun sendFeedBack() {
        userUseCase[UserCountryObserver()]
    }

    fun goChatBot() {
        userUseCase[ChatBotObserver()]
    }

    fun gestionarAsesorRegalo() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                homeView?.showLoading()
                GlobalScope.launch(Dispatchers.IO) {
                    val userconfig = accountUseCase.getConfigByCodeId(UserConfigAccountCode.ASESOR_REGALOS)
                    GlobalScope.launch(Dispatchers.Main) {
                        userconfig?.value1?.let {
                            homeView?.openWebBrowser(it)
                        }
                        homeView?.hideLoading()
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    fun getMenuActive(code1: String, code2: String) {
        this.menuUseCase.getActive(code1, code2, GetMenuObserver())
    }

    fun updateMenu(listaMenu: List<MenuModel>) {
        this.menuUseCase.updateVisibleAndOrden(menuModelDataMapper.transform(listaMenu), UpdateMenuObserver())
    }

    fun displayUpdateMail(userCode: String) {
        this.sessionUseCase.isUpdateMail(userCode, UpdateMailObserver())
    }

    fun getCountNotifications() {
        this.notificationUseCase.getCountNotificationsWithoutLookingObservable(NotificationCounter())
    }

    fun guardarVideoVisto(idVideo: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                userUseCase.getUser()?.let { usuario ->
                    storieUseCase.saveStateContenidoCoroutine(ContenidoRequest().apply {
                        campaniaID = usuario.campaing?.toInt()
                        codigoRegion = usuario.regionCode
                        codigoZona = usuario.zoneCode
                        codigoSeccion = usuario.codigoSeccion
                        indicadorConsultoraDigital = usuario.indicadorConsultoraDigital.toString()
                        numeroDocumento = usuario.numeroDocumento
                        idContenidoDetalle = idVideo.toInt()
                    })
                }
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    fun getTitleSubcampaign(){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.configuracion()
                val configSubcampaign = result.firstOrNull {config -> config?.tipoOferta == OfferTypes.SR}?.subCampaniaConfiguracion
                var title = StringUtil.Empty
                configSubcampaign?.let { config ->
                    config.bannerTextoTitulo?.let { configTitle ->
                        if(configTitle.isNotEmpty())
                            title = configTitle
                    }
                }

                GlobalScope.launch (Dispatchers.Main) {
                    homeView?.setTitleSubcampaign(title)
                }

            } catch (e: Exception) {}

        }
    }

    private fun getRemoteFlagSubcampaign(){
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        remoteConfig.fetch().addOnCompleteListener{task->

            if (task.isSuccessful) {
                remoteConfig.activateFetched()
            }

            GlobalScope.launch(Dispatchers.IO) {

                try {
                    val flagSubcampaign = remoteConfig.getBoolean(BuildConfig.REMOTE_CONFIG_FLAG_MENU_SUBCAMPAIGN)
                    GlobalScope.launch (Dispatchers.Main) {
                        homeView?.showItemMenuBlackFriday(flagSubcampaign)
                    }

                } catch (e: Exception) {}

            }
        }
    }


    private fun showUserData(login: Login, isOnline: Boolean, traerVideo: Boolean = false) {
        homeView?.showChatbot(login.isChatBot)
        getRemoteFlagSubcampaign()

        login.consultantCode?.let {
            Analytics.addTag("cod_consultora", it)
        } ?: kotlin.run {
            Analytics.addTag("cod_consultora", "00000000")
        }

        login.campaing?.let {
            Analytics.addTag("cod_campania", it)
        } ?: kotlin.run {
            Analytics.addTag("cod_campania", "000000")
        }

        login.countryISO?.let { Analytics.addTag("cod_pais", it) }

        login.consultantName?.let { Analytics.addTag("nombre", it) }

        var endDay = ""
        var endHour = ""
        val countryISO = login.countryISO
        var campaign = login.campaing
        val closingDays = login.closingDays

        if (!TextUtils.isEmpty(campaign) && campaign?.length == 6)
            campaign = campaign.substring(4)

        try {
            endHour = DateUtil.convert24to12hour(login.horaFinPortal)
            endDay = StringUtil.capitalize(DateUtil.convertFechaToString(
                DateUtil.convertirISODatetoDate(login.billingStartDate), "dd MMMM"))
        } catch (e: ParseException) {
            BelcorpLogger.w(TAG, "ParseException", e)
        }

        if (TextUtils.isEmpty(login.alias)) {
            val name = login.consultantName?.split(" ")
            login.alias = name?.get(0)
        }

        var ganaMas = false

        if (login.isGanaMasNativo) ganaMas = true

        homeView?.let { hv ->

            login.userType?.let { hv.setUserType(it) }

            login.userType?.let { usrtype ->
                login.isTieneGND?.let { tieneGND ->
                    login.revistaDigitalSuscripcion?.let { revDigSuscrip ->
                        login.bannerGanaMas?.let { bnrGanaMas ->
                            login.isRDEsSuscrita?.let {esSuscrita ->
                                hv.setRevistaDigitalSuscripcion(usrtype, tieneGND, revDigSuscrip, bnrGanaMas, ganaMas, esSuscrita)
                            }

                        }
                    }
                }
            }

            countryISO?.let {
                hv.setCountryISO(it)
                hv.setDecimalFormatISO(CountryUtil.getDecimalFormatByISO(it, true))
            }

            login.countryMoneySymbol?.let { hv.setCountryMoneySymbol(it) }


            GlobalScope.launch {
                sessionUseCase.updateMultiOrderState(login.isMultipedido)
                GlobalScope.launch(Dispatchers.Main) {
                    if (login.isMultipedido) {
                        homeView?.showMultiOrderType()
                    } else {
                        homeView?.hideMultiOrderType()
                    }
                }
            }

            login.detail?.let { logDetail ->
                if (logDetail.size > 5) {
                    val estadoCuentaDetail = logDetail[5]
                    estadoCuentaDetail?.let { estadoCuentaDtl ->
                        campaign?.let { cmp ->
                            closingDays?.let { diaCierre ->
                                login.expirationDate?.let { dayExpiration ->
                                    login.isPagoEnLinea?.let { pagOnline ->
                                        hv.setCampaign(cmp, endDay, endHour, diaCierre, pagOnline,
                                            dayExpiration, estadoCuentaDtl, login.isMultipedido, login.lineaConsultora)
                                    } ?: kotlin.run {
                                        hv.setCampaign(cmp, endDay, endHour, diaCierre, false,
                                            dayExpiration, estadoCuentaDtl, login.isMultipedido, login.lineaConsultora)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            hv.setUserData(loginModelDataMapper.transform(login))


            login.detail?.let { detail ->
                countryISO?.let { iso ->
                    if (detail.isEmpty()) {
                        hv.showEmptyCards(iso)
                    } else {
                        if (detail.size >= 7) {
                            showHomeDetail(detail[CardType.CHAT], iso)
                        }
                        showHomeDetail(detail[CardType.INCENTIVES], iso)

                        if (detail.size > 1)
                            showHomeDetail(detail[CardType.CLIENTS], iso)

                        if (detail.size >= 3) {
                            val monto = detail[CardType.ORDERS]?.amount?.let { it }
                                ?: kotlin.run { 0 }
                            detail[CardType.OFFERS]?.value = java.lang.Double.valueOf(monto.toDouble())
                            showHomeDetail(detail[CardType.OFFERS], iso)

                        }

                        if (detail.size >= 4) {
                            detail[CardType.GANANCIAS]?.amount = CountryUtil.getMessageTypeByISO(iso)
                            showHomeDetail(detail[CardType.GANANCIAS], iso)
                        }

                        if (detail.size >= 6) {
                            showHomeDetail(detail[CardType.CATALOGO], iso)
                        }
                    }
                }
            } ?: kotlin.run {
                countryISO?.let { hv.showEmptyCards(it) }
            }



            login.campaing?.let {
                sessionUseCase.isIntrigueStatus(CheckIntrigueStatusObserver(it))
                sessionUseCase.isRenewStatus(CheckRenewStatusObserver(it))
            }
            getResumenContenidoCoroutine()
        }
    }


    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun gestionarDialogSiguienteCampania() {
        GlobalScope.launch(Dispatchers.IO) {
            try {

                GlobalScope.launch(Dispatchers.Main) { homeView?.showLoading() }

                val esPopUpActive = accountUseCase.getConfigActiveCoroutine(GlobalConstant.POPUP_COMPARTIR_CATALOGO)
                esPopUpActive?.let { active ->
                    if (active) {
                        val usuario = userUseCase.getUser()
                        val statusDialog = sessionUseCase.getStatusDialogNextCampaing()
                        val shareCampaing = sessionUseCase.getActualCampaing()
                        usuario?.let { user ->
                            statusDialog?.let { status ->

                                if ((shareCampaing.isEmpty() || shareCampaing != user.campaing) && !status) {
                                    val catalogos = catalogUseCase.getCorroutine(user, CountryUtil.getMaximumCampaign(user.countryISO), true)
                                    catalogos?.let { lista ->
                                        GlobalScope.launch(Dispatchers.Main) {
                                            homeView?.let { hv ->
                                                var link = ""
                                                user.detail?.forEach { t ->
                                                    if (t?.detailType == 6) {
                                                        link = t.name?.let { it -> it }
                                                            ?: kotlin.run { "" }
                                                        return@forEach
                                                    }
                                                }
                                                if (link.isNotEmpty()) {
                                                    val campaingNumber = user.campaing?.removeRange(0, 4)

                                                    hv.showCatalogsPopUp(lista.filterNotNull(), campaingNumber, link)

                                                    GlobalScope.launch(Dispatchers.IO) {

                                                        sessionUseCase.saveViewedDialogNextCampaing(true)

                                                        GlobalScope.launch(Dispatchers.Main) { homeView?.hideLoading() }

                                                        sessionUseCase.saveActualCampaing(user.campaing?.let { it1 -> it1 }
                                                            ?: kotlin.run { "" })
                                                    }
                                                } else {
                                                    GlobalScope.launch(Dispatchers.Main) { homeView?.hideLoading() }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    GlobalScope.launch(Dispatchers.Main) { homeView?.hideLoading() }
                                    sessionUseCase.saveViewedDialogNextCampaing(false)
                                }

                            }
                        }

                    } else {
                        GlobalScope.launch(Dispatchers.Main) { homeView?.hideLoading() }
                    }
                } ?: kotlin.run { GlobalScope.launch(Dispatchers.Main) { homeView?.hideLoading() } }

            } catch (e: Exception) {
                BelcorpLogger.e(e)
                GlobalScope.launch(Dispatchers.Main) { homeView?.hideLoading() }
            }
        }
    }

    fun saveStatusShareDialog(status: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                sessionUseCase.saveViewedDialogNextCampaing(status)
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    private fun updateOffersCount(login: Login) {
        homeView?.showOrdersCount(login.detail?.get(CardType.ORDERS)?.count)
    }

    private fun showHomeDetail(loginDetail: LoginDetail?, countryISO: String) {

        homeView?.let { hv ->
            hv.showLoading()
            loginDetail?.let { logDetail ->

                logDetail.detailType?.let { detailType ->

                    val cardEntity = CardEntity(detailType, logDetail.detailDescription)
                    cardEntity.name = logDetail.name

                    logDetail.amount?.let {
                        cardEntity.amount = it
                    }
                    logDetail.isState?.let {
                        cardEntity.isState = it
                    }
                    logDetail.value?.let {
                        cardEntity.value = it
                    }
                    when (cardEntity.cardType) {
                        CardType.INCENTIVES -> hv.showIncentives(cardEntity, countryISO)
                        CardType.OFFERS -> hv.showOffers(cardEntity)
                        CardType.GANANCIAS -> hv.showOrders(cardEntity)
                        CardType.CLIENTS -> hv.showClients(cardEntity)
                        CardType.CATALOGO -> hv.showCatalog(cardEntity)
                        CardType.CHAT -> hv.showChat(cardEntity)
                        else -> {
                            //Not required
                        }
                    }

                }

            } ?: kotlin.run {
                hv.hideLoading()
            }
            hv.hideLoading()
        }
    }

    fun showBannerAction(position: Int) {
        banner?.contenidoDetalle?.get(position)?.let { banDetail ->
            processBannerAction(banDetail)
        }
    }


    private fun processBannerAction(contenidoDetalleModel: BannerModel.ContenidoDetalleModel){
        when {
            contenidoDetalleModel.accion?.contains(AdRedirectCode.GANA_MAS) == true -> {
                homeView?.goToLaunchProduct(contenidoDetalleModel.accion)
            }
            contenidoDetalleModel.accion == AdRedirectCode.ADD_CART_CUV -> {
                contenidoDetalleModel.descripcion?.let{ cuv ->
                    homeView?.showLoading()
                    orderUseCase.searchCUV(cuv, CUVObserver())

                }
            }
            else -> {
                homeView?.redirectToAction(contenidoDetalleModel.accion)
            }
        }
    }

    fun trackEvent(menuType: Int,
                   eventLabel: String,
                   eventName: String,
                   screenName: String) {
        userUseCase[UserPropertyObserver(menuType, eventLabel, eventName, screenName)]
    }

    fun trackEvent(menuType: Int,
                   eventLabel: String,
                   eventName: String,
                   screenName: String,
                   action: String) {
        userUseCase[UserPropertyObserver(menuType, eventLabel, eventName, screenName, action)]
    }

    fun trackEvent(screenHome: String,
                   eventCatHomeOption: String,
                   eventActionHomeOption: String,
                   eventLabelSecondaryMenu: String,
                   eventNameHomeOption: String, logout: Boolean) {
        userUseCase[EventPropertyObserver(screenHome,
            eventCatHomeOption,
            eventActionHomeOption,
            eventLabelSecondaryMenu,
            eventNameHomeOption, logout)]
    }

    fun trackBackPressed(screenName: String) {
        userUseCase[BackPressedPropertyObserver(screenName)]
    }

    fun trackMenu(screenName: String) {
        userUseCase[MenuPropertyObserver(screenName)]
    }

    fun checkBirthdayByYear(year: Int) {
        userUseCase.checkBirthdayByYear(year, CheckBirthdayByYearObserver())
    }

    fun updateBirthdayByYear(year: Int) {
        userUseCase.updateBirthdayByYear(year, UpdateBirthdayByYearObserver())
    }

    fun checkAnniversaryByYear(year: Int) {
        userUseCase.checkAnniversaryByYear(year, CheckAnniversaryByYearObserver())
    }

    fun updateAnniversaryByYear(year: Int) {
        userUseCase.updateAnniversaryByYear(year, UpdateAnniversaryByYearObserver())
    }

    fun checkChristmasByYear(year: Int) {
        userUseCase.checkChristmasByYear(year, CheckChristmasByYearObserver())
    }

    fun updateChristmasByYear(year: Int) {
        userUseCase.updateChristmasByYear(year, UpdateChristmasByYearObserver())
    }

    fun checkNewYearByYear(year: Int) {
        userUseCase.checkNewYearByYear(year, CheckNewYearByYearObserver())
    }

    fun updateNewYearByYear(year: Int) {
        userUseCase.updateNewYearByYear(year, UpdateNewYearByYearObserver())
    }

    fun checkConsultantDayByYear(year: Int) {
        userUseCase.checkConsultantDayByYear(year, CheckConsultantDayByYearObserver())
    }

    fun updateConsultantDayByYear(year: Int) {
        userUseCase.updateConsultantDayByYear(year, UpdateConsultantDayByYearObserver())
    }

    fun checkPostulant() {
        userUseCase.checkPostulant(CheckPostulantObserver())
    }

    fun checkNewConsultant() {
        userUseCase.checkNewConsultant(CheckNewConsultantObserver())
    }

    fun checkPasoSextoPedido() {
        userUseCase.checkPasoSextoPedido(CheckPasoSextoPedidoObserver())
    }

    fun checkBelcorpFifty() {
        userUseCase.checkBelcorpFifty(CheckBelcorpFiftyObserver())
    }

    fun updatePasoSextoPedido() {
        userUseCase.updatePasoSextoPedido(UpdatePasoSextoPedidoObserver())
    }

    fun updateBelcorpFifty() {
        userUseCase.updateBelcorpFifty(UpdateBelcorpFiftyObserver())
    }

    fun updatePostulant() {
        userUseCase.updatePostulant(UpdatePostulantObserver())
    }

    fun updateNewConsultant() {
        userUseCase.updateNewConsultant(UpdateNewConsultantObserver())
    }

    fun getNotificationesCliente() {
        clienteUseCase.getNotificacionesCliente(GetNotificacionesClienteObserver())
    }

    fun getNotificationsRecordatorio() {
        clienteUseCase.getNotificationesRecordatorio(GetNotificacionesRecordatorioObserver())
    }

    fun startSDK(data: String) {
        val observer = EnableSDKObserver()
        observer.kinesisModel = remoteConfigDataMapper.transform(data)
        accountUseCase.enableSDK(observer)
    }

    fun saveUsabilityConfig(config: String) {
        userUseCase.saveUsabilityConfig(config, SaveUsabilityConfigObserver())
    }

    fun getUsabilityConfig(login: Login) {
        userUseCase.getUsabilityConfig(GetUsabilityConfigObserver(login))
    }

    fun checkSearchPrompt() {
        userUseCase.checkSearchPrompt(CheckSearchPromptObserver())
    }

    fun saveSearchPrompt() {
        userUseCase.saveSearchPrompt(SaveSearchPromptObserver())
    }

    fun updateNotificationStatus(status: Boolean?) {
        status?.let {
            sessionUseCase.updateNotificationStatus(it, UpdateNotificationStatus())
        }

    }

    fun checkNewNotifications() {
        this.notificationUseCase.getCountNotificationsWithoutLookingObservable(NotificationCounter())
    }

    fun clearIsShowMessagesGiftBar() {
        sessionUseCase.saveStatusGiftAnimation(true, GiftGenericSetStatus())
    }

    fun gotToMethodPay() {
        accountUseCase.getConfig(UserConfigAccountCode.PAYONLINE, object : BaseObserver<Collection<UserConfigData?>?>() {
            override fun onNext(t: Collection<UserConfigData?>?) {
                t?.let { userConfigData ->
                    for (item in userConfigData) {
                        item?.let { itm ->
                            itm.code?.let { code ->
                                if (PagoEnLineaConfigCode.FLUJO.compareTo(code, ignoreCase = true) == 0) {
                                    homeView?.gotToMethodPay(item)
                                }
                            }
                        }
                    }
                }
            }
        })
    }


    fun loadCaminoBrillante() {
        accountUseCase.getConfig(UserConfigAccountCode.CAMINO_BRILLANTE, object : BaseObserver<Collection<UserConfigData?>?>() {
            override fun onNext(t: Collection<UserConfigData?>?) {
                t?.let { userConfigsData ->

                    if (!userConfigsData.isEmpty()) {
                        caminobrillanteUseCase.getResumenConsultoraAsObserver(object : BaseObserver<NivelConsultoraCaminoBrillante?>() {
                            override fun onNext(t: NivelConsultoraCaminoBrillante?) {
                                t?.let { nivel ->
                                    nivel.nivel?.let { level ->
                                        homeView?.showCaminoBrillante(getMedallaByIdNivel(level))
                                    }
                                }
                            }

                            override fun onComplete() {
                                homeView?.showHome()
                                homeView?.hideLoading()
                            }

                            override fun onError(exception: Throwable) {
                                homeView?.showHome()
                                homeView?.hideLoading()
                            }
                        })
                    } else {
                        homeView?.showHome()
                        homeView?.hideLoading()
                    }
                } ?: kotlin.run {
                    homeView?.showHome()
                    homeView?.hideLoading()
                }
            }
        })
    }

    fun loadDreamMeter(medidorOnClick: DreamMederModel) {
        accountUseCase.getConfig(UserConfigAccountCode.DREAM_METER, object : BaseObserver<Collection<UserConfigData?>?>() {
            override fun onNext(t: Collection<UserConfigData?>?) {
                t?.let { userConfigsData ->
                    if (!userConfigsData.isEmpty()) {
                        homeView?.setDreamMeterOn(medidorOnClick)

                    } else {
                        homeView?.setDreamMeterOn(null)
                    }
                } ?: kotlin.run {
                    homeView?.setDreamMeterOn(null)
                }
            }
        })
    }


    private fun getMedallaByIdNivel(nivelActual: String): Int {
        return when (nivelActual) {
            NIVEL_1 -> R.drawable.camino_brillante_nivel1
            NIVEL_2 -> R.drawable.camino_brillante_nivel2
            NIVEL_3 -> R.drawable.camino_brillante_nivel3
            NIVEL_4 -> R.drawable.camino_brillante_nivel4
            NIVEL_5 -> R.drawable.camino_brillante_nivel5
            NIVEL_6 -> R.drawable.camino_brillante_nivel6
            else -> throw IllegalArgumentException("Id de nivel no existente")
        }
    }

    fun getResumenContenidoCoroutine(traerVideo: Boolean = false) {
        GlobalScope.launch(Dispatchers.IO) {
            try{
                userUseCase.getUser()?.let { user ->
                    val documento = user.numeroDocumento?.let {
                        if (it.isEmpty())
                            GlobalConstant.ZERO_STRING
                        else
                            user.numeroDocumento
                    } ?: kotlin.run { GlobalConstant.ZERO_STRING }

                    val request = ResumenRequest()

                    request.campaing = Integer.parseInt(user.campaing)
                    request.codeRegion = user.regionCode
                    request.codeSection = user.codigoSeccion
                    request.codeZone = user.zoneCode
                    request.idContenidoDetalle = 0
                    request.indConsulDig = (user.indicadorConsultoraDigital).toString()
                    request.numeroDocumento = documento
                    request.primerNombre = user.primerNombre
                    request.primerApellido = user.primerApellido
                    request.fechaNacimiento = user.fechaNacimiento
                    request.correo = user.email

                    val contenidoResumen = accountUseCase.getResumenConfigCoroutine(request)
                    GlobalScope.launch(Dispatchers.Main) {
                        gestionarContenidoResumen(traerVideo, contenidoResumen)
                    }
                }
            } catch (e: Exception) {
                BelcorpLogger.e(e)
            }
        }
    }

    fun gestionarContenidoResumen(traerVideo: Boolean, resumen: BasicDto<Collection<ContenidoResumen>>) {

            val listaResumen = resumen.data
            var item: ContenidoResumen?
            when ((showInContainer).toString()) {

                TypeContentHeader.STORIES -> {
                        val item1 = find(GlobalConstant.TAG_STORIES, listaResumen)
                        if (item1 != null) {
                            history = StorieModelMapper.transform(item1)
                            history?.let { story ->
                                homeView?.onStoriesToThumbnail(story)
                            }
                        }
                }
                TypeContentHeader.CARRUSEL_IMAGE -> {
                    item = find(GlobalConstant.TAG_BANNER_LANZAMIENTO, listaResumen)
                    if (item != null) {
                        banner = BannerModelMapper.transform(item)
                        banner?.contenidoDetalle?.filterNotNull()?.let {
                            processMarquees(BannerModelMapper.transformMarquees(it))
                        }
                    }
                }
                else -> {
                    //Not required
                }
            }

            item = find(GlobalConstant.TAG_GANA_EN_CLICK, listaResumen)
            if (item != null) {
                val winOnClick = WinOnClickModelDataMapper.transform(item)
                homeView?.setWinOnClickVideo(winOnClick)
            } else {
                homeView?.setWinOnClickVideo(null)
            }
            if (traerVideo) {
                item = find(GlobalConstant.TAG_VIDEO_HOME, listaResumen)

                if (item != null) {
                    //Busca si tiene el video
                    item.contenidoDetalle?.find { it.codigoDetalleResumen.equals(GlobalConstant.TAG_VIDEO_NAVIDAD) }?.let {
                        if (!it.visto){ //si aun no se ha isVisto, muestra el video de navidad
                            homeView?.showNavidadVideo(it.urlDetalleResumen, it.idContenido)
                        }else{
                            gestionarDialogSiguienteCampania()
                        }
                    }
                }else{
                    gestionarDialogSiguienteCampania()
                }
            }else{
                gestionarDialogSiguienteCampania()
            }

            item = find(GlobalConstant.TAG_MEDIDOR_DE_SUENIO, listaResumen)
            if (item != null) {

                val medidorOnClick = DreamMeterModelDataMapper.transform(item)
                loadDreamMeter(medidorOnClick)
            } else {
                homeView?.setDreamMeterOn(null)
            }


            val brandConfig = find(GlobalConstant.TAG_BRANDING_CONFIG, listaResumen)
            if (brandConfig != null) {
                if (brandConfig.contenidoDetalle?.isEmpty() == true) {
                    homeView?.clearBrandConfig()
                } else {
                    val brandConfigModel = brandConfigModelDataMapper.transform(brandConfig)
                    homeView?.setBrandingConfig(brandConfigModel)
                }
            } else {
                homeView?.clearBrandConfig()
            }

    }

    fun getListStories() {
        history?.let { storie ->
            homeView?.onListStoriesObtained(storie)
        }
    }

    fun evaluateShowRateDialog() {
        GlobalScope.launch(Dispatchers.IO) {

            val minCountViewHome = 2
            val sessionVersionCode = sessionUseCase.getVersionCode()
            val nowVersionCode = BuildConfig.VERSION_CODE
            val countViewHome = sessionUseCase.getCountViewHome().plus(1)
            val isRate = sessionUseCase.getIsRate()

            if (!isRate && sessionVersionCode != nowVersionCode) {
                if (countViewHome >= minCountViewHome) {
                    GlobalScope.launch(Dispatchers.Main) {
                        homeView?.showRateDialog()
                    }
                } else {
                    sessionUseCase.updateCountViewHome(countViewHome)
                }
            }
        }
    }

    fun updateStateIsRate(state: Boolean = false) {
        GlobalScope.launch {
            sessionUseCase.updateCountViewHome(0)
            sessionUseCase.updateIsRate(state)
            sessionUseCase.updateVersionCode(BuildConfig.VERSION_CODE)
        }
    }

    /** */

    private inner class SaveSearchPromptObserver : BaseObserver<Boolean>()

    private inner class UpdateNotificationStatus : BaseObserver<Boolean>()

    private inner class CheckSearchPromptObserver : BaseObserver<Boolean>() {
        override fun onNext(check: Boolean) {

            if (!check) {
                homeView?.showSearchPrompt()
            }
        }
    }

    private inner class RefreshData : BaseObserver<Login?>() {
        override fun onNext(t: Login?) {
            homeView?.let { hmv ->
                t?.let { login ->
                    showUserData(login, true)
                    updateOffersCount(login)
                    getUsabilityConfig(login)
                    hmv.hideLoading()
                    hmv.goToReferedOption()
                    hmv.checkForGanaMasRequest()

                    userUseCase.getCupon(GetCupon(login, true))

                    if (login.exception == null || login.userType == UserType.POSTULANTE) {
                        login.campaing?.let {
                            clienteUseCase.bajada(it, GetClientObserver())
                        }
                    }

                }
            } ?: kotlin.run { return }
        }

        override fun onError(exception: Throwable) {
            homeView?.let { hmv ->
                hmv.showEmptyCards(null)
                hmv.hideLoading()
                if (exception is VersionException) {
                    val vE = exception as VersionException
                    hmv.onVersionError(vE.isRequiredUpdate, vE.url)
                } else if (exception is ConsultantApprovedException) {
                    hmv.consultantApprovedClose()
                }
            } ?: kotlin.run { return }
        }
    }

    private inner class GetClientObserver : BaseObserver<Collection<Cliente?>?>() {

        override fun onNext(result: Collection<Cliente?>?) {
            homeView?.let { hmv ->
                hmv.getNotificationesCliente()
            }

        }

        override fun onError(exception: Throwable) {
            // EMPTY
        }
    }

    private inner class GetNotificacionesClienteObserver : BaseObserver<List<NotificacionCliente?>?>() {

        override fun onNext(t: List<NotificacionCliente?>?) {
            t?.let { result ->
                homeView?.generateNotificationesCliente(notificacionClienteModelDataMapper.transform(result))
                getNotificationsRecordatorio()
            }

        }

    }

    private inner class GetNotificacionesRecordatorioObserver : BaseObserver<List<NotificacionRecordatorio?>?>() {

        override fun onNext(t: List<NotificacionRecordatorio?>?) {
            t?.let { result ->
                homeView?.generateNotificationesRecordatorio(notificacionRecordatorioModelDataMapper.transform(result))
            }

        }
    }

    private inner class GetUserScreenTrack : BaseObserver<Login?>() {

        override fun onNext(t: Login?) {
            homeView?.let { hmv ->
                t?.let { log ->
                    hmv.initScreenTrack(loginModelDataMapper.transform(log))
                }

            }
        }
    }

    private inner class GetUser constructor(private val isOnline: Boolean?) : BaseObserver<Login?>() {
        override fun onNext(t: Login?) {
            t?.let {
                this@HomePresenter.loginModel = it
                isOnline?.let { online ->
                    accountUseCase.getUserResumen(it, UserResumenObserver(online))
                }
            }
        }
    }

    private inner class GetToken : BaseObserver<Device?>() {

        internal lateinit var device: Device

        override fun onNext(t: Device?) {
            t?.let {
                device = it
                homeView?.let { hv ->
                    hv.checkToken(device)

                }
            } ?: kotlin.run { return }
        }

        override fun onError(exception: Throwable) {
            homeView?.let {
                it.checkToken(null)
            } ?: kotlin.run { return }
        }
    }

    private inner class TokenResult : BaseObserver<Boolean?>()

    private inner class Logout : BaseObserver<Auth?>() {
        override fun onNext(t: Auth?) {
            homeView?.let {
                t?.let { au ->
                    it.authenticated(authModelDataMapper.transform(au))
                }
            } ?: kotlin.run { return }
        }
    }

    private inner class StatusObserver : BaseObserver<Boolean>() {

        override fun onNext(state: Boolean) {
            homeView?.let { hv ->
                if (state) {
                    hv.showLoading()
                    refreshData()
                } else {
                    hv.checkForGanaMasRequest()
                }
            } ?: kotlin.run {
                return
            }
        }

        override fun onError(exception: Throwable) {

            homeView?.onError(exception)

        }
    }

    private inner class GiftGenericSetStatus : BaseObserver<Boolean>() {
        override fun onNext(t: Boolean) {
            super.onNext(t)
            sessionUseCase.saveStatusGiftToolTip(true, GiftGenericSetTootipStatus())
        }
    }


    private inner class GiftGenericSetTootipStatus : BaseObserver<Boolean>()

    private inner class UserCountryObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            homeView?.let { hv ->
                t?.destinatariosFeedback?.let {
                    hv.sendFeedBack(it)
                    hv.hideLoading()
                }
            }
        }

        override fun onError(exception: Throwable) {
            homeView?.let { hv ->
                hv.hideLoading()
                hv.onError(exception)
            }
        }
    }

    private inner class UserPropertyObserver : BaseObserver<User?> {

        private val menuType: Int
        private val eventLabel: String
        private val eventName: String
        private val screenName: String
        private var action: String? = null

        constructor(menuType: Int, eventLabel: String, eventName: String, screenName: String) {
            this.menuType = menuType
            this.eventLabel = eventLabel
            this.eventName = eventName
            this.screenName = screenName
        }

        constructor(menuType: Int, eventLabel: String, eventName: String, screenName: String, action: String) {
            this.menuType = menuType
            this.eventLabel = eventLabel
            this.eventName = eventName
            this.screenName = screenName
            this.action = action
        }

        override fun onNext(t: User?) {
            action?.let { act ->
                t?.let { usr ->
                    homeView?.trackEvent(loginModelDataMapper.transform(usr), menuType, eventLabel, eventName, screenName, act)
                }

            } ?: kotlin.run {
                t?.let { usr ->
                    homeView?.trackEvent(loginModelDataMapper.transform(usr), menuType, eventLabel, eventName, screenName)
                }

            }
        }
    }

    private inner class BackPressedPropertyObserver constructor(private val screenName: String) : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let { user ->
                homeView?.trackBackPressed(loginModelDataMapper.transform(user), screenName)
            }

        }
    }

    private inner class MenuPropertyObserver constructor(private val screenName: String) : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let { user ->
                homeView?.trackMenu(loginModelDataMapper.transform(user), screenName)
            }

        }
    }

    private inner class UpdateMenuObserver : BaseObserver<Boolean?>() {
        override fun onComplete() {
            homeView?.reloadMenu()
        }
    }

    private inner class CheckBirthdayByYearObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showBirthday()
        }
    }

    private inner class UpdateBirthdayByYearObserver : BaseObserver<Boolean>()

    private inner class CheckAnniversaryByYearObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showAnniversary()

        }
    }

    private inner class UpdateAnniversaryByYearObserver : BaseObserver<Boolean>()

    private inner class CheckChristmasByYearObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showChristmas()
        }
    }

    private inner class UpdateChristmasByYearObserver : BaseObserver<Boolean>()

    private inner class CheckNewYearByYearObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showNewYear()

        }
    }

    private inner class UpdateNewYearByYearObserver : BaseObserver<Boolean>()

    private inner class SaveUsabilityConfigObserver : BaseObserver<Boolean>()

    private inner class GetUsabilityConfigObserver internal constructor(private val login: Login) : BaseObserver<String>() {

        override fun onNext(t: String) {
            remoteConfigDataMapper.transform(t)?.let {
                homeView?.setLogAccess(it, login)
            }
        }
    }

    private inner class CheckConsultantDayByYearObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showConsultantDay()
        }
    }

    private inner class UpdateConsultantDayByYearObserver : BaseObserver<Boolean>()

    private inner class CheckPasoSextoPedidoObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showPasoSextoPedido()
        }
    }

    private inner class CheckBelcorpFiftyObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showBelcorpFifty()
        }
    }

    private inner class CheckPostulantObserver : BaseObserver<Boolean>() {
        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.showPostulant()
        }
    }

    private inner class CheckNewConsultantObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (!t)
                homeView?.consultantApproved()
        }
    }

    private inner class UpdatePasoSextoPedidoObserver : BaseObserver<Boolean>()

    private inner class UpdateBelcorpFiftyObserver : BaseObserver<Boolean>()

    private inner class UpdatePostulantObserver : BaseObserver<Boolean>()

    private inner class UpdateNewConsultantObserver : BaseObserver<Boolean>()

    private inner class GetCupon internal constructor(internal var login: Login, internal var isOnline: Boolean) : BaseObserver<String>() {
        override fun onNext(cuponCampaign: String) {

            homeView?.let { hv ->

                login.photoProfile?.let {
                    hv.setPhoto(it)
                } ?: kotlin.run { hv.setPhoto(null) }


                var newConsultora = false

                if (login.exception != null && login.exception is ConsultantApprovedException) {
                    newConsultora = true
                }

                if (cuponCampaign.isEmpty() && login.tipoCondicion != 0 && (login.cuponEstado == CuponType.ACTIVADO)) {
                    hv.showCupon()

                    var urlProfile: String? = null
                    login.photoProfile?.let {
                        if (it.isNotEmpty()) {
                            urlProfile = it
                        }
                    }

                    homeView?.showHeaderNavData(newConsultora,
                        login.userType ?: 0,
                        login.isBirthday ?: false,
                        login.isAnniversary ?: false,
                        login.alias ?: "",
                        login.consultantCode ?: "",
                        login.isPasoSextoPedido ?: false,
                        urlProfile ?: "",
                        true,
                        false)


                } else if (ConsultorasApp.getInstance().datamiType == NetworkEventType.DATAMI_AVAILABLE) {
                    userUseCase.checkStatusDatamiMessage(CheckStatusDatamiMessage(login, isOnline))
                } else {
                    var urlProfile: String? = null
                    login.photoProfile?.let {
                        if (it.isNotEmpty()) {
                            urlProfile = it
                        }
                    }

                    homeView?.showHeaderNavData(newConsultora,
                        login.userType ?: 0,
                        login.isBirthday ?: false,
                        login.isAnniversary ?: false,
                        login.alias ?: "",
                        login.consultantCode ?: "",
                        login.isPasoSextoPedido ?: false,
                        urlProfile ?: "",
                        false,
                        false)

                }

                try {
                    login.campaing?.let {
                        if (it != cuponCampaign) {
                            userUseCase.updateCupon(it, BaseObserver())
                            if (cuponCampaign.isNotEmpty()) {
                                GlobalScope.launch(Dispatchers.IO) {
                                    offerUseCase.deleteAllRecentOffers()
                                    offerUseCase.deleteOfertaFinal()
                                }
                            }

                        }
                    }
                } catch (e: Exception) {
                }


            }
        }
    }


    private inner class CheckStatusDatamiMessage internal constructor(internal var login: Login, internal var isOnline: Boolean) : BaseObserver<Boolean>() {

        override fun onNext(status: Boolean) {

            var newConsultora = false

            if (login.exception != null && login.exception is ConsultantApprovedException) {
                newConsultora = true
            }

            if (!status) {
                var urlProfile: String? = null
                login.photoProfile?.let {
                    if (it.isNotEmpty()) {
                        urlProfile = it
                    }
                }

                homeView?.showHeaderNavData(newConsultora,
                    login.userType ?: 0,
                    login.isBirthday ?: false,
                    login.isAnniversary ?: false,
                    login.alias ?: "",
                    login.consultantCode ?: "",
                    login.isPasoSextoPedido ?: false,
                    urlProfile ?: "",
                    false,
                    true)
                homeView?.showDatamiMessage()

            } else {
                var urlProfile: String? = null
                login.photoProfile?.let {
                    if (it.isNotEmpty()) {
                        urlProfile = it
                    }
                }

                homeView?.showHeaderNavData(newConsultora,
                    login.userType ?: 0,
                    login.isBirthday ?: false,
                    login.isAnniversary ?: false,
                    login.alias ?: "",
                    login.consultantCode ?: "",
                    login.isPasoSextoPedido ?: false,
                    urlProfile ?: "",
                    false,
                    false)
            }
        }
    }

    private inner class EventPropertyObserver constructor(internal val screenHome: String,
                                                          internal val eventCat: String,
                                                          internal val eventAction: String,
                                                          internal val eventLabel: String,
                                                          internal val eventName: String,
                                                          internal val logout: Boolean) : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let { user ->
                Tracker.trackEvent(screenHome,
                    eventCat,
                    eventAction,
                    eventLabel,
                    eventName,
                    loginModelDataMapper.transform(user))
                if (logout) authUseCase.logoutWithData(Logout())
            }
        }
    }

    private inner class EnableSDKObserver : BaseObserver<Boolean?>() {

        internal var kinesisModel: KinesisModel? = null

        override fun onNext(t: Boolean?) {
            t?.let { enable ->
                kinesisModel?.let {
                    homeView?.enableSDK(enable, it)
                }

            }

        }
    }

    private inner class GetMenuObserver : BaseObserver<Menu?>() {

        override fun onNext(t: Menu?) {
            homeView?.let { hv ->
                t?.let {
                    menuModelDataMapper.transform(it)?.let { menu ->
                        hv.setMenuModel(menu)
                    }
                }
            } ?: kotlin.run { return }
        }
    }

    private inner class ChatBotObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            try {
                t?.let { user ->
                    homeView?.let { hv ->
                        val token = JwtEncryption.newInstance().encrypt(
                            biz.belcorp.consultoras.data.util.Constant.SECRET, ("{" +
                            "\"CodigoIso\":\"" + user.countryISO + "\"," +
                            "\"Token\":\"" +
                            AesEncryption.newInstance().encrypt(
                                FirebaseRemoteConfig.getInstance().getString(BuildConfig.REMOTE_CONFIG_SECRET), ("{" +
                                "\"Documento\":\"" + user.numeroDocumento + "\"," +
                                "\"TipoDocumento\":\"1\"," +
                                "\"key\":\"" + DateUtil.convertNowToTicks() + "\"}")) + "\"," +
                            "\"TipoAutentificacion\":4}"))
                        hv.openChatBot(FirebaseRemoteConfig.getInstance().getString(BuildConfig.REMOTE_CONFIG_CHATBOT) + token)
                    }
                }

            } catch (e: Exception) {
                BelcorpLogger.d("Error", "Problema al cargar el ChatBot", e)
            }
        }
    }

    private inner class UpdateMailObserver : BaseObserver<Boolean>() {

        override fun onNext(t: Boolean) {
            if (t) {
                homeView?.showUpdateMailDialog()
            }
        }
    }

    private inner class IntrigueObserver internal constructor(private val campaign: String, private val isIntrigueShowing: Boolean?) : BaseObserver<Collection<UserConfigData?>?>() {

        override fun onNext(userConfigData: Collection<UserConfigData?>?) {

            isIntrigueShowing?.let { intrigue ->
                if (!intrigue) {
                    userConfigData?.filterNotNull()?.let { usrConf ->
                        userUseCase.checkIntrigueStatus(usrConf, campaign, object : BaseObserver<IntrigueBody>() {
                            override fun onNext(intrigueBody: IntrigueBody) {
                                if (intrigueBody.isShow) {
                                    sessionUseCase.updateIntrigueStatus(true, object : BaseObserver<Boolean>() {
                                        override fun onNext(aBoolean: Boolean) {
                                            intrigueBody.image?.let {
                                                homeView?.showIntrigueDialog(it)
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    }
                }
            }
        }
    }

    private inner class RenewObserver internal constructor(private val campaign: String, private val isRenewShowing: Boolean?) : BaseObserver<Collection<UserConfigData?>?>() {

        override fun onNext(userConfigData: Collection<UserConfigData?>?) {

            isRenewShowing?.let { renew ->
                if (!renew) {

                    userConfigData?.filterNotNull()?.let { usrConfig ->
                        userUseCase.checkRenewStatus(usrConfig, campaign, object : BaseObserver<RenewBody>() {
                            override fun onNext(renewBody: RenewBody) {
                                if (renewBody.isShow) {
                                    sessionUseCase.updateRenewStatus(true, object : BaseObserver<Boolean>() {
                                        override fun onNext(aBoolean: Boolean) {
                                            renewBody.image?.let { img ->
                                                renewBody.message?.let { msg ->
                                                    homeView?.showRenewDialog(renewBody.image, renewBody.imagelogo, renewBody.message)
                                                }
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    }
                }
            }
        }
    }

    private inner class CheckIntrigueStatusObserver internal constructor(private val campaign: String) : BaseObserver<Boolean>() {

        override fun onNext(aBoolean: Boolean) {
            aBoolean.let {
                accountUseCase.getConfig(UserConfigAccountCode.INTRIGA, IntrigueObserver(campaign, it))
            }

        }
    }

    private inner class UserResumenObserver(private val isOnline: Boolean) : BaseObserver<Login?>() {
        override fun onNext(t: Login?) {
            t?.let { log ->
                showInContainer = log.mostrarEnBanner
                showUserData(log, isOnline, true)
            }
        }

        override fun onError(exception: Throwable) {
            loginModel?.let {
                showUserData(it, isOnline, true)
            }
        }
    }

    private inner class MarqueeRequestListener(marqueeItems: List<MarqueeItem>) : RequestListener<Bitmap> {

        private val marqueeItems: ArrayList<MarqueeItem>
        private var marqueeChecker: Int = 0


        init {
            this.marqueeItems = marqueeItems as ArrayList<MarqueeItem>
            marqueeChecker = 0
        }

        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
            marqueeChecker++
            if (marqueeItems.size == marqueeChecker) {
                val result = ArrayList<MarqueeItem>()
                for (marquee in marqueeItems) {
                    if (marquee.image != null)
                        result.add(marquee)
                }
                if (result.size > 0) {
                    homeView?.showBannerLanzamiento(result)
                }
            }
            return false
        }

        override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {

            for (item in marqueeItems) {
                if (item.urlImage == model) {
                    item.image = resource
                }
            }

            marqueeChecker++
            if (marqueeItems.size == marqueeChecker) {
                val result = ArrayList<MarqueeItem>()
                for (marquee in marqueeItems) {
                    if (marquee.image != null)
                        result.add(marquee)
                }
                if (result.size > 0) {
                    homeView?.showBannerLanzamiento(result)
                }
            }
            return false
        }
    }

    private inner class CheckRenewStatusObserver internal constructor(private val campaign: String) : BaseObserver<Boolean>() {

        override fun onNext(aBoolean: Boolean) {
            accountUseCase.getConfig(UserConfigAccountCode.INTRIGA, RenewObserver(campaign, aBoolean))
        }
    }

    private inner class UserObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            t?.let {
                userTrack = it
            }
        }
    }


    private inner class NotificationCounter : BaseObserver<Long>() {

        override fun onNext(cantidad: Long) {

            homeView?.showNewNotification(cantidad.toInt())
        }

        override fun onError(exception: Throwable) {
            homeView?.onError(exception)
        }
    }

    private fun find(tag: String, listaResumen: Collection<ContenidoResumen>?): ContenidoResumen? = listaResumen?.find { it.codigoResumen == tag }

    private fun processMarquees(marqueeItems: List<MarqueeItem>) {

        val marqueeRequestListener = MarqueeRequestListener(marqueeItems)
        for (marqueeItem in marqueeItems) {
            Glide.with(homeView?.activityContext()).asBitmap().load(marqueeItem.urlImage).listener(marqueeRequestListener).submit()
        }
    }
    fun insertHomologado(product: ProductCUV, identifier: String){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result =  orderUseCase.insertarPedido(product, identifier,
                    false, -1, product.clienteId ?: 0)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        homeView?.hideLoading()
                        when(it.code) {
                            SearchCUVCode.ERROR_PRODUCTO_FESTIVAL_ALCANZADO ->{
                                homeView?.errorAddingProduct(it.message)
                            }
                            SearchCUVCode.OK -> {
                                homeView?.onProductAdded(it.message)
                            }
                            SearchCUVCode.ERROR_CANTIDAD_EXCEDIDA -> {
                                homeView?.errorAddingProduct(it.message)
                            }
                            SearchCUVCode.ERROR_PRODUCTO_DUOPERFECTO_LIMITE -> {
                                homeView?.errorAddingProduct(it.message)
                            }
                            SearchCUVCode.ERROR_PRODUCTO_CANTIDAD_LIMITE -> {
                                homeView?.errorAddingProduct(it.message)
                            }
                            SearchCUVCode.ERROR_PRODUCTO_ALCANCE_PREMIO_FEST -> {
                                homeView?.errorAddingProduct(it.message)
                            }
                            else -> {
                                homeView?.errorAddingProduct(it.message)
                            }
                        }
                    }
                }
            }catch (e: Exception){
                GlobalScope.launch(Dispatchers.Main) {
                    homeView?.hideLoading()
                    when(e){
                        is VersionException -> homeView?.onVersionError(e.isRequiredUpdate, e.url)
                        else -> homeView?.errorAddingProduct(null)
                    }
                }

            }
        }
    }

    private inner class CUVObserver : BaseObserver<BasicDto<ProductCUV>?>() {

        override fun onNext(t: BasicDto<ProductCUV>?) {
            t?.let {
                when {
                    t.code == SearchCUVCode.OK -> {

                        it.data?.let { product ->


                            homeView?.run {
                                    product.cantidad = 1
                                    val identifier = DeviceUtil.getId(this.context())
                                    product.identifier = identifier
                                    product.clienteId = 0
                                    product.clienteLocalId = 0
                                    insertHomologado(product, identifier)
                            }

                        }
                    }
                    t.code == SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_ESIKA
                        || t.code == SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_LBEL
                    -> {
                        homeView?.hideLoading()
                        homeView?.errorAddingProduct(it.message)
                    }
                    t.code == SearchCUVCode.ERROR_PRODUCTO_NOEXISTE -> {
                        homeView?.hideLoading()
                        homeView?.errorAddingProduct(null)
                    }
                    t.code == SearchCUVCode.ERROR_PRODUCTO_SUGERIDO ||
                        t.code == SearchCUVCode.ERROR_PRODUCTO_AGOTADO -> {
                        it.data?.let { d ->
                            homeView?.hideLoading()
                            homeView?.errorAddingProduct(it.message)
                        }
                    }
                    else -> {
                        homeView?.hideLoading()
                        homeView?.errorAddingProduct(null)
                    }
                }
            }
        }
    }


    companion object {

        private val TAG = "HomePresenter"
    }
}
