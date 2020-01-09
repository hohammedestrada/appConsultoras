package biz.belcorp.consultoras.feature.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler

import com.bumptech.glide.Glide

import java.text.DecimalFormat

import javax.inject.Inject

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.AuthModel
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
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.feature.home.marquee.MarqueeItem
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import biz.belcorp.consultoras.feature.payment.online.PayOnlineActivity
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.PagoEnLineaConfigCode
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.NetworkUtil

/**
 *
 */
/** */

open class BaseHomeFragment : BaseFragment(), HomeView {

    @Inject
    lateinit var presenterHome: HomePresenter

    internal var bhfListener: BaseHomeFragmentListener? = null

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseHomeFragmentListener) {
            bhfListener = context
        }
    }

    /** */

    override fun onInjectView(): Boolean {
        getComponent(HomeComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenterHome?.attachView(this)
    }

    fun checkNotifications() {
        this.presenterHome?.checkNewNotifications()
    }

    override fun context(): Context? {
        return null
    }

    override fun activityContext(): Context {
        return this.activity as Context
    }

    override fun authenticated(authModel: AuthModel) {

        Handler().postDelayed({
            val properties = AnalyticsUtil.getUserNullProperties()
            BelcorpAnalytics.setUserProperties(properties)

            if (authModel.isFacebook) {
                bhfListener?.onLoginFacebook()
            } else {
                bhfListener?.onLogin()
            }


        }, 800)

    }

    override fun showCatalogsPopUp(catalogos: List<CatalogoWrapper>, campaingNumber: String?, urlCatalogo: String) {
       //EMPTY
    }

    override fun notifyRefreshDataError(exception: Throwable) {
        // EMPTY
    }

    override fun setDecimalFormatISO(decimalFormatISO: DecimalFormat) {
        // EMPTY
    }

    override fun setCountryISO(countryISO: String) {
        // EMPTY
    }

    override fun showCupon() {
        // EMPTY
    }

    override fun setRevistaDigitalSuscripcion(userType: Int, tieneGND: Boolean, revistaDigitalSuscripcion: Int, urlBannerGanaMas: String, ganaMasNativo: Boolean, esSuscrita: Boolean) {
        // EMPTY
    }

    override fun setCountryMoneySymbol(countryMoneySymbol: String) {
        // EMPTY
    }

    override fun setUserType(userType: Int) {
        // EMPTY
    }

    override fun setCampaign(campaign: String, endDay: String, endHour: String, days: Int, pagoEnLinea: Boolean, fechaVencimientoPago: String, loginDetail: LoginDetail, isMultiOrder : Boolean,
                             lineaConsultora : String?) {
        // EMPTY
    }

    override fun showIncentives(cardEntity: CardEntity, countryISO: String) {
        // EMPTY
    }

    override fun showOffers(cardEntity: CardEntity) {
        // EMPTY
    }

    override fun showCatalog(cardEntity: CardEntity) {
        // EMPTY
    }

    override fun showOrders(cardEntity: CardEntity) {
        // EMPTY
    }

    override fun showOrdersCount(count: Int?) {
        // EMPTY
    }

    override fun showClients(cardEntity: CardEntity) {
        // EMPTY
    }

    override fun showChat(cardEntity: CardEntity) {
        // EMPTY
    }

    override fun setUserData(model: LoginModel) {
        // EMPTY
    }

    override fun initScreenTrack(model: LoginModel) {
        // EMPTY
    }

    override fun showNewNotification(quantity: Int) {
        if (activity != null) (activity as HomeActivity).showNotificactionIcon(quantity)
    }

    override fun gotToMethodPay(item: UserConfigData?) {
        item?.let {
            it.value1?.let { value1 ->
                if (PagoEnLineaConfigCode.FlujoCode.INTERNO.compareTo(value1) == 0) {
                    val intent = Intent(context, PayOnlineActivity::class.java)
                    startActivity(intent)
                } else if (PagoEnLineaConfigCode.FlujoCode.EXTERNO.compareTo(value1) == 0 && item.value2 != null) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(item.value2))
                    startActivity(i)
                }
            }


        }
    }

    override fun showCaminoBrillante(resDrawableMedalla: Int) {
        //EMPTY
    }

    override fun showHome() {
        //EMPTY
    }

    override fun showIntrigueDialog(image: String) {
        // EMPTY
    }

    override fun onStoriesToThumbnail(storieModel: StorieModel) {
        // EMPTY
    }

    override fun onListStoriesObtained(history: StorieModel) {
        // EMPTY
    }

    override fun showRenewDialog(image: String?, imageLogo: String?, message: String?) {
        // EMPTY
    }

    override fun sendFeedBack(email: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_feedback_subject))

        startActivity(intent)
    }

    override fun showEmptyCards(countryISO: String?) {
        // EMPTY
    }

    override fun onError(e: Throwable) {
        processError(e)
    }

    /** */

    fun logout() {
        showLoading()
        AsyncTask.execute { Glide.get(context).clearDiskCache() }
        presenterHome?.trackEvent(GlobalConstant.SCREEN_HOME,
            GlobalConstant.SCREEN_HOME,
            GlobalConstant.ACTION_LOGOUT,
            GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
            GlobalConstant.EVENT_LOGOUT, true)

    }

    fun sendFeedBack() {
        presenterHome?.sendFeedBack()
    }

    fun getUserDataOffline() {
        presenterHome?.dataOffline()
    }

    fun updateNotificationStatus(status: Boolean?) {
        presenterHome?.updateNotificationStatus(status)
    }

    override fun goToTerms(menuType: Int, urlTerms: String) {
        context?.let {
            if (!NetworkUtil.isThereInternetConnection(it)) {
                showNetworkError()
                return
            }

            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlTerms))
                startActivity(browserIntent)
            } catch (ex: ActivityNotFoundException) {
                BelcorpLogger.w("goToTerms", ex)
            }
        }


    }

    override fun setPhoto(urlImage: String?) {
        // EMPTY
    }

    override fun checkToken(device: Device?) {
        // EMPTY
    }

    override fun showAnniversary() {
        //EMPTY
    }

    override fun showBirthday() {
        //EMPTY
    }

    override fun reloadMenu() {
        // EMPTY
    }

    override fun showChristmas() {
        //EMPTY
    }

    override fun showNewYear() {
        //EMPTY
    }

    override fun showConsultantDay() {
        //EMPTY
    }

    override fun getNotificationesCliente() {
        //EMPTY
    }


    override fun generateNotificationesCliente(transform: List<NotificacionClienteModel>) {
        //EMPTY
    }

    override fun generateNotificationesRecordatorio(transform: List<NotificacionRecordatorioModel>) {
        //EMPTY
    }

    override fun showPasoSextoPedido() {
        //EMPTY
    }

    override fun showBelcorpFifty() {
        //EMPTY
    }

    override fun showPostulant() {
        //EMPTY
    }

    override fun showSearchPrompt() {
        //EMPTY
    }

    override fun openChatBot(url: String) {
        (activity as? HomeActivity)?.openChatBot(url)
    }

    override fun showHeaderNavData(newConsultora: Boolean, userType: Int, birthday: Boolean, anniversary: Boolean, consultantName: String, consultantCode: String, sixth: Boolean, imageUrl: String, isCupon: Boolean, isDatamiMessage: Boolean) {
        (activity as? HomeActivity)?.showHoliday(newConsultora, userType, birthday, anniversary, consultantName,
            consultantCode, sixth, imageUrl, isCupon, isDatamiMessage)
    }

    override fun showDatamiMessage() {
        // EMPTY
    }

    override fun consultantApproved() {
        // EMPTY
    }

    override fun consultantApprovedClose() {
        // EMPTY
    }

    override fun setLogAccess(kinesisModel: KinesisModel, login: Login) {
        // EMPTY
    }

    override fun enableSDK(enable: Boolean, kinesisModel: KinesisModel) {
        // EMPTY
    }

    override fun setMenuModel(menu: MenuModel) {
        // EMPTY
    }

    override fun goToReferedOption() {
        // EMPTY
    }

    override fun checkForGanaMasRequest() { /* Not necessary */
    }

    /** */

    fun trackEvent(menuType: Int, eventLabel: String, eventName: String, screenName: String) {
        presenterHome?.trackEvent(menuType, eventLabel, eventName, screenName)
    }

    fun trackEvent(menuType: Int, eventLabel: String, eventName: String, screenName: String, action: String) {
        presenterHome?.trackEvent(menuType, eventLabel, eventName, screenName, action)
    }


    fun trackEvent(screenHome: String, eventCat: String, eventAction: String,
                   eventLabel: String, eventName: String, logout: Boolean) {
        presenterHome?.trackEvent(screenHome, eventCat, eventAction, eventLabel, eventName, logout)
    }

    override fun trackEvent(loginModel: LoginModel,
                            menuType: Int,
                            eventLabel: String,
                            eventName: String,
                            screenName: String) {
        Tracker.Home.trackMenuOption(loginModel, menuType, eventLabel, eventName, screenName)
    }

    override fun trackEvent(screenHome: String,
                            eventCat: String,
                            eventAction: String,
                            eventLabel: String,
                            eventName: String,
                            loginModel: LoginModel) {
        Tracker.trackEvent(screenHome, eventCat, eventAction, eventLabel, eventName, loginModel)
    }

    fun trackBackPressed(screenName: String) {
        presenterHome?.trackBackPressed(screenName)
    }

    override fun trackBackPressed(loginModel: LoginModel, screenName: String) {
        Tracker.Home.trackBackPressed(loginModel, screenName)
    }

    fun trackMenu(screenName: String) {
        presenterHome?.trackMenu(screenName)
    }

    override fun trackMenu(loginModel: LoginModel, screenName: String) {
        Tracker.Home.trackMenu(loginModel, screenName)
    }

    override fun trackEvent(transform: LoginModel, menuType: Int, eventLabel: String, eventName: String, screenName: String, action: String) {
        Tracker.Home.trackMenuOption(transform, menuType, eventLabel, eventName, screenName, action)
    }

    override fun showChatbot(chatBot: Boolean?) {
        // EMPTY
    }

    override fun showBannerLanzamiento(marquees: List<MarqueeItem>) {
        //EMPTY
    }

    override fun setBrandingConfig(brand: BrandConfigModel) {
        //EMPTY
    }

    override fun showNavidadVideo(urlDetalleResumen: String?, idContenido: String) {
        //EMPTY
    }

    override fun clearBrandConfig() {
        //EMPTY
    }

    override fun goToLaunchProduct(action: String?){
        // EMPTY
    }

    override fun redirectToAction(action: String?){
        // EMPTY
    }

    override fun onProductAdded(message: String?){
        // EMPTY
    }

    override fun errorAddingProduct(message: String?){
        // EMPTY
    }

    override fun showUpdateMailDialog() {
        // EMPTY
    }

    override fun setWinOnClickVideo(winOnClickModel: WinOnClickModel?) {
        // EMPTY
    }

    override fun setDreamMeterOn(dreamMederModel: DreamMederModel?) {
        // EMPTY
    }

    override fun showMultiOrderType() {
        // Empty
    }

    override fun hideMultiOrderType() {
        // Empty
    }

    override fun showRateDialog() {
        // Empty
    }

    /** */

    override fun showLoading() {
        super.showLoading()
        (activity as? HomeActivity)?.lockBackButtons()
    }

    override fun hideLoading() {
        super.hideLoading()
        (activity as? HomeActivity)?.unlockBackButtons()
    }

    override fun showMenuOption(code: String?, status: Boolean?, title: String?) {
        //HARDCODE
        //(activity as? HomeActivity)?.unlockBackButtons()
    }

    override fun setMoverBarraNavegacion(flag: Boolean) {
        /* Empty */
    }

    /** */
    internal interface BaseHomeFragmentListener {
        fun onLogin()
        fun onLoginFacebook()
    }

    override fun showItemMenuBlackFriday(flag: Boolean) {
        /* Empty */
    }

    override fun setTitleSubcampaign(title: String) {
        /* Empty */
    }
}
