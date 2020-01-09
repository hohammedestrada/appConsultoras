package biz.belcorp.consultoras.feature.ofertafinal.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.ficha.ofertafinal.FichaOfertaFinalFragment
import biz.belcorp.consultoras.feature.ofertafinal.OfertaFinalErrorFragment
import biz.belcorp.consultoras.feature.ofertafinal.OfertaFinalLandingFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (OfertaFinalModule::class)])
interface OfertaFinalComponent {

    fun inject(ofertaFinalFragment: OfertaFinalLandingFragment)
    fun inject(ofertaFinalErrorFragment: OfertaFinalErrorFragment)
    fun inject(fichaOfertaFinalFragment: FichaOfertaFinalFragment)

}
