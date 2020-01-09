package biz.belcorp.consultoras.feature.caminobrillante.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado.LogroUnificadoFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.demostrador.DemostradorFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit.KitFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.OfertasEspecialesFragment
import biz.belcorp.consultoras.feature.caminobrillante.feature.home.CaminoBrillanteFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (CaminoBrillanteModule::class)])
interface CaminoBrillanteComponent : ActivityComponent {

    fun inject(caminobrillanteFragment: CaminoBrillanteFragment)
    fun inject(ofertasEspecialesFragment: OfertasEspecialesFragment)
    fun inject(kitFragment: KitFragment)
    fun inject(demostradorFragment: DemostradorFragment)
    fun inject(logroUnificadoFragment: LogroUnificadoFragment)

}
