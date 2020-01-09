package biz.belcorp.consultoras.feature.dreammeter.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.dreammeter.feature.detail.DetailFragment
import biz.belcorp.consultoras.feature.dreammeter.feature.save.SaveFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (DreamMeterModule::class)])
interface DreamMeterComponent : ActivityComponent {

    fun inject(saveFragment: SaveFragment)
    fun inject(detailFragment: DetailFragment)

}
