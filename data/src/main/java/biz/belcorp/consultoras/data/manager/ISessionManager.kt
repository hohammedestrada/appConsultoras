package biz.belcorp.consultoras.data.manager

import android.graphics.Bitmap
import biz.belcorp.consultoras.data.entity.SessionEntity
import biz.belcorp.library.net.AccessToken

interface ISessionManager {

    val isAuthenticated: Boolean?

    val country: String?

    val countrySIM: String?

    val accessToken: AccessToken?

    val oAccessToken: String?

    val callMenu: Boolean?

    val version: String?

    val appName: String?

    val isTutorial: Boolean?

    val isNotificationStatus: Boolean?

    val isIntrigueStatus: Boolean?

    val isRenewStatus: Boolean?

    fun isTutorial(userCode: String?): Boolean?

    fun saveTutorial(userCode: String?): Boolean?

    fun saveCountrySIM(countrySIM: String?): Boolean?

    fun saveAccessToken(accessToken: AccessToken?): Boolean?

    fun saveAuthenticated(authenticated: Boolean?): Boolean?

    fun register(entity: SessionEntity?): Boolean?

    fun update(entity: SessionEntity?): Boolean?

    fun registerLapse(lapse: Long?): Boolean?

    fun get(): SessionEntity?

    fun hasUser(): Boolean?

    fun existsUser(username: String?): Boolean?

    fun cleanToken(): Boolean?

    fun logout(): Boolean?

    fun cleanBelcorp(): Boolean?

    fun remove(): Boolean?

    fun saveVersion(version: String?): Boolean?

    fun saveCallMenu(): Boolean?

    fun saveAppName(appName: String?): Boolean?

    fun updateSyncStatus(status: Boolean?): Boolean?

    fun checkSyncStatus(): Boolean?

    fun checkViewsByCliente(): Int?

    fun checkViewsByDeuda(): Int?

    fun updateViewsByCliente(i: Int?): Boolean?

    fun updateViewsByDeuda(i: Int?): Boolean?

    fun checkViewsByMenu(): Int?

    fun updateViewsByMenu(i: Int?): Boolean?

    fun checkAnniversaryByYear(year: Int?): Boolean?

    fun updateAnniversaryByYear(year: Int?): Boolean?

    fun checkChristmasByViews(year: Int?): Boolean?

    fun checkChristmasByYear(year: Int?): Boolean?

    fun checkNewYearByViews(year: Int?): Boolean?

    fun updateChristmasByYear(year: Int?): Boolean?

    fun checkNewYearByYear(year: Int?): Boolean?

    fun updateNewYearByYear(year: Int?): Boolean?

    fun checkConsultantDayByYear(year: Int?): Boolean?

    fun updateConsultantDayByYear(year: Int?): Boolean?

    fun checkBirthdayByYear(year: Int?): Boolean?

    fun updateBirthdayByYear(year: Int?): Boolean?

    fun checkPasoSextoPedido(): Boolean?

    fun checkBelcorpFifty(): Boolean?

    fun checkPostulant(): Boolean?

    fun checkNewConsultant(): Boolean?

    fun updatePasoSextoPedido(): Boolean?

    fun updateBelcorpFifty(): Boolean?

    fun updatePostulant(): Boolean?

    fun updateNewConsultant(): Boolean?

    fun getCupon(): String?

    fun updateCupon(campaign: String?): Boolean?

    fun checkDatamiMessage(): Boolean?

    fun updateStatusDatamiMessage(): Boolean?

    fun checkUsagePermission(): Boolean?

    fun updateUsagePermission(): Boolean?

    fun saveUsabilityConfig(config: String): Boolean?

    fun getUsabilityConfig(): String?

    fun saveSearchPrompt(): Boolean?

    fun checkSearchPrompt(): Boolean?

    fun cleanData(): Boolean?

    fun updateNotificationStatus(status: Boolean): Boolean?

    fun checkNewNotifications(): Boolean?

    fun updateIntrigueStatus(status: Boolean): Boolean?

    fun updateRenewStatus(status: Boolean): Boolean?

    fun isShowUpdateMail(userCode: String?): Boolean?

    fun saveShowUpdateMail(userCode: String?): Boolean?

    fun saveIsShowGiftAnimation(wasShowed: Boolean): Boolean?

    fun getIsShowed(): Boolean

    fun saveStatusMessageToolTip(fueMostrado: Boolean): Boolean?

    fun getWasShowToolTip(): Boolean?

    fun saveApiCacheEnabled(apiCacheEnabled: Boolean): Boolean?

    fun saveApiCacheOnlineTime(apiCacheOnlineTime: Long): Boolean?

    fun saveApiCacheOfflineTime(apiCacheOfflineTime: Long): Boolean?

    fun getApiCacheEnabled(): Boolean?

    fun getApiCacheOnlineTime(): Long?

    fun getApiCacheOfflineTime(): Long?

    fun saveApiCacheOfflineCaminoBrillanteTime(apiCacheOfflineTime: Long): Boolean?

    fun getApiCacheOfflineCaminoBrillanteTime(): Long?

    fun saveLastUpdateCaminoBrillante(lastUpdateCaminoBrillante: Long): Boolean?

    fun getLastUpdateCaminoBrillante(): Long?

    fun saveImageDialogEnabled(imageDialogEnabled: Boolean): Boolean?

    fun saveImagesMaxFicha(imageMaxFicha: Long): Boolean?

    fun getImageDialogEnabled(): Boolean?

    //funcion para A/B testing con remote config
    fun saveHideViewsGridGanaMas(flagHideViews: Boolean): Boolean?

    fun getHideViewsGridGanaMas(): Boolean?

    fun getImagesMaxFicha(): Long?

    fun saveApiClientId(clientId: String): Boolean?

    fun getApiClientId(): String?

    fun saveApiClientSecret(clientSecret: String): Boolean?

    fun getApiClientSecret(): String?

    fun saveOAccessToken(token: String?): Boolean?

    fun saveExpandedSearchview(expandedSearchview: Boolean): Boolean?

    fun getExpandedSearchview():Boolean?

    fun getOrderConfigurableLever():String?

    fun saveOrderConfigurableLever(flag: String):Boolean?

    fun getABTestingBonificaciones():String?

    fun saveABTestingBonificaciones(flag: String):Boolean?

    fun saveMoverBarraNavegacion(flag: Boolean): Boolean?

    fun getMoverBarraNavegacion():Boolean?

    fun saveTokenAnalytics(token: String): Boolean

    fun getTokenAnalytics(): String?

    fun getHomeBannerImage(): Bitmap?

    fun saveHomeBannerImage(image : Bitmap?) : Boolean?

    fun clearHomeBannerImage()

    fun saveCountMaxRecentSearch(countMax: Int): Boolean?

    fun getCountMaxRecentSearch(): Int?

    fun saveDownloadPdfCatalog(value: Boolean) : Boolean?

    fun getDownloadPdfCatalog(): Boolean?

    fun saveMultiOrder(value: Boolean) : Boolean?

    fun isMultiOrder() : Boolean?

    fun saveVersionCode(value: Int) : Boolean?

    fun getVersionCode() : Int?

    fun saveIsRate(value: Boolean) : Boolean?

    fun isRate() : Boolean?

    fun saveCountViewHome(value : Int) : Boolean?

    fun getCountViewHome() : Int?

    fun saveFeatureFlagOfertaFinal(featureFlag: Boolean): Boolean?

    fun getFeatureFlagOfertaFinal(): Boolean?

    fun getPromotionGroupListEnabled(): Boolean?

    fun savePromotionGroupListEnabled(promotionGroupListEnable: Boolean): Boolean?

    suspend fun  saveViewedDialogNextCampaing(visto: Boolean): Boolean?

    suspend fun getStatusDialogNextCampaing(): Boolean?

    suspend fun saveActualCampaing(campaign: String): Boolean

    suspend fun getActualCampaing(): String

}
