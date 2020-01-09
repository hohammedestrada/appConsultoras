package biz.belcorp.consultoras.data.repository.datasource.session

import android.content.Context
import biz.belcorp.consultoras.data.entity.SessionEntity
import biz.belcorp.consultoras.data.exception.SessionException
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.sql.exception.SqlException
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*
import java.util.concurrent.TimeUnit

class SessionLocalDataStore(private val sessionManager: ISessionManager,
                            internal var context: Context) : SessionDataStore {

    override val isAuthenticated: Observable<Boolean>
        get() = Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.isAuthenticated!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val isTutorial: Observable<Boolean>
        get() = Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.isTutorial!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val countrySIM: Observable<String>
        get() = Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.countrySIM!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val appName: Observable<String>
        get() = Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.appName!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val accessToken: Observable<AccessToken>
        get() = Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.accessToken!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val oAccessToken: Deferred<String?>
        get() = GlobalScope.async {
            sessionManager.oAccessToken
        }

    override val materialTap: Observable<Boolean>
        get() = Observable.create { emitter ->
            try {
                val viewsByMenu = sessionManager.checkViewsByMenu()!!
                if (viewsByMenu < Integer.MAX_VALUE) {
                    emitter.onNext(true)
                    sessionManager.updateViewsByMenu(viewsByMenu + 1)
                } else {
                    emitter.onNext(false)
                }
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val callMenu: Observable<Boolean>
        get() = Observable.create { emitter ->
            try {
                val callMenu = sessionManager.callMenu!!
                emitter.onNext(callMenu)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val version: Observable<String>
        get() = Observable.create { emitter ->
            try {
                val version = sessionManager.version!!
                emitter.onNext(version)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val isIntrigueStatus: Observable<Boolean>
        get() = Observable.create { emitter ->
            try {
                val isIntrigueStatus = sessionManager.isIntrigueStatus ?: false
                emitter.onNext(isIntrigueStatus)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override val isRenewStatus: Observable<Boolean>
        get() = Observable.create { emitter ->
            try {
                val isRenewStatus = sessionManager.isRenewStatus ?: false
                emitter.onNext(isRenewStatus)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    override fun saveCountrySIM(countrySIM: String?): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveCountrySIM(countrySIM)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveAppName(appName: String): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveAppName(appName)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }


    override fun saveAccessToken(accessToken: AccessToken): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveAccessToken(accessToken)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveAuthenticated(authenticated: Boolean): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveAuthenticated(authenticated)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun register(entity: SessionEntity): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.register(entity)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun update(entity: SessionEntity): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.update(entity)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun get(): Observable<SessionEntity> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.get()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun hasUser(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.hasUser()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun existsUser(username: String): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.existsUser(username)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun cleanToken(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.cleanToken()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun logout(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.logout()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun cleanBelcorp(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.cleanBelcorp()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun remove(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.remove()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkSchedule(): Observable<Boolean> {
        return Observable.create { emitter ->

            val sessionState = sessionManager.get()!!.started == 0L

            val lapse = Calendar.getInstance()
            val issued = Calendar.getInstance()

            if (sessionState) {
                sessionManager.registerLapse(lapse.timeInMillis)
                emitter.onNext(true)
            } else {

                issued.timeInMillis = sessionManager.get()!!.started!!

                val time = TimeUnit.MILLISECONDS.toMinutes(lapse.timeInMillis - issued.timeInMillis)

                val result = time > 30
                if (result) {
                    sessionManager.registerLapse(lapse.timeInMillis)
                    emitter.onNext(true)
                } else
                    emitter.onNext(false)
            }

            emitter.onComplete()
        }
    }

    override fun updateSchedule(): Observable<Boolean> {

        return Observable.create { emitter ->
            try {
                sessionManager.registerLapse(0L)
                emitter.onNext(true)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }


    override fun updateSchedule2(): Boolean {
        return try {
            sessionManager.registerLapse(0L)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override fun getWasShowGift(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.getIsShowed())
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun getWasShowToolTip(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.getWasShowToolTip() ?: false)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }

    }


    override fun saveVersion(version: String): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                sessionManager.saveVersion(version)
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveCallMenu(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                sessionManager.saveCallMenu()
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveMaterialTap(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                sessionManager.updateViewsByMenu(Integer.MAX_VALUE)
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkStatusSync(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                val status = sessionManager.checkSyncStatus()
                if (!status!!) {
                    sessionManager.updateSyncStatus(true)
                }
                emitter.onNext(status)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateStatusSync(status: Boolean?): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateSyncStatus(status)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveStatusAnimationGift(wasShowed: Boolean): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveIsShowGiftAnimation(wasShowed)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveStatusMessageToolTip(fueMostrado: Boolean): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveStatusMessageToolTip(fueMostrado)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateMaterialTapCliente(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                sessionManager.updateViewsByCliente(Integer.MAX_VALUE)
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }


    override fun showMaterialTapCliente(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                val viewsByCliente = sessionManager.checkViewsByCliente()!!
                if (viewsByCliente < Integer.MAX_VALUE) {
                    emitter.onNext(true)
                    sessionManager.updateViewsByCliente(viewsByCliente + 1)
                } else {
                    emitter.onNext(false)
                }
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateMaterialTapDeuda(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                sessionManager.updateViewsByDeuda(Integer.MAX_VALUE)
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }


    override fun showMaterialTapDeuda(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                val viewsByDeuda = sessionManager.checkViewsByDeuda()!!
                if (viewsByDeuda < Integer.MAX_VALUE) {
                    emitter.onNext(true)
                    sessionManager.updateViewsByDeuda(viewsByDeuda + 1)
                } else {
                    emitter.onNext(false)
                }
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkBirthdayByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkBirthdayByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateBirthdayByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateBirthdayByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkAnniversaryByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkAnniversaryByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateAnniversaryByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateAnniversaryByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkChristmasByViews(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkChristmasByViews(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }


    override fun checkChristmasByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkChristmasByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateChristmasByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateChristmasByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }


    override fun checkNewYearByViews(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkNewYearByViews(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkNewYearByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkNewYearByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateNewYearByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateNewYearByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkConsultantDayByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkConsultantDayByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateConsultantDayByYear(year: Int): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateConsultantDayByYear(year)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkPasoSextoPedido(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkPasoSextoPedido()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkBelcorpFifty(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkBelcorpFifty()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkPostulant(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkPostulant()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkNewConsultant(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkNewConsultant()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updatePasoSextoPedido(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updatePasoSextoPedido()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateBelcorpFifty(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateBelcorpFifty()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updatePostulant(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updatePostulant()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateNewConsultant(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateNewConsultant()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun getCupon(): Observable<String> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.getCupon()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }


    override fun updateCupon(campaign: String): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                sessionManager.updateCupon(campaign)
                emitter.onNext(true)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkDatamiMessage(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkDatamiMessage()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateStatusDatamiMessage(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateStatusDatamiMessage()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveUsabilityConfig(config: String): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveUsabilityConfig(config)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun getUsabilityConfig(): Observable<String> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.getUsabilityConfig()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }


    override fun saveSearchPrompt(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveSearchPrompt()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun checkSearchPrompt(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkSearchPrompt()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateNotificationStatus(status: Boolean): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateNotificationStatus(status)!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }


    }

    override fun checkNewNotifications(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.checkNewNotifications()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateIntrigueStatus(status: Boolean): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateIntrigueStatus(status) ?: false)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun updateRenewStatus(status: Boolean): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.updateRenewStatus(status) ?: false)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun cleanData(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.cleanData()!!)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))

            }
        }
    }

    override fun isShowUpdateMail(userCode: String?): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.isShowUpdateMail(userCode) ?: false)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))

            }
        }
    }

    override fun saveShowUpdateMail(userCode: String?): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(sessionManager.saveShowUpdateMail(userCode) ?: false)
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))

            }
        }
    }

    override fun getCargarCaminoBrillante(): Observable<Boolean> {
        return Observable.create { emitter ->
            try {
                sessionManager.getLastUpdateCaminoBrillante()?.let {
                    val milisegundos = System.currentTimeMillis() - it
                    val minutos = TimeUnit.MILLISECONDS.toMinutes(milisegundos)
                    sessionManager.getApiCacheOfflineCaminoBrillanteTime()?.let {
                        emitter.onNext(minutos >= it)
                    } ?: emitter.onNext(true)
                } ?: run {
                    emitter.onNext(true)
                }
                emitter.onComplete()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun saveLastUpdateCaminoBrillante(lastUpdateCaminoBrillante: Long): Boolean {
        val ok = sessionManager.saveLastUpdateCaminoBrillante(lastUpdateCaminoBrillante)
        return ok ?: false
    }

    override fun imageDialogEnabled(): Boolean {
        return sessionManager.getImageDialogEnabled() ?: false
    }

    override fun getImagesMaxFicha(): Long? {
        return sessionManager.getImagesMaxFicha()
    }

    override fun flagHideForTesting(): Boolean {
        return sessionManager.getHideViewsGridGanaMas() ?: false
    }

    override fun getApiClientId(): String? {
        return sessionManager.getApiClientId()
    }

    override fun saveApiClientId(clientId: String): Boolean? {
        return sessionManager.saveApiClientId(clientId)
    }

    override fun getApiClientSecret(): String? {
        return sessionManager.getApiClientSecret()
    }

    override fun saveApiClientSecret(clientSecret: String): Boolean? {
        return sessionManager.saveApiClientSecret(clientSecret)
    }

    override fun saveOAccessToken(token: String?): Boolean? {
        return sessionManager.saveOAccessToken(token)
    }

    override fun flagExpandedSearchview(): Boolean {
        return sessionManager.getExpandedSearchview() ?:false
    }

    override fun flagOrderConfigurableLever(): String {
        return sessionManager.getOrderConfigurableLever() ?:""
    }

    override fun getABTestingBonificaciones(): String {
        return sessionManager.getABTestingBonificaciones() ?:""
    }

    override fun saveMultiOrder(value: Boolean): Boolean {
        return sessionManager.saveMultiOrder(value) ?: false
    }

    override fun isMultiOrder(): Boolean {
        return sessionManager.isMultiOrder() ?: false
    }

    override fun flagMoverBarraNavegacion(): Boolean {
        return sessionManager.getMoverBarraNavegacion() ?: false
    }

    override fun saveVersionCode(value: Int): Boolean {
        return sessionManager.saveVersionCode(value) ?: false
    }

    override fun getVersionCode(): Int {
        return sessionManager.getVersionCode() ?: 0
    }

    override fun saveIsRate(value: Boolean): Boolean {
        return sessionManager.saveIsRate(value) ?: false
    }

    override fun isRate(): Boolean {
        return sessionManager.isRate() ?: false
    }

    override fun saveCountViewHome(value: Int): Boolean {
        return sessionManager.saveCountViewHome(value) ?: false
    }

    override fun getCountViewHome(): Int {
        return sessionManager.getCountViewHome() ?: 0
    }

    override fun getPromotionGroupListEnabled(): Boolean? {
        return sessionManager.getPromotionGroupListEnabled()
    }

    override fun isShowUpdateMailAsync(userCode: String?): Boolean {
        return sessionManager.isShowUpdateMail(userCode) ?: false
    }

    override suspend fun saveViewedDialogNextCampaing(status: Boolean): Boolean? {
        return sessionManager.saveViewedDialogNextCampaing(status)
    }

    override suspend fun getStatusDialogNextCampaing(): Boolean? {
        return sessionManager.getStatusDialogNextCampaing()
    }

    override suspend fun saveActualCampaing(campaign: String): Boolean {
        return sessionManager.saveActualCampaing(campaign)
    }

    override suspend fun getActualCampaing(): String {
        return sessionManager.getActualCampaing()
    }
}
