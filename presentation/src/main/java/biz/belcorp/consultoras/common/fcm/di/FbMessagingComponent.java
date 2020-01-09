package biz.belcorp.consultoras.common.fcm.di;

import biz.belcorp.consultoras.common.fcm.FBMessagingService;
import biz.belcorp.consultoras.di.PerService;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.component.ServiceComponent;
import biz.belcorp.consultoras.di.module.ServiceModule;
import biz.belcorp.consultoras.sync.SyncAdapter;
import dagger.Component;

@PerService
@Component(dependencies = AppComponent.class, modules = { ServiceModule.class, FbMessagingModule.class } )
public interface FbMessagingComponent extends ServiceComponent {

    void inject(FBMessagingService fbMessagingService);

}
