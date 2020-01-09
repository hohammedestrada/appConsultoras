package biz.belcorp.consultoras.feature.terms.di


import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.terms.TermsFragment
import biz.belcorp.consultoras.feature.verifyaccount.VerifyAccountFragment
import biz.belcorp.consultoras.feature.vinculacion.VinculacionFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (TermsModule::class)])
interface TermsComponent : ActivityComponent {

    fun inject(termsFragment: TermsFragment)

    fun inject(verifyAccountFragment: VerifyAccountFragment)

    fun inject(vinculacionFragment: VinculacionFragment)

}
