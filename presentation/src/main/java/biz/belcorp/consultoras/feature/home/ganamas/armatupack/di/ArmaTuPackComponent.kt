package biz.belcorp.consultoras.feature.home.ganamas.armatupack.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.home.di.HomeModule
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackErrorFragment
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasErrorFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (ArmaTuPackModule::class)])
interface ArmaTuPackComponent {

    fun inject(armaTuPackFragment: ArmaTuPackFragment)

    fun inject(armaTuPackErrorFragment: ArmaTuPackErrorFragment)

}
