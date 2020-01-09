package biz.belcorp.consultoras.feature.contest.news.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.contest.news.NewProgramFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, NewProgramModule.class})
public interface NewProgramComponent extends ActivityComponent {

    void inject(NewProgramFragment newProgramFragment);

}
