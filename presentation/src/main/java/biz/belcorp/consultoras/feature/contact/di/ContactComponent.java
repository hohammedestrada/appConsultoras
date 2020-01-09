package biz.belcorp.consultoras.feature.contact.di;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.di.component.ActivityComponent;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.contact.ContactListFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ContactModule.class})
public interface ContactComponent extends ActivityComponent {

    void inject(ContactListFragment contactListFragment);

}
