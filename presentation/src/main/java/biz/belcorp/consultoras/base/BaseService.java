package biz.belcorp.consultoras.base;

import android.app.Service;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.di.component.AppComponent;
import biz.belcorp.consultoras.di.module.ServiceModule;

public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        this.getAppComponent().inject(this);
    }

    /**********************************************************/

    protected AppComponent getAppComponent() {
        return ((ConsultorasApp) getApplication()).getAppComponent();
    }

    protected ServiceModule getServiceModule() {
        return new ServiceModule(this);
    }

    /**********************************************************/

}
