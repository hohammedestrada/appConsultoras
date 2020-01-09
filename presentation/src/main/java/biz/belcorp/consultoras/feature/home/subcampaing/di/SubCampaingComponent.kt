package biz.belcorp.consultoras.feature.home.subcampaing.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.home.fest.FestErrorFragment
import biz.belcorp.consultoras.feature.home.fest.FestFragment
import biz.belcorp.consultoras.feature.home.fest.di.FestModule
import biz.belcorp.consultoras.feature.home.subcampaing.FestSubCampaingFragment
import biz.belcorp.consultoras.feature.home.subcampaing.SubCampaingErrorFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (FestModule::class)])
interface SubCampaingComponent {

    fun inject(subCampaingErrorFragment: SubCampaingErrorFragment)
    fun inject(festSubCampaingFragment: FestSubCampaingFragment)
}
