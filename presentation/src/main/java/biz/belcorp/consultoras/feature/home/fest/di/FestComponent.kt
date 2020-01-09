package biz.belcorp.consultoras.feature.home.fest.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.home.fest.FestErrorFragment
import biz.belcorp.consultoras.feature.home.fest.FestFragment
import biz.belcorp.consultoras.feature.home.subcampaing.FestSubCampaingFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (FestModule::class)])
interface FestComponent {

    fun inject(festFragment: FestFragment)

    fun inject(festErrorFragment: FestErrorFragment)

    fun inject(festSubCampaingFragment: FestSubCampaingFragment)

}
