package biz.belcorp.consultoras.feature.home.storie

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.domain.entity.ContenidoRequest
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.StorieUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import javax.inject.Inject

class StoriePresenter
@Inject
internal constructor(private val userUseCase: UserUseCase, private val storieUseCase: StorieUseCase): Presenter<StorieView> {

    var vista: StorieView? = null
    override fun attachView(view: StorieView) {
        vista = view
    }

    override fun resume() {
    }

    override fun pause() {

    }

    override fun destroy() {
        userUseCase.dispose()
        storieUseCase.dispose()
    }

    fun saveStateStorie(idContenidoDetalle: Int){
        userUseCase[UserObserver(idContenidoDetalle)]
    }

    private inner class UserObserver(var idContDetalle: Int) : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            if(t!=null){

                storieUseCase.saveStateContenido(ContenidoRequest().apply {
                    campaniaID = t.campaing?.toInt()
                    codigoRegion = t.regionCode
                    codigoZona = t.zoneCode
                    codigoSeccion = t.codigoSeccion
                    indicadorConsultoraDigital = t.indicadorConsultoraDigital.toString()
                    numeroDocumento = t.numeroDocumento
                    idContenidoDetalle = idContDetalle

                }, object : BaseObserver<String?> (){})
            }

        }
    }

}
