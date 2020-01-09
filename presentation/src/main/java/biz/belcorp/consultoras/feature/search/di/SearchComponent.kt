package biz.belcorp.consultoras.feature.search.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.client.di.ClientModule
import biz.belcorp.consultoras.feature.home.addorders.client.ClientOrderFilterFragment
import biz.belcorp.consultoras.feature.product.ProductFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (SearchModule::class) ])
interface SearchComponent: ActivityComponent, ClientComponent {
    fun inject(productFragment: ProductFragment)
}
