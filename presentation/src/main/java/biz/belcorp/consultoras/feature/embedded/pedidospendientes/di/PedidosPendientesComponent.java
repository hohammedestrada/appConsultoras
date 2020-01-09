package biz.belcorp.consultoras.feature.embedded.pedidospendientes.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.embedded.pedidospendientes.PedidosPendientesFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface PedidosPendientesComponent extends ActivityComponent {

    void inject(PedidosPendientesFragment fragment);

}
