package biz.belcorp.consultoras.feature.contest.rewards.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.contest.rewards.RewardsFragment
import biz.belcorp.consultoras.feature.datami.DatamiMessageFragment
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface RewardsComponent : ActivityComponent {

    fun inject(fragment: RewardsFragment)

}
