package biz.belcorp.consultoras.feature.home.tracking.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.home.tracking.TrackingDetailFragment;
import biz.belcorp.consultoras.feature.home.tracking.TrackingFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, TrackingModule.class})
public interface TrackingComponent extends ActivityComponent {

    void inject(TrackingFragment fragment);

    void inject(TrackingDetailFragment fragment);

}
