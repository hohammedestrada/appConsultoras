package biz.belcorp.consultoras.feature.home.addorders.choosegift.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.home.addorders.choosegift.GiftFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (GiftModule::class)])
interface GiftComponent : ActivityComponent {

    fun inject(giftFragment: GiftFragment)

}
