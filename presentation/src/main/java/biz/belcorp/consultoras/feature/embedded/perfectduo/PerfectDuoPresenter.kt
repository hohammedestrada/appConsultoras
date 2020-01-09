package biz.belcorp.consultoras.feature.embedded.perfectduo

import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.security.JwtEncryption
import org.json.JSONObject
import javax.inject.Inject

class PerfectDuoPresenter @Inject
internal constructor(private val userUseCase: UserUseCase, private val loginModelDataMapper: LoginModelDataMapper) : Presenter<PerfectDuoView> {


    private var perfectView: PerfectDuoView? = null

    override fun attachView(view: PerfectDuoView) {
        perfectView = view
    }

    fun generateUrlBanner() {
        userUseCase[UserUrlBannerDuo()]
    }

    fun trackBack() {
        userUseCase[UserBackPressedObserver()]
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            perfectView?.trackBack(loginModelDataMapper.transform(t))
        }
    }

    private inner class UserUrlBannerDuo : BaseObserver<User?>() {

        override fun onNext(user: User?) {
            var url = ""
            //perfectView!!.showLoading()
            if (null != user) {
                if(user.isMostrarBuscador==true)
                    perfectView!!.showButtonToolbar()
                var token = ""
                val countryISO = user.countryISO
                val consultantId = user.userCode

                if ("" != countryISO && "" != consultantId) {
                    val jsonPayLoad = JSONObject()

                    try {
                        jsonPayLoad.put("Pais", countryISO)
                        jsonPayLoad.put("Pagina", GlobalConstant.DUOPERFECTO)
                        jsonPayLoad.put("CodigoUsuario", consultantId)
                        jsonPayLoad.put("Campania", user.campaing)
                        jsonPayLoad.put("LimpiarSession",true)
                        jsonPayLoad.put("EsAppMobile", "True");
                        val payload = jsonPayLoad.toString()
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload)


                    } catch (e: Exception) {
                        BelcorpLogger.w("GetUser.onNext", e)
                    }
                }
                if ("" != token) {
                    url = BuildConfig.PASE_OFERTA + token
                    perfectView!!.showUrl(url)
                }
            }
            //perfectView!!.hideLoading()
        }
    }

    override fun resume() {
    //nada
    }

    override fun pause() {
        //nada
    }

    override fun destroy() {
        //nada
    }
}
