package biz.belcorp.consultoras.feature.embedded.success.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.embedded.success.WalkSuccessFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class)])
interface WalkSuccessComponent: ActivityComponent {
    fun inject(walkFragmentSuccess: WalkSuccessFragment)
}
