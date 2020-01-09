package biz.belcorp.consultoras.feature.embedded.academia

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.domain.entity.AcademyUrl
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.consultoras.util.anotation.UserType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.security.JwtEncryption
import org.json.JSONObject
import javax.inject.Inject

class AcademiaPresenter @Inject
internal constructor(
    private val accountUseCase: AccountUseCase,
    private val userUseCase: UserUseCase,
    private val menuUseCase: MenuUseCase,
    private val loginModelDataMapper: LoginModelDataMapper) : Presenter<AcademiaOrderView> {

    private var academiaView: AcademiaOrderView? = null

    override fun attachView(view: AcademiaOrderView) {
        academiaView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.accountUseCase.dispose()
        this.userUseCase.dispose()
        this.menuUseCase.dispose()
        this.academiaView = null
    }

    /** */

    fun getPageTitle(code: String) {
        menuUseCase.get(code, GetTitleObserver())
    }

    fun load(deviceId: String) {
        academiaView?.showLoading()
        this.userUseCase[GetUser(deviceId)]
        this.userUseCase.updateScheduler(UpdateObserver())
    }

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun trackBack() {
        userUseCase[UserBackPressedObserver()]
    }

    fun getUrl() {
        academiaView?.showLoading()
        accountUseCase.getAdademyUrl(AcademiaObserver())
    }

    /** */

    private inner class GetTitleObserver : BaseObserver<Menu?>() {
        override fun onNext(t: Menu?) {
            t?.let {
                academiaView?.setMenuTitle(it.descripcion)
            }

        }
    }

    private inner class GetUser(internal var deviceId: String?) : BaseObserver<User?>() {

        override fun onNext(t: User?) {

            var url = ""

            if (null != t) {
                var token = ""
                val countryISO = t.countryISO
                val consultantId = t.userCode

                if ("" != countryISO && "" != consultantId) {
                    if (null == deviceId || deviceId!!.isEmpty())
                        deviceId = consultantId

                    val jsonPayLoad = JSONObject()

                    try {
                        jsonPayLoad.put("Pais", countryISO)
                        jsonPayLoad.put("Pagina", PageUrlType.ORDER)
                        jsonPayLoad.put("CodigoUsuario", consultantId)
                        jsonPayLoad.put("EsAppMobile", "True")
                        jsonPayLoad.put("ClienteId", "0")
                        jsonPayLoad.put("Identifier", deviceId)

                        jsonPayLoad.put("app_belcorp", 1)

                        val payload = jsonPayLoad.toString()
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload)

                    } catch (e: Exception) {
                        BelcorpLogger.w("GetUser.onNext", e)
                    }

                }

                if ("" != token)
                    url = GlobalConstant.URL_ACADEMIA + token
            }

            if (null == academiaView) return

            if (t?.userType == UserType.POSTULANTE) {
                academiaView?.showPostulant()
            }

            academiaView?.showUrl(url)
        }

        override fun onError(exception: Throwable) {
            academiaView?.hideLoading()
            academiaView?.showError()
        }
    }

    private inner class UpdateObserver : BaseObserver<Boolean>()

    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            academiaView?.initScreenTrack(loginModelDataMapper.transform(t))
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            academiaView?.trackBack(loginModelDataMapper.transform(t))
        }
    }

    private inner class AcademiaObserver : BaseObserver<AcademyUrl?>() {
        override fun onNext(t: AcademyUrl?) {
            t?.let {
                it.urlMiAcademia?.let {url ->
                    academiaView?.showUrl(url)
                }
            }
        }
    }

}

