package biz.belcorp.consultoras.feature.embedded.tuvoz

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.ContenidoResumen
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.ResumenRequest
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant

import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil
import javax.inject.Inject

@PerActivity
class TuVozOnlineWebPresenter @Inject
internal constructor(
    private val accountUseCase: AccountUseCase,
    private val userUseCase: UserUseCase,
    private val loginModelDataMapper: LoginModelDataMapper
) : Presenter<TuVozOnlineWebView>, SafeLet {

    private var view: TuVozOnlineWebView? = null

    override fun attachView(view: TuVozOnlineWebView) {
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
        this.view = null
    }

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun trackBack() {
        userUseCase[UserBackPressedObserver()]
    }

    fun getUrlTyC() {
        accountUseCase.getPdfTermsUrl(GetUrlTyCObserver())
    }

    fun checkEmailIsEmpty(){
        accountUseCase.getUpdatedData(LoginObserver())
    }

    fun updateEmail(newEmail: String){
        view?.showLoading()
        accountUseCase.enviarCorreo(newEmail, EnviarCorreoObserver())
    }

    fun getUrl(){
        accountUseCase.getUpdatedData(GetLoginUpdate())
    }

    private inner class EnviarCorreoObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {

            if(t?.code.equals(GlobalConstant.CODE_OK)){
                view?.onUpdateEmail()
            } else {
                view?.hideLoading()
                t?.message?.let {
                    view?.showError(it)
                    BelcorpLogger.e(it)
                }
            }

        }

        override fun onError(exception: Throwable) {
            view?.hideLoading()
            exception.message?.let {
                view?.showError()
                BelcorpLogger.e(it)
            }
        }

    }

    private inner class GetLoginUpdate : BaseObserver<Login?>() {
        override fun onNext(t: Login?) {
            super.onNext(t)
            t?.let {
                val documento = if (it.numeroDocumento.isNullOrEmpty()) "0" else it.numeroDocumento
                val correo = if(it.email.isNullOrEmpty()) it.correoPendiente else it.email

                val request = ResumenRequest()

                request.campaing = Integer.parseInt(it.campaing?: StringUtil.Empty)
                request.codeRegion = it.regionCode ?:  StringUtil.Empty
                request.codeSection = it.codigoSeccion ?:  StringUtil.Empty
                request.codeZone = it.zoneCode ?:  StringUtil.Empty
                request.idContenidoDetalle = 0
                request.indConsulDig = it.indicadorConsultoraDigital.toString()
                request.numeroDocumento = documento
                request.primerNombre = it.primerNombre ?:  StringUtil.Empty
                request.primerApellido = it.primerApellido ?:  StringUtil.Empty
                request.fechaNacimiento = it.fechaNacimiento ?:  StringUtil.Empty
                request.correo = correo
                request.esLider = it.esLider
                accountUseCase.getResumenConfig(request, GetResumen())
            }
        }
    }

    private inner class GetUrlTyCObserver : BaseObserver<String>() {

        override fun onNext(t: String) {
            view?.setUrlTyc(t)
        }
    }

    private inner class LoginObserver : BaseObserver<Login?>() {

        override fun onNext(t: Login?) {

            t?.let {
                if (it.email.isNullOrBlank() && it.correoPendiente.isNullOrBlank()){
                    view?.hideLoading()
                    view?.showEnterDni()
                } else {
                    getUrl()
                }
            }
        }
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                view?.initScreenTrack(loginModelDataMapper.transform(it))
            }
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.trackBack(loginModelDataMapper.transform(t))
        }
    }

    private inner class GetResumen : BaseObserver<BasicDto<Collection<ContenidoResumen>>>() {
        override fun onNext(t: BasicDto<Collection<ContenidoResumen>>) {
            super.onNext(t)

            var urlLoad = GlobalConstant.URL_TUVOZ_ONLINE

            t.data?.forEach {
                if (it.codigoResumen == GlobalConstant.QUESTION_PRO_URL){
                    it.contenidoDetalle?.forEach { it2 ->
                        if (it2.codigoDetalleResumen == GlobalConstant.QUESTION_PRO_URL) {
                            it2.urlDetalleResumen?.let {url ->
                                urlLoad = url
                                return@forEach
                            }
                        }
                    }
                }
            }
            view?.showUrl(urlLoad)
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            view?.showUrl(GlobalConstant.URL_TUVOZ_ONLINE)
        }
    }
}
