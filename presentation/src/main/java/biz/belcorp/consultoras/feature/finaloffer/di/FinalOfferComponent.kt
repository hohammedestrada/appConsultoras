package biz.belcorp.consultoras.feature.finaloffer.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.finaloffer.FinalOfferFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class),
    (FinalOfferModule::class)])
interface FinalOfferComponent: ActivityComponent {

    fun inject(finalOfferFragment: FinalOfferFragment)

}

