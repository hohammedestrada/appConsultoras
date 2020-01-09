package biz.belcorp.consultoras.feature.embedded.academia.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.embedded.academia.AcademiaFragment
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface AcademiaComponent : ActivityComponent {
     fun inject(fragment: AcademiaFragment)
}
