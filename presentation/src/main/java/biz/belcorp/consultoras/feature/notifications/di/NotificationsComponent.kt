package biz.belcorp.consultoras.feature.notifications.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.notifications.list.NotificationListFragment
import biz.belcorp.consultoras.feature.notifications.redirect.NotificationsFragment
import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ActivityModule::class, NotificationsModule::class))
interface NotificationsComponent : ActivityComponent {

    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: NotificationListFragment)

}
