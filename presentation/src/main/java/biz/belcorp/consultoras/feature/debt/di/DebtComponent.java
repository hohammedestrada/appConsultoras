package biz.belcorp.consultoras.feature.debt.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.debt.AddDebtFragment;
import biz.belcorp.consultoras.feature.debt.SendDebtFragment;
import biz.belcorp.consultoras.feature.debt.client.ClientFilterFragment;
import biz.belcorp.consultoras.feature.home.addorders.client.ClientOrderFilterFragment;
import dagger.Component;

/**
 *
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, DebtModule.class})
public interface DebtComponent extends ActivityComponent {

    void inject(AddDebtFragment fragment);

    void inject(SendDebtFragment fragment);

    void inject(ClientFilterFragment fragment);

    void inject(ClientOrderFilterFragment fragment);
}
