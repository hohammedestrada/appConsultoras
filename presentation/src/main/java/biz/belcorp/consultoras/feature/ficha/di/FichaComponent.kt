package biz.belcorp.consultoras.feature.ficha.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class),
    (FichaModule::class) ])

interface FichaComponent: ActivityComponent, ClientComponent {

    fun inject(baseFichaFragment: BaseFichaFragment)

}
