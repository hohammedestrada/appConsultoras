package biz.belcorp.consultoras.feature.payment.online.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.payment.online.confirmacion.ConfirmacionFragment
import biz.belcorp.consultoras.feature.payment.online.constancia.ConstanciaFragment
import biz.belcorp.consultoras.feature.payment.online.metodopago.MethodPaymentFragment
import biz.belcorp.consultoras.feature.payment.online.error.ErrorFragment
import biz.belcorp.consultoras.feature.payment.online.resultado.ResultadoFragment
import biz.belcorp.consultoras.feature.payment.online.tipopago.TipoPagoFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (PaymentOnlineModule::class)])
interface PaymentOnlineComponent : ActivityComponent {

    fun inject(methodPaymentFragment: MethodPaymentFragment)
    fun inject(tipoPagoFragment: TipoPagoFragment)
    fun inject(confirmacionFragment: ConfirmacionFragment)
    fun inject(resultadoFragment: ResultadoFragment)
    fun inject(errorFragment: ErrorFragment)
    fun inject(constanciaFragment: ConstanciaFragment)

}
