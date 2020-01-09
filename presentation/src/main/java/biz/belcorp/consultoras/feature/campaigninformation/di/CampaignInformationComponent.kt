package biz.belcorp.consultoras.feature.campaigninformation.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.campaigninformation.CampaignInformationFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (CampaignInformationModule::class)])
interface CampaignInformationComponent : ActivityComponent {
    fun inject(campaignInformationFragment : CampaignInformationFragment)
}
