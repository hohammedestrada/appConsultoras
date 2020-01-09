package biz.belcorp.consultoras.feature.config.di;


import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.config.ConfigFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ConfigModule.class})
public interface ConfigComponent extends ActivityComponent {

    void inject(ConfigFragment configFragment);

}
