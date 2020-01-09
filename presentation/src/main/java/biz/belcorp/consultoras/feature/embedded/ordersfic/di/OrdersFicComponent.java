package biz.belcorp.consultoras.feature.embedded.ordersfic.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicFragment;
import dagger.Component;

/**
 *
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface OrdersFicComponent extends ActivityComponent {
    void inject(OrdersFicFragment fragment);
}
