package biz.belcorp.consultoras.feature.changeemail.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.changeemail.ChangeEmailFragment
import biz.belcorp.consultoras.feature.verifyemail.VerifyEmailFragment
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ChangeEmailComponent : ActivityComponent {

    fun inject(fragment: ChangeEmailFragment)
    fun inject(fragment: VerifyEmailFragment)
}
