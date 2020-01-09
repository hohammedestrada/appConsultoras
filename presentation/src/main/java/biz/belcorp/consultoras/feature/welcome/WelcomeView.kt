package biz.belcorp.consultoras.feature.welcome


import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Verificacion

/**
 * @author andres.escobar on 4/08/2017.
 */
interface WelcomeView : View, LoadingView {

    fun initScreenTrack(transform: LoginModel)
    fun onError()
    fun onError(message: String)
    fun onSMSResponse(t: BasicDto<Boolean>?)
    fun onSMSError(exception: Throwable)
    fun onVerificacionResponse(verificacion: Verificacion)
}
