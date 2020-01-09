package biz.belcorp.consultoras.feature.contest.constancy.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.contest.constancy.PerConstancyFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, PerConstancyModule.class})
public interface PerConstancyComponent extends ActivityComponent {

    void inject(PerConstancyFragment contestFragment);

}
