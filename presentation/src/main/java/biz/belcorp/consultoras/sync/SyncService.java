package biz.belcorp.consultoras.sync;

import android.content.Intent;
import android.os.IBinder;

import biz.belcorp.consultoras.base.BaseService;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.sync.di.DaggerSyncComponent;
import biz.belcorp.consultoras.sync.di.SyncComponent;
import biz.belcorp.library.log.BelcorpLogger;

public class SyncService extends BaseService implements HasComponent<SyncComponent> {

    private static final String TAG = "SyncService";
    private static final Object sAdapterLock = new Object();
    private static SyncAdapter sAdapter = null;

    private SyncComponent component;

    public SyncService() {
        super();
    }

    @Override
    public void onCreate() {
        initializeInjector();
        BelcorpLogger.d(TAG, "onCreate()");
        synchronized (sAdapterLock) {
            if (sAdapter == null) {
                sAdapter = new SyncAdapter(getApplicationContext(), getComponent());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sAdapter.getSyncAdapterBinder();
    }

    protected void initializeInjector() {
        this.component = DaggerSyncComponent.builder()
            .appComponent(getAppComponent())
            .serviceModule(getServiceModule())
            .build();
    }

    @Override
    public SyncComponent getComponent() {
        return component;
    }
}
