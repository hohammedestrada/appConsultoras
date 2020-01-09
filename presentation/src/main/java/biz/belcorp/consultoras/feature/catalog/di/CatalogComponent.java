package biz.belcorp.consultoras.feature.catalog.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.catalog.CatalogContainerFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, CatalogModule.class})
public interface CatalogComponent extends ActivityComponent {

    void inject(CatalogContainerFragment fragment);
}
