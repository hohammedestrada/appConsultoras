package biz.belcorp.consultoras.feature.galery.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.galery.GalleryContainerFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (GaleryModule::class)])
interface GaleryComponent : ActivityComponent {
    fun inject(galleryContainerFragment : GalleryContainerFragment)
}
