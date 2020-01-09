package biz.belcorp.consultoras.feature.galery.di

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.di.component.ActivityComponent
import biz.belcorp.consultoras.di.component.AppComponent
import biz.belcorp.consultoras.di.module.ActivityModule
import biz.belcorp.consultoras.feature.galery.GalleryDetailFragment
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class), (GaleryModule::class)])
interface GalleryDetailComponent : ActivityComponent {
    fun inject(galleryDetailContainerFragment : GalleryDetailFragment)
}
