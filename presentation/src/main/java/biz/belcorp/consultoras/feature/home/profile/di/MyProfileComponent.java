package biz.belcorp.consultoras.feature.home.profile.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.home.profile.MyProfileFragment;
import biz.belcorp.consultoras.feature.home.profile.password.ChangePasswordFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, MyProfileModule.class})
public interface MyProfileComponent extends ActivityComponent {

    void inject(MyProfileFragment fragment);

    void inject(ChangePasswordFragment fragment);

}
