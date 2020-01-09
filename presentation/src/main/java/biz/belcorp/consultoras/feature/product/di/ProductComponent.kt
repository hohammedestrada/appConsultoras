package biz.belcorp.consultoras.feature.product.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.product.ProductFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class),
    (ProductModule::class) ])
interface ProductComponent: ActivityComponent, ClientComponent {
    fun inject(productFragment: ProductFragment)
}
