package biz.belcorp.consultoras.feature.embedded.esikaahora

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.MenuUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.security.JwtEncryption
import org.json.JSONObject
import javax.inject.Inject

class EsikaAhoraWebPresenter @Inject
internal constructor(
    private val accountUseCase: AccountUseCase,
    private val userUseCase: UserUseCase,
    private val menuUseCase: MenuUseCase) : Presenter<EsikaAhoraWebView> {

    var esikaAhoraView: EsikaAhoraWebView? = null

    override fun attachView(view: EsikaAhoraWebView) {
        esikaAhoraView = view
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
        this.esikaAhoraView = null
    }

    fun getPageTitle(code: String) {
        menuUseCase.get(code, GetTitleObserver())
    }

    fun load(deviceId: String) {
        esikaAhoraView?.showLoading()
        this.userUseCase[GetUser(deviceId)]
        this.userUseCase.updateScheduler(UpdateObserver())
    }

    private inner class GetTitleObserver : BaseObserver<Menu?>() {
        override fun onNext(t: Menu?) {
            t?.let {
                esikaAhoraView?.setMenuTitle(it.descripcion)
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

                if (countryISO?.isNotEmpty() == true && consultantId?.isNotEmpty() == true) {
                    if (null == deviceId || deviceId!!.isEmpty())
                        deviceId = consultantId

                    val jsonPayLoad = JSONObject()

                    try {
                        jsonPayLoad.put("Pais", countryISO)
                        jsonPayLoad.put("Pagina", PageUrlType.ESIKAAHORA)
                        jsonPayLoad.put("CodigoUsuario", consultantId)
                        jsonPayLoad.put("LimpiarSession",true)
                        jsonPayLoad.put("EsAppMobile", "True")

                        val payload = jsonPayLoad.toString()
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload)

                    } catch (e: Exception) {
                        BelcorpLogger.w("GetUser.onNext", e)
                    }
                }

                if ("" != token)
                    url = GlobalConstant.URL_PASE_PEDIDO + token
            }

            if (null == esikaAhoraView) return

            esikaAhoraView?.showUrl(url)
        }

        override fun onError(exception: Throwable) {
            esikaAhoraView?.hideLoading()
            esikaAhoraView?.showError()
        }
    }

    private inner class UpdateObserver : BaseObserver<Boolean>()
}
