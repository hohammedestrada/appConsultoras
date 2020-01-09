package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.AccessToken
import biz.belcorp.consultoras.domain.entity.Session
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SessionUseCase
/**
 * Constructor del caso de uso
 *
 * @param threadExecutor      Ejecutor de un metodo
 * @param postExecutionThread Tipo de ejecucion en un hilo diferente
 */
@Inject
constructor(private val repository: SessionRepository, threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    fun isAuthenticated(observer: BaseObserver<Boolean>) {
        execute(this.repository.isAuthenticated, observer)
    }

    fun isTutorial(observer: BaseObserver<Boolean>) {
        execute(this.repository.isTutorial, observer)
    }

    fun saveCountrySIM(countrySIM: String?, observer: BaseObserver<Boolean>) {
        execute(this.repository.saveCountrySIM(countrySIM), observer)
    }

    fun saveAuthenticated(authenticated: Boolean, observer: BaseObserver<Boolean>) {
        execute(this.repository.saveAuthenticated(authenticated), observer)
    }

    fun register(entity: Session, observer: BaseObserver<Boolean>) {
        execute(this.repository.register(entity), observer)
    }

    fun update(entity: Session, observer: BaseObserver<Boolean>) {
        execute(this.repository.update(entity), observer)
    }

    fun getCountrySIM(observer: BaseObserver<String>) {
        execute(this.repository.countrySIM, observer)
    }

    fun getAccessToken(observer: BaseObserver<AccessToken>) {
        execute(this.repository.accessToken, observer)
    }

    fun isIntrigueStatus(observer: BaseObserver<Boolean>) {
        execute(this.repository.isIntrigueStatus, observer)
    }

    fun isRenewStatus(observer: BaseObserver<Boolean>) {
        execute(this.repository.isRenewStatus, observer)
    }

    operator fun get(observer: BaseObserver<Session>) {
        execute(this.repository.get(), observer)
    }

    fun hasUser(observer: BaseObserver<Boolean>) {
        execute(this.repository.hasUser(), observer)
    }

    fun existsUser(username: String, observer: BaseObserver<Boolean>) {
        execute(this.repository.existsUser(username), observer)
    }

    fun cleanToken(observer: BaseObserver<Boolean>) {
        execute(this.repository.cleanToken(), observer)
    }

    fun logout(observer: BaseObserver<Boolean>) {
        execute(this.repository.logout(), observer)
    }

    fun remove(observer: BaseObserver<Boolean>) {
        execute(this.repository.remove(), observer)
    }

    fun saveVersion(observer: BaseObserver<Boolean>, version: String) {
        execute(this.repository.saveVersion(version), observer)
    }

    fun saveCallMenu(observer: BaseObserver<Boolean>) {
        execute(this.repository.saveCallMenu(), observer)
    }

    fun saveMaterialTap(observer: BaseObserver<Boolean>) {
        execute(this.repository.saveMaterialTap(), observer)
    }

    fun getMaterialTap(observer: BaseObserver<Boolean>) {
        execute(this.repository.materialTap, observer)
    }

    fun getVersion(observer: BaseObserver<String>) {
        execute(this.repository.version, observer)
    }

    fun getCallMenu(observer: BaseObserver<Boolean>) {
        execute(this.repository.callMenu, observer)
    }

    fun cleanBelcorp(observer: BaseObserver<Boolean>) {
        execute(this.repository.cleanBelcorp(), observer)
    }

    fun updateNotificationStatus(status: Boolean, observer: BaseObserver<Boolean>) {
        execute(this.repository.updateNotificationStatus(status), observer)
    }

    fun checkNewNotifications(observer: BaseObserver<Boolean>) {
        execute(this.repository.checkNewNotifications(), observer)
    }

    fun checkSchedule(observer: BaseObserver<Boolean>) {
        execute(this.repository.checkSchedule(), observer)
    }

    fun isUpdateMail(userCode: String?, observer: BaseObserver<Boolean>) {

        execute(this.repository.isShowUpdateMail(userCode), observer)
    }

    fun saveStatusUpdateMail(userCode: String?, observer: BaseObserver<Boolean>) {
        execute(this.repository.saveShowUpdateMail(userCode), observer)
    }

    fun saveStatusGiftAnimation(wasShowing: Boolean, observer: BaseObserver<Boolean>) {
        execute(this.repository.saveIsShowedGiftAnimation(wasShowing), observer)
    }

    fun getIsShowedAnimationGift(observer: BaseObserver<Boolean>) {
        execute(this.repository.getisShowedAnimationGift(), observer)
    }

    fun saveStatusGiftToolTip(wasShowing: Boolean, observer: BaseObserver<Boolean>) {
        execute(this.repository.saveIsShowedToolTipGift(wasShowing), observer)
    }

    fun getIsShowedToolTipGift(observer: BaseObserver<Boolean>) {
        execute(this.repository.getisShowedToolTipGift(), observer)

    }

    fun updateIntrigueStatus(status: Boolean, observer: BaseObserver<Boolean>) {
        execute(this.repository.updateIntrigueStatus(status), observer)
    }

    fun updateRenewStatus(status: Boolean, observer: BaseObserver<Boolean>) {
        execute(this.repository.updateRenewStatus(status), observer)
    }

    suspend fun updateMultiOrderState(status: Boolean) : Boolean{
        return repository.saveMultiOrder(status)
    }

    suspend fun updateVersionCode(versionCode: Int) : Boolean{
        return repository.saveVersionCode(versionCode)
    }

    suspend fun getVersionCode() : Int{
        return repository.getVersionCode()
    }

    fun setMultiOrderState(status: Boolean) = GlobalScope.launch {
        repository.saveMultiOrder(status)
    }

    suspend fun updateIsRate(state : Boolean) : Boolean {
        return repository.saveIsRate(state)
    }

    suspend fun getIsRate() : Boolean{
        return repository.isRate()
    }

    suspend fun updateCountViewHome(value : Int) : Boolean {
        return repository.saveCountViewHome(value)
    }

    suspend fun getCountViewHome() : Int {
        return repository.getCountViewHome()
    }

    fun isUpdateMail(userCode: String?) :Boolean {
        return repository.isShowUpdateMailAsync(userCode)
    }

    suspend fun saveViewedDialogNextCampaing(status: Boolean): Boolean? = this.repository.saveViewedDialogNextCampaing(status)


    suspend fun getStatusDialogNextCampaing(): Boolean? = this.repository.getStatusDialogNextCampaing()


    suspend fun saveActualCampaing(campaign: String): Boolean = this.repository.saveActualCampaing(campaign)

    suspend fun getActualCampaing(): String = this.repository.getActualCampaing()

}
