package biz.belcorp.consultoras.data.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import biz.belcorp.consultoras.data.entity.SessionEntity
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.security.AesEncryption
import biz.belcorp.library.security.BelcorpEncryption
import biz.belcorp.library.util.DateUtil
import biz.belcorp.library.util.PreferenceUtil
import biz.belcorp.library.util.StringUtil
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject
internal constructor(context: Context) : ISessionManager {

    private val pref: PreferenceUtil = PreferenceUtil.Builder(context).build()
    private val encryption: BelcorpEncryption

    private var session: SessionEntity? = null

    override val isAuthenticated: Boolean?
        get() {
            getSession()
            return session?.let {
                it.isLogged?.let { isLogged ->
                    !StringUtil.isNullOrEmpty(it.accessToken) && isLogged
                }
            }
        }

    override val isNotificationStatus: Boolean?
        get() {
            getSession()
            return session?.isNotificationStatus
        }

    override val country: String?
        get() {
            getSession()
            return session?.country
        }

    override val countrySIM: String?
        get() {
            getSession()
            return session?.countrySIM
        }

    override val accessToken: AccessToken?
        get() {
            getSession()
            return session?.let {
                AccessToken(it.tokenType, it.accessToken, it.refreshToken)
            }
        }

    override val oAccessToken: String?
        get() {
            getSession()
            return session?.let {
                return it.oAccessToken
            }
        }


    override val version: String?
        get() {
            getSession()
            return session?.let {
                when {
                    it.version == null -> ""
                    else -> it.version
                }
            }
        }

    override val callMenu: Boolean?
        get() {
            getSession()
            return session?.isCallMenu
        }

    override val appName: String?
        get() {
            getSession()
            return session?.appName
        }

    override val isTutorial: Boolean?
        get() = true

    override val isIntrigueStatus: Boolean?
        get() {
            getSession()
            return session?.isIntrigueStatus
        }

    override val isRenewStatus: Boolean?
        get() {
            getSession()
            return session?.isRenewStatus
        }

    init {
        this.encryption = AesEncryption.newInstance()
    }

    override fun isTutorial(userCode: String?): Boolean? {
        return pref.get(TUTORIAL + userCode, true)
    }

    override fun saveTutorial(userCode: String?): Boolean? {
        return pref.save(TUTORIAL + userCode, false)
    }

    override fun saveCountrySIM(countrySIM: String?): Boolean? {
        getSession()
        return session?.let {
            it.countrySIM = countrySIM
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveAccessToken(accessToken: AccessToken?): Boolean? {
        getSession()
        return session?.let {
            it.tokenType = accessToken?.tokenType
            it.accessToken = accessToken?.accessToken
            it.refreshToken = accessToken?.refreshToken
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveAuthenticated(authenticated: Boolean?): Boolean? {
        getSession()
        return session?.let {
            it.isLogged = authenticated
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    fun saveOffersCount(count: Int) {
        getSession()
        session?.let {
            it.ordersCount = count
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    fun getOrdersCount(): Int? {
        getSession()
        return session?.ordersCount
    }

    override fun register(entity: SessionEntity?): Boolean? {
        getSession()
        return session?.let {
            try {
                it.authType = entity?.authType
                it.username = entity?.username
                it.password = encryption.encrypt(entity?.password)
                it.email = entity?.email
                it.country = entity?.country
                it.tokenType = entity?.tokenType
                it.accessToken = entity?.accessToken
                it.refreshToken = entity?.refreshToken
                it.expiresIn = entity?.expiresIn
                it.issued = entity?.issued
                it.expires = entity?.expires
                it.isTutorial = entity?.isTutorial
                it.ordersCount = entity?.ordersCount
                it.isLogged = true

                val lapse = Calendar.getInstance()
                it.started = lapse.timeInMillis

            } catch (ex: Exception) {
                BelcorpLogger.w(TAG, "Error al encriptar")
            }
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun update(entity: SessionEntity?): Boolean? {
        getSession()
        return session?.let {
            it.country = entity?.country
            it.tokenType = entity?.tokenType
            it.accessToken = entity?.accessToken
            it.refreshToken = entity?.refreshToken
            it.expiresIn = entity?.expiresIn
            it.issued = entity?.issued
            it.expires = entity?.expires
            it.ordersCount = entity?.ordersCount
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun registerLapse(lapse: Long?): Boolean? {
        getSession()
        return session?.let {
            it.started = lapse
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun get(): SessionEntity? {
        return pref.get(KEY_NAME, SessionEntity::class.java)
    }

    override fun hasUser(): Boolean? {
        getSession()
        return session?.let {
            !(StringUtil.isNullOrEmpty(it.country) &&
                StringUtil.isNullOrEmpty(it.username) &&
                StringUtil.isNullOrEmpty(it.password))
        }
    }

    override fun existsUser(username: String?): Boolean? {
        getSession()
        return session?.let {
            !StringUtil.isNullOrEmpty(it.username) && it.username == username
        }
    }

    override fun cleanToken(): Boolean? {
        getSession()
        return session?.let {
            it.tokenType = null
            it.accessToken = null
            it.refreshToken = null
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun logout(): Boolean? {
        getSession()
        return session?.let {
            it.started = 0
            it.isLogged = false
            it.isSyncStatus = false

            it.viewsByMenu = 0
            it.viewsByDeuda = 0
            it.viewsByCliente = 0

            it.birthdays = HashMap()
            it.anniversaries = HashMap()
            it.christmases = HashMap()
            it.consultantDays = HashMap()
            it.newYears = HashMap()
            it.counter = HashMap()

            it.postulant = false
            it.isPasoSextoPedido = false
            it.campaign = ""
            it.datamiMessageView = false
            it.isBelcorpFifty = false
            it.newConsultant = false
            it.searchPrompt = false
            it.campaniaActual = StringUtil.Empty
            it.usabilityConfig = null

            it.lastUpdateCaminoBrillante = null

            it.oAccessToken = null

            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveTokenAnalytics(token: String): Boolean {
        return pref.save(TOKEN_ANALYTICS, token)
    }

    override fun getTokenAnalytics(): String? {
        return pref.get(TOKEN_ANALYTICS, null)
    }

    override fun saveCountMaxRecentSearch(countMax: Int): Boolean? {
        return pref.save(COUNT_MAX_RECENT_SEARCH, countMax)
    }

    override fun getCountMaxRecentSearch(): Int {
        return pref.get(COUNT_MAX_RECENT_SEARCH, DEFAULT_COUNT_MAX_RECENT_SEARCH)
    }

    override fun cleanBelcorp(): Boolean? {
        getSession()
        return session?.let {
            it.isBelcorpFifty = false

            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun remove(): Boolean? {
        return pref.remove(KEY_NAME)
    }

    private fun getSession() {
        session = pref.get(KEY_NAME, SessionEntity::class.java)
    }

    override fun saveVersion(version: String?): Boolean? {
        getSession()
        return session?.let {
            it.version = version
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveCallMenu(): Boolean? {
        getSession()
        return session?.let {
            it.isCallMenu = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveAppName(appName: String?): Boolean? {
        getSession()
        return session?.let {
            it.appName = appName
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkSyncStatus(): Boolean? {
        getSession()
        return session?.isSyncStatus
    }

    override fun checkViewsByCliente(): Int? {
        return session?.viewsByCliente
    }

    override fun checkViewsByDeuda(): Int? {
        return session?.viewsByDeuda
    }

    override fun updateViewsByCliente(i: Int?): Boolean? {
        getSession()
        return session?.let {
            it.viewsByCliente = i
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkViewsByMenu(): Int? {
        return session?.let {
            return it.viewsByMenu
        }
    }

    override fun updateViewsByMenu(i: Int?): Boolean? {
        getSession()
        return session?.let {
            it.viewsByMenu = i
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkAnniversaryByYear(year: Int?): Boolean? {
        return session?.let {
            var aBoolean: Boolean? = it.anniversaries[year]
            if (aBoolean == null) {
                aBoolean = false
            }
            return aBoolean
        }
    }

    override fun updateAnniversaryByYear(year: Int?): Boolean? {
        getSession()
        return session?.let {
            it.anniversaries[year] = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkChristmasByViews(year: Int?): Boolean? {
        getSession()
        session?.let {
            it.counter["Christmas$year"]?.let { count ->
                if (count < 3) {
                    it.christmases[year] = false
                    return pref.save<SessionEntity>(KEY_NAME, it)
                }
            }
        }
        return false
    }

    override fun checkChristmasByYear(year: Int?): Boolean? {
        getSession()
        return session?.let {
            it.christmases[year]?.let { xmas ->
                if (xmas) return true
                else {
                    it.counter["Christmas$year"]?.let { count ->
                        return count > 3
                    } ?: run {
                        return false
                    }
                }
            } ?: run {
                return false
            }
        }
    }

    override fun updateChristmasByYear(year: Int?): Boolean? {
        getSession()
        return session?.let {
            it.christmases[year] = true
            it.counter["Christmas$year"] = (it.counter["Christmas$year"] ?: 0) + 1
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkNewYearByViews(year: Int?): Boolean? {
        getSession()
        session?.let {

            val today = Calendar.getInstance()
            var newyear = year

            if (today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DAY_OF_MONTH) == 31) {
                year?.let { n ->
                    newyear = n + 1
                }
            }

            it.counter["NewYear$newyear"]?.let { count ->
                if (count < 3) {
                    it.newYears[newyear] = false
                    return pref.save<SessionEntity>(KEY_NAME, it)
                }

            }
        }
        return false
    }

    override fun checkNewYearByYear(year: Int?): Boolean? {
        getSession()
        return session?.let {

            val today = Calendar.getInstance()
            var newyear = year

            if (today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DAY_OF_MONTH) == 31) {
                year?.let { n ->
                    newyear = n + 1
                }
            }

            it.newYears[newyear]?.let { nyear ->
                if (nyear) return true
                else {
                    it.counter["NewYear$newyear"]?.let { count ->
                        return count > 3
                    } ?: run {
                        return false
                    }
                }
            } ?: run {
                return false
            }
        }
    }

    override fun updateNewYearByYear(year: Int?): Boolean? {
        getSession()
        return session?.let {

            val today = Calendar.getInstance()
            var newyear = year

            if (today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DAY_OF_MONTH) == 31) {
                year?.let { n ->
                    newyear = n + 1
                }
            }

            it.newYears[newyear] = true
            it.counter["NewYear$newyear"] = (it.counter["NewYear$newyear"] ?: 0) + 1

            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkConsultantDayByYear(year: Int?): Boolean? {
        return session?.let {
            var aBoolean: Boolean? = it.consultantDays[year]
            if (aBoolean == null) {
                aBoolean = false
            }
            return aBoolean
        }
    }

    override fun updateConsultantDayByYear(year: Int?): Boolean? {
        getSession()
        return session?.let {
            it.consultantDays[year] = true
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkBirthdayByYear(year: Int?): Boolean? {
        return session?.let {
            var aBoolean: Boolean? = it.birthdays[year]
            if (aBoolean == null) {
                aBoolean = false
            }
            return aBoolean
        }
    }

    override fun updateBirthdayByYear(year: Int?): Boolean? {
        getSession()
        return session?.let {
            it.birthdays[year] = true
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkPasoSextoPedido(): Boolean? {
        return session?.let {
            return it.isPasoSextoPedido
        }
    }

    override fun checkBelcorpFifty(): Boolean? {
        return session?.let {
            return it.isBelcorpFifty
        }
    }

    override fun checkPostulant(): Boolean? {
        return session?.let {
            return it.postulant
        }
    }

    override fun checkNewConsultant(): Boolean? {
        return session?.let {
            return it.newConsultant
        }
    }

    override fun updatePasoSextoPedido(): Boolean? {
        getSession()
        return session?.let {
            it.isPasoSextoPedido = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updateBelcorpFifty(): Boolean? {
        getSession()
        return session?.let {
            it.isBelcorpFifty = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updatePostulant(): Boolean? {
        getSession()
        return session?.let {
            it.postulant = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updateNewConsultant(): Boolean? {
        getSession()
        return session?.let {
            it.newConsultant = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getCupon(): String? {
        getSession()
        return session?.let {
            if (it.campaign == null) "" else it.campaign
        }

    }

    override fun updateCupon(campaign: String?): Boolean? {
        getSession()
        return session?.let {
            it.campaign = campaign
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkDatamiMessage(): Boolean? {
        getSession()
        return session?.let {
            return it.datamiMessageView
        }
    }

    override fun updateStatusDatamiMessage(): Boolean? {
        getSession()
        return session?.let {
            it.datamiMessageView = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun isShowUpdateMail(userCode: String?): Boolean? {
        return pref.get(UPDATE_MAIL + userCode, true)
    }

    override fun saveShowUpdateMail(userCode: String?): Boolean? {
        return pref.save(UPDATE_MAIL + userCode, false)
    }


    // Usage Permission

    override fun updateUsagePermission(): Boolean? {
        getSession()
        return session?.let {
            it.usagePermission = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun checkUsagePermission(): Boolean {
        getSession()
        return session?.let {
            it.usagePermission.let { p -> p } ?: false
        } ?: run {
            false
        }

    }

    // Log Usabilidad Info
    override fun saveUsabilityConfig(config: String): Boolean? {
        getSession()
        return session?.let {
            it.usabilityConfig = config
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updateNotificationStatus(status: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.isNotificationStatus = status
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getUsabilityConfig(): String? {
        getSession()
        return session?.let {
            return it.usabilityConfig
        }
    }

    // Prompt para el buscador
    override fun checkSearchPrompt(): Boolean? {
        getSession()
        return session?.let {
            return it.searchPrompt
        }
    }

    override fun checkNewNotifications(): Boolean? {
        getSession()
        return session?.let {
            return it.isNotificationStatus
        }
    }

    override fun saveSearchPrompt(): Boolean? {
        getSession()
        return session?.let {
            it.searchPrompt = true
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updateIntrigueStatus(status: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.isIntrigueStatus = status
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updateRenewStatus(status: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.isRenewStatus = status
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updateViewsByDeuda(i: Int?): Boolean? {
        getSession()
        return session?.let {
            it.viewsByDeuda = i
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun updateSyncStatus(status: Boolean?): Boolean? {
        getSession()
        return session?.let {
            it.isSyncStatus = status
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveStatusMessageToolTip(fueMostrado: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.isTooltipmessageGift = fueMostrado
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getWasShowToolTip(): Boolean? {
        getSession()
        return session?.isTooltipmessageGift
    }

    override fun saveApiCacheEnabled(apiCacheEnabled: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.apiCacheEnabled = apiCacheEnabled
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveApiCacheOnlineTime(apiCacheOnlineTime: Long): Boolean? {
        getSession()
        return session?.let {
            it.apiCacheOnlineTime = apiCacheOnlineTime
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveApiCacheOfflineTime(apiCacheOfflineTime: Long): Boolean? {
        getSession()
        return session?.let {
            it.apiCacheOfflineTime = apiCacheOfflineTime
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveApiCacheOfflineCaminoBrillanteTime(apiCacheOfflineTime: Long): Boolean? {
        getSession()
        return session?.let {
            it.apiCacheOfflineCaminoBrilanteTime = apiCacheOfflineTime
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getApiCacheOfflineCaminoBrillanteTime(): Long? {
        getSession()
        return session?.let {
            return it.apiCacheOfflineCaminoBrilanteTime
        }
    }

    override fun saveLastUpdateCaminoBrillante(lastUpdateCaminoBrillante: Long): Boolean? {
        getSession()
        return session?.let {
            Log.d("saveLastUpdateCamino", lastUpdateCaminoBrillante.toString())
            it.lastUpdateCaminoBrillante = lastUpdateCaminoBrillante
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getLastUpdateCaminoBrillante(): Long? {
        getSession()
        return session?.let {
            return it.lastUpdateCaminoBrillante
        }
    }



    override fun getApiCacheEnabled(): Boolean? {
        getSession()
        return session?.let {
            return it.apiCacheEnabled ?: false
        }
    }

    override fun getApiCacheOnlineTime(): Long? {
        getSession()
        return session?.let {
            return it.apiCacheOnlineTime ?: 3600
        }
    }

    override fun getApiCacheOfflineTime(): Long? {
        getSession()
        return session?.let {
            return it.apiCacheOfflineTime ?: 86400
        }
    }

    override fun cleanData(): Boolean? {
        getSession()
        return session?.let {

            //Update cupon
            it.campaign = ""

            // Refresh data forzada
            it.started = 0L

            val year = DateUtil.getCurrentYear()

            // Check Christmas by Views

            it.counter["Christmas$year"]?.let { count ->
                if (count < 3) {
                    it.christmases[year] = false
                }
            }
            // Check NewYear By Views

            val today = Calendar.getInstance()
            var newyear = year

            if (today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DAY_OF_MONTH) == 31) {
                year.let { n ->
                    newyear = n + 1
                }
            }

            it.counter["NewYear$newyear"]?.let { count ->
                if (count < 3) {
                    it.newYears[newyear] = false
                }
            }

            pref.save<SessionEntity>(KEY_NAME, it)
        }


    }

    override fun saveImageDialogEnabled(imageDialogEnabled: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.imageDialogEnabled = imageDialogEnabled
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }


    override fun saveImagesMaxFicha(imageMaxFicha: Long): Boolean? {
        getSession()
        return session?.let {
            it.imagesMaxFicha = imageMaxFicha
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getImageDialogEnabled(): Boolean? {
        getSession()
        return session?.let {
            return it.imageDialogEnabled
        }
    }

    override fun getImagesMaxFicha(): Long? {
        getSession()
        return session?.let {
            return it.imagesMaxFicha
        }
    }

    //########### METODOS PARA SETEAR Y RECUPERAR FLAGS PARA AB-TESTING

    override fun saveHideViewsGridGanaMas(flagHideViews: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.hideViewsGridGanaMas = flagHideViews
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getHideViewsGridGanaMas(): Boolean? {
        getSession()
        return session?.let {
            return it.hideViewsGridGanaMas
        }
    }


    //nuevos metodo, para guardar y recuperar el flag de remote config al iniciar sesión
    override fun saveExpandedSearchview(expandedSearchview: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.expandedSearchviewGanaMas = expandedSearchview
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun getExpandedSearchview(): Boolean? {
        getSession()
        return session?.let {
            return it.expandedSearchviewGanaMas
        }
    }

    override fun saveOrderConfigurableLever(flag: String): Boolean? {
        getSession()
        return session?.let {
            it.orderConfigurableLever = flag
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun getOrderConfigurableLever(): String? {
        getSession()
        return session?.let {
            return it.orderConfigurableLever
        }
    }

    override fun saveABTestingBonificaciones(flag: String): Boolean? {
        getSession()
        return session?.let {
            it.abtestingBonificaciones = flag
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun getABTestingBonificaciones(): String? {
        getSession()
        return session?.let {
            return it.abtestingBonificaciones
        }
    }

    override fun saveMoverBarraNavegacion(flag: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.moverBarraNavegacion = flag
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun getMoverBarraNavegacion(): Boolean? {
        getSession()
        return session?.let {
            return it.moverBarraNavegacion
        }
    }

    //#######################################################

    override fun getHomeBannerImage(): Bitmap? {

         pref.get(HOME_HEADER,"").let {

             if(it.isEmpty())
                 return null

             val decodedBytes = Base64.decode(it, Base64.NO_WRAP)
             return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)


         }
    }

    override fun saveHomeBannerImage(image : Bitmap?) : Boolean? {
        return image?.let {

            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            pref.save<String>(HOME_HEADER, Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP))
        }
    }

    override fun clearHomeBannerImage() {
        pref.remove(HOME_HEADER)
    }

    override fun saveDownloadPdfCatalog(value: Boolean) : Boolean? {
        getSession()
        return session?.let {
            it.downloadCatalogPdf = value
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun getDownloadPdfCatalog(): Boolean? {
        getSession()
        return session?.let {
            return it.downloadCatalogPdf
        }
    }

    override fun saveIsShowGiftAnimation(wasShowed: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.isWasShowGiftAnimation = wasShowed
            return pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getIsShowed(): Boolean {
        getSession()
        return session?.isWasShowGiftAnimation!!
    }

    override suspend fun saveViewedDialogNextCampaing(visto: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.viewdDialogNextCampain = visto
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override suspend fun getStatusDialogNextCampaing(): Boolean? {
        getSession()
        return session?.let {
            it.viewdDialogNextCampain
        }
    }

    override suspend fun saveActualCampaing(campaign: String): Boolean{
        getSession()
        return  session?.let {
            it.campaniaActual = campaign
            pref.save<SessionEntity>(KEY_NAME, it).let {it1->
                 it1
            }
        }?:kotlin.run { false }
    }

    override suspend fun getActualCampaing(): String {
        getSession()
        return session?.let {
            it.campaniaActual
        }?:kotlin.run { StringUtil.Empty }
    }

    override fun saveMultiOrder(value: Boolean) : Boolean? {
        getSession()
        return session?.let {
            it.isMultiOrder = value
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun isMultiOrder(): Boolean? {
        getSession()
        return session?.let {
            return it.isMultiOrder
        }
    }
    override fun saveVersionCode(value: Int) : Boolean? {
        getSession()
        return session?.let {
            it.versionCode = value
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun getVersionCode() : Int? {
        getSession()
        return session?.let {
            return it.versionCode
        }
    }

    override fun saveIsRate(value: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.isRate = value
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun isRate(): Boolean? {
        getSession()
        return session?.let {
            return it.isRate
        }
    }

    override fun saveCountViewHome(value : Int): Boolean? {
        getSession()
        return session?.let {
            it.countViewHome = value
            pref.save<SessionEntity>(KEY_NAME,it)
        }
    }

    override fun getCountViewHome(): Int? {
        getSession()
        return session?.let {
            return it.countViewHome
        }
    }

    override fun saveApiClientId(clientId: String): Boolean? {
        getSession()
        return session?.let {
            it.apiClientId = clientId
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getApiClientId(): String? {
        getSession()
        return session?.let {
            return it.apiClientId
        }
    }

    override fun saveApiClientSecret(clientSecret: String): Boolean? {
        getSession()
        return session?.let {
            it.apiClientSecret = clientSecret
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getApiClientSecret(): String? {
        getSession()
        return session?.let {
            return it.apiClientSecret
        }
    }

    override fun saveOAccessToken(token: String?): Boolean? {
        getSession()
        return session?.let {
            it.oAccessToken = token
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun getPromotionGroupListEnabled(): Boolean? {
        getSession()
        return session?.let {
            return it.promotionGroupListEnable
        }
    }

    override fun savePromotionGroupListEnabled(promotionGroupListEnable: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.promotionGroupListEnable = promotionGroupListEnable
            pref.save<SessionEntity>(KEY_NAME, it)
        }
    }

    override fun saveFeatureFlagOfertaFinal(featureFlag: Boolean): Boolean? {
        getSession()
        return session?.let {
            it.featureFlagOfertaFinal = featureFlag
            pref.save(KEY_NAME, it)
        }
    }

    override fun getFeatureFlagOfertaFinal(): Boolean? {
        getSession()
        return session?.let {
            return it.featureFlagOfertaFinal
        }
    }

    companion object {

        private const val TAG = "SessionManager"
        private const val KEY_NAME = "SESSION"
        private const val TUTORIAL = "TUTORIAL"
        private const val UPDATE_MAIL = "UPDATE_MAIL_OF"
        private const val TOKEN_ANALYTICS = "TOKEN_ANALYTICS"
        private const val COUNT_MAX_RECENT_SEARCH = "COUNT_MAX_RECENT_SEARCH"
        private const val DEFAULT_COUNT_MAX_RECENT_SEARCH = 10
        private const val HOME_HEADER = "HOME_HEADER"
        private var INSTANCE: SessionManager? = null


        @Synchronized
        fun getInstance(context: Context): SessionManager {
            if (INSTANCE == null) {
                INSTANCE = SessionManager(context)
            }
            return INSTANCE as SessionManager
        }
    }

}
