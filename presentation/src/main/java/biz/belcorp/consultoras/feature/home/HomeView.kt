package biz.belcorp.consultoras.feature.home

import android.content.Context

import java.text.DecimalFormat

import biz.belcorp.consultoras.base.View
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
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.feature.home.marquee.MarqueeItem

interface HomeView : View, LoadingView {

    fun authenticated(authModel: AuthModel)

    fun notifyRefreshDataError(exception: Throwable)

    fun setDecimalFormatISO(decimalFormatISO: DecimalFormat)

    fun setCountryISO(countryISO: String)

    fun showCupon()

    fun setRevistaDigitalSuscripcion(userType: Int, tieneGND: Boolean, revistaDigitalSuscripcion: Int, urlBannerGanaMas: String, ganaMasNativo: Boolean, esSuscrita: Boolean)

    fun setCountryMoneySymbol(countryMoneySymbol: String)

    fun setUserType(userType: Int)

    fun setCampaign(campaign: String, endDay: String, endHour: String, days: Int, pagoEnLinea: Boolean,
                    fechaVencimientoPago: String, loginDetail: LoginDetail, isMultiOrder : Boolean,
                    lineaConsultora : String?)

    fun showIncentives(cardEntity: CardEntity, countryISO: String)

    fun showClients(cardEntity: CardEntity)

    fun showOrders(cardEntity: CardEntity)

    fun showOrdersCount(count: Int?)

    fun showOffers(cardEntity: CardEntity)

    fun showCatalog(cardEntity: CardEntity)

    fun showChat(cardEntity: CardEntity)

    fun onError(e: Throwable)

    fun setUserData(model: LoginModel)

    fun initScreenTrack(model: LoginModel)

    fun sendFeedBack(email: String)

    fun showEmptyCards(countryISO: String?)

    fun trackEvent(transform: LoginModel, menuType: Int, eventLabel: String, eventName: String, screenName: String)

    fun trackEvent(screenHome: String,
                   eventCatHomeOption: String,
                   eventActionHomeOption: String,
                   eventLabelSecondaryMenu: String,
                   eventNameHomeOption: String,
                   model: LoginModel)

    fun trackBackPressed(transform: LoginModel, screenName: String)

    fun trackMenu(transform: LoginModel, screenName: String)

    fun goToTerms(menuType: Int, urlTerminos: String)

    fun setPhoto(urlImage: String?)

    fun checkToken(device: Device?)

    fun showAnniversary()

    fun showBirthday()

    fun reloadMenu()

    fun showChristmas()

    fun showNewYear()

    fun showConsultantDay()

    fun getNotificationesCliente()

    fun generateNotificationesCliente(transform: List<NotificacionClienteModel>)

    fun generateNotificationesRecordatorio(transform: List<NotificacionRecordatorioModel>)

    fun showPasoSextoPedido()

    fun showBelcorpFifty()

    fun showPostulant()

    fun showHeaderNavData(newConsultora: Boolean,userType :Int,birthday: Boolean ,anniversary: Boolean ,consultantName: String,
                          consultantCode: String ,sixth: Boolean ,imageUrl: String ,isCupon :Boolean,isDatamiMessage: Boolean)


    fun showDatamiMessage()

    fun consultantApproved()

    fun consultantApprovedClose()

    fun setLogAccess(kinesisModel: KinesisModel, login: Login)

    fun enableSDK(enable: Boolean, kinesisModel: KinesisModel)

    fun setMenuModel(menu: MenuModel)

    fun showSearchPrompt()

    fun openChatBot(url: String)

    fun trackEvent(transform: LoginModel, menuType: Int, eventLabel: String, eventName: String, screenName: String, action: String)

    fun showChatbot(chatBot: Boolean?)

    fun showNewNotification(quantity: Int)

    fun gotToMethodPay(item: UserConfigData?)

    fun showBannerLanzamiento(marquees: List<MarqueeItem>)

    fun showCaminoBrillante(resDrawableMedalla: Int)

    fun showUpdateMailDialog()

    fun showIntrigueDialog(image: String)

    fun showRenewDialog(image: String?, imageLogo: String?, message: String?)

    fun goToReferedOption()

    fun onStoriesToThumbnail(storieModel: StorieModel)

    fun onListStoriesObtained(history: StorieModel)

    fun setWinOnClickVideo(winOnClickModel: WinOnClickModel?)

    fun setDreamMeterOn(dreamMederModel: DreamMederModel?)

    fun checkForGanaMasRequest()

    fun showHome()

    fun setBrandingConfig(brandConfigModel: BrandConfigModel)

    fun clearBrandConfig()

    fun goToLaunchProduct(action: String?)

    fun redirectToAction(action: String?)

    fun onProductAdded(message: String?)

    fun errorAddingProduct(message: String?)

    fun activityContext(): Context

    fun showMenuOption(code : String?, status : Boolean?, title : String?)

    fun showCatalogsPopUp(catalogos: List<CatalogoWrapper>, campaingNumber: String?, urlCatalogo: String)

    fun showNavidadVideo(urlDetalleResumen: String?, idContenido: String)

    fun showMultiOrderType()

    fun hideMultiOrderType()

    fun setMoverBarraNavegacion(flag: Boolean)

    fun showRateDialog()

    fun openWebBrowser(url: String){
        //nada
    }

    fun showItemMenuBlackFriday(flag: Boolean)

    fun setTitleSubcampaign(title: String)

}
