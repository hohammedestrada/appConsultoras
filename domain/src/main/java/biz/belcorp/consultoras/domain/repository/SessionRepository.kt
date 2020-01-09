package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.AccessToken
import biz.belcorp.consultoras.domain.entity.Session
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

interface SessionRepository {

    val isAuthenticated: Observable<Boolean>

    val isTutorial: Observable<Boolean>

    val countrySIM: Observable<String>

    val accessToken: Observable<AccessToken>

    val oAccessToken: Deferred<String?>

    val version: Observable<String>

    val materialTap: Observable<Boolean>

    val callMenu: Observable<Boolean>

    val isIntrigueStatus: Observable<Boolean>

    val isRenewStatus: Observable<Boolean>

    fun saveCountrySIM(countrySIM: String?): Observable<Boolean>

    fun saveAccessToken(accessToken: AccessToken): Observable<Boolean>

    fun saveAuthenticated(authenticated: Boolean): Observable<Boolean>

    fun register(entity: Session): Observable<Boolean>

    fun update(entity: Session): Observable<Boolean>

    fun get(): Observable<Session>

    fun hasUser(): Observable<Boolean>

    fun existsUser(username: String): Observable<Boolean>

    fun cleanToken(): Observable<Boolean>

    fun logout(): Observable<Boolean>

    fun cleanBelcorp(): Observable<Boolean>

    fun remove(): Observable<Boolean>

    fun checkSchedule(): Observable<Boolean>

    fun updateSchedule(): Observable<Boolean>

    suspend fun updateSchedule2(): Boolean

    fun saveVersion(version: String): Observable<Boolean>

    fun saveCallMenu(): Observable<Boolean>

    fun saveMaterialTap(): Observable<Boolean>

    fun checkStatusSync(): Observable<Boolean>

    fun updateStatusSync(status: Boolean?): Observable<Boolean>

    fun showMaterialTapCliente(): Observable<Boolean>

    fun updateMaterialTapCliente(): Observable<Boolean>

    fun showMaterialTapDeuda(): Observable<Boolean>

    fun updateMaterialTapDeuda(): Observable<Boolean>

    fun checkBirthdayByYear(year: Int): Observable<Boolean>

    fun updateBirthdayByYear(year: Int): Observable<Boolean>

    fun checkAnniversaryByYear(year: Int): Observable<Boolean>

    fun updateAnniversaryByYear(year: Int): Observable<Boolean>

    fun checkChristmasByYear(year: Int): Observable<Boolean>

    fun checkChristmasByViews(year: Int): Observable<Boolean>

    fun updateChristmasByYear(year: Int): Observable<Boolean>

    fun checkNewYearByViews(year: Int): Observable<Boolean>

    fun checkNewYearByYear(year: Int): Observable<Boolean>

    fun updateNewYearByYear(year: Int): Observable<Boolean>

    fun checkConsultantDayByYear(year: Int): Observable<Boolean>

    fun updateConsultantDayByYear(year: Int): Observable<Boolean>

    fun checkPasoSextoPedido(): Observable<Boolean>

    fun checkBelcorpFifty(): Observable<Boolean>

    fun checkPostulant(): Observable<Boolean>

    fun checkNewConsultant(): Observable<Boolean>

    fun updatePasoSextoPedido(): Observable<Boolean>

    fun updateBelcorpFifty(): Observable<Boolean>

    fun updatePostulant(): Observable<Boolean>

    fun updateNewConsultant(): Observable<Boolean>

    fun getCupon(): Observable<String>

    fun updateCupon(campaign: String): Observable<Boolean>

    fun checkDatamiMessage(): Observable<Boolean>

    fun updateStatusDatamiMessage(): Observable<Boolean>

    fun saveUsabilityConfig(config: String): Observable<Boolean>

    fun getUsabilityConfig(): Observable<String>

    fun saveSearchPrompt(): Observable<Boolean>

    fun checkSearchPrompt(): Observable<Boolean>

    fun cleanData(): Observable<Boolean>

    fun updateNotificationStatus(status: Boolean): Observable<Boolean>

    fun checkNewNotifications(): Observable<Boolean>

    fun isShowUpdateMail(userCode: String?): Observable<Boolean>

    fun saveShowUpdateMail(userCode: String?): Observable<Boolean>

    fun saveIsShowedGiftAnimation(wasShowed: Boolean): Observable<Boolean>

    fun getisShowedAnimationGift(): Observable<Boolean>

    fun saveIsShowedToolTipGift(fueMostrado: Boolean): Observable<Boolean>

    fun getisShowedToolTipGift(): Observable<Boolean>

    fun updateIntrigueStatus(status: Boolean): Observable<Boolean>

    fun imageDialogEnabled(): Boolean

    fun flagForTesting(): Boolean

    fun flagOrderConfigurableLever(): String

    fun getABTestingBonificaciones(): String

    fun imagesMaxFicha(): Long?

    fun updateRenewStatus(status: Boolean): Observable<Boolean>

    fun flagExpandedSearchviewForTesting(): Boolean

    fun saveMultiOrder(value: Boolean) : Boolean

    fun isMultiOrder() : Boolean

    fun moverBarraNavegacion(): Boolean

    fun saveVersionCode(value: Int) : Boolean

    fun getVersionCode() : Int

    fun saveIsRate(value: Boolean) : Boolean

    fun isRate() : Boolean

    fun saveCountViewHome(value : Int) : Boolean

    fun getCountViewHome() : Int

    fun promotionGroupListEnabled(): Boolean?

    fun isShowUpdateMailAsync(userCode: String?): Boolean

    suspend fun saveViewedDialogNextCampaing(status: Boolean): Boolean?

    suspend fun  getStatusDialogNextCampaing(): Boolean?

    suspend fun saveActualCampaing(campaign: String): Boolean

    suspend fun getActualCampaing(): String

}
