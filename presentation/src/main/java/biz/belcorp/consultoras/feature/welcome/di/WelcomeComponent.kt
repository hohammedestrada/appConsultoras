package biz.belcorp.consultoras.feature.welcome.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.home.profile.di.MyProfileComponent
import biz.belcorp.consultoras.feature.home.profile.password.ChangePasswordFragment
import biz.belcorp.consultoras.feature.sms.di.SMSComponent
import biz.belcorp.consultoras.feature.terms.di.TermsComponent
import biz.belcorp.consultoras.feature.verifyemail.VerifyEmailFragment
import biz.belcorp.consultoras.feature.welcome.WelcomeFragment
import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ActivityModule::class, WelcomeModule::class))
interface WelcomeComponent : ActivityComponent, SMSComponent, MyProfileComponent, TermsComponent {

    fun inject(fragment: WelcomeFragment)
    fun inject(fragment: VerifyEmailFragment)

}
