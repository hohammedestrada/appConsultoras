package biz.belcorp.consultoras.feature.home.cupon.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.home.cupon.CuponFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, CuponModule.class})
public interface CuponComponent extends ActivityComponent {

    void inject(CuponFragment fragment);

}
