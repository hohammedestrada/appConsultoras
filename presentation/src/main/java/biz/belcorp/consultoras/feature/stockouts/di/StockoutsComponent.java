package biz.belcorp.consultoras.feature.stockouts.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.stockouts.StockoutsFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, StockoutsModule.class})
public interface StockoutsComponent extends ActivityComponent {

    void inject(StockoutsFragment fragment);

}
