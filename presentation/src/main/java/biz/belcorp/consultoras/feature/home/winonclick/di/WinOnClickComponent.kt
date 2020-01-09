package biz.belcorp.consultoras.feature.home.winonclick.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.home.winonclick.WinOnClickFragment
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface WinOnClickComponent : ActivityComponent {
    fun inject(fragment: WinOnClickFragment)
}
