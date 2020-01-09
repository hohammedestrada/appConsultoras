package biz.belcorp.consultoras.feature.embedded.tuvoz.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.embedded.tuvoz.TuVozOnlineWebFragment
import dagger.Component

/**
 *
 */
@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class)])
interface TuVozOnlineWebComponent : ActivityComponent {
    fun inject(fragment: TuVozOnlineWebFragment)
}
