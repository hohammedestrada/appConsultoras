package biz.belcorp.consultoras.feature.splash.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.splash.SplashFragment;
import biz.belcorp.consultoras.feature.splash.SplashService;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, SplashModule.class})
public interface SplashComponent extends ActivityComponent {

    void inject(SplashFragment splashFragment);

    void inject(SplashService splashService);

}
