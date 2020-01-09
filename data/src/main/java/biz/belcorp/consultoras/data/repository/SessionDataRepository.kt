package biz.belcorp.consultoras.data.repository

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.SessionEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.session.SessionDataStoreFactory
import biz.belcorp.consultoras.domain.entity.AccessToken
import biz.belcorp.consultoras.domain.entity.Session
import biz.belcorp.consultoras.domain.repository.SessionRepository
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

@Singleton
class SessionDataRepository @Inject
internal constructor(private val sessionDataStoreFactory: SessionDataStoreFactory,
                     private val sessionEntityDataMapper: SessionEntityDataMapper)
    : SessionRepository {

    override val isAuthenticated: Observable<Boolean>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.isAuthenticated.map { result -> result }
        }

    override val isTutorial: Observable<Boolean>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.isTutorial.map { result -> result }
        }

    override val countrySIM: Observable<String>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.countrySIM.map { result -> result }
        }

    override val accessToken: Observable<AccessToken>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.accessToken.map { result ->
                AccessToken(result.tokenType, result.accessToken, result.refreshToken)
            }
        }

    override val oAccessToken: Deferred<String?>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.oAccessToken
        }

    override val version: Observable<String>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.version.map { result -> result }
        }

    override val materialTap: Observable<Boolean>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.materialTap.map { result -> result }
        }

    override val callMenu: Observable<Boolean>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.callMenu.map { result -> result }
        }

    override val isIntrigueStatus: Observable<Boolean>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.isIntrigueStatus.map { result -> result }
        }

    override val isRenewStatus: Observable<Boolean>
        get() {
            val dataStore = this.sessionDataStoreFactory.create()
            return dataStore.isRenewStatus.map { result -> result }
        }

    override fun saveCountrySIM(countrySIM: String?): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.saveCountrySIM(countrySIM)
    }

    override fun saveAccessToken(accessToken: AccessToken): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.saveAccessToken(
            biz.belcorp.library.net.AccessToken(
                accessToken.tokenType,
                accessToken.accessToken,
                accessToken.refreshToken
            )
        )
    }

    override fun saveAuthenticated(authenticated: Boolean): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.saveAuthenticated(authenticated)
    }

    override fun register(entity: Session): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.register(this.sessionEntityDataMapper.transform(entity)!!)
    }

    override fun update(entity: Session): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.update(this.sessionEntityDataMapper.transform(entity)!!)
    }

    override fun get(): Observable<Session> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.get().map { this.sessionEntityDataMapper.transform(it) }
    }

    override fun hasUser(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.hasUser().map { result -> result }
    }

    override fun existsUser(username: String): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.existsUser(username).map { result -> result }
    }

    override fun cleanToken(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.cleanToken().map { result -> result }
    }

    override fun logout(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.logout().map { result -> result }
    }

    override fun cleanBelcorp(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.cleanBelcorp().map { result -> result }
    }

    override fun remove(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.remove().map { result -> result }
    }

    override fun checkSchedule(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.checkSchedule()
    }

    override fun updateSchedule(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateSchedule()
    }

    override suspend fun updateSchedule2(): Boolean {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateSchedule2()

    }

    override fun saveVersion(version: String): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.saveVersion(version).map { result -> result }
    }

    override fun saveCallMenu(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.saveCallMenu().map { result -> result }
    }

    override fun saveMaterialTap(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.saveMaterialTap().map { result -> result }
    }

    override fun checkStatusSync(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.checkStatusSync()
    }

    override fun updateStatusSync(status: Boolean?): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateStatusSync(status)
    }

    override fun showMaterialTapCliente(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.showMaterialTapCliente()
    }

    override fun updateMaterialTapCliente(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateMaterialTapCliente()
    }

    override fun showMaterialTapDeuda(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.showMaterialTapDeuda()
    }

    override fun updateMaterialTapDeuda(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateMaterialTapDeuda()
    }

    override fun checkBirthdayByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkBirthdayByYear(year)
    }

    override fun updateBirthdayByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateBirthdayByYear(year)
    }

    override fun checkAnniversaryByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkAnniversaryByYear(year)
    }

    override fun updateAnniversaryByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateAnniversaryByYear(year)
    }

    override fun checkChristmasByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkChristmasByYear(year)
    }

    override fun checkChristmasByViews(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkChristmasByViews(year)
    }

    override fun updateChristmasByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateChristmasByYear(year)
    }

    override fun checkNewYearByViews(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkNewYearByViews(year)
    }

    override fun checkNewYearByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkNewYearByYear(year)
    }

    override fun updateNewYearByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateNewYearByYear(year)
    }

    override fun checkConsultantDayByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkConsultantDayByYear(year)
    }

    override fun updateConsultantDayByYear(year: Int): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateConsultantDayByYear(year)
    }

    override fun checkPasoSextoPedido(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkPasoSextoPedido()
    }

    override fun checkBelcorpFifty(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkBelcorpFifty()
    }

    override fun checkPostulant(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkPostulant()
    }

    override fun checkNewConsultant(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.checkNewConsultant()
    }

    override fun updatePasoSextoPedido(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updatePasoSextoPedido()
    }

    override fun updateBelcorpFifty(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateBelcorpFifty()
    }

    override fun updatePostulant(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updatePostulant()
    }

    override fun updateNewConsultant(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.updateNewConsultant()
    }

    override fun getCupon(): Observable<String> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.getCupon().map { result -> result }
    }

    override fun updateCupon(campaign: String): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateCupon(campaign)
    }

    override fun checkDatamiMessage(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.checkDatamiMessage()
    }

    override fun updateStatusDatamiMessage(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateStatusDatamiMessage()
    }

    override fun saveUsabilityConfig(config: String): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.saveUsabilityConfig(config)
    }

    override fun getUsabilityConfig(): Observable<String> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.getUsabilityConfig()
    }

    override fun saveSearchPrompt(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.saveSearchPrompt()
    }

    override fun checkSearchPrompt(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.checkSearchPrompt()
    }

    override fun cleanData(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.cleanData()
    }

    override fun updateNotificationStatus(status: Boolean): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateNotificationStatus(status)
    }

    override fun checkNewNotifications(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.checkNewNotifications()
    }

    override fun isShowUpdateMail(userCode: String?): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.isShowUpdateMail(userCode)
    }

    override fun saveShowUpdateMail(userCode: String?): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.saveShowUpdateMail(userCode)
    }

    override fun saveIsShowedGiftAnimation(wasShowed: Boolean): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.saveStatusAnimationGift(wasShowed)
    }

    override fun getisShowedAnimationGift(): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.getWasShowGift().map { result -> result }
    }

    override fun saveIsShowedToolTipGift(fueMostrado: Boolean): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.saveStatusMessageToolTip(fueMostrado)
    }

    override fun getisShowedToolTipGift(): Observable<Boolean> {

        val dataStore = this.sessionDataStoreFactory.create()
        return dataStore.getWasShowToolTip().map { result -> result }

    }

    override fun updateIntrigueStatus(status: Boolean): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateIntrigueStatus(status)
    }

    override fun imageDialogEnabled(): Boolean {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.imageDialogEnabled()
    }

    override fun flagForTesting(): Boolean {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.flagHideForTesting()
    }

    override fun flagOrderConfigurableLever(): String {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.flagOrderConfigurableLever()
    }

    override fun getABTestingBonificaciones(): String {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.getABTestingBonificaciones()
    }

    override fun imagesMaxFicha(): Long? {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.getImagesMaxFicha()
    }

    override fun updateRenewStatus(status: Boolean): Observable<Boolean> {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.updateRenewStatus(status)
    }


    override fun flagExpandedSearchviewForTesting(): Boolean {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.flagExpandedSearchview()
    }

    override suspend fun saveViewedDialogNextCampaing(status: Boolean): Boolean? {
       return this.sessionDataStoreFactory.createLocal().saveViewedDialogNextCampaing(status)
    }

    override suspend fun getStatusDialogNextCampaing(): Boolean? {
       return this.sessionDataStoreFactory.createLocal().getStatusDialogNextCampaing()
    }

    override suspend fun saveActualCampaing(campaign: String): Boolean {
       return this.sessionDataStoreFactory.createLocal().saveActualCampaing(campaign)
    }

    override suspend fun getActualCampaing(): String {
        return this.sessionDataStoreFactory.createLocal().getActualCampaing()
    }

    override fun saveMultiOrder(value: Boolean): Boolean {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.saveMultiOrder(value)
    }

    override fun isMultiOrder(): Boolean {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.isMultiOrder()
    }

    override fun moverBarraNavegacion(): Boolean {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.flagMoverBarraNavegacion()
    }

    override fun saveVersionCode(value: Int): Boolean {
        return this.sessionDataStoreFactory.createLocal().saveVersionCode(value)
    }

    override fun getVersionCode(): Int {
        return this.sessionDataStoreFactory.createLocal().getVersionCode()
    }

    override fun saveIsRate(value: Boolean): Boolean {
        return this.sessionDataStoreFactory.createLocal().saveIsRate(value)
    }

    override fun isRate(): Boolean {
        return this.sessionDataStoreFactory.createLocal().isRate()
    }

    override fun saveCountViewHome(value: Int): Boolean {
        return this.sessionDataStoreFactory.createLocal().saveCountViewHome(value)
    }

    override fun getCountViewHome(): Int {
        return this.sessionDataStoreFactory.createLocal().getCountViewHome()
    }

    override fun isShowUpdateMailAsync(userCode: String?): Boolean {
        return this.sessionDataStoreFactory.createLocal().isShowUpdateMailAsync(userCode)
    }

    override fun promotionGroupListEnabled(): Boolean? {
        val dataStore = this.sessionDataStoreFactory.createLocal()
        return dataStore.getPromotionGroupListEnabled()
    }
}
