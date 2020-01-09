package biz.belcorp.consultoras.feature.embedded.product

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase
import biz.belcorp.consultoras.domain.interactor.MenuUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.consultoras.util.anotation.UserType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.security.JwtEncryption
import org.json.JSONObject
import javax.inject.Inject

@PerActivity
class ProductWebPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val menuUseCase: MenuUseCase,
    private val caminoBrillanteUseCase: CaminoBrillanteUseCase,
    private val loginModelDataMapper: LoginModelDataMapper
    ) : Presenter<ProductWebView>, SafeLet {

    private var view: ProductWebView? = null

    override fun attachView(view: ProductWebView) {
        this.view = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.menuUseCase.dispose()
        this.caminoBrillanteUseCase.dispose()
        this.view = null
    }

    /** functions */

    fun loadURL(deviceId: String, cuv: String, palanca: String, origin: String) {
        view?.showLoading()
        this.caminoBrillanteUseCase.getNivelConsultoraAsObserver(GetNivelConsultoraObserver(deviceId, cuv, palanca, origin))
    }

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun trackBack() {
        userUseCase[UserBackPressedObserver()]
    }

    fun getMenuActive(code1: String, code2: String) {
        menuUseCase.getActive(code1, code2, GetMenuObserver())
    }

    /** Observers */

    private inner class GetMenuObserver : BaseObserver<Menu?>() {
        override fun onNext(t: Menu?) {
            t?.let { view?.onGetMenu(it) }
        }
    }

    private inner class GetUser(var deviceId: String?, var cuv:String, var palanca: String,
                                var origin: String, var nivel: Int)
        : BaseObserver<User?>() {

        override fun onNext(t: User?) {

            var url = ""

            t.let {
                var token = ""
                val countryISO = it?.countryISO
                val consultantId = it?.userCode
                val campaing = it?.campaing

                if ("" != countryISO && "" != consultantId) {
                    if (null == deviceId || deviceId!!.isEmpty())
                        deviceId = consultantId

                    val jsonPayLoad = JSONObject()

                    try {
                        jsonPayLoad.put("Pais", countryISO)
                        jsonPayLoad.put("Pagina", PageUrlType.DETALLE_ESTRATEGIA)
                        jsonPayLoad.put("CodigoUsuario", consultantId)
                        jsonPayLoad.put("EsAppMobile", "True")
                        jsonPayLoad.put("ClienteId", "0")
                        jsonPayLoad.put("Identifier", deviceId)
                        jsonPayLoad.put("CUV", cuv)
                        jsonPayLoad.put("PalancaID", palanca)
                        jsonPayLoad.put("Campania", campaing)
                        jsonPayLoad.put("OrigenPedido", origin)
                        jsonPayLoad.put("NivelCaminoBrillante", nivel)

                        val payload = jsonPayLoad.toString()
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload)

                    } catch (e: Exception) {
                        BelcorpLogger.w("GetUser.onNext", e)
                    }

                }

                if ("" != token)
                    url = GlobalConstant.URL_PASE_PEDIDO + token

                if(it?.userType == UserType.POSTULANTE)
                    view?.showPostulantDialog()

                if(it?.isMostrarBuscador == true){
                    view?.showSearchOption()
                }

            }

            view?.showUrl(url)
        }

        override fun onError(exception: Throwable) {
            BelcorpLogger.w(ProductWebPresenter.TAG, "ERROR!", exception)
            view?.hideLoading()
            view?.showError()
        }
    }

    private inner class UpdateObserver : BaseObserver<Boolean>()

    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                view?.initScreenTrack(loginModelDataMapper.transform(t))
            }
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.trackBack(loginModelDataMapper.transform(t))
        }
    }

    private inner class GetNivelConsultoraObserver(internal var deviceId: String,internal var cuv: String,internal var palanca: String, internal var origin: String) : BaseObserver<Int>() {

        override fun onNext(nivel: Int) {
            userUseCase[GetUser(deviceId,cuv,palanca,origin, nivel)]
            userUseCase.updateScheduler(UpdateObserver())
        }
    }

    /** ProductWebPresenter */

    companion object {
        private const val TAG = "ProductWebPresenter"
    }

}
