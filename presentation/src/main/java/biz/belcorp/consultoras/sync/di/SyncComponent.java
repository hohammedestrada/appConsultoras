package biz.belcorp.consultoras.sync.di;

import biz.belcorp.consultoras.di.PerService;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.component.ServiceComponent;
import biz.belcorp.consultoras.di.module.ServiceModule;
import biz.belcorp.consultoras.sync.SyncAdapter;
import dagger.Component;

@PerService
@Component(dependencies = AppComponent.class, modules = { ServiceModule.class, SyncModule.class } )
public interface SyncComponent extends ServiceComponent {

    void inject(SyncAdapter syncAdapter);

}
