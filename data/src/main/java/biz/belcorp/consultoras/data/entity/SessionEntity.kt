package biz.belcorp.consultoras.data.entity

import biz.belcorp.library.util.StringUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


class SessionEntity : Serializable {

    @SerializedName("countrySIM")
    var countrySIM: String? = null

    @SerializedName("authType")
    var authType: Int? = 0

    @SerializedName("username")
    var username: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("tokenType")
    var tokenType: String? = null

    @SerializedName("accessToken")
    var accessToken: String? = null

    @SerializedName("refreshToken")
    var refreshToken: String? = null

    @SerializedName("expiresIn")
    var expiresIn: Long? = 0

    @SerializedName("issued")
    var issued: String? = null

    @SerializedName("expires")
    var expires: String? = null

    @SerializedName("tutorial")
    var isTutorial: Boolean? = false

    @SerializedName("isLogged")
    var isLogged: Boolean? = false

    @SerializedName("started")
    var started: Long? = 0

    @SerializedName("updated")
    var updated: Long? = 0

    @SerializedName("version")
    var version: String? = null

    @SerializedName("aceptaTerminosCondiciones")
    var isAceptaTerminosCondiciones: Boolean? = false

    @SerializedName("appName")
    var appName: String? = null

    @SerializedName("syncStatus")
    var isSyncStatus: Boolean? = false

    @SerializedName("callMenu")
    var isCallMenu: Boolean? = false

    @SerializedName("viewsByMenu")
    var viewsByMenu: Int? = 0

    @SerializedName("viewsByCliente")
    var viewsByCliente: Int? = 0

    @SerializedName("viewsByDeuda")
    var viewsByDeuda: Int? = 0

    @SerializedName("birthdays")
    var birthdays = HashMap<Int?, Boolean?>()

    @SerializedName("anniversaries")
    var anniversaries = HashMap<Int?, Boolean?>()

    @SerializedName("christmases")
    var christmases = HashMap<Int?, Boolean?>()

    @SerializedName("newYears")
    var newYears = HashMap<Int?, Boolean?>()

    @SerializedName("counter")
    var counter = HashMap<String?, Int?>()

    @SerializedName("consultantDays")
    var consultantDays = HashMap<Int?, Boolean?>()

    @SerializedName("pasoSextoPedido")
    var isPasoSextoPedido: Boolean? = false

    @SerializedName("belcorpFifty")
    var isBelcorpFifty: Boolean? = false

    @SerializedName("postulant")
    var postulant: Boolean? = false

    @SerializedName("newConsultant")
    var newConsultant: Boolean? = false

    @SerializedName("campaign")
    var campaign: String? = null

    @SerializedName("datamiMessageView")
    var datamiMessageView: Boolean? = false

    @SerializedName("usagePermission")
    var usagePermission: Boolean? = false

    @SerializedName("usabilityConfig")
    var usabilityConfig: String? = null

    @SerializedName("searchPrompt")
    var searchPrompt: Boolean? = false

    @SerializedName("notificationStatus")
    var isNotificationStatus: Boolean? = false

    @SerializedName("tipoIngreso")
    var tipoIngreso: String? = null

    @SerializedName("giftAnimation")
    var isWasShowGiftAnimation: Boolean = false

    @SerializedName("messageTooltip")
    var isTooltipmessageGift: Boolean? = false

    @SerializedName("isIntrigueStatus")
    var isIntrigueStatus: Boolean? = false

    @SerializedName("isRenewStatus")
    var isRenewStatus: Boolean? = false

    @SerializedName("apiCacheEnabled")
    var apiCacheEnabled: Boolean? = null

    @SerializedName("apiCacheOnlineTime")
    var apiCacheOnlineTime: Long? = null

    @SerializedName("apiCacheOfflineTime")
    var apiCacheOfflineTime: Long? = null

    @SerializedName("ordersCount")
    var ordersCount: Int? = 0

    @SerializedName("apiCacheOfflineCaminoBrillanteTime")
    var apiCacheOfflineCaminoBrilanteTime: Long? = null

    @SerializedName("lastUpdateCaminoBrillante")
    var lastUpdateCaminoBrillante: Long? = null

    @SerializedName("imageDialogEnabled")
    var imageDialogEnabled: Boolean? = false

    @SerializedName("promotionGroupListEnable")
    var promotionGroupListEnable: Boolean? = false

    @SerializedName("hide_brand_and_amount_client")
    var hideViewsGridGanaMas: Boolean? = false

    @SerializedName("order_configurable_lever")
    var orderConfigurableLever: String? = null

    @SerializedName("ab_testing_bonificaciones")
    var abtestingBonificaciones: String? = null

    @SerializedName("imagesMaxFicha")
    var imagesMaxFicha: Long? = 0

    @SerializedName("apiClientId")
    var apiClientId: String? = null

    @SerializedName("apiClientSecret")
    var apiClientSecret: String? = null

    @SerializedName("oAccessToken")
    var oAccessToken: String? = null

    @SerializedName("expanded_searchview")
    var expandedSearchviewGanaMas: Boolean? = false

    @SerializedName("download_catalog_pdf")
    var downloadCatalogPdf: Boolean? = false

    @SerializedName("status_viewed_dialog_campain")
    var viewdDialogNextCampain: Boolean? = false

    @SerializedName("actual_campaing")
    var campaniaActual: String = StringUtil.Empty

    @SerializedName("isMultiOrder")
    var isMultiOrder: Boolean? = false

    @SerializedName("mover_barra_navegacion")
    var moverBarraNavegacion: Boolean? = false

    @SerializedName("versionCode")
    var versionCode: Int? = 0

    @SerializedName("isRate")
    var isRate: Boolean? = false

    @SerializedName("countViewHome")
    var countViewHome: Int? = 0

    @SerializedName("featureFlagOfertaFinal")
    var featureFlagOfertaFinal: Boolean? = false

    override fun toString(): String {
        return "{" +
                "Username = " + username +
                "Token = " + accessToken +
                "Version = " + version +
                "AppName = " + appName +
                "}"
    }
}
