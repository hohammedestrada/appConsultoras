package biz.belcorp.consultoras.feature.auth.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.auth.login.facebook.LoginFacebookFragment;
import biz.belcorp.consultoras.feature.auth.login.form.LoginFormFragment;
import biz.belcorp.consultoras.feature.auth.login.LoginFragment;
import biz.belcorp.consultoras.feature.auth.recovery.RecoveryFragment;
import biz.belcorp.consultoras.feature.auth.registration.RegistrationFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, AuthModule.class})
public interface AuthComponent extends ActivityComponent {

    void inject(LoginFragment loginFragment);
    void inject(LoginFormFragment loginFormFragment);
    void inject(LoginFacebookFragment loginFacebookFragment);

    void inject(RegistrationFragment registrationFragment);
    void inject(RecoveryFragment recoveryFragment);
}
