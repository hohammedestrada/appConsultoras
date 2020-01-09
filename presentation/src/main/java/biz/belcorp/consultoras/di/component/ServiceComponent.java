package biz.belcorp.consultoras.di.component;

import android.app.Service;

import biz.belcorp.consultoras.di.PerService;
import biz.belcorp.consultoras.di.module.ServiceModule;
import dagger.Component;

@PerService
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    Service service();
}
