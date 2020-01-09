package biz.belcorp.consultoras.feature.sms.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.sms.PhoneFragment
import biz.belcorp.consultoras.feature.sms.SMSFragment
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class, SMSModule::class])
interface SMSComponent {
    fun inject(smsFragment: SMSFragment)
    fun inject(phoneFragment: PhoneFragment)
}
