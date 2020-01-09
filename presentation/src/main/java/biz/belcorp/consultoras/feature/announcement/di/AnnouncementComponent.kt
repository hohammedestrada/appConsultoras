package biz.belcorp.consultoras.feature.announcement.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.announcement.AnnouncementFragment
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface AnnouncementComponent : ActivityComponent {
    fun inject(fragment: AnnouncementFragment)
}
