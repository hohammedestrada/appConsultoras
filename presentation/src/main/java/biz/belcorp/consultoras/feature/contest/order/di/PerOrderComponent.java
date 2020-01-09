package biz.belcorp.consultoras.feature.contest.order.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.contest.order.current.PerCurrentOrderFragment;
import biz.belcorp.consultoras.feature.contest.order.previous.PerPreviousOrderFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, PerOrderModule.class})
public interface PerOrderComponent extends ActivityComponent {

    void inject(PerCurrentOrderFragment perCurrentOrderFragment);

    void inject(PerPreviousOrderFragment perOrderFragment);
}
