package biz.belcorp.consultoras.feature.home.tutorial.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.home.tutorial.TutorialFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, TutorialModule.class})
public interface TutorialComponent extends ActivityComponent {

    void inject(TutorialFragment fragment);
}
