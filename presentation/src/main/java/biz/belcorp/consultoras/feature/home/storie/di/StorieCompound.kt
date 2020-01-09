package biz.belcorp.consultoras.feature.home.storie.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.home.storie.StorieFragmentContent
import biz.belcorp.consultoras.feature.home.storie.StorieRootFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (StorieModule::class)])
interface StorieCompound: ActivityComponent {

    fun inject(storieFragment: StorieRootFragment)
    fun inject (storieFragmentContent: StorieFragmentContent)
}
