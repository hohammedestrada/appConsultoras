package biz.belcorp.consultoras.feature.embedded.product.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.embedded.product.ProductWebFragment
import dagger.Component

/**
 *
 */
@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class)])
interface ProductWebComponent : ActivityComponent {
    fun inject(fragment: ProductWebFragment)
}
