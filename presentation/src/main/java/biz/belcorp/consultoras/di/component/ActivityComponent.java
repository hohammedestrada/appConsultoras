package biz.belcorp.consultoras.di.component;

import android.app.Activity;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.module.ActivityModule;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
