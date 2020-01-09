package biz.belcorp.consultoras.feature.orders.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.orderdetail.OrderDetailFragment

import biz.belcorp.consultoras.feature.orders.OrdersGroup
import biz.belcorp.consultoras.feature.orders.OrdersList
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (OrderDetailModule::class)])
interface OrderDetailComponent : ActivityComponent {

    fun inject(orderDetailFragment: OrderDetailFragment)
}
