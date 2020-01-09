package biz.belcorp.consultoras.feature.scanner.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.scanner.ScannerFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class),
    (ScannerModule::class)])
interface ScannerComponent : ActivityComponent {
    fun inject(scannerFragment: ScannerFragment)
}
