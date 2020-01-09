package biz.belcorp.consultoras.feature.makeup.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.makeup.SummaryFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (SummaryModule::class) ])
interface SummaryComponent: ActivityComponent {
    fun inject(summaryFragment: SummaryFragment)
}
